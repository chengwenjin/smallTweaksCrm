package com.baserbac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 线索查询DTO
 */
@Data
@Schema(description = "线索查询参数")
public class LeadQueryDTO extends PageQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "线索名称（模糊查询）")
    private String leadName;

    @Schema(description = "联系人姓名（模糊查询）")
    private String contactName;

    @Schema(description = "手机号（模糊查询）")
    private String contactMobile;

    @Schema(description = "线索来源ID")
    private Long sourceId;

    @Schema(description = "线索来源编码")
    private String sourceCode;

    @Schema(description = "线索等级：1-高 2-中 3-低")
    private Integer level;

    @Schema(description = "线索状态：0-新线索 1-跟进中 2-已转化 3-已无效 4-已回收")
    private Integer status;

    @Schema(description = "分配负责人ID")
    private Long assignUserId;

    @Schema(description = "创建开始时间")
    private LocalDateTime createTimeStart;

    @Schema(description = "创建结束时间")
    private LocalDateTime createTimeEnd;

    @Schema(description = "标签（模糊查询）")
    private String tags;
}
