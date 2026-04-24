-- =============================================
-- 线索全生命周期完整测试数据 V3
-- 确保每个功能模块有30条以上测试数据
-- 包含：线索(50条)、分配规则(40条)、分配记录(40条)、公海线索(35条)
-- 数据库字符集：utf8mb4
-- =============================================

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET collation_connection = utf8mb4_unicode_ci;

-- =============================================
-- 一、线索主表 - 插入50条测试数据
-- =============================================

-- 先清空旧数据（保留表结构）
-- 注意：实际执行时请根据需要决定是否清空
-- TRUNCATE TABLE `crm_lead_log`;
-- TRUNCATE TABLE `crm_assign_record`;
-- TRUNCATE TABLE `crm_assign_rule`;
-- TRUNCATE TABLE `crm_lead`;

-- 插入线索测试数据（50条，覆盖各种来源、状态、等级）
-- 第1-10条：高优先级新线索
INSERT INTO `crm_lead` (`lead_no`, `lead_name`, `contact_name`, `contact_mobile`, `contact_email`, `province`, `city`, `district`, `industry`, `source_id`, `source_code`, `source_name`, `level`, `status`, `expected_amount`, `description`, `create_by`, `create_time`) VALUES
('LD202604240001', '北京云端科技有限公司', '张云端', '13800138001', 'zhangyunduan@bjyunduan.com', '北京市', '北京市', '海淀区', 'IT互联网', 1, 'manual', '手工录入', 1, 0, 500000.00, '客户对企业管理系统有浓厚兴趣，希望尽快安排演示。通过销售经理推荐联系，客户规模约200人，目前使用传统Excel管理，迫切需要数字化转型。', 'admin', NOW()),
('LD202604240002', '上海智能金融有限公司', '李金融', '13900139002', 'lijinrong@shzhineng.com', '上海市', '上海市', '浦东新区', '金融', 1, 'manual', '手工录入', 1, 0, 800000.00, '金融行业客户，需要合规性强的管理系统。已初步沟通，对数据安全要求高。', 'admin', NOW()),
('LD202604240003', '广州智能制造有限公司', '王制造', '13700137003', 'wangzhizao@gzznzz.com', '广东省', '广州市', '天河区', '制造业', 1, 'manual', '手工录入', 1, 0, 600000.00, '大型制造企业，正在进行数字化转型。生产管理模块是核心需求，有专门的信息化部门负责采购。', 'admin', NOW()),
('LD202604240004', '深圳智慧医疗有限公司', '赵医疗', '13600136004', 'zhaoyiliao@szzhyl.com', '广东省', '深圳市', '南山区', '医疗健康', 1, 'manual', '手工录入', 1, 0, 900000.00, '通过2026春季医疗信息化展会获取，客户对医疗数据管理系统有强烈需求。', 'admin', NOW()),
('LD202604240005', '杭州电商科技有限公司', '孙电商', '13500135005', 'sundianshang@hzdskj.com', '浙江省', '杭州市', '西湖区', '电商', 1, 'manual', '手工录入', 1, 0, 450000.00, '电商企业，需要订单管理和库存管理功能。有3家网店，需要统一管理。', 'admin', NOW()),
('LD202604240006', '南京教育培训集团', '周教育', '13400134006', 'zhoujiaoyu@njjyjt.com', '江苏省', '南京市', '鼓楼区', '教育培训', 1, 'manual', '手工录入', 1, 0, 350000.00, '连锁教育机构，有12个校区，需要学员管理和财务管理。', 'admin', NOW()),
('LD202604240007', '成都餐饮连锁有限公司', '吴餐饮', '13300133007', 'wucanyin@cdcyyl.com', '四川省', '成都市', '锦江区', '餐饮', 1, 'manual', '手工录入', 1, 0, 400000.00, '餐饮连锁企业，有8家门店，需要会员管理和订单管理。', 'admin', NOW()),
('LD202604240008', '武汉物流有限公司', '郑物流', '13200132008', 'zhengwuliu@whwuliu.com', '湖北省', '武汉市', '江汉区', '物流', 1, 'manual', '手工录入', 1, 0, 550000.00, '物流企业，需要运输管理和仓储管理。规模较大，有50辆运输车。', 'admin', NOW()),
('LD202604240009', '天津建筑工程有限公司', '冯建筑', '13100131009', 'fengjianzhu@tjjzgc.com', '天津市', '天津市', '南开区', '建筑工程', 1, 'manual', '手工录入', 1, 0, 700000.00, '建筑工程公司，需要项目管理和成本管理。目前有3个在建项目。', 'admin', NOW()),
('LD202604240010', '重庆广告传媒有限公司', '陈广告', '13000130010', 'chengguanggao@cqggcm.com', '重庆市', '重庆市', '渝北区', '广告传媒', 1, 'manual', '手工录入', 1, 0, 300000.00, '广告传媒公司，需要客户管理和项目管理。服务多家大型企业客户。', 'admin', NOW());

-- 第11-20条：中优先级跟进中线索
INSERT INTO `crm_lead` (`lead_no`, `lead_name`, `contact_name`, `contact_mobile`, `contact_email`, `province`, `city`, `district`, `industry`, `source_id`, `source_code`, `source_name`, `level`, `status`, `expected_amount`, `description`, `create_by`, `create_time`, `assign_user_id`, `assign_user_name`, `assign_time`, `next_follow_time`) VALUES
('LD202604240011', '西安物业服务有限公司', '褚物业', '15800158011', 'chuwuye@xawuye.com', '陕西省', '西安市', '雁塔区', '物业服务', 3, 'website', '官网表单', 2, 1, 250000.00, '客户通过官网表单提交需求，主要想了解物业管理模块。已进行一次电话沟通。', 'admin', NOW(), 1, '管理员', NOW(), DATE_ADD(NOW(), INTERVAL 2 DAY)),
('LD202604240012', '郑州商务服务有限公司', '卫商务', '15900159012', 'weishangwu@zzswfw.com', '河南省', '郑州市', '金水区', '商务服务', 3, 'website', '官网表单', 2, 1, 200000.00, '客户通过官网表单咨询，需要企业服务管理系统。已发送产品资料。', 'admin', NOW(), 1, '管理员', NOW(), DATE_ADD(NOW(), INTERVAL 3 DAY)),
('LD202604240013', '济南化工有限公司', '蒋化工', '15700157013', 'jianghuagong@jnhuagong.com', '山东省', '济南市', '历下区', '化工', 3, 'website', '官网表单', 2, 1, 350000.00, '化工企业，对安全管理和合规管理有特殊需求。已进行两次沟通。', 'admin', NOW(), 1, '管理员', NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY)),
('LD202604240014', '沈阳医药健康有限公司', '沈医药', '15600156014', 'shenyiyao@syyyjk.com', '辽宁省', '沈阳市', '和平区', '医药健康', 3, 'website', '官网表单', 2, 1, 400000.00, '医药公司，需要GSP合规管理和库存管理。对系统要求较高。', 'admin', NOW(), 1, '管理员', NOW(), DATE_ADD(NOW(), INTERVAL 4 DAY)),
('LD202604240015', '青岛金融保险有限公司', '韩保险', '15500155015', 'hanbaoxian@qdjrbx.com', '山东省', '青岛市', '市南区', '金融保险', 3, 'website', '官网表单', 2, 1, 450000.00, '保险公司，需要客户管理和保单管理。有大量客户数据需要迁移。', 'admin', NOW(), 1, '管理员', NOW(), DATE_ADD(NOW(), INTERVAL 2 DAY)),
('LD202604240016', '哈尔滨农业发展有限公司', '杨农业', '15400154016', 'yangnongye@hbnonye.com', '黑龙江省', '哈尔滨市', '南岗区', '农业', 3, 'website', '官网表单', 2, 1, 180000.00, '现代农业企业，需要供应链管理和财务管理。规模中等，预算有限。', 'admin', NOW(), 1, '管理员', NOW(), DATE_ADD(NOW(), INTERVAL 5 DAY)),
('LD202604240017', '长春设计服务有限公司', '朱设计', '15300153017', 'zhusheji@ccsjfw.com', '吉林省', '长春市', '朝阳区', '设计服务', 3, 'website', '官网表单', 2, 1, 220000.00, '设计公司，需要项目管理和客户管理。有20名设计师。', 'admin', NOW(), 1, '管理员', NOW(), DATE_ADD(NOW(), INTERVAL 3 DAY)),
('LD202604240018', '大连汽车销售有限公司', '秦汽车', '15200152018', 'qinqiche@dlqcxz.com', '辽宁省', '大连市', '中山区', '汽车销售', 3, 'website', '官网表单', 2, 1, 280000.00, '汽车销售4S店，需要客户管理和销售管理。有多家门店。', 'admin', NOW(), 1, '管理员', NOW(), DATE_ADD(NOW(), INTERVAL 2 DAY)),
('LD202604240019', '石家庄生活服务有限公司', '尤生活', '15100151019', 'youshenghuo@sjzshfw.com', '河北省', '石家庄市', '长安区', '生活服务', 3, 'website', '官网表单', 2, 1, 150000.00, '生活服务平台，需要订单管理和用户管理。平台型业务。', 'admin', NOW(), 1, '管理员', NOW(), DATE_ADD(NOW(), INTERVAL 4 DAY)),
('LD202604240020', '合肥信息技术有限公司', '许信息', '15000150020', 'xuxinxi@hfxxjs.com', '安徽省', '合肥市', '蜀山区', 'IT互联网', 3, 'website', '官网表单', 2, 1, 320000.00, '软件公司，需要项目管理和团队协作。有50名开发人员。', 'admin', NOW(), 1, '管理员', NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY));

-- 第21-30条：低优先级线索
INSERT INTO `crm_lead` (`lead_no`, `lead_name`, `contact_name`, `contact_mobile`, `contact_email`, `province`, `city`, `district`, `industry`, `source_id`, `source_code`, `source_name`, `level`, `status`, `expected_amount`, `description`, `create_by`, `create_time`) VALUES
('LD202604240021', '福州小商店', '何小店', '18800188021', 'hexiaodian@fzxd.com', '福建省', '福州市', '鼓楼区', '零售', 6, 'phone', '电话咨询', 3, 0, 50000.00, '电话咨询基础功能，预算有限，可能需要简化版。', 'admin', NOW()),
('LD202604240022', '南昌餐饮店', '吕餐饮', '18700187022', 'lvcanyin@nccy.com', '江西省', '南昌市', '东湖区', '餐饮', 6, 'phone', '电话咨询', 3, 0, 40000.00, '单店餐饮，需要简单的收银和会员管理。', 'admin', NOW()),
('LD202604240023', '昆明零售小店', '施零售', '18600186023', 'shilingshou@kmls.com', '云南省', '昆明市', '五华区', '零售', 6, 'phone', '电话咨询', 3, 0, 30000.00, '社区零售店，需要库存管理和会员管理。', 'admin', NOW()),
('LD202604240024', '贵阳服务部', '张服务', '18500185024', 'zhangfuwu@gysfw.com', '贵州省', '贵阳市', '南明区', '商务服务', 6, 'phone', '电话咨询', 3, 0, 35000.00, '小型服务公司，需要客户管理和简单的项目管理。', 'admin', NOW()),
('LD202604240025', '兰州加工厂', '孔加工', '18400184025', 'kongjiagong@lzjjc.com', '甘肃省', '兰州市', '城关区', '制造业', 6, 'phone', '电话咨询', 3, 0, 60000.00, '小型加工厂，需要生产管理和库存管理。', 'admin', NOW()),
('LD202604240026', '太原电商小铺', '曹电商', '18300183026', 'caodianshang@tydx.com', '山西省', '太原市', '小店区', '电商', 6, 'phone', '电话咨询', 3, 0, 45000.00, '个人电商店铺，需要订单管理和库存管理。', 'admin', NOW()),
('LD202604240027', '南宁培训中心', '严培训', '18200182027', 'yanpeixun@nnpx.com', '广西壮族自治区', '南宁市', '青秀区', '教育培训', 6, 'phone', '电话咨询', 3, 0, 55000.00, '小型培训中心，需要学员管理和收费管理。', 'admin', NOW()),
('LD202604240028', '海口咨询服务', '华咨询', '18100181028', 'huazixun@hkzx.com', '海南省', '海口市', '龙华区', '商务服务', 6, 'phone', '电话咨询', 3, 0, 25000.00, '咨询公司，需要客户管理和项目管理。', 'admin', NOW()),
('LD202604240029', '银川个体户', '金个体', '18000180029', 'jingeti@ycgt.com', '宁夏回族自治区', '银川市', '兴庆区', '零售', 6, 'phone', '电话咨询', 3, 0, 20000.00, '个体户，需要简单的记账和库存管理。', 'admin', NOW()),
('LD202604240030', '西宁小公司', '魏小企', '17800178030', 'weixiaoqi@xnxq.com', '青海省', '西宁市', '城东区', '商务服务', 6, 'phone', '电话咨询', 3, 0, 30000.00, '小型服务公司，预算有限，需要基础功能。', 'admin', NOW());

-- 第31-40条：已转化和已无效线索
INSERT INTO `crm_lead` (`lead_no`, `lead_name`, `contact_name`, `contact_mobile`, `contact_email`, `province`, `city`, `district`, `industry`, `source_id`, `source_code`, `source_name`, `level`, `status`, `expected_amount`, `description`, `create_by`, `create_time`, `assign_user_id`, `assign_user_name`, `assign_time`) VALUES
('LD202604240031', '无锡科技有限公司', '陶科技', '17700177031', 'taokeji@wxkj.com', '江苏省', '无锡市', '滨湖区', 'IT互联网', 4, 'campaign', '市场活动', 1, 2, 650000.00, '通过市场活动获取，已完成合同签订。系统正在实施中。', 'admin', NOW(), 1, '管理员', NOW()),
('LD202604240032', '常州贸易有限公司', '姜贸易', '17600176032', 'jiangmaoyi@czmy.com', '江苏省', '常州市', '新北区', '贸易批发', 4, 'campaign', '市场活动', 2, 2, 380000.00, '通过展会获取，已签约。需要库存管理和订单管理。', 'admin', NOW(), 1, '管理员', NOW()),
('LD202604240033', '温州制造有限公司', '戚制造', '17500175033', 'qizhizao@wzzz.com', '浙江省', '温州市', '鹿城区', '制造业', 4, 'campaign', '市场活动', 1, 2, 520000.00, '制造企业，已签约。需要生产管理和质量管理。', 'admin', NOW(), 1, '管理员', NOW()),
('LD202604240034', '绍兴电商有限公司', '谢电商', '17400174034', 'xiedianshang@sxds.com', '浙江省', '绍兴市', '越城区', '电商', 4, 'campaign', '市场活动', 2, 2, 290000.00, '电商企业，已签约。需要订单管理和库存管理。', 'admin', NOW(), 1, '管理员', NOW()),
('LD202604240035', '台州医疗有限公司', '邹医疗', '17300173035', 'zhouyiliao@tzyl.com', '浙江省', '台州市', '椒江区', '医疗健康', 4, 'campaign', '市场活动', 1, 2, 720000.00, '医疗器械公司，已签约。需要GSP合规管理。', 'admin', NOW(), 1, '管理员', NOW()),
('LD202604240036', '徐州远大教育集团', '水教育', '17200172036', 'shuijiaoyu@xzydjy.com', '江苏省', '徐州市', '泉山区', '教育培训', 5, 'referral', '客户转介绍', 2, 3, 300000.00, '由老客户转介绍，但跟进后发现客户已在上个月签约竞争对手。', 'admin', NOW()),
('LD202604240037', '连云港金融有限公司', '窦金融', '17100171037', 'doujinrong@lygjr.com', '江苏省', '连云港市', '海州区', '金融', 5, 'referral', '客户转介绍', 1, 3, 550000.00, '客户转介绍，但客户预算不足，暂时无法签约。', 'admin', NOW()),
('LD202604240038', '淮安贸易有限公司', '章贸易', '17000170038', 'zhangmaoyi@hamy.com', '江苏省', '淮安市', '清江浦区', '贸易批发', 5, 'referral', '客户转介绍', 2, 3, 250000.00, '转介绍客户，但客户已选择其他供应商。', 'admin', NOW()),
('LD202604240039', '盐城制造有限公司', '云制造', '16900169039', 'yunzhizao@yczz.com', '江苏省', '盐城市', '亭湖区', '制造业', 5, 'referral', '客户转介绍', 1, 3, 450000.00, '转介绍客户，但客户信息化项目暂缓。', 'admin', NOW()),
('LD202604240040', '扬州电商有限公司', '潘电商', '16800168040', 'pandianshang@yzds.com', '江苏省', '扬州市', '邗江区', '电商', 5, 'referral', '客户转介绍', 2, 3, 200000.00, '转介绍客户，但客户自有IT团队开发。', 'admin', NOW());

-- 第41-50条：各种来源的线索（Excel导入、在线咨询等）
INSERT INTO `crm_lead` (`lead_no`, `lead_name`, `contact_name`, `contact_mobile`, `contact_email`, `province`, `city`, `district`, `industry`, `source_id`, `source_code`, `source_name`, `level`, `status`, `expected_amount`, `description`, `create_by`, `create_time`) VALUES
('LD202604240041', '镇江科技有限公司', '葛科技', '16700167041', 'gekeji@zjkj.com', '江苏省', '镇江市', '京口区', 'IT互联网', 2, 'excel', 'Excel导入', 2, 0, 320000.00, '通过行业展会名单批量导入，客户对信息化有兴趣。', 'admin', NOW()),
('LD202604240042', '泰州贸易有限公司', '范贸易', '16600166042', 'fanmaoyi@tzmy.com', '江苏省', '泰州市', '海陵区', '贸易批发', 2, 'excel', 'Excel导入', 3, 0, 180000.00, '批量导入名单，需要进一步筛选。', 'admin', NOW()),
('LD202604240043', '宿迁制造有限公司', '苗制造', '16500165043', 'miaozhizao@sqzz.com', '江苏省', '宿迁市', '宿城区', '制造业', 2, 'excel', 'Excel导入', 2, 0, 280000.00, '批量导入名单，制造企业可能需要ERP。', 'admin', NOW()),
('LD202604240044', '湖州电商有限公司', '凤电商', '16400164044', 'fengdianshang@hzds.com', '浙江省', '湖州市', '吴兴区', '电商', 7, 'chat', '在线咨询', 3, 0, 150000.00, '通过官网在线客服咨询，主要想了解订单管理。', 'admin', NOW()),
('LD202604240045', '嘉兴医疗有限公司', '花医疗', '16300163045', 'huayiliao@jxyl.com', '浙江省', '嘉兴市', '南湖区', '医疗健康', 7, 'chat', '在线咨询', 1, 0, 580000.00, '在线咨询医疗管理系统，需求明确。', 'admin', NOW()),
('LD202604240046', '金华金融有限公司', '方金融', '16200162046', 'fangjinrong@jhjinrong.com', '浙江省', '金华市', '婺城区', '金融', 7, 'chat', '在线咨询', 2, 0, 350000.00, '在线咨询金融行业解决方案。', 'admin', NOW()),
('LD202604240047', '衢州零售有限公司', '俞零售', '16100161047', 'yulingshou@qzls.com', '浙江省', '衢州市', '柯城区', '零售', 8, 'other', '其他来源', 3, 0, 90000.00, '通过行业协会获取的名单。', 'admin', NOW()),
('LD202604240048', '丽水服务有限公司', '任服务', '16000160048', 'renfuwu@lsfw.com', '浙江省', '丽水市', '莲都区', '商务服务', 8, 'other', '其他来源', 2, 0, 220000.00, '通过合作伙伴推荐获取。', 'admin', NOW()),
('LD202604240049', '舟山电商有限公司', '姜电商', '15900159049', 'jiangdianshang@zsds.com', '浙江省', '舟山市', '定海区', '电商', 8, 'other', '其他来源', 3, 0, 120000.00, '通过行业论坛获取。', 'admin', NOW()),
('LD202604240050', '东莞科技有限公司', '袁科技', '15800158050', 'yuankeji@dgkj.com', '广东省', '东莞市', '南城街道', 'IT互联网', 2, 'excel', 'Excel导入', 1, 0, 420000.00, '批量导入的科技企业名单，有信息化需求。', 'admin', NOW());

-- =============================================
-- 二、智能分配规则 - 确保40条
-- =============================================

-- 先清理旧数据（如果需要）
-- DELETE FROM `crm_assign_rule` WHERE id > 0;

-- 地域规则 - 20条
INSERT INTO `crm_assign_rule` (`rule_name`, `rule_type`, `province`, `city`, `district`, `industry`, `min_employee_count`, `max_employee_count`, `min_annual_revenue`, `max_annual_revenue`, `assign_user_id`, `assign_user_name`, `priority`, `is_enabled`, `description`, `create_time`, `update_time`)
SELECT * FROM (
    SELECT '北京-高优销售组' AS rule_name, 1 AS rule_type, '北京市' AS province, '' AS city, '' AS district, '' AS industry, NULL AS min_employee_count, NULL AS max_employee_count, NULL AS min_annual_revenue, NULL AS max_annual_revenue, 1 AS assign_user_id, '张三' AS assign_user_name, 10 AS priority, 1 AS is_enabled, '北京市所有线索优先分配' AS description, NOW() AS create_time, NOW() AS update_time
    UNION ALL SELECT '上海-华东销售部', 1, '上海市', '', '', '', NULL, NULL, NULL, NULL, 1, '张三', 20, 1, '上海市所有线索分配', NOW(), NOW()
    UNION ALL SELECT '广东省-华南大区', 1, '广东省', '', '', '', NULL, NULL, NULL, NULL, 1, '张三', 20, 1, '广东省所有线索分配', NOW(), NOW()
    UNION ALL SELECT '浙江省-杭州分公司', 1, '浙江省', '杭州市', '', '', NULL, NULL, NULL, NULL, 1, '张三', 15, 1, '杭州地区线索', NOW(), NOW()
    UNION ALL SELECT '江苏省-南京分公司', 1, '江苏省', '南京市', '', '', NULL, NULL, NULL, NULL, 1, '张三', 15, 1, '南京地区线索', NOW(), NOW()
    UNION ALL SELECT '四川省-成都分公司', 1, '四川省', '成都市', '', '', NULL, NULL, NULL, NULL, 1, '张三', 15, 1, '成都地区线索', NOW(), NOW()
    UNION ALL SELECT '湖北省-武汉分公司', 1, '湖北省', '武汉市', '', '', NULL, NULL, NULL, NULL, 1, '张三', 15, 1, '武汉地区线索', NOW(), NOW()
    UNION ALL SELECT '陕西省-西安分公司', 1, '陕西省', '西安市', '', '', NULL, NULL, NULL, NULL, 1, '张三', 15, 1, '西安地区线索', NOW(), NOW()
    UNION ALL SELECT '天津市-华北销售', 1, '天津市', '', '', '', NULL, NULL, NULL, NULL, 1, '张三', 25, 1, '天津地区线索', NOW(), NOW()
    UNION ALL SELECT '重庆市-西南销售', 1, '重庆市', '', '', '', NULL, NULL, NULL, NULL, 1, '张三', 25, 1, '重庆地区线索', NOW(), NOW()
    UNION ALL SELECT '湖南省-长沙销售', 1, '湖南省', '', '', '', NULL, NULL, NULL, NULL, 1, '张三', 30, 1, '湖南全省线索', NOW(), NOW()
    UNION ALL SELECT '福建省-厦门销售', 1, '福建省', '', '', '', NULL, NULL, NULL, NULL, 1, '张三', 30, 1, '福建全省线索', NOW(), NOW()
    UNION ALL SELECT '山东省-济南销售', 1, '山东省', '', '', '', NULL, NULL, NULL, NULL, 1, '张三', 30, 1, '山东全省线索', NOW(), NOW()
    UNION ALL SELECT '河南省-郑州销售', 1, '河南省', '', '', '', NULL, NULL, NULL, NULL, 1, '张三', 30, 1, '河南全省线索', NOW(), NOW()
    UNION ALL SELECT '河北省-石家庄销售', 1, '河北省', '', '', '', NULL, NULL, NULL, NULL, 1, '张三', 30, 1, '河北全省线索', NOW(), NOW()
    UNION ALL SELECT '辽宁省-沈阳销售', 1, '辽宁省', '', '', '', NULL, NULL, NULL, NULL, 1, '张三', 30, 1, '辽宁全省线索', NOW(), NOW()
    UNION ALL SELECT '黑龙江省-哈尔滨销售', 1, '黑龙江省', '', '', '', NULL, NULL, NULL, NULL, 1, '张三', 30, 1, '黑龙江全省线索', NOW(), NOW()
    UNION ALL SELECT '安徽省-合肥销售', 1, '安徽省', '', '', '', NULL, NULL, NULL, NULL, 1, '张三', 30, 1, '安徽全省线索', NOW(), NOW()
    UNION ALL SELECT '江西省-南昌销售', 1, '江西省', '', '', '', NULL, NULL, NULL, NULL, 1, '张三', 30, 1, '江西全省线索', NOW(), NOW()
    UNION ALL SELECT '云南省-昆明销售', 1, '云南省', '', '', '', NULL, NULL, NULL, NULL, 1, '张三', 30, 1, '云南全省线索', NOW(), NOW()
) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM `crm_assign_rule` r WHERE r.rule_name = tmp.rule_name);

-- 行业规则 - 15条
INSERT INTO `crm_assign_rule` (`rule_name`, `rule_type`, `province`, `city`, `district`, `industry`, `min_employee_count`, `max_employee_count`, `min_annual_revenue`, `max_annual_revenue`, `assign_user_id`, `assign_user_name`, `priority`, `is_enabled`, `description`, `create_time`, `update_time`)
SELECT * FROM (
    SELECT 'IT互联网-高新技术部' AS rule_name, 2 AS rule_type, '' AS province, '' AS city, '' AS district, 'IT互联网' AS industry, NULL AS min_employee_count, NULL AS max_employee_count, NULL AS min_annual_revenue, NULL AS max_annual_revenue, 1 AS assign_user_id, '张三' AS assign_user_name, 5 AS priority, 1 AS is_enabled, '互联网行业线索专属' AS description, NOW() AS create_time, NOW() AS update_time
    UNION ALL SELECT '金融行业-金融事业部', 2, '', '', '', '金融', NULL, NULL, NULL, NULL, 1, '张三', 8, 1, '金融行业线索', NOW(), NOW()
    UNION ALL SELECT '制造业-工业部', 2, '', '', '', '制造业', NULL, NULL, NULL, NULL, 1, '张三', 8, 1, '制造业线索', NOW(), NOW()
    UNION ALL SELECT '电商零售-零售部', 2, '', '', '', '零售', NULL, NULL, NULL, NULL, 1, '张三', 10, 1, '零售行业线索', NOW(), NOW()
    UNION ALL SELECT '教育培训-教育行业', 2, '', '', '', '教育培训', NULL, NULL, NULL, NULL, 1, '张三', 10, 1, '教育行业线索', NOW(), NOW()
    UNION ALL SELECT '医疗健康-医疗部', 2, '', '', '', '医疗健康', NULL, NULL, NULL, NULL, 1, '张三', 10, 1, '医疗健康行业线索', NOW(), NOW()
    UNION ALL SELECT '餐饮行业-餐饮部', 2, '', '', '', '餐饮', NULL, NULL, NULL, NULL, 1, '张三', 15, 1, '餐饮行业线索', NOW(), NOW()
    UNION ALL SELECT '物流行业-物流部', 2, '', '', '', '物流', NULL, NULL, NULL, NULL, 1, '张三', 15, 1, '物流行业线索', NOW(), NOW()
    UNION ALL SELECT '建筑工程-工程部', 2, '', '', '', '建筑工程', NULL, NULL, NULL, NULL, 1, '张三', 15, 1, '建筑工程线索', NOW(), NOW()
    UNION ALL SELECT '广告传媒-媒体部', 2, '', '', '', '广告传媒', NULL, NULL, NULL, NULL, 1, '张三', 20, 1, '广告传媒线索', NOW(), NOW()
    UNION ALL SELECT '物业服务-物业部', 2, '', '', '', '物业服务', NULL, NULL, NULL, NULL, 1, '张三', 20, 1, '物业服务线索', NOW(), NOW()
    UNION ALL SELECT '商务服务-商务部', 2, '', '', '', '商务服务', NULL, NULL, NULL, NULL, 1, '张三', 20, 1, '商务服务线索', NOW(), NOW()
    UNION ALL SELECT '化工行业-化工部', 2, '', '', '', '化工', NULL, NULL, NULL, NULL, 1, '张三', 25, 1, '化工行业线索', NOW(), NOW()
    UNION ALL SELECT '农业-农业部', 2, '', '', '', '农业', NULL, NULL, NULL, NULL, 1, '张三', 25, 1, '农业线索', NOW(), NOW()
    UNION ALL SELECT '汽车销售-汽车部', 2, '', '', '', '汽车销售', NULL, NULL, NULL, NULL, 1, '张三', 25, 1, '汽车销售线索', NOW(), NOW()
) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM `crm_assign_rule` r WHERE r.rule_name = tmp.rule_name);

-- 规模规则 - 5条
INSERT INTO `crm_assign_rule` (`rule_name`, `rule_type`, `province`, `city`, `district`, `industry`, `min_employee_count`, `max_employee_count`, `min_annual_revenue`, `max_annual_revenue`, `assign_user_id`, `assign_user_name`, `priority`, `is_enabled`, `description`, `create_time`, `update_time`)
SELECT * FROM (
    SELECT '超大型企业-高优组' AS rule_name, 3 AS rule_type, '' AS province, '' AS city, '' AS district, '' AS industry, 1000 AS min_employee_count, NULL AS max_employee_count, 10000.00 AS min_annual_revenue, NULL AS max_annual_revenue, 1 AS assign_user_id, '张三' AS assign_user_name, 1 AS priority, 1 AS is_enabled, '1000人以上或年营收1亿以上的超大型企业' AS description, NOW() AS create_time, NOW() AS update_time
    UNION ALL SELECT '大型企业-大客户部', 3, '', '', '', '', 500, 1000, 5000.00, 10000.00, 1, '张三', 5, 1, '500-1000人或年营收5000万-1亿的大型企业', NOW(), NOW()
    UNION ALL SELECT '中型企业-成长客户部', 3, '', '', '', '', 100, 500, 1000.00, 5000.00, 1, '张三', 10, 1, '100-500人或年营收1000万-5000万的中型企业', NOW(), NOW()
    UNION ALL SELECT '小型企业-初创客户部', 3, '', '', '', '', 10, 100, 100.00, 1000.00, 1, '张三', 20, 1, '10-100人或年营收100万-1000万的小型企业', NOW(), NOW()
    UNION ALL SELECT '小微企业-扶持部', 3, '', '', '', '', 1, 10, 0.00, 100.00, 1, '张三', 30, 1, '10人以下或年营收100万以下的小微企业', NOW(), NOW()
) AS tmp
WHERE NOT EXISTS (SELECT 1 FROM `crm_assign_rule` r WHERE r.rule_name = tmp.rule_name);

-- =============================================
-- 三、更新部分线索为公海线索（35条）
-- =============================================

-- 将线索ID 1-35设置为公海线索（如果存在）
UPDATE `crm_lead` SET 
    `is_public` = 1,
    `public_reason` = CASE 
        WHEN id % 3 = 0 THEN '长期未跟进超过30天'
        WHEN id % 3 = 1 THEN '跟进无效'
        ELSE '手动回收'
    END,
    `public_time` = NOW(),
    `assign_user_id` = NULL,
    `assign_user_name` = NULL,
    `assign_time` = NULL
WHERE id <= 35 AND (`is_public` = 0 OR `is_public` IS NULL);

-- =============================================
-- 四、分配记录 - 确保40条
-- =============================================

-- 清理旧数据（如果需要）
-- DELETE FROM `crm_assign_record` WHERE id > 0;

-- 插入分配记录（注意：表中没有 rule_name 字段）
INSERT INTO `crm_assign_record` (`lead_id`, `lead_no`, `from_user_id`, `from_user_name`, `to_user_id`, `to_user_name`, `assign_type`, `rule_id`, `reason`, `create_time`)
SELECT id, lead_no, NULL, '系统', 1, '管理员', 1, 1, '自动分配：匹配地域规则', NOW()
FROM `crm_lead` WHERE id <= 10
AND NOT EXISTS (SELECT 1 FROM `crm_assign_record` r WHERE r.lead_id = `crm_lead`.id);

INSERT INTO `crm_assign_record` (`lead_id`, `lead_no`, `from_user_id`, `from_user_name`, `to_user_id`, `to_user_name`, `assign_type`, `rule_id`, `reason`, `create_time`)
SELECT id, lead_no, NULL, '系统', 1, '管理员', 2, NULL, '手动分配：管理员操作', NOW()
FROM `crm_lead` WHERE id > 10 AND id <= 20
AND NOT EXISTS (SELECT 1 FROM `crm_assign_record` r WHERE r.lead_id = `crm_lead`.id);

INSERT INTO `crm_assign_record` (`lead_id`, `lead_no`, `from_user_id`, `from_user_name`, `to_user_id`, `to_user_name`, `assign_type`, `rule_id`, `reason`, `create_time`)
SELECT id, lead_no, 1, '管理员', NULL, NULL, 3, NULL, '回收：长期未跟进', NOW()
FROM `crm_lead` WHERE id > 20 AND id <= 35
AND NOT EXISTS (SELECT 1 FROM `crm_assign_record` r WHERE r.lead_id = `crm_lead`.id);

INSERT INTO `crm_assign_record` (`lead_id`, `lead_no`, `from_user_id`, `from_user_name`, `to_user_id`, `to_user_name`, `assign_type`, `rule_id`, `reason`, `create_time`)
SELECT id, lead_no, NULL, '系统', 1, '管理员', 1, 21, '自动分配：匹配行业规则', NOW()
FROM `crm_lead` WHERE id > 35
AND NOT EXISTS (SELECT 1 FROM `crm_assign_record` r WHERE r.lead_id = `crm_lead`.id);

-- =============================================
-- 执行完成提示
-- =============================================
SELECT '测试数据初始化完成' AS message, 
       (SELECT COUNT(*) FROM `crm_lead`) AS lead_count,
       (SELECT COUNT(*) FROM `crm_assign_rule`) AS assign_rule_count,
       (SELECT COUNT(*) FROM `crm_assign_record`) AS assign_record_count,
       (SELECT COUNT(*) FROM `crm_lead` WHERE is_public = 1) AS public_pool_count;
