package com.baserbac.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("crm_assign_record")
public class CrmAssignRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long leadId;

    private String leadNo;

    private Long fromUserId;

    private String fromUserName;

    private Long toUserId;

    private String toUserName;

    private Integer assignType;

    private Long ruleId;

    private String reason;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
