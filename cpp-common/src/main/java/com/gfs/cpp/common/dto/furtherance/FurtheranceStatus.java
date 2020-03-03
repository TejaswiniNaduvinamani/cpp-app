package com.gfs.cpp.common.dto.furtherance;

public enum FurtheranceStatus {

    FURTHERANCE_INITIATED(1), FURTHERANCE_SAVED(2), FURTHERANCE_ACTIVATED(3);

    public final int code;

    FurtheranceStatus(int code) {
        this.code = code;
    }

    public final int getCode() {
        return code;
    }

}
