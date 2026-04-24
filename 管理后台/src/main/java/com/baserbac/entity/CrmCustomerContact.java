package com.baserbac.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("crm_customer_contact")
public class CrmCustomerContact implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int GENDER_UNKNOWN = 0;
    public static final int GENDER_MALE = 1;
    public static final int GENDER_FEMALE = 2;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long customerId;

    private String contactName;

    private String contactPosition;

    private String contactDepartment;

    private String mobile;

    private String phone;

    private String email;

    private String wechat;

    private String qq;

    private Integer isKeyContact;

    private Integer isPrimary;

    private Integer sortOrder;

    private Integer gender;

    private LocalDate birthday;

    private String description;

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
