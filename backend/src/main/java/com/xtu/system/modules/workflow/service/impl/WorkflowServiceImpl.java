package com.xtu.system.modules.workflow.service.impl;

import com.xtu.system.common.api.PageResponse;
import com.xtu.system.common.exception.BusinessException;
import com.xtu.system.config.security.SecurityUtils;
import com.xtu.system.modules.business.application.dto.ApplicationReviewRequest;
import com.xtu.system.modules.business.application.mapper.ApplicationMapper;
import com.xtu.system.modules.business.application.service.ApplicationService;
import com.xtu.system.modules.workflow.dto.WorkflowTaskQueryRequest;
import com.xtu.system.modules.workflow.dto.WorkflowTaskReviewRequest;
import com.xtu.system.modules.workflow.service.WorkflowService;
import com.xtu.system.modules.workflow.vo.WorkflowTaskItemVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkflowServiceImpl implements WorkflowService {

    private static final int STATUS_APPROVED = 1;
    private static final int STATUS_REJECTED = 2;

    private final ApplicationMapper applicationMapper;
    private final ApplicationService applicationService;

    public WorkflowServiceImpl(ApplicationMapper applicationMapper, ApplicationService applicationService) {
        this.applicationMapper = applicationMapper;
        this.applicationService = applicationService;
    }

    @Override
    public PageResponse<WorkflowTaskItemVO> getTodoPage(WorkflowTaskQueryRequest request) {
        List<WorkflowTaskItemVO> list = applicationMapper.selectWorkflowTodo(request);
        long total = applicationMapper.countWorkflowTodo(request);
        return PageResponse.of(list, request.getPageNum(), request.getPageSize(), total);
    }

    @Override
    public PageResponse<WorkflowTaskItemVO> getDonePage(WorkflowTaskQueryRequest request) {
        request.setCurrentUserId(SecurityUtils.getCurrentUserId());
        List<WorkflowTaskItemVO> list = applicationMapper.selectWorkflowDone(request);
        long total = applicationMapper.countWorkflowDone(request);
        return PageResponse.of(list, request.getPageNum(), request.getPageSize(), total);
    }

    @Override
    public void approveTask(Long recordId, WorkflowTaskReviewRequest request) {
        ApplicationReviewRequest reviewRequest = new ApplicationReviewRequest();
        reviewRequest.setStatus(STATUS_APPROVED);
        reviewRequest.setReviewRemark(request == null ? null : request.getCommentText());
        applicationService.reviewApplication(recordId, reviewRequest);
    }

    @Override
    public void rejectTask(Long recordId, WorkflowTaskReviewRequest request) {
        if (request == null || request.getCommentText() == null || request.getCommentText().isBlank()) {
            throw new BusinessException("驳回原因不能为空");
        }
        ApplicationReviewRequest reviewRequest = new ApplicationReviewRequest();
        reviewRequest.setStatus(STATUS_REJECTED);
        reviewRequest.setReviewRemark(request.getCommentText());
        applicationService.reviewApplication(recordId, reviewRequest);
    }
}
