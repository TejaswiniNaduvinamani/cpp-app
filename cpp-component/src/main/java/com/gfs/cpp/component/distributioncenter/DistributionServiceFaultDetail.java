package com.gfs.cpp.component.distributioncenter;

import java.io.Serializable;

public class DistributionServiceFaultDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String errorMessage;
    private String requestID;

    public DistributionServiceFaultDetail(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public DistributionServiceFaultDetail() {
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }
}
