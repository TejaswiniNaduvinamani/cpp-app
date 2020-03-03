package com.gfs.cpp.web.controller.distributioncenter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.distributioncenter.DistributionCenterDTO;
import com.gfs.cpp.common.dto.distributioncenter.DistributionCenterSaveDTO;
import com.gfs.cpp.common.model.distributioncenter.DistributionCenterDetailDO;
import com.gfs.cpp.component.distributioncenter.DistributionCenterService;
import com.gfs.cpp.component.userdetails.CppUserDetailsService;

@RestController
@RequestMapping(value = "/distributionCenter")
public class DistributionCenterController {

    @Autowired
    @Qualifier("distributionCenterService")
    DistributionCenterService distributionCenterService;

    @Autowired
    CppUserDetailsService gfsUserDetailsService;

    @GetMapping(value = "/distributionCentersForCompany", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<List<DistributionCenterDTO>> fetchDistributionCenters() {
        return new ResponseEntity<>(distributionCenterService.fetchAllDistirbutionCenters(CPPConstants.COMPANY_NUMBER, CPPConstants.STATUS_CODE,
                CPPConstants.LANGUAGE_CODE), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole(T(com.gfs.cpp.component.userdetails.RoleConstants).POWER_USER, "
            + "T(com.gfs.cpp.component.userdetails.RoleConstants).ACCOUNT_MANAGER )")
    @PostMapping(value = "/saveDistributionCenters", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<String> saveDistributionCenters(@RequestBody DistributionCenterSaveDTO distributionCenterSaveDTO) {
        String userName = gfsUserDetailsService.getCurrentUserId();
        distributionCenterService.saveDistributionCenters(distributionCenterSaveDTO, userName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/fetchSavedDistributionCenters", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<List<DistributionCenterDetailDO>> fetchSavedDistributionCenters(@RequestParam Integer contractPriceProfileSeq) {
        return new ResponseEntity<>(distributionCenterService.fetchSavedDistributionCenters(contractPriceProfileSeq), HttpStatus.OK);
    }
}
