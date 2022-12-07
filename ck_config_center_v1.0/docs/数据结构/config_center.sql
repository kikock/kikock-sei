/*
 Navicat Premium Data Transfer

 Source Server         : jx mysql
 Source Server Type    : MySQL
 Source Server Version : 50723
 Source Host           : kikock.tk:3306
 Source Schema         : config_center

 Target Server Type    : MySQL
 Target Server Version : 50723
 File Encoding         : 65001

 Date: 18/03/2022 11:19:05
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for application_module
-- ----------------------------
DROP TABLE IF EXISTS `application_module`;
CREATE TABLE `application_module`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id标识',
  `platform_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '平台Id',
  `code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '代码',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_app_module_platform_code`(`platform_id`, `code`) USING BTREE,
  INDEX `idx_app_module_code`(`code`) USING BTREE,
  INDEX `idx_app_module_platform_id`(`platform_id`) USING BTREE,
  CONSTRAINT `application_module_ibfk_1` FOREIGN KEY (`platform_id`) REFERENCES `platform` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '应用模块' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of application_module
-- ----------------------------
INSERT INTO `application_module` VALUES ('4028c2925f520f4f015f523589010003', '4028c2925f5175c5015f51eb9c770001', 'BASIC_API', '基础应用API');
INSERT INTO `application_module` VALUES ('c0a84203-631f-17fa-8163-a59669270118', '4028c2925f5175c5015f51eb9c770001', 'API_GATEWAY', 'API网关');
INSERT INTO `application_module` VALUES ('c0a84203-631f-17fa-8163-de005c1a02f1', '4028c2925f5175c5015f51eb9c770001', 'MC_API', '监控中心API');
INSERT INTO `application_module` VALUES ('c0a84211-6208-19cb-8162-2347677e0078', '4028c2925f5175c5015f51eb9c770001', 'FSOP_AMS_API', '财务共享运营平台--档案管理系统API');
INSERT INTO `application_module` VALUES ('c0a84211-6208-19cb-8162-2801c899009c', '4028c2925f5175c5015f51eb9c770001', 'FSOP_EAMS_API', '财务共享运营平台--电子档案管理系统API');
INSERT INTO `application_module` VALUES ('c0a84212-5f7c-131f-815f-7fd1821f0015', '4028c2925f5175c5015f51eb9c770001', 'FLOW_API', '工作流API');
INSERT INTO `application_module` VALUES ('c0a84212-5f7c-131f-815f-7fd1c9b30016', '4028c2925f5175c5015f51eb9c770001', 'FLOW_WEB', '工作流WEB');
INSERT INTO `application_module` VALUES ('c0a84212-5f87-13e3-815f-87824f610000', '4028c2925f5175c5015f51eb9c770001', 'TASK_API', '后台作业API');
INSERT INTO `application_module` VALUES ('c0a84402-7688-1e64-8176-d56084380000', '4028c2925f5175c5015f51eb9c770001', 'ATND_API', '社会责任认证');
INSERT INTO `application_module` VALUES ('c0a84402-7916-1bea-8179-64a91b4c0001', '4028c2925f5175c5015f51eb9c770001', 'THOB_MOBILE_REACT', '三重一大移动端页面');
INSERT INTO `application_module` VALUES ('c0a84402-7916-1bea-8179-acfeaacc0005', '4028c2925f5175c5015f51eb9c770001', 'SAI_API', '支付宝,微信,钉钉等三方集成api');
INSERT INTO `application_module` VALUES ('c0a84403-7770-13d6-8177-bd0a53030000', '4028c2925f5175c5015f51eb9c770001', 'DINGTALK_API', '钉钉集成模块');
INSERT INTO `application_module` VALUES ('c0a84403-7bec-1054-817b-ed48d0130000', '4028c2925f5175c5015f51eb9c770001', 'CADRES_API', '干部专职化管理-服务端');
INSERT INTO `application_module` VALUES ('c0a84403-7bec-1054-817b-ed4a99350001', '4028c2925f5175c5015f51eb9c770001', 'CADRES_WEB', '干部专职化管理-web端');
INSERT INTO `application_module` VALUES ('c0a84405-78af-143b-8178-b066ca8c0001', '4028c2925f5175c5015f51eb9c770001', 'REACT_THOB_WEB', '三重一大网眼监督平台WEB');
INSERT INTO `application_module` VALUES ('c0a84406-7217-163a-8172-6ec3c2b00003', '4028c2925f5175c5015f51eb9c770001', 'OFFICIAL_API', '官网API');
INSERT INTO `application_module` VALUES ('c0a84406-7217-163a-8172-6ec419a30004', '4028c2925f5175c5015f51eb9c770001', 'OFFICIAL_REACT', '官网React');
INSERT INTO `application_module` VALUES ('c0a84408-6661-1b3f-8167-594d0df202b2', '4028c2925f5175c5015f51eb9c770001', 'EDM_API', '附件管理');
INSERT INTO `application_module` VALUES ('c0a84409-6974-110c-8169-b7f5d00c0047', '4028c2925f5175c5015f51eb9c770001', 'FSOP_EAMS_REACT', '财务共享运营平台--电子档案管理系统react-web');
INSERT INTO `application_module` VALUES ('c0a8440a-710e-1aee-8171-0f454f2d0000', '4028c2925f5175c5015f51eb9c770001', 'NOTIFY_API', '通告API');
INSERT INTO `application_module` VALUES ('c0a8440a-710e-1aee-8171-0f45d8660001', '4028c2925f5175c5015f51eb9c770001', 'NOTIFY_REACT', '通告React');
INSERT INTO `application_module` VALUES ('c0a8440a-7170-13c7-8171-72d92d5e000b', '4028c2925f5175c5015f51eb9c770001', 'NOTICE_API', '公告API');
INSERT INTO `application_module` VALUES ('c0a8440a-7170-13c7-8171-72d976b7000c', '4028c2925f5175c5015f51eb9c770001', 'NOTICE_REACT', '公告React');
INSERT INTO `application_module` VALUES ('c0a8440e-6c41-103e-816c-4156cecb0003', '4028c2925f5175c5015f51eb9c770001', 'FSOP_AMS_REACT', '财务共享运营平台--档案管理系统React');
INSERT INTO `application_module` VALUES ('c0a8440e-6c41-103e-816c-4158a0f40007', '4028c2925f5175c5015f51eb9c770001', 'IAS_API', '发票管理API');
INSERT INTO `application_module` VALUES ('c0a8440e-6c41-103e-816c-4158d6980008', '4028c2925f5175c5015f51eb9c770001', 'IAS_WEB', '发票管理WEB');
INSERT INTO `application_module` VALUES ('c0a8440e-6c41-1c3b-816c-421e0fea0000', '4028c2925f5175c5015f51eb9c770001', 'BASIC_WEB', '基础应用WEB');
INSERT INTO `application_module` VALUES ('c0a8440e-6c4a-177a-816c-92e2f983000d', '4028c2925f5175c5015f51eb9c770001', 'fsop_ams', 'ams测试');
INSERT INTO `application_module` VALUES ('c0a8440f-7863-1a35-8178-86ccc66f0004', '4028c2925f5175c5015f51eb9c770001', 'MAJOREVENT_API', '三重一大网眼监督平台');
INSERT INTO `application_module` VALUES ('c0a8440f-7863-1a35-8178-906ded320019', '4028c2925f5175c5015f51eb9c770001', 'THOB_API', '三重一大网眼监督平台');
INSERT INTO `application_module` VALUES ('c0a84410-67de-137b-8168-601c93890007', '4028c2925f5175c5015f51eb9c770001', 'REACT_BASIC_WEB', '基础应用WEB');
INSERT INTO `application_module` VALUES ('c0a84410-67de-137b-8168-9dd623b20013', '4028c2925f5175c5015f51eb9c770001', 'REACT_TASK_WEB', '后台作业WEB');
INSERT INTO `application_module` VALUES ('c0a84410-67de-137b-8168-eec0519b001d', '4028c2925f5175c5015f51eb9c770001', 'REACT_FLOW_WEB', '工作流WEB');
INSERT INTO `application_module` VALUES ('c0a84421-696a-1c13-8169-706f05700033', '4028c2925f5175c5015f51eb9c770001', 'AUTH_API', '平台认证中心');

-- ----------------------------
-- Table structure for application_service
-- ----------------------------
DROP TABLE IF EXISTS `application_service`;
CREATE TABLE `application_service`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Id标识',
  `app_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用标识',
  `application_module_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用模块Id',
  `runtime_environment_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '运行环境Id',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用服务说明',
  `api_docs_url` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'API文档地址',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_app_ser_app_id`(`app_id`) USING BTREE,
  UNIQUE INDEX `uk_app_ser_mod_run_env`(`application_module_id`, `runtime_environment_id`) USING BTREE,
  INDEX `idx_app_ser_app_mod_id`(`application_module_id`) USING BTREE,
  INDEX `idx_app_ser_run_env_id`(`runtime_environment_id`) USING BTREE,
  CONSTRAINT `application_service_ibfk_1` FOREIGN KEY (`application_module_id`) REFERENCES `application_module` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `application_service_ibfk_2` FOREIGN KEY (`runtime_environment_id`) REFERENCES `runtime_environment` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '应用服务' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of application_service
-- ----------------------------
INSERT INTO `application_service` VALUES ('c0a84203-6497-1b84-8164-a3955eac0142', '2087BCD7-EE36-6C91-876F-764D52380ABD', 'c0a84212-5f87-13e3-815f-87824f610000', 'c0a84203-6497-1b84-8164-a253d0fc0013', '后台作业API（task-service）', 'http://116.169.61.178:19008/task-service/api-docs?/url=/task-service/swagger.json#/');
INSERT INTO `application_service` VALUES ('c0a84203-6497-1b84-8164-a39737a70146', 'B80330B4-3A0C-6B3A-5477-73A067EA93CC', '4028c2925f520f4f015f523589010003', 'c0a84203-6497-1b84-8164-a253d0fc0013', '基础应用API（basic-service）', 'http://116.169.61.178:19000/basic-service/api-docs?/url=/basic-service/swagger.json#/');
INSERT INTO `application_service` VALUES ('c0a84203-6497-1b84-8164-a397fba50148', '9F46523E-0B07-62A8-E1AD-6F21E5F2C8A9', 'c0a84203-631f-17fa-8163-a59669270118', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'API网关', '');
INSERT INTO `application_service` VALUES ('c0a84203-6497-1b84-8164-a3984cee0149', '8F9EA8C5-F325-11E9-BBC2-0242C0A8440A', 'c0a84212-5f7c-131f-815f-7fd1821f0015', 'c0a84203-6497-1b84-8164-a253d0fc0013', '工作流API（flow-service）', 'http://116.169.61.178:19006/flow-service/api-docs?/url=/flow-service/swagger.json#/');
INSERT INTO `application_service` VALUES ('c0a84203-6497-1b84-8164-a398d397014a', '9FD6BCC1-ED69-11E9-B181-0242C0A8440A', 'c0a84212-5f7c-131f-815f-7fd1c9b30016', 'c0a84203-6497-1b84-8164-a253d0fc0013', '工作流WEB（flow-web）', '');
INSERT INTO `application_service` VALUES ('c0a84402-7688-1e64-8176-d56120b50001', '00E27C88-D6BC-11EA-BA21-02F48DB7D23B', 'c0a84402-7688-1e64-8176-d56084380000', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'sa8000和csr认证', '');
INSERT INTO `application_service` VALUES ('c0a84402-7916-1bea-8179-ad1752ac0006', 'B3D9D968-C8EF-D5D4-034D-1625D3D5D38F', 'c0a84402-7916-1bea-8179-acfeaacc0005', 'c0a84203-6497-1b84-8164-a253d0fc0013', '支付宝,微信,钉钉等三方集成api', '');
INSERT INTO `application_service` VALUES ('c0a84403-7770-13d6-8177-bd171e0c0001', '5EE9C87D-731C-11EB-8241-ACDE48001122', 'c0a84403-7770-13d6-8177-bd0a53030000', 'c0a84203-6497-1b84-8164-a253d0fc0013', '钉钉集成模块', 'http://116.169.61.178:20206/dingtalk-service/api-docs?/url=/dingtalk-service/swagger.json#/');
INSERT INTO `application_service` VALUES ('c0a84403-7bec-1054-817b-ed4d054a0002', 'EB4F8A26-976F-4984-A93D-1D861035314F', 'c0a84403-7bec-1054-817b-ed48d0130000', 'c0a84203-6497-1b84-8164-a253d0fc0013', '干部专职化管理-服务端', 'http://116.169.61.178:20211/cadres-service/api-docs?/url=/cadres-service/swagger.json#/');
INSERT INTO `application_service` VALUES ('c0a84406-7217-163a-8172-6ec4c9bb0005', '6AD8D4D6-8728-48E1-8716-8E6F467FF2DC', 'c0a84406-7217-163a-8172-6ec3c2b00003', 'c0a84203-6497-1b84-8164-a253d0fc0013', '官网API', 'http://116.169.61.178:20204/official-service/api-docs?/url=/official-service/swagger.json#/');
INSERT INTO `application_service` VALUES ('c0a84408-6661-1b3f-8167-594d9f7a02b3', '64A100B4-1BB4-6AA3-91B4-EA7BC6127C3A', 'c0a84408-6661-1b3f-8167-594d0df202b2', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'edm 在线预览服务', 'http://116.169.61.178:18016/edm-service/index');
INSERT INTO `application_service` VALUES ('c0a8440a-710e-1aee-8171-0f478edd0002', 'BEF717F7-5F78-43CC-96D3-C2F259FCD8B1', 'c0a8440a-710e-1aee-8171-0f454f2d0000', 'c0a84203-6497-1b84-8164-a253d0fc0013', '通告API', 'http://116.169.61.178:20001/notify-service/api-docs?/url=/notify-service/swagger.json#/');
INSERT INTO `application_service` VALUES ('c0a8440a-7170-13c7-8171-72db5dcd000d', '17AB55A6-2C75-4EBE-BA51-785FF8D2D834', 'c0a8440a-7170-13c7-8171-72d92d5e000b', 'c0a84203-6497-1b84-8164-a253d0fc0013', '公告API', 'http://116.169.61.178:20203/notice-service/api-docs?/url=/notice-service/swagger.json#/');
INSERT INTO `application_service` VALUES ('c0a8440b-64a9-14f2-8164-af2c258d0052', 'DA6FCBC6-D47D-6573-0DD0-9E7CC7335948', 'c0a84203-631f-17fa-8163-de005c1a02f1', 'c0a84203-6497-1b84-8164-a253d0fc0013', '监控中心API（mc-service）', '');
INSERT INTO `application_service` VALUES ('c0a8440e-6c41-103e-816c-4159ea2c0009', 'FDF0A745-754D-11E9-A9C4-0242C0A84306', 'c0a8440e-6c41-103e-816c-4158a0f40007', 'c0a84203-6497-1b84-8164-a253d0fc0013', '发票管理系统API(ias-service)', 'http://172.18.3.80:19307/ias-service/swagger-ui.html');
INSERT INTO `application_service` VALUES ('c0a8440e-6c41-1c3b-816c-421ee2340001', '95E74E9C-7A59-697E-DDD1-F6ED954650C8', 'c0a8440e-6c41-1c3b-816c-421e0fea0000', 'c0a84203-6497-1b84-8164-a253d0fc0013', '基础应用WEB', '');
INSERT INTO `application_service` VALUES ('c0a8440f-7863-1a35-8178-906fd5f9001a', 'D95FD96D-550E-35E8-230E-8BBDD72325F9', 'c0a8440f-7863-1a35-8178-906ded320019', 'c0a84203-6497-1b84-8164-a253d0fc0013', '三重一大网眼监督平台(thob-service)', 'http://116.169.61.178:20210/thob-service/api-docs?/url=/thob-service/swagger.json#/');
INSERT INTO `application_service` VALUES ('c0a84421-696a-1c13-8169-707040470034', '4B0D8A71-1270-4AD0-A92E-8E2B76CBFB18', 'c0a84421-696a-1c13-8169-706f05700033', 'c0a84203-6497-1b84-8164-a253d0fc0013', '平台认证中心', 'http://116.169.61.178:19001/auth-service/swagger-ui.html#/');

-- ----------------------------
-- Table structure for environment_var_config
-- ----------------------------
DROP TABLE IF EXISTS `environment_var_config`;
CREATE TABLE `environment_var_config`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Id标识',
  `environment_variable_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '环境变量Id',
  `runtime_environment_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '运行环境Id',
  `config_value` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置值',
  `encrypted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否加密',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_env_var_con_var_run`(`environment_variable_id`, `runtime_environment_id`) USING BTREE,
  INDEX `idx_env_var_con_env_var_id`(`environment_variable_id`) USING BTREE,
  INDEX `idx_env_var_con_run_env_id`(`runtime_environment_id`) USING BTREE,
  CONSTRAINT `environment_var_config_ibfk_1` FOREIGN KEY (`environment_variable_id`) REFERENCES `environment_variable` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `environment_var_config_ibfk_2` FOREIGN KEY (`runtime_environment_id`) REFERENCES `runtime_environment` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '环境变量配置' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of environment_var_config
-- ----------------------------
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d1440014', '4028c2925f57a1ed015f57a56cc40000', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'com.mysql.jdbc.Driver', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d145001c', 'c0a84203-631f-17fa-8163-268435ff000a', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.155.253:9300', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d145001d', 'c0a84203-631f-17fa-8163-26849ad90010', 'c0a84203-6497-1b84-8164-a253d0fc0013', '', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d1500068', 'c0a84203-631f-17fa-8163-d92c157102df', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.159.61:19003', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d1500069', 'c0a84203-631f-17fa-8163-d92d8f7e02e5', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.159.61:19002', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d150006b', 'c0a84203-631f-17fa-8163-de07116302f4', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.159.61:19012', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d150006c', 'c0a84203-631f-17fa-8163-ed65583f02fd', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.155.253', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d150006d', 'c0a84203-631f-17fa-8163-ed6680a70303', 'c0a84203-6497-1b84-8164-a253d0fc0013', '24224', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d150006e', 'c0a84203-631f-17fa-8163-ef2b5c10030c', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.159.61:20202', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d151007b', 'c0a84203-641c-13ee-8164-201f0ea00031', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'Yc6ElTTu7p92enS', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d151007c', 'c0a84203-641c-13ee-8164-201fb11a0037', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'gateway_siud', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d151007d', 'c0a84203-641c-13ee-8164-2023b47a003d', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.155.116:3306', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d15500b7', 'c0a84211-6208-19cb-8162-2352d33c007a', 'c0a84203-6497-1b84-8164-a253d0fc0013', '172.18.3.80:19305', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d15500b8', 'c0a84211-6208-19cb-8162-2354a4e60080', 'c0a84203-6497-1b84-8164-a253d0fc0013', '10.0.0.7:3306', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d15500b9', 'c0a84211-6208-19cb-8162-235dc5b40088', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'ppAIvuxRF4FFHMD', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d15500ba', 'c0a84211-6208-19cb-8162-235e6fc0008e', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'fsop_ams_siud', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d15500bb', 'c0a84211-6208-19cb-8162-280afff500a2', 'c0a84203-6497-1b84-8164-a253d0fc0013', '172.18.3.80:19306', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d15500bc', 'c0a84211-6208-19cb-8162-280bbfab00a8', 'c0a84203-6497-1b84-8164-a253d0fc0013', '10.0.0.7:3306', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d15500bd', 'c0a84211-6208-19cb-8162-280c841500ae', 'c0a84203-6497-1b84-8164-a253d0fc0013', '18wbEbBqPapnrYB', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d15500be', 'c0a84211-6208-19cb-8162-280d1aed00b4', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'fsop_eams_siud', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d15800e7', 'c0a84211-6208-19cb-8162-d31c8eb702d0', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.159.61:20202', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d15900f5', 'c0a84212-5f7a-1b2a-815f-7a74f25d0000', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'root', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d15900f6', 'c0a84212-5f7a-1b2a-815f-7a759bdc0004', 'c0a84203-6497-1b84-8164-a253d0fc0013', '8%3BJ07SS$#2GWea', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d15900f7', 'c0a84212-5f7a-1b2a-815f-7a7a0cb30008', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.155.253', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d15900f8', 'c0a84212-5f7a-1b2a-815f-7a7ae7d4000c', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'gKnouKDyVE7E4yOyv7EbnFfeRejesQnJyES3iTyDrVSmwhow59', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d15900f9', 'c0a84212-5f7a-1b2a-815f-7a828e830012', 'c0a84203-6497-1b84-8164-a253d0fc0013', '6379', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d15900fd', 'c0a84212-5f7c-131f-815f-7f4933650001', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.155.253:9092', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d15900fe', 'c0a84212-5f7c-131f-815f-7fbd2bca000a', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'SEI业务协作平台3.0-开发系统', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d1590100', 'c0a84212-5f7c-131f-815f-804f79a70019', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.159.61:19006', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d15a0102', 'c0a84212-5f80-12ae-815f-80a8db010022', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.155.116:3306', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d15a0103', 'c0a84212-5f80-12ae-815f-80aa5637002a', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'kTLSJ8BLAOcCrwbt', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d15a0104', 'c0a84212-5f80-12ae-815f-80ab4af1002e', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'flow_siud', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d15a0108', 'c0a84212-5f86-1e25-815f-873b326c0000', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.155.116:3306', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d15b0117', 'c0a84212-5f8f-111c-815f-8fd5cad20000', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.159.61:19008', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d15b0118', 'c0a84212-5f8f-111c-815f-8fee899a0009', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.155.116:3306', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d15b0119', 'c0a84212-5f8f-111c-815f-8ff085a80015', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'kmqdDPXFYL3RE6k', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d15b011a', 'c0a84212-5f8f-111c-815f-8ff1152a001d', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'task_siud', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d15d0127', 'c0a84212-5f90-1ef5-815f-907ff9f4000d', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.159.61:19000', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d15e0130', 'c0a84212-5f94-1e7d-815f-95882fcd0004', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'VgVjUqplkRZGqR6o1DfRCmEogsioyzP3', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d15e0131', 'c0a84212-5f94-1e7d-815f-958a56c40008', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.155.253:27017', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84203-6497-1b84-8164-a253d15e0132', 'c0a84212-5f94-1e7d-815f-958c300c000c', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'edm_dev', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84402-7916-1bea-8179-64ab51e80003', 'c0a84402-7916-1bea-8179-64ab51da0002', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.219.123:20202', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84403-77bd-1050-8177-bd62c39c0001', 'c0a84403-77bd-1050-8177-bd62c3680000', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.155.116:3306', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84403-77bd-1050-8177-bd6449000003', 'c0a84403-77bd-1050-8177-bd6448fa0002', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'fwIU55lFgf1T5es', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84403-77bd-1050-8177-bd65de1c0005', 'c0a84403-77bd-1050-8177-bd65de130004', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'dingtalk_suid', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84403-7bec-1054-817b-f1557dc5000d', 'c0a84403-7bec-1054-817b-f1557db5000c', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'cadres123', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84403-7bec-1054-817b-f155bcfc000f', 'c0a84403-7bec-1054-817b-f155bcf5000e', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.155.116:3306', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84403-7bec-1054-817b-f155e5f40011', 'c0a84403-7bec-1054-817b-f155e5ed0010', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'cadres_suid', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84405-78af-143b-8178-b46701670008', 'c0a84405-78af-143b-8178-b467015d0007', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.219.123:20202', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84405-78af-143b-8178-b55f51d3000b', 'c0a84405-78af-143b-8178-b55f51cd000a', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.159.61:20210', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84406-7217-163a-8172-257daad40002', 'c0a84406-7217-163a-8172-257daabf0001', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.159.61:20202', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84406-7217-163a-8172-6ec762590007', 'c0a84406-7217-163a-8172-6ec762500006', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.159.61:20202', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84406-7217-163a-8172-6ec8a6310009', 'c0a84406-7217-163a-8172-6ec8a6260008', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.159.61:20204', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84406-7217-163a-8172-6ec915ba000b', 'c0a84406-7217-163a-8172-6ec915b1000a', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.155.116:3306', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84406-7217-163a-8172-6ec9ad4b000d', 'c0a84406-7217-163a-8172-6ec9ad41000c', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'ecmp_official', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84406-7217-163a-8172-6eca37d4000f', 'c0a84406-7217-163a-8172-6eca37cc000e', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'AGy0OAprflt6XRc2', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84406-7217-163a-8172-6ecac14e0011', 'c0a84406-7217-163a-8172-6ecac1450010', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'official_siud', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84406-7217-163a-8172-cb6796970019', 'c0a84406-7217-163a-8172-cb6796900018', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'ecmp_task', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84408-6661-1b3f-8167-720658cc02d7', 'c0a84408-6661-1b3f-8167-720658bb02d4', 'c0a84203-6497-1b84-8164-a253d0fc0013', '', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84409-6974-110c-8169-7a74e5850007', 'c0a84409-6974-110c-8169-7a74e5770004', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.159.61:19001', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84409-6974-110c-8169-b7f8b53c004b', 'c0a84409-6974-110c-8169-b7f8b5370048', 'c0a84203-6497-1b84-8164-a253d0fc0013', '172.18.3.80:18012', 0);
INSERT INTO `environment_var_config` VALUES ('c0a8440a-7170-13c7-8171-71311e970002', 'c0a8440a-7170-13c7-8171-71311e8b0001', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'notify_siud', 0);
INSERT INTO `environment_var_config` VALUES ('c0a8440a-7170-13c7-8171-71319fce0004', 'c0a8440a-7170-13c7-8171-71319fc30003', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'VaZtZEGMzPXuvswO', 0);
INSERT INTO `environment_var_config` VALUES ('c0a8440a-7170-13c7-8171-7131db900006', 'c0a8440a-7170-13c7-8171-7131db870005', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'ecmp_notify', 0);
INSERT INTO `environment_var_config` VALUES ('c0a8440a-7170-13c7-8171-7131ed320008', 'c0a8440a-7170-13c7-8171-7131ed2c0007', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.155.116:3306', 0);
INSERT INTO `environment_var_config` VALUES ('c0a8440a-7170-13c7-8171-7131fe6c000a', 'c0a8440a-7170-13c7-8171-7131fe520009', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.159.61:20001', 0);
INSERT INTO `environment_var_config` VALUES ('c0a8440a-7170-13c7-8171-72dcb1c0000f', 'c0a8440a-7170-13c7-8171-72dcb1b3000e', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.159.61:20203', 0);
INSERT INTO `environment_var_config` VALUES ('c0a8440a-7170-13c7-8171-72dcf4f10011', 'c0a8440a-7170-13c7-8171-72dcf4e80010', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.155.116:3306', 0);
INSERT INTO `environment_var_config` VALUES ('c0a8440a-7170-13c7-8171-72dd53540013', 'c0a8440a-7170-13c7-8171-72dd534a0012', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'ecmp_notice', 0);
INSERT INTO `environment_var_config` VALUES ('c0a8440a-7170-13c7-8171-72dd94bc0015', 'c0a8440a-7170-13c7-8171-72dd94b00014', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'VaZtZEGMzPXuvswO', 0);
INSERT INTO `environment_var_config` VALUES ('c0a8440a-7170-13c7-8171-72dde5070017', 'c0a8440a-7170-13c7-8171-72dde4f40016', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'notice_siud', 0);
INSERT INTO `environment_var_config` VALUES ('c0a8440e-6c41-103e-816c-41573c3b0005', 'c0a8440e-6c41-103e-816c-41573c370004', 'c0a84203-6497-1b84-8164-a253d0fc0013', '172.18.3.80:18012', 0);
INSERT INTO `environment_var_config` VALUES ('c0a8440e-6c41-103e-816c-415ac9b3000b', 'c0a8440e-6c41-103e-816c-415ac9b0000a', 'c0a84203-6497-1b84-8164-a253d0fc0013', '172.18.3.81:3306', 0);
INSERT INTO `environment_var_config` VALUES ('c0a8440e-6c41-103e-816c-415b4436000d', 'c0a8440e-6c41-103e-816c-415b4432000c', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'm462wAj5YS3cAV4j', 0);
INSERT INTO `environment_var_config` VALUES ('c0a8440e-6c41-103e-816c-415bf674000f', 'c0a8440e-6c41-103e-816c-415bf670000e', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'ias_siud', 0);
INSERT INTO `environment_var_config` VALUES ('c0a8440e-6c41-103e-816c-415e302f0011', 'c0a8440e-6c41-103e-816c-415e302b0010', 'c0a84203-6497-1b84-8164-a253d0fc0013', '172.18.3.80:19307', 0);
INSERT INTO `environment_var_config` VALUES ('c0a8440e-6c41-103e-816c-415f00350013', 'c0a8440e-6c41-103e-816c-415f00320012', 'c0a84203-6497-1b84-8164-a253d0fc0013', '172.18.3.80:18012', 0);
INSERT INTO `environment_var_config` VALUES ('c0a8440e-6c41-1c3b-816c-45a73ba10003', 'c0a8440e-6c41-1c3b-816c-45a73b990002', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.159.61:20202', 0);
INSERT INTO `environment_var_config` VALUES ('c0a8440f-7863-1a35-8178-7bfab54d0002', 'c0a8440f-7863-1a35-8178-7bfab51c0001', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.159.61:20206', 0);
INSERT INTO `environment_var_config` VALUES ('c0a8440f-7863-1a35-8178-877762430010', 'c0a8440f-7863-1a35-8178-87776237000f', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'majorevent_suid', 0);
INSERT INTO `environment_var_config` VALUES ('c0a8440f-7863-1a35-8178-877a20eb0014', 'c0a8440f-7863-1a35-8178-877a20e20013', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.175.12:3306', 0);
INSERT INTO `environment_var_config` VALUES ('c0a8440f-7863-1a35-8178-87895b670018', 'c0a8440f-7863-1a35-8178-87895b5c0017', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'qUidNAwJLiUJ8y3g', 0);
INSERT INTO `environment_var_config` VALUES ('c0a8440f-7863-1a35-8178-907168f6001c', 'c0a8440f-7863-1a35-8178-907168f1001b', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.155.116:3306', 0);
INSERT INTO `environment_var_config` VALUES ('c0a8440f-7863-1a35-8178-9072abbc001e', 'c0a8440f-7863-1a35-8178-9072abb3001d', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'thob_suid', 0);
INSERT INTO `environment_var_config` VALUES ('c0a8440f-7863-1a35-8178-9073d8b60020', 'c0a8440f-7863-1a35-8178-9073d8b1001f', 'c0a84203-6497-1b84-8164-a253d0fc0013', 'dm3KqIQNfUrJ5LDL', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84410-67de-137b-8168-9dd8b70f0017', 'c0a84410-67de-137b-8168-9dd8b6fa0014', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.159.61:20202', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84410-67de-137b-8168-eec5637a0026', 'c0a84410-67de-137b-8168-eec563760023', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.159.61:20202', 0);
INSERT INTO `environment_var_config` VALUES ('c0a84414-6ad5-1324-816b-50104fb60004', 'c0a84414-6ad5-1324-816b-50104f890001', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.159.61:18016', 0);
INSERT INTO `environment_var_config` VALUES ('c0a85607-7c20-116d-817c-210267120003', 'c0a85607-7c20-116d-817c-210266fc0002', 'c0a84203-6497-1b84-8164-a253d0fc0013', '211.149.159.61:20211', 0);

-- ----------------------------
-- Table structure for environment_variable
-- ----------------------------
DROP TABLE IF EXISTS `environment_variable`;
CREATE TABLE `environment_variable`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Id标识',
  `platform_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '平台Id',
  `application_module_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用模块Id',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '代码',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_plat_code`(`platform_id`, `code`) USING BTREE,
  INDEX `idx_env_var_app_mod_id`(`application_module_id`) USING BTREE,
  INDEX `idx_env_var_code`(`code`) USING BTREE,
  INDEX `idx_env_var_platform_id`(`platform_id`) USING BTREE,
  CONSTRAINT `environment_variable_ibfk_1` FOREIGN KEY (`application_module_id`) REFERENCES `application_module` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `environment_variable_ibfk_2` FOREIGN KEY (`platform_id`) REFERENCES `platform` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '环境变量' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of environment_variable
-- ----------------------------
INSERT INTO `environment_variable` VALUES ('4028c2925f57a1ed015f57a56cc40000', '4028c2925f5175c5015f51eb9c770001', NULL, 'JDBC_MYSQL_DRIVER', 'ECMP_MYSQL数据库驱动器');
INSERT INTO `environment_variable` VALUES ('c0a84203-631f-17fa-8163-268435ff000a', '4028c2925f5175c5015f51eb9c770001', NULL, 'ECMP_ELASTIC_SEARCH_HOST', '平台Elasticsearch服务器');
INSERT INTO `environment_variable` VALUES ('c0a84203-631f-17fa-8163-26849ad90010', '4028c2925f5175c5015f51eb9c770001', NULL, 'ECMP_ELASTIC_SEARCH_PORT', '平台Elasticsearch端口');
INSERT INTO `environment_variable` VALUES ('c0a84203-631f-17fa-8163-d92c157102df', '4028c2925f5175c5015f51eb9c770001', NULL, 'API_GATEWAY_HOST', 'API网关Router服务(gateway-router-server)基地址');
INSERT INTO `environment_variable` VALUES ('c0a84203-631f-17fa-8163-d92d8f7e02e5', '4028c2925f5175c5015f51eb9c770001', NULL, 'CONFIG_CENTER_API_HOST', '配置中心API基地址');
INSERT INTO `environment_variable` VALUES ('c0a84203-631f-17fa-8163-de07116302f4', '4028c2925f5175c5015f51eb9c770001', 'c0a84203-631f-17fa-8163-de005c1a02f1', 'MC_API_HOST', '监控中心API服务器');
INSERT INTO `environment_variable` VALUES ('c0a84203-631f-17fa-8163-ed65583f02fd', '4028c2925f5175c5015f51eb9c770001', NULL, 'ECMP_FLUENTD_HOST', '平台Fluentd服务器');
INSERT INTO `environment_variable` VALUES ('c0a84203-631f-17fa-8163-ed6680a70303', '4028c2925f5175c5015f51eb9c770001', NULL, 'ECMP_FLUENTD_PORT', '平台Fluentd端口');
INSERT INTO `environment_variable` VALUES ('c0a84203-631f-17fa-8163-ef2b5c10030c', '4028c2925f5175c5015f51eb9c770001', NULL, 'STATIC.RESOURCE.URL_HOST', '静态资源服务器基地址');
INSERT INTO `environment_variable` VALUES ('c0a84203-641c-13ee-8164-201f0ea00031', '4028c2925f5175c5015f51eb9c770001', 'c0a84203-631f-17fa-8163-a59669270118', 'ECMP_GATEWAY_DB_PASSWORD', '网关数据库密码');
INSERT INTO `environment_variable` VALUES ('c0a84203-641c-13ee-8164-201fb11a0037', '4028c2925f5175c5015f51eb9c770001', 'c0a84203-631f-17fa-8163-a59669270118', 'ECMP_GATEWAY_DB_USER', '网关数据库用户名');
INSERT INTO `environment_variable` VALUES ('c0a84203-641c-13ee-8164-2023b47a003d', '4028c2925f5175c5015f51eb9c770001', 'c0a84203-631f-17fa-8163-a59669270118', 'ECMP_GATEWAY_DB_HOST', '网关数据库地址');
INSERT INTO `environment_variable` VALUES ('c0a84211-6208-19cb-8162-2352d33c007a', '4028c2925f5175c5015f51eb9c770001', 'c0a84211-6208-19cb-8162-2347677e0078', 'FSOP_AMS_API_HOST', '财务共享运营平台-档案管理系统API服务器');
INSERT INTO `environment_variable` VALUES ('c0a84211-6208-19cb-8162-2354a4e60080', '4028c2925f5175c5015f51eb9c770001', 'c0a84211-6208-19cb-8162-2347677e0078', 'FSOP_AMS_DB_HOST', '财务共享运营平台 -档案管理系统数据库服务器');
INSERT INTO `environment_variable` VALUES ('c0a84211-6208-19cb-8162-235dc5b40088', '4028c2925f5175c5015f51eb9c770001', 'c0a84211-6208-19cb-8162-2347677e0078', 'FSOP_AMS_DB_PASSWORD', '财务共享运营平台-档案管理系统数据库密码');
INSERT INTO `environment_variable` VALUES ('c0a84211-6208-19cb-8162-235e6fc0008e', '4028c2925f5175c5015f51eb9c770001', 'c0a84211-6208-19cb-8162-2347677e0078', 'FSOP_AMS_DB_USER', '财务共享运营平台-档案管理系统数据库账号');
INSERT INTO `environment_variable` VALUES ('c0a84211-6208-19cb-8162-280afff500a2', '4028c2925f5175c5015f51eb9c770001', 'c0a84211-6208-19cb-8162-2801c899009c', 'FSOP_EAMS_API_HOST', '财务共享运营平台-电子档案管理系统API服务器');
INSERT INTO `environment_variable` VALUES ('c0a84211-6208-19cb-8162-280bbfab00a8', '4028c2925f5175c5015f51eb9c770001', 'c0a84211-6208-19cb-8162-2801c899009c', 'FSOP_EAMS_DB_HOST', '财务共享运营平台-电子档案管理系统数据库服务器');
INSERT INTO `environment_variable` VALUES ('c0a84211-6208-19cb-8162-280c841500ae', '4028c2925f5175c5015f51eb9c770001', 'c0a84211-6208-19cb-8162-2801c899009c', 'FSOP_EAMS_DB_PASSWORD', '财务共享运营平台-电子档案管理数据库密码');
INSERT INTO `environment_variable` VALUES ('c0a84211-6208-19cb-8162-280d1aed00b4', '4028c2925f5175c5015f51eb9c770001', 'c0a84211-6208-19cb-8162-2801c899009c', 'FSOP_EAMS_DB_USER', '财务共享运营平台-档案管理系统数据库账号');
INSERT INTO `environment_variable` VALUES ('c0a84211-6208-19cb-8162-d31c8eb702d0', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1c9b30016', 'FLOW_WEB_HOST', '流程WEB基地址');
INSERT INTO `environment_variable` VALUES ('c0a84212-5f7a-1b2a-815f-7a74f25d0000', '4028c2925f5175c5015f51eb9c770001', '4028c2925f520f4f015f523589010003', 'ECMP_BASIC_DB_USER', '基础应用数据库用户名');
INSERT INTO `environment_variable` VALUES ('c0a84212-5f7a-1b2a-815f-7a759bdc0004', '4028c2925f5175c5015f51eb9c770001', '4028c2925f520f4f015f523589010003', 'ECMP_BASIC_DB_PASSWORD', '基础应用数据库密码');
INSERT INTO `environment_variable` VALUES ('c0a84212-5f7a-1b2a-815f-7a7a0cb30008', '4028c2925f5175c5015f51eb9c770001', NULL, 'ECMP_REDIS_HOST', 'Redis缓存服务器（单节点）');
INSERT INTO `environment_variable` VALUES ('c0a84212-5f7a-1b2a-815f-7a7ae7d4000c', '4028c2925f5175c5015f51eb9c770001', NULL, 'ECMP_REDIS_PASSWORD', 'Redis缓存密码');
INSERT INTO `environment_variable` VALUES ('c0a84212-5f7a-1b2a-815f-7a828e830012', '4028c2925f5175c5015f51eb9c770001', NULL, 'ECMP_REDIS_PORT', 'Redis缓存端口（单节点）');
INSERT INTO `environment_variable` VALUES ('c0a84212-5f7c-131f-815f-7f4933650001', '4028c2925f5175c5015f51eb9c770001', NULL, 'ECMP_KAFKA_HOST', 'ECMP平台kafka消息服务器');
INSERT INTO `environment_variable` VALUES ('c0a84212-5f7c-131f-815f-7fbd2bca000a', '4028c2925f5175c5015f51eb9c770001', NULL, 'ECMP_NAME', '平台运行环境名称');
INSERT INTO `environment_variable` VALUES ('c0a84212-5f7c-131f-815f-804f79a70019', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1821f0015', 'FLOW_API_HOST', '业务流程API服务器');
INSERT INTO `environment_variable` VALUES ('c0a84212-5f80-12ae-815f-80a8db010022', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1821f0015', 'FLOW_DB_HOST', '工作流数据库服务器');
INSERT INTO `environment_variable` VALUES ('c0a84212-5f80-12ae-815f-80aa5637002a', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1821f0015', 'FLOW_DB_PASSWORD', '业务流程数据库密码');
INSERT INTO `environment_variable` VALUES ('c0a84212-5f80-12ae-815f-80ab4af1002e', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1821f0015', 'FLOW_DB_USER', '业务流程数据库账号');
INSERT INTO `environment_variable` VALUES ('c0a84212-5f86-1e25-815f-873b326c0000', '4028c2925f5175c5015f51eb9c770001', '4028c2925f520f4f015f523589010003', 'ECMP_BASIC_DB_HOST', '基础应用数据库服务器');
INSERT INTO `environment_variable` VALUES ('c0a84212-5f8f-111c-815f-8fd5cad20000', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f87-13e3-815f-87824f610000', 'TASK_API_HOST', '后台作业API服务器');
INSERT INTO `environment_variable` VALUES ('c0a84212-5f8f-111c-815f-8fee899a0009', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f87-13e3-815f-87824f610000', 'TASK_DB_HOST', '后台作业数据库服务器');
INSERT INTO `environment_variable` VALUES ('c0a84212-5f8f-111c-815f-8ff085a80015', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f87-13e3-815f-87824f610000', 'TASK_DB_PASSWORD', '后台作业数据库密码');
INSERT INTO `environment_variable` VALUES ('c0a84212-5f8f-111c-815f-8ff1152a001d', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f87-13e3-815f-87824f610000', 'TASK_DB_USER', '后台作业数据库账号');
INSERT INTO `environment_variable` VALUES ('c0a84212-5f90-1ef5-815f-907ff9f4000d', '4028c2925f5175c5015f51eb9c770001', '4028c2925f520f4f015f523589010003', 'BASIC_API_HOST', '基础应用API服务器');
INSERT INTO `environment_variable` VALUES ('c0a84212-5f94-1e7d-815f-95882fcd0004', '4028c2925f5175c5015f51eb9c770001', NULL, 'ECMP_MONGODB_PASSWORD', 'MongoDB数据库连接密码');
INSERT INTO `environment_variable` VALUES ('c0a84212-5f94-1e7d-815f-958a56c40008', '4028c2925f5175c5015f51eb9c770001', NULL, 'ECMP_MONGODB_HOST', 'MongoDB数据库服务器');
INSERT INTO `environment_variable` VALUES ('c0a84212-5f94-1e7d-815f-958c300c000c', '4028c2925f5175c5015f51eb9c770001', NULL, 'ECMP_MONGODB_USER', 'MongoDB数据库连接用户');
INSERT INTO `environment_variable` VALUES ('c0a84402-7916-1bea-8179-64ab51da0002', '4028c2925f5175c5015f51eb9c770001', 'c0a84402-7916-1bea-8179-64a91b4c0001', 'THOB-MOBILE-REACT-HOST', '移动页面基地址');
INSERT INTO `environment_variable` VALUES ('c0a84403-77bd-1050-8177-bd62c3680000', '4028c2925f5175c5015f51eb9c770001', 'c0a84403-7770-13d6-8177-bd0a53030000', 'DINGTALK_API_DB_HOST', '钉钉集成数据库地址');
INSERT INTO `environment_variable` VALUES ('c0a84403-77bd-1050-8177-bd6448fa0002', '4028c2925f5175c5015f51eb9c770001', 'c0a84403-7770-13d6-8177-bd0a53030000', 'DINGTALK_API_DB_PASSWORD', '钉钉集成数据库密码');
INSERT INTO `environment_variable` VALUES ('c0a84403-77bd-1050-8177-bd65de130004', '4028c2925f5175c5015f51eb9c770001', 'c0a84403-7770-13d6-8177-bd0a53030000', 'DINGTALK_API_DB_USER', '钉钉集成数据库用户名');
INSERT INTO `environment_variable` VALUES ('c0a84403-7bec-1054-817b-f1557db5000c', '4028c2925f5175c5015f51eb9c770001', 'c0a84403-7bec-1054-817b-ed48d0130000', 'CADRES_API_DB_PASSWORD', '干部专职化数据库密码');
INSERT INTO `environment_variable` VALUES ('c0a84403-7bec-1054-817b-f155bcf5000e', '4028c2925f5175c5015f51eb9c770001', 'c0a84403-7bec-1054-817b-ed48d0130000', 'CADRES_API_DB_HOST', '干部专职化数据库地址');
INSERT INTO `environment_variable` VALUES ('c0a84403-7bec-1054-817b-f155e5ed0010', '4028c2925f5175c5015f51eb9c770001', 'c0a84403-7bec-1054-817b-ed48d0130000', 'CADRES_API_DB_USER', '干部专职化数据库用户');
INSERT INTO `environment_variable` VALUES ('c0a84405-78af-143b-8178-b467015d0007', '4028c2925f5175c5015f51eb9c770001', 'c0a84405-78af-143b-8178-b066ca8c0001', 'REACT-THOB-WEB-HOST', '三重一大网眼监督平台WEB地址');
INSERT INTO `environment_variable` VALUES ('c0a84405-78af-143b-8178-b55f51cd000a', '4028c2925f5175c5015f51eb9c770001', 'c0a8440f-7863-1a35-8178-906ded320019', 'THOB_API_HOST', '三重一大服务基地址');
INSERT INTO `environment_variable` VALUES ('c0a84406-7217-163a-8172-257daabf0001', '4028c2925f5175c5015f51eb9c770001', 'c0a8440a-7170-13c7-8171-72d976b7000c', 'NOTICE-REACT-HOST', '公告前端web地址');
INSERT INTO `environment_variable` VALUES ('c0a84406-7217-163a-8172-6ec762500006', '4028c2925f5175c5015f51eb9c770001', 'c0a84406-7217-163a-8172-6ec419a30004', 'OFFICIAL-REACT-HOST', '官网前端web地址');
INSERT INTO `environment_variable` VALUES ('c0a84406-7217-163a-8172-6ec8a6260008', '4028c2925f5175c5015f51eb9c770001', 'c0a84406-7217-163a-8172-6ec3c2b00003', 'OFFICIAL_API_HOST', '官网API服务器');
INSERT INTO `environment_variable` VALUES ('c0a84406-7217-163a-8172-6ec915b1000a', '4028c2925f5175c5015f51eb9c770001', 'c0a84406-7217-163a-8172-6ec3c2b00003', 'OFFICIAL_DB_HOST', '官网数据库服务器');
INSERT INTO `environment_variable` VALUES ('c0a84406-7217-163a-8172-6ec9ad41000c', '4028c2925f5175c5015f51eb9c770001', 'c0a84406-7217-163a-8172-6ec3c2b00003', 'OFFICIAL_DB_NAME', '官网数据库名称');
INSERT INTO `environment_variable` VALUES ('c0a84406-7217-163a-8172-6eca37cc000e', '4028c2925f5175c5015f51eb9c770001', 'c0a84406-7217-163a-8172-6ec3c2b00003', 'OFFICIAL_DB_PASSWORD', '官网数据库密码');
INSERT INTO `environment_variable` VALUES ('c0a84406-7217-163a-8172-6ecac1450010', '4028c2925f5175c5015f51eb9c770001', 'c0a84406-7217-163a-8172-6ec3c2b00003', 'OFFICIAL_DB_USER', '官网数据库用户');
INSERT INTO `environment_variable` VALUES ('c0a84406-7217-163a-8172-cb6796900018', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f87-13e3-815f-87824f610000', 'TASK_DB_NAME', '后台作业数据库名称');
INSERT INTO `environment_variable` VALUES ('c0a84408-6661-1b3f-8167-720658bb02d4', '4028c2925f5175c5015f51eb9c770001', NULL, 'ECMP_REDIS_NODES', 'Redis缓存服务器（集群）');
INSERT INTO `environment_variable` VALUES ('c0a84409-6974-110c-8169-7a74e5770004', '4028c2925f5175c5015f51eb9c770001', 'c0a84421-696a-1c13-8169-706f05700033', 'AUTH_API_HOST', '认证中心API服务器');
INSERT INTO `environment_variable` VALUES ('c0a84409-6974-110c-8169-b7f8b5370048', '4028c2925f5175c5015f51eb9c770001', 'c0a84409-6974-110c-8169-b7f5d00c0047', 'FSOP_EAMS_REACT_HOST', 'REACT电子档案管理系统WEB');
INSERT INTO `environment_variable` VALUES ('c0a8440a-7170-13c7-8171-71311e8b0001', '4028c2925f5175c5015f51eb9c770001', 'c0a8440a-710e-1aee-8171-0f454f2d0000', 'NOTIFY_DB_USER', '通告数据库用户');
INSERT INTO `environment_variable` VALUES ('c0a8440a-7170-13c7-8171-71319fc30003', '4028c2925f5175c5015f51eb9c770001', 'c0a8440a-710e-1aee-8171-0f454f2d0000', 'NOTIFY_DB_PASSWORD', '通告数据库密码');
INSERT INTO `environment_variable` VALUES ('c0a8440a-7170-13c7-8171-7131db870005', '4028c2925f5175c5015f51eb9c770001', 'c0a8440a-710e-1aee-8171-0f454f2d0000', 'NOTIFY_DB_NAME', '通告数据库名称');
INSERT INTO `environment_variable` VALUES ('c0a8440a-7170-13c7-8171-7131ed2c0007', '4028c2925f5175c5015f51eb9c770001', 'c0a8440a-710e-1aee-8171-0f454f2d0000', 'NOTIFY_DB_HOST', '通告数据库服务器');
INSERT INTO `environment_variable` VALUES ('c0a8440a-7170-13c7-8171-7131fe520009', '4028c2925f5175c5015f51eb9c770001', 'c0a8440a-710e-1aee-8171-0f454f2d0000', 'NOTIFY_API_HOST', '通告API服务器');
INSERT INTO `environment_variable` VALUES ('c0a8440a-7170-13c7-8171-72dcb1b3000e', '4028c2925f5175c5015f51eb9c770001', 'c0a8440a-7170-13c7-8171-72d92d5e000b', 'NOTICE_API_HOST', '公告API服务器');
INSERT INTO `environment_variable` VALUES ('c0a8440a-7170-13c7-8171-72dcf4e80010', '4028c2925f5175c5015f51eb9c770001', 'c0a8440a-7170-13c7-8171-72d92d5e000b', 'NOTICE_DB_HOST', '通告数据库服务器');
INSERT INTO `environment_variable` VALUES ('c0a8440a-7170-13c7-8171-72dd534a0012', '4028c2925f5175c5015f51eb9c770001', 'c0a8440a-7170-13c7-8171-72d92d5e000b', 'NOTICE_DB_NAME', '公告数据库名称');
INSERT INTO `environment_variable` VALUES ('c0a8440a-7170-13c7-8171-72dd94b00014', '4028c2925f5175c5015f51eb9c770001', 'c0a8440a-7170-13c7-8171-72d92d5e000b', 'NOTICE_DB_PASSWORD', '公告数据库密码');
INSERT INTO `environment_variable` VALUES ('c0a8440a-7170-13c7-8171-72dde4f40016', '4028c2925f5175c5015f51eb9c770001', 'c0a8440a-7170-13c7-8171-72d92d5e000b', 'NOTICE_DB_USER', '公告数据库用户');
INSERT INTO `environment_variable` VALUES ('c0a8440e-6c41-103e-816c-41573c370004', '4028c2925f5175c5015f51eb9c770001', 'c0a8440e-6c41-103e-816c-4156cecb0003', 'FSOP_AMS_REACT_HOST', '档案管理系统WEB');
INSERT INTO `environment_variable` VALUES ('c0a8440e-6c41-103e-816c-415ac9b0000a', '4028c2925f5175c5015f51eb9c770001', 'c0a8440e-6c41-103e-816c-4158a0f40007', 'IAS_API_DB_HOST', '发票管理--数据库基地址');
INSERT INTO `environment_variable` VALUES ('c0a8440e-6c41-103e-816c-415b4432000c', '4028c2925f5175c5015f51eb9c770001', 'c0a8440e-6c41-103e-816c-4158a0f40007', 'IAS_API_DB_PASSWORD', '发票管理--数据库密码');
INSERT INTO `environment_variable` VALUES ('c0a8440e-6c41-103e-816c-415bf670000e', '4028c2925f5175c5015f51eb9c770001', 'c0a8440e-6c41-103e-816c-4158a0f40007', 'IAS_API_DB_USER', '发票管理--数据库用户名');
INSERT INTO `environment_variable` VALUES ('c0a8440e-6c41-103e-816c-415e302b0010', '4028c2925f5175c5015f51eb9c770001', 'c0a8440e-6c41-103e-816c-4158a0f40007', 'IAS_API_HOST', '发票管理--API基地址');
INSERT INTO `environment_variable` VALUES ('c0a8440e-6c41-103e-816c-415f00320012', '4028c2925f5175c5015f51eb9c770001', 'c0a8440e-6c41-103e-816c-4158d6980008', 'IAS_WEB_HOST', '发票管理--WEB基地址');
INSERT INTO `environment_variable` VALUES ('c0a8440e-6c41-1c3b-816c-45a73b990002', '4028c2925f5175c5015f51eb9c770001', 'c0a8440e-6c41-1c3b-816c-421e0fea0000', 'BASIC_WEB_HOST', '基础应用WEB');
INSERT INTO `environment_variable` VALUES ('c0a8440f-7863-1a35-8178-7bfab51c0001', '4028c2925f5175c5015f51eb9c770001', 'c0a84403-7770-13d6-8177-bd0a53030000', 'DINGTALK_API_HOST', '钉钉集成服务地址');
INSERT INTO `environment_variable` VALUES ('c0a8440f-7863-1a35-8178-87776237000f', '4028c2925f5175c5015f51eb9c770001', 'c0a8440f-7863-1a35-8178-86ccc66f0004', 'MAJOREVENT_API_DB_USER', '三重一大数据库用户');
INSERT INTO `environment_variable` VALUES ('c0a8440f-7863-1a35-8178-877a20e20013', '4028c2925f5175c5015f51eb9c770001', 'c0a8440f-7863-1a35-8178-86ccc66f0004', 'MAJOREVENT_API_DB_HOST', '三重一大数据库地址');
INSERT INTO `environment_variable` VALUES ('c0a8440f-7863-1a35-8178-87895b5c0017', '4028c2925f5175c5015f51eb9c770001', 'c0a8440f-7863-1a35-8178-86ccc66f0004', 'MAJOREVENT_API_DB_PASSWORD', '三重一大数据库密码');
INSERT INTO `environment_variable` VALUES ('c0a8440f-7863-1a35-8178-907168f1001b', '4028c2925f5175c5015f51eb9c770001', 'c0a8440f-7863-1a35-8178-906ded320019', 'THOB_API_DB_HOST', '三重一大数据库地址');
INSERT INTO `environment_variable` VALUES ('c0a8440f-7863-1a35-8178-9072abb3001d', '4028c2925f5175c5015f51eb9c770001', 'c0a8440f-7863-1a35-8178-906ded320019', 'THOB_API_DB_USER', '三重一大数据库用户');
INSERT INTO `environment_variable` VALUES ('c0a8440f-7863-1a35-8178-9073d8b1001f', '4028c2925f5175c5015f51eb9c770001', 'c0a8440f-7863-1a35-8178-906ded320019', 'THOB_API_DB_PASSWORD', '三重一大数据库密码');
INSERT INTO `environment_variable` VALUES ('c0a84410-67de-137b-8168-9dd8b6fa0014', '4028c2925f5175c5015f51eb9c770001', 'c0a84410-67de-137b-8168-9dd623b20013', 'REACT_TASK_WEB_HOST', 'REACT后台作业WEB');
INSERT INTO `environment_variable` VALUES ('c0a84410-67de-137b-8168-eec563760023', '4028c2925f5175c5015f51eb9c770001', 'c0a84410-67de-137b-8168-eec0519b001d', 'REACT_FLOW_WEB_HOST', '流程WEB基地址');
INSERT INTO `environment_variable` VALUES ('c0a84414-6ad5-1324-816b-50104f890001', '4028c2925f5175c5015f51eb9c770001', 'c0a84408-6661-1b3f-8167-594d0df202b2', 'EDM_API_HOST', '附件管理API服务器');
INSERT INTO `environment_variable` VALUES ('c0a85607-7c20-116d-817c-210266fc0002', '4028c2925f5175c5015f51eb9c770001', 'c0a84403-7bec-1054-817b-ed48d0130000', 'CADRES_API_HOST', 'CADRES_REACTAPI服务器');

-- ----------------------------
-- Table structure for global_param_config
-- ----------------------------
DROP TABLE IF EXISTS `global_param_config`;
CREATE TABLE `global_param_config`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Id标识',
  `platform_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '平台Id',
  `application_module_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用模块Id',
  `param_data_type` smallint(6) NOT NULL COMMENT '数据类型',
  `param_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置项的键',
  `param_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '配置项的值',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置参数说明',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_par_val_pla_mod_key`(`platform_id`, `application_module_id`, `param_key`) USING BTREE,
  INDEX `idx_par_val_app_mod_id`(`application_module_id`) USING BTREE,
  INDEX `idx_par_val_param_key`(`param_key`) USING BTREE,
  INDEX `idx_par_val_platform_id`(`platform_id`) USING BTREE,
  CONSTRAINT `global_param_config_ibfk_1` FOREIGN KEY (`application_module_id`) REFERENCES `application_module` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `global_param_config_ibfk_2` FOREIGN KEY (`platform_id`) REFERENCES `platform` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '全局参数配置' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of global_param_config
-- ----------------------------
INSERT INTO `global_param_config` VALUES ('c0a84203-631f-17fa-8163-268301570009', '4028c2925f5175c5015f51eb9c770001', NULL, 1, 'ECMP_ELASTIC_SEARCH', '{\"cluster-nodes\":\"{ECMP_ELASTIC_SEARCH_HOST}\"}', '平台Elasticsearch配置');
INSERT INTO `global_param_config` VALUES ('c0a84203-631f-17fa-8163-38e3de0c0021', '4028c2925f5175c5015f51eb9c770001', 'c0a84211-6208-19cb-8162-2801c899009c', 2, 'spring.data.elasticsearch', 'ECMP_ELASTIC_SEARCH', 'FSOP_EAMS全文检索');
INSERT INTO `global_param_config` VALUES ('c0a84203-631f-17fa-8163-8c4d96e0009d', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1821f0015', 0, 'BASIC.ORG.FINDORGANIZATIONDIMENSION', 'http://{BASIC_API_HOST}/basic-service/organization/findOrganizationDimension', '组织机构维度');
INSERT INTO `global_param_config` VALUES ('c0a84203-631f-17fa-8163-8c7f88e000a0', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1c9b30016', 0, 'BASIC.ORG.FINDORGANIZATIONDIMENSION', 'http://{BASIC_API_HOST}/basic-service/organization/findOrganizationDimension', '组织机构维度');
INSERT INTO `global_param_config` VALUES ('c0a84203-631f-17fa-8163-a4b8f91500a2', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'API_GATEWAY_HOST', 'http://{API_GATEWAY_HOST}/gateway-router-server/', 'API网关Router服务(gateway-router-server)基地址');
INSERT INTO `global_param_config` VALUES ('c0a84203-631f-17fa-8163-c87ea0a5021b', '4028c2925f5175c5015f51eb9c770001', 'c0a84211-6208-19cb-8162-2347677e0078', 2, 'FSOP_AMS_API_EDM', 'ECMP_EDM_MONGODB', '档案管理API模块EDM配置');
INSERT INTO `global_param_config` VALUES ('c0a84203-631f-17fa-8163-d31da3f80285', '4028c2925f5175c5015f51eb9c770001', 'c0a84211-6208-19cb-8162-2347677e0078', 2, 'EDM_MONGODB', 'ECMP_EDM_MONGODB', 'FSOP_AMS_API文档管理配置');
INSERT INTO `global_param_config` VALUES ('c0a84203-631f-17fa-8163-d92b250902de', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'CONFIG_CENTER_API', 'http://{CONFIG_CENTER_API_HOST}/config-center-service/api', '配置中心API服务地址');
INSERT INTO `global_param_config` VALUES ('c0a84203-631f-17fa-8163-de067cda02f3', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'MC_API', 'http://{MC_API_HOST}/mc-service/', '监控中心API基地址');
INSERT INTO `global_param_config` VALUES ('c0a84203-631f-17fa-8163-ed684f8e0309', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'ECMP_FLUENTD_HOST', '{ECMP_FLUENTD_HOST}', 'EFK日志 Fluentd 服务器地址');
INSERT INTO `global_param_config` VALUES ('c0a84203-631f-17fa-8163-ed68bc70030a', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'ECMP_FLUENTD_PORT', '{ECMP_FLUENTD_PORT}', 'EFK日志 Fluentd 服务器端口');
INSERT INTO `global_param_config` VALUES ('c0a84203-641c-13ee-8164-201944cc002a', '4028c2925f5175c5015f51eb9c770001', 'c0a84203-631f-17fa-8163-a59669270118', 1, 'DATASOURCE', '{\"url\":\"jdbc:mysql://{ECMP_GATEWAY_DB_HOST}/api_gateway?characterEncoding=utf8&useSSL=false\",\"username\":\"{ECMP_GATEWAY_DB_USER}\",\"password\":\"{ECMP_GATEWAY_DB_PASSWORD}\",\"driverClassName\":\"{JDBC_MYSQL_DRIVER}\",\"initialSize\":\"1\",\"minIdle\":\"1\",\"maxActive\":\"100\",\"second_level_cache_host\":\"{ECMP_REDIS_HOST}\",\"second_level_cache_port\":\"{ECMP_REDIS_PORT}\",\"second_level_cache_password\":\"{ECMP_REDIS_PASSWORD}\",\"second_level_cache_db\":\"2\"}', '数据源');
INSERT INTO `global_param_config` VALUES ('c0a84211-606d-19cd-8161-2745280200be', '4028c2925f5175c5015f51eb9c770001', '4028c2925f520f4f015f523589010003', 1, 'CLEAR_ORG_OTHER_CACHES', '{\"FLOW\":\"FlowOrgParentCodes_*\"}', '清除其他应用的组织机构缓存');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-2814641c00d2', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'FSOP_AMS_API', 'http://{FSOP_AMS_API_HOST}/fsop-ams-service/', '财务共享运营平台--档案管理系统API基地址');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-2815f27f00d4', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'FSOP_EAMS_API', 'http://{FSOP_EAMS_API_HOST}/fsop-eams-service/', '财务共享运营平台--电子档案管理系统API基地址');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-281afa8300d8', '4028c2925f5175c5015f51eb9c770001', 'c0a84211-6208-19cb-8162-2347677e0078', 0, 'API_PACKAGE', 'com.ecmp.fsop.ams.api', 'api接口包路径');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-281b9aea00d9', '4028c2925f5175c5015f51eb9c770001', 'c0a84211-6208-19cb-8162-2347677e0078', 2, 'BIZ_CACHE', 'ECMP_BIZ_CACHE', '财务共享运营平台-档案管理系统--业务缓存');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-2823c83800da', '4028c2925f5175c5015f51eb9c770001', 'c0a84211-6208-19cb-8162-2347677e0078', 1, 'DATASOURCE', '{\"url\":\"jdbc:mysql://{FSOP_AMS_DB_HOST}/fsop_ams?characterEncoding=utf8&useSSL=false\",\"username\":\"{FSOP_AMS_DB_USER}\",\"password\":\"{FSOP_AMS_DB_PASSWORD}\",\"driverClassName\":\"{JDBC_MYSQL_DRIVER}\",\"initialSize\":\"1\",\"minIdle\":\"1\",\"maxActive\":\"100\",\"second_level_cache_host\":\"{ECMP_REDIS_HOST}\",\"second_level_cache_port\":\"{ECMP_REDIS_PORT}\",\"second_level_cache_password\":\"{ECMP_REDIS_PASSWORD}\",\"second_level_cache_db\":\"2\"}', '数据源');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-2826c04d00db', '4028c2925f5175c5015f51eb9c770001', 'c0a84211-6208-19cb-8162-2801c899009c', 0, 'API_PACKAGE', 'com.ecmp.fsop.eams.api', 'api接口包路径');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-2827027c00dc', '4028c2925f5175c5015f51eb9c770001', 'c0a84211-6208-19cb-8162-2801c899009c', 2, 'BIZ_CACHE', 'ECMP_BIZ_CACHE', '财务共享运营平台-电子档案管理系统--业务缓存');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-282860ce00dd', '4028c2925f5175c5015f51eb9c770001', 'c0a84211-6208-19cb-8162-2801c899009c', 1, 'DATASOURCE', '{\"url\":\"jdbc:mysql://{FSOP_EAMS_DB_HOST}/fsop_eams?characterEncoding=utf8&useSSL=false\",\"username\":\"{FSOP_EAMS_DB_USER}\",\"password\":\"{FSOP_EAMS_DB_PASSWORD}\",\"driverClassName\":\"{JDBC_MYSQL_DRIVER}\",\"initialSize\":\"1\",\"minIdle\":\"1\",\"maxActive\":\"100\",\"second_level_cache_host\":\"{ECMP_REDIS_HOST}\",\"second_level_cache_port\":\"{ECMP_REDIS_PORT}\",\"second_level_cache_password\":\"{ECMP_REDIS_PASSWORD}\",\"second_level_cache_db\":\"2\"}', '数据源');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-508b77fd0157', '4028c2925f5175c5015f51eb9c770001', 'c0a84211-6208-19cb-8162-2801c899009c', 2, 'EDM_MONGODB', 'ECMP_EDM_MONGODB', 'FSOP_EAMS文档管理');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-bcee8ba7025e', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1821f0015', 0, 'BASIC.ORG.LISTALLORGS', 'http://{BASIC_API_HOST}/basic-service/organization/findOrgTreeWithoutFrozen', '获取所有组织机构服务地址');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-bd334ece025f', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1821f0015', 0, 'BASIC.ORG.FINDONE', 'http://{BASIC_API_HOST}/basic-service/organization/findOne', '获取指定id的组织机构');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-bd33b40d0260', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1821f0015', 0, 'BASIC.ORG.FINDPARENTNODES', 'http://{BASIC_API_HOST}/basic-service/organization/getParentNodes', '获取指定id的父组织机构对象列表');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-bd33f7e10261', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1821f0015', 0, 'BASIC.POSITION.FINDBYPAGE', 'http://{BASIC_API_HOST}/basic-service/position/findByPage', '获取岗位列表');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-bd3494060263', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1821f0015', 0, 'BASIC.POSITION.FINDONE', 'http://{BASIC_API_HOST}/basic-service/position/findOne', '获取指定id岗位');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-bd35c1fc0264', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1821f0015', 0, 'BASIC.POSITION.FINDBYIDS', 'http://{BASIC_API_HOST}/basic-service/position/findByIds', '根据岗位id列表获取岗位');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-bd367cea0265', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1821f0015', 0, 'BASIC.POSITION.GETEXECUTORSBYPOSITIONIDS', 'http://{BASIC_API_HOST}/basic-service/position/getExecutorsByPositionIds', '根据岗位的id列表获取执行人');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-bd373d5f0266', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1821f0015', 0, 'BASIC.POSITION.GETEXECUTORSBYPOSCATEIDS', 'http://{BASIC_API_HOST}/basic-service/position/getExecutorsByPosCateIds', '据岗位类别的id列表获取执行人');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-bd37cefc0267', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1821f0015', 0, 'BASIC.POSITIONCATEGORY.FINDALL', 'http://{BASIC_API_HOST}/basic-service/positionCategory/findAll', '获取所有岗位类别列表');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-bd3850af0268', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1821f0015', 0, 'BASIC.POSITIONCATEGORY.FINDONE', 'http://{BASIC_API_HOST}/basic-service/positionCategory/findOne', '获取指定id岗位类别');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-bd38e10d0269', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1821f0015', 0, 'BASIC.EMPLOYEE.GETEXECUTORSBYEMPLOYEEIDS', 'http://{BASIC_API_HOST}/basic-service/employee/getExecutorsByEmployeeIds', '根据企业员工的id列表获取执行人');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-bd3965dc026a', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1821f0015', 0, 'BASIC.EMPLOYEE.FINDBYORGANIZATIONID', 'http://{BASIC_API_HOST}/basic-service/employee/findByOrganizationIdWithoutFrozen', '根据组织机构的id获取员工');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-bd39e6ec026b', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1821f0015', 0, 'BASIC.EMPLOYEE.FINDBYIDS', 'http://{BASIC_API_HOST}/basic-service/employee/findByIds', '根据组织机构的id列表获取员工列表');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-bd3a7abb026c', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1821f0015', 0, 'BASIC.EMPLOYEE.FINDBYEMPLOYEEPARAM', 'http://{BASIC_API_HOST}/basic-service/employee/findByEmployeeParam', '根据自定义的查询参数获取员工列表');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-bd87fa7c026d', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1c9b30016', 0, 'BASIC.EMPLOYEE.FINDBYEMPLOYEEPARAM', 'http://{BASIC_API_HOST}/basic-service/employee/findByEmployeeParam', '根据自定义的查询参数获取员工列表');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-bd886c2b026e', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1c9b30016', 0, 'BASIC.EMPLOYEE.FINDBYIDS', 'http://{BASIC_API_HOST}/basic-service/employee/findByIds', '根据组织机构的id列表获取员工列表');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-bd88b59b026f', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1c9b30016', 0, 'BASIC.EMPLOYEE.FINDBYORGANIZATIONID', 'http://{BASIC_API_HOST}/basic-service/employee/findByOrganizationIdWithoutFrozen', '根据组织机构的id获取员工');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-bd88f1110270', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1c9b30016', 0, 'BASIC.EMPLOYEE.GETEXECUTORSBYEMPLOYEEIDS', 'http://{BASIC_API_HOST}/basic-service/employee/getExecutorsByEmployeeIds', '根据企业员工的id列表获取执行人');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-bd8924ea0271', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1c9b30016', 0, 'BASIC.ORG.FINDONE', 'http://{BASIC_API_HOST}/basic-service/organization/findOne', '获取指定id的组织机构');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-bd8958bb0272', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1c9b30016', 0, 'BASIC.ORG.FINDPARENTNODES', 'http://{BASIC_API_HOST}/basic-service/organization/getParentNodes', '获取指定id的父组织机构对象列表');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-bd89c3100273', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1c9b30016', 0, 'BASIC.ORG.LISTALLORGS', 'http://{BASIC_API_HOST}/basic-service/organization/findOrgTreeWithoutFrozen', '获取所有组织机构服务地址');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-bd8a19240275', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1c9b30016', 0, 'BASIC.POSITION.FINDBYIDS', 'http://{BASIC_API_HOST}/basic-service/position/findByIds', '根据岗位id列表获取岗位');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-bd8a574e0276', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1c9b30016', 0, 'BASIC.POSITION.FINDBYPAGE', 'http://{BASIC_API_HOST}/basic-service/position/findByPage', '获取岗位列表');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-bd8af5ed0278', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1c9b30016', 0, 'BASIC.POSITION.FINDONE', 'http://{BASIC_API_HOST}/basic-service/position/findOne', '获取指定id岗位');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-bd8b59da0279', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1c9b30016', 0, 'BASIC.POSITION.GETEXECUTORSBYPOSCATEIDS', 'http://{BASIC_API_HOST}/basic-service/position/getExecutorsByPosCateIds', '据岗位类别的id列表获取执行人');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-bd8b8c95027a', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1c9b30016', 0, 'BASIC.POSITION.GETEXECUTORSBYPOSITIONIDS', 'http://{BASIC_API_HOST}/basic-service/position/getExecutorsByPositionIds', '根据岗位的id列表获取执行人');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-bd8bbe40027b', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1c9b30016', 0, 'BASIC.POSITIONCATEGORY.FINDALL', 'http://{BASIC_API_HOST}/basic-service/positionCategory/findAll', '获取所有岗位类别列表');
INSERT INTO `global_param_config` VALUES ('c0a84211-6208-19cb-8162-bd8bfa07027c', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1c9b30016', 0, 'BASIC.POSITIONCATEGORY.FINDONE', 'http://{BASIC_API_HOST}/basic-service/positionCategory/findOne', '获取指定id岗位类别');
INSERT INTO `global_param_config` VALUES ('c0a84212-5f77-1b94-815f-78539bc20000', '4028c2925f5175c5015f51eb9c770001', '4028c2925f520f4f015f523589010003', 1, 'spring.datasource', '{\"url\":\"jdbc:mysql://{ECMP_BASIC_DB_HOST}/ecmp_basic?characterEncoding=utf8&useSSL=false\",\"username\":\"{ECMP_BASIC_DB_USER}\",\"password\":\"{ECMP_BASIC_DB_PASSWORD}\",\"driver-class-name\":\"{JDBC_MYSQL_DRIVER}\",\"type\":\"com.zaxxer.hikari.HikariDataSource\",\"hikari.minimum-idle\":\"5\",\"hikari.maximum-pool-size\":\"15\"}', '数据源');
INSERT INTO `global_param_config` VALUES ('c0a84212-5f77-1b94-815f-786d543e0001', '4028c2925f5175c5015f51eb9c770001', '4028c2925f520f4f015f523589010003', 0, 'API_PACKAGE', 'com.kcmp.ck.basic.api', 'api接口包路径');
INSERT INTO `global_param_config` VALUES ('c0a84212-5f77-1b94-815f-787090400002', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'static.resource.url', 'http://{STATIC.RESOURCE.URL_HOST}', '静态资源服务器的全局配置');
INSERT INTO `global_param_config` VALUES ('c0a84212-5f7a-1b2a-815f-7a7e4f5c0010', '4028c2925f5175c5015f51eb9c770001', NULL, 1, 'ECMP_SESSION_CACHE', '{\"host\":\"{ECMP_REDIS_HOST}\",\"port\":\"{ECMP_REDIS_PORT}\",\"password\":\"{ECMP_REDIS_PASSWORD}\",\"db\":\"0\",\"max_total\":\"512\",\"expired\":\"28800\"}', '平台全局会话缓存');
INSERT INTO `global_param_config` VALUES ('c0a84212-5f7a-1e59-815f-7ae54d5a0000', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'BASIC_API', 'http://{BASIC_API_HOST}/basic-service/', '基础应用API基地址');
INSERT INTO `global_param_config` VALUES ('c0a84212-5f7c-131f-815f-7f4c06cc0005', '4028c2925f5175c5015f51eb9c770001', NULL, 1, 'MQ_CONSUMER', '{\"bootstrap.servers\":\"{ECMP_KAFKA_HOST}\",\"enable.auto.commit\":\"false\",\"key.deserializer\":\"org.apache.kafka.common.serialization.StringDeserializer\",\"value.deserializer\":\"org.apache.kafka.common.serialization.StringDeserializer\"}', '平台kafka消费者参数配置');
INSERT INTO `global_param_config` VALUES ('c0a84212-5f7c-131f-815f-7f4d791b0006', '4028c2925f5175c5015f51eb9c770001', NULL, 1, 'MQ_PRODUCER', '{\"bootstrap.servers\":\"{ECMP_KAFKA_HOST}\",\"key.serializer\":\"org.apache.kafka.common.serialization.StringSerializer\",\"value.serializer\":\"org.apache.kafka.common.serialization.StringSerializer\",\"timeout.ms\":\"3000\"}', '平台kafka生产者参数配置');
INSERT INTO `global_param_config` VALUES ('c0a84212-5f7c-131f-815f-7f8ec7070007', '4028c2925f5175c5015f51eb9c770001', '4028c2925f520f4f015f523589010003', 0, 'ECMP_DEFAULT_USER_PASSWORD', '123456', 'ECMP平台默认初始密码');
INSERT INTO `global_param_config` VALUES ('c0a84212-5f80-12ae-815f-80b1ae1a004b', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1821f0015', 0, 'API_PACKAGE', 'com.kcmp.ck.flow.api', 'api接口包路径');
INSERT INTO `global_param_config` VALUES ('c0a84212-5f81-1791-815f-828b6d850002', '4028c2925f5175c5015f51eb9c770001', NULL, 1, 'ECMP_EDM_MONGODB', '{\"user\":\"{ECMP_MONGODB_USER}\",\"password\":\"{ECMP_MONGODB_PASSWORD}\",\"url\":\"{ECMP_MONGODB_HOST}\",\"port\":\"27017\",\"database\":\"edm\",\"mechanism\":\"SCRAM-SHA-1\"}', '平台电子文档管理');
INSERT INTO `global_param_config` VALUES ('c0a84212-5f86-1e25-815f-874be9fa0009', '4028c2925f5175c5015f51eb9c770001', NULL, 1, 'ECMP_BIZ_CACHE', '{\"host\":\"{ECMP_REDIS_HOST}\",\"password\":\"{ECMP_REDIS_PASSWORD}\",\"db\":\"0\",\"timeout\":\"6000\",\"jedis.pool.max-idle\":\"8\",\"jedis.pool.min-idle\":\"0\",\"jedis.pool.max-wait\":\"-1\",\"jedis.pool.max-active\":\"8\",\"port\":\"{ECMP_REDIS_PORT}\"}', '平台Redis缓存');
INSERT INTO `global_param_config` VALUES ('c0a84212-5f86-1e25-815f-874caeb8000a', '4028c2925f5175c5015f51eb9c770001', '4028c2925f520f4f015f523589010003', 2, 'spring.redis', 'ECMP_BIZ_CACHE', 'BASIC业务缓存');
INSERT INTO `global_param_config` VALUES ('c0a84212-5f8c-1f4f-815f-8f153205000d', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'FLOW_API', 'http://{FLOW_API_HOST}/flow-service/', '工作流API基地址');
INSERT INTO `global_param_config` VALUES ('c0a84212-5f8c-1f4f-815f-8f15c5ed000e', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'FLOW_WEB', 'http://{FLOW_WEB_HOST}/flow-web/', '工作流WEB基地址');
INSERT INTO `global_param_config` VALUES ('c0a84212-5f8c-1f4f-815f-8f176c9c0017', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1821f0015', 2, 'BIZ_CACHE', 'ECMP_BIZ_CACHE', 'FLOW业务缓存');
INSERT INTO `global_param_config` VALUES ('c0a84212-5f8c-1f4f-815f-8f186454001c', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1821f0015', 1, 'DATASOURCE', '{\"url\":\"jdbc:mysql://{FLOW_DB_HOST}/ecmp_flow?characterEncoding=utf8&useSSL=false\",\"username\":\"{FLOW_DB_USER}\",\"password\":\"{FLOW_DB_PASSWORD}\",\"driverClassName\":\"{JDBC_MYSQL_DRIVER}\",\"initialSize\":\"10\",\"minIdle\":\"200\",\"maxActive\":\"800\",\"second_level_cache_host\":\"{ECMP_REDIS_HOST}\",\"second_level_cache_port\":\"{ECMP_REDIS_PORT}\",\"second_level_cache_password\":\"{ECMP_REDIS_PASSWORD}\",\"second_level_cache_db\":\"2\"}', 'FLOW API数据源');
INSERT INTO `global_param_config` VALUES ('c0a84212-5f8f-111c-815f-8ffa064e0039', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f87-13e3-815f-87824f610000', 0, 'API_PACKAGE', 'com.kcmp.ck.task.api', 'api接口包路径');
INSERT INTO `global_param_config` VALUES ('c0a84212-5f8f-111c-815f-8ffba3a0003f', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f87-13e3-815f-87824f610000', 1, 'DATASOURCE', '{\"url\":\"jdbc:mysql://{TASK_DB_HOST}/{TASK_DB_NAME}?characterEncoding=utf8&useSSL=false\",\"username\":\"{TASK_DB_USER}\",\"password\":\"{TASK_DB_PASSWORD}\",\"driverClassName\":\"{JDBC_MYSQL_DRIVER}\",\"initialSize\":\"1\",\"minIdle\":\"1\",\"maxActive\":\"100\",\"second_level_cache_host\":\"{ECMP_REDIS_HOST}\",\"second_level_cache_port\":\"{ECMP_REDIS_PORT}\",\"second_level_cache_password\":\"{ECMP_REDIS_PASSWORD}\",\"second_level_cache_db\":\"2\"}', '数据源');
INSERT INTO `global_param_config` VALUES ('c0a84212-5f90-1684-815f-900c914e0004', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'TASK_API', 'http://{TASK_API_HOST}/task-service/', '后台作业API基地址');
INSERT INTO `global_param_config` VALUES ('c0a84212-5f90-1c63-815f-9454ffee0000', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f87-13e3-815f-87824f610000', 2, 'EDM_MONGODB', 'ECMP_EDM_MONGODB', 'TASK文档管理');
INSERT INTO `global_param_config` VALUES ('c0a84402-7688-1e64-8177-613fc1ab0002', '4028c2925f5175c5015f51eb9c770001', '4028c2925f520f4f015f523589010003', 1, 'jx.ding', '{\"agent.id\":\"1090466089\",\"app.key\":\"ding4e4wvbnslblynbng\",\"app.secret\":\"3X9c6tEg07R2Y6d33_-J_n3341lhWsJ-uPfWhrPI5d6Jesl1g51jSKnxWTR3CiHR\"}', '对接钉钉接口参数');
INSERT INTO `global_param_config` VALUES ('c0a84402-7916-1bea-8179-64a72d980000', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'THOB_MOBILE_REACT', 'http://{THOB-MOBILE-REACT-HOST}/react-thob-mobile#/', '三重一大移动端基地址');
INSERT INTO `global_param_config` VALUES ('c0a84403-6fbc-1b96-816f-bce8d2580001', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'API_GATEWAY_HOST_COPY', 'http://{API_GATEWAY_HOST}/gateway-router-server/', 'API网关Router服务(gateway-router-server)基地址');
INSERT INTO `global_param_config` VALUES ('c0a84403-7770-13d6-8177-bd1d87990002', '4028c2925f5175c5015f51eb9c770001', 'c0a84403-7770-13d6-8177-bd0a53030000', 1, 'jx.ding', '{\"agent.id\":\"1090466089\",\"app.key\":\"ding4e4wvbnslblynbng\",\"app.secret\":\"3X9c6tEg07R2Y6d33_-J_n3341lhWsJ-uPfWhrPI5d6Jesl1g51jSKnxWTR3CiHR\"}', '对接钉钉接口参数');
INSERT INTO `global_param_config` VALUES ('c0a84403-77bd-1050-8177-bd6885bb0006', '4028c2925f5175c5015f51eb9c770001', 'c0a84403-7770-13d6-8177-bd0a53030000', 1, 'DATASOURCE', '{\"url\":\"jdbc:mysql://{DINGTALK_API_DB_HOST}/ecmp_dingtalk?characterEncoding=utf8&useSSL=false\",\"username\":\"{DINGTALK_API_DB_USER}\",\"password\":\"{DINGTALK_API_DB_PASSWORD}\",\"driverClassName\":\"{JDBC_MYSQL_DRIVER}\",\"initialSize\":\"1\",\"minIdle\":\"1\",\"maxActive\":\"100\"}', '数据源');
INSERT INTO `global_param_config` VALUES ('c0a84403-77d2-140b-8177-d864e2e00000', '4028c2925f5175c5015f51eb9c770001', 'c0a84403-7770-13d6-8177-bd0a53030000', 0, 'jx.ding.root.dept.id', '1', '钉钉组织机构跟部门ID');
INSERT INTO `global_param_config` VALUES ('c0a84403-77d2-140b-8177-fae12e390001', '4028c2925f5175c5015f51eb9c770001', 'c0a84403-7770-13d6-8177-bd0a53030000', 0, 'API_PACKAGE', 'com.ecmp.dingtalk', 'API接口包路径');
INSERT INTO `global_param_config` VALUES ('c0a84403-7bec-1054-817b-ed89cd270004', '4028c2925f5175c5015f51eb9c770001', 'c0a84403-7bec-1054-817b-ed48d0130000', 1, 'DATASOURCE', '{\"url\":\"jdbc:mysql://{CADRES_API_DB_HOST}/ecmp_cadres?characterEncoding=utf8&useSSL=false\",\"username\":\"{CADRES_API_DB_USER}\",\"password\":\"{CADRES_API_DB_PASSWORD}\",\"driverClassName\":\"{JDBC_MYSQL_DRIVER}\",\"initialSize\":\"1\",\"minIdle\":\"1\",\"maxActive\":\"50\"}', '数据源');
INSERT INTO `global_param_config` VALUES ('c0a84403-7bec-1054-817b-ed8af0de0005', '4028c2925f5175c5015f51eb9c770001', 'c0a84403-7bec-1054-817b-ed48d0130000', 0, 'API_PACKAGE', 'com.ecmp.cadres.api', 'API接口包路径');
INSERT INTO `global_param_config` VALUES ('c0a84405-78af-143b-8178-b558c70e0009', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'REACT_THOB_WEB', 'http://{STATIC.RESOURCE.URL_HOST}/react-thob-web/', '三重一大web地址');
INSERT INTO `global_param_config` VALUES ('c0a84405-78af-143b-8178-b560b42a000c', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'THOB_API', 'http://{THOB_API_HOST}/thob-service/', '三重一大API基地址');
INSERT INTO `global_param_config` VALUES ('c0a84405-78af-143b-8178-c8eb96d4000d', '4028c2925f5175c5015f51eb9c770001', '4028c2925f520f4f015f523589010003', 0, 'jx.ding.root.dept.id', '1', '同步钉钉组织机构的根组织机构ID');
INSERT INTO `global_param_config` VALUES ('c0a84406-65c1-1dfb-8165-cc7f9bcf002c', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'SINGLE_TENANT_CODE', '10006', '获取单一租户代码');
INSERT INTO `global_param_config` VALUES ('c0a84406-65c1-1dfb-8165-cc7fdbff002d', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'SINGLE_TENANT_ADMIN', '10006', '获取单一租户管理员');
INSERT INTO `global_param_config` VALUES ('c0a84406-7217-163a-8172-257b10e20000', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'NOTICE_REACT', 'http://{NOTICE-REACT-HOST}/notice-react/', '信息中心web地址');
INSERT INTO `global_param_config` VALUES ('c0a84406-7217-163a-8172-6ecbe2f30012', '4028c2925f5175c5015f51eb9c770001', 'c0a84406-7217-163a-8172-6ec3c2b00003', 0, 'API_PACKAGE', 'com.kcmp.ck.official.api', 'api接口包路径');
INSERT INTO `global_param_config` VALUES ('c0a84406-7217-163a-8172-6eccc4c00013', '4028c2925f5175c5015f51eb9c770001', 'c0a84406-7217-163a-8172-6ec3c2b00003', 1, 'DATASOURCE', '{\"url\":\"jdbc:mysql://{OFFICIAL_DB_HOST}/{OFFICIAL_DB_NAME}?characterEncoding=utf8&useSSL=false\",\"username\":\"{OFFICIAL_DB_USER}\",\"password\":\"{OFFICIAL_DB_PASSWORD}\",\"driverClassName\":\"{JDBC_MYSQL_DRIVER}\",\"initialSize\":\"1\",\"minIdle\":\"1\",\"maxActive\":\"100\",\"second_level_cache_host\":\"{ECMP_REDIS_HOST}\",\"second_level_cache_port\":\"{ECMP_REDIS_PORT}\",\"second_level_cache_password\":\"{ECMP_REDIS_PASSWORD}\",\"second_level_cache_db\":\"2\"}', '数据源');
INSERT INTO `global_param_config` VALUES ('c0a84406-7217-163a-8172-6eccf68f0014', '4028c2925f5175c5015f51eb9c770001', 'c0a84406-7217-163a-8172-6ec3c2b00003', 1, 'spring.grpc', '{\"enable\":\"false\"}', 'grpc配置');
INSERT INTO `global_param_config` VALUES ('c0a84406-7217-163a-8172-6ecde9680015', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'OFFICIAL_API', 'http://{OFFICIAL_API_HOST}/official-service/', '官网API基地址');
INSERT INTO `global_param_config` VALUES ('c0a84406-7217-163a-8172-6eceadd80016', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'OFFICIAL_REACT', 'http://{OFFICIAL-REACT-HOST}/official-react/', '官网web地址');
INSERT INTO `global_param_config` VALUES ('c0a84406-73a2-17ce-8173-e64161530001', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'REPEAT_COMMIT_TIMEOUT', '0', '重复提交超时时间设置，如5，为5秒内不允许该用户相同的请求提交，5秒后才能重新提交该请求');
INSERT INTO `global_param_config` VALUES ('c0a84408-6661-1b3f-8167-1144e48a01be', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1c9b30016', 0, 'SHOW_BATCH_BUTTON', 'true', '是否显示待办中的批量处理按钮');
INSERT INTO `global_param_config` VALUES ('c0a84408-6661-1b3f-8167-594ea44602b4', '4028c2925f5175c5015f51eb9c770001', 'c0a84408-6661-1b3f-8167-594d0df202b2', 1, 'spring.data.mongodb', '{\"uri\":\"mongodb://{ECMP_MONGODB_USER}:{ECMP_MONGODB_PASSWORD}@{ECMP_MONGODB_HOST}/edm\",\"port\":\"27017\",\"database\":\"edm\"}', 'MongoDB');
INSERT INTO `global_param_config` VALUES ('c0a84408-6661-1b3f-8167-595289e602b5', '4028c2925f5175c5015f51eb9c770001', 'c0a84408-6661-1b3f-8167-594d0df202b2', 1, 'spring.redisson', '{\"address\":\"{ECMP_REDIS_HOST}:{ECMP_REDIS_PORT}\",\"password\":\"{ECMP_REDIS_PASSWORD}\",\"database\":\"3\"}', 'redis');
INSERT INTO `global_param_config` VALUES ('c0a84408-6661-1b3f-8167-5953505102b6', '4028c2925f5175c5015f51eb9c770001', 'c0a84408-6661-1b3f-8167-594d0df202b2', 0, 'logging.path', '/opt/logs', '应用日志路径');
INSERT INTO `global_param_config` VALUES ('c0a84408-6661-1b3f-8167-5953b41302b7', '4028c2925f5175c5015f51eb9c770001', 'c0a84408-6661-1b3f-8167-594d0df202b2', 0, 'file.dir', '/opt/file_data/', '为转换文件实际存储地址，注意要以/结尾');
INSERT INTO `global_param_config` VALUES ('c0a84408-6661-1b3f-8167-59562ffc02b8', '4028c2925f5175c5015f51eb9c770001', 'c0a84408-6661-1b3f-8167-594d0df202b2', 0, 'office.home', '/opt/openoffice4', 'OpenOffice或者LibreOffice 安装目录');
INSERT INTO `global_param_config` VALUES ('c0a84409-6974-110c-8169-7a7096320002', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'AUTH_API', 'http://{AUTH_API_HOST}/auth-service/', '认证中心API基地址');
INSERT INTO `global_param_config` VALUES ('c0a84409-6974-110c-8169-7a71bd690003', '4028c2925f5175c5015f51eb9c770001', 'c0a84421-696a-1c13-8169-706f05700033', 0, 'API_PACKAGE', 'com.ecmp.auth.api', 'api包地址');
INSERT INTO `global_param_config` VALUES ('c0a84409-6974-110c-8169-7f248b5f0009', '4028c2925f5175c5015f51eb9c770001', 'c0a84203-631f-17fa-8163-a59669270118', 2, 'BIZ_CACHE', 'ECMP_BIZ_CACHE', '网关缓存');
INSERT INTO `global_param_config` VALUES ('c0a84409-6974-110c-8169-7ff7b409000a', '4028c2925f5175c5015f51eb9c770001', 'c0a84421-696a-1c13-8169-706f05700033', 2, 'BIZ_CACHE', 'ECMP_BIZ_CACHE', 'AUTH缓存');
INSERT INTO `global_param_config` VALUES ('c0a84409-6974-110c-8169-b7fc88ce0053', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'FSOP_EAMS_REACT', 'http://{FSOP_EAMS_REACT_HOST}/fsop-eams-react/', 'REACT 电子档案管理系统--WEB基地址');
INSERT INTO `global_param_config` VALUES ('c0a84409-69dc-1246-816a-011c901b0009', '4028c2925f5175c5015f51eb9c770001', '4028c2925f520f4f015f523589010003', 1, 'spring.grpc', '{\"enable\":\"false\"}', 'grpc配置');
INSERT INTO `global_param_config` VALUES ('c0a84409-69dc-1246-816a-01841853000f', '4028c2925f5175c5015f51eb9c770001', 'c0a84211-6208-19cb-8162-2347677e0078', 1, 'logging.level', '{\"root\":\"debug\"}', '日志');
INSERT INTO `global_param_config` VALUES ('c0a84409-69dc-1246-816a-01849d170010', '4028c2925f5175c5015f51eb9c770001', 'c0a84211-6208-19cb-8162-2801c899009c', 1, 'logging.level', '{\"root\":\"debug\"}', '日志');
INSERT INTO `global_param_config` VALUES ('c0a84409-69dc-1246-816a-018b3bd10011', '4028c2925f5175c5015f51eb9c770001', 'c0a84211-6208-19cb-8162-2347677e0078', 0, 'spring.grpc.enable', 'false', 'RPC');
INSERT INTO `global_param_config` VALUES ('c0a84409-69dc-1246-816a-01eb69350013', '4028c2925f5175c5015f51eb9c770001', 'c0a84211-6208-19cb-8162-2801c899009c', 0, 'SCAN_URL', 'http://{STATIC.RESOURCE.URL_HOST}/scanner/Rcsit.Scanner.application', '扫描程序部署路径');
INSERT INTO `global_param_config` VALUES ('c0a84409-69dc-1246-816a-0b6c2a290017', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1c9b30016', 1, 'logging.level', '{\"com.kcmp.ck.core.security.jwt\":\"debug\"}', '日志');
INSERT INTO `global_param_config` VALUES ('c0a84409-69dc-1246-816a-0bca5a030018', '4028c2925f5175c5015f51eb9c770001', 'c0a84421-696a-1c13-8169-706f05700033', 1, 'logging.level', '{\"com.kcmp.ck.auth.service\":\"debug\"}', '日志');
INSERT INTO `global_param_config` VALUES ('c0a8440a-710e-1aee-8171-0f5e3e0a000d', '4028c2925f5175c5015f51eb9c770001', 'c0a8440a-710e-1aee-8171-0f454f2d0000', 0, 'API_PACKAGE', 'com.kcmp.ck.notify.api', 'api接口包路径');
INSERT INTO `global_param_config` VALUES ('c0a8440a-710e-1aee-8171-0f616360000e', '4028c2925f5175c5015f51eb9c770001', 'c0a8440a-710e-1aee-8171-0f454f2d0000', 1, 'DATASOURCE', '{\"url\":\"jdbc:mysql://{NOTIFY_DB_HOST}/{NOTIFY_DB_NAME}?characterEncoding=utf8&useSSL=false\",\"username\":\"{NOTIFY_DB_USER}\",\"password\":\"{NOTIFY_DB_PASSWORD}\",\"driverClassName\":\"{JDBC_MYSQL_DRIVER}\",\"initialSize\":\"1\",\"minIdle\":\"1\",\"maxActive\":\"100\",\"second_level_cache_host\":\"{ECMP_REDIS_HOST}\",\"second_level_cache_port\":\"{ECMP_REDIS_PORT}\",\"second_level_cache_password\":\"{ECMP_REDIS_PASSWORD}\",\"second_level_cache_db\":\"2\"}', '数据源');
INSERT INTO `global_param_config` VALUES ('c0a8440a-710e-1aee-8171-0f6340c9000f', '4028c2925f5175c5015f51eb9c770001', 'c0a8440a-710e-1aee-8171-0f454f2d0000', 1, 'spring.grpc', '{\"enable\":\"false\"}', 'grpc配置');
INSERT INTO `global_param_config` VALUES ('c0a8440a-710e-1aee-8171-0f67e49e0010', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'NOTIFY_API', 'http://{NOTIFY_API_HOST}/notify-service/', '通告API基地址');
INSERT INTO `global_param_config` VALUES ('c0a8440a-7170-13c7-8171-712d51040000', '4028c2925f5175c5015f51eb9c770001', NULL, 1, 'ECMP_EMAIL', '{\"host\":\"211.149.219.123\",\"port\":\"1111\",\"user\":\"user\",\"password\":\"password\"}', 'EMAIL服务的参数');
INSERT INTO `global_param_config` VALUES ('c0a8440a-7170-13c7-8171-72debfbd0018', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'NOTICE_API', 'http://{NOTICE_API_HOST}/notice-service/', '公告API基地址');
INSERT INTO `global_param_config` VALUES ('c0a8440a-7170-13c7-8171-72df3b280019', '4028c2925f5175c5015f51eb9c770001', 'c0a8440a-7170-13c7-8171-72d92d5e000b', 0, 'API_PACKAGE', 'com.kcmp.ck.notice.api', 'api接口包路径');
INSERT INTO `global_param_config` VALUES ('c0a8440a-7170-13c7-8171-72dfa3e4001a', '4028c2925f5175c5015f51eb9c770001', 'c0a8440a-7170-13c7-8171-72d92d5e000b', 1, 'DATASOURCE', '{\"url\":\"jdbc:mysql://{NOTICE_DB_HOST}/{NOTICE_DB_NAME}?characterEncoding=utf8&useSSL=false\",\"username\":\"{NOTICE_DB_USER}\",\"password\":\"{NOTICE_DB_PASSWORD}\",\"driverClassName\":\"{JDBC_MYSQL_DRIVER}\",\"initialSize\":\"1\",\"minIdle\":\"1\",\"maxActive\":\"100\",\"second_level_cache_host\":\"{ECMP_REDIS_HOST}\",\"second_level_cache_port\":\"{ECMP_REDIS_PORT}\",\"second_level_cache_password\":\"{ECMP_REDIS_PASSWORD}\",\"second_level_cache_db\":\"2\"}', '数据源');
INSERT INTO `global_param_config` VALUES ('c0a8440a-7170-13c7-8171-72dfc85c001b', '4028c2925f5175c5015f51eb9c770001', 'c0a8440a-7170-13c7-8171-72d92d5e000b', 1, 'spring.grpc', '{\"enable\":\"false\"}', 'grpc配置');
INSERT INTO `global_param_config` VALUES ('c0a8440a-7184-1d2d-8171-86bd281a0000', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'EDM_DOC_ADDRESS_NEW', 'http://{EDM_API_HOST}/edm-service/download?docId=', 'edm调用基地址加上文件源地址');
INSERT INTO `global_param_config` VALUES ('c0a8440e-6c41-103e-816c-414d0d5e0000', '4028c2925f5175c5015f51eb9c770001', 'c0a84421-696a-1c13-8169-706f05700033', 1, 'spring.grpc', '{\"enable\":\"false\"}', 'grpc配置');
INSERT INTO `global_param_config` VALUES ('c0a8440e-6c41-103e-816c-414e867c0001', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1821f0015', 1, 'spring.grpc', '{\"enable\":\"false\"}', 'grpc配置');
INSERT INTO `global_param_config` VALUES ('c0a8440e-6c41-103e-816c-415304650002', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f87-13e3-815f-87824f610000', 1, 'spring.grpc', '{\"enable\":\"false\"}', 'grpc配置');
INSERT INTO `global_param_config` VALUES ('c0a8440e-6c41-103e-816c-4157e1ba0006', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'FSOP_AMS_REACT', 'http://{FSOP_AMS_REACT_HOST}/fsop-ams-react/', '档案管理系统--WEB基地址');
INSERT INTO `global_param_config` VALUES ('c0a8440e-6c41-103e-816c-4161cfea0014', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'IAS_API', 'http://{IAS_API_HOST}/ias-service/', '发票管理系统API地址');
INSERT INTO `global_param_config` VALUES ('c0a8440e-6c41-103e-816c-41624a6e0015', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'IAS_WEB', 'http://{IAS_WEB_HOST}/ias-web/', '发票管理系统WEB地址');
INSERT INTO `global_param_config` VALUES ('c0a8440e-6c41-103e-816c-4164aa630016', '4028c2925f5175c5015f51eb9c770001', 'c0a8440e-6c41-103e-816c-4158a0f40007', 1, 'SPRING.DATASOURCE', '{\"url\":\"jdbc:mysql://{IAS_API_DB_HOST}/ias_server?characterEncoding=utf8&useSSL=false\",\"username\":\"{IAS_API_DB_USER}\",\"password\":\"{IAS_API_DB_PASSWORD}\",\"driverClassName\":\"{JDBC_MYSQL_DRIVER}\",\"type\":\"com.zaxxer.hikari.HikariDataSource\",\"hikari.minimum-idle\":\"5\",\"hikari.maximum-pool-size\":\"15\"}', '数据源');
INSERT INTO `global_param_config` VALUES ('c0a8440e-6c41-103e-816c-4166aba80017', '4028c2925f5175c5015f51eb9c770001', 'c0a8440e-6c41-103e-816c-4158a0f40007', 2, 'EDM_MONGODB', 'ECMP_EDM_MONGODB', '发票文档管理');
INSERT INTO `global_param_config` VALUES ('c0a8440e-6c41-1c3b-816c-45a7fc1b0004', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'BASIC_WEB', 'http://{BASIC_WEB_HOST}/basic-web/', '基础应用WEB基地址');
INSERT INTO `global_param_config` VALUES ('c0a8440e-6c4a-177a-816c-d7683371000e', '4028c2925f5175c5015f51eb9c770001', 'c0a84421-696a-1c13-8169-706f05700033', 0, 'jx.sso.appkey', 'leading-sso.key', 'appkey秘钥');
INSERT INTO `global_param_config` VALUES ('c0a8440e-6c4a-177a-816c-d7cf5d9e000f', '4028c2925f5175c5015f51eb9c770001', 'c0a84408-6661-1b3f-8167-594d0df202b2', 2, 'spring.data.elasticsearch', 'ECMP_ELASTIC_SEARCH', 'EDM全文检索');
INSERT INTO `global_param_config` VALUES ('c0a8440e-6cef-12c3-816c-f0fe83ec0000', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'SYSTEM_CODE_FOR_OA', 'yx', 'OA系统显示名称');
INSERT INTO `global_param_config` VALUES ('c0a8440e-6cfa-13b8-816c-fb34b4f20000', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'TO_DO_WEB_SERVICE', 'http://10.100.105.60:90/services/OfsTodoDataWebService', '推送OA代办地址');
INSERT INTO `global_param_config` VALUES ('c0a8440e-6cfa-13b8-816c-ffdc6f9e0001', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'BASE_URL', 'http://{BASIC_WEB_HOST}', 'BASE_URL');
INSERT INTO `global_param_config` VALUES ('c0a8440e-6cff-17f7-816d-050b64d50000', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'EDM_DOC_ADDRESS', 'http://{EDM_API_HOST}/edm-service/', 'edm调用基地址');
INSERT INTO `global_param_config` VALUES ('c0a8440e-6cff-17f7-816d-050c143f0001', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'OCR_URL', 'http://172.18.2.60:9990/', 'OCR调用基地址');
INSERT INTO `global_param_config` VALUES ('c0a8440e-6d05-10b0-816d-05521a650000', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'ECMP_SYNC_PATH', 'http://10.100.105.60:90/services/HrmService', 'oa系统同步地址');
INSERT INTO `global_param_config` VALUES ('c0a8440e-6d05-10b0-816d-0552829e0001', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'ECMP_SYNC_IP', '10.100.105.60', 'oa系统同步ip地址');
INSERT INTO `global_param_config` VALUES ('c0a8440e-6d05-10b0-816d-0552e0d30002', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'ECMP_IS_INIT', '0', '是否初始化（0：否，1：是）');
INSERT INTO `global_param_config` VALUES ('c0a8440e-6d05-10b0-816d-055391b70003', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'ECMP_UPDATE_DAYS', '30', '更新天数');
INSERT INTO `global_param_config` VALUES ('c0a8440e-6db8-1a8d-816d-bbf0ced10001', '4028c2925f5175c5015f51eb9c770001', 'c0a84408-6661-1b3f-8167-594d0df202b2', 1, 'jx.edm', '{\"watermark\":\"JX TEST\",\"barcode.match\":\"JK,YWZDBX,CCBX,GYSYFK,GYSFK,BX,YXFYBX,YSW-BX,XCJPBX,WBRYBX,SLBX\"}', '附件配置EDM');
INSERT INTO `global_param_config` VALUES ('c0a8440e-6dbb-122d-816d-bf55e8110000', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'ECMP_BUSINESS_TYPE_ID', '76E47660-C7DB-11E9-97D8-0242C0A8440A,E27E797E-C483-11E9-94AB-38BAF81C00FB', '需要发票信息回传的业务类型');
INSERT INTO `global_param_config` VALUES ('c0a8440e-6dc7-1bb5-816d-ca1292790009', '4028c2925f5175c5015f51eb9c770001', 'c0a8440e-6c41-103e-816c-4158a0f40007', 0, 'IAS_NEON_CLIENT_ID', '9151010071752521811', '发票系统--clientId');
INSERT INTO `global_param_config` VALUES ('c0a8440e-6dc7-1bb5-816d-ca131a47000a', '4028c2925f5175c5015f51eb9c770001', 'c0a8440e-6c41-103e-816c-4158a0f40007', 0, 'IAS_NEON_CLIENT_SECRET', '135f2d23ac2d4019b72cb2347bdb8fcf', '发票系统--token');
INSERT INTO `global_param_config` VALUES ('c0a8440e-6dc7-1bb5-816d-ca138d9f000b', '4028c2925f5175c5015f51eb9c770001', 'c0a8440e-6c41-103e-816c-4158a0f40007', 0, 'SINGLE_TENANT_ADMIN', 'ecmp', '发票系统--租户账号');
INSERT INTO `global_param_config` VALUES ('c0a8440e-6dc7-1bb5-816d-ca13dfec000c', '4028c2925f5175c5015f51eb9c770001', 'c0a8440e-6c41-103e-816c-4158a0f40007', 0, 'SINGLE_TENANT_CODE', 'global', '发票系统--租户代码');
INSERT INTO `global_param_config` VALUES ('c0a8440f-7863-1a35-8178-7c2dc17d0003', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'DINGTALK_API', 'http://{DINGTALK_API_HOST}/dingtalk-service/', '钉钉集成服务基地址');
INSERT INTO `global_param_config` VALUES ('c0a8440f-7863-1a35-8178-86d70724000e', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'MAJOREVENT_API', 'http://{MAJOREVENT_API_HOST}/majorevent-service/', '三重一大服务基地址');
INSERT INTO `global_param_config` VALUES ('c0a8440f-7863-1a35-8178-87865f0f0016', '4028c2925f5175c5015f51eb9c770001', 'c0a8440f-7863-1a35-8178-86ccc66f0004', 1, 'DATASOURCE', '{\"url\":\"jdbc:mysql://{MAJOREVENT_API_DB_HOST}/ecmp_majorevent?characterEncoding=utf8&useSSL=false\",\"username\":\"{MAJOREVENT_API_DB_USER}\",\"password\":\"{MAJOREVENT_API_DB_PASSWORD}\",\"driverClassName\":\"{JDBC_MYSQL_DRIVER}\",\"initialSize\":\"1\",\"minIdle\":\"1\",\"maxActive\":\"100\"}', '数据源');
INSERT INTO `global_param_config` VALUES ('c0a8440f-7863-1a35-8178-90758c8c0021', '4028c2925f5175c5015f51eb9c770001', 'c0a8440f-7863-1a35-8178-906ded320019', 0, 'API_PACKAGE', 'com.ecmp.thob', 'API接口包路径');
INSERT INTO `global_param_config` VALUES ('c0a8440f-7863-1a35-8178-90782fdd0022', '4028c2925f5175c5015f51eb9c770001', 'c0a8440f-7863-1a35-8178-906ded320019', 1, 'DATASOURCE', '{\"url\":\"jdbc:mysql://{THOB_API_DB_HOST}/ecmp_thob?characterEncoding=utf8&useSSL=false\",\"username\":\"{THOB_API_DB_USER}\",\"password\":\"{THOB_API_DB_PASSWORD}\",\"driverClassName\":\"{JDBC_MYSQL_DRIVER}\"}', '数据源');
INSERT INTO `global_param_config` VALUES ('c0a84410-67de-137b-8167-f2be3fe80002', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1821f0015', 0, 'BASIC.POSITIONCATANDORG.GETEXECUTORS', 'http://{BASIC_API_HOST}/basic-service/employee/getExecutorsByPostCatAndOrg', '根据岗位类别id列表和组织机构id列表获取执行人');
INSERT INTO `global_param_config` VALUES ('c0a84410-67de-137b-8168-601f7b83000d', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'REACT_BASIC_WEB', 'http://{STATIC.RESOURCE.URL_HOST}/web/', 'REACT基础应用WEB基地址');
INSERT INTO `global_param_config` VALUES ('c0a84410-67de-137b-8168-9ddcab550019', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'REACT_TASK_WEB', 'http://{REACT_TASK_WEB_HOST}/react-task-web/', 'REACT后台作业WEB基地址');
INSERT INTO `global_param_config` VALUES ('c0a84410-67de-137b-8168-eebae7e7001b', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'REACT_FLOW_WEB', 'http://{REACT_FLOW_WEB_HOST}/react-flow-web/', 'REACT流程WEB基地址');
INSERT INTO `global_param_config` VALUES ('c0a84410-68f4-1f68-8169-5be1dcc200d1', '4028c2925f5175c5015f51eb9c770001', 'c0a84212-5f7c-131f-815f-7fd1821f0015', 0, 'FLOW_PUSH_TASK_BASIC', 'true', '是否推送流程信息到basic');
INSERT INTO `global_param_config` VALUES ('c0a84414-6a34-1d07-816a-53e730180000', '4028c2925f5175c5015f51eb9c770001', 'c0a84211-6208-19cb-8162-2347677e0078', 1, 'spring.grpc', '{\"enable\":\"false\"}', 'grpc配置');
INSERT INTO `global_param_config` VALUES ('c0a84414-6a34-1d07-816a-53e77f4e0001', '4028c2925f5175c5015f51eb9c770001', 'c0a84211-6208-19cb-8162-2801c899009c', 1, 'spring.grpc', '{\"enable\":\"false\"}', 'grpc配置');
INSERT INTO `global_param_config` VALUES ('c0a84414-6a34-1d07-816a-68773b980005', '4028c2925f5175c5015f51eb9c770001', 'c0a84408-6661-1b3f-8167-594d0df202b2', 0, 'API_PACKAGE', 'com.kcmp.ck.edm.api', 'api接口包路径');
INSERT INTO `global_param_config` VALUES ('c0a84414-6a34-1d07-816a-68cd167e0006', '4028c2925f5175c5015f51eb9c770001', 'c0a84408-6661-1b3f-8167-594d0df202b2', 1, 'spring.grpc', '{\"enable\":\"false\"}', 'grpc配置');
INSERT INTO `global_param_config` VALUES ('c0a84414-6ad5-1324-816b-500ecf970000', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'EDM_API', 'http://{EDM_API_HOST}/edm-service/api/', '附件管理API基地址');
INSERT INTO `global_param_config` VALUES ('c0a8441b-658f-17d5-8165-96003ad70002', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'SERVER.SERVLET.SESSION.TIMEOUT', '28800', '平台全局会话超时时间');
INSERT INTO `global_param_config` VALUES ('c0a85607-7c20-116d-817c-20fe75cc0000', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'CADRES_API', 'http://{CADRES_API_HOST}/cadres-service/', '干部专职化管理API基地址');
INSERT INTO `global_param_config` VALUES ('c0a85607-7c20-116d-817c-210029480001', '4028c2925f5175c5015f51eb9c770001', NULL, 0, 'CADRES_WEB', 'http://{STATIC.RESOURCE.URL_HOST}/cadres-web/', '干部专职化管理web地址');
INSERT INTO `global_param_config` VALUES ('c0a8560a-7e52-1b8c-817e-75419b130001', '4028c2925f5175c5015f51eb9c770001', 'c0a84403-7bec-1054-817b-ed48d0130000', 1, 'jx.manager', '{\"pxid\":\"DBCD31C7-3944-11EC-8683-0242C0A8560D\",\"zzbid\":\"0AA0A5CC-392B-11EC-8683-0242C0A8560D\",\"control\":\"CE33C9C0-3950-11EC-8683-0242C0A8560D\"}', '权限相关ID');
INSERT INTO `global_param_config` VALUES ('c0a8560a-7ee1-101e-817e-f6675a170000', '4028c2925f5175c5015f51eb9c770001', NULL, 1, 'CMS-JS', '{\"CreateAccounting\":\"https://tkjgzt.ylhdc.com.cn:6299/fbd/manPay/settlementInforShare?lvCode=GC&operate=view&sourceId=\",\"ReviewAccounting\":\"https://tkjgzt.ylhdc.com.cn:6299/fbd/manPay/settlementInforShare?lvCode=GC&operate=recheck&sourceId=\"}', '122');

-- ----------------------------
-- Table structure for platform
-- ----------------------------
DROP TABLE IF EXISTS `platform`;
CREATE TABLE `platform`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id标识',
  `code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '代码',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `rank` int(11) NOT NULL DEFAULT 0 COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_platform_code`(`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '平台' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of platform
-- ----------------------------
INSERT INTO `platform` VALUES ('4028c2925f5175c5015f51eb9c770001', 'JX', 'JX业务平台', 1);

-- ----------------------------
-- Table structure for runtime_environment
-- ----------------------------
DROP TABLE IF EXISTS `runtime_environment`;
CREATE TABLE `runtime_environment`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id标识',
  `code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '代码',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `rank` int(11) NOT NULL DEFAULT 0 COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_run_env_code`(`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '运行环境' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of runtime_environment
-- ----------------------------
INSERT INTO `runtime_environment` VALUES ('c0a84203-6497-1b84-8164-a253d0fc0013', 'DEV3.0', '开发系统', 0);

-- ----------------------------
-- Table structure for serial_number_config
-- ----------------------------
DROP TABLE IF EXISTS `serial_number_config`;
CREATE TABLE `serial_number_config`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Id标识',
  `entity_class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '实体类名（全名）',
  `isolation_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '隔离码',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '实体名称',
  `prefixes` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编号前缀',
  `length` int(11) NOT NULL DEFAULT 0 COMMENT '编号长度',
  `initial_serial` int(11) NOT NULL DEFAULT 0 COMMENT '初始序号',
  `current_serial` int(11) NOT NULL DEFAULT 0 COMMENT '当前序号',
  `current_number` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '当前编号',
  `no_leading_zero` bit(1) NOT NULL DEFAULT b'0' COMMENT '无前导零',
  `env_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'DEV',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_class_name_isolation_code`(`entity_class_name`, `isolation_code`, `env_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '编号生成器配置' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of serial_number_config
-- ----------------------------
INSERT INTO `serial_number_config` VALUES ('7f000001-5ba2-1a19-815b-a3ec5df20000', 'com.kcmp.ck.basic.entity.Tenant', NULL, '租户', '', 5, 10000, 10000, '10000', b'0', 'DEV3.0');
INSERT INTO `serial_number_config` VALUES ('7f000001-5ba2-1a19-815b-ae2702c20006', 'com.ecmp.basic.entity.Position', NULL, '岗位', '', 5, 10000, 10025, '10025', b'0', 'DEV3.0');
INSERT INTO `serial_number_config` VALUES ('7f000001-5ba2-1a19-815b-ae277c760007', 'com.kcmp.ck.basic.entity.PositionCategory', NULL, '岗位类别', '', 5, 10000, 10016, '10016', b'0', 'DEV3.0');
INSERT INTO `serial_number_config` VALUES ('7f000001-5ba2-1a19-815b-ae281f190008', 'com.kcmp.ck.basic.entity.Organization', NULL, '组织机构', '', 5, 10000, 10949, '10949', b'0', 'DEV3.0');
INSERT INTO `serial_number_config` VALUES ('9BBA46E4-51BB-11E7-8155-960F8309DEA7', 'com.ecmp.flow.entity.DefaultBusinessModel', NULL, '默认业务单据', 'D', 5, 10000, 0, '0', b'0', 'DEV3.0');
INSERT INTO `serial_number_config` VALUES ('ADEE76B5-51BB-11E7-8155-960F8309DEA7', 'com.ecmp.flow.entity.DefaultBusinessModel2', NULL, '采购业务单据', 'C', 5, 10000, 0, '0', b'0', 'DEV3.0');
INSERT INTO `serial_number_config` VALUES ('B6DB77A6-51BB-11E7-8155-960F8309DEA7', 'com.ecmp.flow.entity.DefaultBusinessModel3', NULL, '销售业务单据', 'S', 5, 10000, 0, '0', b'0', 'DEV3.0');
INSERT INTO `serial_number_config` VALUES ('c0a84203-62f5-178b-8162-f5a64ee70000', 'com.ecmp.fsop.ams.entity.ArchivesBook.BarCode', NULL, '档案册条码', '', 6, 0, 0, '0', b'0', 'DEV3.0');
INSERT INTO `serial_number_config` VALUES ('c0a84203-631f-17fa-8163-491e1d670053', 'com.ecmp.fsop.ams.entity.request.StoreRequest', NULL, '入库/移库申请单', 'SR', 10, 0, 22, 'SR22', b'1', 'DEV3.0');
INSERT INTO `serial_number_config` VALUES ('c0a84203-631f-17fa-8163-85a6571f0098', 'com.kcmp.ck.basic.entity.Menu', NULL, '菜单', '', 5, 10520, 91284, '91284', b'0', 'DEV3.0');
INSERT INTO `serial_number_config` VALUES ('c0a84203-631f-17fa-8163-a450c9d400a1', 'com.ecmp.fsop.ams.entity.BorrowRequest', NULL, '借阅/归还申请单', 'B', 5, 1000, 1000, 'B01000', b'0', 'DEV3.0');
INSERT INTO `serial_number_config` VALUES ('c0a84203-631f-17fa-8163-d39c81e60287', 'com.ecmp.fsop.ams.entity.request.DestroyRequest', NULL, '销毁申请单', 'DR', 10, 0, 0, '0', b'1', 'DEV3.0');
INSERT INTO `serial_number_config` VALUES ('c0a84203-631f-17fa-8163-de90d79402fc', 'com.ecmp.fsop.ams.entity.request.MigrateRequest', NULL, '移交申请单', 'MR', 10, 0, 0, '0', b'1', 'DEV3.0');
INSERT INTO `serial_number_config` VALUES ('c0a84203-6401-1678-8164-0263e7c50000', 'com.ecmp.fsop.ams.entity.request.RectificationRequest', NULL, '整改申请单', 'RR', 10, 0, 0, '0', b'1', 'DEV3.0');
INSERT INTO `serial_number_config` VALUES ('c0a84211-6208-19cb-8162-2865afe100e2', 'com.ecmp.fsop.ams.entity.ArchivesCabinets', NULL, '档案柜', '', 10, 0, 13, '13', b'1', 'DEV3.0');
INSERT INTO `serial_number_config` VALUES ('c0a84211-6208-19cb-8162-2866341000e3', 'com.ecmp.fsop.ams.entity.CheckErrorCategories', NULL, '抽查错误类型', '', 10, 0, 0, '0', b'1', 'DEV3.0');
INSERT INTO `serial_number_config` VALUES ('c0a84211-6208-19cb-8162-2866a5c700e4', 'com.ecmp.fsop.ams.entity.ArchiveCategories', NULL, '档案分类', '', 10, 0, 5, '5', b'1', 'DEV3.0');
INSERT INTO `serial_number_config` VALUES ('c0a84211-6208-19cb-8162-2cb05c7e00eb', 'com.ecmp.fsop.eams.entity.DocumentCategory', NULL, '文档分类', '', 4, 1000, 1003, '1003', b'0', 'DEV3.0');
INSERT INTO `serial_number_config` VALUES ('c0a84211-6208-19cb-8162-5062669b0156', 'com.ecmp.fsop.eams.entity.Archive', NULL, '电子档案', 'E', 10, 0, 366, 'E366', b'1', 'DEV3.0');
INSERT INTO `serial_number_config` VALUES ('c0a84402-7916-1bea-8179-7d5d503a0004', 'com.kcmp.ck.basic.entity.Corporation', NULL, '公司', 'CP', 4, 0, 0, '', b'0', 'DEV3.0');
INSERT INTO `serial_number_config` VALUES ('c0a84405-64af-1390-8164-b35a419f0008', 'com.ecmp.fsop.ams.entity.ArchivesBook', NULL, '档案册条码', '', 6, 0, 0, '0', b'0', 'DEV3.0');
INSERT INTO `serial_number_config` VALUES ('c0a84406-7217-163a-8172-c67295140017', 'com.kcmp.ck.official.entity.Subject', NULL, '官网栏目', '', 5, 10000, 10008, '10008', b'0', 'DEV3.0');
INSERT INTO `serial_number_config` VALUES ('c0a84406-73a2-17ce-8173-b1ecbeb30000', 'com.kcmp.ck.notice.entity.request.NoticeRequest', NULL, '公告申请单', 'NR', 10, 1, 5, 'NR5', b'1', 'DEV3.0');
INSERT INTO `serial_number_config` VALUES ('c0a8440e-6c41-1c3b-816c-45ac695c0005', 'com.ecmp.fincloud.ias.drawinvoice.entity.dataentry.BusinessDocumentRequests', NULL, '发票申请单（编号）', '', 6, 0, 0, '', b'0', 'DEV3.0');
INSERT INTO `serial_number_config` VALUES ('c0a8440e-6c41-1c3b-816c-45ad9de20006', 'com.ecmp.fincloud.ias.drawinvoice.entity.invoicemanage.DrawHeaders', NULL, '发票红冲作废（编号）', '', 6, 0, 0, NULL, b'0', 'DEV3.0');
INSERT INTO `serial_number_config` VALUES ('c0a85605-7c26-152c-817c-34703c810000', 'com.ecmp.cadres.entity.Organize', NULL, '蓬溪县干部组织架构', 'px', 5, 10000, 10874, '10874', b'0', 'DEV3.0');
INSERT INTO `serial_number_config` VALUES ('c0a85605-7c6e-1c77-817c-9d0033f80000', 'com.ecmp.cadres.entity.CadreApply', NULL, '蓬溪县干部申请记录编号', 'SQ', 5, 10000, 0, '', b'0', 'DEV3.0');

SET FOREIGN_KEY_CHECKS = 1;
