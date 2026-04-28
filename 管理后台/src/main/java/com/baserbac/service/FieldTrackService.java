package com.baserbac.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baserbac.dto.FieldTrackQueryDTO;
import com.baserbac.entity.CrmFieldTrack;
import com.baserbac.mapper.FieldTrackMapper;
import com.baserbac.vo.FieldTrackVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FieldTrackService {

    private final FieldTrackMapper fieldTrackMapper;

    public Page<FieldTrackVO> getTrackPage(FieldTrackQueryDTO queryDTO) {
        Page<CrmFieldTrack> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        
        LambdaQueryWrapper<CrmFieldTrack> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(queryDTO.getTrackUserId() != null, CrmFieldTrack::getTrackUserId, queryDTO.getTrackUserId())
               .eq(queryDTO.getTrackDate() != null, CrmFieldTrack::getTrackDate, queryDTO.getTrackDate())
               .orderByAsc(CrmFieldTrack::getRecordTime);
        
        Page<CrmFieldTrack> resultPage = fieldTrackMapper.selectPage(page, wrapper);
        
        Page<FieldTrackVO> voPage = new Page<>();
        voPage.setCurrent(resultPage.getCurrent());
        voPage.setSize(resultPage.getSize());
        voPage.setTotal(resultPage.getTotal());
        voPage.setRecords(resultPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList()));
        
        return voPage;
    }

    public List<FieldTrackVO> getTracksByUserAndDate(Long userId, java.time.LocalDate date) {
        List<CrmFieldTrack> tracks = fieldTrackMapper.selectList(
                new LambdaQueryWrapper<CrmFieldTrack>()
                        .eq(CrmFieldTrack::getTrackUserId, userId)
                        .eq(CrmFieldTrack::getTrackDate, date)
                        .orderByAsc(CrmFieldTrack::getRecordTime)
        );
        return tracks.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    public void createTrackInternal(CrmFieldTrack track) {
        fieldTrackMapper.insert(track);
    }

    private FieldTrackVO convertToVO(CrmFieldTrack track) {
        return FieldTrackVO.builder()
                .id(track.getId())
                .trackNo(track.getTrackNo())
                .trackUserId(track.getTrackUserId())
                .trackUserName(track.getTrackUserName())
                .trackDate(track.getTrackDate())
                .latitude(track.getLatitude())
                .longitude(track.getLongitude())
                .locationAddress(track.getLocationAddress())
                .locationProvince(track.getLocationProvince())
                .locationCity(track.getLocationCity())
                .locationDistrict(track.getLocationDistrict())
                .accuracy(track.getAccuracy())
                .speed(track.getSpeed())
                .direction(track.getDirection())
                .recordTime(track.getRecordTime())
                .createTime(track.getCreateTime())
                .build();
    }
}
