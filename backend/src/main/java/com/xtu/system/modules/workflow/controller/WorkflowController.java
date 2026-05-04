package com.xtu.system.modules.workflow.controller;

import com.xtu.system.common.api.ApiResponse;
import com.xtu.system.common.api.PageResponse;
import com.xtu.system.modules.workflow.dto.WorkflowTaskQueryRequest;
import com.xtu.system.modules.workflow.dto.WorkflowTaskReviewRequest;
import com.xtu.system.modules.workflow.service.WorkflowService;
import com.xtu.system.modules.workflow.vo.WorkflowTaskItemVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workflow")
public class WorkflowController {

    private final WorkflowService workflowService;

    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @GetMapping("/todo")
    @PreAuthorize("hasAuthority('business:application:view')")
    public ApiResponse<PageResponse<WorkflowTaskItemVO>> getTodoPage(WorkflowTaskQueryRequest request) {
        return ApiResponse.success(workflowService.getTodoPage(request));
    }

    @GetMapping("/done")
    @PreAuthorize("hasAuthority('business:application:view')")
    public ApiResponse<PageResponse<WorkflowTaskItemVO>> getDonePage(WorkflowTaskQueryRequest request) {
        return ApiResponse.success(workflowService.getDonePage(request));
    }

    @PutMapping("/tasks/{recordId}/approve")
    @PreAuthorize("hasAuthority('business:application:review')")
    public ApiResponse<Boolean> approveTask(@PathVariable Long recordId, @RequestBody(required = false) WorkflowTaskReviewRequest request) {
        workflowService.approveTask(recordId, request);
        return ApiResponse.success("审批通过", Boolean.TRUE);
    }

    @PutMapping("/tasks/{recordId}/reject")
    @PreAuthorize("hasAuthority('business:application:review')")
    public ApiResponse<Boolean> rejectTask(@PathVariable Long recordId, @RequestBody WorkflowTaskReviewRequest request) {
        workflowService.rejectTask(recordId, request);
        return ApiResponse.success("审批驳回", Boolean.TRUE);
    }
}
