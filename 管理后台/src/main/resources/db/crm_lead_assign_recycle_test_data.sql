-- ============================================================
-- 智能分配与回收功能测试数据（30条以上公海线索）
-- ============================================================

-- 清空现有公海相关测试数据
-- UPDATE crm_lead SET is_public = 0, public_reason = NULL, public_time = NULL;

-- 创建公海线索测试数据（30条）
-- 注意：以下数据假设已有基本线索数据，这里主要标记部分线索为公海

-- ============================================================
-- 插入公海线索测试数据（通过UPDATE已有线索或INSERT新线索）
-- ============================================================

-- 首先更新已有线索的公海字段
-- 长期未跟进的线索（超过30天未跟进）
UPDATE crm_lead SET 
    is_public = 1,
    public_reason = '长期未跟进：超过30天未跟进',
    public_time = NOW(),
    assign_user_id = NULL,
    assign_user_name = NULL,
    assign_time = NULL,
    status = 4,
    last_follow_time = DATE_SUB(NOW(), INTERVAL 45 DAY),
    follow_count = 0,
    employee_count = 50,
    annual_revenue = 100.00,
    company_scale = '小微企业'
WHERE id IN (3, 5, 6, 7);

-- 跟进无效的线索
UPDATE crm_lead SET 
    is_public = 1,
    public_reason = '跟进无效：客户明确表示暂时不需要',
    public_time = NOW(),
    assign_user_id = NULL,
    assign_user_name = NULL,
    assign_time = NULL,
    status = 4,
    last_follow_time = DATE_SUB(NOW(), INTERVAL 10 DAY),
    follow_count = 3,
    employee_count = 200,
    annual_revenue = 500.00,
    company_scale = '中型企业'
WHERE id IN (8);

-- ============================================================
-- 插入30条新的公海线索测试数据
-- ============================================================

-- 小微企业公海线索
INSERT INTO crm_lead (lead_no, lead_name, contact_name, contact_mobile, province, city, industry, source_id, source_name, level, status, is_public, public_reason, public_time, last_follow_time, follow_count, employee_count, annual_revenue, company_scale, is_deleted, create_time)
VALUES 
('LD'||UNIX_TIMESTAMP()||'01', '北京天天美食餐饮有限公司', '王经理', '13800138001', '北京市', '北京市', '餐饮', 1, '手工录入', 3, 4, 1, '长期未跟进：超过30天', NOW(), DATE_SUB(NOW(), INTERVAL 35 DAY), 0, 20, 50.00, '小微企业', 0, NOW()),

('LD'||UNIX_TIMESTAMP()||'02', '上海鲜果汇水果店', '李店长', '13800138002', '上海市', '上海市', '零售', 2, '官网表单', 3, 4, 1, '跟进无效：客户已选择竞争对手', NOW(), DATE_SUB(NOW(), INTERVAL 15 DAY), 2, 15, 30.00, '小微企业', 0, NOW()),

('LD'||UNIX_TIMESTAMP()||'03', '广州快剪理发店', '张师傅', '13800138003', '广东省', '广州市', '生活服务', 3, '电话咨询', 3, 4, 1, '长期未跟进：超过30天', NOW(), DATE_SUB(NOW(), INTERVAL 60 DAY), 1, 8, 20.00, '小微企业', 0, NOW()),

('LD'||UNIX_TIMESTAMP()||'04', '深圳爱宠宠物医院', '陈医生', '13800138004', '广东省', '深圳市', '医疗服务', 4, '市场活动', 2, 4, 1, '跟进无效：暂时不考虑系统', NOW(), DATE_SUB(NOW(), INTERVAL 20 DAY), 3, 25, 80.00, '小微企业', 0, NOW()),

('LD'||UNIX_TIMESTAMP()||'05', '杭州甜蜜时光蛋糕店', '刘店长', '13800138005', '浙江省', '杭州市', '餐饮', 5, '客户转介绍', 3, 4, 1, '长期未跟进：超过30天', NOW(), DATE_SUB(NOW(), INTERVAL 40 DAY), 0, 12, 45.00, '小微企业', 0, NOW()),

('LD'||UNIX_TIMESTAMP()||'06', '南京晨光文具店', '赵老板', '13800138006', '江苏省', '南京市', '零售', 6, '在线咨询', 3, 4, 1, '跟进无效：预算不足', NOW(), DATE_SUB(NOW(), INTERVAL 25 DAY), 2, 10, 35.00, '小微企业', 0, NOW()),

('LD'||UNIX_TIMESTAMP()||'07', '武汉美丽造型理发店', '孙总监', '13800138007', '湖北省', '武汉市', '生活服务', 7, 'Excel导入', 3, 4, 1, '长期未跟进：超过30天', NOW(), DATE_SUB(NOW(), INTERVAL 50 DAY), 1, 18, 60.00, '小微企业', 0, NOW()),

('LD'||UNIX_TIMESTAMP()||'08', '成都蜀湘汇川菜馆', '周经理', '13800138008', '四川省', '成都市', '餐饮', 8, '其他来源', 2, 4, 1, '跟进无效：客户需求不明确', NOW(), DATE_SUB(NOW(), INTERVAL 18 DAY), 4, 30, 90.00, '小微企业', 0, NOW());

-- 中型企业公海线索
INSERT INTO crm_lead (lead_no, lead_name, contact_name, contact_mobile, province, city, industry, source_id, source_name, level, status, is_public, public_reason, public_time, last_follow_time, follow_count, employee_count, annual_revenue, company_scale, is_deleted, create_time)
VALUES 
('LD'||UNIX_TIMESTAMP()||'09', '北京鑫源科技有限公司', '王总', '13800138009', '北京市', '北京市', 'IT互联网', 1, '手工录入', 1, 4, 1, '长期未跟进：超过30天', NOW(), DATE_SUB(NOW(), INTERVAL 38 DAY), 0, 100, 500.00, '中型企业', 0, NOW()),

('LD'||UNIX_TIMESTAMP()||'10', '上海恒通物流有限公司', '李经理', '13800138010', '上海市', '上海市', '物流', 2, '官网表单', 2, 4, 1, '跟进无效：客户已选择其他厂商', NOW(), DATE_SUB(NOW(), INTERVAL 12 DAY), 3, 150, 800.00, '中型企业', 0, NOW()),

('LD'||UNIX_TIMESTAMP()||'11', '广州华南制造有限公司', '张厂长', '13800138011', '广东省', '广州市', '制造业', 3, '电话咨询', 1, 4, 1, '长期未跟进：超过30天', NOW(), DATE_SUB(NOW(), INTERVAL 45 DAY), 1, 200, 1200.00, '中型企业', 0, NOW()),

('LD'||UNIX_TIMESTAMP()||'12', '深圳智慧医疗科技有限公司', '陈总', '13800138012', '广东省', '深圳市', '医疗健康', 4, '市场活动', 1, 4, 1, '跟进无效：暂时不采购', NOW(), DATE_SUB(NOW(), INTERVAL 22 DAY), 5, 80, 600.00, '中型企业', 0, NOW()),

('LD'||UNIX_TIMESTAMP()||'13', '杭州新东方教育集团', '刘校长', '13800138013', '浙江省', '杭州市', '教育培训', 5, '客户转介绍', 2, 4, 1, '长期未跟进：超过30天', NOW(), DATE_SUB(NOW(), INTERVAL 55 DAY), 2, 120, 900.00, '中型企业', 0, NOW()),

('LD'||UNIX_TIMESTAMP()||'14', '南京金陵建筑工程有限公司', '赵经理', '13800138014', '江苏省', '南京市', '建筑工程', 6, '在线咨询', 1, 4, 1, '跟进无效：预算审批未通过', NOW(), DATE_SUB(NOW(), INTERVAL 28 DAY), 4, 250, 1500.00, '中型企业', 0, NOW()),

('LD'||UNIX_TIMESTAMP()||'15', '武汉楚天传媒有限公司', '孙总监', '13800138015', '湖北省', '武汉市', '广告传媒', 7, 'Excel导入', 2, 4, 1, '长期未跟进：超过30天', NOW(), DATE_SUB(NOW(), INTERVAL 42 DAY), 1, 90, 700.00, '中型企业', 0, NOW()),

('LD'||UNIX_TIMESTAMP()||'16', '成都天府旅游有限公司', '周总', '13800138016', '四川省', '成都市', '旅游服务', 8, '其他来源', 2, 4, 1, '跟进无效：客户需求变化', NOW(), DATE_SUB(NOW(), INTERVAL 16 DAY), 3, 180, 1100.00, '中型企业', 0, NOW());

-- 大型企业公海线索
INSERT INTO crm_lead (lead_no, lead_name, contact_name, contact_mobile, province, city, industry, source_id, source_name, level, status, is_public, public_reason, public_time, last_follow_time, follow_count, employee_count, annual_revenue, company_scale, is_deleted, create_time)
VALUES 
('LD'||UNIX_TIMESTAMP()||'17', '北京国际大厦物业服务有限公司', '王总', '13800138017', '北京市', '北京市', '物业服务', 1, '手工录入', 1, 4, 1, '长期未跟进：超过30天', NOW(), DATE_SUB(NOW(), INTERVAL 60 DAY), 0, 600, 3000.00, '大型企业', 0, NOW()),

('LD'||UNIX_TIMESTAMP()||'18', '上海环球金融中心有限公司', '李经理', '13800138018', '上海市', '上海市', '商务服务', 2, '官网表单', 1, 4, 1, '跟进无效：已有内部系统', NOW(), DATE_SUB(NOW(), INTERVAL 25 DAY), 6, 800, 5000.00, '大型企业', 0, NOW()),

('LD'||UNIX_TIMESTAMP()||'19', '广州白云山制药集团', '张总', '13800138019', '广东省', '广州市', '医药健康', 3, '电话咨询', 1, 4, 1, '长期未跟进：超过30天', NOW(), DATE_SUB(NOW(), INTERVAL 70 DAY), 2, 1500, 8000.00, '大型企业', 0, NOW()),

('LD'||UNIX_TIMESTAMP()||'20', '深圳平安保险集团', '陈经理', '13800138020', '广东省', '深圳市', '金融保险', 4, '市场活动', 1, 4, 1, '跟进无效：总部统一采购', NOW(), DATE_SUB(NOW(), INTERVAL 30 DAY), 4, 2000, 10000.00, '大型企业', 0, NOW());

-- 更多公海线索（确保超过30条）
INSERT INTO crm_lead (lead_no, lead_name, contact_name, contact_mobile, province, city, industry, source_id, source_name, level, status, is_public, public_reason, public_time, last_follow_time, follow_count, employee_count, annual_revenue, company_scale, is_deleted, create_time)
VALUES 
('LD'||UNIX_TIMESTAMP()||'21', '天津海河化工有限公司', '王厂长', '13800138021', '天津市', '天津市', '化工', 1, '手工录入', 2, 4, 1, '长期未跟进：超过30天', NOW(), DATE_SUB(NOW(), INTERVAL 35 DAY), 0, 25, 150.00, '小微企业', 0, NOW()),

('LD'||UNIX_TIMESTAMP()||'22', '重庆山城火锅店连锁', '李店长', '13800138022', '重庆市', '重庆市', '餐饮', 2, '官网表单', 2, 4, 1, '跟进无效：客户选择其他品牌', NOW(), DATE_SUB(NOW(), INTERVAL 18 DAY), 2, 35, 180.00, '小微企业', 0, NOW()),

('LD'||UNIX_TIMESTAMP()||'23', '西安古城旅游开发有限公司', '张总', '13800138023', '陕西省', '西安市', '旅游服务', 3, '电话咨询', 1, 4, 1, '长期未跟进：超过30天', NOW(), DATE_SUB(NOW(), INTERVAL 50 DAY), 1, 120, 700.00, '中型企业', 0, NOW()),

('LD'||UNIX_TIMESTAMP()||'24', '青岛海洋运输有限公司', '陈经理', '13800138024', '山东省', '青岛市', '物流', 4, '市场活动', 1, 4, 1, '跟进无效：暂时不需要', NOW(), DATE_SUB(NOW(), INTERVAL 22 DAY), 3, 200, 1500.00, '中型企业', 0, NOW()),

('LD'||UNIX_TIMESTAMP()||'25', '大连东北重工机械有限公司', '刘厂长', '13800138025', '辽宁省', '大连市', '制造业', 5, '客户转介绍', 1, 4, 1, '长期未跟进：超过30天', NOW(), DATE_SUB(NOW(), INTERVAL 45 DAY), 0, 500, 3500.00, '大型企业', 0, NOW()),

('LD'||UNIX_TIMESTAMP()||'26', '厦门鼓浪屿酒店集团', '赵总', '13800138026', '福建省', '厦门市', '酒店服务', 6, '在线咨询', 2, 4, 1, '跟进无效：已有管理系统', NOW(), DATE_SUB(NOW(), INTERVAL 15 DAY), 4, 300, 2000.00, '中型企业', 0, NOW()),

('LD'||UNIX_TIMESTAMP()||'27', '昆明春城花卉有限公司', '孙经理', '13800138027', '云南省', '昆明市', '农业', 7, 'Excel导入', 3, 4, 1, '长期未跟进：超过30天', NOW(), DATE_SUB(NOW(), INTERVAL 55 DAY), 1, 40, 120.00, '小微企业', 0, NOW()),

('LD'||UNIX_TIMESTAMP()||'28', '哈尔滨冰雪大世界有限公司', '周总', '13800138028', '黑龙江省', '哈尔滨市', '旅游服务', 8, '其他来源', 2, 4, 1, '跟进无效：季节性业务', NOW(), DATE_SUB(NOW(), INTERVAL 20 DAY), 2, 150, 800.00, '中型企业', 0, NOW()),

('LD'||UNIX_TIMESTAMP()||'29', '苏州园林设计有限公司', '吴经理', '13800138029', '江苏省', '苏州市', '设计服务', 1, '手工录入', 1, 4, 1, '长期未跟进：超过30天', NOW(), DATE_SUB(NOW(), INTERVAL 40 DAY), 0, 60, 400.00, '中型企业', 0, NOW()),

('LD'||UNIX_TIMESTAMP()||'30', '郑州中原汽车销售有限公司', '郑总', '13800138030', '河南省', '郑州市', '汽车销售', 2, '官网表单', 2, 4, 1, '跟进无效：客户已购车', NOW(), DATE_SUB(NOW(), INTERVAL 10 DAY), 5, 80, 600.00, '中型企业', 0, NOW()),

('LD'||UNIX_TIMESTAMP()||'31', '长沙橘子洲文化传播有限公司', '王经理', '13800138031', '湖南省', '长沙市', '广告传媒', 3, '电话咨询', 1, 4, 1, '长期未跟进：超过30天', NOW(), DATE_SUB(NOW(), INTERVAL 60 DAY), 1, 100, 500.00, '中型企业', 0, NOW()),

('LD'||UNIX_TIMESTAMP()||'32', '合肥科大讯飞科技有限公司', '刘总', '13800138032', '安徽省', '合肥市', 'IT互联网', 4, '市场活动', 1, 4, 1, '跟进无效：自己开发', NOW(), DATE_SUB(NOW(), INTERVAL 25 DAY), 3, 1000, 6000.00, '大型企业', 0, NOW());

-- ============================================================
-- 更新已有线索的规模字段
-- ============================================================

UPDATE crm_lead SET 
    employee_count = 15,
    annual_revenue = 80.00,
    company_scale = '小微企业'
WHERE id = 1;

UPDATE crm_lead SET 
    employee_count = 200,
    annual_revenue = 1500.00,
    company_scale = '中型企业'
WHERE id = 2;

UPDATE crm_lead SET 
    employee_count = 800,
    annual_revenue = 5000.00,
    company_scale = '大型企业'
WHERE id = 4;
