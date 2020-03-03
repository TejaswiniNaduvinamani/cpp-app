package com.gfs.cpp.acceptanceTests.common.data;

public enum FurtheranceAction {

    ADDED(1), UPDATED(2), DELETED(3);

    int code;

    FurtheranceAction(int code) {
        this.code = code;
    }

    public final int getFurtheranceActionCode() {
        return code;
    }
}
