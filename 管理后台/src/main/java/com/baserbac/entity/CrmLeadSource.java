package com.baserbac.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 线索来源配置表实体
 */
@Data
@TableName("crm_lead_source")
public class CrmLeadSource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 来源编码（如：manual, excel, website, campaign）
     */
    private String sourceCode;

    /**
     * 来源名称（如：手工录入、Excel导入、官网表单、市场活动）
     */
    private String sourceName;

    /**
     * 来源类型：1-手动录入 2-批量导入 3-API对接
     */
    private Integer sourceType;

    /**
     * 是否系统内置：0-否 1-是
     */
    private Integer isSystem;

    /**
     * 是否启用：0-禁用 1-启用
     */
    private Integer isEnabled;

    /**
     * 排序号
     */
    private Integer sortOrder;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 预留扩展字段1
     */
    private String ext1;

    /**
     * 预留扩展字段2
     */
    private String ext2;

    /**
     * 预留扩展字段3
     */
    private String ext3;
}
