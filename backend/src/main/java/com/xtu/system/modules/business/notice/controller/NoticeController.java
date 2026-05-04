package com.xtu.system.modules.business.notice.controller;

import com.xtu.system.common.api.ApiResponse;
import com.xtu.system.common.api.PageResponse;
import com.xtu.system.modules.business.notice.dto.NoticeCreateRequest;
import com.xtu.system.modules.business.notice.dto.NoticePublishRequest;
import com.xtu.system.modules.business.notice.dto.NoticeQueryRequest;
import com.xtu.system.modules.business.notice.dto.NoticeUpdateRequest;
import com.xtu.system.modules.business.notice.service.NoticeService;
import com.xtu.system.modules.business.notice.vo.NoticeDetailVO;
import com.xtu.system.modules.business.notice.vo.NoticePageItemVO;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/notices")
public class NoticeController {

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('business:notice:view')")
    public ApiResponse<PageResponse<NoticePageItemVO>> getNoticePage(NoticeQueryRequest request) {
        return ApiResponse.success(noticeService.getNoticePage(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('business:notice:view')")
    public ApiResponse<NoticeDetailVO> getNoticeDetail(@PathVariable Long id) {
        return ApiResponse.success(noticeService.getNoticeDetail(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('business:notice:create')")
    public ApiResponse<Long> createNotice(@Valid @RequestBody NoticeCreateRequest request) {
        return ApiResponse.success("创建成功", noticeService.createNotice(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('business:notice:update')")
    public ApiResponse<Boolean> updateNotice(@PathVariable Long id, @Valid @RequestBody NoticeUpdateRequest request) {
        noticeService.updateNotice(id, request);
        return ApiResponse.success("更新成功", Boolean.TRUE);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('business:notice:delete')")
    public ApiResponse<Boolean> deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return ApiResponse.success("删除成功", Boolean.TRUE);
    }

    @PutMapping("/{id}/publish")
    @PreAuthorize("hasAuthority('business:notice:publish')")
    public ApiResponse<Boolean> updatePublishStatus(@PathVariable Long id, @Valid @RequestBody NoticePublishRequest request) {
        noticeService.updatePublishStatus(id, request);
        return ApiResponse.success("状态更新成功", Boolean.TRUE);
    }

    @PutMapping("/{id}/revoke")
    @PreAuthorize("hasAuthority('business:notice:publish')")
    public ApiResponse<Boolean> revokeNotice(@PathVariable Long id) {
        noticeService.revokeNotice(id);
        return ApiResponse.success("撤回成功", Boolean.TRUE);
    }
}
