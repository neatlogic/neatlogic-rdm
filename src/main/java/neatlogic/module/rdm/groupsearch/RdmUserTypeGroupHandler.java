/* Copyright (C) 2025 TechSure Co., Ltd. All Rights Reserved. */
package neatlogic.module.rdm.groupsearch;

import neatlogic.framework.rdm.enums.IssueGroupSearch;
import neatlogic.framework.rdm.enums.IssueUserType;
import neatlogic.framework.restful.groupsearch.core.GroupSearchOptionVo;
import neatlogic.framework.restful.groupsearch.core.GroupSearchVo;
import neatlogic.framework.restful.groupsearch.core.IGroupSearchHandler;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 通知策略中的RDM动态通知对象处理器。
 */
@Service
public class RdmUserTypeGroupHandler implements IGroupSearchHandler {
    @Override
    public String getName() {
        return IssueGroupSearch.RDMUSERTYPE.getValue();
    }

    @Override
    public String getLabel() {
        return IssueGroupSearch.RDMUSERTYPE.getText();
    }

    @Override
    public String getHeader() {
        return getName() + "#";
    }

    @Override
    public List<GroupSearchOptionVo> search(GroupSearchVo groupSearchVo) {
        List<GroupSearchOptionVo> resultList = new ArrayList<>();
        List<String> includeList = groupSearchVo.getIncludeList();
        for (IssueUserType userType : IssueUserType.values()) {
            String value = getHeader() + userType.getValue();
            boolean isIncluded = CollectionUtils.isNotEmpty(includeList) && includeList.contains(value);
            if (!userType.getIsShow() && !isIncluded) {
                continue;
            }
            if (StringUtils.isNotBlank(groupSearchVo.getKeyword()) && !userType.getText().contains(groupSearchVo.getKeyword())) {
                continue;
            }
            resultList.add(createOption(value, userType.getText()));
        }
        groupSearchVo.setPageSize(resultList.size());
        groupSearchVo.setRowNum(resultList.size());
        return resultList;
    }

    @Override
    public List<GroupSearchOptionVo> reload(GroupSearchVo groupSearchVo) {
        List<GroupSearchOptionVo> resultList = new ArrayList<>();
        if (CollectionUtils.isEmpty(groupSearchVo.getValueList())) {
            return resultList;
        }
        for (String value : groupSearchVo.getValueList()) {
            if (!value.startsWith(getHeader())) {
                continue;
            }
            String userTypeValue = value.substring(getHeader().length());
            String text = IssueUserType.getText(userTypeValue);
            if (StringUtils.isNotBlank(text)) {
                resultList.add(createOption(value, text));
            }
        }
        return resultList;
    }

    @Override
    public int getSort() {
        return 1;
    }

    @Override
    public Boolean isLimit() {
        return false;
    }

    private GroupSearchOptionVo createOption(String value, String text) {
        GroupSearchOptionVo optionVo = new GroupSearchOptionVo();
        optionVo.setValue(value);
        optionVo.setText(text);
        return optionVo;
    }
}
