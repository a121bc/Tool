package com.ltj.tool.cache.lock;

import java.lang.annotation.*;

/**
 * Cache parameter annotation.
 *
 * @author Liu Tian Jun
 * @date 2021-03-18 10:41
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CacheParem {

}
