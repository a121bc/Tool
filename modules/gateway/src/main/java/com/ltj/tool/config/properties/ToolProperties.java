package com.ltj.tool.config.properties;

import com.ltj.tool.model.enums.Mode;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.ltj.tool.model.support.ToolConst.*;
import static com.ltj.tool.utils.ToolUtils.ensureSuffix;

/**
 * Tool configuration properties.
 *
 * @author Liu Tian Jun
 * @date 2021-03-02 10:50
 */

@Data
@ConfigurationProperties("tool")
public class ToolProperties {

    /**
     * Doc api disabled. (Default is true)
     */
    private boolean docDisabled = true;

    /**
     * Production env. (Default is true)
     */
    private boolean productionEnv = true;

    /**
     * Authentication enabled
     */
    private boolean authEnabled = true;

    /**
     * Tool startup mode.
     */
    private Mode mode = Mode.PRODUCTION;

    /**
     * Admin path.
     */
    private String adminPath = "admin";

    /**
     * Work directory.
     */
    private String workDir = ensureSuffix(USER_HOME, FILE_SEPARATOR) + ".tool" + FILE_SEPARATOR;

    /**
     * Tool backup directory.(Not recommended to modify this config);
     */
    private String backupDir = ensureSuffix(TEMP_DIR, FILE_SEPARATOR) + "tool-backup" + FILE_SEPARATOR;

    /**
     * Tool data export directory.
     */
    private String dataExportDir = ensureSuffix(TEMP_DIR, FILE_SEPARATOR) + "tool-data-export" + FILE_SEPARATOR;

    /**
     * Upload prefix.
     */
    private String uploadUrlPrefix = "upload";

    /**
     * Download Timeout.
     */
    private Duration downloadTimeout = Duration.ofSeconds(30);

    /**
     * cache store impl
     * memory
     * level
     */
    private String cache = "memory";

    private List<String> cacheRedisNodes = new ArrayList<>();

    private String cacheRedisPassword = "";

    /**
     * hazelcast cache store impl
     * memory
     * level
     */
    private List<String> hazelcastMembers = new ArrayList<>();

    private String hazelcastGroupName;

    private int initialBackoffSeconds = 5;
}
