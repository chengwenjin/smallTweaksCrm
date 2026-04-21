package com.baserbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baserbac.entity.CrmLeadLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 线索操作日志 Mapper
 */
@Mapper
public interface LeadLogMapper extends BaseMapper<CrmLeadLog> {

}
