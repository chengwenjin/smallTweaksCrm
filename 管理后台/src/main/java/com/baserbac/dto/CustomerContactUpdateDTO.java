package com.baserbac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Schema(description = "客户联系人更新参数")
public class CustomerContactUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "联系人ID")
    private Long id;

    @Schema(description = "客户ID")
    private Long customerId;

    @Schema(description = "联系人姓名")
    private String contactName;

    @Schema(description = "联系人职位")
    private String contactPosition;

    @Schema(description = "联系人部门")
    private String contactDepartment;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "办公电话")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "微信号")
    private String wechat;

    @Schema(description = "QQ号")
    private String qq;

    @Schema(description = "是否关键联系人：0-否 1-是")
    private Integer isKeyContact;

    @Schema(description = "是否主联系人：0-否 1-是")
    private Integer isPrimary;

    @Schema(description = "排序号")
    private Integer sortOrder;

    @Schema(description = "性别：0-未知 1-男 2-女")
    private Integer gender;

    @Schema(description = "生日")
    private LocalDate birthday;

    @Schema(description = "联系人描述")
    private String description;
}
