package com.ltj.tool.activiti.factory;

import com.ltj.tool.activiti.history.HistoricActivityInstanceResponse;
import com.ltj.tool.activiti.repository.DeploymentResourceResponse;
import com.ltj.tool.activiti.repository.DeploymentResponse;
import com.ltj.tool.activiti.repository.ProcessDefinitionResponse;
import com.ltj.tool.activiti.resolver.ContentTypeResolver;
import com.ltj.tool.activiti.history.HistoricProcessInstanceResponse;
import com.ltj.tool.activiti.history.HistoricTaskInstanceResponse;
import com.ltj.tool.activiti.runtime.process.ProcessInstanceResponse;
import com.ltj.tool.activiti.runtime.task.TaskResponse;
import com.ltj.tool.activiti.utils.RestUrlBuilder;
import com.ltj.tool.activiti.utils.RestUrls;
import com.ltj.tool.activiti.variable.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.bpmn.deployer.ResourceNameUtil;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @describe： TODO
 * @Date: 2021-01-29 09:55
 */

public class ActResponseFactory {

    public static final int VARIABLE_TASK = 1;
    public static final int VARIABLE_EXECUTION = 2;
    public static final int VARIABLE_PROCESS = 3;
    public static final int VARIABLE_HISTORY_TASK = 4;
    public static final int VARIABLE_HISTORY_PROCESS = 5;
    public static final int VARIABLE_HISTORY_VARINSTANCE = 6;
    public static final int VARIABLE_HISTORY_DETAIL = 7;

    public static final String BYTE_ARRAY_VARIABLE_TYPE = "binary";
    public static final String SERIALIZABLE_VARIABLE_TYPE = "serializable";

    protected List<RestVariableConverter> variableConverters = new ArrayList<>();

    public ActResponseFactory() {
        initializeVariableConverters();
    }

    public List<DeploymentResourceResponse> createDeploymentResourceResponseList(String deploymentId, List<String> resourceList, ContentTypeResolver contentTypeResolver) {
        RestUrlBuilder urlBuilder = createUrlBuilder();
        // Add additional metadata to the artifact-strings before returning
        List<DeploymentResourceResponse> responseList = new ArrayList<DeploymentResourceResponse>();
        for (String resourceId : resourceList) {
            responseList.add(createDeploymentResourceResponse(deploymentId, resourceId, contentTypeResolver.resolveContentType(resourceId), urlBuilder));
        }
        return responseList;
    }

    public DeploymentResourceResponse createDeploymentResourceResponse(String deploymentId, String resourceId, String contentType) {
        return createDeploymentResourceResponse(deploymentId, resourceId, contentType, createUrlBuilder());
    }

    public DeploymentResourceResponse createDeploymentResourceResponse(String deploymentId, String resourceId, String contentType, RestUrlBuilder urlBuilder) {
        // Create URL's
        String resourceUrl = urlBuilder.buildUrl(RestUrls.URL_DEPLOYMENT_RESOURCE, deploymentId, resourceId);
        String resourceContentUrl = urlBuilder.buildUrl(RestUrls.URL_DEPLOYMENT_RESOURCE_CONTENT, deploymentId, resourceId);

        // Determine type
        String type = "resource";
        for (String suffix : ResourceNameUtil.BPMN_RESOURCE_SUFFIXES) {
            if (resourceId.endsWith(suffix)) {
                type = "processDefinition";
                break;
            }
        }
        return new DeploymentResourceResponse(resourceId, resourceUrl, resourceContentUrl, contentType, type);
    }

    public List<ProcessDefinitionResponse> createProcessDefinitionResponseList(List<ProcessDefinition> processDefinitions) {
        RestUrlBuilder urlBuilder = createUrlBuilder();
        List<ProcessDefinitionResponse> responseList = new ArrayList<ProcessDefinitionResponse>();
        for (ProcessDefinition instance : processDefinitions) {
            responseList.add(createProcessDefinitionResponse(instance, urlBuilder));
        }
        return responseList;
    }

    public ProcessDefinitionResponse createProcessDefinitionResponse(ProcessDefinition processDefinition) {
        return createProcessDefinitionResponse(processDefinition, createUrlBuilder());
    }

    public ProcessDefinitionResponse createProcessDefinitionResponse(ProcessDefinition processDefinition, RestUrlBuilder urlBuilder) {
        ProcessDefinitionResponse response = new ProcessDefinitionResponse();
        response.setUrl(urlBuilder.buildUrl(RestUrls.URL_PROCESS_DEFINITION, processDefinition.getId()));
        response.setId(processDefinition.getId());
        response.setKey(processDefinition.getKey());
        response.setVersion(processDefinition.getVersion());
        response.setCategory(processDefinition.getCategory());
        response.setName(processDefinition.getName());
        response.setDescription(processDefinition.getDescription());
        response.setSuspended(processDefinition.isSuspended());
        response.setStartFormDefined(processDefinition.hasStartFormKey());
        response.setGraphicalNotationDefined(processDefinition.hasGraphicalNotation());
        response.setTenantId(processDefinition.getTenantId());

        // Links to other resources
        response.setDeploymentId(processDefinition.getDeploymentId());
        response.setDeploymentUrl(urlBuilder.buildUrl(RestUrls.URL_DEPLOYMENT, processDefinition.getDeploymentId()));
        response.setResource(urlBuilder.buildUrl(RestUrls.URL_DEPLOYMENT_RESOURCE, processDefinition.getDeploymentId(), processDefinition.getResourceName()));
        if (processDefinition.getDiagramResourceName() != null) {
            response.setDiagramResource(urlBuilder.buildUrl(RestUrls.URL_DEPLOYMENT_RESOURCE, processDefinition.getDeploymentId(), processDefinition.getDiagramResourceName()));
        }
        return response;
    }

    public List<HistoricActivityInstanceResponse> createHistoricActivityInstanceResponseList(List<HistoricActivityInstance> activityInstances) {
        RestUrlBuilder urlBuilder = createUrlBuilder();
        List<HistoricActivityInstanceResponse> responseList = new ArrayList<HistoricActivityInstanceResponse>();
        for (HistoricActivityInstance instance : activityInstances) {
            responseList.add(createHistoricActivityInstanceResponse(instance, urlBuilder));
        }
        return responseList;
    }

    public HistoricActivityInstanceResponse createHistoricActivityInstanceResponse(HistoricActivityInstance activityInstance) {
        return createHistoricActivityInstanceResponse(activityInstance, createUrlBuilder());
    }

    public HistoricActivityInstanceResponse createHistoricActivityInstanceResponse(HistoricActivityInstance activityInstance, RestUrlBuilder urlBuilder) {
        HistoricActivityInstanceResponse result = new HistoricActivityInstanceResponse();
        result.setActivityId(activityInstance.getActivityId());
        result.setActivityName(activityInstance.getActivityName());
        result.setActivityType(activityInstance.getActivityType());
        result.setAssignee(activityInstance.getAssignee());
        result.setCalledProcessInstanceId(activityInstance.getCalledProcessInstanceId());
        result.setDurationInMillis(activityInstance.getDurationInMillis());
        result.setEndTime(activityInstance.getEndTime());
        result.setExecutionId(activityInstance.getExecutionId());
        result.setId(activityInstance.getId());
        result.setProcessDefinitionId(activityInstance.getProcessDefinitionId());
        result.setProcessDefinitionUrl(urlBuilder.buildUrl(RestUrls.URL_PROCESS_DEFINITION, activityInstance.getProcessDefinitionId()));
        result.setProcessInstanceId(activityInstance.getProcessInstanceId());
        result.setProcessInstanceUrl(urlBuilder.buildUrl(RestUrls.URL_HISTORIC_PROCESS_INSTANCE, activityInstance.getProcessInstanceId()));
        result.setStartTime(activityInstance.getStartTime());
        result.setTaskId(activityInstance.getTaskId());
        result.setTenantId(activityInstance.getTenantId());
        return result;
    }

    public List<DeploymentResponse> createDeploymentResponseList(List<Deployment> deployments) {
        RestUrlBuilder urlBuilder = createUrlBuilder();
        List<DeploymentResponse> responseList = new ArrayList<DeploymentResponse>();
        for (Deployment instance : deployments) {
            responseList.add(createDeploymentResponse(instance, urlBuilder));
        }
        return responseList;
    }

    public DeploymentResponse createDeploymentResponse(Deployment deployment) {
        return createDeploymentResponse(deployment, createUrlBuilder());
    }

    public DeploymentResponse createDeploymentResponse(Deployment deployment, RestUrlBuilder urlBuilder) {
        return new DeploymentResponse(deployment, urlBuilder.buildUrl(RestUrls.URL_DEPLOYMENT, deployment.getId()));
    }

    public List<TaskResponse> createTaskResponseList(List<Task> tasks) {
        RestUrlBuilder urlBuilder = createUrlBuilder();
        List<TaskResponse> responseList = new ArrayList<TaskResponse>();
        for (Task instance : tasks) {
            responseList.add(createTaskResponse(instance, urlBuilder));
        }
        return responseList;
    }

    public TaskResponse createTaskResponse(Task task) {
        return createTaskResponse(task, createUrlBuilder());
    }

    public TaskResponse createTaskResponse(Task task, RestUrlBuilder urlBuilder) {
        TaskResponse response = new TaskResponse(task);
        response.setUrl(urlBuilder.buildUrl(RestUrls.URL_TASK, task.getId()));

        // Add references to other resources, if needed
        if (response.getParentTaskId() != null) {
            response.setParentTaskUrl(urlBuilder.buildUrl(RestUrls.URL_TASK, response.getParentTaskId()));
        }
        if (response.getProcessDefinitionId() != null) {
            response.setProcessDefinitionUrl(urlBuilder.buildUrl(RestUrls.URL_PROCESS_DEFINITION, response.getProcessDefinitionId()));
        }
        if (response.getExecutionId() != null) {
            response.setExecutionUrl(urlBuilder.buildUrl(RestUrls.URL_EXECUTION, response.getExecutionId()));
        }
        if (response.getProcessInstanceId() != null) {
            response.setProcessInstanceUrl(urlBuilder.buildUrl(RestUrls.URL_PROCESS_INSTANCE, response.getProcessInstanceId()));
        }

        if (task.getProcessVariables() != null) {
            Map<String, Object> variableMap = task.getProcessVariables();
            for (String name : variableMap.keySet()) {
                response.addVariable(createRestVariable(name, variableMap.get(name), RestVariable.RestVariableScope.GLOBAL, task.getId(), VARIABLE_TASK, false, urlBuilder));
            }
        }
        if (task.getTaskLocalVariables() != null) {
            Map<String, Object> variableMap = task.getTaskLocalVariables();
            for (String name : variableMap.keySet()) {
                response.addVariable(createRestVariable(name, variableMap.get(name), RestVariable.RestVariableScope.LOCAL, task.getId(), VARIABLE_TASK, false, urlBuilder));
            }
        }

        return response;
    }

    public List<ProcessInstanceResponse> createProcessInstanceResponseList(List<ProcessInstance> processInstances) {
        RestUrlBuilder urlBuilder = createUrlBuilder();
        List<ProcessInstanceResponse> responseList = new ArrayList<ProcessInstanceResponse>();
        for (ProcessInstance instance : processInstances) {
            responseList.add(createProcessInstanceResponse(instance, urlBuilder));
        }
        return responseList;
    }

    public ProcessInstanceResponse createProcessInstanceResponse(ProcessInstance processInstance) {
        return createProcessInstanceResponse(processInstance, createUrlBuilder());
    }

    public ProcessInstanceResponse createProcessInstanceResponse(ProcessInstance processInstance, RestUrlBuilder urlBuilder) {
        ProcessInstanceResponse result = new ProcessInstanceResponse();
        result.setActivityId(processInstance.getActivityId());
        result.setBusinessKey(processInstance.getBusinessKey());
        result.setId(processInstance.getId());
        result.setName(processInstance.getName());
        result.setProcessDefinitionId(processInstance.getProcessDefinitionId());
        result.setProcessDefinitionUrl(urlBuilder.buildUrl(RestUrls.URL_PROCESS_DEFINITION, processInstance.getProcessDefinitionId()));
        result.setProcessDefinitionKey(processInstance.getProcessDefinitionKey());
        result.setEnded(processInstance.isEnded());
        result.setSuspended(processInstance.isSuspended());
        result.setUrl(urlBuilder.buildUrl(new String[]{"getDiagram","{0}"}, processInstance.getId()));
        result.setTenantId(processInstance.getTenantId());

        // Added by Ryan Johnston
        if (processInstance.isEnded()) {
            // Process complete. Note the same in the result.
            result.setCompleted(true);
        } else {
            // Process not complete. Note the same in the result.
            result.setCompleted(false);
        }
        // End Added by Ryan Johnston

        if (processInstance.getProcessVariables() != null) {
            Map<String, Object> variableMap = processInstance.getProcessVariables();
            for (String name : variableMap.keySet()) {
                result.addVariable(createRestVariable(name, variableMap.get(name), RestVariable.RestVariableScope.LOCAL, processInstance.getId(), VARIABLE_PROCESS, false, urlBuilder));
            }
        }

        return result;
    }

    public List<HistoricProcessInstanceResponse> createHistoricProcessInstanceResponseList(List<HistoricProcessInstance> processInstances) {
        RestUrlBuilder urlBuilder = createUrlBuilder();
        List<HistoricProcessInstanceResponse> responseList = new ArrayList<HistoricProcessInstanceResponse>();
        for (HistoricProcessInstance instance : processInstances) {
            responseList.add(createHistoricProcessInstanceResponse(instance, urlBuilder));
        }
        return responseList;
    }

    public HistoricProcessInstanceResponse createHistoricProcessInstanceResponse(HistoricProcessInstance processInstance) {
        return createHistoricProcessInstanceResponse(processInstance, createUrlBuilder());
    }

    @SuppressWarnings("deprecation")
    public HistoricProcessInstanceResponse createHistoricProcessInstanceResponse(HistoricProcessInstance processInstance, RestUrlBuilder urlBuilder) {
        HistoricProcessInstanceResponse result = new HistoricProcessInstanceResponse();
        result.setBusinessKey(processInstance.getBusinessKey());
        result.setDeleteReason(processInstance.getDeleteReason());
        result.setDurationInMillis(processInstance.getDurationInMillis());
        result.setEndActivityId(processInstance.getEndActivityId());
        result.setEndTime(processInstance.getEndTime());
        result.setId(processInstance.getId());
        result.setName(processInstance.getName());
        result.setProcessDefinitionId(processInstance.getProcessDefinitionId());
        result.setProcessDefinitionUrl(urlBuilder.buildUrl(RestUrls.URL_PROCESS_DEFINITION, processInstance.getProcessDefinitionId()));
        result.setStartActivityId(processInstance.getStartActivityId());
        result.setStartTime(processInstance.getStartTime());
        result.setStartUserId(processInstance.getStartUserId());
        result.setSuperProcessInstanceId(processInstance.getSuperProcessInstanceId());
        result.setUrl(urlBuilder.buildUrl(RestUrls.URL_HISTORIC_PROCESS_INSTANCE, processInstance.getId()));
        if (processInstance.getProcessVariables() != null) {
            Map<String, Object> variableMap = processInstance.getProcessVariables();
            for (String name : variableMap.keySet()) {
                result.addVariable(createRestVariable(name, variableMap.get(name), RestVariable.RestVariableScope.LOCAL, processInstance.getId(), VARIABLE_HISTORY_PROCESS, false, urlBuilder));
            }
        }
        result.setTenantId(processInstance.getTenantId());
        return result;
    }

    public RestVariable createRestVariable(String name, Object value, RestVariable.RestVariableScope scope, String id, int variableType, boolean includeBinaryValue) {
        return createRestVariable(name, value, scope, id, variableType, includeBinaryValue, createUrlBuilder());
    }

    public RestVariable createRestVariable(String name, Object value, RestVariable.RestVariableScope scope, String id, int variableType, boolean includeBinaryValue, RestUrlBuilder urlBuilder) {

        RestVariableConverter converter = null;
        RestVariable restVar = new RestVariable();
        restVar.setVariableScope(scope);
        restVar.setName(name);

        if (value != null) {
            // Try converting the value
            for (RestVariableConverter c : variableConverters) {
                if (c.getVariableType().isAssignableFrom(value.getClass())) {
                    converter = c;
                    break;
                }
            }

            if (converter != null) {
                converter.convertVariableValue(value, restVar);
                restVar.setType(converter.getRestTypeName());
            } else {
                // Revert to default conversion, which is the
                // serializable/byte-array form
                if (value instanceof Byte[] || value instanceof byte[]) {
                    restVar.setType(BYTE_ARRAY_VARIABLE_TYPE);
                } else {
                    restVar.setType(SERIALIZABLE_VARIABLE_TYPE);
                }

                if (includeBinaryValue) {
                    restVar.setValue(value);
                }

                if (variableType == VARIABLE_TASK) {
                    restVar.setValueUrl(urlBuilder.buildUrl(RestUrls.URL_TASK_VARIABLE_DATA, id, name));
                } else if (variableType == VARIABLE_EXECUTION) {
                    restVar.setValueUrl(urlBuilder.buildUrl(RestUrls.URL_EXECUTION_VARIABLE_DATA, id, name));
                } else if (variableType == VARIABLE_PROCESS) {
                    restVar.setValueUrl(urlBuilder.buildUrl(RestUrls.URL_PROCESS_INSTANCE_VARIABLE_DATA, id, name));
                } else if (variableType == VARIABLE_HISTORY_TASK) {
                    restVar.setValueUrl(urlBuilder.buildUrl(RestUrls.URL_HISTORIC_TASK_INSTANCE_VARIABLE_DATA, id, name));
                } else if (variableType == VARIABLE_HISTORY_PROCESS) {
                    restVar.setValueUrl(urlBuilder.buildUrl(RestUrls.URL_HISTORIC_PROCESS_INSTANCE_VARIABLE_DATA, id, name));
                } else if (variableType == VARIABLE_HISTORY_VARINSTANCE) {
                    restVar.setValueUrl(urlBuilder.buildUrl(RestUrls.URL_HISTORIC_VARIABLE_INSTANCE_DATA, id));
                } else if (variableType == VARIABLE_HISTORY_DETAIL) {
                    restVar.setValueUrl(urlBuilder.buildUrl(RestUrls.URL_HISTORIC_DETAIL_VARIABLE_DATA, id));
                }
            }
        }
        return restVar;
    }

    public List<HistoricTaskInstanceResponse> createHistoricTaskInstanceResponseList(List<HistoricTaskInstance> taskInstances) {
        RestUrlBuilder urlBuilder = createUrlBuilder();
        List<HistoricTaskInstanceResponse> responseList = new ArrayList<HistoricTaskInstanceResponse>();
        for (HistoricTaskInstance instance : taskInstances) {
            responseList.add(createHistoricTaskInstanceResponse(instance, urlBuilder));
        }
        return responseList;
    }

    public HistoricTaskInstanceResponse createHistoricTaskInstanceResponse(HistoricTaskInstance taskInstance) {
        return createHistoricTaskInstanceResponse(taskInstance, createUrlBuilder());
    }

    public HistoricTaskInstanceResponse createHistoricTaskInstanceResponse(HistoricTaskInstance taskInstance, RestUrlBuilder urlBuilder) {
        HistoricTaskInstanceResponse result = new HistoricTaskInstanceResponse();
        result.setAssignee(taskInstance.getAssignee());
        result.setClaimTime(taskInstance.getClaimTime());
        result.setDeleteReason(taskInstance.getDeleteReason());
        result.setDescription(taskInstance.getDescription());
        result.setDueDate(taskInstance.getDueDate());
        result.setDurationInMillis(taskInstance.getDurationInMillis());
        result.setEndTime(taskInstance.getEndTime());
        result.setExecutionId(taskInstance.getExecutionId());
        result.setFormKey(taskInstance.getFormKey());
        result.setId(taskInstance.getId());
        result.setName(taskInstance.getName());
        result.setOwner(taskInstance.getOwner());
        result.setParentTaskId(taskInstance.getParentTaskId());
        result.setPriority(taskInstance.getPriority());
        result.setProcessDefinitionId(taskInstance.getProcessDefinitionId());
        result.setTenantId(taskInstance.getTenantId());
        result.setCategory(taskInstance.getCategory());
        if (taskInstance.getProcessDefinitionId() != null) {
            result.setProcessDefinitionUrl(urlBuilder.buildUrl(RestUrls.URL_PROCESS_DEFINITION, taskInstance.getProcessDefinitionId()));
        }
        result.setProcessInstanceId(taskInstance.getProcessInstanceId());
        if (taskInstance.getProcessInstanceId() != null) {
            result.setProcessInstanceUrl(urlBuilder.buildUrl(RestUrls.URL_HISTORIC_PROCESS_INSTANCE, taskInstance.getProcessInstanceId()));
        }
        result.setStartTime(taskInstance.getStartTime());
        result.setTaskDefinitionKey(taskInstance.getTaskDefinitionKey());
        result.setWorkTimeInMillis(taskInstance.getWorkTimeInMillis());
        result.setUrl(urlBuilder.buildUrl(RestUrls.URL_HISTORIC_TASK_INSTANCE, taskInstance.getId()));
        if (taskInstance.getProcessVariables() != null) {
            Map<String, Object> variableMap = taskInstance.getProcessVariables();
            for (String name : variableMap.keySet()) {
                result.addVariable(createRestVariable(name, variableMap.get(name), RestVariable.RestVariableScope.GLOBAL, taskInstance.getId(), VARIABLE_HISTORY_TASK, false, urlBuilder));
            }
        }
        if (taskInstance.getTaskLocalVariables() != null) {
            Map<String, Object> variableMap = taskInstance.getTaskLocalVariables();
            for (String name : variableMap.keySet()) {
                result.addVariable(createRestVariable(name, variableMap.get(name), RestVariable.RestVariableScope.LOCAL, taskInstance.getId(), VARIABLE_HISTORY_TASK, false, urlBuilder));
            }
        }

        return result;
    }

    protected void initializeVariableConverters() {
        variableConverters.add(new StringRestVariableConverter());
        variableConverters.add(new IntegerRestVariableConverter());
        variableConverters.add(new LongRestVariableConverter());
        variableConverters.add(new ShortRestVariableConverter());
        variableConverters.add(new DoubleRestVariableConverter());
        variableConverters.add(new BooleanRestVariableConverter());
        variableConverters.add(new DateRestVariableConverter());
    }

    protected RestUrlBuilder createUrlBuilder() {
        return RestUrlBuilder.fromCurrentRequest();
    }
}
