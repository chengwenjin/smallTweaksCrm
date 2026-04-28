package com.baserbac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "跟进记录创建参数")
public class FollowRecordCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "业务类型不能为空")
    @Schema(description = "业务类型：customer-客户, lead-线索, opportunity-商机", required = true)
    private String businessType;

    @NotNull(message = "业务ID不能为空")
    @Schema(description = "业务ID", required = true)
    private Long businessId;

    @Schema(description = "跟进方式：1-电话 2-微信 3-邮件 4-拜访 5-其他")
    private Integer followType;

    @NotNull(message = "内容类型不能为空")
    @Schema(description = "内容类型：1-文本 2-语音 3-图片 4-视频 5-文件", required = true)
    private Integer contentType;

    @Schema(description = "文本内容")
    private String textContent;

    @Schema(description = "语音文件URL")
    private String voiceUrl;

    @Schema(description = "语音时长（秒）")
    private Integer voiceDuration;

    @Schema(description = "图片URL列表")
    private List<String> imageUrls;

    @Schema(description = "视频文件URL")
    private String videoUrl;

    @Schema(description = "视频时长（秒）")
    private Integer videoDuration;

    @Schema(description = "附件文件URL")
    private String fileUrl;

    @Schema(description = "附件文件名")
    private String fileName;

    @Schema(description = "附件文件大小（字节）")
    private Long fileSize;

    @Schema(description = "位置纬度")
    private BigDecimal locationLatitude;

    @Schema(description = "位置经度")
    private BigDecimal locationLongitude;

    @Schema(description = "位置地址")
    private String locationAddress;

    @Schema(description = "下次跟进时间")
    private LocalDateTime nextFollowTime;

    @Schema(description = "下次跟进备注")
    private String nextFollowRemark;

    @Schema(description = "是否创建待办提醒")
    private Boolean createTodo;

    @Schema(description = "待办提醒时间")
    private LocalDateTime todoRemindTime;

    @Schema(description = "待办截止时间")
    private LocalDateTime todoEndTime;
}
