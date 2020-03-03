package com.gfs.cpp.component.distributioncenter;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import com.gfs.cpp.common.dto.distributioncenter.DistributionCenterSOAPResponseDTO;

@WebService(targetNamespace = "http://my.gfs.com/200810/IDistributionCenterQuery")
public interface DistributionSOAPClientService {
    
    @WebResult(name = "return", targetNamespace = "")
    public List<DistributionCenterSOAPResponseDTO> getShipDCsForCompanyByLangTypeCode(
        @WebParam(name = "companyNumber", targetNamespace = "") Integer companyNumber,
        @WebParam(name = "statusCodes", targetNamespace = "") List<java.lang.String> statusCodes,
        @WebParam(name = "languageTypeCode", targetNamespace = "") String languageTypeCode
    ) throws DistributionServiceException;

}
