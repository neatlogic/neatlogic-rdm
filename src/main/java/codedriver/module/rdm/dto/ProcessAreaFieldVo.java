package codedriver.module.rdm.dto;

/**
 * @ClassName ProjectFieldVo
 * @Description 过程域属性类
 * @Auther
 * @Date 2019/12/4 15:58
 **/
public class ProcessAreaFieldVo extends FieldVo {

    /**
     * 过程域uuid
     */
    private String processAreaUuid;

    public ProcessAreaFieldVo(){

    }

    public ProcessAreaFieldVo(FieldVo _fieldVo){
        this.setName(_fieldVo.getName());
        this.setField(_fieldVo.getField());
        this.setUuid(_fieldVo.getUuid());
        this.setType(_fieldVo.getType());
        this.setConfig(_fieldVo.getConfig());
        this.setSort(_fieldVo.getSort());
    }

    public String getProcessAreaUuid() {
        return processAreaUuid;
    }

    public void setProcessAreaUuid(String processAreaUuid) {
        this.processAreaUuid = processAreaUuid;
    }
}
