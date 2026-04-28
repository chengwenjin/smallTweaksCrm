package com.baserbac.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baserbac.common.enums.ResultCode;
import com.baserbac.common.exception.BusinessException;
import com.baserbac.dto.FieldCheckinCreateDTO;
import com.baserbac.dto.FieldCheckinQueryDTO;
import com.baserbac.entity.CrmFieldCheckin;
import com.baserbac.entity.CrmFieldTrack;
import com.baserbac.mapper.FieldCheckinMapper;
import com.baserbac.vo.FieldCheckinVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FieldCheckinService {

    private final FieldCheckinMapper fieldCheckinMapper;
    private final FieldTrackService fieldTrackService;
    private final ObjectMapper objectMapper;

    public Page<FieldCheckinVO> getCheckinPage(FieldCheckinQueryDTO queryDTO) {
        Page<CrmFieldCheckin> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        
        LambdaQueryWrapper<CrmFieldCheckin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(queryDTO.getCheckinUserId() != null, CrmFieldCheckin::getCheckinUserId, queryDTO.getCheckinUserId())
               .eq(queryDTO.getCheckinType() != null, CrmFieldCheckin::getCheckinType, queryDTO.getCheckinType())
               .eq(queryDTO.getBusinessType() != null, CrmFieldCheckin::getBusinessType, queryDTO.getBusinessType())
               .eq(queryDTO.getBusinessId() != null, CrmFieldCheckin::getBusinessId, queryDTO.getBusinessId())
               .eq(queryDTO.getIsAbnormal() != null, CrmFieldCheckin::getIsAbnormal, queryDTO.getIsAbnormal())
               .ge(queryDTO.getStartTime() != null, CrmFieldCheckin::getCheckinTime, queryDTO.getStartTime())
               .le(queryDTO.getEndTime() != null, CrmFieldCheckin::getCheckinTime, queryDTO.getEndTime());
        
        if (queryDTO.getCheckinDate() != null) {
            LocalDateTime startOfDay = queryDTO.getCheckinDate().atStartOfDay();
            LocalDateTime endOfDay = queryDTO.getCheckinDate().atTime(LocalTime.MAX);
            wrapper.between(CrmFieldCheckin::getCheckinTime, startOfDay, endOfDay);
        }
        
        wrapper.orderByDesc(CrmFieldCheckin::getCheckinTime);
        
        Page<CrmFieldCheckin> resultPage = fieldCheckinMapper.selectPage(page, wrapper);
        
        Page<FieldCheckinVO> voPage = new Page<>();
        voPage.setCurrent(resultPage.getCurrent());
        voPage.setSize(resultPage.getSize());
        voPage.setTotal(resultPage.getTotal());
        voPage.setRecords(resultPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList()));
        
        return voPage;
    }

    public List<FieldCheckinVO> getTodayCheckins(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        
        List<CrmFieldCheckin> checkins = fieldCheckinMapper.selectList(
                new LambdaQueryWrapper<CrmFieldCheckin>()
                        .eq(CrmFieldCheckin::getCheckinUserId, userId)
                        .between(CrmFieldCheckin::getCheckinTime, startOfDay, endOfDay)
                        .orderByAsc(CrmFieldCheckin::getCheckinTime)
        );
        
        return checkins.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    public FieldCheckinVO getCheckinById(Long id) {
        CrmFieldCheckin checkin = fieldCheckinMapper.selectById(id);
        if (checkin == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        return convertToVO(checkin);
    }

    @Transactional(rollbackFor = Exception.class)
    public Long createCheckin(FieldCheckinCreateDTO createDTO, Long userId, String userName) {
        CrmFieldCheckin checkin = new CrmFieldCheckin();
        checkin.setCheckinNo(generateCheckinNo());
        checkin.setCheckinUserId(userId);
        checkin.setCheckinUserName(userName);
        checkin.setCheckinType(createDTO.getCheckinType() != null ? createDTO.getCheckinType() : CrmFieldCheckin.CHECKIN_TYPE_CHECKIN);
        checkin.setCheckinTime(LocalDateTime.now());
        checkin.setLatitude(createDTO.getLatitude());
        checkin.setLongitude(createDTO.getLongitude());
        checkin.setLocationAddress(createDTO.getLocationAddress());
        checkin.setLocationProvince(createDTO.getLocationProvince());
        checkin.setLocationCity(createDTO.getLocationCity());
        checkin.setLocationDistrict(createDTO.getLocationDistrict());
        
        if (createDTO.getPhotoUrls() != null && !createDTO.getPhotoUrls().isEmpty()) {
            try {
                checkin.setPhotoUrls(objectMapper.writeValueAsString(createDTO.getPhotoUrls()));
            } catch (JsonProcessingException e) {
                log.error("序列化照片URL失败", e);
            }
        }
        
        checkin.setBusinessType(createDTO.getBusinessType());
        checkin.setBusinessId(createDTO.getBusinessId());
        checkin.setBusinessName(createDTO.getBusinessName());
        checkin.setPurpose(createDTO.getPurpose());
        checkin.setRemark(createDTO.getRemark());
        checkin.setDeviceInfo(createDTO.getDeviceInfo());
        checkin.setIpAddress(createDTO.getIpAddress());
        checkin.setNetworkType(createDTO.getNetworkType());
        checkin.setBatteryLevel(createDTO.getBatteryLevel());
        checkin.setIsAbnormal(0);

        fieldCheckinMapper.insert(checkin);

        CrmFieldTrack track = new CrmFieldTrack();
        track.setTrackNo(generateTrackNo());
        track.setTrackUserId(userId);
        track.setTrackUserName(userName);
        track.setTrackDate(LocalDate.now());
        track.setLatitude(createDTO.getLatitude());
        track.setLongitude(createDTO.getLongitude());
        track.setLocationAddress(createDTO.getLocationAddress());
        track.setLocationProvince(createDTO.getLocationProvince());
        track.setLocationCity(createDTO.getLocationCity());
        track.setLocationDistrict(createDTO.getLocationDistrict());
        track.setRecordTime(LocalDateTime.now());
        
        fieldTrackService.createTrackInternal(track);

        return checkin.getId();
    }

    private FieldCheckinVO convertToVO(CrmFieldCheckin checkin) {
        List<String> photoUrls = null;
        if (checkin.getPhotoUrls() != null) {
            try {
                photoUrls = objectMapper.readValue(checkin.getPhotoUrls(), new TypeReference<List<String>>() {});
            } catch (JsonProcessingException e) {
                log.error("反序列化照片URL失败", e);
                photoUrls = Collections.emptyList();
            }
        }

        return FieldCheckinVO.builder()
                .id(checkin.getId())
                .checkinNo(checkin.getCheckinNo())
                .checkinUserId(checkin.getCheckinUserId())
                .checkinUserName(checkin.getCheckinUserName())
                .checkinType(checkin.getCheckinType())
                .checkinTypeName(checkin.getCheckinType() != null && checkin.getCheckinType() >= 1 && checkin.getCheckinType() <= 2
                        ? CrmFieldCheckin.CHECKIN_TYPE_NAMES[checkin.getCheckinType()] : null)
                .checkinTime(checkin.getCheckinTime())
                .latitude(checkin.getLatitude())
                .longitude(checkin.getLongitude())
                .locationAddress(checkin.getLocationAddress())
                .locationProvince(checkin.getLocationProvince())
                .locationCity(checkin.getLocationCity())
                .locationDistrict(checkin.getLocationDistrict())
                .photoUrls(photoUrls)
                .businessType(checkin.getBusinessType())
                .businessId(checkin.getBusinessId())
                .businessName(checkin.getBusinessName())
                .purpose(checkin.getPurpose())
                .remark(checkin.getRemark())
                .deviceInfo(checkin.getDeviceInfo())
                .ipAddress(checkin.getIpAddress())
                .networkType(checkin.getNetworkType())
                .batteryLevel(checkin.getBatteryLevel())
                .isAbnormal(checkin.getIsAbnormal())
                .abnormalReason(checkin.getAbnormalReason())
                .createTime(checkin.getCreateTime())
                .updateTime(checkin.getUpdateTime())
                .build();
    }

    private String generateCheckinNo() {
        return "CK" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }

    private String generateTrackNo() {
        return "TK" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }
}
