package com.wht.item.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.google.common.base.Joiner;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.wht.item.admin.dto.CmsNoteNode;
import com.wht.item.admin.service.CmsArticleService;
import com.wht.item.admin.service.CmsNoteService;
import com.wht.item.common.util.ZipUtils;
import com.wht.item.mapper.CmsNoteMapper;
import com.wht.item.model.CmsArticle;
import com.wht.item.model.CmsNote;
import com.wht.item.model.CmsNoteExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CmsNoteServiceImpl implements CmsNoteService {

    @Resource
    private CmsNoteMapper noteMapper;
    @Resource
    private CmsArticleService articleService;
    private static final Logger LOGGER = LoggerFactory.getLogger(CmsNoteServiceImpl.class);


    @Override
    public int create(CmsNote cmsNote) {
        if(cmsNote.getParentId() == null) {
            cmsNote.setParentId(0L);
        }
        //查询同一层级是否有相同的名称
        CmsNoteExample example = new CmsNoteExample();
        example.createCriteria()
                .andNameEqualTo(cmsNote.getName())
                .andParentIdEqualTo(cmsNote.getParentId());
        List<CmsNote> cmsNoteList = noteMapper.selectByExample(example);
        if (cmsNoteList.size() > 0) {
            return 0;
        }
        int count = noteMapper.insert(cmsNote);
        if (count > 0) {
            try {
                createFile(getBathPath(cmsNote.getId()), "");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    @Override
    public void upload(MultipartFile[] multipartFiles, Long id) throws IOException {
        String BasePath;
        if (id == 0) {
            BasePath = "";
        } else {
            BasePath = getBathPath(id) + "/";
        }
        for (MultipartFile file : multipartFiles) {
            String filePath = file.getOriginalFilename();
            assert filePath != null;
            filePath = BasePath + filePath;
            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
            InputStream inputStream = file.getInputStream();
            String content = handleMd(inputStream);
            Long aid = createArticle(content, fileName);
            create(filePath, aid, content);
        }
    }

    @Override
    public int update(Long id, CmsNote cmsNote) {
        cmsNote.setId(id);
        updateFileName(id, cmsNote.getName());
        return noteMapper.updateByPrimaryKeySelective(cmsNote);
    }

    @Override
    public CmsNote getItem(Long id) {
        return noteMapper.selectByPrimaryKey(id);
    }

    @Override
    public int delete(Long id) {

        List<CmsNote> noteList = noteMapper.selectByExample(new CmsNoteExample());
        List<Long> delNoteIds = new ArrayList<>();
        List<Long> delArticleIds = new ArrayList<>();
        initIds(delNoteIds, delArticleIds, noteList, id);

        CmsNoteExample example = new CmsNoteExample();
        example.createCriteria().andIdIn(delNoteIds);
        String delFilePath = getBathPath(id);
        int count = noteMapper.deleteByExample(example);
        if (count > 0 && delArticleIds.size() > 0) {
            int aCount = articleService.delete(delArticleIds);
            LOGGER.info("删除笔记关联的文章数量：{}", aCount);
            deleteFile(delFilePath);
        }
        return count;

    }

    @Override
    public List<CmsNote> list(Long parentId, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        CmsNoteExample example = new CmsNoteExample();
        example.setOrderByClause("updated_time desc");
        example.createCriteria().andParentIdEqualTo(parentId);
        return noteMapper.selectByExample(example);
    }

    @Override
    public List<CmsNote> listAll() {
        CmsNoteExample example = new CmsNoteExample();
        example.setOrderByClause("menu_type desc");
        return noteMapper.selectByExample(example);
    }

    @Override
    public List<CmsNoteNode> treeList() {
        List<CmsNote> noteList = listAll();
        List<CmsNoteNode> noteNodeList = Lists.newArrayList();
        for (CmsNote note: noteList) {
            noteNodeList.add(toNode(note));
        }
        return listToTree(noteNodeList);
    }
    @Override
    public void updateFile(Long aid, String content) {
        String path = "upload/notes/";
        String filePath = getBathPathByArticleId(aid);
        try {
            OutputStream output = new FileOutputStream(path + filePath);
            output.write(content.getBytes("UTF-8"));
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public ResponseEntity download(Long id) throws IOException {
        String path = "upload/notes/";
        String downloadPath;
        String zipFile;
        if (id != 0) {
            downloadPath = getBathPath(id);
            zipFile = downloadPath.substring(downloadPath.lastIndexOf("/") + 1);
        } else {
            downloadPath = "";
            zipFile = "allNote";
        }
        String temporaryPath = "upload/.temporaryPath/";
        File temporaryDir = new File(temporaryPath);
        if(!temporaryDir.isDirectory()) temporaryDir.mkdirs();
        ZipUtils.doCompress(path + downloadPath, temporaryPath + zipFile + ".zip");
        File file = new File(temporaryPath + zipFile + ".zip");
        String fileName = URLEncoder.encode(file.getName(), "UTF-8");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment; filename=" + fileName);
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new FileSystemResource(file));
    }

    private String getBathPath(Long id) {
        List<CmsNote> noteList = listAll();
        StringBuffer BasePath = new StringBuffer();
        for (CmsNote note: noteList) {
            if (note.getId().equals(id)) {
                BasePath.append(note.getName());
                if (note.getParentId() != 0) {
                    getParentPath(note.getParentId(), noteList, BasePath);
                }
            }
        }
        return BasePath.toString();
    }

    private void getParentPath(Long parentId, List<CmsNote> noteList, StringBuffer BasePath) {
        for (CmsNote note: noteList) {
            if (note.getId().equals(parentId)) {
                BasePath.insert(0, note.getName() + "/" );
                if (note.getParentId() != 0) {
                    getParentPath(note.getParentId(), noteList, BasePath);
                }
            }
        }
    }

    private String getBathPathByArticleId(Long aid) {
        List<CmsNote> noteList = listAll();
        StringBuffer BasePath = new StringBuffer();
        for (CmsNote note: noteList) {
            if (note.getArticleId() != null && note.getArticleId().equals(aid)) {
                BasePath.append(note.getName());
                if (note.getParentId() != 0) {
                    getParentPath(note.getParentId(), noteList, BasePath);
                }
            }
        }
        return BasePath.toString();
    }

    private CmsNoteNode toNode(CmsNote note) {
        CmsNoteNode noteNode = new CmsNoteNode();
        BeanUtils.copyProperties(note, noteNode);
        return noteNode;
    }

    private List<CmsNoteNode> listToTree(List<CmsNoteNode> noteNodeList) {
        if (CollectionUtils.isEmpty(noteNodeList)) {
            return Lists.newArrayList();
        }
        Multimap<Long, CmsNoteNode> map= ArrayListMultimap.create();
        List<CmsNoteNode> rootList = Lists.newArrayList();
        for (CmsNoteNode note: noteNodeList) {
            map.put(note.getParentId(), note);
            if (note.getParentId().equals(0L)) {
                rootList.add(note);
            }
        }
        // 递归生成树
        transformTree(rootList, map);
        return rootList;
    }
    private void transformTree(List<CmsNoteNode> rootList, Multimap<Long, CmsNoteNode> map) {
        for (CmsNoteNode note: rootList) {
            // 遍历该层的每个元素
            // 处理当前层级的数据
            Long nextLevel = note.getId();
            // 处理下一层
            List<CmsNoteNode> tempList = (List<CmsNoteNode>) map.get(nextLevel);
            if (!CollectionUtils.isEmpty(tempList)) {
                note.setChildren(tempList);
                transformTree(tempList, map);
            }
        }
    }

    private void initIds(List<Long> delNoteIds, List<Long> delArticleIds, List<CmsNote> noteList, Long delId) {
        for (CmsNote note: noteList) {
            if (note.getId().equals(delId)) {
                delNoteIds.add(note.getId());
                if (note.getArticleId() != null) delArticleIds.add(note.getArticleId());
            }
            if (note.getParentId().equals(delId)) {
                delNoteIds.add(note.getId());
                if (note.getArticleId() != null) delArticleIds.add(note.getArticleId());
                initIds(delNoteIds, delArticleIds, noteList, note.getId());
            }
        }
    }

    private void create(String path, Long aid, String content) throws IOException {
        String[] nameArr = path.split("/");
        Long pid = null;
        for (int i = 0; i < nameArr.length; i++) {

            CmsNote note = new CmsNote();
            note.setName(nameArr[i]);
            if (i < nameArr.length -1) {
                note.setMenuType("folder");
                note.setParentId(pid);
                pid = uploadCreate(note, path, content);
            } else {
                note.setMenuType("file");
                note.setParentId(pid);
                note.setArticleId(aid);
                uploadCreate(note, path, content);
            }
        }
    }

    private Long uploadCreate(CmsNote cmsNote, String path, String content) throws IOException {
        if(cmsNote.getParentId() == null) {
            cmsNote.setParentId(0L);
        }
        //查询同一层级是否有相同的名称
        CmsNoteExample example = new CmsNoteExample();
        example.createCriteria()
                .andNameEqualTo(cmsNote.getName())
                .andParentIdEqualTo(cmsNote.getParentId());
        List<CmsNote> cmsNoteList = noteMapper.selectByExample(example);
        if (cmsNoteList.size() > 0) {
            articleService.delete(cmsNoteList.get(0).getArticleId());
            return cmsNoteList.get(0).getId();
        }
        noteMapper.insert(cmsNote);
        createFile(path, content);
        return cmsNote.getId();
    }

    private Long createArticle(String content, String name) {
        CmsArticle cmsArticle = new CmsArticle();
        cmsArticle.setArticleType("note");
        cmsArticle.setEditorType("markdownEditor");
        cmsArticle.setTitle(name);
        cmsArticle.setContent(content);
        articleService.create(cmsArticle);
        return cmsArticle.getId();
    }

    private void createFile(String filePath, String content) throws IOException {
        String path = "upload/notes/";
        File noteFile = new File(path + filePath);

        if(!(noteFile.getParentFile().exists())){
            noteFile.getParentFile().mkdirs();
        }
        if(noteFile.exists()){
            noteFile.delete();
        }else{
            noteFile.createNewFile();
        }

        OutputStream output = new FileOutputStream(path + filePath);
        output.write(content.getBytes("UTF-8"));
        output.close();
    }

    private void deleteFile(String filePath) {
        String path = "upload/notes/";
        File noteFile = new File(path + filePath);
        try {
            deleteFolder(noteFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void deleteFolder(File folder) throws Exception {
        if (!folder.exists()) {
            throw new Exception("文件不存在");
        }
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    //递归直到目录下没有文件
                    deleteFolder(file);
                } else {
                    //删除
                    file.delete();
                }
            }
        }
        //删除
        folder.delete();
    }

    private void updateFileName(Long id, String fileName) {
        String filePath = "upload/notes/" + getBathPath(id);
        String filePathNew = filePath.substring(0, filePath.lastIndexOf("/") + 1) + fileName;
        File file = new File(filePath);
        file.renameTo(new File(filePathNew));

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
}
