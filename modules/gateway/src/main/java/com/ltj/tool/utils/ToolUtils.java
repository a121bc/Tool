package com.ltj.tool.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import static com.ltj.tool.model.support.ToolConst.FILE_SEPARATOR;

/**
 * Common utils
 *
 * @author Liu Tian Jun
 * @date 2021-03-02 11:37
 */

@Slf4j
public class ToolUtils {

    public static final String URL_SEPARATOR = "/";
    private static final String RE_HTML_MARK = "(<[^<]*?>)|(<[\\s]*?/[^<]*?>)|(<[^<]*?/[\\s]*?>)";

    @NonNull
    public static String ensureBoth(@NonNull String string, @NonNull String bothfix) {
        return ensureBoth(string, bothfix, bothfix);
    }

    @NonNull
    public static String ensureBoth(@NonNull String string, @NonNull String prefix, @NonNull String suffix) {
        return ensureSuffix(ensurePrefix(string, prefix), suffix);
    }

    /**
     * Ensures the string contain prefix.
     *
     * @param string string must not be blank
     * @param prefix prefix must not be blank
     * @return string contain prefix specified
     */
    @NonNull
    public static String ensurePrefix(@NonNull String string, @NonNull String prefix) {
        Assert.hasText(string, "String must not be blank");
        Assert.hasText(prefix, "Prefix must not be blank");

        return prefix + StringUtils.removeStart(string, prefix);
    }

    /**
     * Ensures the string contain suffix.
     *
     * @param string string must not be blank
     * @param suffix suffix must not be blank
     * @return string contain suffix specified
     */
    @NonNull
    public static String ensureSuffix(@NonNull String string, @NonNull String suffix) {
        Assert.hasText(string, "String must not be blank");
        Assert.hasText(suffix, "Suffix must not be blank");

        return StringUtils.removeEnd(string, suffix) + suffix;
    }

    /**
     * Composites partial url to full http url.
     *
     * @param partUrls partial urls must not be empty
     * @return full url
     */
    public static String compositeHttpUrl(@NonNull String... partUrls) {
        Assert.notEmpty(partUrls, "Partial url must not be blank");

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < partUrls.length; i++) {
            String partUrl = partUrls[i];
            if (StringUtils.isBlank(partUrl)) {
                continue;
            }
            partUrl = StringUtils.removeStart(partUrl, URL_SEPARATOR);
            partUrl = StringUtils.removeEnd(partUrl, URL_SEPARATOR);
            if (i != 0) {
                builder.append(URL_SEPARATOR);
            }
            builder.append(partUrl);
        }

        return builder.toString();
    }

    /**
     * Desensitizes the plain text.
     *
     * @param plainText plain text must not be null
     * @param leftSize  left size
     * @param rightSize right size
     * @return desensitization
     */
    public static String desensitize(@NonNull String plainText, int leftSize, int rightSize) {
        Assert.hasText(plainText, "Plain text must not be blank");

        if (leftSize < 0) {
            leftSize = 0;
        }

        if (leftSize > plainText.length()) {
            leftSize = plainText.length();
        }

        if (rightSize < 0) {
            rightSize = 0;
        }

        if (rightSize > plainText.length()) {
            rightSize = plainText.length();
        }

        if (plainText.length() < leftSize + rightSize) {
            rightSize = plainText.length() - leftSize;
        }

        int remainSize = plainText.length() - rightSize - leftSize;

        String left = StringUtils.left(plainText, leftSize);
        String right = StringUtils.right(plainText, rightSize);
        return StringUtils.rightPad(left, remainSize + leftSize, '*') + right;
    }

    /**
     * Changes file separator to url separator.
     *
     * @param pathname full path name must not be blank.
     * @return text with url separator
     */
    public static String changeFileSeparatorToUrlSeparator(@NonNull String pathname) {
        Assert.hasText(pathname, "Path name must not be blank");

        return pathname.replace(FILE_SEPARATOR, "/");
    }


}
