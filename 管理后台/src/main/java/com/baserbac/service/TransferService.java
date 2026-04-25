package com.baserbac.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baserbac.common.enums.ResultCode;
import com.baserbac.common.exception.BusinessException;
import com.baserbac.common.result.PageResult;
import com.baserbac.dto.TransferCreateDTO;
import com.baserbac.dto.TransferRecordQueryDTO;
import com.baserbac.entity.*;
import com.baserbac.mapper.*;
import com.baserbac.vo.TransferDetailVO;
import com.baserbac.vo.TransferRecordVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferService {

    private final TransferRecordMapper transferRecordMapper;
    private final TransferDetailMapper transferDetailMapper;
    private final CustomerMapper customerMapper;
    private final LeadMapper leadMapper;
    private final UserMapper userMapper;

    public List<CrmCustomer> getUserCustomers(Long userId) {
        return customerMapper.selectList(
                new LambdaQueryWrapper<CrmCustomer>()
                        .eq(CrmCustomer::getOwnerUserId, userId)
                        .eq(CrmCustomer::getIsDeleted, 0)
                        .orderByDesc(CrmCustomer::getCreateTime)
        );
    }

    public List<CrmLead> getUserLeads(Long userId) {
        return leadMapper.selectList(
                new LambdaQueryWrapper<CrmLead>()
                        .eq(CrmLead::getAssignUserId, userId)
                        .eq(CrmLead::getIsPublic, 0)
                        .eq(CrmLead::getIsDeleted, 0)
                        .orderByDesc(CrmLead::getCreateTime)
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public TransferRecordVO resignTransfer(Long fromUserId, Long toUserId, String reason, Long operatorId, String operatorName) {
        List<Long> customerIds = new ArrayList<>();
        List<Long> leadIds = new ArrayList<>();

        Long customerCount = customerMapper.selectCount(
                new LambdaQueryWrapper<CrmCustomer>()
                        .eq(CrmCustomer::getOwnerUserId, fromUserId)
                        .eq(CrmCustomer::getIsDeleted, 0)
        );
        if (customerCount > 0) {
            List<CrmCustomer> customers = customerMapper.selectList(
                    new LambdaQueryWrapper<CrmCustomer>()
                            .select(CrmCustomer::getId)
                            .eq(CrmCustomer::getOwnerUserId, fromUserId)
                            .eq(CrmCustomer::getIsDeleted, 0)
            );
            customerIds = customers.stream().map(CrmCustomer::getId).collect(Collectors.toList());
        }

        Long leadCount = leadMapper.selectCount(
                new LambdaQueryWrapper<CrmLead>()
                        .eq(CrmLead::getAssignUserId, fromUserId)
                        .eq(CrmLead::getIsPublic, 0)
                        .eq(CrmLead::getIsDeleted, 0)
        );
        if (leadCount > 0) {
            List<CrmLead> leads = leadMapper.selectList(
                    new LambdaQueryWrapper<CrmLead>()
                            .select(CrmLead::getId)
                            .eq(CrmLead::getAssignUserId, fromUserId)
                            .eq(CrmLead::getIsPublic, 0)
                            .eq(CrmLead::getIsDeleted, 0)
            );
            leadIds = leads.stream().map(CrmLead::getId).collect(Collectors.toList());
        }

        if (customerIds.isEmpty() && leadIds.isEmpty()) {
            throw new BusinessException("该用户名下无资源可转移");
        }

        SysUser fromUser = userMapper.selectById(fromUserId);
        SysUser toUser = toUserId != null ? userMapper.selectById(toUserId) : null;

        CrmTransferRecord record = new CrmTransferRecord();
        record.setTransferNo(generateTransferNo());
        record.setTransferType(CrmTransferRecord.TRANSFER_TYPE_RESIGN);
        record.setTransferTypeName(CrmTransferRecord.TRANSFER_TYPE_NAMES[CrmTransferRecord.TRANSFER_TYPE_RESIGN]);
        record.setFromUserId(fromUserId);
        record.setFromUserName(fromUser != null ? (fromUser.getRealName() != null ? fromUser.getRealName() : fromUser.getUsername()) : "未知");
        if (toUserId != null) {
            record.setToUserId(toUserId);
            record.setToUserName(toUser != null ? (toUser.getRealName() != null ? toUser.getRealName() : toUser.getUsername()) : "未知");
        }
        record.setTransferMethod(CrmTransferRecord.TRANSFER_METHOD_ALL);
        record.setCustomerCount(customerIds.size());
        record.setLeadCount(leadIds.size());
        record.setContractCount(0);
        record.setFollowCount(0);
        record.setStatus(CrmTransferRecord.STATUS_COMPLETED);
        record.setReason(StrUtil.isNotBlank(reason) ? reason : "员工离职回收");
        record.setOperatorId(operatorId);
        record.setOperatorName(operatorName);

        transferRecordMapper.insert(record);

        List<CrmTransferDetail> details = new ArrayList<>();

        for (Long customerId : customerIds) {
            CrmTransferDetail detail = new CrmTransferDetail();
            detail.setRecordId(record.getId());
            detail.setResourceType(CrmTransferDetail.RESOURCE_TYPE_CUSTOMER);
            detail.setResourceTypeName(CrmTransferDetail.RESOURCE_TYPE_NAMES[CrmTransferDetail.RESOURCE_TYPE_CUSTOMER]);
            detail.setResourceId(customerId);
            detail.setFromUserId(fromUserId);
            detail.setFromUserName(fromUser != null ? (fromUser.getRealName() != null ? fromUser.getRealName() : fromUser.getUsername()) : "未知");
            if (toUserId != null) {
                detail.setToUserId(toUserId);
                detail.setToUserName(toUser != null ? (toUser.getRealName() != null ? toUser.getRealName() : toUser.getUsername()) : "未知");
            }
            details.add(detail);
        }

        for (Long leadId : leadIds) {
            CrmTransferDetail detail = new CrmTransferDetail();
            detail.setRecordId(record.getId());
            detail.setResourceType(CrmTransferDetail.RESOURCE_TYPE_LEAD);
            detail.setResourceTypeName(CrmTransferDetail.RESOURCE_TYPE_NAMES[CrmTransferDetail.RESOURCE_TYPE_LEAD]);
            detail.setResourceId(leadId);
            detail.setFromUserId(fromUserId);
            detail.setFromUserName(fromUser != null ? (fromUser.getRealName() != null ? fromUser.getRealName() : fromUser.getUsername()) : "未知");
            if (toUserId != null) {
                detail.setToUserId(toUserId);
                detail.setToUserName(toUser != null ? (toUser.getRealName() != null ? toUser.getRealName() : toUser.getUsername()) : "未知");
            }
            details.add(detail);
        }

        if (!details.isEmpty()) {
            for (CrmTransferDetail detail : details) {
                transferDetailMapper.insert(detail);
            }
        }

        if (!customerIds.isEmpty() && toUserId != null) {
            customerMapper.update(null,
                    new LambdaUpdateWrapper<CrmCustomer>()
                            .in(CrmCustomer::getId, customerIds)
                            .set(CrmCustomer::getOwnerUserId, toUserId)
                            .set(CrmCustomer::getOwnerUserName, toUser != null ? (toUser.getRealName() != null ? toUser.getRealName() : toUser.getUsername()) : null)
            );
        } else if (!customerIds.isEmpty()) {
            customerMapper.update(null,
                    new LambdaUpdateWrapper<CrmCustomer>()
                            .in(CrmCustomer::getId, customerIds)
                            .set(CrmCustomer::getOwnerUserId, null)
                            .set(CrmCustomer::getOwnerUserName, null)
            );
        }

        if (!leadIds.isEmpty() && toUserId != null) {
            leadMapper.update(null,
                    new LambdaUpdateWrapper<CrmLead>()
                            .in(CrmLead::getId, leadIds)
                            .set(CrmLead::getAssignUserId, toUserId)
                            .set(CrmLead::getAssignUserName, toUser != null ? (toUser.getRealName() != null ? toUser.getRealName() : toUser.getUsername()) : null)
                            .set(CrmLead::getIsPublic, 0)
            );
        } else if (!leadIds.isEmpty()) {
            leadMapper.update(null,
                    new LambdaUpdateWrapper<CrmLead>()
                            .in(CrmLead::getId, leadIds)
                            .set(CrmLead::getAssignUserId, null)
                            .set(CrmLead::getAssignUserName, null)
                            .set(CrmLead::getIsPublic, 1)
            );
        }

        log.info("离职回收完成: 转移记录ID={}, 客户数={}, 线索数={}", record.getId(), customerIds.size(), leadIds.size());

        return convertToVO(record);
    }

    @Transactional(rollbackFor = Exception.class)
    public TransferRecordVO createTransfer(TransferCreateDTO createDTO, Long operatorId, String operatorName) {
        List<Long> customerIds = createDTO.getCustomerIds() != null ? createDTO.getCustomerIds() : new ArrayList<>();
        List<Long> leadIds = createDTO.getLeadIds() != null ? createDTO.getLeadIds() : new ArrayList<>();

        if (customerIds.isEmpty() && leadIds.isEmpty()) {
            throw new BusinessException("请选择要转移的资源");
        }

        SysUser fromUser = null;
        if (createDTO.getFromUserId() != null) {
            fromUser = userMapper.selectById(createDTO.getFromUserId());
        }

        SysUser toUser = null;
        if (createDTO.getToUserId() != null) {
            toUser = userMapper.selectById(createDTO.getToUserId());
        }

        CrmTransferRecord record = new CrmTransferRecord();
        record.setTransferNo(generateTransferNo());
        record.setTransferType(createDTO.getTransferType());
        record.setTransferTypeName(createDTO.getTransferType() != null && createDTO.getTransferType() >= 1 && createDTO.getTransferType() <= 4
                ? CrmTransferRecord.TRANSFER_TYPE_NAMES[createDTO.getTransferType()] : null);
        record.setFromUserId(createDTO.getFromUserId());
        record.setFromUserName(fromUser != null ? (fromUser.getRealName() != null ? fromUser.getRealName() : fromUser.getUsername()) : createDTO.getFromUserName());
        if (createDTO.getToUserId() != null) {
            record.setToUserId(createDTO.getToUserId());
            record.setToUserName(toUser != null ? (toUser.getRealName() != null ? toUser.getRealName() : toUser.getUsername()) : createDTO.getToUserName());
        }
        record.setTransferMethod(createDTO.getTransferMethod());
        record.setCustomerCount(customerIds.size());
        record.setLeadCount(leadIds.size());
        record.setContractCount(0);
        record.setFollowCount(0);
        record.setStatus(CrmTransferRecord.STATUS_COMPLETED);
        record.setReason(createDTO.getReason());
        record.setRemark(createDTO.getRemark());
        record.setOperatorId(operatorId);
        record.setOperatorName(operatorName);

        transferRecordMapper.insert(record);

        List<CrmTransferDetail> details = new ArrayList<>();

        for (Long customerId : customerIds) {
            CrmTransferDetail detail = new CrmTransferDetail();
            detail.setRecordId(record.getId());
            detail.setResourceType(CrmTransferDetail.RESOURCE_TYPE_CUSTOMER);
            detail.setResourceTypeName(CrmTransferDetail.RESOURCE_TYPE_NAMES[CrmTransferDetail.RESOURCE_TYPE_CUSTOMER]);
            detail.setResourceId(customerId);
            detail.setFromUserId(createDTO.getFromUserId());
            detail.setFromUserName(fromUser != null ? (fromUser.getRealName() != null ? fromUser.getRealName() : fromUser.getUsername()) : createDTO.getFromUserName());
            if (createDTO.getToUserId() != null) {
                detail.setToUserId(createDTO.getToUserId());
                detail.setToUserName(toUser != null ? (toUser.getRealName() != null ? toUser.getRealName() : toUser.getUsername()) : createDTO.getToUserName());
            }
            details.add(detail);
        }

        for (Long leadId : leadIds) {
            CrmTransferDetail detail = new CrmTransferDetail();
            detail.setRecordId(record.getId());
            detail.setResourceType(CrmTransferDetail.RESOURCE_TYPE_LEAD);
            detail.setResourceTypeName(CrmTransferDetail.RESOURCE_TYPE_NAMES[CrmTransferDetail.RESOURCE_TYPE_LEAD]);
            detail.setResourceId(leadId);
            detail.setFromUserId(createDTO.getFromUserId());
            detail.setFromUserName(fromUser != null ? (fromUser.getRealName() != null ? fromUser.getRealName() : fromUser.getUsername()) : createDTO.getFromUserName());
            if (createDTO.getToUserId() != null) {
                detail.setToUserId(createDTO.getToUserId());
                detail.setToUserName(toUser != null ? (toUser.getRealName() != null ? toUser.getRealName() : toUser.getUsername()) : createDTO.getToUserName());
            }
            details.add(detail);
        }

        if (!details.isEmpty()) {
            for (CrmTransferDetail detail : details) {
                transferDetailMapper.insert(detail);
            }
        }

        boolean isToPublic = createDTO.getTransferMethod() != null && createDTO.getTransferMethod() == CrmTransferRecord.TRANSFER_METHOD_TO_PUBLIC;

        if (!customerIds.isEmpty() && createDTO.getToUserId() != null && !isToPublic) {
            customerMapper.update(null,
                    new LambdaUpdateWrapper<CrmCustomer>()
                            .in(CrmCustomer::getId, customerIds)
                            .set(CrmCustomer::getOwnerUserId, createDTO.getToUserId())
                            .set(CrmCustomer::getOwnerUserName, toUser != null ? (toUser.getRealName() != null ? toUser.getRealName() : toUser.getUsername()) : null)
            );
        } else if (!customerIds.isEmpty()) {
            customerMapper.update(null,
                    new LambdaUpdateWrapper<CrmCustomer>()
                            .in(CrmCustomer::getId, customerIds)
                            .set(CrmCustomer::getOwnerUserId, null)
                            .set(CrmCustomer::getOwnerUserName, null)
            );
        }

        if (!leadIds.isEmpty() && createDTO.getToUserId() != null && !isToPublic) {
            leadMapper.update(null,
                    new LambdaUpdateWrapper<CrmLead>()
                            .in(CrmLead::getId, leadIds)
                            .set(CrmLead::getAssignUserId, createDTO.getToUserId())
                            .set(CrmLead::getAssignUserName, toUser != null ? (toUser.getRealName() != null ? toUser.getRealName() : toUser.getUsername()) : null)
                            .set(CrmLead::getIsPublic, 0)
            );
        } else if (!leadIds.isEmpty()) {
            leadMapper.update(null,
                    new LambdaUpdateWrapper<CrmLead>()
                            .in(CrmLead::getId, leadIds)
                            .set(CrmLead::getAssignUserId, null)
                            .set(CrmLead::getAssignUserName, null)
                            .set(CrmLead::getIsPublic, 1)
            );
        }

        log.info("手动转移完成: 转移记录ID={}, 客户数={}, 线索数={}", record.getId(), customerIds.size(), leadIds.size());

        return convertToVO(record);
    }

    public PageResult<TransferRecordVO> pageRecords(TransferRecordQueryDTO queryDTO) {
        Page<CrmTransferRecord> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        LambdaQueryWrapper<CrmTransferRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(queryDTO.getTransferType() != null, CrmTransferRecord::getTransferType, queryDTO.getTransferType())
                .eq(queryDTO.getFromUserId() != null, CrmTransferRecord::getFromUserId, queryDTO.getFromUserId())
                .eq(queryDTO.getToUserId() != null, CrmTransferRecord::getToUserId, queryDTO.getToUserId())
                .eq(queryDTO.getStatus() != null, CrmTransferRecord::getStatus, queryDTO.getStatus())
                .like(queryDTO.getTransferNo() != null, CrmTransferRecord::getTransferNo, queryDTO.getTransferNo())
                .orderByDesc(CrmTransferRecord::getCreateTime)
                .orderByDesc(CrmTransferRecord::getId);

        Page<CrmTransferRecord> result = transferRecordMapper.selectPage(page, wrapper);

        List<TransferRecordVO> voList = result.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return PageResult.of(
                result.getTotal(),
                voList,
                (long) result.getCurrent(),
                (long) result.getSize()
        );
    }

    public TransferRecordVO getRecordById(Long id) {
        CrmTransferRecord record = transferRecordMapper.selectById(id);
        if (record == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        TransferRecordVO vo = convertToVO(record);
        vo.setDetails(getDetailsByRecordId(id));
        return vo;
    }

    public List<TransferDetailVO> getDetailsByRecordId(Long recordId) {
        List<CrmTransferDetail> details = transferDetailMapper.selectList(
                new LambdaQueryWrapper<CrmTransferDetail>()
                        .eq(CrmTransferDetail::getRecordId, recordId)
                        .orderByAsc(CrmTransferDetail::getResourceType)
                        .orderByAsc(CrmTransferDetail::getId)
        );
        return details.stream()
                .map(this::convertDetailToVO)
                .collect(Collectors.toList());
    }

    private String generateTransferNo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        String random = IdUtil.getSnowflakeNextIdStr();
        if (random.length() > 6) {
            random = random.substring(random.length() - 6);
        }
        return "TR" + timestamp + random;
    }

    private TransferRecordVO convertToVO(CrmTransferRecord record) {
        return TransferRecordVO.builder()
                .id(record.getId())
                .transferNo(record.getTransferNo())
                .transferType(record.getTransferType())
                .transferTypeName(record.getTransferTypeName())
                .fromUserId(record.getFromUserId())
                .fromUserName(record.getFromUserName())
                .fromDepartmentId(record.getFromDepartmentId())
                .fromDepartmentName(record.getFromDepartmentName())
                .toUserId(record.getToUserId())
                .toUserName(record.getToUserName())
                .toDepartmentId(record.getToDepartmentId())
                .toDepartmentName(record.getToDepartmentName())
                .transferMethod(record.getTransferMethod())
                .transferMethodName(record.getTransferMethod() != null && record.getTransferMethod() >= 1 && record.getTransferMethod() <= 3
                        ? CrmTransferRecord.TRANSFER_METHOD_NAMES[record.getTransferMethod()] : null)
                .customerCount(record.getCustomerCount())
                .leadCount(record.getLeadCount())
                .contractCount(record.getContractCount())
                .followCount(record.getFollowCount())
                .status(record.getStatus())
                .statusName(record.getStatus() != null && record.getStatus() >= 0 && record.getStatus() <= 2
                        ? CrmTransferRecord.STATUS_NAMES[record.getStatus()] : null)
                .reason(record.getReason())
                .remark(record.getRemark())
                .operatorId(record.getOperatorId())
                .operatorName(record.getOperatorName())
                .createTime(record.getCreateTime())
                .updateTime(record.getUpdateTime())
                .build();
    }

    private TransferDetailVO convertDetailToVO(CrmTransferDetail detail) {
        return TransferDetailVO.builder()
                .id(detail.getId())
                .recordId(detail.getRecordId())
                .resourceType(detail.getResourceType())
                .resourceTypeName(detail.getResourceTypeName())
                .resourceId(detail.getResourceId())
                .fromUserId(detail.getFromUserId())
                .fromUserName(detail.getFromUserName())
                .toUserId(detail.getToUserId())
                .toUserName(detail.getToUserName())
                .build();
    }
}
