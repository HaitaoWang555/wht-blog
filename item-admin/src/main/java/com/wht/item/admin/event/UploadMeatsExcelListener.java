package com.wht.item.admin.event;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.wht.item.admin.dto.CmsMetasParam;
import com.wht.item.admin.service.CmsMetasService;
import com.wht.item.model.CmsMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Meats模板的读取类
 *
 * @author wht
 * @since 2020-05-30 21:45
 */
public class UploadMeatsExcelListener extends AnalysisEventListener<CmsMetasParam> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadMeatsExcelListener.class);
    public CmsMetasService metasService;

    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 3000;
    private List<CmsMetasParam> list = new ArrayList<>();

    @Override
    public void invoke(CmsMetasParam data, AnalysisContext context) {
        list.add(data);
        if (list.size() >= BATCH_COUNT) {
            saveData();
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
        LOGGER.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        LOGGER.info("{}条数据，开始存储数据库！", list.size());
        metasService.insertList(list);
        LOGGER.info("存储数据库成功！");
    }
}
