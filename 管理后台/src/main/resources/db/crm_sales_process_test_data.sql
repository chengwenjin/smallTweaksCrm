-- ============================================================
-- 销售过程与商机管理 -> 过程跟进与外勤
-- 测试数据初始化脚本
-- ============================================================

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET collation_connection = utf8mb4_unicode_ci;

-- 先清空测试数据（如果存在）
DELETE FROM `crm_todo_reminder` WHERE `todo_no` LIKE 'TDTEST%';
DELETE FROM `crm_field_track` WHERE `track_no` LIKE 'TKTEST%';
DELETE FROM `crm_field_checkin` WHERE `checkin_no` LIKE 'CKTEST%';
DELETE FROM `crm_follow_record` WHERE `business_type` IN ('customer', 'lead', 'opportunity') AND `id` > 0;

-- 跟进记录测试数据（35条）
INSERT INTO `crm_follow_record` 
(`business_type`, `business_id`, `business_name`, `follow_user_id`, `follow_user_name`, `follow_type`, `content_type`, `text_content`, `voice_url`, `voice_duration`, `image_urls`, `video_url`, `video_duration`, `file_url`, `file_name`, `file_size`, `location_latitude`, `location_longitude`, `location_address`, `next_follow_time`, `next_follow_remark`, `todo_id`, `is_last`, `create_time`)
VALUES
('customer', 1, '北京科技有限公司', 1, '系统管理员', 1, 1, '电话沟通客户需求，客户表示对我们的产品很感兴趣，约定下周安排现场演示。', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 39.9042, 116.4074, '北京市朝阳区建国路88号', '2026-05-05 14:00:00', '安排现场演示', NULL, 1, '2026-04-28 10:30:00'),
('customer', 2, '上海贸易集团', 1, '系统管理员', 2, 1, '微信发送产品报价单，客户反馈价格偏高，需要再商量。', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 31.2304, 121.4737, '上海市浦东新区陆家嘴金融中心', '2026-05-03 09:30:00', '跟进价格优惠', NULL, 1, '2026-04-28 11:15:00'),
('customer', 3, '广州制造企业', 1, '系统管理员', 4, 3, '现场拜访客户，参观了客户的生产车间，了解了客户的实际需求。拍摄了现场照片。', NULL, NULL, '["https://example.com/images/visit1.jpg", "https://example.com/images/visit2.jpg"]', NULL, NULL, NULL, NULL, NULL, 23.1291, 113.2644, '广州市天河区珠江新城', '2026-05-10 10:00:00', '提交解决方案', NULL, 1, '2026-04-28 14:00:00'),
('customer', 1, '北京科技有限公司', 1, '系统管理员', 2, 2, '发送语音消息，详细介绍产品功能。', 'https://example.com/voice/msg1.mp3', 120, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, '2026-04-27 15:20:00'),
('customer', 4, '深圳电子科技', 1, '系统管理员', 3, 1, '发送邮件确认合同细节，客户已确认，准备签署。', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 22.5431, 114.0579, '深圳市南山区科技园', '2026-05-01 16:00:00', '签署合同', NULL, 1, '2026-04-28 09:00:00'),
('customer', 5, '杭州互联网公司', 1, '系统管理员', 1, 1, '电话沟通项目进度，客户表示满意，希望加快推进。', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 30.2741, 120.1551, '杭州市西湖区文三路', '2026-05-04 10:00:00', '项目推进会', NULL, 1, '2026-04-28 16:30:00'),
('customer', 6, '成都软件开发', 1, '系统管理员', 2, 3, '微信发送产品截图，客户反馈界面设计需要调整。', NULL, NULL, '["https://example.com/images/screenshot1.png"]', NULL, NULL, NULL, NULL, NULL, 30.5728, 104.0668, '成都市高新区天府大道', '2026-05-06 11:00:00', '调整界面设计', NULL, 1, '2026-04-27 10:00:00'),
('customer', 7, '武汉制造企业', 1, '系统管理员', 4, 1, '现场拜访，了解客户生产流程，准备定制方案。', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 30.5928, 114.3055, '武汉市东湖高新区', '2026-05-08 14:00:00', '提交定制方案', NULL, 1, '2026-04-26 09:30:00'),
('customer', 8, '南京科技公司', 1, '系统管理员', 1, 5, '电话确认技术方案，发送技术文档给客户参考。', NULL, NULL, NULL, NULL, NULL, 'https://example.com/files/tech_spec.pdf', '技术规格说明书.pdf', 1048576, 32.0603, 118.7969, '南京市建邺区河西CBD', '2026-05-02 15:00:00', '确认技术方案', NULL, 1, '2026-04-28 13:45:00'),
('customer', 9, '重庆工业集团', 1, '系统管理员', 2, 1, '微信沟通付款事宜，客户表示下周安排付款。', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 29.4316, 106.9123, '重庆市渝北区两江新区', '2026-05-05 10:00:00', '跟进付款', NULL, 1, '2026-04-27 14:20:00'),
('customer', 10, '天津贸易公司', 1, '系统管理员', 3, 1, '发送邮件确认发货时间，客户希望尽快安排。', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 39.0842, 117.2009, '天津市和平区南京路', '2026-04-30 09:00:00', '安排发货', NULL, 1, '2026-04-28 08:30:00'),
('lead', 1, '线索-北京新客户', 1, '系统管理员', 1, 1, '首次电话联系，客户表示有兴趣了解产品，约定下周详细沟通。', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-04 10:00:00', '详细沟通产品', NULL, 1, '2026-04-28 09:15:00'),
('lead', 2, '线索-上海潜在客户', 1, '系统管理员', 2, 1, '微信发送公司介绍，客户已查看，待反馈。', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-03 14:00:00', '跟进反馈', NULL, 1, '2026-04-27 16:00:00'),
('lead', 3, '线索-广州意向客户', 1, '系统管理员', 4, 3, '现场拜访客户，了解客户具体需求，拍摄了现场照片。', NULL, NULL, '["https://example.com/images/lead_visit1.jpg"]', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-07 10:00:00', '提交需求方案', NULL, 1, '2026-04-26 11:30:00'),
('opportunity', 1, '商机-北京项目', 1, '系统管理员', 1, 1, '电话沟通项目细节，客户确认预算范围，准备报价。', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-02 15:00:00', '提交报价单', NULL, 1, '2026-04-28 11:00:00'),
('opportunity', 2, '商机-上海大项目', 1, '系统管理员', 4, 3, '现场演示产品，客户反应良好，拍摄了演示现场照片。', NULL, NULL, '["https://example.com/images/demo1.jpg", "https://example.com/images/demo2.jpg"]', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-06 10:00:00', '跟进演示反馈', NULL, 1, '2026-04-27 09:30:00'),
('customer', 1, '北京科技有限公司', 1, '系统管理员', 1, 1, '电话确认演示时间，客户表示周三下午有空。', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-03 14:00:00', '现场演示', NULL, 0, '2026-04-25 10:00:00'),
('customer', 2, '上海贸易集团', 1, '系统管理员', 2, 1, '微信发送竞品对比资料，客户表示需要研究。', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-02 14:00:00', '跟进对比结果', NULL, 0, '2026-04-26 14:00:00'),
('customer', 3, '广州制造企业', 1, '系统管理员', 3, 5, '发送技术方案文档，等待客户反馈。', NULL, NULL, NULL, NULL, NULL, 'https://example.com/files/solution.pdf', '技术解决方案.pdf', 2097152, NULL, NULL, NULL, '2026-05-05 10:00:00', '确认方案', NULL, 0, '2026-04-24 16:00:00'),
('customer', 4, '深圳电子科技', 1, '系统管理员', 1, 1, '电话确认合同签署时间，客户表示周五可以签署。', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-30 10:00:00', '签署合同', NULL, 0, '2026-04-27 11:00:00'),
('customer', 5, '杭州互联网公司', 1, '系统管理员', 2, 3, '微信发送项目进度截图，客户表示满意。', NULL, NULL, '["https://example.com/images/progress1.png"]', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, '2026-04-25 15:00:00'),
('customer', 6, '成都软件开发', 1, '系统管理员', 1, 1, '电话确认UI设计稿，客户提出修改意见。', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-04 10:00:00', '提交修改稿', NULL, 0, '2026-04-26 09:00:00'),
('customer', 7, '武汉制造企业', 1, '系统管理员', 4, 1, '现场拜访，收集客户需求，准备方案。', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-07 14:00:00', '提交需求方案', NULL, 0, '2026-04-24 10:00:00'),
('customer', 8, '南京科技公司', 1, '系统管理员', 3, 1, '发送邮件确认技术细节，等待回复。', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-01 14:00:00', '确认技术细节', NULL, 0, '2026-04-27 13:00:00'),
('customer', 9, '重庆工业集团', 1, '系统管理员', 1, 1, '电话确认付款计划，客户表示下周三付款。', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-04 10:00:00', '跟进付款', NULL, 0, '2026-04-26 15:00:00'),
('customer', 10, '天津贸易公司', 1, '系统管理员', 2, 1, '微信确认发货地址，客户已提供。', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, '2026-04-27 10:00:00'),
('lead', 4, '线索-深圳新客户', 1, '系统管理员', 1, 1, '首次联系，客户表示有兴趣了解我们的CRM系统。', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-05 10:00:00', '详细介绍产品', NULL, 1, '2026-04-28 14:00:00'),
('lead', 5, '线索-杭州潜在客户', 1, '系统管理员', 2, 1, '微信发送产品资料，客户已接收。', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-03 14:00:00', '跟进反馈', NULL, 1, '2026-04-27 11:00:00'),
('lead', 6, '线索-成都意向客户', 1, '系统管理员', 1, 1, '电话沟通需求，客户希望了解更多关于报表功能的信息。', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-04 15:00:00', '发送报表功能说明', NULL, 1, '2026-04-28 09:30:00'),
('opportunity', 3, '商机-广州项目', 1, '系统管理员', 1, 1, '电话确认报价，客户表示预算内，准备合同。', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-03 10:00:00', '准备合同', NULL, 1, '2026-04-28 10:00:00'),
('opportunity', 4, '商机-深圳项目', 1, '系统管理员', 2, 3, '微信发送产品演示视频，客户已查看。', NULL, NULL, '["https://example.com/images/video_thumb.jpg"]', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2026-05-05 14:00:00', '跟进视频反馈', NULL, 1, '2026-04-27 16:00:00'),
('customer', 1, '北京科技有限公司', 1, '系统管理员', 1, 1, '电话确认下周演示的具体时间和地点。', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, '2026-04-23 14:00:00'),
('customer', 2, '上海贸易集团', 1, '系统管理员', 1, 1, '电话跟进价格谈判，客户表示需要再考虑。', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-29 10:00:00', '再次跟进', NULL, 0, '2026-04-25 09:00:00'),
('customer', 3, '广州制造企业', 1, '系统管理员', 2, 1, '微信发送方案初稿，等待客户反馈。', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2026-04-30 14:00:00', '跟进方案反馈', NULL, 0, '2026-04-26 16:00:00');

-- 外勤签到测试数据（15条）
INSERT INTO `crm_field_checkin` 
(`checkin_no`, `checkin_user_id`, `checkin_user_name`, `checkin_type`, `checkin_time`, `latitude`, `longitude`, `location_address`, `location_province`, `location_city`, `location_district`, `photo_urls`, `business_type`, `business_id`, `business_name`, `purpose`, `remark`, `device_info`, `ip_address`, `network_type`, `battery_level`, `is_abnormal`, `create_time`)
VALUES
('CKTEST001', 1, '系统管理员', 1, '2026-04-28 09:00:00', 39.9042, 116.4074, '北京市朝阳区建国路88号SOHO现代城', '北京市', '北京市', '朝阳区', '["https://example.com/checkin/photo1.jpg"]', 'customer', 1, '北京科技有限公司', '客户拜访', '准时到达客户公司', '{"device":"iPhone 15","os":"iOS 17.0"}', '192.168.1.100', '4g', 85, 0, '2026-04-28 09:00:00'),
('CKTEST002', 1, '系统管理员', 2, '2026-04-28 11:30:00', 39.9042, 116.4074, '北京市朝阳区建国路88号SOHO现代城', '北京市', '北京市', '朝阳区', '["https://example.com/checkin/photo2.jpg"]', 'customer', 1, '北京科技有限公司', '客户拜访结束', '拜访结束，客户对产品很感兴趣', '{"device":"iPhone 15","os":"iOS 17.0"}', '192.168.1.100', 'wifi', 78, 0, '2026-04-28 11:30:00'),
('CKTEST003', 1, '系统管理员', 1, '2026-04-27 14:00:00', 31.2304, 121.4737, '上海市浦东新区陆家嘴环路1000号', '上海市', '上海市', '浦东新区', '["https://example.com/checkin/photo3.jpg"]', 'customer', 2, '上海贸易集团', '商务洽谈', '与客户洽谈价格事宜', '{"device":"iPhone 15","os":"iOS 17.0"}', '192.168.2.50', '5g', 90, 0, '2026-04-27 14:00:00'),
('CKTEST004', 1, '系统管理员', 1, '2026-04-26 10:00:00', 23.1291, 113.2644, '广州市天河区珠江新城华夏路8号', '广东省', '广州市', '天河区', '["https://example.com/checkin/photo4.jpg", "https://example.com/checkin/photo5.jpg"]', 'customer', 3, '广州制造企业', '现场考察', '参观客户生产车间', '{"device":"Android 13","os":"Android 13.0"}', '192.168.3.20', 'wifi', 95, 0, '2026-04-26 10:00:00'),
('CKTEST005', 1, '系统管理员', 2, '2026-04-26 16:00:00', 23.1291, 113.2644, '广州市天河区珠江新城华夏路8号', '广东省', '广州市', '天河区', '["https://example.com/checkin/photo6.jpg"]', 'customer', 3, '广州制造企业', '现场考察结束', '考察结束，收集了详细需求', '{"device":"Android 13","os":"Android 13.0"}', '192.168.3.20', '4g', 60, 0, '2026-04-26 16:00:00'),
('CKTEST006', 1, '系统管理员', 1, '2026-04-25 09:30:00', 22.5431, 114.0579, '深圳市南山区科技园南区', '广东省', '深圳市', '南山区', '["https://example.com/checkin/photo7.jpg"]', 'customer', 4, '深圳电子科技', '合同签署', '准备与客户签署合同', '{"device":"iPhone 15","os":"iOS 17.0"}', '192.168.4.10', '5g', 88, 0, '2026-04-25 09:30:00'),
('CKTEST007', 1, '系统管理员', 1, '2026-04-24 14:00:00', 30.2741, 120.1551, '杭州市西湖区文三路478号', '浙江省', '杭州市', '西湖区', '["https://example.com/checkin/photo8.jpg"]', 'customer', 5, '杭州互联网公司', '项目推进会', '与客户讨论项目进度', '{"device":"Android 13","os":"Android 13.0"}', '192.168.5.30', 'wifi', 92, 0, '2026-04-24 14:00:00'),
('CKTEST008', 1, '系统管理员', 2, '2026-04-24 17:00:00', 30.2741, 120.1551, '杭州市西湖区文三路478号', '浙江省', '杭州市', '西湖区', NULL, 'customer', 5, '杭州互联网公司', '项目推进会结束', '会议顺利结束，确定了下一步计划', '{"device":"Android 13","os":"Android 13.0"}', '192.168.5.30', '4g', 75, 0, '2026-04-24 17:00:00'),
('CKTEST009', 1, '系统管理员', 1, '2026-04-28 14:00:00', 30.5728, 104.0668, '成都市高新区天府大道中段1700号', '四川省', '成都市', '高新区', '["https://example.com/checkin/photo9.jpg"]', 'customer', 6, '成都软件开发', '需求沟通', '与客户沟通UI设计需求', '{"device":"iPhone 15","os":"iOS 17.0"}', '192.168.6.15', 'wifi', 80, 0, '2026-04-28 14:00:00'),
('CKTEST010', 1, '系统管理员', 1, '2026-04-23 10:00:00', 30.5928, 114.3055, '武汉市东湖高新区光谷大道77号', '湖北省', '武汉市', '东湖高新区', '["https://example.com/checkin/photo10.jpg"]', 'customer', 7, '武汉制造企业', '需求调研', '现场调研客户需求', '{"device":"Android 13","os":"Android 13.0"}', '192.168.7.25', '4g', 85, 0, '2026-04-23 10:00:00'),
('CKTEST011', 1, '系统管理员', 2, '2026-04-23 15:30:00', 30.5928, 114.3055, '武汉市东湖高新区光谷大道77号', '湖北省', '武汉市', '东湖高新区', '["https://example.com/checkin/photo11.jpg", "https://example.com/checkin/photo12.jpg"]', 'customer', 7, '武汉制造企业', '需求调研结束', '调研完成，收集了完整需求', '{"device":"Android 13","os":"Android 13.0"}', '192.168.7.25', 'wifi', 55, 0, '2026-04-23 15:30:00'),
('CKTEST012', 1, '系统管理员', 1, '2026-04-28 10:30:00', 32.0603, 118.7969, '南京市建邺区河西大街198号', '江苏省', '南京市', '建邺区', '["https://example.com/checkin/photo13.jpg"]', 'customer', 8, '南京科技公司', '技术交流', '与客户技术团队交流', '{"device":"iPhone 15","os":"iOS 17.0"}', '192.168.8.40', 'wifi', 90, 0, '2026-04-28 10:30:00'),
('CKTEST013', 1, '系统管理员', 1, '2026-04-22 09:00:00', 29.4316, 106.9123, '重庆市渝北区金开大道999号', '重庆市', '重庆市', '渝北区', '["https://example.com/checkin/photo14.jpg"]', 'customer', 9, '重庆工业集团', '商务洽谈', '洽谈付款事宜', '{"device":"Android 13","os":"Android 13.0"}', '192.168.9.50', '4g', 85, 0, '2026-04-22 09:00:00'),
('CKTEST014', 1, '系统管理员', 1, '2026-04-28 08:30:00', 39.0842, 117.2009, '天津市和平区南京路189号', '天津市', '天津市', '和平区', '["https://example.com/checkin/photo15.jpg"]', 'customer', 10, '天津贸易公司', '发货确认', '确认发货时间和地址', '{"device":"iPhone 15","os":"iOS 17.0"}', '192.168.10.60', 'wifi', 95, 0, '2026-04-28 08:30:00'),
('CKTEST015', 1, '系统管理员', 1, '2026-04-28 15:00:00', 39.9042, 116.4074, '北京市朝阳区建国路88号SOHO现代城', '北京市', '北京市', '朝阳区', '["https://example.com/checkin/photo16.jpg"]', 'lead', 1, '线索-北京新客户', '首次拜访', '与潜在客户首次接触', '{"device":"iPhone 15","os":"iOS 17.0"}', '192.168.1.100', '4g', 70, 0, '2026-04-28 15:00:00');

-- 待办提醒测试数据（20条）
INSERT INTO `crm_todo_reminder` 
(`todo_no`, `user_id`, `user_name`, `title`, `content`, `priority`, `status`, `remind_time`, `end_time`, `complete_time`, `complete_remark`, `business_type`, `business_id`, `business_name`, `follow_record_id`, `remind_type`, `remind_count`, `last_remind_time`, `is_recurring`, `recurring_type`, `recurring_config`, `create_time`)
VALUES
('TDTEST001', 1, '系统管理员', '北京科技有限公司现场演示', '准备产品演示PPT，确认演示时间为周三下午2点。', 1, 0, '2026-05-03 14:00:00', '2026-05-03 18:00:00', NULL, NULL, 'customer', 1, '北京科技有限公司', 1, 1, 0, NULL, 0, NULL, NULL, '2026-04-28 10:00:00'),
('TDTEST002', 1, '系统管理员', '跟进上海贸易集团价格优惠', '客户反馈价格偏高，需要申请优惠价格。', 2, 0, '2026-05-03 09:30:00', '2026-05-10 18:00:00', NULL, NULL, 'customer', 2, '上海贸易集团', 2, 1, 0, NULL, 0, NULL, NULL, '2026-04-28 11:30:00'),
('TDTEST003', 1, '系统管理员', '提交广州制造企业解决方案', '根据现场调研结果，准备解决方案文档。', 1, 0, '2026-05-10 10:00:00', '2026-05-12 18:00:00', NULL, NULL, 'customer', 3, '广州制造企业', 3, 1, 0, NULL, 0, NULL, NULL, '2026-04-28 14:30:00'),
('TDTEST004', 1, '系统管理员', '签署深圳电子科技合同', '客户已确认合同细节，安排签署时间。', 1, 0, '2026-04-30 10:00:00', '2026-05-05 18:00:00', NULL, NULL, 'customer', 4, '深圳电子科技', 5, 1, 0, NULL, 0, NULL, NULL, '2026-04-28 09:00:00'),
('TDTEST005', 1, '系统管理员', '杭州互联网公司项目推进会', '与客户讨论项目进度，确定下一步计划。', 2, 0, '2026-05-04 10:00:00', '2026-05-04 18:00:00', NULL, NULL, 'customer', 5, '杭州互联网公司', 6, 1, 0, NULL, 0, NULL, NULL, '2026-04-28 16:45:00'),
('TDTEST006', 1, '系统管理员', '调整成都软件开发UI设计', '根据客户反馈，调整界面设计稿。', 2, 0, '2026-05-06 11:00:00', '2026-05-08 18:00:00', NULL, NULL, 'customer', 6, '成都软件开发', 7, 1, 0, NULL, 0, NULL, NULL, '2026-04-27 10:30:00'),
('TDTEST007', 1, '系统管理员', '提交武汉制造企业定制方案', '根据客户需求，准备定制方案。', 1, 0, '2026-05-08 14:00:00', '2026-05-15 18:00:00', NULL, NULL, 'customer', 7, '武汉制造企业', 8, 1, 0, NULL, 0, NULL, NULL, '2026-04-26 10:00:00'),
('TDTEST008', 1, '系统管理员', '确认南京科技公司技术方案', '与客户确认技术方案细节。', 2, 0, '2026-05-02 15:00:00', '2026-05-05 18:00:00', NULL, NULL, 'customer', 8, '南京科技公司', 9, 1, 0, NULL, 0, NULL, NULL, '2026-04-28 14:00:00'),
('TDTEST009', 1, '系统管理员', '跟进重庆工业集团付款', '客户表示下周安排付款，需要跟进。', 1, 0, '2026-05-04 10:00:00', '2026-05-10 18:00:00', NULL, NULL, 'customer', 9, '重庆工业集团', 10, 1, 0, NULL, 0, NULL, NULL, '2026-04-27 14:45:00'),
('TDTEST010', 1, '系统管理员', '安排天津贸易公司发货', '确认发货时间和地址，安排物流。', 1, 0, '2026-04-30 09:00:00', '2026-05-02 18:00:00', NULL, NULL, 'customer', 10, '天津贸易公司', 11, 1, 0, NULL, 0, NULL, NULL, '2026-04-28 08:45:00'),
('TDTEST011', 1, '系统管理员', '详细沟通北京新客户', '首次联系后，需要详细沟通产品功能。', 2, 0, '2026-05-04 10:00:00', '2026-05-06 18:00:00', NULL, NULL, 'lead', 1, '线索-北京新客户', 12, 1, 0, NULL, 0, NULL, NULL, '2026-04-28 09:30:00'),
('TDTEST012', 1, '系统管理员', '跟进上海潜在客户反馈', '发送公司介绍后，等待客户反馈。', 2, 0, '2026-05-03 14:00:00', '2026-05-05 18:00:00', NULL, NULL, 'lead', 2, '线索-上海潜在客户', 13, 1, 0, NULL, 0, NULL, NULL, '2026-04-27 16:30:00'),
('TDTEST013', 1, '系统管理员', '提交广州意向客户需求方案', '现场拜访后，根据需求准备方案。', 1, 0, '2026-05-07 10:00:00', '2026-05-09 18:00:00', NULL, NULL, 'lead', 3, '线索-广州意向客户', 14, 1, 0, NULL, 0, NULL, NULL, '2026-04-26 12:00:00'),
('TDTEST014', 1, '系统管理员', '提交北京项目报价单', '确认预算范围后，准备正式报价单。', 1, 0, '2026-05-02 15:00:00', '2026-05-04 18:00:00', NULL, NULL, 'opportunity', 1, '商机-北京项目', 15, 1, 0, NULL, 0, NULL, NULL, '2026-04-28 11:15:00'),
('TDTEST015', 1, '系统管理员', '跟进上海大项目演示反馈', '现场演示后，需要跟进客户反馈。', 1, 0, '2026-05-06 10:00:00', '2026-05-08 18:00:00', NULL, NULL, 'opportunity', 2, '商机-上海大项目', 16, 1, 0, NULL, 0, NULL, NULL, '2026-04-27 10:00:00'),
('TDTEST016', 1, '系统管理员', '周工作总结', '总结本周工作，安排下周计划。', 3, 0, '2026-05-03 17:00:00', '2026-05-03 18:00:00', NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, NULL, 1, 'weekly', '{"weekday":5}', '2026-04-28 08:00:00'),
('TDTEST017', 1, '系统管理员', '每日客户跟进提醒', '跟进所有待跟进客户。', 2, 0, '2026-04-29 09:00:00', '2026-04-29 18:00:00', NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, NULL, 1, 'daily', '{}', '2026-04-28 08:30:00'),
('TDTEST018', 1, '系统管理员', '月度销售会议', '参加月度销售总结会议。', 2, 1, '2026-04-25 14:00:00', '2026-04-25 16:00:00', '2026-04-25 16:30:00', '会议顺利完成，确定了下月目标。', NULL, NULL, NULL, NULL, 1, 0, '2026-04-25 16:30:00', 0, NULL, NULL, '2026-04-20 09:00:00'),
('TDTEST019', 1, '系统管理员', '客户回访', '对已签约客户进行回访。', 2, 1, '2026-04-24 10:00:00', '2026-04-24 12:00:00', '2026-04-24 11:30:00', '客户对服务满意，无问题。', NULL, NULL, NULL, NULL, 1, 1, '2026-04-24 10:00:00', 0, NULL, NULL, '2026-04-22 09:00:00'),
('TDTEST020', 1, '系统管理员', '取消的待办任务示例', '这是一个已取消的待办任务。', 3, 2, '2026-04-23 15:00:00', '2026-04-25 18:00:00', NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, NULL, 0, NULL, NULL, '2026-04-20 14:00:00');
