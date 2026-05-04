package com.xtu.system.modules.workflow.service;

import com.xtu.system.common.api.PageResponse;
import com.xtu.system.modules.workflow.dto.WorkflowTaskQueryRequest;
import com.xtu.system.modules.workflow.dto.WorkflowTaskReviewRequest;
import com.xtu.system.modules.workflow.vo.WorkflowTaskItemVO;

public interface WorkflowService {

    PageResponse<WorkflowTaskItemVO> getTodoPage(WorkflowTaskQueryRequest request);

    PageResponse<WorkflowTaskItemVO> getDonePage(WorkflowTaskQueryRequest request);

    void approveTask(Long recordId, WorkflowTaskReviewRequest request);

    void rejectTask(Long recordId, WorkflowTaskReviewRequest request);
}
