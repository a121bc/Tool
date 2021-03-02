package com.ltj.tool.cache;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 *  TODO
 * @date 2021-02-26 14:33
 * @author Liu Tian Jun
 */

@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CacheWrapper<V> implements Serializable {

    /**
     * Cache data
     */
    private V data;

    /**
     * Expired time.
     */
    private Date expireAt;

    /**
     * Create time.
     */
    private Date createAt;
}
