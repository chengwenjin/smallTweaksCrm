-- ============================================
-- 销售漏斗模块额外阶段测试数据
-- 增加自定义阶段和禁用阶段数据
-- 目标：至少20条阶段数据
-- ============================================

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET collation_connection = utf8mb4_unicode_ci;

-- ============================================
-- 插入额外的自定义销售阶段（15条）
-- 包含启用和禁用的阶段
-- ============================================

-- 新增阶段：商机挖掘（启用）
INSERT INTO `crm_sales_stage` (`stage_code`, `stage_name`, `sort_order`, `win_probability`, `description`, `is_system`, `is_enabled`, `is_closed`, `close_type`) VALUES
('lead_discovery', '商机挖掘', 0, 5.00, '从线索或市场活动中发现潜在商机，尚未进行初步沟通', 0, 1, 0, 0);

-- 新增阶段：需求深入分析（启用）
INSERT INTO `crm_sales_stage` (`stage_code`, `stage_name`, `sort_order`, `win_probability`, `description`, `is_system`, `is_enabled`, `is_closed`, `close_type`) VALUES
('needs_deep_analysis', '需求深入分析', 2, 25.00, '深入分析客户业务痛点，挖掘潜在需求，制定解决方案框架', 0, 1, 0, 0);

-- 新增阶段：方案演示（启用）
INSERT INTO `crm_sales_stage` (`stage_code`, `stage_name`, `sort_order`, `win_probability`, `description`, `is_system`, `is_enabled`, `is_closed`, `close_type`) VALUES
('solution_demo', '方案演示', 3, 35.00, '向客户进行产品或方案演示，解答疑问，展示价值', 0, 1, 0, 0);

-- 新增阶段：技术评估（启用）
INSERT INTO `crm_sales_stage` (`stage_code`, `stage_name`, `sort_order`, `win_probability`, `description`, `is_system`, `is_enabled`, `is_closed`, `close_type`) VALUES
('technical_assessment', '技术评估', 4, 50.00, '客户进行技术可行性评估、兼容性测试、性能测试等', 0, 1, 0, 0);

-- 新增阶段：供应商筛选（启用）
INSERT INTO `crm_sales_stage` (`stage_code`, `stage_name`, `sort_order`, `win_probability`, `description`, `is_system`, `is_enabled`, `is_closed`, `close_type`) VALUES
('vendor_selection', '供应商筛选', 5, 55.00, '客户在多个供应商中进行比较和筛选', 0, 1, 0, 0);

-- 新增阶段：POC验证（启用）
INSERT INTO `crm_sales_stage` (`stage_code`, `stage_name`, `sort_order`, `win_probability`, `description`, `is_system`, `is_enabled`, `is_closed`, `close_type`) VALUES
('poc_verification', 'POC验证', 5, 60.00, '进行概念验证或试点项目，验证方案可行性', 0, 1, 0, 0);

-- 新增阶段：价格谈判（启用）
INSERT INTO `crm_sales_stage` (`stage_code`, `stage_name`, `sort_order`, `win_probability`, `description`, `is_system`, `is_enabled`, `is_closed`, `close_type`) VALUES
('price_negotiation', '价格谈判', 6, 65.00, '进行价格、折扣、付款方式等商务条款谈判', 0, 1, 0, 0);

-- 新增阶段：合同审核（启用）
INSERT INTO `crm_sales_stage` (`stage_code`, `stage_name`, `sort_order`, `win_probability`, `description`, `is_system`, `is_enabled`, `is_closed`, `close_type`) VALUES
('contract_review', '合同审核', 6, 75.00, '法务部门或管理层审核合同条款', 0, 1, 0, 0);

-- 新增阶段：合同签署（启用）
INSERT INTO `crm_sales_stage` (`stage_code`, `stage_name`, `sort_order`, `win_probability`, `description`, `is_system`, `is_enabled`, `is_closed`, `close_type`) VALUES
('contract_signing', '合同签署', 7, 85.00, '双方签署合同，等待盖章生效', 0, 1, 0, 0);

-- 新增阶段：项目启动（启用）
INSERT INTO `crm_sales_stage` (`stage_code`, `stage_name`, `sort_order`, `win_probability`, `description`, `is_system`, `is_enabled`, `is_closed`, `close_type`) VALUES
('project_kickoff', '项目启动', 7, 90.00, '合同生效后，项目启动准备阶段', 0, 1, 0, 0);

-- ============================================
-- 插入禁用的阶段数据（5条，用于测试筛选功能）
-- ============================================

-- 禁用阶段：旧版-初步接触
INSERT INTO `crm_sales_stage` (`stage_code`, `stage_name`, `sort_order`, `win_probability`, `description`, `is_system`, `is_enabled`, `is_closed`, `close_type`) VALUES
('old_initial_contact', '旧版-初步接触', 1, 10.00, '已废弃的初步接触阶段，请勿使用', 0, 0, 0, 0);

-- 禁用阶段：旧版-需求评估
INSERT INTO `crm_sales_stage` (`stage_code`, `stage_name`, `sort_order`, `win_probability`, `description`, `is_system`, `is_enabled`, `is_closed`, `close_type`) VALUES
('old_needs_assessment', '旧版-需求评估', 2, 15.00, '已废弃的需求评估阶段，合并到需求确认', 0, 0, 0, 0);

-- 禁用阶段：旧版-方案制定
INSERT INTO `crm_sales_stage` (`stage_code`, `stage_name`, `sort_order`, `win_probability`, `description`, `is_system`, `is_enabled`, `is_closed`, `close_type`) VALUES
('old_solution_development', '旧版-方案制定', 3, 30.00, '已废弃的方案制定阶段，合并到方案报价', 0, 0, 0, 0);

-- 禁用阶段：旧版-高层达成
INSERT INTO `crm_sales_stage` (`stage_code`, `stage_name`, `sort_order`, `win_probability`, `description`, `is_system`, `is_enabled`, `is_closed`, `close_type`) VALUES
('old_executive_sponsor', '旧版-高层达成', 4, 60.00, '已废弃的高层达成阶段，合并到商务谈判', 0, 0, 0, 0);

-- 禁用阶段：已暂停
INSERT INTO `crm_sales_stage` (`stage_code`, `stage_name`, `sort_order`, `win_probability`, `description`, `is_system`, `is_enabled`, `is_closed`, `close_type`) VALUES
('stage_suspended', '已暂停', 9, 0.00, '商机暂时搁置，等待客户后续决策', 0, 0, 1, 0);

-- ============================================
-- 执行完成提示
-- ============================================
SELECT '额外阶段数据初始化完成' AS message, 
       (SELECT COUNT(*) FROM `crm_sales_stage` WHERE is_deleted = 0) AS total_stage_count,
       (SELECT COUNT(*) FROM `crm_sales_stage` WHERE is_enabled = 1 AND is_deleted = 0) AS enabled_stage_count,
       (SELECT COUNT(*) FROM `crm_sales_stage` WHERE is_enabled = 0 AND is_deleted = 0) AS disabled_stage_count;
