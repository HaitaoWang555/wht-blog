package com.wht.item.portal.service.impl;

import com.github.pagehelper.PageHelper;
import com.wht.item.mapper.CmsArticleMapper;
import com.wht.item.model.CmsArticle;
import com.wht.item.model.CmsArticleExample;
import com.wht.item.portal.service.ArticleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wht
 * @since 2020-06-08 0:42
 */
@Service
public class ArticleServiceImpl implements ArticleService {
    @Resource
    private CmsArticleMapper articleMapper;

    @Override
    public List<CmsArticle> list(Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        CmsArticleExample example = new CmsArticleExample();
        CmsArticleExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo("publish");
        example.setOrderByClause("updated_time desc");
        List<CmsArticle> articleList = articleMapper.selectByExampleWithBLOBs(example);
        for (CmsArticle item : articleList) {
            transformPreView(item);
        }
        return articleList;
    }

    @Override
    public CmsArticle getItem(Long id) {
        return articleMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateCmsArticleHits(CmsArticle article) {
        // TODO 锁
        articleMapper.updateByPrimaryKey(article);
    }

    @Override
    public List<CmsArticle> getAllPublish() {
        CmsArticleExample example = new CmsArticleExample();
        CmsArticleExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo("publish");
        return articleMapper.selectByExampleWithBLOBs(example);
    }

    private void transformPreView(CmsArticle article) {
        String content = article.getContent();
        content =delHTMLTag(content);
        int maxLen = 150;
        int newLen = Math.min(content.length(), maxLen);
        content =content.substring(0, newLen).concat(" ......");
        article.setContent(content);
    }
    /**
     * 过滤html标签
     */
    private static String delHTMLTag(String htmlStr) {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?</script>"; //定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?</style>"; //定义style的正则表达式
        String regEx_string = "&nbsp;"; //定义特殊字符串
        String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式
        htmlStr = getString(htmlStr, regEx_style, regEx_script);
        htmlStr = getString(htmlStr, regEx_string, regEx_html);
        return htmlStr.trim(); //返回文本字符串
    }

    private static String getString(String htmlStr, String regEx_string, String regEx_html) {
        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); //过滤html标签
        Pattern p_string = Pattern.compile(regEx_string, Pattern.CASE_INSENSITIVE);
        Matcher m_string = p_string.matcher(htmlStr);
        htmlStr = m_string.replaceAll(""); //过滤特殊字符
        return htmlStr;
    }
}
