package com.baserbac.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferRecordVO {
    private Long id;
    private String transferNo;
    private Integer transferType;
    private String transferTypeName;
    private Long fromUserId;
    private String fromUserName;
    private Long fromDepartmentId;
    private String fromDepartmentName;
    private Long toUserId;
    private String toUserName;
    private Long toDepartmentId;
    private String toDepartmentName;
    private Integer transferMethod;
    private String transferMethodName;
    private Integer customerCount;
    private Integer leadCount;
    private Integer contractCount;
    private Integer followCount;
    private Integer status;
    private String statusName;
    private String reason;
    private String remark;
    private Long operatorId;
    private String operatorName;
    private List<TransferDetailVO> details;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
