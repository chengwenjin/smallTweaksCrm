package com.baserbac.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baserbac.common.enums.ResultCode;
import com.baserbac.common.exception.BusinessException;
import com.baserbac.dto.CustomerTagCreateDTO;
import com.baserbac.entity.CrmCustomerTag;
import com.baserbac.mapper.CustomerTagMapper;
import com.baserbac.vo.CustomerTagVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerTagService {

    private final CustomerTagMapper customerTagMapper;

    public List<CustomerTagVO> getAllTags() {
        List<CrmCustomerTag> tags = customerTagMapper.selectList(
                new LambdaQueryWrapper<CrmCustomerTag>()
                        .eq(CrmCustomerTag::getIsEnabled, 1)
                        .orderByAsc(CrmCustomerTag::getSortOrder)
        );
        return tags.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    public List<CustomerTagVO> getTagsByCategory(String category) {
        List<CrmCustomerTag> tags = customerTagMapper.selectList(
                new LambdaQueryWrapper<CrmCustomerTag>()
                        .eq(CrmCustomerTag::getIsEnabled, 1)
                        .eq(CrmCustomerTag::getTagCategory, category)
                        .orderByAsc(CrmCustomerTag::getSortOrder)
        );
        return tags.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public Long createTag(CustomerTagCreateDTO createDTO) {
        CrmCustomerTag existTag = customerTagMapper.selectOne(
                new LambdaQueryWrapper<CrmCustomerTag>()
                        .eq(CrmCustomerTag::getTagName, createDTO.getTagName())
        );
        if (existTag != null) {
            throw new BusinessException("标签名称已存在");
        }

        CrmCustomerTag tag = new CrmCustomerTag();
        tag.setTagName(createDTO.getTagName());
        tag.setTagColor(createDTO.getTagColor() != null ? createDTO.getTagColor() : "#1890ff");
        tag.setTagCategory(createDTO.getTagCategory());
        tag.setSortOrder(createDTO.getSortOrder() != null ? createDTO.getSortOrder() : 0);
        tag.setDescription(createDTO.getDescription());
        tag.setIsEnabled(1);

        customerTagMapper.insert(tag);
        return tag.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteTag(Long id) {
        CrmCustomerTag tag = customerTagMapper.selectById(id);
        if (tag == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        customerTagMapper.deleteById(id);
    }

    private CustomerTagVO convertToVO(CrmCustomerTag tag) {
        return CustomerTagVO.builder()
                .id(tag.getId())
                .tagName(tag.getTagName())
                .tagColor(tag.getTagColor())
                .tagCategory(tag.getTagCategory())
                .sortOrder(tag.getSortOrder())
                .description(tag.getDescription())
                .isEnabled(tag.getIsEnabled())
                .createTime(tag.getCreateTime())
                .updateTime(tag.getUpdateTime())
                .build();
    }
}
