-- ============================================================
-- 智能分配与回收、查重防重功能 - 数据库表结构脚本
-- ============================================================

-- 1. 修改线索表，添加公海相关字段和查重字段
ALTER TABLE crm_lead ADD COLUMN is_public TINYINT DEFAULT 0 COMMENT '是否在公海：0-否 1-是' AFTER tags;
ALTER TABLE crm_lead ADD COLUMN public_reason VARCHAR(500) COMMENT '进入公海原因' AFTER is_public;
ALTER TABLE crm_lead ADD COLUMN public_time DATETIME COMMENT '进入公海时间' AFTER public_reason;
ALTER TABLE crm_lead ADD COLUMN last_follow_time DATETIME COMMENT '最后跟进时间' AFTER public_time;
ALTER TABLE crm_lead ADD COLUMN follow_count BIGINT DEFAULT 0 COMMENT '跟进次数' AFTER last_follow_time;
ALTER TABLE crm_lead ADD COLUMN source_user_id BIGINT COMMENT '来源用户ID' AFTER follow_count;
ALTER TABLE crm_lead ADD COLUMN source_user_name VARCHAR(50) COMMENT '来源用户名' AFTER source_user_id;
ALTER TABLE crm_lead ADD COLUMN employee_count INT COMMENT '企业规模-员工数' AFTER source_user_name;
ALTER TABLE crm_lead ADD COLUMN annual_revenue DECIMAL(18,2) COMMENT '企业规模-年营收(万元)' AFTER employee_count;
ALTER TABLE crm_lead ADD COLUMN company_scale VARCHAR(50) COMMENT '企业规模描述：小微企业、中型企业、大型企业' AFTER annual_revenue;

-- 2. 创建分配规则表
CREATE TABLE IF NOT EXISTS crm_assign_rule (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    rule_name VARCHAR(100) NOT NULL COMMENT '规则名称',
    rule_type TINYINT DEFAULT 1 COMMENT '规则类型：1-地域规则 2-行业规则 3-规模规则',
    province VARCHAR(50) COMMENT '省份',
    city VARCHAR(50) COMMENT '城市',
    district VARCHAR(50) COMMENT '区县',
    industry VARCHAR(100) COMMENT '行业',
    min_employee_count INT COMMENT '最小员工数',
    max_employee_count INT COMMENT '最大员工数',
    min_annual_revenue DECIMAL(18,2) COMMENT '最小年营收(万元)',
    max_annual_revenue DECIMAL(18,2) COMMENT '最大年营收(万元)',
    assign_user_id BIGINT NOT NULL COMMENT '分配目标用户ID',
    assign_user_name VARCHAR(50) COMMENT '分配目标用户名',
    priority INT DEFAULT 100 COMMENT '优先级(数字越小优先级越高)',
    is_enabled TINYINT DEFAULT 1 COMMENT '是否启用：1-启用 0-禁用',
    description VARCHAR(500) COMMENT '规则描述',
    is_deleted TINYINT DEFAULT 0 COMMENT '软删除：0-未删除 1-已删除',
    create_by VARCHAR(50) COMMENT '创建人',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(50) COMMENT '更新人',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    KEY idx_rule_type (rule_type),
    KEY idx_priority (priority),
    KEY idx_enabled (is_enabled)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分配规则表';

-- 3. 创建分配记录表
CREATE TABLE IF NOT EXISTS crm_assign_record (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    lead_id BIGINT NOT NULL COMMENT '线索ID',
    lead_no VARCHAR(50) COMMENT '线索编号',
    from_user_id BIGINT COMMENT '原负责人ID',
    from_user_name VARCHAR(50) COMMENT '原负责人姓名',
    to_user_id BIGINT COMMENT '新负责人ID',
    to_user_name VARCHAR(50) COMMENT '新负责人姓名',
    assign_type TINYINT DEFAULT 2 COMMENT '分配类型：1-自动分配 2-手动分配 3-回收',
    rule_id BIGINT COMMENT '匹配的规则ID',
    reason VARCHAR(500) COMMENT '分配/回收原因',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '分配时间',
    PRIMARY KEY (id),
    KEY idx_lead_id (lead_id),
    KEY idx_from_user (from_user_id),
    KEY idx_to_user (to_user_id),
    KEY idx_assign_type (assign_type),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分配记录表';

-- 4. 创建线索跟进记录表
CREATE TABLE IF NOT EXISTS crm_lead_follow (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    lead_id BIGINT NOT NULL COMMENT '线索ID',
    lead_no VARCHAR(50) COMMENT '线索编号',
    follow_user_id BIGINT NOT NULL COMMENT '跟进人ID',
    follow_user_name VARCHAR(50) COMMENT '跟进人姓名',
    follow_type TINYINT DEFAULT 5 COMMENT '跟进方式：1-电话 2-微信 3-邮件 4-拜访 5-其他',
    follow_content TEXT COMMENT '跟进内容',
    next_follow_time DATETIME COMMENT '下次跟进时间',
    next_follow_remark VARCHAR(500) COMMENT '下次跟进备注',
    is_last TINYINT DEFAULT 1 COMMENT '是否最后一次跟进：1-是 0-否',
    is_deleted TINYINT DEFAULT 0 COMMENT '软删除：0-未删除 1-已删除',
    create_by VARCHAR(50) COMMENT '创建人',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(50) COMMENT '更新人',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    KEY idx_lead_id (lead_id),
    KEY idx_follow_user (follow_user_id),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='线索跟进记录表';

-- ============================================================
-- 插入预置分配规则数据
-- ============================================================

-- 地域规则 - 北京地区
INSERT INTO crm_assign_rule (rule_name, rule_type, province, city, industry, assign_user_id, assign_user_name, priority, is_enabled, description)
VALUES ('北京地区IT互联网企业分配给销售张三', 1, '北京市', '北京市', 'IT互联网', 2, '张三', 1, 1, '北京地区IT互联网行业的线索自动分配给销售张三');

INSERT INTO crm_assign_rule (rule_name, rule_type, province, city, industry, assign_user_id, assign_user_name, priority, is_enabled, description)
VALUES ('北京地区金融行业分配给销售李四', 1, '北京市', '北京市', '金融', 3, '李四', 2, 1, '北京地区金融行业的线索自动分配给销售李四');

-- 地域规则 - 上海地区
INSERT INTO crm_assign_rule (rule_name, rule_type, province, city, industry, assign_user_id, assign_user_name, priority, is_enabled, description)
VALUES ('上海地区制造业分配给销售王五', 1, '上海市', '上海市', '制造业', 4, '王五', 3, 1, '上海地区制造业的线索自动分配给销售王五');

INSERT INTO crm_assign_rule (rule_name, rule_type, province, city, industry, assign_user_id, assign_user_name, priority, is_enabled, description)
VALUES ('上海地区电商行业分配给销售赵六', 1, '上海市', '上海市', '电商', 5, '赵六', 4, 1, '上海地区电商行业的线索自动分配给销售赵六');

-- 行业规则
INSERT INTO crm_assign_rule (rule_name, rule_type, province, city, industry, assign_user_id, assign_user_name, priority, is_enabled, description)
VALUES ('教育培训行业分配给销售孙七', 2, NULL, NULL, '教育培训', 6, '孙七', 50, 1, '所有地区教育培训行业的线索自动分配给销售孙七');

INSERT INTO crm_assign_rule (rule_name, rule_type, province, city, industry, assign_user_id, assign_user_name, priority, is_enabled, description)
VALUES ('医疗健康行业分配给销售周八', 2, NULL, NULL, '医疗健康', 7, '周八', 51, 1, '所有地区医疗健康行业的线索自动分配给销售周八');

-- 规模规则
INSERT INTO crm_assign_rule (rule_name, rule_type, province, city, industry, min_employee_count, max_employee_count, assign_user_id, assign_user_name, priority, is_enabled, description)
VALUES ('大型企业(500人以上)分配给销售钱九', 3, NULL, NULL, NULL, 500, 999999, 8, '钱九', 20, 1, '员工数500人以上的大型企业分配给销售钱九');

INSERT INTO crm_assign_rule (rule_name, rule_type, province, city, industry, min_employee_count, max_employee_count, assign_user_id, assign_user_name, priority, is_enabled, description)
VALUES ('中型企业(50-500人)分配给销售吴十', 3, NULL, NULL, NULL, 50, 500, 9, '吴十', 21, 1, '员工数50-500人的中型企业分配给销售吴十');

INSERT INTO crm_assign_rule (rule_name, rule_type, province, city, industry, min_employee_count, max_employee_count, assign_user_id, assign_user_name, priority, is_enabled, description)
VALUES ('小微企业(50人以下)分配给销售郑十一', 3, NULL, NULL, NULL, 1, 50, 10, '郑十一', 22, 1, '员工数50人以下的小微企业分配给销售郑十一');

-- ============================================================
-- 插入分配记录测试数据
-- ============================================================

INSERT INTO crm_assign_record (lead_id, lead_no, from_user_id, from_user_name, to_user_id, to_user_name, assign_type, rule_id, reason, create_time)
VALUES (1, 'LD202604230001', NULL, NULL, 2, '张三', 1, 1, '自动分配：匹配规则「北京地区IT互联网企业分配给销售张三」', NOW());

INSERT INTO crm_assign_record (lead_id, lead_no, from_user_id, from_user_name, to_user_id, to_user_name, assign_type, rule_id, reason, create_time)
VALUES (2, 'LD202604230002', 3, '李四', 2, '张三', 2, NULL, '手动分配：李四转给张三', NOW());

INSERT INTO crm_assign_record (lead_id, lead_no, from_user_id, from_user_name, to_user_id, to_user_name, assign_type, rule_id, reason, create_time)
VALUES (3, 'LD202604230003', 4, '王五', NULL, NULL, 3, NULL, '回收：长期未跟进超过30天', NOW());

-- ============================================================
-- 插入线索跟进记录测试数据
-- ============================================================

INSERT INTO crm_lead_follow (lead_id, lead_no, follow_user_id, follow_user_name, follow_type, follow_content, next_follow_time, next_follow_remark, is_last, create_time)
VALUES (1, 'LD202604230001', 2, '张三', 1, '首次电话沟通，客户对我们的产品很感兴趣，约定下周二上门演示。', '2026-04-25 10:00:00', '带产品演示PPT和方案书', 1, '2026-04-23 09:30:00');

INSERT INTO crm_lead_follow (lead_id, lead_no, follow_user_id, follow_user_name, follow_type, follow_content, next_follow_time, next_follow_remark, is_last, create_time)
VALUES (2, 'LD202604230002', 3, '李四', 4, '上门拜访，客户主要需求是客户管理和销售自动化。', '2026-04-26 14:00:00', '准备详细报价方案', 1, '2026-04-22 15:00:00');

INSERT INTO crm_lead_follow (lead_id, lead_no, follow_user_id, follow_user_name, follow_type, follow_content, next_follow_time, next_follow_remark, is_last, create_time)
VALUES (4, 'LD202604230004', 2, '张三', 2, '微信沟通，发送了产品资料，客户说会仔细看看。', NULL, NULL, 1, '2026-04-20 16:00:00');
