-- ----------------------------
-- Table structure for rdm_app
-- ----------------------------
CREATE TABLE IF NOT EXISTS `rdm_app`
(
    `id`         bigint NOT NULL COMMENT '对象id',
    `app_type`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '标题',
    `project_id` bigint                                                       DEFAULT NULL COMMENT '项目id',
    `sort`       int                                                          DEFAULT NULL COMMENT '排序',
    `is_active`  tinyint                                                      DEFAULT NULL COMMENT '是否激活',
    `config`     text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Table structure for rdm_app_attr
-- ----------------------------
CREATE TABLE IF NOT EXISTS `rdm_app_attr`
(
    `id`           bigint                                                        NOT NULL COMMENT 'id',
    `app_id`       bigint                                                        NOT NULL COMMENT '引用rdm_object的id',
    `type`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '值类型',
    `name`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '英文名',
    `label`        varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '录入时的label名称',
    `description`  varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci                        DEFAULT NULL COMMENT '描述',
    `validator_id` bigint                                                                               DEFAULT '0' COMMENT '验证器组件id，需要在代码中实现验证器',
    `is_required`  tinyint                                                       NOT NULL COMMENT '是否必填',
    `sort`         int                                                           NOT NULL               DEFAULT '1' COMMENT '排序',
    `is_private`   tinyint(1)                                                                           DEFAULT NULL COMMENT '私有属性，系统出厂自带不允许删除',
    `config`       text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '属性设置',
    `is_active`    tinyint                                                                              DEFAULT NULL COMMENT '是否激活',
    `show_type`    enum ('none','all','list','detail') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '显示方式',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_name` (`app_id`, `name`, `is_private`) USING BTREE,
    KEY `idx_name` (`name`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Table structure for rdm_app_catalog
-- ----------------------------
CREATE TABLE IF NOT EXISTS `rdm_app_catalog`
(
    `id`        bigint NOT NULL,
    `name`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `app_id`    bigint                                                        DEFAULT NULL,
    `parent_id` bigint                                                        DEFAULT NULL,
    `lft`       int                                                           DEFAULT NULL,
    `rht`       int                                                           DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_app_id` (`app_id`) USING BTREE,
    KEY `idx_parent_id` (`parent_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Table structure for rdm_app_priority
-- ----------------------------
CREATE TABLE IF NOT EXISTS `rdm_app_priority`
(
    `id`     bigint NOT NULL COMMENT '主键',
    `name`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '唯一标识',
    `label`  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '名称',
    `app_id` bigint                                                       DEFAULT NULL COMMENT '对象id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Table structure for rdm_app_status
-- ----------------------------
CREATE TABLE IF NOT EXISTS `rdm_app_status`
(
    `id`          bigint NOT NULL COMMENT '主键',
    `app_id`      bigint                                                        DEFAULT NULL COMMENT '对象id',
    `is_start`    tinyint                                                       DEFAULT '0',
    `is_end`      tinyint                                                       DEFAULT '0',
    `name`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '唯一标识',
    `label`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '名称',
    `sort`        int                                                           DEFAULT NULL COMMENT '排序',
    `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '说明',
    `color`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_obejct_id` (`app_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Table structure for rdm_app_status_rel
-- ----------------------------
CREATE TABLE IF NOT EXISTS `rdm_app_status_rel`
(
    `id`             bigint DEFAULT NULL,
    `from_status_id` bigint NOT NULL,
    `to_status_id`   bigint NOT NULL,
    `app_id`         bigint NOT NULL,
    `config`         longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '属性设置\n',
    PRIMARY KEY (`from_status_id`, `to_status_id`) USING BTREE,
    UNIQUE KEY `uk_id` (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Table structure for rdm_app_usersetting
-- ----------------------------
CREATE TABLE IF NOT EXISTS `rdm_app_usersetting`
(
    `user_id` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `app_id`  bigint                                                    NOT NULL,
    `config`  longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    PRIMARY KEY (`user_id`, `app_id`) USING BTREE,
    KEY `idx_app_id` (`app_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Table structure for rdm_favoriteissue
-- ----------------------------
CREATE TABLE IF NOT EXISTS `rdm_favoriteissue`
(
    `issue_id` bigint                                                    NOT NULL,
    `user_id`  char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    PRIMARY KEY (`issue_id`, `user_id`) USING BTREE,
    KEY `idx_userid` (`user_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Table structure for rdm_issue
-- ----------------------------
CREATE TABLE `rdm_issue`
(
    `id`          bigint NOT NULL,
    `name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '名称',
    `app_id`      bigint                                                        DEFAULT NULL COMMENT '应用id',
    `create_user` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     DEFAULT NULL COMMENT '创建者',
    `priority`    bigint                                                        DEFAULT NULL COMMENT '优先级id',
    `status`      bigint                                                        DEFAULT NULL COMMENT '状态id',
    `create_date` datetime(3)                                                   DEFAULT NULL COMMENT '创建日期',
    `content`     longtext COLLATE utf8mb4_general_ci COMMENT '描述',
    `catalog`     bigint                                                        DEFAULT NULL COMMENT '目录id',
    `parent_id`   bigint                                                        DEFAULT NULL COMMENT '父issue id',
    `iteration`   bigint                                                        DEFAULT NULL COMMENT '迭代',
    `start_date`  date                                                          DEFAULT NULL COMMENT '预计开始',
    `end_date`    date                                                          DEFAULT NULL COMMENT '预计结束',
    `timecost`    int                                                           DEFAULT NULL COMMENT '预计耗时',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_objectid` (`app_id`) USING BTREE,
    KEY `idx_createdate` (`create_date`) USING BTREE,
    KEY `idx_parent_id` (`parent_id`) USING BTREE,
    KEY `idx_iteration` (`iteration`) USING BTREE,
    KEY `idx_catalog` (`catalog`) USING BTREE,
    KEY `idx_user_id` (`create_user`) USING BTREE,
    KEY `idx_create_date` (`create_date`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Table structure for rdm_issue_audit
-- ----------------------------
CREATE TABLE IF NOT EXISTS `rdm_issue_audit`
(
    `id`           bigint NOT NULL COMMENT 'Id',
    `issue_id`     bigint                                                        DEFAULT NULL COMMENT '任务id',
    `attr_name`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '属性名称',
    `attr_id`      bigint                                                        DEFAULT NULL COMMENT '属性id',
    `old_value`    text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '旧值',
    `new_value`    text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '新值',
    `input_from`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '修改方式',
    `input_time`   datetime(3)                                                   DEFAULT NULL COMMENT '修改时间',
    `input_user`   char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     DEFAULT NULL COMMENT '修改用户',
    `rel_app_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '关系应用类型',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_issue_id` (`issue_id`) USING BTREE,
    KEY `idx_input_user` (`input_user`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Table structure for rdm_issue_comment
-- ----------------------------
CREATE TABLE IF NOT EXISTS `rdm_issue_comment`
(
    `id`        bigint NOT NULL,
    `issue_id`  bigint                                                    DEFAULT NULL COMMENT '任务id',
    `status`    bigint                                                    DEFAULT NULL COMMENT '回复时所在状态',
    `content`   longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '内容',
    `fcu`       char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `fcd`       datetime(3)                                               DEFAULT NULL,
    `lcd`       datetime(3)                                               DEFAULT NULL,
    `parent_id` bigint                                                    DEFAULT NULL COMMENT '回复评论id',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_issue_id` (`issue_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Table structure for rdm_issue_file
-- ----------------------------
CREATE TABLE IF NOT EXISTS `rdm_issue_file`
(
    `issue_id` bigint NOT NULL,
    `file_id`  bigint NOT NULL,
    PRIMARY KEY (`issue_id`, `file_id`) USING BTREE,
    KEY `idx_file_id` (`file_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Table structure for rdm_issue_rel
-- ----------------------------
CREATE TABLE IF NOT EXISTS `rdm_issue_rel`
(
    `from_issue_id` bigint NOT NULL,
    `to_issue_id`   bigint NOT NULL,
    `from_app_id`   bigint                                                                               DEFAULT NULL,
    `to_app_id`     bigint                                                                               DEFAULT NULL,
    `rel_type`      enum ('extend','relative','repeat') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '关系，从属关系是extend，关联是relative，相同是repeat',
    PRIMARY KEY (`from_issue_id`, `to_issue_id`) USING BTREE,
    KEY `idx_to_issue_id` (`to_issue_id`) USING BTREE,
    KEY `idx_to_app_id` (`to_app_id`) USING BTREE,
    KEY `idx_from_app_id` (`from_app_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Table structure for rdm_issue_tag
-- ----------------------------
CREATE TABLE IF NOT EXISTS `rdm_issue_tag`
(
    `issue_id` bigint NOT NULL,
    `tag_id`   bigint NOT NULL,
    PRIMARY KEY (`issue_id`, `tag_id`) USING BTREE,
    KEY `idx_tag_id` (`tag_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Table structure for rdm_issue_user
-- ----------------------------
CREATE TABLE IF NOT EXISTS `rdm_issue_user`
(
    `issue_id` bigint                                                    NOT NULL,
    `user_id`  char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    PRIMARY KEY (`issue_id`, `user_id`) USING BTREE,
    KEY `idx_userid` (`user_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Table structure for rdm_issue_webhookdata
-- ----------------------------
CREATE TABLE IF NOT EXISTS `rdm_issue_webhookdata`
(
    `issue_id` bigint NOT NULL,
    `data_id`  bigint NOT NULL,
    PRIMARY KEY (`issue_id`, `data_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Table structure for rdm_iteration
-- ----------------------------
CREATE TABLE IF NOT EXISTS `rdm_iteration`
(
    `id`          bigint NOT NULL,
    `name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `project_id`  bigint                                                        DEFAULT NULL,
    `start_date`  date                                                          DEFAULT NULL,
    `end_date`    date                                                          DEFAULT NULL,
    `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    `is_open`     tinyint                                                       DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Table structure for rdm_priority
-- ----------------------------
CREATE TABLE IF NOT EXISTS `rdm_priority`
(
    `id`    bigint NOT NULL,
    `name`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `color` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `sort`  int                                                           DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Table structure for rdm_project
-- ----------------------------
CREATE TABLE IF NOT EXISTS `rdm_project`
(
    `id`          bigint       NOT NULL COMMENT 'id',
    `name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '名称',
    `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '说明',
    `start_date`  date                                                          DEFAULT NULL COMMENT '开始日期',
    `end_date`    date                                                          DEFAULT NULL COMMENT '结束日期',
    `type`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '项目类型',
    `color`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '颜色标识',
    `template_id` bigint                                                        DEFAULT NULL COMMENT '模板',
    `fcd`         timestamp(3) NULL                                             DEFAULT NULL,
    `fcu`         char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     DEFAULT NULL,
    `lcd`         timestamp(3) NULL                                             DEFAULT NULL,
    `lcu`         char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     DEFAULT NULL,
    `is_close`    tinyint                                                       DEFAULT NULL COMMENT '是否关闭',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Table structure for rdm_project_status
-- ----------------------------
CREATE TABLE IF NOT EXISTS `rdm_project_status`
(
    `id`          bigint NOT NULL,
    `project_id`  bigint                                                        DEFAULT NULL,
    `is_start`    tinyint(1)                                                    DEFAULT '0',
    `is_end`      tinyint                                                       DEFAULT '0',
    `name`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `label`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `sort`        int                                                           DEFAULT NULL,
    `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `color`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

/*
 * Copyright(c) 2023 NeatLogic Co., Ltd. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

-- ----------------------------
-- Table structure for rdm_project_status_rel
-- ----------------------------
CREATE TABLE IF NOT EXISTS `rdm_project_status_rel`
(
    `id`             bigint NOT NULL,
    `from_status_id` bigint NOT NULL,
    `to_status_id`   bigint NOT NULL,
    `project_id`     bigint NOT NULL,
    PRIMARY KEY (`from_status_id`, `to_status_id`) USING BTREE,
    UNIQUE KEY `uk_idx` (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Table structure for rdm_project_template
-- ----------------------------
CREATE TABLE IF NOT EXISTS `rdm_project_template`
(
    `id`        bigint NOT NULL COMMENT 'id',
    `name`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '模板名称',
    `is_active` tinyint                                                      DEFAULT NULL COMMENT '是否激活',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Table structure for rdm_project_template_apptype
-- ----------------------------
CREATE TABLE IF NOT EXISTS `rdm_project_template_apptype`
(
    `template_id` bigint                                                       NOT NULL COMMENT '模板id',
    `app_type`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用类型',
    `sort`        int DEFAULT NULL COMMENT '排序',
    PRIMARY KEY (`template_id`, `app_type`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Table structure for rdm_project_user
-- ----------------------------
CREATE TABLE IF NOT EXISTS `rdm_project_user`
(
    `project_id` bigint                                                                            NOT NULL,
    `user_id`    char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci                         NOT NULL,
    `user_type`  enum ('member','leader','owner') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    PRIMARY KEY (`project_id`, `user_id`, `user_type`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Table structure for rdm_tag
-- ----------------------------
CREATE TABLE IF NOT EXISTS `rdm_tag`
(
    `id`   bigint NOT NULL,
    `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `idx_tag` (`name`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Table structure for rdm_webhook_config
-- ----------------------------
CREATE TABLE IF NOT EXISTS `rdm_webhook_config`
(
    `app_id`       bigint NOT NULL,
    `webhook_url`  char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `secret_token` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    PRIMARY KEY (`app_id`) USING BTREE,
    KEY `idx_url` (`webhook_url`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Table structure for rdm_webhook_data
-- ----------------------------
CREATE TABLE IF NOT EXISTS `rdm_webhook_data`
(
    `id`          bigint NOT NULL,
    `app_id`      bigint                                                        DEFAULT NULL,
    `create_time` datetime(3)                                                   DEFAULT NULL,
    `data`        longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    `event`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `user_id`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `email`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `user_name`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_app_id` (`app_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Table structure for rdm_issue_cost
-- ----------------------------

CREATE TABLE `rdm_issue_cost`
(
    `id`        bigint NOT NULL,
    `issue_id`  bigint DEFAULT NULL,
    `cost_date` date   DEFAULT NULL,
    `timecost`  int    DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_issue_id` (`issue_id`) USING BTREE,
    KEY `idx_cost_date` (`cost_date`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;