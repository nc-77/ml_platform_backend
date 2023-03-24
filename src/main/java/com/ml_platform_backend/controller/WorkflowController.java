package com.ml_platform_backend.controller;

import com.ml_platform_backend.entry.Workflow;
import com.ml_platform_backend.entry.result.Code;
import com.ml_platform_backend.entry.result.ResponseEntity;
import com.ml_platform_backend.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WorkflowController {

    @Autowired
    private WorkflowService workflowService;

    @GetMapping("/workflows/{id}")
    public ResponseEntity getWorkflowById(@PathVariable Integer id) {
        Workflow workflow = workflowService.getWorkflowById(id);
        if (workflow == null) {
            return new ResponseEntity(Code.FAILED.getValue(), null, Code.FAILED.getDescription());
        }
        return new ResponseEntity(Code.SUCCESS.getValue(), workflow, Code.SUCCESS.getDescription());
    }

    @GetMapping("/workflows")
    public ResponseEntity getAllWorkflowsByUser(@RequestParam("userId") Integer userId) {
        List<Workflow> workflows = workflowService.getAllWorkflowsByUser(userId);
        return new ResponseEntity(Code.SUCCESS.getValue(), workflows, Code.SUCCESS.getDescription());
    }

    @PostMapping("/workflow")
    public ResponseEntity insertWorkflows(@RequestBody Workflow workflow) {
        workflowService.setWorkflow(workflow);
        return new ResponseEntity(Code.SUCCESS.getValue(), workflow, Code.SUCCESS.getDescription());
    }

    @DeleteMapping("/workflow/{id}")
    public ResponseEntity deleteWorkflowById(@PathVariable Integer id) {
        workflowService.deleteWorkflowById(id);
        return new ResponseEntity(Code.SUCCESS.getValue(), null, Code.SUCCESS.getDescription());
    }
}
