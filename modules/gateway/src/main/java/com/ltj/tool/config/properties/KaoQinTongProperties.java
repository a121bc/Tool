package com.ltj.tool.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 考勤通 properties
 *
 * @author Liu Tian Jun
 * @date 2021-03-18 09:57
 */

@Data
@ConfigurationProperties("kaoqintong")
public class KaoQinTongProperties {

    private String idKey;

    private String sKey;
}
