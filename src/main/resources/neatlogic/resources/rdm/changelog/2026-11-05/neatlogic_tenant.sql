ALTER TABLE `rdm_event_audit` MODIFY COLUMN `start_time` timestamp(3) NOT NULL COMMENT '执行开始时间';
ALTER TABLE `rdm_event_audit` MODIFY COLUMN `end_time` timestamp(3) NULL DEFAULT NULL COMMENT '执行结束时间';

ALTER TABLE `rdm_issue` MODIFY COLUMN `create_date` timestamp(3) NULL DEFAULT NULL COMMENT '创建日期';

ALTER TABLE `rdm_issue_audit` MODIFY COLUMN `input_time` timestamp(3) NULL DEFAULT NULL COMMENT '修改时间';

ALTER TABLE `rdm_issue_comment` MODIFY COLUMN `fcd` timestamp(3) NULL DEFAULT NULL;
ALTER TABLE `rdm_issue_comment` MODIFY COLUMN `lcd` timestamp(3) NULL DEFAULT NULL;

ALTER TABLE `rdm_webhook_data` MODIFY COLUMN `create_time` timestamp(3) NULL DEFAULT NULL;
