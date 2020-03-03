package com.gfs.cpp.common.util;

public enum MarkupTypeEnum {
    SELL_UNIT("Sell Unit", 1), PER_CASE("Per Case", 2), PER_WEIGHT("Per Weight", 3);

    private final String value;
    private final Integer markupType;

    MarkupTypeEnum(final String value, final Integer markupType) {
        this.value = value;
        this.markupType = markupType;
    }

    public Integer getMarkupType() {
        return markupType;
    }

    public String getValue() {
        return value;
    }

    public static String getNameByCode(Integer markupType) {
        for (MarkupTypeEnum markupTypeEnum : values()) {
            if (markupTypeEnum.markupType.equals(markupType)) {
                return markupTypeEnum.value;
            }
        }
        return null;
    }

}
