package com.gfs.cpp.acceptanceTests.stepdefs.clmRedirect;

import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getAttribute;
import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.setAttribute;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.web.controller.clm.ClmRedirectController;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ClmRedirectStepDefs {
	
	@Autowired
	private ClmRedirectController clmRedirectController;
	
    @When("^a request is made to get the url that returns to clm$")
    public void request_fetch_clm_url() {
        final ResponseEntity<Map<String, String>> response = clmRedirectController.returnToClm();
        setAttribute("response", response.getBody());
    }

    @Then("^the url is returned that points to clm$")
    public void return_clm_url() {
        final Map<String, String> response = (Map<String, String>) getAttribute("response");
        assertThat(response.get(CPPConstants.CLM_URL_KEY), is("https://gfsdev.icertis.com/Agreement/Details?entityName="));
    }


}
