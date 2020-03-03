package com.gfs.cpp.web.controller.splitcase;

import java.util.Date;
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

import com.gfs.cpp.common.dto.splitcase.SplitCaseDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseGridDTO;
import com.gfs.cpp.component.splitcase.SplitCaseService;
import com.gfs.cpp.component.userdetails.CppUserDetailsService;

@RestController
@RequestMapping(value = "/splitcase")
public class SplitCaseController {

    @Autowired
    @Qualifier("splitCaseService")
    SplitCaseService splitCaseService;

    @Autowired
    CppUserDetailsService gfsUserDetailsService;

    @GetMapping(value = "/fetchSplitCase", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<List<SplitCaseDTO>> fetchSplitCaseFee(@RequestParam Integer contractPriceProfileId, @RequestParam Date pricingEffectiveDate,
            @RequestParam Date pricingExpirationDate) {
        SplitCaseGridDTO splitCaseGridDTO = splitCaseService.fetchSplitCaseFee(contractPriceProfileId, pricingEffectiveDate, pricingExpirationDate);
        return new ResponseEntity<>(splitCaseGridDTO.getSplitCaseFeeValues(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole(T(com.gfs.cpp.component.userdetails.RoleConstants).POWER_USER, "
            + "T(com.gfs.cpp.component.userdetails.RoleConstants).ACCOUNT_MANAGER )")
    @PostMapping(value = "/saveSplitCaseFee", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<String> saveSplitCase(@RequestBody SplitCaseGridDTO splitCaseDTO) {
        String userName = gfsUserDetailsService.getCurrentUserId();
        splitCaseService.saveOrUpdateSplitCase(splitCaseDTO, userName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
