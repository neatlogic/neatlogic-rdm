package neatlogic.module.rdm.app;

import neatlogic.framework.rdm.app.IRdmAppCapabilityProvider;
import neatlogic.framework.rdm.app.RdmAppCapability;
import org.springframework.stereotype.Component;

import java.util.*;

/** 社区模块显式声明自身应用的 Schema 与工作项能力。 */
@Component
public class CommunityAppCapabilityProvider implements IRdmAppCapabilityProvider {
    /** 迭代和其他非工作项应用不声明这两项能力。 */
    @Override
    public Map<String, Set<RdmAppCapability>> getCapabilities() {
        Map<String, Set<RdmAppCapability>> result = new LinkedHashMap<>();
        for (String type : Arrays.asList("story", "task", "bug")) {
            result.put(type, EnumSet.of(RdmAppCapability.OBJECT_SCHEMA, RdmAppCapability.WORK_ITEM));
        }
        return result;
    }
}
