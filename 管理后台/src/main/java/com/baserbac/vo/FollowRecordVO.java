package com.baserbac.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowRecordVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String businessType;

    private Long businessId;

    private String businessName;

    private Long followUserId;

    private String followUserName;

    private Integer followType;

    private String followTypeName;

    private Integer contentType;

    private String contentTypeName;

    private String textContent;

    private String voiceUrl;

    private Integer voiceDuration;

    private List<String> imageUrls;

    private String videoUrl;

    private Integer videoDuration;

    private String fileUrl;

    private String fileName;

    private Long fileSize;

    private BigDecimal locationLatitude;

    private BigDecimal locationLongitude;

    private String locationAddress;

    private LocalDateTime nextFollowTime;

    private String nextFollowRemark;

    private Long todoId;

    private Integer isLast;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
