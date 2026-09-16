# RDM 强类型事件引擎

## 业务接入

当前已接入需求保存、删除 API 和商业模块 GitLab Push 接收 API。应用设置已增加事件配置页，不覆盖导入、副本同步等内部写入入口。

```java
RdmEventManager.doEvent(projectId, appId, IssueEvents.UPDATED, issueVo);
```

事件定义 `RdmEventDefinition<T>` 将事件标识、允许的应用类型、对象标识及 `Class<T>` 和删除快照策略绑定，通过工厂获取 `IRdmEventObjectAdapter<T>`。`IssueEvents` 保留四个 `ISSUE_*` 标识；其他应用通过自己的 `IRdmEventDefinitionProvider` 注册其他 Vo 事件，同一个应用可以支持多种对象事件。

插件必须继承 `RdmEventHandlerBase<T>`，声明 `getObjectClass()`、支持事件与父插件标识，实现 `myTrigger(..., T object, ...)`。注册表拒绝绕过基类的实现，以保证事务、重载、身份验证与审计始终执行。插件方法直接接收具体 Vo，无需业务转换。

父插件在 `myTrigger` 中使用 `triggerChild(parentConfig, childConfig, object, audit)` 选择子配置；子配置必须属于相同项目、应用和事件，`parentId` 指向当前配置，且子插件声明允许此父插件。配置树和执行栈均检查循环。

适配器负责对象类型、已有 ID 提取、独立快照、业务重载与范围校验。引擎不会生成业务对象 ID，不使用 JSON/Map 传递管理对象。Issue 私有快照复制器直接复制字段，隔离日期、集合和原有动态属性，保留循环和共享引用；不支持的可变类型明确失败，需要扩展适配器。

## 执行语义

- 发布前检查项目、应用、注册定义与对象身份，制作独立快照；提交后按项目应用精确匹配配置，回滚不发布。空范围旧规则不再匹配。
- 单次事件按 `sort + id` 串行执行，各事件可并发；没有跨事件顺序保证。
- 每步普通事件在新业务事务内重新加载对象；对象已不存在则失败。删除事件使用快照。
- 插件必须返回非空同类型、同 ID、同归属对象。需要传到下一插件的普通业务变更必须通过 Service 保存，未保存的字段会被下次重载替换。
- 插件、提交或子执行失败停止当前链。子失败即使被父业务捕获，也会令父插件失败；已提交的前序或子插件不做补偿回滚。
- 审计开始与结束各自使用独立事务，父业务回滚不吞掉子审计。审计收尾失败记录错误日志，不改变已提交的业务结果。
- 沿用内存执行池，不提供自动重试、持久化投递、重启恢复。

## 存储与升级

审计定位采用 `projectId + appId + objectType + objectId`。对象 ID 为最多 255 字符的字符串，对象类型最多 100 字符，数据库区分大小写。插件配置和审计展示结果仍可以保存 JSON，但不是插件间业务载荷。

2026-09-13 SQL 更新新建定义，并为旧表补充字段、将 `issue_id` 改成可空、回填旧审计、调整索引。旧列暂留，运行代码不再读写它。升级沿用项目逐条 SQL 哈希执行、忽略已存在列/索引等规则，不能用普通 stop-on-error 脚本执行器直接替代生产升级机制。

本轮没有执行租户数据库升级。实际迁移验证仅使用非 3306 端口的独立临时 MySQL，检查新建、旧数据、重复执行及字符串标识；未启动完整生产 ChangelogUtil。

## 修改文件

`neatlogic-rdm-base/src/main/java/neatlogic/framework/rdm/`：

- `event/IRdmEventObjectAdapter.java`、`RdmEventDefinition.java`、`IRdmEventDefinitionProvider.java`、`RdmEventRegistry.java`：新增类型与注册契约。
- `event/IRdmEventHandler.java`、`RdmEventHandlerFactory.java`、`RdmEventHandlerBase.java`、`RdmEventManager.java`：重构执行链；删除 `RdmEventType.java`。
- `dto/IssueVo.java`：删除 `prevEventResult`；`RdmEventAuditVo.java`、`RdmEventHandlerVo.java`：通用审计及注册表名称。
- `dao/mapper/RdmEventMapper.java`、`RdmEventMapper.xml`：精确范围与通用对象查询。

`neatlogic-rdm/src/main/`：

- `java/neatlogic/module/rdm/event/IssueEvents.java`、`IssueEventObjectAdapter.java`、`IssueEventSnapshot.java`：Issue 接入。
- `resources/neatlogic/resources/rdm/changelog/2026-09-13/neatlogic_tenant.sql`、`sqldefine/tables/rdm_event_audit.json`、`rdm_event_handler.json`：表定义及迁移。

测试：base 模块 `RdmEventTestSupport`、`RdmEventSerialTest`、`RdmEventHandlerTest`、`RdmEventDispatchTest`、`RdmEventPersistenceTest`、`RdmEventCompileContractTest`、`RdmEventTypeSafetyTest`；业务模块 `IssueEventObjectAdapterTest`、`IssueEventsTest`。保留原有无关工作区改动。

## 验证

从 `neatlogic-build-root` 执行：

```shell
mvn -Pdevelop -pl ../neatlogic-rdm -am -DskipTests compile
```

8 个独立 main 测试已实际运行：编译类型正反例、串行失败、事务与审计、真实异步提交/回滚和快照、异构类型/父子/循环、真实 MyBatis 持久化、Issue 深拷贝、Issue 定义初始化。独立 main 不是 JUnit，不能将 Maven 的 `-DskipTests compile` 视为测试执行。

尚未完成生产应用启动、租户升级或用户验收。上面的 8 项验证对应框架重构阶段；本次应用接入验证见下文。


## 需求与 GitLab 的真实调用

需求在 `neatlogic-rdm/src/main/java/neatlogic/module/rdm/api/issue/SaveIssueApi.java` 完成保存、关联处理和完整对象读取后，复用已有通知条件发布创建、状态变更或普通更新事件；`DeleteIssueApi.java` 在删除及索引处理完成后传入删除前完整对象。

```java
RdmEventManager.doEvent(
    currentIssueVo.getProjectId(),
    currentIssueVo.getAppId(),
    IssueEvents.CREATED,
    currentIssueVo
);
```

新建仅发布创建事件；一次编辑若同时符合状态变化和普通更新条件，会发布两个独立事件，不保证二者异步执行顺序。四种事件都依赖现有 API 事务，回滚不会发布。需求事件目前沿用所有 `hasIssue` 应用的类型定义。

GitLab 在商业模块 `SaveWebhookDataApi.java` 完成现有 Webhook 保存和索引处理后，将已接受的 Push 转换成 `GitlabPushVo` 再发布：

```java
RdmEventManager.doEvent(
    appVo.getProjectId(),
    appVo.getId(),
    GitlabEvents.PUSH_RECEIVED,
    pushVo
);
```

GitLab DTO、解析器、事件定义、适配器、异常及测试全部位于 `neatlogic-rdm-commercial`。本地 Webhook 记录 ID 是引擎对象 ID；`projectId` 是 RDM 项目，`gitlabProjectId` 是 GitLab 项目。插件拿到强类型仓库、分支和提交列表，无需解析原始 JSON。每步执行仍通过已保存的 Webhook 重载，查询同时解压原有压缩报文。

插件实现方法中的对象类型分别如下，其余类型、事件支持和父插件声明遵循上述基类契约：

```java
// 需求插件继承 RdmEventHandlerBase<IssueVo>。
protected IssueVo myTrigger(RdmEventHandlerVo config, RdmEventPluginVo plugin,
        IssueVo issue, RdmEventAuditVo audit, RdmEventStatusVo status) {
    String name = issue.getName();
    return issue;
}

// GitLab 插件在商业模块继承 RdmEventHandlerBase<GitlabPushVo>。
protected GitlabPushVo myTrigger(RdmEventHandlerVo config, RdmEventPluginVo plugin,
        GitlabPushVo push, RdmEventAuditVo audit, RdmEventStatusVo status) {
    String ref = push.getRef();
    List<GitlabCommitVo> commits = push.getCommits();
    return push;
}
```

以上插件仅展示方法签名；本轮没有注册演示性生产插件或写入默认事件配置。没有匹配配置时不会执行插件。保留现有 GitLab Push 接受条件、鉴权及重复投递行为；不新增 Merge Request Hook 或去重机制。

商业模块编译从 `neatlogic-build-root` 执行：

```shell
mvn -Pdevelop,commercial -pl ../neatlogic-rdm-commercial -am -DskipTests compile
```


### 本次接入文件与验证（2026-09-14）

- 需求模块修改 `SaveIssueApi.java`、`DeleteIssueApi.java`，新增 `IssueEventApiTriggerTest.java`，更新本文档。
- 商业模块修改 `SaveWebhookDataApi.java`、`WebhookMapper.xml`；新增 `dto/gitlab/GitlabPushVo.java`、`GitlabCommitVo.java`，`event/gitlab/GitlabEvents.java`、`GitlabEventObjectAdapter.java`、`GitlabPushParser.java`，以及 `exception/gitlab` 下六个固定语义异常类。
- 商业模块新增 `GitlabPushEventTest.java`；没有向公共模块添加 GitLab 类，没有修改 SQL、租户数据或现有鉴权规则。

最终 `develop,commercial` Maven 聚合编译通过。两个独立 main 测试实际通过：

- `IssueEventApiTriggerTest` 调用真实保存、删除 API，代理数据库、索引和通知边界，使用真实事件提交回调；覆盖创建、状态、普通更新、双事件、同状态不触发，以及创建和删除的回滚不发布。
- `GitlabPushEventTest` 使用真实解析器、适配器、MyBatis 解压处理器、Spring 事务与异步队列，测试插件直接获取 `GitlabPushVo` 和提交对象。验证字段、嵌套深复制、归属与重载、提交后执行和回滚不发布；GitLab API 中保存→索引→发布的位置采用静态检查。

本轮未启动 HTTP 服务、连接 GitLab 或执行租户数据库操作；未进行完整环境部署与人工验收。


## 项目应用事件配置（2026-09-14）

应用设置在通知设置之后增加事件设置；GitLab 保留 Webhook 配置并增加同级事件设置。页面按后端返回的事件显示配置数量，支持配置树编辑、启停、删除、根配置排序及递归查看。没有事件或没有可用插件时显示空态。本次没有增加生产插件或默认配置。

所有接口都要求 `projectId`、`appId`，由 `RdmEventConfigService` 核对应用真实归属和项目负责人/所有者身份。接口如下（省略 `/api/rest` 前缀）：

| 接口 | 附加输入 | 返回 |
| --- | --- | --- |
| `/rdm/event/list` | 无 | 事件元数据及根配置数量 |
| `/rdm/event/plugin/list` | `event`、可选 `parentPlugin` | 当前范围可选插件元数据 |
| `/rdm/event/handler/list` | `event` | 根配置及完整子树 |
| `/rdm/event/handler/get` | `id` | 当前应用单个根配置及子树 |
| `/rdm/event/handler/save` | 根配置 Vo 及 `handlerList` | 保存后的根 ID |
| `/rdm/event/handler/delete` | `id` | 删除根配置及子树，保留审计 |
| `/rdm/event/handler/sort` | `event`、完整 `idList` | 保存根配置顺序 |

写操作使用应用行锁，保存根和子树采用同一事务。新配置追加到末尾，编辑保留顺序；不能通过编辑迁移应用、事件、插件或引用其他配置树的 ID/UUID。排序必须提交当前事件完整且无重复的根 ID 集合。历史缺失或失配插件标记不可用，仍可查看、删除。

### 插件后端扩展

插件继承 `RdmEventHandlerBase<T>`，除对象类型、支持事件及父插件声明外，可覆盖：

- `supportAppTypes()`：默认空集合表示不额外限制应用；仍需事件支持当前应用且对象类型精确匹配。
- `validateConfig(JSONObject config)`：校验业务配置，失败使用固定语义业务异常。列表适配、保存和实际执行共同使用框架适配规则；执行前再次校验业务配置。
- `requiresConfigEditor()`：有必需专用编辑器时返回 `true`。
- `makeupChildHandler(RdmEventHandlerVo handler)`：从插件配置中整理子配置至 `handlerList`。默认保持前端提交的子树；插件有内嵌分支配置时应自行提取对应子节点，框架随后统一校验范围、父引用、适配及循环。

`config` 只用于插件配置；事件业务对象继续使用明确的泛型 Vo。商业事件、插件和专属编辑器继续属于商业模块。

### 插件前端扩展

所属模块在自身 `import.js` 注册同名编辑器、查看器：

```javascript
ComponentManager.registerRdmEventHandlerEditComponent({
  pluginName: () => import('./pages/event/plugin-name/edit.vue')
});
ComponentManager.registerRdmEventHandlerViewComponent({
  pluginName: () => import('./pages/event/plugin-name/view.vue')
});
```

组件接收 `projectId`、`appId`、`event`、`handler`、`config`。编辑器通过 `update:config` 更新配置，也可实现 `save()` 返回配置；`valid()` 用于校验，可返回 Promise。公共组件承担名称、启停、子配置选择与递归编辑。无参数插件无需编辑器；声明必需编辑器但前端未注册时禁止新增和修改，不提供通用 JSON 编辑替代。

### 本次配置功能文件范围

- `neatlogic-rdm-base`：`IRdmEventHandler`、`RdmEventHandlerFactory`、配置与元数据 DTO、`RdmEventMapper`、独立适配异常及 `RdmEventHandlerCompatibilityTest`。
- `neatlogic-rdm-commercial`：`neatlogic.module.rdm.cs.api.event` 下配置与邮件元数据 API、`cs.service.event`、`cs.event.email`、`cs.dto.event.email`、`cs.exception.event` 及商业配置专项测试。普通 `neatlogic-rdm` 仅保留能力发现接口和 Issue 业务接入。
- `neatlogic-web`：普通 RDM 应用页签通过 ComponentManager 发现商业入口，配置页面和邮件组件位于 `src/commercial-module/rdm`。商业组件注册与后端能力查询均满足时才显示事件设置。


### 配置功能验证结果

最终商业模块聚合编译通过：`mvn -Pdevelop,commercial -pl ../neatlogic-rdm-commercial -am -DskipTests compile`。实际包含 `neatlogic-rdm-commercial` 及其依赖模块。

本次独立运行 8 项后端 main 测试通过：`RdmEventHandlerCompatibilityTest`、`RdmEventPersistedTreeDispatchTest`、`RdmEventConfigServiceTest`，以及 `RdmEventCompileContractTest`、`RdmEventSerialTest`、`RdmEventHandlerTest`、`RdmEventDispatchTest`、`RdmEventTypeSafetyTest` 回归。配置服务测试使用真实 Spring 事务代理和持久化替身，覆盖角色权限、整树保存回滚、排序、循环和 ID/UUID 隔离；没有连接租户数据库或验证真实数据库并发锁竞争。

复核补齐了正式发布路径的持久化子树加载：按父标识递归读取子节点，验证整树归属与适配后入队，父插件可直接从 `handlerList` 选择调用 `triggerChild`。测试验证真实提交及异步执行、父子审计、禁用子节点，以及跨应用和错误事件拒绝。

前端执行 `node scripts/test-rdm-event-config.cjs` 通过：12 个 Vue 2 模板编译及实际组件逻辑，覆盖缺失编辑器、空态、保存刷新、子配置增删改及排序、展开折叠、根配置排序删除与应用切换的过期响应隔离。限定本次文件的 ESLint 和差异格式检查通过。

浏览器连接超时，未完成登录页面联调、浅深色主题的实际渲染验收；页面使用公共主题样式，没有新增局部配色。未写入生产插件、默认配置或租户数据库。本轮实现与静态/自动化验证已完成，实际环境验收仍待进行。


### 适配器工厂与启动依赖

`RdmEventManager` 沿用 `@Service`，构造器只注入 `RdmEventMapper`。应用归属和类型通过共享 Mapper 查询 `rdm_app`，不需要应用解析器接口、外部 setter 或 `@Bean` 装配。

业务对象适配器沿用 NeatLogic 工厂模式：所属模块以 Spring 组件实现 `IRdmEventObjectAdapter<T>`，按当前规范应通过 `@Resource` 字段注入模块 Service 接口/Mapper（本轮已调整商业 GitLab 适配器；普通 Issue 适配器的历史构造器注入不在本轮修改范围）；`RdmEventObjectAdapterFactory` 使用 `@RootComponent` 和模块初始化监听统一发现组件，按稳定的 `objectType` 索引。工厂拒绝重复标识、缺失适配器和类型不一致，强制转换集中封装在经过 Class 校验的工厂边界。

`RdmEventDefinition<T>` 保存 `objectType` 和 `Class<T>`，不再保存手工构造的适配器实例。事件常量可以在模块初始化前声明，执行时通过工厂获取实际组件。例如需求定义使用 `"issue", IssueVo.class`；商业 GitLab 使用自己的对象标识与 `GitlabPushVo.class`。`IssueEvents`、`GitlabEvents` 只提供事件定义，不再承担适配器实例化、初始化或应用查询。

本次工厂调整已通过商业聚合编译、8 项 base 独立测试以及 6 项模块测试。新增 `RdmEventObjectAdapterFactoryTest` 验证模块自动发现、注册前读取定义元数据、异构类型查询、重复和类型失配拒绝；`RdmEventContextTest` 使用真实模块上下文验证根管理器启动与子模块刷新后工厂自动注册。Issue 和 GitLab 执行回归通过。本次未重跑需要隔离数据库的持久化测试，也未重启部署中的服务。


## Issue 邮件插件

后续已新增实际业务插件 `ISSUE_EMAIL`，适用于四种 Issue 事件，使用强类型 `IssueVo`。配置、模板变量及发送边界见 [Issue 邮件通知事件插件](issue-email-event.md)。插件不会自动生成或启用事件规则；不改变已有通知策略。

## Issue 调用集成插件

商业插件 `ISSUE_INTEGRATION` 支持输入参数模板和成功、失败子动作，邮件插件可作为两种分支的子动作。配置与事务边界见 [Issue 调用集成事件插件](issue-integration-event.md)。集成响应不进入业务对象，子插件仍通过框架接收强类型 `IssueVo`。

## 事件配置与执行商业边界

所有应用的事件配置和执行均要求 RDM 商业模块安装且授权有效。基础泛型引擎与共享持久化契约保留在 `neatlogic-rdm-base`，通过 `IRdmEventRuntimeCapability` 和 `RdmEventRuntimeCapabilityFactory` 发现唯一商业提供方。无提供方时能力默认关闭，普通需求生命周期无需引用商业实现，仍正常保存、删除并运行原通知策略。

商业提供方 `neatlogic.module.rdm.cs.event.RdmEventRuntimeCapability` 复用 `RdmAuthBean` 的授权结果与模块/全局到期判断。未授权时 `.cs` 下 API、Service、邮件插件和提供方由现有授权处理器移除。配置 Service 和邮件服务同时检查运行能力，不能通过已获取的服务引用绕过。工厂在模块关闭时注销提供方。

发布入口、事务提交后、异步执行和每步插件开始时检查能力。能力关闭后不启动后续动作，历史配置和审计保留，已提交动作及已发送邮件不撤销。API 路径、事件/插件标识和持久化配置字段不变，无数据库迁移。

商业测试入口已迁移为 `neatlogic.module.rdm.cs.event.IssueSendMailEventHandlerTest`、`neatlogic.module.rdm.cs.event.RdmEventContextTest` 和 `neatlogic.module.rdm.cs.service.event.RdmEventConfigServiceTest`；新增 `neatlogic.module.rdm.cs.event.RdmCommercialBeanScopeTest` 检查授权注册边界。前述历史验证结果不等同于本轮迁移验收，本轮编译与测试结果以实际运行记录为准。

### 本轮商业化验证（2026-09-14）

- 普通聚合 `mvn -Pdevelop -pl ../neatlogic-rdm -am -DskipTests clean compile` 与商业聚合 `mvn -Pdevelop,commercial -pl ../neatlogic-rdm-commercial -am -DskipTests compile` 均成功。普通输出目录仅保留能力查询 API，没有迁移前的商业配置类残留。
- 17 个独立 main 通过：9 个基础引擎用例（含新增 `RdmEventRuntimeCapabilityTest`）、3 个普通 Issue 用例、5 个商业用例（配置、邮件、容器发现/关闭、授权 Bean 边界、GitLab）。普通 Issue 用例及能力边界用例的运行 classpath 排除了商业模块。
- 授权边界测试使用进程内授权结果替身验证现有 Bean 移除机制；没有伪造或更改真实授权文件，也不代表部署环境的签名授权验收。
- 前端事件配置回归覆盖双条件四组合、能力失效、应用切换及原有邮件交互；限定 ESLint 与 Vue 模板编译通过。真实 webpack 限定入口编译排除了商业 RDM context，并禁止直接引用其路径，验证公共应用容器与七个应用编辑器可以独立编译；共享表单及其他业务组件作为 external，未执行全站构建。
- 未连接业务数据库、发送真实邮件或重启部署服务；浏览器交互及浅深色主题未验收。新前端商业目录遵循独立仓库布局，受父仓库忽略规则影响，交付时必须纳入对应商业前端仓库，不可只提交公共前端仓库。
