package com.ltj.tool.activiti.resolver;

/**
 * Default implementation of a {@link ContentTypeResolver}, resolving a limited set of well-known content types used in the engine.
 *
 * @author Tijs Rademakers
 */
public class DefaultContentTypeResolver implements ContentTypeResolver {

    @Override
    public String resolveContentType(String resourceName) {
        String contentType = null;
        if (resourceName != null && !resourceName.isEmpty()) {
            String lowerResourceName = resourceName.toLowerCase();

            if (lowerResourceName.endsWith("png")) {
                contentType = "image/png";
            } else if (lowerResourceName.endsWith("xml") || lowerResourceName.endsWith("bpmn")) {
                contentType = "text/xml";
            }
        }
        return contentType;
    }
}
