ALTER TABLE `rdm_app_attr` ADD COLUMN `stat_key` varchar(100) NULL COMMENT '统计属性标识';

ALTER TABLE `rdm_app_attr` ADD UNIQUE KEY `uk_app_stat_key` (`app_id`, `stat_key`);
