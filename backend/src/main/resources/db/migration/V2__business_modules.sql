CREATE TABLE IF NOT EXISTS `biz_course` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `course_code` VARCHAR(32) NOT NULL COMMENT '课程编码',
    `course_name` VARCHAR(100) NOT NULL COMMENT '课程名称',
    `dept_id` BIGINT NOT NULL COMMENT '所属部门ID',
    `teacher_id` BIGINT DEFAULT NULL COMMENT '授课教师ID',
    `credit` DECIMAL(4, 1) NOT NULL DEFAULT 0 COMMENT '学分',
    `course_type` VARCHAR(32) DEFAULT NULL COMMENT '课程类型',
    `semester` VARCHAR(32) DEFAULT NULL COMMENT '开课学期',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1启用 0停用',
    `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
    `created_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` BIGINT DEFAULT NULL COMMENT '修改人ID',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_biz_course_code` (`course_code`, `deleted`),
    KEY `idx_biz_course_dept_id` (`dept_id`),
    KEY `idx_biz_course_teacher_id` (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='课程表';

CREATE TABLE IF NOT EXISTS `biz_notice` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `title` VARCHAR(150) NOT NULL COMMENT '公告标题',
    `notice_type` VARCHAR(32) DEFAULT NULL COMMENT '公告类型',
    `content` TEXT COMMENT '公告内容',
    `publish_status` TINYINT NOT NULL DEFAULT 0 COMMENT '发布状态：0草稿 1已发布',
    `pinned` TINYINT NOT NULL DEFAULT 0 COMMENT '是否置顶：0否 1是',
    `publisher_id` BIGINT DEFAULT NULL COMMENT '发布人ID',
    `publish_time` DATETIME DEFAULT NULL COMMENT '发布时间',
    `created_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` BIGINT DEFAULT NULL COMMENT '修改人ID',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    PRIMARY KEY (`id`),
    KEY `idx_biz_notice_publish_status` (`publish_status`),
    KEY `idx_biz_notice_publish_time` (`publish_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公告表';

CREATE TABLE IF NOT EXISTS `biz_application` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `applicant_user_id` BIGINT NOT NULL COMMENT '申请人用户ID',
    `applicant_name` VARCHAR(50) NOT NULL COMMENT '申请人姓名',
    `application_type` VARCHAR(32) NOT NULL COMMENT '申请类型',
    `target_name` VARCHAR(100) DEFAULT NULL COMMENT '申请对象',
    `reason` VARCHAR(500) NOT NULL COMMENT '申请原因',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0待审核 1已通过 2已驳回',
    `submit_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
    `process_time` DATETIME DEFAULT NULL COMMENT '处理时间',
    `approver_user_id` BIGINT DEFAULT NULL COMMENT '审核人用户ID',
    `approver_name` VARCHAR(50) DEFAULT NULL COMMENT '审核人姓名',
    `review_remark` VARCHAR(255) DEFAULT NULL COMMENT '审核备注',
    `created_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` BIGINT DEFAULT NULL COMMENT '修改人ID',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    PRIMARY KEY (`id`),
    KEY `idx_biz_application_applicant_user_id` (`applicant_user_id`),
    KEY `idx_biz_application_status` (`status`),
    KEY `idx_biz_application_submit_time` (`submit_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='申请单表';

INSERT INTO `sys_role` (
    `id`, `role_code`, `role_name`, `data_scope`, `status`, `remark`, `created_by`, `updated_by`, `deleted`, `version`
) VALUES
    (3, 'student', '学生', 4, 1, '学生演示角色', 1, 1, 0, 0)
ON DUPLICATE KEY UPDATE
    `role_code` = VALUES(`role_code`),
    `role_name` = VALUES(`role_name`),
    `data_scope` = VALUES(`data_scope`),
    `status` = VALUES(`status`),
    `remark` = VALUES(`remark`),
    `updated_by` = VALUES(`updated_by`),
    `deleted` = 0;

INSERT INTO `biz_course` (
    `id`, `course_code`, `course_name`, `dept_id`, `teacher_id`, `credit`, `course_type`, `semester`, `status`, `remark`, `created_by`, `updated_by`, `deleted`, `version`
) VALUES
    (30001, 'SE101', '软件工程导论', 2001, 20001, 3.0, '必修', '2026春', 1, '初始化课程数据', 1, 1, 0, 0),
    (30002, 'CS204', '数据库系统原理', 2001, 20002, 4.0, '核心', '2026春', 1, '初始化课程数据', 1, 1, 0, 0)
ON DUPLICATE KEY UPDATE
    `course_name` = VALUES(`course_name`),
    `dept_id` = VALUES(`dept_id`),
    `teacher_id` = VALUES(`teacher_id`),
    `credit` = VALUES(`credit`),
    `course_type` = VALUES(`course_type`),
    `semester` = VALUES(`semester`),
    `status` = VALUES(`status`),
    `remark` = VALUES(`remark`),
    `updated_by` = VALUES(`updated_by`),
    `deleted` = 0;

INSERT INTO `biz_notice` (
    `id`, `title`, `notice_type`, `content`, `publish_status`, `pinned`, `publisher_id`, `publish_time`, `created_by`, `updated_by`, `deleted`, `version`
) VALUES
    (40001, '2026 春季学期选课提醒', '教学通知', '请各学院在第 8 周前完成选课确认。', 1, 1, 1, NOW(), 1, 1, 0, 0),
    (40002, '教师资料维护说明', '系统通知', '请教师于本周内补充职称和联系方式。', 0, 0, 1, NULL, 1, 1, 0, 0)
ON DUPLICATE KEY UPDATE
    `title` = VALUES(`title`),
    `notice_type` = VALUES(`notice_type`),
    `content` = VALUES(`content`),
    `publish_status` = VALUES(`publish_status`),
    `pinned` = VALUES(`pinned`),
    `publisher_id` = VALUES(`publisher_id`),
    `publish_time` = VALUES(`publish_time`),
    `updated_by` = VALUES(`updated_by`),
    `deleted` = 0;

INSERT INTO `biz_application` (
    `id`, `applicant_user_id`, `applicant_name`, `application_type`, `target_name`, `reason`, `status`, `submit_time`, `process_time`, `approver_user_id`, `approver_name`, `review_remark`, `created_by`, `updated_by`, `deleted`, `version`
) VALUES
    (50001, 2, '张老师', '课程调整', '软件工程导论', '申请调整上课时间到周四下午。', 0, NOW(), NULL, NULL, NULL, NULL, 2, 2, 0, 0),
    (50002, 1, '系统管理员', '公告发布', '教师资料维护说明', '需要统一发布教师资料维护说明。', 1, NOW(), NOW(), 1, '系统管理员', '已审核通过', 1, 1, 0, 0)
ON DUPLICATE KEY UPDATE
    `applicant_name` = VALUES(`applicant_name`),
    `application_type` = VALUES(`application_type`),
    `target_name` = VALUES(`target_name`),
    `reason` = VALUES(`reason`),
    `status` = VALUES(`status`),
    `process_time` = VALUES(`process_time`),
    `approver_user_id` = VALUES(`approver_user_id`),
    `approver_name` = VALUES(`approver_name`),
    `review_remark` = VALUES(`review_remark`),
    `updated_by` = VALUES(`updated_by`),
    `deleted` = 0;

INSERT INTO `sys_menu` (
    `id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component_path`, `permission_code`, `icon`, `sort_no`, `visible`, `status`, `remark`, `created_by`, `updated_by`, `deleted`, `version`
) VALUES
    (314, 310, '学生账号管理', 'B', NULL, NULL, 'personnel:student:account', NULL, 4, 0, 1, '学生账号按钮', 1, 1, 0, 0),
    (324, 320, '教师账号管理', 'B', NULL, NULL, 'personnel:teacher:account', NULL, 4, 0, 1, '教师账号按钮', 1, 1, 0, 0),
    (400, 0, '业务管理', 'M', '/business', NULL, NULL, 'Briefcase', 40, 1, 1, '业务管理目录', 1, 1, 0, 0),
    (410, 400, '课程管理', 'C', '/business/courses', 'views/business/course/index.vue', 'business:course:view', 'ReadingLamp', 41, 1, 1, '课程管理菜单', 1, 1, 0, 0),
    (411, 410, '课程新增', 'B', NULL, NULL, 'business:course:create', NULL, 1, 0, 1, '课程新增按钮', 1, 1, 0, 0),
    (412, 410, '课程编辑', 'B', NULL, NULL, 'business:course:update', NULL, 2, 0, 1, '课程编辑按钮', 1, 1, 0, 0),
    (413, 410, '课程删除', 'B', NULL, NULL, 'business:course:delete', NULL, 3, 0, 1, '课程删除按钮', 1, 1, 0, 0),
    (420, 400, '公告管理', 'C', '/business/notices', 'views/business/notice/index.vue', 'business:notice:view', 'Bell', 42, 1, 1, '公告管理菜单', 1, 1, 0, 0),
    (421, 420, '公告新增', 'B', NULL, NULL, 'business:notice:create', NULL, 1, 0, 1, '公告新增按钮', 1, 1, 0, 0),
    (422, 420, '公告编辑', 'B', NULL, NULL, 'business:notice:update', NULL, 2, 0, 1, '公告编辑按钮', 1, 1, 0, 0),
    (423, 420, '公告删除', 'B', NULL, NULL, 'business:notice:delete', NULL, 3, 0, 1, '公告删除按钮', 1, 1, 0, 0),
    (424, 420, '公告发布', 'B', NULL, NULL, 'business:notice:publish', NULL, 4, 0, 1, '公告发布按钮', 1, 1, 0, 0),
    (430, 400, '申请管理', 'C', '/business/applications', 'views/business/application/index.vue', 'business:application:view', 'Tickets', 43, 1, 1, '申请管理菜单', 1, 1, 0, 0),
    (431, 430, '申请新增', 'B', NULL, NULL, 'business:application:create', NULL, 1, 0, 1, '申请新增按钮', 1, 1, 0, 0),
    (432, 430, '申请编辑', 'B', NULL, NULL, 'business:application:update', NULL, 2, 0, 1, '申请编辑按钮', 1, 1, 0, 0),
    (433, 430, '申请删除', 'B', NULL, NULL, 'business:application:delete', NULL, 3, 0, 1, '申请删除按钮', 1, 1, 0, 0),
    (434, 430, '申请审核', 'B', NULL, NULL, 'business:application:review', NULL, 4, 0, 1, '申请审核按钮', 1, 1, 0, 0)
ON DUPLICATE KEY UPDATE
    `parent_id` = VALUES(`parent_id`),
    `menu_name` = VALUES(`menu_name`),
    `menu_type` = VALUES(`menu_type`),
    `route_path` = VALUES(`route_path`),
    `component_path` = VALUES(`component_path`),
    `permission_code` = VALUES(`permission_code`),
    `icon` = VALUES(`icon`),
    `sort_no` = VALUES(`sort_no`),
    `visible` = VALUES(`visible`),
    `status` = VALUES(`status`),
    `updated_by` = VALUES(`updated_by`),
    `deleted` = 0;

INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `created_by`) VALUES
    (1, 314, 1),
    (1, 324, 1),
    (1, 400, 1),
    (1, 410, 1),
    (1, 411, 1),
    (1, 412, 1),
    (1, 413, 1),
    (1, 420, 1),
    (1, 421, 1),
    (1, 422, 1),
    (1, 423, 1),
    (1, 424, 1),
    (1, 430, 1),
    (1, 431, 1),
    (1, 432, 1),
    (1, 433, 1),
    (1, 434, 1)
ON DUPLICATE KEY UPDATE
    `created_by` = VALUES(`created_by`);
