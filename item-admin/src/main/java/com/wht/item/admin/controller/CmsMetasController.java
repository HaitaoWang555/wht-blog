package com.wht.item.admin.controller;

import com.alibaba.excel.EasyExcel;
import com.wht.item.admin.dto.CmsMetasParam;
import com.wht.item.admin.event.UploadMeatsExcelListener;
import com.wht.item.admin.service.CmsMetasService;
import com.wht.item.common.api.CommonPage;
import com.wht.item.common.api.CommonResult;
import com.wht.item.model.CmsMeta;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 属性(标签和分类)管理 Controller
 *
 * @author wht
 * @since 2020-05-30 19:36
 */
@RestController
@Api(tags = "属性(标签和分类)管理")
@RequestMapping("/metas")
public class CmsMetasController {

    @Resource
    private CmsMetasService metasService;

    @ApiOperation("添加属性")
    @PostMapping("/create")
    public CommonResult create(@RequestBody CmsMeta cmsMeta) {
        int count = metasService.create(cmsMeta);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("根据ID删除标签和分类")
    @PostMapping("/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        int count = metasService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("批量删除")
    @PostMapping("/delete")
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = metasService.delete(ids);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("根据ID获取标签和分类详情")
    @GetMapping("/{id}")
    public CommonResult<CmsMeta> getItem(@PathVariable Long id) {
        CmsMeta cmsMeta = metasService.getItem(id);
        return CommonResult.success(cmsMeta);
    }

    @ApiOperation("修改标签和分类")
    @PostMapping("/update/{id}")
    public CommonResult update(@PathVariable Long id,
                               @RequestBody CmsMeta cmsMeta) {
        int count = metasService.update(id, cmsMeta);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("查询所有标签和分类")
    @GetMapping("/listAll")
    public CommonResult<List<CmsMeta>> listAll() {
        List<CmsMeta> resourceList = metasService.listAll();
        return CommonResult.success(resourceList);
    }

    @ApiOperation("分页模糊查询标签和分类")
    @GetMapping("/list")
    public CommonResult<CommonPage<CmsMeta>> list(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "sortBy", required = false, defaultValue = "updated_time desc") String sortBy,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<CmsMeta> resourceList = metasService.list(type, keyword, sortBy, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(resourceList));
    }

    @ApiOperation("查询所有标签和分类")
    @PostMapping("/import")
    public CommonResult importByExcel(MultipartFile file) throws IOException {
        UploadMeatsExcelListener uploadExcelListener = new UploadMeatsExcelListener();
        uploadExcelListener.metasService = metasService;
        EasyExcel.read(file.getInputStream(), CmsMetasParam.class, uploadExcelListener).sheet().doRead();
        return CommonResult.success("导入成功");
    }

    @ApiOperation("下载导入模板")
    @GetMapping("/downloadTemplate")
    public void downloadTemplate( HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("标签与分类", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        List<CmsMetasParam> metas = new ArrayList<>();
        EasyExcel.write(response.getOutputStream(), CmsMetasParam.class).sheet("标签与分类").doWrite(metas);
    }

    @ApiOperation("导出数据")
    @GetMapping("/export")
    public void download(
            @RequestParam(value = "ids", required = false) String ids,
            HttpServletResponse response
    ) throws IOException {
        // 这里注意 有同学反应下载的文件名不对。这个时候 请别使用swagger 他会有影响
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        String fileName = URLEncoder.encode("标签与分类", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), CmsMetasParam.class).sheet("标签与分类").doWrite(downloadMeta(ids));
    }

    private List<CmsMeta> downloadMeta(String ids) {
        if (StringUtils.isEmpty(ids)) {
            return metasService.listAll();
        } else {
            String[] idArr = ids.split(",");
            return metasService.searchByIds(idArr);
        }
    }

}
