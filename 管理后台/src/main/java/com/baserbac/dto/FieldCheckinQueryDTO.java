package com.baserbac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Schema(description = "外勤签到查询参数")
public class FieldCheckinQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "签到人ID")
    private Long checkinUserId;

    @Schema(description = "签到类型：1-签到 2-签退")
    private Integer checkinType;

    @Schema(description = "关联业务类型")
    private String businessType;

    @Schema(description = "关联业务ID")
    private Long businessId;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "签到日期")
    private LocalDate checkinDate;

    @Schema(description = "是否异常")
    private Integer isAbnormal;

    @Schema(description = "页码", defaultValue = "1")
    private Integer pageNum = 1;

    @Schema(description = "每页大小", defaultValue = "10")
    private Integer pageSize = 10;
}
