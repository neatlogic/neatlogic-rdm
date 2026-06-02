ALTER TABLE `rdm_issue` ADD COLUMN `source_issue_id` bigint NULL COMMENT '来源issue id';

ALTER TABLE `rdm_issue` ADD INDEX `idx_source_issue_id` (`source_issue_id`);
