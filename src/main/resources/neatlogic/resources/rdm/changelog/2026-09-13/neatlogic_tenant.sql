-- 研发事件处理器配置；按项目和应用精确匹配，空范围不执行，子处理器由父插件显式触发。
CREATE TABLE IF NOT EXISTS `rdm_event_handler` (
  `id` bigint NOT NULL COMMENT '处理器配置id',
  `uuid` varchar(32) DEFAULT NULL COMMENT '配置引用标识',
  `parent_id` bigint DEFAULT NULL COMMENT '父处理器配置id，根处理器为空',
  `project_id` bigint DEFAULT NULL COMMENT '适用项目id，空范围不执行',
  `app_id` bigint DEFAULT NULL COMMENT '适用应用id，空范围不执行',
  `name` varchar(200) DEFAULT NULL COMMENT '处理器配置名称',
  `handler` varchar(100) NOT NULL COMMENT '插件唯一标识',
  `event` varchar(100) NOT NULL COMMENT '事件标识',
  `sort` int NOT NULL DEFAULT '0' COMMENT '执行顺序，相同时按id顺序执行',
  `config` longtext COMMENT '处理器JSON配置',
  `is_active` tinyint NOT NULL DEFAULT '1' COMMENT '是否启用',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_uuid` (`uuid`),
  KEY `idx_event_scope` (`event`,`project_id`,`app_id`),
  KEY `idx_parent_sort` (`parent_id`,`sort`,`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='研发事件处理器配置';

CREATE TABLE IF NOT EXISTS `rdm_event_plugin` (
  `name` varchar(100) NOT NULL COMMENT '插件唯一标识',
  `config` longtext COMMENT '插件全局JSON配置',
  `is_active` tinyint NOT NULL DEFAULT '1' COMMENT '是否启用',
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='研发事件插件全局配置';

CREATE TABLE IF NOT EXISTS `rdm_event_audit` (
  `id` bigint NOT NULL COMMENT '执行审计id',
  `parent_id` bigint DEFAULT NULL COMMENT '父执行审计id',
  `issue_id` bigint DEFAULT NULL COMMENT '历史需求id，仅用于旧审计迁移',
  `object_type` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '业务对象类型',
  `object_id` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '业务对象标识',
  `project_id` bigint DEFAULT NULL COMMENT '项目id',
  `app_id` bigint DEFAULT NULL COMMENT '应用id',
  `event` varchar(100) NOT NULL COMMENT '事件标识',
  `handler` varchar(100) NOT NULL COMMENT '插件唯一标识',
  `handler_name` varchar(200) DEFAULT NULL COMMENT '处理器配置名称快照',
  `event_handler_id` bigint NOT NULL COMMENT '处理器配置id',
  `start_time` datetime(3) NOT NULL COMMENT '执行开始时间',
  `end_time` datetime(3) DEFAULT NULL COMMENT '执行结束时间',
  `status` varchar(50) NOT NULL COMMENT '执行状态',
  `error` longtext COMMENT '失败或跳过原因',
  `config` longtext COMMENT '处理器JSON配置快照',
  `result` longtext COMMENT '插件执行结果',
  `server_id` int DEFAULT NULL COMMENT '执行服务节点',
  PRIMARY KEY (`id`),
  KEY `idx_object_id` (`project_id`,`app_id`,`object_type`,`object_id`,`parent_id`,`id`),
  KEY `idx_parent_id` (`parent_id`,`id`),
  KEY `idx_start_time` (`start_time`),
  KEY `idx_status_start` (`status`,`start_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='研发事件执行审计';

-- 同日期变更按 SQL 哈希补执行；新库已有字段或索引的重复错误由框架按幂等规则忽略。
ALTER TABLE `rdm_event_audit` ADD COLUMN `object_type` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '业务对象类型';
ALTER TABLE `rdm_event_audit` ADD COLUMN `object_id` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '业务对象标识';
ALTER TABLE `rdm_event_audit` MODIFY COLUMN `issue_id` bigint DEFAULT NULL COMMENT '历史需求id，仅用于旧审计迁移';
-- 仅补齐历史需求审计，保留已有通用对象定位值。
UPDATE `rdm_event_audit` SET `object_type` = 'issue', `object_id` = CAST(`issue_id` AS CHAR) WHERE `issue_id` IS NOT NULL AND `object_type` IS NULL AND `object_id` IS NULL;
ALTER TABLE `rdm_event_audit` ADD INDEX `idx_object_id` (`project_id`,`app_id`,`object_type`,`object_id`,`parent_id`,`id`);
ALTER TABLE `rdm_event_audit` DROP INDEX `idx_issue_start`;
-- 旧空范围配置保留供人工调整，但不会被引擎匹配或自动复制。
ALTER TABLE `rdm_event_handler` MODIFY COLUMN `project_id` bigint DEFAULT NULL COMMENT '适用项目id，空范围不执行';
ALTER TABLE `rdm_event_handler` MODIFY COLUMN `app_id` bigint DEFAULT NULL COMMENT '适用应用id，空范围不执行';
