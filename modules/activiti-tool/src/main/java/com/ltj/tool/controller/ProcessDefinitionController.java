package com.ltj.tool.controller;

import com.ltj.tool.activiti.factory.ActResponseFactory;
import com.ltj.tool.activiti.repository.DeploymentResourceResponse;
import com.ltj.tool.activiti.repository.DeploymentResponse;
import com.ltj.tool.activiti.repository.ProcessDefinitionResponse;
import com.ltj.tool.activiti.resolver.ContentTypeResolver;
import lombok.RequiredArgsConstructor;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @describe： TODO
 * @Date: 2021-02-02 14:22
 */

@RestController
@RequestMapping("/repository")
@RequiredArgsConstructor
public class ProcessDefinitionController {

    protected final RepositoryService repositoryService;

    protected final ActResponseFactory actResponseFactory;

    protected final ContentTypeResolver contentTypeResolver;

    /**
     * @Description 查询最新流程定义
     * @param
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @author Liu Tian Jun
     * @date 15:26 2021-02-02 0002
     **/
    @GetMapping("/process-definitions")
    public Map<String, Object> getProcessDefinitions() {
        Map<String, Object> map = new HashMap<>();
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().latestVersion().list();
        List<ProcessDefinitionResponse> processDefinitionList = actResponseFactory.createProcessDefinitionResponseList(list);
        map.put("processDefinitionList",processDefinitionList);
        return map;
    }

    /**
     * @Description 根据流程定义id查询流程
     * @param processDefinitionId
     * @return com.ltj.tool.activiti.repository.ProcessDefinitionResponse
     * @author Liu Tian Jun
     * @date 15:56 2021-02-02 0002
     **/
    @GetMapping("/process-definitions/{processDefinitionId}")
    public ProcessDefinitionResponse getProcessDefinition(@PathVariable String processDefinitionId) {
        ProcessDefinition processDefinition = repositoryService.getProcessDefinition(processDefinitionId);
        if (processDefinition == null) {
            throw new ActivitiObjectNotFoundException("Could not find a process definition with id '" + processDefinitionId + "'.", ProcessDefinition.class);
        }
        return actResponseFactory.createProcessDefinitionResponse(processDefinition);
    }

    /**
     * @Description 根据deploymentId查询流程定义
     * @param deploymentId
     * @param request
     * @return DeploymentResponse
     * @author Liu Tian Jun
     * @date 16:00 2021-02-02 0002
     **/
    @GetMapping("/deployments/{deploymentId}")
    public DeploymentResponse getDeployment(@PathVariable String deploymentId, HttpServletRequest request) {
        Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();

        if (deployment == null) {
            throw new ActivitiObjectNotFoundException("Could not find a deployment with id '" + deploymentId + "'.", Deployment.class);
        }

        return actResponseFactory.createDeploymentResponse(deployment);
    }

    /**
     * @Description 获取路径资源
     * @param deploymentId
     * @param request
     * @return DeploymentResourceResponse
     * @author Liu Tian Jun
     * @date 16:27 2021-02-02 0002
     **/
    @RequestMapping(value = "/deployments/{deploymentId}/resources/**", method = RequestMethod.GET, produces = "application/json")
    public DeploymentResourceResponse getDeploymentResource(@PathVariable("deploymentId") String deploymentId, HttpServletRequest request) {

        Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
        if (deployment == null) {
            throw new ActivitiObjectNotFoundException("Could not find a deployment with id '" + deploymentId + "'.");
        }

        String pathInfo = request.getServletPath();
        String resourceName = pathInfo.replace("/repository/deployments/" + deploymentId + "/resources/", "")
                .replaceAll("/","\\\\")
                ;

        List<String> resourceList = repositoryService.getDeploymentResourceNames(deploymentId);

        if (resourceList.contains(resourceName)) {
            // Build resource representation
            DeploymentResourceResponse response = actResponseFactory.createDeploymentResourceResponse(deploymentId, resourceName, contentTypeResolver.resolveContentType(resourceName));
            return response;

        } else {
            // Resource not found in deployment
            throw new ActivitiObjectNotFoundException("Could not find a resource with id '" + resourceName + "' in deployment '" + deploymentId + "'.");
        }
    }

    @RequestMapping(value = "/deployments/{deploymentId}/resourcedata/**", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getDeploymentResource(@PathVariable("deploymentId") String deploymentId, HttpServletRequest request, HttpServletResponse response) {
        String pathInfo = request.getServletPath();
        String resourceName = pathInfo.replace("/repository/deployments/" + deploymentId + "/resourcedata/", "")
                .replaceAll("/","\\\\")
                ;
        if (deploymentId == null) {
            throw new ActivitiIllegalArgumentException("No deployment id provided");
        }

        // Check if deployment exists
        Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
        if (deployment == null) {
            throw new ActivitiObjectNotFoundException("Could not find a deployment with id '" + deploymentId + "'.", Deployment.class);
        }

        List<String> resourceList = repositoryService.getDeploymentResourceNames(deploymentId);

        if (resourceList.contains(resourceName)) {
            final InputStream resourceStream = repositoryService.getResourceAsStream(deploymentId, resourceName);

            String contentType = contentTypeResolver.resolveContentType(resourceName);

            try {
                byte[] bytes = IOUtils.toByteArray(resourceStream);
                ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();
                bodyBuilder.contentLength(bytes.length);
                bodyBuilder.contentType(MediaType.valueOf(contentType));
                return bodyBuilder.body(bytes);
            } catch (Exception e) {
                throw new ActivitiException("Error converting resource stream", e);
            }
        } else {
            // Resource not found in deployment
            throw new ActivitiObjectNotFoundException("Could not find a resource with name '" + resourceName + "' in deployment '" + deploymentId + "'.", String.class);
        }
    }
}
