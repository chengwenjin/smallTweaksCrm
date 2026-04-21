package com.baserbac.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 线索操作日志表实体
 */
@Data
@TableName("crm_lead_log")
public class CrmLeadLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 线索ID
     */
    private Long leadId;

    /**
     * 线索编号
     */
    private String leadNo;

    /**
     * 操作类型：1-创建 2-编辑 3-分配 4-跟进 5-转化 6-无效 7-回收 8-删除
     */
    private Integer operateType;

    /**
     * 操作名称
     */
    private String operateName;

    /**
     * 操作内容（JSON格式存储变更前后数据）
     */
    private String operateContent;

    /**
     * 操作人ID
     */
    private Long operateUserId;

    /**
     * 操作人姓名
     */
    private String operateUserName;

    /**
     * 操作时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime operateTime;

    /**
     * 备注
     */
    private String remark;
}
