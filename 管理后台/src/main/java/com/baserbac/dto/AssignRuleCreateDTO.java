package com.baserbac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Schema(description = "分配规则创建参数")
public class AssignRuleCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "规则名称不能为空")
    @Schema(description = "规则名称", example = "北京地区高价值客户分配")
    private String ruleName;

    @Schema(description = "规则类型：1-地域规则 2-行业规则 3-规模规则", example = "1")
    private Integer ruleType;

    @Schema(description = "省份", example = "北京市")
    private String province;

    @Schema(description = "城市", example = "北京市")
    private String city;

    @Schema(description = "区县", example = "朝阳区")
    private String district;

    @Schema(description = "行业", example = "IT互联网")
    private String industry;

    @Schema(description = "最小员工数", example = "100")
    private Integer minEmployeeCount;

    @Schema(description = "最大员工数", example = "1000")
    private Integer maxEmployeeCount;

    @Schema(description = "最小年营收(万元)", example = "100.00")
    private BigDecimal minAnnualRevenue;

    @Schema(description = "最大年营收(万元)", example = "1000.00")
    private BigDecimal maxAnnualRevenue;

    @Schema(description = "分配目标用户ID", example = "2")
    private Long assignUserId;

    @Schema(description = "优先级(数字越小优先级越高)", example = "1")
    private Integer priority;

    @Schema(description = "是否启用：1-启用 0-禁用", example = "1")
    private Integer isEnabled;

    @Schema(description = "规则描述", example = "北京地区IT互联网行业的高价值客户分配给销售张三")
    private String description;
}
