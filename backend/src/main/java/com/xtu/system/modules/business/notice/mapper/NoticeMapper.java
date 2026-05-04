package com.xtu.system.modules.business.notice.mapper;

import com.xtu.system.modules.business.notice.dto.NoticeQueryRequest;
import com.xtu.system.modules.business.notice.entity.NoticeEntity;
import com.xtu.system.modules.business.notice.vo.NoticeDetailVO;
import com.xtu.system.modules.business.notice.vo.NoticePageItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NoticeMapper {

    List<NoticePageItemVO> selectNoticePage(NoticeQueryRequest request);

    long countNoticePage(NoticeQueryRequest request);

    NoticeDetailVO selectNoticeDetailById(@Param("id") Long id);

    NoticeEntity selectNoticeEntityById(@Param("id") Long id);

    int insertNotice(NoticeEntity entity);

    int updateNotice(NoticeEntity entity);

    int logicDeleteNotice(@Param("id") Long id, @Param("updatedBy") Long updatedBy);

    int updatePublishStatus(@Param("id") Long id, @Param("publishStatus") Integer publishStatus, @Param("updatedBy") Long updatedBy);
}
