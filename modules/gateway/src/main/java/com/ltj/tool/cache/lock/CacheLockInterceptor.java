package com.ltj.tool.cache.lock;

import com.ltj.tool.cache.AbstractStringCacheStore;
import com.ltj.tool.exception.FrequentAccessException;
import com.ltj.tool.exception.ServiceException;
import com.ltj.tool.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;

/**
 * Interceptor for cache lock annotation.
 *
 * @author Liu Tian Jun
 * @date 2021-03-18 11:26
 */

@Slf4j
@Aspect
@Configuration
public class CacheLockInterceptor {

    private final static String CACHE_LOCK_PREFOX = "cache_lock_";

    private final static String CACHE_LOCK_VALUE = "locked";

    private final AbstractStringCacheStore cacheStore;

    public CacheLockInterceptor(AbstractStringCacheStore cacheStore) {
        this.cacheStore = cacheStore;
    }

    @Around("@annotation(com.ltj.tool.cache.lock.CacheLock)")
    public Object interceptCacheLock(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取被注解的方法
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        log.debug("Starting locking: [{}]", methodSignature.toString());

        //获取CacheLock注解的实例
        CacheLock cacheLock = methodSignature.getMethod().getAnnotation(CacheLock.class);

        // 获取缓存key
        String cacheLockKey = buildCacheLockKey(cacheLock, joinPoint);

        log.debug("Nuilt lock key: [{}]", cacheLockKey);

        try {
            // Get form cache
            Boolean cacheRequest = cacheStore.putIfAbsent(cacheLockKey, CACHE_LOCK_VALUE, cacheLock.expired(), cacheLock.timeUnit());

            if (cacheRequest == null) {
                throw new ServiceException("Unknown reason of cache " + cacheLockKey).setErrorData(cacheLockKey);
            }

            if (!cacheRequest) {
                throw new FrequentAccessException("访问过于频繁，请稍后再试！").setErrorData(cacheLockKey);
            }

            // Proceed the method
            return joinPoint.proceed();
        } finally {
            // Delete the cache
            if (cacheLock.autoDelete()) {
                cacheStore.delete(cacheLockKey);
                log.debug("Deleted the cache lock: [{}]", cacheLock);
            }
        }
    }

    private String buildCacheLockKey(@NonNull CacheLock cacheLock, @NonNull ProceedingJoinPoint joinPoint) {
        Assert.notNull(cacheLock,"Cache lock must not be null");
        Assert.notNull(joinPoint, "Proceeding join point must not be null");

        //获取被注解的方法
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        StringBuilder cacheKeyBuilder = new StringBuilder(CACHE_LOCK_PREFOX);

        String delimiter = cacheLock.delimiter();

        if (StringUtils.isNotBlank(cacheLock.prefix())) {
            cacheKeyBuilder.append(cacheLock.prefix());
        } else {
            cacheKeyBuilder.append(methodSignature.getMethod().toString());
        }

        Annotation[][] parameterAnnotations = methodSignature.getMethod().getParameterAnnotations();

        for (int i = 0; i < parameterAnnotations.length; i++) {
            log.debug("Parameter annotation[{}] = {}", i, parameterAnnotations[i]);

            for (int j = 0; j < parameterAnnotations[i].length; j++) {
                Annotation annotation = parameterAnnotations[i][j];
                log.debug("Parameter annotation[{}][{}]: {}", i, j, annotation);
                if (annotation instanceof CacheLock) {
                    // Get current argument
                    Object arg = joinPoint.getArgs()[i];
                    log.debug("Cache param args: [{}]", arg);

                    // Append to the cache key
                    cacheKeyBuilder.append(delimiter).append(arg.toString());
                }
            }
        }

        if (cacheLock.traceRequest()) {
            // Append http request info
            cacheKeyBuilder.append(delimiter).append(ServletUtils.getRequestIp());
        }
        return cacheKeyBuilder.toString();

    }


}
