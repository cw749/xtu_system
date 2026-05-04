package com.xtu.system.modules.business.notice.service;

import com.xtu.system.common.api.PageResponse;
import com.xtu.system.modules.business.notice.dto.NoticeCreateRequest;
import com.xtu.system.modules.business.notice.dto.NoticePublishRequest;
import com.xtu.system.modules.business.notice.dto.NoticeQueryRequest;
import com.xtu.system.modules.business.notice.dto.NoticeUpdateRequest;
import com.xtu.system.modules.business.notice.vo.NoticeDetailVO;
import com.xtu.system.modules.business.notice.vo.NoticePageItemVO;

public interface NoticeService {

    PageResponse<NoticePageItemVO> getNoticePage(NoticeQueryRequest request);

    NoticeDetailVO getNoticeDetail(Long id);

    Long createNotice(NoticeCreateRequest request);

    void updateNotice(Long id, NoticeUpdateRequest request);

    void deleteNotice(Long id);

    void updatePublishStatus(Long id, NoticePublishRequest request);

    void revokeNotice(Long id);
}
