package com.ltj.tool.controller;

import com.ltj.tool.activiti.factory.ActResponseFactory;
import com.ltj.tool.activiti.history.HistoricActivityInstanceResponse;
import com.ltj.tool.activiti.history.HistoricProcessInstanceResponse;
import lombok.RequiredArgsConstructor;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @describe： TODO
 * @Date: 2021-02-03 10:10
 */
@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
public class HistoricProcessController {

    protected final ActResponseFactory actResponseFactory;
    protected final HistoryService historyService;

    /**
     * @Description 查询流程
     * @param
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @author Liu Tian Jun
     * @date 10:31 2021-02-03 0003
     **/
    @GetMapping("/historic-process-instances")
    public Map<String, Object> queryProcessInstances(@RequestParam(defaultValue = "1") int start, @RequestParam(defaultValue = "10")int size) {
        Map<String, Object> map = new HashMap<>(1);
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery()
                .listPage(start,size);
        List<HistoricProcessInstanceResponse> historicProcessInstanceList = actResponseFactory.createHistoricProcessInstanceResponseList(list);
        map.put("historicProcessInstanceList",historicProcessInstanceList);
        return map;
    }

    /**
     * @Description 根据流程实例id获取信息
     * @param processInstanceId
     * @param request
     * @return com.ltj.tool.activiti.runtime.history.HistoricProcessInstanceResponse
     * @author Liu Tian Jun
     * @date 10:20 2021-02-03 0003
     **/
    @GetMapping("/historic-process-instances/{processInstanceId}")
    public HistoricProcessInstanceResponse getProcessInstance( @PathVariable String processInstanceId, HttpServletRequest request) {
        HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery()
//                .superProcessInstanceId(processInstanceId)
                .processInstanceId(processInstanceId)
                .singleResult();
        if (processInstance == null) {
            throw new ActivitiObjectNotFoundException("Could not find a process instance with id '" + processInstanceId + "'.", HistoricProcessInstance.class);
        }
        return actResponseFactory.createHistoricProcessInstanceResponse(processInstance);
    }

    /**
     * @Description 根据流程实例id获取审批人信息
     * @param
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @author Liu Tian Jun
     * @date 10:57 2021-02-03 0003
     **/
    @GetMapping("/historic-activity-instances/{processInstanceId}")
    public Map<String, Object> queryActivityInstances(@PathVariable String processInstanceId) {
        Map<String, Object> map = new HashMap<>(1);
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByActivityId().asc()
                .list();
        List<HistoricActivityInstanceResponse> historicActivityInstanceList = actResponseFactory.createHistoricActivityInstanceResponseList(list);
        map.put("historicActivityInstanceList",historicActivityInstanceList);
        return map;
    }

}
