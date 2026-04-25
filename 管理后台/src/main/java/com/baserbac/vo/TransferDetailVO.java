package com.baserbac.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferDetailVO {
    private Long id;
    private Long recordId;
    private String transferNo;
    private Integer resourceType;
    private String resourceTypeName;
    private Long resourceId;
    private String resourceNo;
    private String resourceName;
    private Long fromUserId;
    private String fromUserName;
    private Long toUserId;
    private String toUserName;
    private Integer transferStatus;
    private String transferStatusName;
    private LocalDateTime transferTime;
    private String remark;
}
