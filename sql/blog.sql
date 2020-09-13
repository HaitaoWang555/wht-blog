-- MySQL dump 10.13  Distrib 8.0.20, for Linux (x86_64)
--
-- Host: localhost    Database: blog
-- ------------------------------------------------------
-- Server version	8.0.20

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cms_article`
--

DROP TABLE IF EXISTS `cms_article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cms_article` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '文章ID',
  `title` varchar(255) NOT NULL COMMENT '文章标题',
  `content` mediumtext COMMENT '文章内容',
  `author_id` bigint DEFAULT NULL COMMENT '文章作者ID',
  `hits` int DEFAULT '0' COMMENT '文章点击量',
  `tags` varchar(255) DEFAULT NULL COMMENT '文章标签',
  `category` varchar(255) DEFAULT NULL COMMENT '文章分类',
  `status` varchar(32) DEFAULT NULL COMMENT '文章状态 publish 已发布 draft 草稿',
  `editor_type` varchar(32) DEFAULT NULL COMMENT '编辑器类型 markdownEditor tinymceEditor',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `article_type` varchar(32) DEFAULT NULL COMMENT '文章类型 blog 博客 note 笔记',
  `updated_content_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '文章内容更新时间',
  `comment_count` int NOT NULL DEFAULT '0' COMMENT '文章评论数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=536 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cms_comment`
--

DROP TABLE IF EXISTS `cms_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cms_comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `article_id` bigint NOT NULL,
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `link` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `quote_content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'ip地址',
  `user_agent` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cms_meta`
--

DROP TABLE IF EXISTS `cms_meta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cms_meta` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类/标签ID',
  `name` varchar(255) NOT NULL COMMENT '分类/标签ID',
  `type` varchar(45) NOT NULL COMMENT '分类/标签ID',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=125 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cms_middle`
--

DROP TABLE IF EXISTS `cms_middle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cms_middle` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `a_id` bigint NOT NULL COMMENT '文章ID',
  `m_id` bigint NOT NULL COMMENT '分类/标签ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=134 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cms_note`
--

DROP TABLE IF EXISTS `cms_note`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cms_note` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '笔记菜单名称',
  `parent_id` bigint DEFAULT NULL COMMENT '父级ID',
  `menu_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '菜单类型 folder 文件夹 file 文件',
  `article_id` bigint DEFAULT NULL COMMENT '对应的文章id',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `author_id` bigint DEFAULT '0' COMMENT '文章作者ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=611 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cms_poetry`
--

DROP TABLE IF EXISTS `cms_poetry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cms_poetry` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '诗词名称',
  `dynasty` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '朝代',
  `author` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '作者',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1114374 DEFAULT CHARSET=utf8 COMMENT='汉朝诗词';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ums_admin`
--

DROP TABLE IF EXISTS `ums_admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ums_admin` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(64) DEFAULT NULL,
  `password` varchar(64) DEFAULT NULL,
  `icon` varchar(500) DEFAULT NULL COMMENT '头像',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `nick_name` varchar(200) DEFAULT NULL COMMENT '昵称',
  `note` varchar(500) DEFAULT NULL COMMENT '备注信息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `status` int DEFAULT '1' COMMENT '帐号启用状态：0->禁用；1->启用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='后台用户表';
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Dumping data for table `ums_admin`
--

LOCK TABLES `ums_admin` WRITE;
/*!40000 ALTER TABLE `ums_admin` DISABLE KEYS */;
INSERT INTO `ums_admin` VALUES (1,'blog','$2a$10$uROVCwt7aB0dPFtRCKjz5.mWp1Bw9iXX07.z9G7MDBM.8tLQZ9sBm',NULL,'15124505701@163.com','王海涛','','2020-05-30 10:44:02','2020-09-13 15:48:21',1),(2,'admin','$2a$10$uROVCwt7aB0dPFtRCKjz5.mWp1Bw9iXX07.z9G7MDBM.8tLQZ9sBm',NULL,'admin@admin.com','admin','admin','2020-05-30 11:16:49',NULL,1),(3,'article','$2a$10$uROVCwt7aB0dPFtRCKjz5.mWp1Bw9iXX07.z9G7MDBM.8tLQZ9sBm',NULL,'article@163.com','article',NULL,'2020-06-14 04:46:41','2020-08-12 05:19:31',1),(4,'guest','$2a$10$rWEsN8FX.fvi7O5RjwsdPe.8ZsIdGktmaWnSUu/A9gViiHDgt3Lu2',NULL,'guest@guest.com','guest','guest','2020-09-13 15:36:22','2020-09-13 21:27:52',1);
/*!40000 ALTER TABLE `ums_admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ums_admin_login_log`
--

DROP TABLE IF EXISTS `ums_admin_login_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ums_admin_login_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `admin_id` bigint DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `ip` varchar(64) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `user_agent` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='后台用户登录日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ums_admin_role_relation`
--

DROP TABLE IF EXISTS `ums_admin_role_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ums_admin_role_relation` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `admin_id` bigint DEFAULT NULL,
  `role_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='后台用户和角色关系表';
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Dumping data for table `ums_admin_role_relation`
--

LOCK TABLES `ums_admin_role_relation` WRITE;
/*!40000 ALTER TABLE `ums_admin_role_relation` DISABLE KEYS */;
INSERT INTO `ums_admin_role_relation` VALUES (1,1,1),(3,4,3),(4,3,2),(5,3,1);
/*!40000 ALTER TABLE `ums_admin_role_relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ums_menu`
--

DROP TABLE IF EXISTS `ums_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ums_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint DEFAULT NULL COMMENT '父级ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `title` varchar(100) DEFAULT NULL COMMENT '菜单名称',
  `level` int DEFAULT NULL COMMENT '菜单级数',
  `sort` int DEFAULT NULL COMMENT '菜单排序',
  `name` varchar(100) DEFAULT NULL COMMENT '前端名称',
  `icon` varchar(200) DEFAULT NULL COMMENT '前端图标',
  `hidden` int DEFAULT NULL COMMENT '前端隐藏',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='后台菜单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ums_menu`
--

LOCK TABLES `ums_menu` WRITE;
/*!40000 ALTER TABLE `ums_menu` DISABLE KEYS */;
INSERT INTO `ums_menu` VALUES (1,0,'2020-05-30 10:52:27','权限管理',0,2,'ums','ums',0),(2,1,'2020-05-30 10:52:24','用户列表',1,2,'admin','ums-admin',0),(3,1,'2020-05-30 10:52:21','角色列表',1,2,'role','ums-role',0),(4,1,'2020-05-30 10:52:16','菜单列表',1,2,'menu','ums-menu',0),(5,1,'2020-05-30 10:52:13','资源列表',1,2,'resource','ums-resource',0),(6,0,'2020-05-30 18:27:23','内容管理',0,3,'content','cms-article',0),(7,6,'2020-05-30 18:29:32','文章列表',1,3,'list','cms-list',0),(8,6,'2020-05-30 18:30:22','创建文章',1,3,'create','cms-create',0),(9,6,'2020-05-30 18:33:26','标签与分类',1,3,'metas','cms-list',0),(10,6,'2020-05-30 18:51:46','编辑文章',1,0,'edit','',1),(11,1,'2020-05-30 19:11:53','分配菜单',1,0,'allocMenu','',1),(12,1,'2020-05-30 19:12:19','分配资源',1,0,'allocResource','',1),(13,1,'2020-05-30 19:12:43','添加菜单',1,0,'addMenu','',1),(14,1,'2020-05-30 19:13:05','修改菜单',1,0,'updateMenu','',1),(15,1,'2020-05-30 19:13:25','资源分类',1,0,'resourceCategory','',1),(16,6,'2020-06-21 20:18:09','诗词列表',1,0,'poetry','cms-list',0),(17,6,'2020-08-06 16:02:55','笔记列表',1,0,'note','cms-list',0),(18,6,'2020-08-18 21:22:49','评论管理',1,0,'comment','cms-list',0);
/*!40000 ALTER TABLE `ums_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ums_resource`
--

DROP TABLE IF EXISTS `ums_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ums_resource` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `name` varchar(200) DEFAULT NULL COMMENT '资源名称',
  `url` varchar(200) DEFAULT NULL COMMENT '资源URL',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `category_id` bigint DEFAULT NULL COMMENT '资源分类ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='后台资源表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ums_resource`
--

LOCK TABLES `ums_resource` WRITE;
/*!40000 ALTER TABLE `ums_resource` DISABLE KEYS */;
INSERT INTO `ums_resource` VALUES (1,'2020-06-14 02:44:52','用户列表','/api/manage/admin/list','获取后台用户列表',1),(2,'2020-06-14 02:44:52','新增用户','/api/manage/admin/register','注册账号',1),(3,'2020-06-14 02:44:52','是否启用用户','/api/manage/admin/updateStatus/*','修改用户状态',1),(4,'2020-06-14 02:44:52','编辑用户','/api/manage/admin/update/*','修改用户信息',1),(5,'2020-06-14 02:44:52','删除用户','/api/manage/admin/delete/*','删除用户',1),(6,'2020-06-14 02:44:52','角色列表','/api/manage/role/list','获取角色列表',1),(7,'2020-06-14 02:44:52','新增角色','/api/manage/role/create','注册账号',1),(8,'2020-06-14 02:44:52','是否启用角色','/api/manage/role/updateStatus/*','修改用户状态',1),(9,'2020-06-14 02:44:52','修改角色信息','/api/manage/role/update/*','修改角色信息',1),(10,'2020-06-14 02:44:52','删除角色','/api/manage/role/delete','删除角色',1),(11,'2020-06-14 02:44:52','分配资源','/api/manage/role/allocResource','分配资源',1),(12,'2020-06-14 02:44:52','分配菜单','/api/manage/role/allocMenu','分配菜单',1),(13,'2020-06-14 02:44:52','菜单列表','/api/manage/menu/treeList','获取菜单列表',1),(14,'2020-06-14 02:44:52','新增菜单','/api/manage/menu/create','新增菜单',1),(15,'2020-06-14 02:44:52','是否显示菜单','/api/manage/menu/updateHidden/*','是否显示菜单',1),(16,'2020-06-14 02:44:52','编辑菜单','/api/manage/menu/update/*','编辑菜单',1),(17,'2020-06-14 02:44:52','删除菜单','/api/manage/menu/delete/*','删除菜单',1),(18,'2020-06-14 02:44:52','资源列表','/api/manage/resource/list','获取资源列表',1),(19,'2020-06-14 02:44:52','新增资源','/api/manage/resource/create','新增资源',1),(20,'2020-06-14 02:44:52','编辑资源','/api/manage/resource/update/*','编辑资源',1),(21,'2020-06-14 02:44:52','删除资源','/api/manage/resource/delete/*','删除资源',1),(22,'2020-06-14 02:44:52','查看资源分类','/api/manage/resourceCategory/listAll','查看资源分类',1),(23,'2020-06-14 02:44:52','新增资源分类','/api/manage/resourceCategory/create','新增资源分类',1),(24,'2020-06-14 02:44:52','编辑资源分类','/api/manage/resourceCategory/update/*','编辑资源分类',1),(25,'2020-06-14 02:44:52','删除资源分类','/api/manage/resourceCategory/delete/*','删除资源分类',1),(26,'2020-06-14 02:44:52','查看标签列表','/api/manage/metas/list','查看标签列表',2),(27,'2020-06-14 02:44:52','新增标签','/api/manage/metas/create','新增标签',2),(28,'2020-06-14 02:44:52','编辑标签','/api/manage/metas/update/*','编辑标签',2),(29,'2020-06-14 02:44:52','删除标签','/api/manage/metas/delete','删除标签',2),(30,'2020-06-14 02:44:52','批量导入标签','/api/manage/metas/import','批量导入标签',2),(31,'2020-06-14 02:44:52','导出标签','/api/manage/metas/export','导出标签',2),(32,'2020-06-14 02:44:52','下载标签模板','/api/manage/metas/downloadTemplate','下载标签模板',2),(33,'2020-06-14 04:34:36','文章列表','/api/manage/article/list','',2),(34,'2020-06-14 04:39:30','新增文章','/api/manage/article/create','',2),(35,'2020-06-14 04:40:26','批量删除文章','/api/manage/article/delete','',2),(36,'2020-06-14 04:41:45','修改文章','/api/manage/article/update/*','',2),(37,'2020-06-14 04:42:55','导入文章文件夹','/api/manage/article/uploadDir','',2),(38,'2020-08-08 21:38:19','获取全部诗词','/api/manage/poetry/listAll','',2),(39,'2020-06-21 20:19:03','获取诗词分页列表','/api/manage/poetry/list','',2),(40,'2020-06-21 20:19:17','新增诗词','/api/manage/poetry/create','',2),(41,'2020-06-21 20:19:31','根据ID更新诗词','/api/manage/poetry/update/*','',2),(42,'2020-06-21 20:19:45','根据ID批量删除诗词','/api/manage/poetry/delete','',2),(43,'2020-06-21 20:19:59','搜索诗词','/api/manage/poetry/search','',2),(44,'2020-08-05 21:48:14','诗词导出','/api/manage/poetry/export','',2),(45,'2020-08-05 21:48:14','添加笔记列表','/api/manage/note/create','',2),(46,'2020-08-05 21:48:14','笔记上传文件夹','/api/manage/note/uploadDir','',2),(47,'2020-08-05 21:48:14','笔记下载','/api/manage/note/download','',2),(48,'2020-08-05 21:48:14','修改笔记菜单','/api/manage/note//update/*','',2),(49,'2020-08-05 21:48:14','复制到文章','/api/manage/note/toArticle/*','',2),(50,'2020-08-05 21:48:14','分页查询笔记列表','/api/manage/note/list/*',NULL,2),(51,'2020-08-05 21:48:14','全部笔记列表','/api/manage/note/allList','',2),(52,'2020-08-05 21:48:14','树形笔记列表','/api/manage/note/treeList','',2),(53,'2020-08-05 21:48:14','根据ID删除笔记','/api/manage/note/delete/*','',2),(55,'2020-09-13 15:41:57','评论列表','/api/manage/comment/search','',2),(56,'2020-09-13 15:43:03','批量删除评论','/api/manage/comment/delete','',2),(57,'2020-09-13 15:43:51','批量删除评论中链接','/api/manage/comment/deleteLinks','',2),(58,'2020-09-13 16:04:55','分配角色','/api/manage/admin/role/update','',1);
/*!40000 ALTER TABLE `ums_resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ums_resource_category`
--

DROP TABLE IF EXISTS `ums_resource_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ums_resource_category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `name` varchar(200) DEFAULT NULL COMMENT '分类名称',
  `sort` int DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='资源分类表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ums_resource_category`
--

LOCK TABLES `ums_resource_category` WRITE;
/*!40000 ALTER TABLE `ums_resource_category` DISABLE KEYS */;
INSERT INTO `ums_resource_category` VALUES (1,'2020-06-14 02:38:23','权限模块',0),(2,'2020-06-14 02:38:40','文章模块',0);
/*!40000 ALTER TABLE `ums_resource_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ums_role`
--

DROP TABLE IF EXISTS `ums_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ums_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `admin_count` int DEFAULT NULL COMMENT '后台用户数量',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `status` int DEFAULT '1' COMMENT '启用状态：0->禁用；1->启用',
  `sort` int DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='后台用户角色表';
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Dumping data for table `ums_role`
--

LOCK TABLES `ums_role` WRITE;
/*!40000 ALTER TABLE `ums_role` DISABLE KEYS */;
INSERT INTO `ums_role` VALUES (1,'超级管理员','拥有所有查看和操作功能',2,'2020-02-02 15:11:05',1,0),(2,'文章管理员',NULL,1,'2020-06-14 04:47:08',1,0),(3,'访客权限','拥有查看权限无修改权限',1,'2020-09-13 15:37:21',1,0);
/*!40000 ALTER TABLE `ums_role` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `ums_role_menu_relation`
--

DROP TABLE IF EXISTS `ums_role_menu_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ums_role_menu_relation` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=143 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='后台角色菜单关系表';
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Dumping data for table `ums_role_menu_relation`
--

LOCK TABLES `ums_role_menu_relation` WRITE;
/*!40000 ALTER TABLE `ums_role_menu_relation` DISABLE KEYS */;
INSERT INTO `ums_role_menu_relation` VALUES (69,2,6),(70,2,7),(71,2,8),(72,2,9),(73,2,10),(107,1,1),(108,1,2),(109,1,3),(110,1,4),(111,1,5),(112,1,11),(113,1,12),(114,1,13),(115,1,14),(116,1,15),(117,1,6),(118,1,7),(119,1,8),(120,1,9),(121,1,10),(122,1,16),(123,1,17),(124,1,18),(125,3,1),(126,3,2),(127,3,3),(128,3,4),(129,3,5),(130,3,11),(131,3,12),(132,3,13),(133,3,14),(134,3,15),(135,3,6),(136,3,7),(137,3,8),(138,3,9),(139,3,10),(140,3,16),(141,3,17),(142,3,18);
/*!40000 ALTER TABLE `ums_role_menu_relation` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `ums_role_resource_relation`
--

DROP TABLE IF EXISTS `ums_role_resource_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ums_role_resource_relation` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint DEFAULT NULL COMMENT '角色ID',
  `resource_id` bigint DEFAULT NULL COMMENT '资源ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=354 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='后台角色资源关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ums_role_resource_relation`
--

LOCK TABLES `ums_role_resource_relation` WRITE;
/*!40000 ALTER TABLE `ums_role_resource_relation` DISABLE KEYS */;
INSERT INTO `ums_role_resource_relation` VALUES (63,2,26),(64,2,27),(65,2,28),(66,2,29),(67,2,30),(68,2,31),(69,2,32),(70,2,33),(71,2,34),(72,2,35),(73,2,36),(74,2,37),(279,1,1),(280,1,2),(281,1,3),(282,1,4),(283,1,5),(284,1,6),(285,1,7),(286,1,8),(287,1,9),(288,1,10),(289,1,11),(290,1,12),(291,1,13),(292,1,14),(293,1,15),(294,1,16),(295,1,17),(296,1,18),(297,1,19),(298,1,20),(299,1,21),(300,1,22),(301,1,23),(302,1,24),(303,1,25),(304,1,26),(305,1,27),(306,1,28),(307,1,29),(308,1,30),(309,1,31),(310,1,32),(311,1,33),(312,1,34),(313,1,35),(314,1,36),(315,1,37),(316,1,38),(317,1,39),(318,1,40),(319,1,41),(320,1,42),(321,1,43),(322,1,44),(323,1,45),(324,1,46),(325,1,47),(326,1,48),(327,1,49),(328,1,50),(329,1,51),(330,1,52),(331,1,53),(332,1,55),(333,1,56),(334,1,57),(335,1,58),(336,3,1),(337,3,6),(338,3,13),(339,3,18),(340,3,22),(341,3,26),(342,3,32),(343,3,33),(344,3,38),(345,3,39),(346,3,43),(347,3,44),(348,3,46),(349,3,47),(350,3,50),(351,3,51),(352,3,52),(353,3,55);
/*!40000 ALTER TABLE `ums_role_resource_relation` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-09-13 22:14:29
