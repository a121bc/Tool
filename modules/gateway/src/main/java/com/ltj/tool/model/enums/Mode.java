package com.ltj.tool.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;

/**
 * Tool runtime mode
 *
 * @author Liu Tian Jun
 * @date 2021-03-02 11:05
 */
public enum Mode {

    /**
     * Production mode
     */
    PRODUCTION,

    /**
     * Develop mode
     */
    DEVELOPMENT,

    /**
     * Demo mode
     */
    DEMO,

    /**
     * Test mode
     */
    TEST;

    /**
     * Get mode from value.
     *
     * @param value mode value
     * @return runtime mode
     **/
    @JsonCreator
    public static Mode valueFrom(@Nullable String value) {
        Mode modeResult = null;
        for (Mode mode : values()) {
            if (mode.name().equalsIgnoreCase(value)) {
                modeResult = mode;
                break;
            }
        }
        if (modeResult == null) {
            modeResult = PRODUCTION;
        }
        return modeResult;
    }

    @JsonValue
    String getValue() {
        return this.name().toLowerCase();
    }
}
