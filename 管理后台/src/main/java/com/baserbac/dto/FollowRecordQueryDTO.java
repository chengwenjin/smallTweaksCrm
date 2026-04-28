package com.baserbac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "跟进记录查询参数")
public class FollowRecordQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "业务类型：customer-客户, lead-线索, opportunity-商机")
    private String businessType;

    @Schema(description = "业务ID")
    private Long businessId;

    @Schema(description = "跟进人ID")
    private Long followUserId;

    @Schema(description = "跟进方式")
    private Integer followType;

    @Schema(description = "内容类型")
    private Integer contentType;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "页码", defaultValue = "1")
    private Integer pageNum = 1;

    @Schema(description = "每页大小", defaultValue = "10")
    private Integer pageSize = 10;
}
