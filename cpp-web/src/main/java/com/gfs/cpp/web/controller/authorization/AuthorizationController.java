package com.gfs.cpp.web.controller.authorization;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gfs.cpp.common.service.authorization.AuthorizationDetailsDTO;
import com.gfs.cpp.component.authorization.AuthorizationService;

@RestController
@RequestMapping(value = "/authorization")
public class AuthorizationController {

    @Autowired
    private AuthorizationService authorizationService;

    public static final Logger logger = LoggerFactory.getLogger(AuthorizationController.class);

    @GetMapping(value = "/fetchAuthorizationDetails", params = { "contractPriceProfileSeq", "isAmendment", "clmStatus" }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<AuthorizationDetailsDTO> fetchAuthorizationDetails(@RequestParam int contractPriceProfileSeq,
            @RequestParam boolean isAmendment, @RequestParam String clmStatus) {

        return new ResponseEntity<>(authorizationService.buildAuthorizationDetails(contractPriceProfileSeq, isAmendment, clmStatus), HttpStatus.OK);

    }

    @GetMapping(value = "/canEditFurtherance", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Map<String, Boolean>> canEditFurtherance(@RequestParam int furtheranceSeq) {

        Map<String, Boolean> response = new HashMap<>();

        response.put("canEditFurtherance", authorizationService.canEditFurtherance(furtheranceSeq));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    
    @GetMapping(value = "/isUserAuthorizedToViewOnly", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Map<String, Boolean>> isUserAuthorizedToViewOnly() {

        Map<String, Boolean> response = new HashMap<>();

        response.put("isUserAuthorizedToViewOnly", authorizationService.isUserAuthorizedToViewOnly());

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}
