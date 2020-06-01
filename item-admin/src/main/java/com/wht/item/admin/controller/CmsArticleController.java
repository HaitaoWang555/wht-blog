package com.wht.item.admin.controller;

import com.google.common.base.Joiner;
import com.wht.item.admin.dto.CmsArticleParam;
import com.wht.item.admin.service.CmsArticleService;
import com.wht.item.admin.service.CmsMetasService;
import com.wht.item.admin.service.UmsAdminService;
import com.wht.item.common.api.CommonPage;
import com.wht.item.common.api.CommonResult;
import com.wht.item.model.CmsArticle;
import com.wht.item.model.UmsAdmin;
import fr.opensagres.poi.xwpf.converter.core.ImageManager;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLConverter;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLOptions;
import io.micrometer.core.instrument.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.fit.cssbox.css.CSSNorm;
import org.fit.cssbox.css.DOMAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 内容管理 博客文章
 *
 * @author wht
 * @since 2020-05-31 2:19
 */

@Controller
@Api(tags = "CmsArticleController", description = "内容管理 博客文章")
@RequestMapping("/article")
public class CmsArticleController {
    @Resource
    private CmsArticleService articleService;
    @Resource
    private UmsAdminService adminService;
    @Resource
    private CmsMetasService metasService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsArticleController.class);

    @ApiOperation("添加")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody CmsArticleParam cmsArticleParam, Principal principal) {
        CmsArticle cmsArticle = new CmsArticle();
        BeanUtils.copyProperties(cmsArticleParam, cmsArticle);
        String username = principal.getName();
        UmsAdmin umsAdmin = adminService.getAdminByUsername(username);
        cmsArticle.setAuthorId(umsAdmin.getId());
        int count = articleService.create(cmsArticle);
        if (count > 0) {
            //存储分类和标签
            Long articleId = cmsArticle.getId();
            String tags = cmsArticle.getTags();
            String category = cmsArticle.getCategory();
            if (StringUtils.isNotBlank(tags)) metasService.saveOrRemoveMeta(tags, "tag", articleId);
            if (StringUtils.isNotBlank(category)) metasService.saveOrRemoveMeta(category, "category", articleId);
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("分页模糊查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<CmsArticle>> list(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "authorId", required = false) Long authorId,
            @RequestParam(value = "meta", required = false) String meta,
            @RequestParam(value = "sortBy", required = false, defaultValue = "updated_time desc") String sortBy,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<CmsArticle> resourceList = articleService.list(title, status, meta, authorId, sortBy, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(resourceList));
    }

    @ApiOperation("根据ID获取")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CmsArticle> getItem(@PathVariable Long id) {
        CmsArticle cmsMeta = articleService.getItem(id);
        return CommonResult.success(cmsMeta);
    }

    @ApiOperation("批量删除")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = articleService.delete(ids);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("图片上传")
    @RequestMapping(value = "/upload/img", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult uploadImg(@RequestParam("file") MultipartFile file, Principal principal) {
        if (file.isEmpty()) {
            return CommonResult.failed("文件为空");
        }
        String username = principal.getName();
        UmsAdmin umsAdmin = adminService.getAdminByUsername(username);
        String realPath =  createFilePath(umsAdmin.getId().toString());
        String thumbnail = "thumbnail/";
        File dir = new File(realPath);
        if(!dir.isDirectory()){
            dir.mkdirs();
        }
        String fileName = file.getOriginalFilename();
        String localFilePath = dir.getAbsolutePath() + File.separator + fileName;
        String thumbnailPath = dir.getAbsolutePath() + File.separator + thumbnail;
        File thumbnailDir =new File(thumbnailPath);
        if(!thumbnailDir.isDirectory()){
            thumbnailDir.mkdirs();
        }

        String thumbnailFilePath =thumbnailPath + fileName;
        try {
            file.transferTo(new File(localFilePath));
            imgThumbnail(localFilePath, thumbnailFilePath);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return CommonResult.failed("文件上传失败");
        }
        String url = Objects.equals(file.getContentType(), "image/jpeg")
                ? "/" + realPath.concat(thumbnail) + fileName
                : "/" + realPath.concat(thumbnail) + fileName + ".jpg";
        LOGGER.info("【文件上传至本地】绝对路径：{}", localFilePath);
        return CommonResult.success(url, "上传成功");
    }

    @ApiOperation("文章上传")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult uploadArticle(@RequestParam("file") MultipartFile file, Principal principal) throws IOException, TransformerException, ParserConfigurationException {
        String fileName = file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();
        assert fileName != null;
        String username = principal.getName();
        UmsAdmin umsAdmin = adminService.getAdminByUsername(username);
        String suffix = fileName.substring(fileName.indexOf(".") + 1);
        String content = getArticleContent(suffix, inputStream, umsAdmin.getId().toString());
        if (content.equals("")) {
            return CommonResult.failed("格式不正确");
        }
        return CommonResult.success(content,"导入成功");
    }

    @ApiOperation("文章上传文件夹")
    @RequestMapping(value = "/uploadDir", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult uploadDir(@RequestParam(required =false, value = "file") MultipartFile[] multipartFiles, Principal principal) throws IOException, TransformerException, ParserConfigurationException {
        for (MultipartFile file : multipartFiles) {
            String fileName = file.getOriginalFilename();
            InputStream inputStream = file.getInputStream();
            String suffix = fileName.substring(fileName.indexOf(".") + 1);
            String username = principal.getName();
            UmsAdmin umsAdmin = adminService.getAdminByUsername(username);
            String content = getArticleContent(suffix, inputStream, umsAdmin.getId().toString());
            if (content.equals("")) break;
            String type;
            if (suffix.equals("md")) {
                type = "markdownEditor";
            } else {
                type = "tinymceEditor";
            }
            try {
                CmsArticle cmsArticle = new CmsArticle();
                cmsArticle.setTitle(fileName);
                cmsArticle.setContent(content);
                cmsArticle.setEditorType(type);
                cmsArticle.setStatus("draft");
                articleService.create(cmsArticle);
            } catch (Exception e) {
                LOGGER.error(" ERROR: {}",  e.getMessage());
            }
        }
        return CommonResult.success("导入成功");
    }


    /**
     * 创建上传文件路径
     * @return upload - 模块 - 用户ID - 时间 - 文件名
     */
    private String createFilePath(String userId) {
        String fileTempPath = "upload/";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String format = sdf.format(new Date());
        return fileTempPath + "article" + "/" + userId + "/" + format + "/";
    }

    /**
     * 压缩图片
     * @param localFilePath 原文件地址
     * @param thumbnailFilePath 压缩文件地址
     */
    private void imgThumbnail(String localFilePath, String thumbnailFilePath) throws IOException {
        Thumbnails.of(localFilePath)
                .scale(1)
                .outputQuality(0.5)
                .outputFormat("jpg")
                .toFile(thumbnailFilePath);
    }
    private void imgThumbnail(String path) throws IOException {
        File file = new File(path);
        String [] fileName = file.list();
        if (fileName == null) return;
        if (fileName.length == 0) return;
        String thumbnailPath = path.concat("thumbnail/");
        File thumbnailDir =  new File(thumbnailPath);
        if(!thumbnailDir.isDirectory()){
            thumbnailDir.mkdirs();
        }
        List<String> needThumbnailFileName = new ArrayList<>();
        for (String f : fileName) {
            String imagePath = path.concat(f);
            needThumbnailFileName.add(imagePath);
        }
        String [] needThumbnailFileNames = new String[needThumbnailFileName.size()];
        toThumbnail(thumbnailDir, needThumbnailFileName.toArray(needThumbnailFileNames));
    }
    private void toThumbnail(File filePath, String... files) throws IOException {
        Thumbnails.of(files)
                .scale(1)
                .outputFormat("jpg")
                .outputQuality(0.5)
                .toFiles(filePath, Rename.NO_CHANGE);
    }

    /**
     * 根据后缀 处理上传文件
     * @param suffix suffix
     * @param inputStream 上传文件
     */
    private String getArticleContent(String suffix, InputStream inputStream, String userId) throws ParserConfigurationException, TransformerException, IOException {
        String content = "";
        switch (suffix.toLowerCase()) {
            case "md":
                content = handleMd(inputStream);
                break;
            case "doc":
                content = handleDoc(inputStream, userId);
                break;
            case "docx":
                content = handleDocx(inputStream, userId);
                break;
            default:
        }
        return content;
    }

    /**
     * 处理 .md 返回 字符
     * @param inputStream 上传文件
     */
    private String handleMd(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        List<String> list = reader.lines().collect(Collectors.toList());
        return Joiner.on("\n").join(list);
    }

    /**
     * 处理 .doc 返回 html 字符
     * @param inputStream 上传文件
     * @param userId 当前用户ID
     */
    private String handleDoc(InputStream inputStream, String userId) throws IOException, ParserConfigurationException, TransformerException {
        String imagePathStr = createFilePath(userId);

        HWPFDocument wordDocument = new HWPFDocument(inputStream);
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        wordToHtmlConverter.setPicturesManager((a, b, suggestedName, d, e) -> imagePathStr + File.separator + suggestedName);
        wordToHtmlConverter.processDocument(wordDocument);
        List<Picture> pics = wordDocument.getPicturesTable().getAllPictures();

        for (Picture pic : pics) {
            File dir = new File(imagePathStr);
            if(!dir.isDirectory()) dir.mkdirs();
            pic.writeImageContent(new FileOutputStream(imagePathStr + pic.suggestFullFileName()));
        }

        Document htmlDocument = wordToHtmlConverter.getDocument();

        DOMAnalyzer da = new DOMAnalyzer(htmlDocument);
        da.attributesToStyles();
        da.addStyleSheet(null, CSSNorm.stdStyleSheet(), DOMAnalyzer.Origin.AGENT);
        da.addStyleSheet(null, CSSNorm.userStyleSheet(), DOMAnalyzer.Origin.AGENT);
        da.getStyleSheets(); //load the author style sheets
        da.stylesToDomInherited();

        DOMSource domSource = new DOMSource(htmlDocument);
        StringWriter stringWriter = new StringWriter();
        Transformer transformer = TransformerFactory.newInstance()
                .newTransformer();
        transformer.setOutputProperty( OutputKeys.INDENT, "yes" );
        transformer.setOutputProperty( OutputKeys.ENCODING, "utf-8" );
        transformer.setOutputProperty( OutputKeys.METHOD, "html" );
        transformer.transform(
                domSource,
                new StreamResult( stringWriter ) );

        // 压缩图片
        imgThumbnail(imagePathStr);

        return stringWriter.toString();
    }
    /**
     * 处理 .docx 返回 html 字符
     * @param inputStream 上传文件
     * @param userId 当前用户ID
     */
    private String handleDocx(InputStream inputStream, String userId) throws IOException {

        String imagePathStr = createFilePath(userId);

        XWPFDocument document = new XWPFDocument(inputStream);
        XHTMLOptions options = XHTMLOptions.create();

        // 存放图片的文件夹 html中图片的路径
        options.setImageManager(new ImageManager( new File("./"),  "/" + imagePathStr ));
        options.setIgnoreStylesIfUnused(false);
        options.setFragment(true);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        XHTMLConverter.getInstance().convert(document, byteArrayOutputStream, options);

        // 压缩图片
        imgThumbnail(imagePathStr);
        return byteArrayOutputStream.toString();
    }
}
