# Issue 邮件通知事件插件

## 商业能力要求

邮件事件插件、配置接口和前端编辑器属于 RDM 商业模块。只有商业组件已安装且授权有效时才提供事件配置与执行；未安装或未授权时不执行历史规则、不发送插件邮件，原通知策略继续运行。已有配置及审计不删除。后端实现位于 `neatlogic-rdm-commercial/src/main/java/neatlogic/module/rdm/cs`，前端位于 `neatlogic-web/src/commercial-module/rdm`。

## 使用方式

在项目的应用设置中打开事件设置，选择创建、更新、状态变更或删除事件，新增“邮件通知”配置。插件标识为 `ISSUE_EMAIL`，适用于使用 `IssueVo` 的应用；GitLab 使用其他管理对象，不支持此插件。

配置包括邮件服务器、收件用户、收件角色、抄送用户、抄送角色、标题和富文本正文。收件人和抄送人分别使用一个 UserSelect，同时选择系统用户、创建人和当前处理人；保存时仍拆分为现有用户 UUID 列表与角色列表，兼容已有配置。角色支持创建人和处理人；未指定服务器时使用系统默认服务器。收件人与抄送按邮箱去重，相同邮箱优先保留在收件人中。

变量列表按当前项目、应用查询。内置变量与自定义属性均由后端提供插入片段，自定义属性以属性 ID 标识，重命名不影响已有模板。正文普通字段进行 HTML 转义，需求正文作为富文本提供；标题禁止换行。

找不到用户或用户邮箱无效时记录跳过原因；最终没有有效主收件人时，本次执行标记跳过。指定的邮件服务器不存在或不可用、模板渲染失败、发送失败时明确报错，不静默换用其他服务器。

## 配置与接口

插件配置保存在既有事件配置的 `config` 中：

- `mailServerId`：可选邮件服务器 ID。
- `toUserUuidList`、`ccUserUuidList`：系统用户 UUID 列表。
- `toRoleList`、`ccRoleList`：`owner`（创建人）、`worker`（处理人）。
- `title`、`content`：标题与 HTML 正文模板。

配置增删改、启停、排序仍使用既有事件配置接口。新增的邮件变量与邮件服务器选项接口都检查项目负责人/所有者权限、应用归属和 Issue 事件适配范围：

- `/rdm/event/issue/email/variable/list`
- `/rdm/event/issue/email/mailserver/list`

邮件服务器选项仅返回名称、ID 和默认标记，不返回账号、密码或 SMTP 连接配置，不要求项目负责人取得全局通知配置权限。

## 扩展与执行边界

后端邮件插件继承 `RdmEventHandlerBase<IssueVo>`，业务对象保持明确类型；专用编辑器与查看器通过 RDM ComponentManager 扩展注册。首次上线不会写入默认配置或启用规则。

邮件专属配置校验、收件人解析、邮件服务器选择和发送集中在 `IssueSendMailEventHandler`，不再另建 `IssueEmailService` 或独立发送器接口。插件内部复用 `EmailUtil`，测试通过重写受保护的发送方法替换网络调用。邮件与集成实际共用的变量和模板处理通过 `IssueEventTemplateService` 接口和 `IssueEventTemplateServiceImpl` 实现提供。

服务器列表 API 直接通过 `@Resource` 注入 `NotifyConfigMapper`，先由 `RdmEventConfigService` 接口检查商业能力、项目权限及应用归属，再校验 Issue 应用范围。返回仅包含服务器 ID、名称、默认和激活状态，不返回 SMTP 配置。Handler 不提供 `listServers()` 等契约外公开查询方法，API 不通过注入或 Factory 调用具体插件查询。

邮件插件也可以配置在 `ISSUE_INTEGRATION` 的成功或失败子动作中，仍使用原有收件人和模板配置。它接收引擎提供的需求对象，不接收集成响应；父子执行审计通过事件框架关联。集成父节点失败不会撤销已经发送的邮件。

普通事件使用当前插件事务重载的最新对象；删除事件使用触发快照，邮件角色和属性解析不重新读取已删除的 Issue。发送复用框架 `EmailUtil`，使用已经校验的服务器配置，测试通过发送替身截获邮件。

现有通知策略与此插件独立运行，同时配置可能发送两封邮件。邮件已经送出后不能随数据库事务回滚；不提供自动重试、去重投递或恰好一次发送保证。审计保留结果、收件人数和跳过原因，不记录 SMTP 凭据。

本版不包含附件、外部邮箱输入、团队收件人、共享模板管理或发送频率限制。

## 历史验证结果（商业迁移前）

商业聚合编译通过：从 `neatlogic-build-root` 执行 `mvn -Pdevelop,commercial -pl ../neatlogic-rdm-commercial -am -DskipTests compile`。

实际运行 `IssueSendMailEventHandlerTest` 以及模块启动、事件配置服务、Issue API 触发、对象快照、插件兼容和持久化子树发布共 7 项独立测试，全部通过。发送替身覆盖默认/指定服务器、去重、无收件人跳过、禁用、删除快照、发送异常及真实基类审计；同时检查变量 ID 稳定、可读文本、HTML 转义、纯文本标题和 FreeMarker 非授权能力限制。`RdmEventContextTest` 验证模块刷新后自动注册邮件插件，未改用手动注册生产组件。

前端 `node scripts/test-rdm-event-config.cjs` 通过 14 个 Vue 2 模板及组件逻辑检查，包含邮件配置回显、保存校验、变量请求隔离和组件注册；限定本次修改文件的 ESLint 与差异检查通过。浏览器连接超时，未完成真实页面联调和浅深色主题渲染验收。没有发送真实邮件、改动租户数据或重启服务。


## 2026-09-15 API、Handler 与 Service 职责调整验证

配置服务和共享模板服务已采用接口与 `Impl` 实现；新增共享集成元数据服务，邮件服务器 API 直接查询 Mapper。插件不再公开查询方法，商业插件与 GitLab 适配器改用 `@Resource` 字段注入，事务保持默认 `@Transactional`。

商业聚合 `clean compile` 及最终源码聚合编译通过；8 项商业 main（配置、集成持久化执行、邮件、集成、Spring 上下文、授权 Bean 范围、架构与元数据、GitLab）全部通过。另运行 9 项基础框架和 3 项普通 Issue main 回归通过。真实 Spring 测试上下文验证三种 Service 接口解析与插件工厂注册；元数据测试覆盖安全字段投影、权限及应用范围拒绝、停用和缺失集成、商业能力关闭。

执行测试使用发送替身与内存持久化替身，不发送真实邮件、不调用外部集成、不修改数据库；未进行部署环境或真实数据库验收。本轮没有前端变更。
