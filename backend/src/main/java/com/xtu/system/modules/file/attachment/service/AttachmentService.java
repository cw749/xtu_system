package com.xtu.system.modules.file.attachment.service;

import com.xtu.system.modules.file.attachment.vo.AttachmentVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttachmentService {

    AttachmentVO uploadAttachment(String bizType, Long bizId, MultipartFile file);

    List<AttachmentVO> getAttachments(String bizType, Long bizId);

    AttachmentVO getAttachmentDetail(Long id);

    byte[] downloadAttachment(Long id);

    String getDownloadFileName(Long id);

    void deleteAttachment(Long id);

    void deleteAttachmentsByBiz(String bizType, Long bizId, Long updatedBy);
}
