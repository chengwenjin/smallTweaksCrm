package com.baserbac.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baserbac.common.enums.ResultCode;
import com.baserbac.common.exception.BusinessException;
import com.baserbac.dto.FollowRecordCreateDTO;
import com.baserbac.dto.FollowRecordQueryDTO;
import com.baserbac.entity.CrmFollowRecord;
import com.baserbac.entity.CrmTodoReminder;
import com.baserbac.mapper.FollowRecordMapper;
import com.baserbac.vo.FollowRecordVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FollowRecordService {

    private final FollowRecordMapper followRecordMapper;
    private final TodoReminderService todoReminderService;
    private final ObjectMapper objectMapper;

    public Page<FollowRecordVO> getFollowRecordPage(FollowRecordQueryDTO queryDTO) {
        Page<CrmFollowRecord> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        
        LambdaQueryWrapper<CrmFollowRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(queryDTO.getBusinessType() != null, CrmFollowRecord::getBusinessType, queryDTO.getBusinessType())
               .eq(queryDTO.getBusinessId() != null, CrmFollowRecord::getBusinessId, queryDTO.getBusinessId())
               .eq(queryDTO.getFollowUserId() != null, CrmFollowRecord::getFollowUserId, queryDTO.getFollowUserId())
               .eq(queryDTO.getFollowType() != null, CrmFollowRecord::getFollowType, queryDTO.getFollowType())
               .eq(queryDTO.getContentType() != null, CrmFollowRecord::getContentType, queryDTO.getContentType())
               .ge(queryDTO.getStartTime() != null, CrmFollowRecord::getCreateTime, queryDTO.getStartTime())
               .le(queryDTO.getEndTime() != null, CrmFollowRecord::getCreateTime, queryDTO.getEndTime())
               .orderByDesc(CrmFollowRecord::getCreateTime);
        
        Page<CrmFollowRecord> resultPage = followRecordMapper.selectPage(page, wrapper);
        
        Page<FollowRecordVO> voPage = new Page<>();
        voPage.setCurrent(resultPage.getCurrent());
        voPage.setSize(resultPage.getSize());
        voPage.setTotal(resultPage.getTotal());
        voPage.setRecords(resultPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList()));
        
        return voPage;
    }

    public List<FollowRecordVO> getFollowRecordsByBusiness(String businessType, Long businessId) {
        List<CrmFollowRecord> records = followRecordMapper.selectList(
                new LambdaQueryWrapper<CrmFollowRecord>()
                        .eq(CrmFollowRecord::getBusinessType, businessType)
                        .eq(CrmFollowRecord::getBusinessId, businessId)
                        .orderByDesc(CrmFollowRecord::getCreateTime)
                        .last("LIMIT 100")
        );
        return records.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    public FollowRecordVO getFollowRecordById(Long id) {
        CrmFollowRecord record = followRecordMapper.selectById(id);
        if (record == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        return convertToVO(record);
    }

    @Transactional(rollbackFor = Exception.class)
    public Long createFollowRecord(FollowRecordCreateDTO createDTO, Long userId, String userName) {
        followRecordMapper.update(
                null,
                new LambdaUpdateWrapper<CrmFollowRecord>()
                        .eq(CrmFollowRecord::getBusinessType, createDTO.getBusinessType())
                        .eq(CrmFollowRecord::getBusinessId, createDTO.getBusinessId())
                        .eq(CrmFollowRecord::getIsLast, 1)
                        .set(CrmFollowRecord::getIsLast, 0)
        );

        CrmFollowRecord record = new CrmFollowRecord();
        record.setBusinessType(createDTO.getBusinessType());
        record.setBusinessId(createDTO.getBusinessId());
        record.setFollowUserId(userId);
        record.setFollowUserName(userName);
        record.setFollowType(createDTO.getFollowType() != null ? createDTO.getFollowType() : CrmFollowRecord.FOLLOW_TYPE_OTHER);
        record.setContentType(createDTO.getContentType());
        record.setTextContent(createDTO.getTextContent());
        record.setVoiceUrl(createDTO.getVoiceUrl());
        record.setVoiceDuration(createDTO.getVoiceDuration());
        
        if (createDTO.getImageUrls() != null && !createDTO.getImageUrls().isEmpty()) {
            try {
                record.setImageUrls(objectMapper.writeValueAsString(createDTO.getImageUrls()));
            } catch (JsonProcessingException e) {
                log.error("序列化图片URL失败", e);
            }
        }
        
        record.setVideoUrl(createDTO.getVideoUrl());
        record.setVideoDuration(createDTO.getVideoDuration());
        record.setFileUrl(createDTO.getFileUrl());
        record.setFileName(createDTO.getFileName());
        record.setFileSize(createDTO.getFileSize());
        record.setLocationLatitude(createDTO.getLocationLatitude());
        record.setLocationLongitude(createDTO.getLocationLongitude());
        record.setLocationAddress(createDTO.getLocationAddress());
        record.setNextFollowTime(createDTO.getNextFollowTime());
        record.setNextFollowRemark(createDTO.getNextFollowRemark());
        record.setIsLast(1);

        followRecordMapper.insert(record);

        if (Boolean.TRUE.equals(createDTO.getCreateTodo())) {
            CrmTodoReminder todo = new CrmTodoReminder();
            todo.setTodoNo(generateTodoNo());
            todo.setUserId(userId);
            todo.setUserName(userName);
            todo.setTitle("跟进提醒 - " + (createDTO.getTextContent() != null ? 
                    (createDTO.getTextContent().length() > 20 ? createDTO.getTextContent().substring(0, 20) + "..." : createDTO.getTextContent()) : "跟进任务"));
            todo.setContent(createDTO.getTextContent());
            todo.setPriority(CrmTodoReminder.PRIORITY_MEDIUM);
            todo.setStatus(CrmTodoReminder.STATUS_PENDING);
            todo.setRemindTime(createDTO.getTodoRemindTime() != null ? createDTO.getTodoRemindTime() : createDTO.getNextFollowTime());
            todo.setEndTime(createDTO.getTodoEndTime());
            todo.setBusinessType(createDTO.getBusinessType());
            todo.setBusinessId(createDTO.getBusinessId());
            todo.setFollowRecordId(record.getId());
            todo.setRemindType(CrmTodoReminder.REMIND_TYPE_NOTIFICATION);
            todo.setRemindCount(0);
            todo.setIsRecurring(0);
            
            todoReminderService.createTodoReminderInternal(todo);
            record.setTodoId(todo.getId());
            followRecordMapper.updateById(record);
        }

        return record.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteFollowRecord(Long id) {
        CrmFollowRecord record = followRecordMapper.selectById(id);
        if (record == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        followRecordMapper.deleteById(id);
    }

    private FollowRecordVO convertToVO(CrmFollowRecord record) {
        List<String> imageUrls = null;
        if (record.getImageUrls() != null) {
            try {
                imageUrls = objectMapper.readValue(record.getImageUrls(), new TypeReference<List<String>>() {});
            } catch (JsonProcessingException e) {
                log.error("反序列化图片URL失败", e);
                imageUrls = Collections.emptyList();
            }
        }

        return FollowRecordVO.builder()
                .id(record.getId())
                .businessType(record.getBusinessType())
                .businessId(record.getBusinessId())
                .businessName(record.getBusinessName())
                .followUserId(record.getFollowUserId())
                .followUserName(record.getFollowUserName())
                .followType(record.getFollowType())
                .followTypeName(record.getFollowType() != null && record.getFollowType() >= 1 && record.getFollowType() <= 5
                        ? CrmFollowRecord.FOLLOW_TYPE_NAMES[record.getFollowType()] : null)
                .contentType(record.getContentType())
                .contentTypeName(record.getContentType() != null && record.getContentType() >= 1 && record.getContentType() <= 5
                        ? CrmFollowRecord.CONTENT_TYPE_NAMES[record.getContentType()] : null)
                .textContent(record.getTextContent())
                .voiceUrl(record.getVoiceUrl())
                .voiceDuration(record.getVoiceDuration())
                .imageUrls(imageUrls)
                .videoUrl(record.getVideoUrl())
                .videoDuration(record.getVideoDuration())
                .fileUrl(record.getFileUrl())
                .fileName(record.getFileName())
                .fileSize(record.getFileSize())
                .locationLatitude(record.getLocationLatitude())
                .locationLongitude(record.getLocationLongitude())
                .locationAddress(record.getLocationAddress())
                .nextFollowTime(record.getNextFollowTime())
                .nextFollowRemark(record.getNextFollowRemark())
                .todoId(record.getTodoId())
                .isLast(record.getIsLast())
                .createTime(record.getCreateTime())
                .updateTime(record.getUpdateTime())
                .build();
    }

    private String generateTodoNo() {
        return "TD" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }
}
