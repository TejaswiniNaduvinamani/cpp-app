package com.gfs.cpp.proxy;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;

import com.gfs.cpp.common.dto.costmodel.CostModelDTO;

@WebService(targetNamespace = "http://my.gfs.com/201505/ModeledCostQueryService", name = "modeledCostQueryService")
public interface ModeledCostQueryServiceProxy {

    @WebMethod
    @RequestWrapper(localName = "lookupAllActiveCostModels", targetNamespace = "http://my.gfs.com/201505/ModeledCostQueryService")
    @WebResult(name = "activeCostModels", targetNamespace = "")
    public java.util.List<CostModelDTO> lookupAllActiveCostModels();
}
