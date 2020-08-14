package com.wht.item.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.google.common.base.Joiner;
import com.wht.item.admin.dao.CmsArticleDao;
import com.wht.item.admin.service.CmsArticleService;
import com.wht.item.admin.service.CmsNoteService;
import com.wht.item.admin.util.Util;
import com.wht.item.mapper.CmsArticleMapper;
import com.wht.item.model.CmsArticle;
import com.wht.item.model.CmsArticleExample;
import fr.opensagres.poi.xwpf.converter.core.ImageManager;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLConverter;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLOptions;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.fit.cssbox.css.CSSNorm;
import org.fit.cssbox.css.DOMAnalyzer;
import org.springframework.stereotype.Service;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 内容管理 博客文章
 *
 * @author wht
 * @since 2020-05-31 1:39
 */
@Service
public class CmsArticleServiceImpl implements CmsArticleService {
    @Resource
    private CmsArticleMapper articleMapper;
    @Resource
    private CmsArticleDao articleDao;
    @Resource
    private CmsNoteService noteService;

    @Override
    public List<CmsArticle> listAll() {
        return articleMapper.selectByExample(example());
    }

    @Override
    public List<CmsArticle> listAllWithContent() {
        return articleMapper.selectByExampleWithBLOBs(example());
    }

    @Override
    public CmsArticle getItem(Long id) {
        return articleMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(Long id, CmsArticle cmsArticle) {
        if (cmsArticle.getArticleType().equals("note")) {
            noteService.updateFile(id, cmsArticle.getContent());
        }
        return articleMapper.updateByPrimaryKeySelective(cmsArticle);
    }

    @Override
    public List<CmsArticle> list(String title, String status, String meta, Long authorId, String sortBy, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize, sortBy);
        List<Integer> metaArr = new ArrayList<>();
        if (meta != null) {
            for (String ele : meta.split(",")) {
                Integer item = Integer.valueOf(ele);
                metaArr.add(item);
            }
        }
        return articleDao.search(title, status, authorId, metaArr);
    }

    @Override
    public int create(CmsArticle cmsArticle) {
        return articleMapper.insertSelective(cmsArticle);
    }

    @Override
    public int delete(Long id) {
        return articleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int delete(List<Long> ids) {
        CmsArticleExample example = new CmsArticleExample();
        example.createCriteria().andIdIn(ids);
        return articleMapper.deleteByExample(example);
    }

    /**
     * 更新文章评论数
     * @param id 文章ID
     */
    public void updateCmsArticleCommentCount(Long id, int count) {
        CmsArticle article = articleMapper.selectByPrimaryKey(id);
        article.setCommentCount(article.getCommentCount() - count);
        articleMapper.updateByPrimaryKey(article);
    }

    private CmsArticleExample example() {
        CmsArticleExample example = new CmsArticleExample();
        example.createCriteria().andArticleTypeEqualTo("blog");
        return example;
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
    @Override
    public String getArticleContent(String suffix, InputStream inputStream) throws ParserConfigurationException, TransformerException, IOException {
        String content = "";
        switch (suffix.toLowerCase()) {
            case "doc":
                content = handleDoc(inputStream);
                break;
            case "docx":
                content = handleDocx(inputStream);
                break;
            default:
                content = handleMd(inputStream);
                break;
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
     */
    private String handleDoc(InputStream inputStream) throws IOException, ParserConfigurationException, TransformerException {
        String imagePathStr = Util.createFilePath("article");

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
     */
    private String handleDocx(InputStream inputStream) throws IOException {

        String imagePathStr = Util.createFilePath("article");

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
