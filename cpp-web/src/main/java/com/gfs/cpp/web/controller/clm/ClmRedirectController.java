package com.gfs.cpp.web.controller.clm;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gfs.cpp.common.constants.CPPConstants;


@RestController
@RequestMapping(value = "/clm")
public class ClmRedirectController {

	
    @Value("${clm.url}")
    private String clmUrl;
	
    public static final Logger logger = LoggerFactory.getLogger(ClmRedirectController.class);
	
    @GetMapping(value = "/returnToClm", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Map<String, String>> returnToClm() {
        ResponseEntity<Map<String, String>> response = null;
        Map<String, String> clmUrlMap = new HashMap<>();
            clmUrlMap.put(CPPConstants.CLM_URL_KEY, clmUrl);
            response = new ResponseEntity<>(clmUrlMap, HttpStatus.OK);
        return response;
    }
	
}
