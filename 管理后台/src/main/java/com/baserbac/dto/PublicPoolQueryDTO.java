package com.baserbac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "公海池查询参数")
public class PublicPoolQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer pageNum = 1;
    private Integer pageSize = 10;

    @Schema(description = "线索名称")
    private String leadName;

    @Schema(description = "手机号")
    private String contactMobile;

    @Schema(description = "行业")
    private String industry;

    @Schema(description = "来源")
    private Long sourceId;

    @Schema(description = "等级")
    private Integer level;

    @Schema(description = "回收类型：1-长期未跟进 2-跟进无效 3-手动回收")
    private Integer recycleType;

    @Schema(description = "是否已认领：1-是 0-否")
    private Integer isClaimed;

    @Schema(description = "线索状态")
    private Integer status;

    @Schema(description = "回收开始时间")
    private LocalDateTime recycleTimeStart;

    @Schema(description = "回收结束时间")
    private LocalDateTime recycleTimeEnd;
}
