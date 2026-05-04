package com.xtu.system.modules.file.attachment.mapper;

import com.xtu.system.modules.file.attachment.entity.AttachmentEntity;
import com.xtu.system.modules.file.attachment.vo.AttachmentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AttachmentMapper {

    int insertAttachment(AttachmentEntity entity);

    List<AttachmentVO> selectAttachments(@Param("bizType") String bizType, @Param("bizId") Long bizId);

    List<AttachmentEntity> selectAttachmentEntitiesByBiz(@Param("bizType") String bizType, @Param("bizId") Long bizId);

    AttachmentEntity selectAttachmentEntityById(@Param("id") Long id);

    AttachmentVO selectAttachmentDetailById(@Param("id") Long id);

    int updateAttachmentFileUrl(@Param("id") Long id, @Param("fileUrl") String fileUrl, @Param("updatedBy") Long updatedBy);

    int logicDeleteAttachment(@Param("id") Long id, @Param("updatedBy") Long updatedBy);

    int logicDeleteAttachmentsByBiz(@Param("bizType") String bizType, @Param("bizId") Long bizId, @Param("updatedBy") Long updatedBy);
}
