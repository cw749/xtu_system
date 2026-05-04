CREATE TABLE IF NOT EXISTS `biz_application_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `application_id` BIGINT NOT NULL COMMENT '申请单ID',
    `step_no` INT NOT NULL DEFAULT 1 COMMENT '步骤序号',
    `approver_id` BIGINT DEFAULT NULL COMMENT '处理人ID',
    `approver_name` VARCHAR(50) DEFAULT NULL COMMENT '处理人姓名',
    `action_type` VARCHAR(20) NOT NULL COMMENT '动作类型：submit/approve/reject/withdraw',
    `comment_text` VARCHAR(500) DEFAULT NULL COMMENT '处理意见',
    `before_status` TINYINT DEFAULT NULL COMMENT '处理前状态',
    `after_status` TINYINT DEFAULT NULL COMMENT '处理后状态',
    `acted_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '处理时间',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_biz_application_record_app_id` (`application_id`),
    KEY `idx_biz_application_record_acted_at` (`acted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='审批记录表';

CREATE TABLE IF NOT EXISTS `biz_attachment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `biz_type` VARCHAR(50) NOT NULL COMMENT '关联业务类型',
    `biz_id` BIGINT NOT NULL COMMENT '关联业务ID',
    `file_name` VARCHAR(255) NOT NULL COMMENT '原始文件名',
    `file_ext` VARCHAR(20) DEFAULT NULL COMMENT '文件扩展名',
    `content_type` VARCHAR(100) DEFAULT NULL COMMENT '文件MIME类型',
    `file_size` BIGINT NOT NULL DEFAULT 0 COMMENT '文件大小',
    `storage_type` VARCHAR(20) NOT NULL DEFAULT 'local' COMMENT '存储类型',
    `bucket_name` VARCHAR(100) DEFAULT NULL COMMENT '存储桶',
    `object_key` VARCHAR(255) DEFAULT NULL COMMENT '存储对象Key',
    `file_url` VARCHAR(500) DEFAULT NULL COMMENT '访问地址',
    `uploader_id` BIGINT NOT NULL COMMENT '上传人ID',
    `created_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` BIGINT DEFAULT NULL COMMENT '修改人ID',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    PRIMARY KEY (`id`),
    KEY `idx_biz_attachment_biz` (`biz_type`, `biz_id`),
    KEY `idx_biz_attachment_uploader_id` (`uploader_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='附件表';

CREATE TABLE IF NOT EXISTS `sys_login_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT DEFAULT NULL COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '登录账号',
    `real_name` VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
    `login_type` VARCHAR(20) NOT NULL DEFAULT 'password' COMMENT '登录方式',
    `login_ip` VARCHAR(64) DEFAULT NULL COMMENT '登录IP',
    `login_location` VARCHAR(100) DEFAULT NULL COMMENT '登录地点',
    `user_agent` VARCHAR(500) DEFAULT NULL COMMENT 'User-Agent',
    `browser` VARCHAR(100) DEFAULT NULL COMMENT '浏览器',
    `os` VARCHAR(100) DEFAULT NULL COMMENT '操作系统',
    `login_status` TINYINT NOT NULL COMMENT '登录结果',
    `fail_reason` VARCHAR(255) DEFAULT NULL COMMENT '失败原因',
    `login_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
    PRIMARY KEY (`id`),
    KEY `idx_sys_login_log_user_id` (`user_id`),
    KEY `idx_sys_login_log_username` (`username`),
    KEY `idx_sys_login_log_login_at` (`login_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='登录日志表';

CREATE TABLE IF NOT EXISTS `sys_operation_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `module_name` VARCHAR(50) NOT NULL COMMENT '模块名称',
    `biz_type` VARCHAR(50) DEFAULT NULL COMMENT '业务类型',
    `biz_id` BIGINT DEFAULT NULL COMMENT '业务ID',
    `operation_type` VARCHAR(20) NOT NULL COMMENT '操作类型',
    `request_uri` VARCHAR(255) NOT NULL COMMENT '请求地址',
    `request_method` VARCHAR(10) NOT NULL COMMENT '请求方法',
    `request_params` TEXT DEFAULT NULL COMMENT '请求参数',
    `operator_id` BIGINT DEFAULT NULL COMMENT '操作人ID',
    `operator_name` VARCHAR(50) DEFAULT NULL COMMENT '操作人姓名',
    `operator_ip` VARCHAR(64) DEFAULT NULL COMMENT '操作IP',
    `result_status` TINYINT NOT NULL DEFAULT 1 COMMENT '执行结果',
    `error_message` VARCHAR(500) DEFAULT NULL COMMENT '错误信息',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    PRIMARY KEY (`id`),
    KEY `idx_sys_operation_log_module_name` (`module_name`),
    KEY `idx_sys_operation_log_operator_id` (`operator_id`),
    KEY `idx_sys_operation_log_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

INSERT INTO `biz_application_record` (
    `id`, `application_id`, `step_no`, `approver_id`, `approver_name`, `action_type`, `comment_text`, `before_status`, `after_status`, `acted_at`
) VALUES
    (60001, 50001, 1, 2, '张老师', 'submit', '申请调整上课时间到周四下午。', NULL, 0, NOW()),
    (60002, 50002, 1, 1, '系统管理员', 'submit', '需要统一发布教师资料维护说明。', NULL, 0, NOW()),
    (60003, 50002, 2, 1, '系统管理员', 'approve', '已审核通过', 0, 1, NOW())
ON DUPLICATE KEY UPDATE
    `approver_name` = VALUES(`approver_name`),
    `action_type` = VALUES(`action_type`),
    `comment_text` = VALUES(`comment_text`),
    `before_status` = VALUES(`before_status`),
    `after_status` = VALUES(`after_status`),
    `acted_at` = VALUES(`acted_at`);

INSERT INTO `sys_menu` (
    `id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component_path`, `permission_code`, `icon`, `sort_no`, `visible`, `status`, `remark`, `created_by`, `updated_by`, `deleted`, `version`
) VALUES
    (140, 100, '日志管理', 'C', '/system/logs', 'views/system/log/index.vue', 'system:log:login:view', 'Document', 14, 1, 1, '日志管理页面', 1, 1, 0, 0),
    (141, 100, '登录日志查看', 'B', NULL, NULL, 'system:log:login:view', NULL, 4, 0, 1, '登录日志权限', 1, 1, 0, 0),
    (142, 100, '操作日志查看', 'B', NULL, NULL, 'system:log:operation:view', NULL, 5, 0, 1, '操作日志权限', 1, 1, 0, 0),
    (440, 400, '附件管理', 'C', '/business/attachments', 'views/file/attachment/index.vue', 'file:attachment:view', 'FolderOpened', 44, 1, 1, '附件管理页面', 1, 1, 0, 0),
    (441, 400, '附件上传', 'B', NULL, NULL, 'file:attachment:upload', NULL, 5, 0, 1, '附件上传权限', 1, 1, 0, 0),
    (442, 400, '附件查看', 'B', NULL, NULL, 'file:attachment:view', NULL, 6, 0, 1, '附件查看权限', 1, 1, 0, 0),
    (443, 400, '附件删除', 'B', NULL, NULL, 'file:attachment:delete', NULL, 7, 0, 1, '附件删除权限', 1, 1, 0, 0),
    (500, 0, '流程中心', 'M', '/workflow', NULL, NULL, 'Connection', 50, 1, 1, '流程中心目录', 1, 1, 0, 0),
    (510, 500, '审批任务', 'C', '/workflow/applications', 'views/workflow/application/index.vue', 'business:application:view', 'Tickets', 51, 1, 1, '审批任务页面', 1, 1, 0, 0)
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
    (1, 140, 1),
    (1, 141, 1),
    (1, 142, 1),
    (1, 440, 1),
    (1, 441, 1),
    (1, 442, 1),
    (1, 443, 1),
    (1, 500, 1),
    (1, 510, 1)
ON DUPLICATE KEY UPDATE
    `created_by` = VALUES(`created_by`);
