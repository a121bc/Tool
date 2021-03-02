package com.ltj.tool.model.support;

import java.io.File;

/**
 * 公共常量
 *
 * @author Liu Tian Jun
 * @date 2021-03-02 13:39
 */
public class ToolConst {

    /**
     * User home directory.
     */
    public final static String USER_HOME = System.getProperties().getProperty("user.home");

    /**
     * Temporary directory.
     */
    public final static String TEMP_DIR = "/tmp/run.tool.app";

    public final static String PROTOCOL_HTTPS = "https://";

    public final static String PROTOCOL_HTTP = "http://";

    public final static String URL_SEPARATOR = "/";

    /**
     * Tool backup prefix
     */
    public final static String TOOL_BACKUP_PREFIX = "tool-backup-";

    /**
     * Tool data export prefix
     */
    public final static String TOOL_DATA_EXPORT_PREFIX = "tool-data-export-";

    /**
     * Static pages pack prefix.
     */
    public final static String STATIC_PAGE_PACK_PREFIX = "static-pages-";

    /**
     * Default theme name.
     */
    public final static String DEFAULT_THEME_ID = "caicai_anatole";

    /**
     * Default error path.
     */
    public static final String DEFAULT_ERROR_PATH = "common/error/error";

    /**
     * Path separator.
     */
    public static final String FILE_SEPARATOR = File.separator;

}
