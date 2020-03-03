package com.gfs.cpp.common.dto.furtherance;

public enum FurtheranceAction {

    ADDED(1), UPDATED(2), DELETED(3);

    public final int code;

    FurtheranceAction(int code) {
        this.code = code;
    }

    public final int getCode() {
        return code;
    }
}
