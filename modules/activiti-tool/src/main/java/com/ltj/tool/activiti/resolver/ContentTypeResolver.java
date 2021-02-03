package com.ltj.tool.activiti.resolver;

/**
 * Interface describing a class that is capable of resolving the content type of a resource/file based on the resource name.
 *
 * @author Tijs Rademakers
 */
public interface ContentTypeResolver {

    /**
     * @return the content type resolved from the given resource name. Returns null if the content type cannot be resolved.
     */
    String resolveContentType(String resourceName);
}
