package com.gfs.cpp.web.controller.activatepricing;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.component.activatepricing.ActivatePricingService;
import com.gfs.cpp.component.contractpricing.ContractPricingService;
import com.gfs.cpp.component.userdetails.CppUserDetailsService;

@RestController
@RequestMapping(value = "/activatePricing")
public class ActivatePricingController {

    @Autowired
    @Qualifier("contractPricingService")
    ContractPricingService contractPricingService;

    @Autowired
    @Qualifier("activatePricingService")
    ActivatePricingService activatePricingService;

    @Autowired
    CppUserDetailsService gfsUserDetailsService;

    @PreAuthorize("hasRole(T(com.gfs.cpp.component.userdetails.RoleConstants).POWER_USER)")
    @GetMapping(value = "/activatePricingForCustomer", params = { "contractPriceProfileSeq", "isAmendment", "clmContractStatus" }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Map<String, String>> activatePricing(@RequestParam int contractPriceProfileSeq, @RequestParam boolean isAmendment,
            @RequestParam String clmContractStatus) {
        Map<String, String> activatePricingResponseMap = new HashMap<>();
        String userId = gfsUserDetailsService.getCurrentUserId();
        activatePricingService.activatePricing(contractPriceProfileSeq, userId, isAmendment, clmContractStatus);
        activatePricingResponseMap.put(CPPConstants.MESSAGE, "Pricing activated for the contract");
        return new ResponseEntity<>(activatePricingResponseMap, HttpStatus.OK);
    }

    @PreAuthorize("hasRole(T(com.gfs.cpp.component.userdetails.RoleConstants).POWER_USER)")
    @GetMapping(value = "/enableActivatePricing", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Map<String, Boolean>> enableActivatePricing(@RequestParam int contractPriceProfileSeq) {

        return new ResponseEntity<>(activatePricingService.validateActivatePricingEnabler(contractPriceProfileSeq), HttpStatus.OK);

    }
}
