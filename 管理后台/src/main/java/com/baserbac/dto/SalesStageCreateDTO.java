package com.baserbac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Schema(description = "销售阶段创建参数")
public class SalesStageCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "阶段编码不能为空")
    @Schema(description = "阶段编码", example = "initial_contact")
    private String stageCode;

    @NotBlank(message = "阶段名称不能为空")
    @Schema(description = "阶段名称", example = "初步接洽")
    private String stageName;

    @NotNull(message = "排序不能为空")
    @Schema(description = "排序号", example = "1")
    private Integer sortOrder;

    @Schema(description = "赢率（百分比：0-100）", example = "10")
    private BigDecimal winProbability;

    @Schema(description = "阶段描述", example = "初次接触客户，了解基本需求")
    private String description;

    @Schema(description = "是否启用：0-禁用 1-启用", example = "1")
    private Integer isEnabled;

    @Schema(description = "是否结束阶段：0-否 1-是", example = "0")
    private Integer isClosed;

    @Schema(description = "结束类型：1-赢单 2-输单 0-非结束阶段", example = "0")
    private Integer closeType;
}
