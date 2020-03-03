package com.gfs.cpp.component.distributioncenter;

import javax.xml.ws.WebFault;

@WebFault(name = "FaultDetail", targetNamespace = "http://my.gfs.com/200810/IDistributionCenterQuery")
public class DistributionServiceException extends Exception {

    private static final long serialVersionUID = 1L;

    private final DistributionServiceFaultDetail faultDetail;

    public DistributionServiceException( DistributionServiceFaultDetail faultDetail) {
        super(faultDetail.getErrorMessage());
        this.faultDetail = faultDetail;
    }

    public DistributionServiceException( DistributionServiceFaultDetail faultDetail, Throwable cause) {
        super(faultDetail.getErrorMessage(), cause);
        this.faultDetail = faultDetail;
    }

    public DistributionServiceFaultDetail getFaultInfo() {
        return faultDetail;
    }
}