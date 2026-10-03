-- 升级事件定义前迁移所有根节点与子节点的事件标识；配置和审计保持原样。
-- 仅有明确 app_id 的规则才能确定所属应用；历史空应用范围本来不会被执行，保持原值待人工处理。
-- 旧服务节点仍按 ISSUE_* 查询，升级期间须停止旧节点，全部节点使用新代码后再恢复事件执行。
UPDATE `rdm_event_handler` h
INNER JOIN `rdm_app` a ON a.`id` = h.`app_id`
SET h.`event` = CONCAT(UPPER(a.`app_type`), SUBSTRING(h.`event`, 6))
WHERE h.`event` IN ('ISSUE_CREATE', 'ISSUE_DELETE', 'ISSUE_STATUS_CHANGE', 'ISSUE_UPDATE')
  AND a.`app_type` IN ('story', 'task', 'bug', 'testcase', 'testplan');
