package com.gfs.cpp.web.controller.search;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gfs.cpp.common.dto.search.ContractSearchResultDTO;
import com.gfs.cpp.component.search.ContractSearchService;

@RestController
@RequestMapping(value = "/search")
public class ContractSearchController {

    @Autowired
    private ContractSearchService contractSearchService;

    @GetMapping(value = "/searchContractsByContractName", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<List<ContractSearchResultDTO>> searchContractsByContractName(@RequestParam String searchContractName) {
        return new ResponseEntity<>(contractSearchService.searchContractsByContractName(searchContractName), HttpStatus.OK);
    }

    @GetMapping(value = "/searchContractsByCustomer", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<List<ContractSearchResultDTO>> searchContractsByCustomer(@RequestParam String gfsCustomerId,
            @RequestParam int gfsCustomerTypeCode) {
        return new ResponseEntity<>(contractSearchService.searchContractsByCustomer(gfsCustomerId, gfsCustomerTypeCode), HttpStatus.OK);
    }

    @GetMapping(value = "/searchContractsByCPPId", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<List<ContractSearchResultDTO>> searchContractsByCPPId(@RequestParam long cppId) {
        return new ResponseEntity<>(contractSearchService.searchContractsByCPPId(cppId), HttpStatus.OK);
    }

}
