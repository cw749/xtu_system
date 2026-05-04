package com.xtu.system.modules.file.attachment.controller;

import com.xtu.system.common.api.ApiResponse;
import com.xtu.system.modules.file.attachment.service.AttachmentService;
import com.xtu.system.modules.file.attachment.vo.AttachmentVO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/attachments")
public class AttachmentController {

    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('file:attachment:upload')")
    public ApiResponse<AttachmentVO> uploadAttachment(
        @RequestParam("bizType") @NotBlank String bizType,
        @RequestParam("bizId") @NotNull Long bizId,
        @RequestParam("file") MultipartFile file
    ) {
        return ApiResponse.success("上传成功", attachmentService.uploadAttachment(bizType, bizId, file));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('file:attachment:view')")
    public ApiResponse<List<AttachmentVO>> getAttachments(
        @RequestParam("bizType") @NotBlank String bizType,
        @RequestParam("bizId") @NotNull Long bizId
    ) {
        return ApiResponse.success(attachmentService.getAttachments(bizType, bizId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('file:attachment:view')")
    public ApiResponse<AttachmentVO> getAttachmentDetail(@PathVariable Long id) {
        return ApiResponse.success(attachmentService.getAttachmentDetail(id));
    }

    @GetMapping("/{id}/download")
    @PreAuthorize("hasAuthority('file:attachment:view')")
    public ResponseEntity<byte[]> downloadAttachment(@PathVariable Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.attachment().filename(attachmentService.getDownloadFileName(id), StandardCharsets.UTF_8).build());
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok().headers(headers).body(attachmentService.downloadAttachment(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('file:attachment:delete')")
    public ApiResponse<Boolean> deleteAttachment(@PathVariable Long id) {
        attachmentService.deleteAttachment(id);
        return ApiResponse.success("删除成功", Boolean.TRUE);
    }
}
