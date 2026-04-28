package com.baserbac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "完成待办参数")
public class TodoCompleteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "完成备注")
    private String completeRemark;
}
