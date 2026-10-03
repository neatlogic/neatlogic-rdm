package neatlogic.module.rdm.event;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.rdm.app.RdmAppCapability;
import neatlogic.framework.rdm.app.RdmAppCapabilityRegistry;
import neatlogic.framework.rdm.dto.AppVo;
import neatlogic.framework.rdm.dto.IssueVo;
import neatlogic.framework.rdm.event.RdmEventDefinition;
import neatlogic.module.rdm.app.CommunityAppCapabilityProvider;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** 无 Web 容器时验证事件与应用能力分别显式注册。 */
public class IssueEventsTest {
    /** 每个社区工作项应用必须只拥有自身的四种强类型事件。 */
    public static void main(String[] args) {
        List<RdmEventDefinition<?>> events = new IssueEvents().getEvents();
        if (events.size() != 12) { throw new AssertionError("社区应注册十二个应用事件"); }
        Set<String> names = new HashSet<>();
        for (RdmEventDefinition<?> event : events) {
            names.add(event.getName());
            String type = event.getName().substring(0, event.getName().indexOf('_')).toLowerCase();
            if (event.getObjectClass() != IssueVo.class || !event.getAppTypes().equals(java.util.Collections.singleton(type))) {
                throw new AssertionError("事件必须只绑定所属应用，IssueVo 仅作为数据类型");
            }
            if (event.isDeleted() != event.getName().endsWith("_DELETE")) {
                throw new AssertionError("仅删除事件使用删除快照");
            }
        }
        for (String type : Arrays.asList("STORY", "TASK", "BUG")) {
            for (String action : Arrays.asList("CREATE", "DELETE", "STATUS_CHANGE", "UPDATE")) {
                if (!names.contains(type + "_" + action)) { throw new AssertionError("缺少应用事件"); }
            }
        }
        CommunityAppCapabilityProvider provider = new CommunityAppCapabilityProvider();
        provider.getCapabilities().forEach(RdmAppCapabilityRegistry::register);
        for (String type : Arrays.asList("story", "task", "bug")) {
            AppVo app = new AppVo(); app.setType(type);
            if (!app.getCapabilities().containsAll(Arrays.asList("OBJECT_SCHEMA", "WORK_ITEM"))
                    || !RdmAppCapabilityRegistry.has(type, RdmAppCapability.WORK_ITEM)) {
                throw new AssertionError("工作项能力未注册");
            }
            String payload = JSONObject.toJSONString(app);
            if (!payload.contains("\"capabilities\"") || payload.contains("\"hasIssue\"")) {
                throw new AssertionError("应用接口必须返回能力列表并移除 hasIssue");
            }
        }
        AppVo iteration = new AppVo(); iteration.setType("iteration");
        if (!iteration.getCapabilities().isEmpty()) { throw new AssertionError("迭代不应具有工作项能力"); }
        System.out.println("IssueEventsTest PASSED");
    }
}
