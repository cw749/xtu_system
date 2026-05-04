package com.xtu.system.modules.file.attachment.service.impl;

import com.xtu.system.common.exception.BusinessException;
import com.xtu.system.config.security.SecurityUtils;
import com.xtu.system.modules.file.attachment.entity.AttachmentEntity;
import com.xtu.system.modules.file.attachment.mapper.AttachmentMapper;
import com.xtu.system.modules.file.attachment.service.AttachmentService;
import com.xtu.system.modules.file.attachment.vo.AttachmentVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentMapper attachmentMapper;
    private final Path uploadRoot;

    public AttachmentServiceImpl(
        AttachmentMapper attachmentMapper,
        @Value("${storage.upload-dir}") String uploadDir
    ) {
        this.attachmentMapper = attachmentMapper;
        this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AttachmentVO uploadAttachment(String bizType, Long bizId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("上传文件不能为空");
        }
        if (bizType == null || bizType.isBlank() || bizId == null) {
            throw new BusinessException("业务信息不能为空");
        }

        String originalFilename = file.getOriginalFilename() == null ? "file" : file.getOriginalFilename();
        String fileExt = resolveFileExt(originalFilename);
        String objectKey = bizType + "/" + UUID.randomUUID() + (fileExt.isBlank() ? "" : "." + fileExt);
        Path targetPath = uploadRoot.resolve(objectKey).normalize();

        try {
            Files.createDirectories(targetPath.getParent());
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {
            throw new BusinessException("附件保存失败");
        }

        Long operatorId = SecurityUtils.getCurrentUserId();
        AttachmentEntity entity = new AttachmentEntity();
        entity.setBizType(bizType);
        entity.setBizId(bizId);
        entity.setFileName(originalFilename);
        entity.setFileExt(fileExt);
        entity.setContentType(file.getContentType());
        entity.setFileSize(file.getSize());
        entity.setStorageType("local");
        entity.setObjectKey(objectKey.replace('\\', '/'));
        entity.setFileUrl("/api/attachments/__TEMP__/download");
        entity.setUploaderId(operatorId);
        entity.setCreatedBy(operatorId);
        entity.setUpdatedBy(operatorId);
        attachmentMapper.insertAttachment(entity);

        String fileUrl = "/api/attachments/" + entity.getId() + "/download";
        attachmentMapper.updateAttachmentFileUrl(entity.getId(), fileUrl, operatorId);

        AttachmentVO vo = new AttachmentVO();
        vo.setId(entity.getId());
        vo.setBizType(entity.getBizType());
        vo.setBizId(entity.getBizId());
        vo.setFileName(entity.getFileName());
        vo.setFileSize(entity.getFileSize());
        vo.setContentType(entity.getContentType());
        vo.setFileUrl(fileUrl);
        return vo;
    }

    @Override
    public List<AttachmentVO> getAttachments(String bizType, Long bizId) {
        List<AttachmentVO> list = attachmentMapper.selectAttachments(bizType, bizId);
        for (AttachmentVO item : list) {
            item.setFileUrl("/api/attachments/" + item.getId() + "/download");
        }
        return list;
    }

    @Override
    public AttachmentVO getAttachmentDetail(Long id) {
        AttachmentVO detail = attachmentMapper.selectAttachmentDetailById(id);
        if (detail == null) {
            throw new BusinessException("附件不存在");
        }
        detail.setFileUrl("/api/attachments/" + detail.getId() + "/download");
        return detail;
    }

    @Override
    public byte[] downloadAttachment(Long id) {
        AttachmentEntity entity = getAttachmentEntity(id);
        Path filePath = uploadRoot.resolve(entity.getObjectKey()).normalize();
        if (!Files.exists(filePath)) {
            throw new BusinessException("附件文件不存在");
        }
        try {
            return Files.readAllBytes(filePath);
        } catch (IOException exception) {
            throw new BusinessException("附件读取失败");
        }
    }

    @Override
    public String getDownloadFileName(Long id) {
        return getAttachmentEntity(id).getFileName();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAttachment(Long id) {
        AttachmentEntity entity = getAttachmentEntity(id);
        deleteAttachmentFile(entity);
        if (attachmentMapper.logicDeleteAttachment(id, SecurityUtils.getCurrentUserId()) == 0) {
            throw new BusinessException("附件不存在");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAttachmentsByBiz(String bizType, Long bizId, Long updatedBy) {
        if (bizType == null || bizType.isBlank() || bizId == null) {
            return;
        }

        List<AttachmentEntity> attachments = attachmentMapper.selectAttachmentEntitiesByBiz(bizType, bizId);
        for (AttachmentEntity attachment : attachments) {
            deleteAttachmentFile(attachment);
        }

        attachmentMapper.logicDeleteAttachmentsByBiz(bizType, bizId, updatedBy == null ? 1L : updatedBy);
    }

    private AttachmentEntity getAttachmentEntity(Long id) {
        AttachmentEntity entity = attachmentMapper.selectAttachmentEntityById(id);
        if (entity == null) {
            throw new BusinessException("附件不存在");
        }
        return entity;
    }

    private void deleteAttachmentFile(AttachmentEntity entity) {
        Path filePath = uploadRoot.resolve(entity.getObjectKey()).normalize();
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException exception) {
            throw new BusinessException("附件删除失败");
        }
    }

    private String resolveFileExt(String filename) {
        int index = filename.lastIndexOf('.');
        if (index < 0 || index == filename.length() - 1) {
            return "";
        }
        return filename.substring(index + 1).toLowerCase();
    }
}
