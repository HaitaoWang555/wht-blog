package com.wht.item.portal.service.impl;

import com.wht.item.portal.dao.MetasDao;
import com.wht.item.portal.service.MetasService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wht
 * @since 2020-06-08 2:43
 */
@Service
public class MetasServiceImpl implements MetasService {
    @Resource
    private MetasDao metasDao;

    @Override
    public List list(String type) {
        return metasDao.selectMetasDtoPublish(type);
    }
}
