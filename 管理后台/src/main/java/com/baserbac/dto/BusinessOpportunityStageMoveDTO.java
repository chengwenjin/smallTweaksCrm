package com.baserbac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "商机阶段转移参数")
public class BusinessOpportunityStageMoveDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "目标阶段ID不能为空")
    @Schema(description = "目标阶段ID", example = "2")
    private Long targetStageId;

    @Schema(description = "备注", example = "客户已确认需求")
    private String remark;
}
