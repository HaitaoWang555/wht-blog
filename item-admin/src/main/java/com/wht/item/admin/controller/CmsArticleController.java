package com.wht.item.admin.controller;

import com.google.common.base.Joiner;
import com.wht.item.admin.dto.CmsArticleParam;
import com.wht.item.admin.service.CmsArticleService;
import com.wht.item.admin.service.CmsMetasService;
import com.wht.item.admin.service.UmsAdminService;
import com.wht.item.admin.util.SecurityUtil;
import com.wht.item.admin.util.Util;
import com.wht.item.common.api.CommonPage;
import com.wht.item.common.api.CommonResult;
import com.wht.item.model.CmsArticle;
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

@RestController
@Api(tags = "内容管理 博客文章")
@RequestMapping("/article")
public class CmsArticleController {
    @Resource
    private CmsArticleService articleService;
    @Resource
    private CmsMetasService metasService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsArticleController.class);

    @ApiOperation("添加")
    @PostMapping("/create")
    public CommonResult create(@RequestBody CmsArticleParam cmsArticleParam) {
        CmsArticle cmsArticle = new CmsArticle();
        BeanUtils.copyProperties(cmsArticleParam, cmsArticle);
        cmsArticle.setAuthorId(SecurityUtil.getCurrentUserId());
        cmsArticle.setArticleType("blog");
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

    @ApiOperation("更新")
    @PostMapping("/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody CmsArticleParam cmsArticleParam) {
        CmsArticle cmsArticle = new CmsArticle();
        BeanUtils.copyProperties(cmsArticleParam, cmsArticle);
        if (cmsArticleParam.getContentUpdate() != null && cmsArticleParam.getContentUpdate()) {
            cmsArticle.setUpdatedContentTime(new Date());
        }
        cmsArticle.setId(id);
        int count = articleService.update(id, cmsArticle);
        if (count > 0) {
            String tags = cmsArticle.getTags();
            String category = cmsArticle.getCategory();
            if (StringUtils.isNotBlank(tags)) metasService.saveOrRemoveMeta(tags, "tag", id);
            if (StringUtils.isNotBlank(category)) metasService.saveOrRemoveMeta(category, "category", id);
            return CommonResult.success("修改成功");
        }
        return CommonResult.failed();
    }

    @ApiOperation("分页模糊查询")
    @GetMapping("/list")
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
    @GetMapping("/{id}")
    public CommonResult<CmsArticle> getItem(@PathVariable Long id) {
        CmsArticle cmsArticle = articleService.getItem(id);
        return CommonResult.success(cmsArticle);
    }

    @ApiOperation("批量删除")
    @PostMapping("/delete")
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = articleService.delete(ids);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("图片上传")
    @PostMapping("/upload/img")
    public CommonResult uploadImg(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return CommonResult.failed("文件为空");
        }
        String realPath =  Util.createFilePath("article");
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
            Util.imgThumbnail(localFilePath, thumbnailFilePath);
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
    @PostMapping("/upload")
    public CommonResult uploadArticle(@RequestParam("file") MultipartFile file) throws IOException, TransformerException, ParserConfigurationException {
        String fileName = file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();
        assert fileName != null;
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String content = articleService.getArticleContent(suffix, inputStream);
        if (content.equals("")) {
            return CommonResult.failed("格式不正确");
        }
        return CommonResult.success(content,"导入成功");
    }

    @ApiOperation("文章上传文件夹")
    @PostMapping("/uploadDir")
    public CommonResult uploadDir(@RequestParam(required =false, value = "file") MultipartFile[] multipartFiles) throws IOException, TransformerException, ParserConfigurationException {
        for (MultipartFile file : multipartFiles) {
            String fileName = file.getOriginalFilename();
            InputStream inputStream = file.getInputStream();
            String suffix = fileName.substring(fileName.indexOf(".") + 1);
            String content = articleService.getArticleContent(suffix, inputStream);
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
                cmsArticle.setArticleType("blog");
                articleService.create(cmsArticle);
            } catch (Exception e) {
                LOGGER.error(" ERROR: {}",  e.getMessage());
            }
        }
        return CommonResult.success("导入成功");
    }

}
