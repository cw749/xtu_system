package com.xtu.system.modules.business.notice.service.impl;

import com.xtu.system.common.api.PageResponse;
import com.xtu.system.common.exception.BusinessException;
import com.xtu.system.config.security.SecurityUtils;
import com.xtu.system.modules.file.attachment.service.AttachmentService;
import com.xtu.system.modules.business.notice.dto.NoticeCreateRequest;
import com.xtu.system.modules.business.notice.dto.NoticePublishRequest;
import com.xtu.system.modules.business.notice.dto.NoticeQueryRequest;
import com.xtu.system.modules.business.notice.dto.NoticeUpdateRequest;
import com.xtu.system.modules.business.notice.entity.NoticeEntity;
import com.xtu.system.modules.business.notice.mapper.NoticeMapper;
import com.xtu.system.modules.business.notice.service.NoticeService;
import com.xtu.system.modules.business.notice.vo.NoticeDetailVO;
import com.xtu.system.modules.business.notice.vo.NoticePageItemVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {

    private static final long SYSTEM_OPERATOR_ID = 1L;

    private final NoticeMapper noticeMapper;
    private final AttachmentService attachmentService;

    public NoticeServiceImpl(NoticeMapper noticeMapper, AttachmentService attachmentService) {
        this.noticeMapper = noticeMapper;
        this.attachmentService = attachmentService;
    }

    @Override
    public PageResponse<NoticePageItemVO> getNoticePage(NoticeQueryRequest request) {
        List<NoticePageItemVO> list = noticeMapper.selectNoticePage(request);
        long total = noticeMapper.countNoticePage(request);
        return PageResponse.of(list, request.getPageNum(), request.getPageSize(), total);
    }

    @Override
    public NoticeDetailVO getNoticeDetail(Long id) {
        NoticeDetailVO detail = noticeMapper.selectNoticeDetailById(id);
        if (detail == null) {
            throw new BusinessException("公告不存在");
        }
        return detail;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createNotice(NoticeCreateRequest request) {
        NoticeEntity entity = new NoticeEntity();
        fillNoticeEntity(entity, request.getTitle(), request.getNoticeType(), request.getContent(), request.getPublishStatus(), request.getPinned());
        entity.setPublisherId(getOperatorId());
        entity.setCreatedBy(getOperatorId());
        entity.setUpdatedBy(getOperatorId());
        noticeMapper.insertNotice(entity);
        return entity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateNotice(Long id, NoticeUpdateRequest request) {
        NoticeEntity existing = noticeMapper.selectNoticeEntityById(id);
        if (existing == null) {
            throw new BusinessException("公告不存在");
        }
        fillNoticeEntity(existing, request.getTitle(), request.getNoticeType(), request.getContent(), request.getPublishStatus(), request.getPinned());
        existing.setUpdatedBy(getOperatorId());
        noticeMapper.updateNotice(existing);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteNotice(Long id) {
        Long operatorId = getOperatorId();
        if (noticeMapper.logicDeleteNotice(id, operatorId) == 0) {
            throw new BusinessException("公告不存在");
        }
        attachmentService.deleteAttachmentsByBiz("notice", id, operatorId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePublishStatus(Long id, NoticePublishRequest request) {
        NoticeEntity existing = noticeMapper.selectNoticeEntityById(id);
        if (existing == null) {
            throw new BusinessException("公告不存在");
        }
        if (noticeMapper.updatePublishStatus(id, request.getPublishStatus(), getOperatorId()) == 0) {
            throw new BusinessException("公告不存在");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void revokeNotice(Long id) {
        NoticeEntity existing = noticeMapper.selectNoticeEntityById(id);
        if (existing == null) {
            throw new BusinessException("公告不存在");
        }
        if (noticeMapper.updatePublishStatus(id, 0, getOperatorId()) == 0) {
            throw new BusinessException("公告不存在");
        }
    }

    private void fillNoticeEntity(
        NoticeEntity entity,
        String title,
        String noticeType,
        String content,
        Integer publishStatus,
        Integer pinned
    ) {
        entity.setTitle(title);
        entity.setNoticeType(noticeType);
        entity.setContent(content);
        entity.setPublishStatus(publishStatus == null ? 0 : publishStatus);
        entity.setPinned(pinned == null ? 0 : pinned);
    }

    private Long getOperatorId() {
        try {
            return SecurityUtils.getCurrentUserId();
        } catch (BusinessException exception) {
            return SYSTEM_OPERATOR_ID;
        }
    }
}
