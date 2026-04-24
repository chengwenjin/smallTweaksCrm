package com.baserbac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Schema(description = "线索查重参数")
public class LeadDuplicateCheckDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "线索ID(编辑时传入)", example = "1")
    private Long leadId;

    @Schema(description = "线索名称(企业名称)", example = "北京创新科技有限公司")
    private String leadName;

    @Schema(description = "手机号", example = "13800138000")
    private String contactMobile;

    @Schema(description = "检查类型：1-手机号 2-企业名称 3-两者都检查", example = "3")
    private Integer checkType;
}
