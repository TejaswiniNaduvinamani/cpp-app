package com.gfs.cpp.web.controller.customerinfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gfs.cpp.common.dto.clm.ClmContractDTO;
import com.gfs.cpp.component.customerinfo.CPPInformationService;

@RestController
@RequestMapping(value = "/customer")
public class CPPInformationController {

    @Autowired
    @Qualifier("cppInformationService")
    private CPPInformationService cppInformationService;

    @GetMapping(value = "/fetchContractPriceProfileInfo", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<ClmContractDTO> fetchContractPriceProfileInfo(@RequestParam String agreementId, @RequestParam String contractType) {
        ResponseEntity<ClmContractDTO> response = null;
        response = new ResponseEntity<>(cppInformationService.fetchContractPriceProfileInfo(agreementId, contractType), HttpStatus.OK);
        return response;
    }

}