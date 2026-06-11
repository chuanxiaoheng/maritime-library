# 前后端综合项目 数据库设计
# 删除数据库
DROP DATABASE IF EXISTS `maritime_db`;

# 创建数据库
CREATE DATABASE IF NOT EXISTS `maritime_db`;

# 使用（切换）数据库
USE `maritime_db`;

-- 用户主表
DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
	`id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
	`username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
	`nickname` VARCHAR(50) COMMENT '昵称',
	`password` VARCHAR(100) NOT NULL COMMENT '加密密码',
	`phone` CHAR(11) UNIQUE COMMENT '手机号',
	`id_card` VARBINARY(255) COMMENT '身份证号，加密存储',
	`email` VARCHAR(50) COMMENT '邮箱',
	`avatar` VARCHAR(255) COMMENT '头像图片URL',
	`sex` TINYINT DEFAULT 0 COMMENT '性别：0-女，1-男，2-未知',
	`birthday` DATE COMMENT '生日',
	`real_name` VARCHAR(50) COMMENT '真实姓名',
	`address` VARCHAR(255) COMMENT '联系地址',
	`intro` VARCHAR(200) COMMENT '个人简介',
	`role_id` INT COMMENT '角色编号，关联角色表',
	`dept_id` INT comment '部门编号，关联部门表',
	`salt` VARCHAR(100) NOT NULL COMMENT '加密盐',
	`vip` TINYINT DEFAULT 0 COMMENT '会员：0-否，1-是',
	`status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-正常，1-禁用，2-锁定，3-注销',
	`last_login_time` DATETIME COMMENT '最后登录时间',
	`last_login_ip` VARCHAR(50) COMMENT '最后登录IP',
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = INNODB DEFAULT CHARSET=utf8mb4 COMMENT '用户主表';

-- 用户资料表
DROP TABLE IF EXISTS `user_profiles`;
CREATE TABLE IF NOT EXISTS `user_profiles` (
	`id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
	`user_id` BIGINT NOT NULL UNIQUE COMMENT '用户编号',
	`education` VARCHAR(20) COMMENT '学历',
	`occupation` VARCHAR(20) COMMENT '职业',
	`hobby` VARCHAR(100) COMMENT '兴趣爱好',
	`personal_sign` VARCHAR(100) COMMENT '个人签名',
	`credit_score` INT UNSIGNED DEFAULT 100 COMMENT '信用分',
	`receive_email` TINYINT DEFAULT 1 COMMENT '是否接受邮件，1-接收，0-拒绝',
	`receive_due` TINYINT DEFAULT 1 COMMENT '是否接受到期提醒，1-接收，0-拒绝',
	`receive_sms` TINYINT DEFAULT 1 COMMENT '是否接受短信，1-接收，0-拒绝',
	`receive_notice` TINYINT DEFAULT 1 COMMENT '是否接受公告，1-接收，0-拒绝',
	`profile_visible` TINYINT DEFAULT 1 COMMENT '个人信息是否可见，1-可见，0-隐藏',
	`borrow_his_visible` TINYINT DEFAULT 1 COMMENT '借阅历史是否可见，1-可见，0-隐藏',
	INDEX `idx_user_id_profile` (`user_id`)
) ENGINE = INNODB DEFAULT CHARSET=utf8mb4 COMMENT '用户资料表';

# 触发器：基于用户主表，当新增用户时，自动将该用户在资料表添加对应记录
DROP TRIGGER IF EXISTS `insert_user_profile_trigger`;
CREATE TRIGGER `insert_user_profile_trigger` AFTER INSERT ON `users` FOR EACH ROW
BEGIN
	-- 语法：NEW|OLD.表中列名，NEW或者OLD，代表触发该触发器操作的那条数据
	INSERT INTO `user_profiles`(`user_id`) values (NEW.id);
END
;

-- 角色表
DROP TABLE IF EXISTS `roles`;
CREATE TABLE IF NOT EXISTS `roles` (
	`id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
	`role_code` VARCHAR(20) NOT NULL UNIQUE COMMENT '角色编码',
	`role_name` VARCHAR(20) NOT NULL COMMENT '角色名称',
	`role_sort` TINYINT DEFAULT 0 COMMENT '排序字段',
	`status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
	`remark` VARCHAR(255) COMMENT '角色备注',
	`del_flag` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-正常，1-已删除',
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = INNODB DEFAULT CHARSET=utf8mb4 COMMENT '角色表';

-- 初始化角色数据
INSERT INTO `roles` VALUES (1, 'admin', '系统管理员', 0, 1, '所有权限', 0, '2026-05-12 11:11:11', '2026-05-12 11:11:11');
INSERT INTO `roles` VALUES (2, 'librarian', '图书管理员', 0, 1, '图书管理权限', 0, '2026-05-12 11:11:11', '2026-05-12 11:11:11');
INSERT INTO `roles` VALUES (3, 'reader', '普通读者', 0, 1, '读者权限', 0, '2026-05-12 11:11:11', '2026-05-12 11:11:11');

-- 用户登录记录表
DROP TABLE IF EXISTS `user_login_logs`;
CREATE TABLE IF NOT EXISTS `user_login_logs` (
	`id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
	`user_id` BIGINT NOT NULL COMMENT '用户编号',
	`username` VARCHAR(50) COMMENT '用户名',	
	`login_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
	`login_ip` VARCHAR(50) COMMENT '登录IP',
	`login_location` VARCHAR(50) COMMENT '登录地址位置',
	`device_info` VARCHAR(200) COMMENT '登录设备信息',
	`browser` VARCHAR(20) COMMENT '浏览器类型',
	`os` VARCHAR(50) COMMENT '操作系统',
	`login_type` TINYINT UNSIGNED DEFAULT 0 COMMENT '登录方式：0-密码，1-短信，2-第三方',
	`status` TINYINT DEFAULT 1 COMMENT '状态：1-成功，0-失败',
	INDEX `idx_user_id_logs` (`user_id`),
	INDEX `idx_login_time_logs` (`login_time`)
) ENGINE = INNODB DEFAULT CHARSET=utf8mb4 COMMENT '用户登录记录表';

-- 图书表
DROP TABLE IF EXISTS `books`;
CREATE TABLE `books`  (
  `id` BIGINT AUTO_INCREMENT COMMENT '主键ID',
  `isbn` VARCHAR(20) NOT NULL UNIQUE COMMENT '图书ISBN编号',
  `title` VARCHAR(100) NOT NULL COMMENT '图书名称',
  `author` VARCHAR(50) NOT NULL COMMENT '作者',
  `publisher` VARCHAR(100)  NULL DEFAULT NULL COMMENT '出版社',
  `publish_date` DATE COMMENT '出版日期',
  `category_id` BIGINT NOT NULL COMMENT '分类ID',
  `location` VARCHAR(100) COMMENT '存放位置',
  `description` TEXT NULL COMMENT '图书描述',
  `price` DECIMAL(10, 2) COMMENT '价格',
  `total_copies` INT DEFAULT 0 COMMENT '总副本数量',
  `available_copies` INT DEFAULT 0 COMMENT '可借副本数量',
  `borrowed_copies` INT DEFAULT 0 COMMENT '已借副本数量',
  `damaged_copies` INT DEFAULT 0 COMMENT '损坏副本数量',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0-下架，1-上架',
  `cover` VARCHAR(255) COMMENT '图书封面',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_title`(`title`),
  INDEX `idx_author`(`author`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '图书表';

-- https://placehold.co，在线占位图生成工具，可通过 URL 快速生成指定尺寸、格式、颜色和文本的占位图片，广泛用于网页设计与开发中的原型制作和界面测试。
INSERT INTO `books` VALUES (1, '9787766147857', '红楼梦', '曹雪芹', '人民文学出版社', '2008-01-01', 1, 'A区1排1架', '中国古典四大名著之一，讲述贾宝玉与林黛玉的爱情故事', 88.00, 5, 5, 0, 0, 1, 'https://placehold.co/300x400/8B4513/FFF?text=红楼梦', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (2, '9787136268928', '西游记', '吴承恩', '人民文学出版社', '2008-01-01', 1, 'A区1排2架', '中国古典四大名著之一，讲述唐僧师徒西天取经的故事', 75.00, 4, 4, 0, 0, 1, 'https://placehold.co/300x400/FFD700/000?text=西游记', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (3, '9787310302780', '三国演义', '罗贯中', '人民文学出版社', '2008-01-01', 1, 'A区1排3架', '中国古典四大名著之一，讲述三国时期的英雄故事', 68.00, 6, 6, 0, 0, 1, 'https://placehold.co/300x400/8B0000/FFF?text=三国演义', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (4, '9787643900855', '水浒传', '施耐庵', '人民文学出版社', '2008-01-01', 1, 'A区1排4架', '中国古典四大名著之一，讲述梁山好汉的故事', 65.00, 3, 3, 0, 0, 1, 'https://placehold.co/300x400/2F4F4F/FFF?text=水浒传', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (5, '9787936416636', '围城', '钱钟书', '人民文学出版社', '2010-05-01', 1, 'A区2排1架', '现代文学经典，讲述知识分子的生活与命运', 42.00, 8, 8, 0, 0, 1, 'https://placehold.co/300x400/708090/FFF?text=围城', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (6, '9787434006094', '活着', '余华', '作家出版社', '2012-03-01', 1, 'A区2排2架', '讲述一个人一生的故事，反映时代变迁', 35.00, 10, 10, 0, 0, 1, 'https://placehold.co/300x400/556B2F/FFF?text=活着', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (7, '9787174917492', '百年孤独', '加西亚·马尔克斯', '南海出版公司', '2011-06-01', 1, 'A区2排3架', '魔幻现实主义文学代表作', 55.00, 7, 7, 0, 0, 1, 'https://placehold.co/300x400/FF8C00/FFF?text=百年孤独', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (8, '9787716308313', '追风筝的人', '卡勒德·胡赛尼', '上海人民出版社', '2006-05-01', 1, 'A区2排4架', '关于友谊、背叛与救赎的故事', 38.00, 5, 5, 0, 0, 1, 'https://placehold.co/300x400/4682B4/FFF?text=追风筝的人', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (9, '9787647593357', '解忧杂货店', '东野圭吾', '南海出版公司', '2014-08-01', 1, 'A区3排1架', '日本推理小说，讲述一家神奇的杂货店', 45.00, 6, 6, 0, 0, 1, 'https://placehold.co/300x400/9370DB/FFF?text=解忧杂货店', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (10, '9787745474541', '白夜行', '东野圭吾', '南海出版公司', '2008-09-01', 1, 'A区3排2架', '日本推理小说经典之作', 48.00, 4, 4, 0, 0, 1, 'https://placehold.co/300x400/2F4F4F/FFF?text=白夜行', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (11, '9787603595872', '嫌疑人X的献身', '东野圭吾', '南海出版公司', '2008-09-01', 1, 'A区3排3架', '日本推理小说，逻辑严密', 39.00, 5, 5, 0, 0, 1, 'https://placehold.co/300x400/191970/FFF?text=嫌疑人X', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (12, '9787691493179', '三体', '刘慈欣', '重庆出版社', '2008-01-01', 2, 'B区1排1架', '中国科幻小说代表作', 68.00, 8, 8, 0, 0, 1, 'https://placehold.co/300x400/000080/FFF?text=三体', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (13, '9787111378246', '三体Ⅱ：黑暗森林', '刘慈欣', '重庆出版社', '2008-01-01', 2, 'B区1排2架', '三体系列第二部', 68.00, 6, 6, 0, 0, 1, 'https://placehold.co/300x400/1C1C1C/FFF?text=黑暗森林', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (14, '9787326767035', '三体Ⅲ：死神永生', '刘慈欣', '重庆出版社', '2010-11-01', 2, 'B区1排3架', '三体系列第三部', 78.00, 5, 5, 0, 0, 1, 'https://placehold.co/300x400/4B0082/FFF?text=死神永生', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (15, '9787565967476', '流浪地球', '刘慈欣', '四川科技出版社', '2018-04-01', 2, 'B区1排4架', '科幻小说，讲述地球流浪的故事', 32.00, 7, 7, 0, 0, 1, 'https://placehold.co/300x400/FF4500/FFF?text=流浪地球', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (16, '9787467212053', '人类简史', '尤瓦尔·赫拉利', '中信出版社', '2014-11-01', 2, 'B区2排1架', '从认知革命到人工智能，讲述人类发展史', 68.00, 10, 10, 0, 0, 1, 'https://placehold.co/300x400/8B4513/FFF?text=人类简史', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (17, '9787432500392', '未来简史', '尤瓦尔·赫拉利', '中信出版社', '2017-02-01', 2, 'B区2排2架', '人类简史续作，探讨未来发展方向', 68.00, 8, 8, 0, 0, 1, 'https://placehold.co/300x400/006400/FFF?text=未来简史', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (18, '9787348029917', '今日简史', '尤瓦尔·赫拉利', '中信出版社', '2018-08-01', 2, 'B区2排3架', '人类简史系列第三部', 58.00, 6, 6, 0, 0, 1, 'https://placehold.co/300x400/B8860B/FFF?text=今日简史', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (19, '9787628648153', '时间简史', '史蒂芬·霍金', '湖南科技出版社', '2002-02-01', 2, 'B区2排4架', '关于宇宙起源和发展的科普读物', 45.00, 5, 5, 0, 0, 1, 'https://placehold.co/300x400/000033/FFF?text=时间简史', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (20, '9787568154503', '明朝那些事儿', '当年明月', '中国海关出版社', '2006-03-01', 3, 'C区1排1架', '以幽默风趣的方式讲述明朝历史', 88.00, 15, 15, 0, 0, 1, 'https://placehold.co/300x400/DC143C/FFF?text=明朝那些事儿', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (21, '9787109793850', '万历十五年', '黄仁宇', '中华书局', '2007-01-01', 3, 'C区1排2架', '以小见大，分析明朝政治', 32.00, 4, 4, 0, 0, 1, 'https://placehold.co/300x400/8B0000/FFF?text=万历十五年', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (22, '9787674089067', '中国通史', '吕思勉', '华东师范大学出版社', '2011-01-01', 3, 'C区1排3架', '中国历史通识读物', 98.00, 10, 10, 0, 0, 1, 'https://placehold.co/300x400/A52A2A/FFF?text=中国通史', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (23, '9787176131042', '全球通史', '斯塔夫里阿诺斯', '北京大学出版社', '2006-10-01', 3, 'C区1排4架', '世界历史通识读物', 128.00, 8, 8, 0, 0, 1, 'https://placehold.co/300x400/4682B4/FFF?text=全球通史', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (24, '9787787649006', '史记', '司马迁', '中华书局', '2013-01-01', 3, 'C区2排1架', '中国第一部纪传体通史', 68.00, 12, 12, 0, 0, 1, 'https://placehold.co/300x400/800000/FFF?text=史记', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (25, '9787460472551', '资治通鉴', '司马光', '中华书局', '2013-01-01', 3, 'C区2排2架', '中国第一部编年体通史', 98.00, 8, 8, 0, 0, 1, 'https://placehold.co/300x400/4B0082/FFF?text=资治通鉴', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (26, '9787393942833', '苏菲的世界', '乔斯坦·贾德', '作家出版社', '2007-10-01', 4, 'D区1排1架', '哲学入门读物，以小说形式讲述哲学史', 48.00, 10, 10, 0, 0, 1, 'https://placehold.co/300x400/DDA0DD/000?text=苏菲的世界', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (27, '9787486334287', '理想国', '柏拉图', '商务印书馆', '2002-01-01', 4, 'D区1排2架', '西方哲学经典著作', 35.00, 6, 6, 0, 0, 1, 'https://placehold.co/300x400/F5F5DC/000?text=理想国', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (28, '9787971313827', '尼各马可伦理学', '亚里士多德', '商务印书馆', '2003-01-01', 4, 'D区1排3架', '古希腊哲学经典', 32.00, 4, 4, 0, 0, 1, 'https://placehold.co/300x400/DAA520/FFF?text=尼各马可伦理学', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (29, '9787353090581', '存在与时间', '海德格尔', '商务印书馆', '2012-01-01', 4, 'D区1排4架', '现代哲学经典著作', 88.00, 5, 5, 0, 0, 1, 'https://placehold.co/300x400/2F4F4F/FFF?text=存在与时间', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (30, '9787825648606', '纯粹理性批判', '康德', '商务印书馆', '2004-01-01', 4, 'D区2排1架', '德国古典哲学经典', 58.00, 4, 4, 0, 0, 1, 'https://placehold.co/300x400/191970/FFF?text=纯粹理性批判', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (31, '9787324051002', '经济学原理', '曼昆', '北京大学出版社', '2015-09-01', 5, 'E区1排1架', '经济学入门教材', 78.00, 12, 12, 0, 0, 1, 'https://placehold.co/300x400/228B22/FFF?text=经济学原理', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (32, '9787788177452', '国富论', '亚当·斯密', '商务印书馆', '2004-01-01', 5, 'E区1排2架', '经济学经典著作', 45.00, 6, 6, 0, 0, 1, 'https://placehold.co/300x400/FFD700/000?text=国富论', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (33, '9787199471461', '资本论', '马克思', '人民出版社', '2004-01-01', 5, 'E区1排3架', '马克思主义经典著作', 68.00, 8, 8, 0, 0, 1, 'https://placehold.co/300x400/8B0000/FFF?text=资本论', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (34, '9787404496196', '金融学', '博迪', '中国人民大学出版社', '2010-01-01', 5, 'E区1排4架', '金融学入门教材', 65.00, 10, 10, 0, 0, 1, 'https://placehold.co/300x400/008B8B/FFF?text=金融学', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (35, '9787425402328', '投资学', '博迪', '机械工业出版社', '2012-01-01', 5, 'E区2排1架', '投资学经典教材', 88.00, 7, 7, 0, 0, 1, 'https://placehold.co/300x400/6B8E23/FFF?text=投资学', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (36, '9787882163939', '艺术的故事', '贡布里希', '广西美术出版社', '2008-04-01', 6, 'F区1排1架', '艺术史入门读物', 128.00, 8, 8, 0, 0, 1, 'https://placehold.co/300x400/FF69B4/FFF?text=艺术的故事', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (37, '9787784615173', '艺术哲学', '丹托', '中国人民大学出版社', '2010-01-01', 6, 'F区1排2架', '艺术哲学入门', 58.00, 5, 5, 0, 0, 1, 'https://placehold.co/300x400/DA70D6/FFF?text=艺术哲学', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (38, '9787716321606', '美的历程', '李泽厚', '广西师范大学出版社', '2001-01-01', 6, 'F区1排3架', '中国美学经典著作', 48.00, 6, 6, 0, 0, 1, 'https://placehold.co/300x400/FFB6C1/000?text=美的历程', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (39, '9787391773247', '西方美术史', '丹纳', '人民美术出版社', '2004-01-01', 6, 'F区1排4架', '西方美术史教材', 68.00, 4, 4, 0, 0, 1, 'https://placehold.co/300x400/BC8F8F/FFF?text=西方美术史', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (40, '9787806510189', '中国美术史', '洪再新', '高等教育出版社', '2010-01-01', 6, 'F区2排1架', '中国美术史教材', 75.00, 5, 5, 0, 0, 1, 'https://placehold.co/300x400/CD853F/FFF?text=中国美术史', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (41, '9787263943080', '教育学', '王道俊', '人民教育出版社', '2016-01-01', 7, 'G区1排1架', '教育学入门教材', 45.00, 10, 10, 0, 0, 1, 'https://placehold.co/300x400/3CB371/FFF?text=教育学', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (42, '9787769717126', '教育心理学', '皮连生', '人民教育出版社', '2011-01-01', 7, 'G区1排2架', '教育心理学教材', 55.00, 8, 8, 0, 0, 1, 'https://placehold.co/300x400/20B2AA/FFF?text=教育心理学', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (43, '9787314874286', '课程与教学论', '王本陆', '高等教育出版社', '2009-01-01', 7, 'G区1排3架', '课程论教材', 42.00, 6, 6, 0, 0, 1, 'https://placehold.co/300x400/66CDAA/000?text=课程与教学论', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (44, '9787345580865', '教学设计', '加涅', '华东师范大学出版社', '2007-01-01', 7, 'G区1排4架', '教学设计经典著作', 38.00, 5, 5, 0, 0, 1, 'https://placehold.co/300x400/8FBC8F/000?text=教学设计', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (45, '9787957273478', '学习论', '桑新民', '人民教育出版社', '2005-01-01', 7, 'G区2排1架', '学习理论著作', 35.00, 4, 4, 0, 0, 1, 'https://placehold.co/300x400/90EE90/000?text=学习论', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (46, '9787781923685', '内科学', '葛均波', '人民卫生出版社', '2018-01-01', 8, 'H区1排1架', '医学内科学教材', 168.00, 6, 6, 0, 0, 1, 'https://placehold.co/300x400/008080/FFF?text=内科学', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (47, '9787374898641', '外科学', '陈孝平', '人民卫生出版社', '2018-01-01', 8, 'H区1排2架', '医学外科学教材', 158.00, 5, 5, 0, 0, 1, 'https://placehold.co/300x400/00CED1/FFF?text=外科学', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (48, '9787142055112', '诊断学', '万学红', '人民卫生出版社', '2018-01-01', 8, 'H区1排3架', '医学诊断学教材', 98.00, 8, 8, 0, 0, 1, 'https://placehold.co/300x400/48D1CC/000?text=诊断学', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (49, '9787275462547', '药理学', '杨宝峰', '人民卫生出版社', '2018-01-01', 8, 'H区1排4架', '医学药理学教材', 78.00, 7, 7, 0, 0, 1, 'https://placehold.co/300x400/40E0D0/000?text=药理学', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (50, '9787761608277', '病理学', '李玉林', '人民卫生出版社', '2018-01-01', 8, 'H区2排1架', '医学病理学教材', 68.00, 6, 6, 0, 0, 1, 'https://placehold.co/300x400/7FFFD4/000?text=病理学', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (51, '9787984814151', '民法学', '王利明', '中国人民大学出版社', '2015-01-01', 9, 'I区1排1架', '法学入门教材', 68.00, 10, 10, 0, 0, 1, 'https://placehold.co/300x400/4169E1/FFF?text=民法学', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (52, '9787550320859', '刑法学', '高铭暄', '北京大学出版社', '2016-01-01', 9, 'I区1排2架', '刑法学教材', 58.00, 8, 8, 0, 0, 1, 'https://placehold.co/300x400/0000CD/FFF?text=刑法学', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (53, '9787348234530', '宪法学', '周叶中', '中国人民大学出版社', '2015-01-01', 9, 'I区1排3架', '宪法学教材', 48.00, 6, 6, 0, 0, 1, 'https://placehold.co/300x400/6495ED/FFF?text=宪法学', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (54, '9787898903997', '行政法学', '姜明安', '北京大学出版社', '2016-01-01', 9, 'I区1排4架', '行政法学教材', 45.00, 5, 5, 0, 0, 1, 'https://placehold.co/300x400/87CEEB/000?text=行政法学', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (55, '9787373312049', '管理学原理', '周三多', '高等教育出版社', '2014-01-01', 10, 'J区1排1架', '管理学入门教材', 45.00, 12, 12, 0, 0, 1, 'https://placehold.co/300x400/6A5ACD/FFF?text=管理学原理', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (56, '9787476938787', '组织行为学', '罗宾斯', '中国人民大学出版社', '2016-01-01', 10, 'J区1排2架', '组织行为学教材', 58.00, 8, 8, 0, 0, 1, 'https://placehold.co/300x400/9370DB/FFF?text=组织行为学', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (57, '9787109755367', '人力资源管理', '德斯勒', '中国人民大学出版社', '2015-01-01', 10, 'J区1排3架', '人力资源管理教材', 55.00, 7, 7, 0, 0, 1, 'https://placehold.co/300x400/BA55D3/FFF?text=人力资源管理', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (58, '9787660229248', '市场营销学', '科特勒', '中国人民大学出版社', '2014-01-01', 10, 'J区1排4架', '市场营销学教材', 68.00, 9, 9, 0, 0, 1, 'https://placehold.co/300x400/DA70D6/FFF?text=市场营销学', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (59, '9787131041055', '战略管理', '希特', '机械工业出版社', '2015-01-01', 10, 'J区2排1架', '战略管理教材', 52.00, 6, 6, 0, 0, 1, 'https://placehold.co/300x400/EE82EE/000?text=战略管理', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (60, '9787831304129', '平凡的世界', '路遥', '北京十月文艺出版社', '2013-05-01', 1, 'A区3排4架', '茅盾文学奖作品，讲述普通人的奋斗故事', 88.00, 10, 10, 0, 0, 1, 'https://placehold.co/300x400/D2691E/FFF?text=平凡的世界', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (61, '9787695656609', '白鹿原', '陈忠实', '人民文学出版社', '2012-09-01', 1, 'A区4排1架', '茅盾文学奖作品，讲述关中平原的故事', 65.00, 8, 8, 0, 0, 1, 'https://placehold.co/300x400/F4A460/FFF?text=白鹿原', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (62, '9787925436875', '长恨歌', '王安忆', '人民文学出版社', '2011-01-01', 1, 'A区4排2架', '茅盾文学奖作品，讲述上海的故事', 48.00, 6, 6, 0, 0, 1, 'https://placehold.co/300x400/DEB887/000?text=长恨歌', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (63, '9787527726928', '繁花', '金宇澄', '上海文艺出版社', '2013-03-01', 1, 'A区4排3架', '茅盾文学奖作品，讲述上海的故事', 58.00, 7, 7, 0, 0, 1, 'https://placehold.co/300x400/D2B48C/000?text=繁花', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (64, '9787379989129', '许三观卖血记', '余华', '作家出版社', '2012-08-01', 1, 'A区4排4架', '讲述一个普通人的故事', 35.00, 5, 5, 0, 0, 1, 'https://placehold.co/300x400/BC8F8F/FFF?text=许三观卖血记', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (65, '9787526175293', '兄弟', '余华', '作家出版社', '2012-08-01', 1, 'A区5排1架', '讲述两兄弟的故事', 45.00, 6, 6, 0, 0, 1, 'https://placehold.co/300x400/F0E68C/000?text=兄弟', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (66, '9787965547219', '在细雨中呼喊', '余华', '作家出版社', '2012-08-01', 1, 'A区5排2架', '余华早期作品', 38.00, 4, 4, 0, 0, 1, 'https://placehold.co/300x400/778899/FFF?text=在细雨中呼喊', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (67, '9787815208447', '第七天', '余华', '作家出版社', '2013-06-01', 1, 'A区5排3架', '余华新作', 42.00, 5, 5, 0, 0, 1, 'https://placehold.co/300x400/696969/FFF?text=第七天', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (68, '9787419971401', '文城', '余华', '北京十月文艺出版社', '2021-03-01', 1, 'A区5排4架', '余华最新作品', 55.00, 8, 8, 0, 0, 1, 'https://placehold.co/300x400/A9A9A9/FFF?text=文城', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (69, '9787961899505', '银河帝国：基地', '阿西莫夫', '江苏凤凰文艺出版社', '2015-10-01', 2, 'B区3排1架', '科幻小说经典系列', 48.00, 7, 7, 0, 0, 1, 'https://placehold.co/300x400/1E90FF/FFF?text=银河帝国基地', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (70, '9787825868109', '银河帝国：基地与帝国', '阿西莫夫', '江苏凤凰文艺出版社', '2015-10-01', 2, 'B区3排2架', '科幻小说经典系列', 48.00, 6, 6, 0, 0, 1, 'https://placehold.co/300x400/4169E1/FFF?text=基地与帝国', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (71, '9787298987803', '银河帝国：第二基地', '阿西莫夫', '江苏凤凰文艺出版社', '2015-10-01', 2, 'B区3排3架', '科幻小说经典系列', 48.00, 5, 5, 0, 0, 1, 'https://placehold.co/300x400/6495ED/FFF?text=第二基地', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (72, '9787934249322', '沙丘', '弗兰克·赫伯特', '江苏凤凰文艺出版社', '2017-02-01', 2, 'B区3排4架', '科幻小说经典', 58.00, 8, 8, 0, 0, 1, 'https://placehold.co/300x400/DAA520/FFF?text=沙丘', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (73, '9787916961716', '神经漫游者', '威廉·吉布森', '江苏凤凰文艺出版社', '2013-06-01', 2, 'B区4排1架', '赛博朋克小说经典', 42.00, 6, 6, 0, 0, 1, 'https://placehold.co/300x400/00FF7F/000?text=神经漫游者', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (74, '9787406453012', '雪崩', '尼尔·斯蒂芬森', '四川科学技术出版社', '2018-05-01', 2, 'B区4排2架', '赛博朋克小说经典', 68.00, 5, 5, 0, 0, 1, 'https://placehold.co/300x400/00FA9A/000?text=雪崩', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (75, '9787564433560', '安德的游戏', '奥森·斯科特·卡德', '安徽文艺出版社', '2016-01-01', 2, 'B区4排3架', '科幻小说经典', 45.00, 7, 7, 0, 0, 1, 'https://placehold.co/300x400/98FB98/000?text=安德的游戏', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (76, '9787788087293', '海伯利安', '丹·西蒙斯', '文汇出版社', '2014-11-01', 2, 'B区4排4架', '科幻小说经典系列', 58.00, 6, 6, 0, 0, 1, 'https://placehold.co/300x400/90EE90/000?text=海伯利安', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (77, '9787525039497', '1984', '乔治·奥威尔', '北京十月文艺出版社', '2010-04-01', 1, 'A区6排3架', '反乌托邦小说经典', 32.00, 10, 10, 0, 0, 1, 'https://placehold.co/300x400/2F4F4F/FFF?text=1984', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (78, '9787922296979', '美丽新世界', '阿道斯·赫胥黎', '北京十月文艺出版社', '2010-04-01', 1, 'A区6排4架', '反乌托邦小说经典', 35.00, 8, 8, 0, 0, 1, 'https://placehold.co/300x400/87CEEB/000?text=美丽新世界', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (79, '9787815525807', '我们', '扎米亚京', '上海译文出版社', '2017-01-01', 1, 'A区7排1架', '反乌托邦小说经典', 38.00, 6, 6, 0, 0, 1, 'https://placehold.co/300x400/B0C4DE/000?text=我们', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (80, '9787332594801', '动物农场', '乔治·奥威尔', '北京十月文艺出版社', '2010-04-01', 1, 'A区7排2架', '反乌托邦小说经典', 25.00, 12, 12, 0, 0, 1, 'https://placehold.co/300x400/ADD8E6/000?text=动物农场', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (81, '9787951308440', '了不起的盖茨比', '菲茨杰拉德', '上海译文出版社', '2011-06-01', 1, 'A区7排3架', '美国文学经典', 28.00, 8, 8, 0, 0, 1, 'https://placehold.co/300x400/FFD700/000?text=盖茨比', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (82, '9787700095888', '老人与海', '海明威', '上海译文出版社', '2011-06-01', 1, 'A区7排4架', '诺贝尔文学奖作品', 32.00, 10, 10, 0, 0, 1, 'https://placehold.co/300x400/4682B4/FFF?text=老人与海', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (83, '9787458000604', '麦田里的守望者', '塞林格', '译林出版社', '2010-01-01', 1, 'A区8排1架', '美国文学经典', 28.00, 7, 7, 0, 0, 1, 'https://placehold.co/300x400/9ACD32/FFF?text=麦田守望者', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (84, '9787272732736', '飘', '玛格丽特·米切尔', '译林出版社', '2010-01-01', 1, 'A区8排2架', '美国文学经典', 68.00, 6, 6, 0, 0, 1, 'https://placehold.co/300x400/228B22/FFF?text=飘', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (85, '9787551736652', '简爱', '夏洛蒂·勃朗特', '译林出版社', '2010-01-01', 1, 'A区8排3架', '英国文学经典', 35.00, 8, 8, 0, 0, 1, 'https://placehold.co/300x400/8B008B/FFF?text=简爱', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (86, '9787573279608', '呼啸山庄', '艾米莉·勃朗特', '译林出版社', '2010-01-01', 1, 'A区8排4架', '英国文学经典', 32.00, 6, 6, 0, 0, 1, 'https://placehold.co/300x400/4B0082/FFF?text=呼啸山庄', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (87, '9787221327250', '傲慢与偏见', '简·奥斯汀', '译林出版社', '2010-01-01', 1, 'A区9排1架', '英国文学经典', 38.00, 10, 10, 0, 0, 1, 'https://placehold.co/300x400/DA70D6/FFF?text=傲慢与偏见', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (88, '9787399342918', '理智与情感', '简·奥斯汀', '译林出版社', '2010-01-01', 1, 'A区9排2架', '英国文学经典', 35.00, 7, 7, 0, 0, 1, 'https://placehold.co/300x400/EE82EE/000?text=理智与情感', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (89, '9787753432871', '战争与和平', '托尔斯泰', '译林出版社', '2011-01-01', 1, 'A区9排3架', '俄国文学经典', 128.00, 5, 5, 0, 0, 1, 'https://placehold.co/300x400/800000/FFF?text=战争与和平', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (90, '9787829304145', '安娜·卡列尼娜', '托尔斯泰', '译林出版社', '2011-01-01', 1, 'A区9排4架', '俄国文学经典', 68.00, 6, 6, 0, 0, 1, 'https://placehold.co/300x400/A52A2A/FFF?text=安娜卡列尼娜', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (91, '9787372589073', '罪与罚', '陀思妥耶夫斯基', '译林出版社', '2011-01-01', 1, 'A区10排1架', '俄国文学经典', 48.00, 8, 8, 0, 0, 1, 'https://placehold.co/300x400/2F4F4F/FFF?text=罪与罚', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (92, '9787647457096', '卡拉马佐夫兄弟', '陀思妥耶夫斯基', '译林出版社', '2011-01-01', 1, 'A区10排2架', '俄国文学经典', 88.00, 5, 5, 0, 0, 1, 'https://placehold.co/300x400/696969/FFF?text=卡拉马佐夫', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (93, '9787408209665', '悲惨世界', '雨果', '译林出版社', '2011-01-01', 1, 'A区10排3架', '法国文学经典', 98.00, 7, 7, 0, 0, 1, 'https://placehold.co/300x400/00008B/FFF?text=悲惨世界', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (94, '9787470344077', '巴黎圣母院', '雨果', '译林出版社', '2011-01-01', 1, 'A区10排4架', '法国文学经典', 48.00, 6, 6, 0, 0, 1, 'https://placehold.co/300x400/4169E1/FFF?text=巴黎圣母院', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (95, '9787609754538', '红与黑', '司汤达', '译林出版社', '2011-01-01', 1, 'A区11排1架', '法国文学经典', 45.00, 5, 5, 0, 0, 1, 'https://placehold.co/300x400/8B0000/FFF?text=红与黑', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (96, '9787232055566', '基督山伯爵', '大仲马', '译林出版社', '2011-01-01', 1, 'A区11排2架', '法国文学经典', 68.00, 8, 8, 0, 0, 1, 'https://placehold.co/300x400/FFD700/000?text=基督山伯爵', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (97, '9787768319756', '茶花女', '小仲马', '译林出版社', '2011-01-01', 1, 'A区11排3架', '法国文学经典', 28.00, 6, 6, 0, 0, 1, 'https://placehold.co/300x400/FFB6C1/000?text=茶花女', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (98, '9787544326435', '变形记', '卡夫卡', '译林出版社', '2012-01-01', 1, 'A区11排4架', '现代文学经典', 25.00, 5, 5, 0, 0, 1, 'https://placehold.co/300x400/556B2F/FFF?text=变形记', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (99, '9787716650193', '平凡的世界（全三册）', '路遥', '北京十月文艺出版社', '2013-05-01', 1, 'A区6排1架', '茅盾文学奖作品完整版', 128.00, 5, 5, 0, 0, 1, 'https://placehold.co/300x400/D2691E/FFF?text=平凡的世界', '2026-03-07 01:13:01', '2026-03-07 01:13:01');
INSERT INTO `books` VALUES (100, '9787622582167', '人生', '路遥', '北京十月文艺出版社', '2010-01-01', 1, 'A区6排2架', '路遥中篇小说', 28.00, 6, 6, 0, 0, 1, 'https://placehold.co/300x400/CD853F/FFF?text=人生', '2026-03-07 01:13:01', '2026-03-07 01:13:01');


-- 更新副本数据
update books set total_copies = 1, available_copies = 1, borrowed_copies = 0,
                 damaged_copies = 0;

-- 图书副本表
DROP TABLE IF EXISTS `book_copies`;
CREATE TABLE `book_copies`  (
  `id` BIGINT AUTO_INCREMENT COMMENT '主键ID',
  `book_id` BIGINT NOT NULL COMMENT '关联的图书ID',
  `book_title` VARCHAR(100) NOT NULL COMMENT '关联的图书名称',
  `copy_no` VARCHAR(10) NOT NULL COMMENT '副本编号，如001，002，003',
  `barcode` VARCHAR(50) UNIQUE NOT NULL COMMENT '实体书条形码',
  `location` VARCHAR(100) COMMENT '索书号',
  `status` TINYINT DEFAULT 1 COMMENT '状态：1-可借阅，2-已借出，3-已损坏，4-已遗失',
  `borrow_count` INT DEFAULT 0 COMMENT '被借阅次数',
  `remark` TEXT COMMENT '备注描述',
	`purchase_date` DATE COMMENT '购入日期',
  `last_borrowed_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '最后借出时间',
  `last_returned_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '最后归还时间',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
	INDEX `idx_copy_book_id`(`book_id`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '图书副本表';

-- insert into `book_copies`(`book_id`, `book_title`, `copy_no`, `barcode`) 
-- select `id`, `title`, '001', concat('H', floor(RAND()*90000000 + 10000000)) from `books`


-- 图书分类表
DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories`  (
                               `id` int(20) AUTO_INCREMENT COMMENT '主键ID',
                               `name` varchar(50) NOT NULL COMMENT '分类名称',
                               `color` VARCHAR(10) DEFAULT NULL COMMENT '分类描述',
                               `description` varchar(255) NULL DEFAULT NULL COMMENT '分类描述',
                               `sort_order` int NULL DEFAULT 0 COMMENT '排序',
                               `parent_id` int COMMENT '父分类ID',
                               `code` varchar(10) NOT NULL COMMENT '分类代码',
                               `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET= utf8mb4 COMMENT = '图书分类表';

INSERT INTO `categories`(`id`, `name`, `color`, `description`, `sort_order`, `parent_id`, `code`) VALUES (1, '文学', '#409EFF', '中外文学名著、经典文学作品', 1,0,'C10001');
INSERT INTO `categories`(`id`, `name`, `color`, `description`, `sort_order`, `parent_id`, `code`) VALUES (2, '科技', '#67C23A', '计算机类书籍', 2,0,'C10002');
INSERT INTO `categories`(`id`, `name`, `color`, `description`, `sort_order`, `parent_id`, `code`) VALUES (3, '历史', '#E6A23C', '历史类书籍', 3,0,'C10003');
INSERT INTO `categories`(`id`, `name`, `color`, `description`, `sort_order`, `parent_id`, `code`) VALUES (4, '哲学', '#F56C6C', '哲学类书籍', 4,0,'C10004');
INSERT INTO `categories`(`id`, `name`, `color`, `description`, `sort_order`, `parent_id`, `code`) VALUES (5, '经济', '#F78989', '经济类书籍', 5,0,'C10005');
INSERT INTO `categories`(`id`, `name`, `color`, `description`, `sort_order`, `parent_id`, `code`) VALUES (6, '艺术', '#909399', '艺术类书籍', 6,0,'C10006');
INSERT INTO `categories`(`id`, `name`, `color`, `description`, `sort_order`, `parent_id`, `code`) VALUES (7, '教育', '#2C3E50', '教育类书籍', 7,0,'C10007');
INSERT INTO `categories`(`id`, `name`, `color`, `description`, `sort_order`, `parent_id`, `code`) VALUES (8, '医学', '#9B59B6', '医学类书籍', 8,0,'C10008');
INSERT INTO `categories`(`id`, `name`, `color`, `description`, `sort_order`, `parent_id`, `code`) VALUES (9, '法律', '#27AE60', '法律类书籍', 9,0,'C10009');
INSERT INTO `categories`(`id`, `name`, `color`, `description`, `sort_order`, `parent_id`, `code`) VALUES (10, '管理', '#2ECC71', '管理类书籍', 10,0,'C10010');
