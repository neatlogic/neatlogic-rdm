package codedriver.module.rdm.dto;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-01-06 10:25
 **/
public class ProjectGroupVo {
    private Long id;
    private String name;
    private String projectUuid;
    private String role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProjectUuid() {
        return projectUuid;
    }

    public void setProjectUuid(String projectUuid) {
        this.projectUuid = projectUuid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
