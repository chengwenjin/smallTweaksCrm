package com.baserbac.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("crm_customer_follow")
public class CrmCustomerFollow implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int TYPE_PHONE = 1;
    public static final int TYPE_WECHAT = 2;
    public static final int TYPE_EMAIL = 3;
    public static final int TYPE_VISIT = 4;
    public static final int TYPE_OTHER = 5;

    public static final String[] TYPE_NAMES = {"", "电话", "微信", "邮件", "拜访", "其他"};

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long customerId;

    private Long followUserId;

    private String followUserName;

    private Integer followType;

    private String followContent;

    private LocalDateTime nextFollowTime;

    private String nextFollowRemark;

    private Integer isLast;

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
}
