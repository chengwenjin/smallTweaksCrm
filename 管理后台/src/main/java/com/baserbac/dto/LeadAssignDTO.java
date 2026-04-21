package com.baserbac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 线索分配 DTO
 */
@Data
@Schema(description = "线索分配参数")
public class LeadAssignDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "负责人ID不能为空")
    @Schema(description = "分配负责人ID", example = "2")
    private Long assignUserId;

    @Schema(description = "备注", example = "分配给张三跟进")
    private String remark;
}
