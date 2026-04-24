package com.baserbac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "分配记录查询参数")
public class AssignRecordQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer pageNum = 1;
    private Integer pageSize = 10;

    @Schema(description = "线索编号")
    private String leadNo;

    @Schema(description = "线索名称")
    private String leadName;

    @Schema(description = "分配类型：1-自动分配 2-手动分配 3-回收")
    private Integer assignType;

    @Schema(description = "原负责人ID")
    private Long fromUserId;

    @Schema(description = "新负责人ID")
    private Long toUserId;

    @Schema(description = "分配开始时间")
    private LocalDateTime createTimeStart;

    @Schema(description = "分配结束时间")
    private LocalDateTime createTimeEnd;
}
