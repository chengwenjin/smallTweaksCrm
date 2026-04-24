package com.baserbac.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "分配记录VO")
public class AssignRecordVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long leadId;
    private String leadNo;
    private String leadName;
    private Long fromUserId;
    private String fromUserName;
    private Long toUserId;
    private String toUserName;
    private Integer assignType;
    private String assignTypeName;
    private Long ruleId;
    private String ruleName;
    private String reason;
    private LocalDateTime createTime;
}
