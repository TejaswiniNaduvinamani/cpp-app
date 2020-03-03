package com.gfs.cpp.web.controller.contractpricing;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingDTO;
import com.gfs.cpp.component.contractpricing.ContractPricingService;
import com.gfs.cpp.component.userdetails.CppUserDetailsService;

@RestController
@RequestMapping(value = "/contractPricing")
public class ContractPricingController {

    @Autowired
    @Qualifier("contractPricingService")
    ContractPricingService contractPricingService;

    @Autowired
    CppUserDetailsService gfsUserDetailsService;

    @PreAuthorize("hasAnyRole(T(com.gfs.cpp.component.userdetails.RoleConstants).POWER_USER, "
            + "T(com.gfs.cpp.component.userdetails.RoleConstants).ACCOUNT_MANAGER )")
    @PostMapping(value = "/savePricingInformation", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Map<String, Integer>> saveContractPricing(@RequestBody ContractPricingDTO contractPricingDTO) {
        String userName = gfsUserDetailsService.getCurrentUserId();
        contractPricingService.saveContractPricing(contractPricingDTO, userName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/fetchCPPSequence", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Map<String, Integer>> fetchCPPSequence(@RequestParam int contractPriceProfileId) {

        Map<String, Integer> cppSequenceJSONMap = new HashMap<>();
        cppSequenceJSONMap.put(CPPConstants.CONTRACT_PRICE_PROFILE_SEQ, contractPricingService.fetchCPPSequence(contractPriceProfileId));
        return new ResponseEntity<>(cppSequenceJSONMap, HttpStatus.OK);
    }

    @GetMapping(value = "/fetchPricingInformation", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<ContractPricingDTO> fetchPricingInformation(@RequestParam int contractPriceProfileSeq, @RequestParam String agreementId,
            @RequestParam String contractStatus) {
        return new ResponseEntity<>(contractPricingService.fetchPricingInformation(contractPriceProfileSeq, agreementId, contractStatus),
                HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole(T(com.gfs.cpp.component.userdetails.RoleConstants).POWER_USER, "
            + "T(com.gfs.cpp.component.userdetails.RoleConstants).ACCOUNT_MANAGER )")
    @DeleteMapping(value = "/deletePricingExhibit", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Void> deletePricingExhibit(@RequestParam String agreementId) {
        String userName = gfsUserDetailsService.getCurrentUserId();
        contractPricingService.deletePricingExhibit(agreementId, userName);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
