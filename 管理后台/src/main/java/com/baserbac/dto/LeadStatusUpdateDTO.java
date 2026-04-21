package com.baserbac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 线索状态更新 DTO
 */
@Data
@Schema(description = "线索状态更新参数")
public class LeadStatusUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "状态不能为空")
    @Schema(description = "线索状态：0-新线索 1-跟进中 2-已转化 3-已无效 4-已回收", example = "1")
    private Integer status;

    @Schema(description = "备注", example = "已联系客户，正在跟进")
    private String remark;
}
