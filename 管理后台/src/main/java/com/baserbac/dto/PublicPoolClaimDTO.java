package com.baserbac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "公海线索认领参数")
public class PublicPoolClaimDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "线索ID不能为空")
    @Schema(description = "线索ID", example = "1")
    private Long leadId;

    @Schema(description = "认领备注", example = "对这个客户比较熟悉")
    private String remark;
}
