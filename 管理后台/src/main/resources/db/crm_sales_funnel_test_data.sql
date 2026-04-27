-- ============================================
-- 销售漏斗模块测试数据
-- 确保每个功能模块有30条以上测试数据
-- 数据库字符集：utf8mb4
-- ============================================

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET collation_connection = utf8mb4_unicode_ci;

-- ============================================
-- 一、商机测试数据 - 插入50条测试数据
-- 覆盖各个阶段、各种行业、不同金额
-- ============================================

-- 第1-10条：初步接洽阶段（赢率10%）
INSERT INTO `crm_business_opportunity` (
    `opportunity_no`, `opportunity_name`, `customer_id`, `customer_name`, `stage_id`, `stage_code`, `stage_name`, 
    `win_probability`, `expected_amount`, `forecasted_amount`, `expected_deal_date`, 
    `assign_user_id`, `assign_user_name`, `assign_time`, `status`, `source_name`, `industry`, 
    `description`, `tags`, `create_by`, `create_time`
) VALUES
('OP202604270001', '北京云端科技ERP系统项目', 1, '北京云端科技有限公司', 1, 'initial_contact', '初步接洽', 10.00, 500000.00, 50000.00, '2026-07-15', 
 1, '管理员', NOW(), 0, '线索转化', 'IT互联网', 
 '客户对企业管理系统有浓厚兴趣，希望尽快安排演示。客户规模约200人，目前使用传统Excel管理，迫切需要数字化转型。', '重要客户,高价值', 'admin', NOW()),
('OP202604270002', '上海智能金融风控系统项目', 2, '上海智能金融有限公司', 1, 'initial_contact', '初步接洽', 10.00, 800000.00, 80000.00, '2026-08-01', 
 1, '管理员', NOW(), 0, '官网咨询', '金融', 
 '金融行业客户，需要合规性强的管理系统。已初步沟通，对数据安全要求高。', '金融行业,高预算', 'admin', NOW()),
('OP202604270003', '广州智能制造MES系统项目', 3, '广州智能制造有限公司', 1, 'initial_contact', '初步接洽', 10.00, 600000.00, 60000.00, '2026-07-30', 
 1, '管理员', NOW(), 0, '展会获取', '制造业', 
 '大型制造企业，正在进行数字化转型。生产管理模块是核心需求，有专门的信息化部门负责采购。', '制造企业,数字化转型', 'admin', NOW()),
('OP202604270004', '深圳智慧医疗HIS系统项目', 4, '深圳智慧医疗有限公司', 1, 'initial_contact', '初步接洽', 10.00, 900000.00, 90000.00, '2026-09-15', 
 1, '管理员', NOW(), 0, '市场活动', '医疗健康', 
 '通过2026春季医疗信息化展会获取，客户对医疗数据管理系统有强烈需求。', '医疗行业,信息化', 'admin', NOW()),
('OP202604270005', '杭州电商科技订单管理系统', 5, '杭州电商科技有限公司', 1, 'initial_contact', '初步接洽', 10.00, 450000.00, 45000.00, '2026-06-30', 
 1, '管理员', NOW(), 0, '客户转介绍', '电商', 
 '电商企业，需要订单管理和库存管理功能。有3家网店，需要统一管理。', '电商企业,多店管理', 'admin', NOW()),
('OP202604270006', '南京教育培训学员管理系统', 6, '南京教育培训集团', 1, 'initial_contact', '初步接洽', 10.00, 350000.00, 35000.00, '2026-07-20', 
 1, '管理员', NOW(), 0, '电话咨询', '教育培训', 
 '连锁教育机构，有12个校区，需要学员管理和财务管理。', '连锁教育,多校区', 'admin', NOW()),
('OP202604270007', '成都餐饮连锁会员管理系统', 7, '成都餐饮连锁有限公司', 1, 'initial_contact', '初步接洽', 10.00, 400000.00, 40000.00, '2026-06-25', 
 1, '管理员', NOW(), 0, '行业推荐', '餐饮', 
 '餐饮连锁企业，有8家门店，需要会员管理和订单管理。', '餐饮连锁,会员管理', 'admin', NOW()),
('OP202604270008', '武汉物流TMS运输管理系统', 8, '武汉物流有限公司', 1, 'initial_contact', '初步接洽', 10.00, 550000.00, 55000.00, '2026-08-10', 
 1, '管理员', NOW(), 0, '官网表单', '物流', 
 '物流企业，需要运输管理和仓储管理。规模较大，有50辆运输车。', '物流企业,运输管理', 'admin', NOW()),
('OP202604270009', '天津建筑工程项目管理系统', 9, '天津建筑工程有限公司', 1, 'initial_contact', '初步接洽', 10.00, 700000.00, 70000.00, '2026-09-01', 
 1, '管理员', NOW(), 0, '合作伙伴', '建筑工程', 
 '建筑工程公司，需要项目管理和成本管理。目前有3个在建项目。', '建筑工程,项目管理', 'admin', NOW()),
('OP202604270010', '重庆广告传媒客户管理系统', 10, '重庆广告传媒有限公司', 1, 'initial_contact', '初步接洽', 10.00, 300000.00, 30000.00, '2026-07-10', 
 1, '管理员', NOW(), 0, '网络推广', '广告传媒', 
 '广告传媒公司，需要客户管理和项目管理。服务多家大型企业客户。', '广告传媒,客户管理', 'admin', NOW());

-- 第11-20条：需求确认阶段（赢率20%）
INSERT INTO `crm_business_opportunity` (
    `opportunity_no`, `opportunity_name`, `customer_id`, `customer_name`, `stage_id`, `stage_code`, `stage_name`, 
    `win_probability`, `expected_amount`, `forecasted_amount`, `expected_deal_date`, 
    `assign_user_id`, `assign_user_name`, `assign_time`, `status`, `source_name`, `industry`, 
    `description`, `tags`, `create_by`, `create_time`, `next_follow_time`
) VALUES
('OP202604270011', '西安物业服务ERP系统', 11, '西安物业服务有限公司', 2, 'requirement_confirmed', '需求确认', 20.00, 250000.00, 50000.00, '2026-06-30', 
 1, '管理员', NOW(), 0, '线索转化', '物业服务', 
 '客户通过官网表单提交需求，主要想了解物业管理模块。已进行一次电话沟通，确认了基本需求。', '物业行业,需求确认', 'admin', NOW(), DATE_ADD(NOW(), INTERVAL 2 DAY)),
('OP202604270012', '郑州商务服务OA系统', 12, '郑州商务服务有限公司', 2, 'requirement_confirmed', '需求确认', 20.00, 200000.00, 40000.00, '2026-07-15', 
 1, '管理员', NOW(), 0, '官网咨询', '商务服务', 
 '客户通过官网表单咨询，需要企业服务管理系统。已发送产品资料，待确认详细需求。', '商务服务,OA系统', 'admin', NOW(), DATE_ADD(NOW(), INTERVAL 3 DAY)),
('OP202604270013', '济南化工安全管理系统', 13, '济南化工有限公司', 2, 'requirement_confirmed', '需求确认', 20.00, 350000.00, 70000.00, '2026-08-01', 
 1, '管理员', NOW(), 0, '展会获取', '化工', 
 '化工企业，对安全管理和合规管理有特殊需求。已进行两次沟通，需求基本确认。', '化工行业,安全合规', 'admin', NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY)),
('OP202604270014', '沈阳医药GSP管理系统', 14, '沈阳医药健康有限公司', 2, 'requirement_confirmed', '需求确认', 20.00, 400000.00, 80000.00, '2026-08-15', 
 1, '管理员', NOW(), 0, '市场活动', '医药健康', 
 '医药公司，需要GSP合规管理和库存管理。对系统要求较高，已确认核心需求。', '医药行业,GSP合规', 'admin', NOW(), DATE_ADD(NOW(), INTERVAL 4 DAY)),
('OP202604270015', '青岛金融保险CRM系统', 15, '青岛金融保险有限公司', 2, 'requirement_confirmed', '需求确认', 20.00, 450000.00, 90000.00, '2026-07-20', 
 1, '管理员', NOW(), 0, '客户转介绍', '金融保险', 
 '保险公司，需要客户管理和保单管理。有大量客户数据需要迁移，已确认数据迁移方案。', '保险行业,CRM', 'admin', NOW(), DATE_ADD(NOW(), INTERVAL 2 DAY)),
('OP202604270016', '哈尔滨农业供应链系统', 16, '哈尔滨农业发展有限公司', 2, 'requirement_confirmed', '需求确认', 20.00, 180000.00, 36000.00, '2026-06-25', 
 1, '管理员', NOW(), 0, '电话咨询', '农业', 
 '现代农业企业，需要供应链管理和财务管理。规模中等，预算有限，已确认简化方案。', '农业企业,供应链', 'admin', NOW(), DATE_ADD(NOW(), INTERVAL 5 DAY)),
('OP202604270017', '长春设计项目管理系统', 17, '长春设计服务有限公司', 2, 'requirement_confirmed', '需求确认', 20.00, 220000.00, 44000.00, '2026-07-10', 
 1, '管理员', NOW(), 0, '行业推荐', '设计服务', 
 '设计公司，需要项目管理和客户管理。有20名设计师，已确认协作需求。', '设计服务,项目管理', 'admin', NOW(), DATE_ADD(NOW(), INTERVAL 3 DAY)),
('OP202604270018', '大连汽车销售DMS系统', 18, '大连汽车销售有限公司', 2, 'requirement_confirmed', '需求确认', 20.00, 280000.00, 56000.00, '2026-08-05', 
 1, '管理员', NOW(), 0, '官网表单', '汽车销售', 
 '汽车销售4S店，需要客户管理和销售管理。有多家门店，已确认多店管理需求。', '汽车销售,DMS', 'admin', NOW(), DATE_ADD(NOW(), INTERVAL 2 DAY)),
('OP202604270019', '石家庄生活服务平台', 19, '石家庄生活服务有限公司', 2, 'requirement_confirmed', '需求确认', 20.00, 150000.00, 30000.00, '2026-06-20', 
 1, '管理员', NOW(), 0, '网络推广', '生活服务', 
 '生活服务平台，需要订单管理和用户管理。平台型业务，已确认核心功能需求。', '生活服务,平台型', 'admin', NOW(), DATE_ADD(NOW(), INTERVAL 4 DAY)),
('OP202604270020', '合肥信息项目管理系统', 20, '合肥信息技术有限公司', 2, 'requirement_confirmed', '需求确认', 20.00, 320000.00, 64000.00, '2026-07-05', 
 1, '管理员', NOW(), 0, '线索转化', 'IT互联网', 
 '软件公司，需要项目管理和团队协作。有50名开发人员，已确认协作工具需求。', '软件公司,项目管理', 'admin', NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY));

-- 第21-30条：方案报价阶段（赢率40%）
INSERT INTO `crm_business_opportunity` (
    `opportunity_no`, `opportunity_name`, `customer_id`, `customer_name`, `stage_id`, `stage_code`, `stage_name`, 
    `win_probability`, `expected_amount`, `forecasted_amount`, `expected_deal_date`, 
    `assign_user_id`, `assign_user_name`, `assign_time`, `status`, `source_name`, `industry`, 
    `description`, `tags`, `create_by`, `create_time`, `next_follow_time`
) VALUES
('OP202604270021', '无锡科技ERP定制项目', 21, '无锡科技有限公司', 3, 'solution_quotation', '方案报价', 40.00, 650000.00, 260000.00, '2026-06-15', 
 1, '管理员', NOW(), 0, '市场活动', 'IT互联网', 
 '通过市场活动获取，已完成需求调研，正在制定详细方案。客户对系统功能要求较高。', 'ERP定制,方案阶段', 'admin', NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY)),
('OP202604270022', '常州贸易库存管理系统', 22, '常州贸易有限公司', 3, 'solution_quotation', '方案报价', 40.00, 380000.00, 152000.00, '2026-06-20', 
 1, '管理员', NOW(), 0, '展会获取', '贸易批发', 
 '通过展会获取，已确认需求，正在制作方案和报价。需要库存管理和订单管理。', '贸易批发,库存管理', 'admin', NOW(), DATE_ADD(NOW(), INTERVAL 2 DAY)),
('OP202604270023', '温州制造MES系统', 23, '温州制造有限公司', 3, 'solution_quotation', '方案报价', 40.00, 520000.00, 208000.00, '2026-07-01', 
 1, '管理员', NOW(), 0, '客户转介绍', '制造业', 
 '制造企业，已签约意向，正在制定详细方案。需要生产管理和质量管理。', '智能制造,MES', 'admin', NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY)),
('OP202604270024', '绍兴电商OMS系统', 24, '绍兴电商有限公司', 3, 'solution_quotation', '方案报价', 40.00, 290000.00, 116000.00, '2026-06-10', 
 1, '管理员', NOW(), 0, '官网咨询', '电商', 
 '电商企业，已确认需求，正在制定方案。需要订单管理和库存管理。', '电商企业,OMS', 'admin', NOW(), DATE_ADD(NOW(), INTERVAL 2 DAY)),
('OP202604270025', '台州医疗器械ERP系统', 25, '台州医疗有限公司', 3, 'solution_quotation', '方案报价', 40.00, 720000.00, 288000.00, '2026-07-15', 
 1, '管理员', NOW(), 0, '市场活动', '医疗健康', 
 '医疗器械公司，已确认需求，正在制定方案。需要GSP合规管理。', '医疗器械,GSP', 'admin', NOW(), DATE_ADD(NOW(), INTERVAL 3 DAY)),
('OP202604270026', '福州零售POS系统', 26, '福州小商店', 3, 'solution_quotation', '方案报价', 40.00, 50000.00, 20000.00, '2026-05-30', 
 1, '管理员', NOW(), 0, '电话咨询', '零售', 
 '电话咨询基础功能，需要简单的收银和会员管理。已确认简化方案。', '零售小店,POS', 'admin', NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY)),
('OP202604270027', '南昌餐饮SaaS系统', 27, '南昌餐饮店', 3, 'solution_quotation', '方案报价', 40.00, 40000.00, 16000.00, '2026-05-25', 
 1, '管理员', NOW(), 0, '电话咨询', '餐饮', 
 '单店餐饮，需要简单的收银和会员管理。已确认SaaS方案。', '餐饮单店,SaaS', 'admin', NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY)),
('OP202604270028', '昆明零售库存系统', 28, '昆明零售小店', 3, 'solution_quotation', '方案报价', 40.00, 30000.00, 12000.00, '2026-05-20', 
 1, '管理员', NOW(), 0, '电话咨询', '零售', 
 '社区零售店，需要库存管理和会员管理。已确认简化方案。', '社区零售,库存管理', 'admin', NOW(), DATE_ADD(NOW(), INTERVAL 2 DAY)),
('OP202604270029', '贵阳服务CRM系统', 29, '贵阳服务部', 3, 'solution_quotation', '方案报价', 40.00, 35000.00, 14000.00, '2026-05-28', 
 1, '管理员', NOW(), 0, '电话咨询', '商务服务', 
 '小型服务公司，需要客户管理和简单的项目管理。已确认基础方案。', '小型服务,CRM', 'admin', NOW(), DATE_ADD(NOW(), INTERVAL 2 DAY)),
('OP202604270030', '兰州加工ERP系统', 30, '兰州加工厂', 3, 'solution_quotation', '方案报价', 40.00, 60000.00, 24000.00, '2026-06-05', 
 1, '管理员', NOW(), 0, '电话咨询', '制造业', 
 '小型加工厂，需要生产管理和库存管理。已确认简化方案。', '小型加工,生产管理', 'admin', NOW(), DATE_ADD(NOW(), INTERVAL 3 DAY));

-- 第31-40条：商务谈判阶段（赢率70%）
INSERT INTO `crm_business_opportunity` (
    `opportunity_no`, `opportunity_name`, `customer_id`, `customer_name`, `stage_id`, `stage_code`, `stage_name`, 
    `win_probability`, `expected_amount`, `forecasted_amount`, `expected_deal_date`, 
    `assign_user_id`, `assign_user_name`, `assign_time`, `status`, `source_name`, `industry`, 
    `description`, `tags`, `create_by`, `create_time`, `next_follow_time`
) VALUES
('OP202604270031', '北京云端科技签约项目', 1, '北京云端科技有限公司', 4, 'business_negotiation', '商务谈判', 70.00, 500000.00, 350000.00, '2026-05-15', 
 1, '管理员', NOW(), 0, '线索转化', 'IT互联网', 
 '客户对企业管理系统有浓厚兴趣，已完成方案演示，正在进行合同谈判。', '高概率,合同谈判', 'admin', NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY)),
('OP202604270032', '上海智能金融签约项目', 2, '上海智能金融有限公司', 4, 'business_negotiation', '商务谈判', 70.00, 800000.00, 560000.00, '2026-05-20', 
 1, '管理员', NOW(), 0, '官网咨询', '金融', 
 '金融行业客户，已完成方案确认，正在进行价格谈判。对数据安全要求高。', '金融行业,价格谈判', 'admin', NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY)),
('OP202604270033', '广州智能制造签约项目', 3, '广州智能制造有限公司', 4, 'business_negotiation', '商务谈判', 70.00, 600000.00, 420000.00, '2026-05-25', 
 1, '管理员', NOW(), 0, '展会获取', '制造业', 
 '大型制造企业，已完成方案演示，正在进行合同细节确认。', '智能制造,合同确认', 'admin', NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY)),
('OP202604270034', '深圳智慧医疗签约项目', 4, '深圳智慧医疗有限公司', 4, 'business_negotiation', '商务谈判', 70.00, 900000.00, 630000.00, '2026-06-01', 
 1, '管理员', NOW(), 0, '市场活动', '医疗健康', 
 '通过医疗信息化展会获取，已完成需求确认，正在进行合同谈判。', '医疗行业,合同谈判', 'admin', NOW(), DATE_ADD(NOW(), INTERVAL 2 DAY)),
('OP202604270035', '杭州电商科技签约项目', 5, '杭州电商科技有限公司', 4, 'business_negotiation', '商务谈判', 70.00, 450000.00, 315000.00, '2026-05-18', 
 1, '管理员', NOW(), 0, '客户转介绍', '电商', 
 '电商企业，已完成方案确认，正在进行服务条款谈判。', '电商企业,服务条款', 'admin', NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY)),
('OP202604270036', '南京教育培训签约项目', 6, '南京教育培训集团', 4, 'business_negotiation', '商务谈判', 70.00, 350000.00, 245000.00, '2026-05-22', 
 1, '管理员', NOW(), 0, '电话咨询', '教育培训', 
 '连锁教育机构，已完成多校区方案确认，正在进行价格谈判。', '连锁教育,价格谈判', 'admin', NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY)),
('OP202604270037', '成都餐饮连锁签约项目', 7, '成都餐饮连锁有限公司', 4, 'business_negotiation', '商务谈判', 70.00, 400000.00, 280000.00, '2026-05-16', 
 1, '管理员', NOW(), 0, '行业推荐', '餐饮', 
 '餐饮连锁企业，已完成方案演示，正在进行合同签署前确认。', '餐饮连锁,合同确认', 'admin', NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY)),
('OP202604270038', '武汉物流签约项目', 8, '武汉物流有限公司', 4, 'business_negotiation', '商务谈判', 70.00, 550000.00, 385000.00, '2026-05-28', 
 1, '管理员', NOW(), 0, '官网表单', '物流', 
 '物流企业，已完成方案确认，正在进行服务期谈判。', '物流企业,服务期', 'admin', NOW(), DATE_ADD(NOW(), INTERVAL 2 DAY)),
('OP202604270039', '天津建筑工程签约项目', 9, '天津建筑工程有限公司', 4, 'business_negotiation', '商务谈判', 70.00, 700000.00, 490000.00, '2026-06-05', 
 1, '管理员', NOW(), 0, '合作伙伴', '建筑工程', 
 '建筑工程公司，已完成方案确认，正在进行付款方式谈判。', '建筑工程,付款方式', 'admin', NOW(), DATE_ADD(NOW(), INTERVAL 2 DAY)),
('OP202604270040', '重庆广告传媒签约项目', 10, '重庆广告传媒有限公司', 4, 'business_negotiation', '商务谈判', 70.00, 300000.00, 210000.00, '2026-05-19', 
 1, '管理员', NOW(), 0, '网络推广', '广告传媒', 
 '广告传媒公司，已完成方案确认，正在进行合同细节确认。', '广告传媒,合同确认', 'admin', NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY));

-- 第41-50条：已赢单和已输单的商机
INSERT INTO `crm_business_opportunity` (
    `opportunity_no`, `opportunity_name`, `customer_id`, `customer_name`, `stage_id`, `stage_code`, `stage_name`, 
    `win_probability`, `expected_amount`, `forecasted_amount`, `expected_deal_date`, 
    `assign_user_id`, `assign_user_name`, `assign_time`, `status`, `source_name`, `industry`, 
    `description`, `tags`, `create_by`, `create_time`
) VALUES
('OP202604270041', '镇江科技已签约项目', 31, '镇江科技有限公司', 5, 'won', '赢单', 100.00, 320000.00, 320000.00, '2026-04-20', 
 1, '管理员', NOW(), 1, 'Excel导入', 'IT互联网', 
 '通过行业展会名单批量导入，已完成合同签订。系统正在实施中。', '已赢单,实施中', 'admin', NOW()),
('OP202604270042', '泰州贸易已签约项目', 32, '泰州贸易有限公司', 5, 'won', '赢单', 100.00, 180000.00, 180000.00, '2026-04-15', 
 1, '管理员', NOW(), 1, 'Excel导入', '贸易批发', 
 '批量导入名单，已签约。需要库存管理和订单管理。', '已赢单,库存管理', 'admin', NOW()),
('OP202604270043', '宿迁制造已签约项目', 33, '宿迁制造有限公司', 5, 'won', '赢单', 100.00, 280000.00, 280000.00, '2026-04-18', 
 1, '管理员', NOW(), 1, 'Excel导入', '制造业', 
 '批量导入名单，制造企业已签约。需要生产管理。', '已赢单,生产管理', 'admin', NOW()),
('OP202604270044', '湖州电商已签约项目', 34, '湖州电商有限公司', 5, 'won', '赢单', 100.00, 150000.00, 150000.00, '2026-04-10', 
 1, '管理员', NOW(), 1, '在线咨询', '电商', 
 '通过官网在线客服咨询，已签约。主要使用订单管理功能。', '已赢单,订单管理', 'admin', NOW()),
('OP202604270045', '嘉兴医疗已签约项目', 35, '嘉兴医疗有限公司', 5, 'won', '赢单', 100.00, 580000.00, 580000.00, '2026-04-22', 
 1, '管理员', NOW(), 1, '在线咨询', '医疗健康', 
 '在线咨询医疗管理系统，已签约。需求明确，正在实施。', '已赢单,实施中', 'admin', NOW()),
('OP202604270046', '金华金融输单项目', 36, '金华金融有限公司', 6, 'lost', '输单', 0.00, 350000.00, 0.00, NULL, 
 1, '管理员', NOW(), 2, '在线咨询', '金融', 
 '在线咨询金融行业解决方案。跟进后发现客户已在上个月签约竞争对手。', '已输单,竞争对手', 'admin', NOW()),
('OP202604270047', '衢州零售输单项目', 37, '衢州零售有限公司', 6, 'lost', '输单', 0.00, 90000.00, 0.00, NULL, 
 1, '管理员', NOW(), 2, '其他来源', '零售', 
 '通过行业协会获取的名单。客户预算不足，暂时无法签约。', '已输单,预算不足', 'admin', NOW()),
('OP202604270048', '丽水服务输单项目', 38, '丽水服务有限公司', 6, 'lost', '输单', 0.00, 220000.00, 0.00, NULL, 
 1, '管理员', NOW(), 2, '其他来源', '商务服务', 
 '通过合作伙伴推荐获取。客户已选择其他供应商。', '已输单,其他供应商', 'admin', NOW()),
('OP202604270049', '舟山电商输单项目', 39, '舟山电商有限公司', 6, 'lost', '输单', 0.00, 120000.00, 0.00, NULL, 
 1, '管理员', NOW(), 2, '其他来源', '电商', 
 '通过行业论坛获取。客户自有IT团队开发。', '已输单,自主开发', 'admin', NOW()),
('OP202604270050', '东莞科技输单项目', 40, '东莞科技有限公司', 6, 'lost', '输单', 0.00, 420000.00, 0.00, NULL, 
 1, '管理员', NOW(), 2, 'Excel导入', 'IT互联网', 
 '批量导入的科技企业名单。客户信息化项目暂缓。', '已输单,项目暂缓', 'admin', NOW());

-- ============================================
-- 执行完成提示
-- ============================================
SELECT '销售漏斗测试数据初始化完成' AS message, 
       (SELECT COUNT(*) FROM `crm_sales_stage`) AS stage_count,
       (SELECT COUNT(*) FROM `crm_business_opportunity`) AS opportunity_count,
       (SELECT COUNT(*) FROM `crm_business_opportunity` WHERE `stage_code` = 'initial_contact') AS initial_contact_count,
       (SELECT COUNT(*) FROM `crm_business_opportunity` WHERE `stage_code` = 'requirement_confirmed') AS requirement_confirmed_count,
       (SELECT COUNT(*) FROM `crm_business_opportunity` WHERE `stage_code` = 'solution_quotation') AS solution_quotation_count,
       (SELECT COUNT(*) FROM `crm_business_opportunity` WHERE `stage_code` = 'business_negotiation') AS business_negotiation_count,
       (SELECT COUNT(*) FROM `crm_business_opportunity` WHERE `stage_code` = 'won') AS won_count,
       (SELECT COUNT(*) FROM `crm_business_opportunity` WHERE `stage_code` = 'lost') AS lost_count;
