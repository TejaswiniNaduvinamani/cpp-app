package com.gfs.cpp.web.controller.costmodel;

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
import org.springframework.web.bind.annotation.RestController;

import com.gfs.cpp.common.dto.costmodel.CostModelResponseDTO;
import com.gfs.cpp.common.dto.markup.PrcProfNonBrktCstMdlDTO;
import com.gfs.cpp.component.costmodel.CostModelService;
import com.gfs.cpp.component.userdetails.CppUserDetailsService;

@RestController
@RequestMapping(value = "/costModel")
public class CostModelController {

    @Autowired
    @Qualifier("costModelService")
    private CostModelService costModelService;

    @Autowired
    CppUserDetailsService gfsUserDetailsService;

    @GetMapping(value = "/fetchAllActiveCostModels", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<List<CostModelResponseDTO>> fetchAllActiveCostModels() {
        return new ResponseEntity<>(costModelService.fetchAllActiveCostModels(), HttpStatus.OK);
    }

    @GetMapping(value = "/fetchCostGrid", produces = { MediaType.APPLICATION_JSON_VALUE })
    public List<PrcProfNonBrktCstMdlDTO> fetchCostModelForGrid(int contractPriceProfileSeq) {
        return costModelService.fetchProductTypeAndCostModel(contractPriceProfileSeq);
    }

    @PreAuthorize("hasAnyRole(T(com.gfs.cpp.component.userdetails.RoleConstants).POWER_USER, "
            + "T(com.gfs.cpp.component.userdetails.RoleConstants).ACCOUNT_MANAGER )")
    @PostMapping(value = "/saveCostGrid", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<String> saveUpdatedCostModel(@RequestBody List<PrcProfNonBrktCstMdlDTO> costModelTypeDTOList) {
        String userId = gfsUserDetailsService.getCurrentUserId();
        costModelService.updateCostModel(costModelTypeDTOList, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
