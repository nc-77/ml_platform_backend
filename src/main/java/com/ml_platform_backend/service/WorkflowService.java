package com.ml_platform_backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ml_platform_backend.entry.Workflow;
import com.ml_platform_backend.mapper.WorkflowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkflowService {
    @Autowired
    private WorkflowMapper workflowMapper;

    public Workflow getWorkflowById(Integer id) {
        return workflowMapper.selectById(id);
    }

    // add workflow if it doesn't exist or update workflow
    public int setWorkflow(Workflow workflow) {
        if (workflow.getId() == null) {
            return workflowMapper.insert(workflow);
        }
        return workflowMapper.updateById(workflow);
    }

    public int deleteWorkflowById(Integer id) {
        return workflowMapper.deleteById(id);
    }

    public List<Workflow> getAllWorkflowsByUser(Integer userId) {
        QueryWrapper<Workflow> query = new QueryWrapper<>();
        query.eq("user_id", userId);
        return workflowMapper.selectList(query);
    }
}
