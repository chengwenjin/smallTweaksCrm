package com.baserbac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Schema(description = "外勤轨迹查询参数")
public class FieldTrackQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "轨迹用户ID")
    private Long trackUserId;

    @Schema(description = "轨迹日期")
    private LocalDate trackDate;

    @Schema(description = "页码", defaultValue = "1")
    private Integer pageNum = 1;

    @Schema(description = "每页大小", defaultValue = "100")
    private Integer pageSize = 100;
}
