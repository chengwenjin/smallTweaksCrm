package com.baserbac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "客户更新参数")
public class CustomerUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "客户ID")
    private Long id;

    @Schema(description = "客户名称（企业名称）")
    private String customerName;

    @Schema(description = "客户简称")
    private String shortName;

    @Schema(description = "客户类型：1-企业客户 2-个人客户")
    private String customerType;

    @Schema(description = "统一社会信用代码")
    private String creditCode;

    @Schema(description = "法定代表人")
    private String legalPerson;

    @Schema(description = "注册资本（万元）")
    private BigDecimal registeredCapital;

    @Schema(description = "成立日期")
    private LocalDate establishDate;

    @Schema(description = "经营状态")
    private String businessStatus;

    @Schema(description = "经营范围")
    private String businessScope;

    @Schema(description = "注册地址")
    private String registeredAddress;

    @Schema(description = "所属行业")
    private String industry;

    @Schema(description = "省份")
    private String province;

    @Schema(description = "城市")
    private String city;

    @Schema(description = "区县")
    private String district;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "企业官网")
    private String website;

    @Schema(description = "企业邮箱")
    private String email;

    @Schema(description = "企业电话")
    private String phone;

    @Schema(description = "企业传真")
    private String fax;

    @Schema(description = "员工人数")
    private Integer employeeCount;

    @Schema(description = "年营收（万元）")
    private BigDecimal annualRevenue;

    @Schema(description = "企业规模描述")
    private String companyScale;

    @Schema(description = "客户等级：A、B、C、D")
    private String levelCode;

    @Schema(description = "负责人ID")
    private Long ownerUserId;

    @Schema(description = "标签（多个用逗号分隔）")
    private String tags;

    @Schema(description = "客户来源")
    private String source;

    @Schema(description = "客户状态：0-潜在 1-合作中 2-已流失 3-休眠")
    private Integer status;

    @Schema(description = "客户描述")
    private String description;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "上级客户ID（组织架构）")
    private Long parentCustomerId;
}
