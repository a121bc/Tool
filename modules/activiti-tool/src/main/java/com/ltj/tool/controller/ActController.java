package com.ltj.tool.controller;

import com.ltj.tool.activiti.factory.ActResponseFactory;
import com.ltj.tool.activiti.history.HistoricTaskInstanceResponse;
import com.ltj.tool.activiti.runtime.task.TaskResponse;
import com.ltj.tool.activiti.variable.RestVariable;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.persistence.entity.VariableInstanceEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @describe： TODO
 * @Date: 2021-01-27 16:09
 */

@RestController
//@RequestMapping("/Test")
@Slf4j
public class ActController {



    private final RepositoryService repositoryService;
    private final TaskService taskService;
    private final RuntimeService runtimeService;
    private final HistoryService historyService;
    private final ActResponseFactory actResponseFactory;
    private final ProcessEngineConfiguration processEngineConfiguration;


    public ActController(RepositoryService repositoryService,
                         TaskService taskService,
                         RuntimeService runtimeService,
                         HistoryService historyService,
                         ActResponseFactory actResponseFactory,
                         ProcessEngineConfiguration processEngineConfiguration) {
        this.repositoryService = repositoryService;
        this.taskService = taskService;
        this.runtimeService = runtimeService;
        this.historyService = historyService;
        this.actResponseFactory = actResponseFactory;
        this.processEngineConfiguration = processEngineConfiguration;
    }

    @GetMapping("/test/{varInstanceId}")
    public Map<String, Object> test(@PathVariable("varInstanceId") String varInstanceId,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        Map<String,Object> map = new HashMap<>(1);
        byte[] result = null;
        RestVariable variable = getVariableFromRequest(true, varInstanceId, request);
        map.put("variable",variable);
        return map;

    }

    @GetMapping("/getTaskInstances/{processInstanceId}")
    public Map<String, Object> getTaskInstances(@PathVariable String processInstanceId) {
        Map<String,Object> map = new HashMap<>(1);
        List<HistoricTaskInstance> list1 = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId("processInstanceId")
                .list();
        List<HistoricTaskInstanceResponse> historicTaskInstanceResponseList = actResponseFactory.createHistoricTaskInstanceResponseList(list1);
        map.put("historicTaskInstanceResponseList",historicTaskInstanceResponseList);
        return map;
    }

    @GetMapping("/getTasks/{processInstanceId}")
    public Map<String, Object> getTasks(@PathVariable String processInstanceId) {
        Map<String,Object> map = new HashMap<>(1);
        List<Task> list = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .list();
        List<TaskResponse> taskResponseList = actResponseFactory.createTaskResponseList(list);
        map.put("taskResponseList",taskResponseList);
        return map;
    }

    /**
     * @Description 获取流程实例
     * @param
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @author Liu Tian Jun
     * @date 10:43 2021-01-29 0029
     **/
    @GetMapping("/getProcessInstances")
    public Map<String, Object> getProcessInstances() {
        Map<String,Object> map = new HashMap<>(1);
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().list();
        map.put("processInstanceList",actResponseFactory.createProcessInstanceResponseList(list));
        return map;
    }

    /**
     * @Description 根据流程id获取流程图
     * @param processInstanceId
     * @return org.springframework.http.ResponseEntity<byte[]>
     * @author Liu Tian Jun
     * @date 10:43 2021-01-29 0029
     **/
    @GetMapping("/getDiagram/{processInstanceId}")
    public ResponseEntity<byte[]> getProcessInstanceDiagram(@PathVariable String processInstanceId) {
        ProcessInstance processInstance = getProcessInstanceFromRequest(processInstanceId);

        ProcessDefinition pde = repositoryService.getProcessDefinition(processInstance.getProcessDefinitionId());

        if (pde != null && pde.hasGraphicalNotation()) {
            BpmnModel bpmnModel = repositoryService.getBpmnModel(pde.getId());
            ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
            InputStream resource = diagramGenerator.generateDiagram(bpmnModel, "png", runtimeService.getActiveActivityIds(processInstance.getId()), Collections.<String> emptyList(),
                    processEngineConfiguration.getActivityFontName(), processEngineConfiguration.getLabelFontName(),
                    processEngineConfiguration.getAnnotationFontName(), processEngineConfiguration.getClassLoader(), 1.0);

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("Content-Type", "image/png");
            try {
                return new ResponseEntity<byte[]>(IOUtils.toByteArray(resource), responseHeaders, HttpStatus.OK);
            } catch (Exception e) {
                throw new ActivitiIllegalArgumentException("Error exporting diagram", e);
            }

        } else {
            throw new ActivitiIllegalArgumentException("Process instance with id '" + processInstance.getId() + "' has no graphical notation defined.");
        }
    }

    protected ProcessInstance getProcessInstanceFromRequest(String processInstanceId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (processInstance == null) {
            throw new ActivitiObjectNotFoundException("Could not find a process instance with id '" + processInstanceId + "'.");
        }
        return processInstance;
    }

    /**
     * Get valid task from request. Throws exception if task doen't exist or if task id is not provided.
     */
    protected Task getTaskFromRequest(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new ActivitiObjectNotFoundException("Could not find a task with id '" + taskId + "'.", Task.class);
        }
        return task;
    }

    public RestVariable getVariableFromRequest(boolean includeBinary, String varInstanceId, HttpServletRequest request) {
        HistoricVariableInstance varObject = historyService.createHistoricVariableInstanceQuery().id(varInstanceId).singleResult();

        if (varObject == null) {
            throw new ActivitiObjectNotFoundException("Historic variable instance '" + varInstanceId + "' couldn't be found.", VariableInstanceEntity.class);
        } else {
            return actResponseFactory.createRestVariable(varObject.getVariableName(), varObject.getValue(), null, varInstanceId, ActResponseFactory.VARIABLE_HISTORY_VARINSTANCE, includeBinary);
        }
    }



}
