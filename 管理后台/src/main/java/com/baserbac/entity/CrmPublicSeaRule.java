package com.baserbac.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("crm_public_sea_rule")
public class CrmPublicSeaRule implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int RULE_TYPE_FIRST_COME = 1;
    public static final int RULE_TYPE_ROTATE = 2;

    public static final String[] RULE_TYPE_NAMES = {"", "先抢先得", "定期轮换"};

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String ruleName;

    private Integer ruleType;

    private Integer rotateDays;

    private Integer claimLimitPerDay;

    private Integer claimLimitPerWeek;

    private Integer autoRecycleEnabled;

    private Integer autoRecycleDays;

    private Integer isEnabled;

    private String description;

    private Integer sortOrder;

    @TableLogic
    private Integer isDeleted;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private String ext1;

    private String ext2;

    private String ext3;
}
