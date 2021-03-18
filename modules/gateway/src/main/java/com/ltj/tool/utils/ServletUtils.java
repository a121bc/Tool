package com.ltj.tool.utils;

import cn.hutool.extra.servlet.ServletUtil;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Servlet utilities
 *
 * @author Liu Tian Jun
 * @date 2021-03-18 13:43
 */
public class ServletUtils {

    private ServletUtils() {
    }

    /**
     * Gets current http servlet request.
     *
     * @return java.util.Optional<javax.servlet.http.HttpServletRequest>
     **/
    @NonNull
    public static Optional<HttpServletRequest> getCurrentRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(requestAttributes -> requestAttributes instanceof ServletRequestAttributes)
                .map(requestAttributes -> (ServletRequestAttributes)requestAttributes)
                .map(ServletRequestAttributes::getRequest);
    }

    /**
     * Gets request ip.
     *
     * @return ip address or null
     */
    @Nullable
    public static String getRequestIp() {
        return  getCurrentRequest()
                .map(ServletUtil::getClientIP)
                .orElse(null);
    }

    @Nullable
    public static String getHeaderIgnoreCase(String header) {
        return getCurrentRequest()
                .map(request -> ServletUtil.getHeaderIgnoreCase(request, header))
                .orElse(null);
    }
}
