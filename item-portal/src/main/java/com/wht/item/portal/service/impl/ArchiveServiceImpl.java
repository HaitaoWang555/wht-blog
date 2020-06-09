package com.wht.item.portal.service.impl;

import com.wht.item.mapper.CmsArticleMapper;
import com.wht.item.model.CmsArticle;
import com.wht.item.model.CmsArticleExample;
import com.wht.item.portal.dto.Archives;
import com.wht.item.portal.service.ArchiveService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wht
 * @since 2020-06-10 6:52
 */
@Service
public class ArchiveServiceImpl implements ArchiveService {
    @Resource
    private CmsArticleMapper cmsArticleMapper;

    @Override
    public List<CmsArticle> getAll() {
        CmsArticleExample example = new CmsArticleExample();
        example.createCriteria().andStatusEqualTo("publish");
        return cmsArticleMapper.selectByExample(example);
    }

    @Override
    public List<Archives> archive(List<CmsArticle> articleList, List<Archives> archivesList) {
        String current = "";
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM" );
        for (CmsArticle article:articleList) {
            String dateStr = sdf.format(article.getUpdatedTime());
            if (dateStr.equals(current)) {
                Archives arc = archivesList.get(archivesList.size() - 1);
                arc.getArticle().add(article);
                arc.setCount(arc.getArticle().size());
            } else {
                current = dateStr;
                Archives arc = new Archives();
                arc.setDateStr(dateStr);
                arc.setCount(1);
                List<CmsArticle> arts = new ArrayList<>();
                arts.add(article);
                arc.setArticle(arts);
                archivesList.add(arc);
            }
        }
        return archivesList;
    }
}
