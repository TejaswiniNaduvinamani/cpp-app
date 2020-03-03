package com.gfs.cpp.proxy.clm;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class UpdateCppUrlRequestDataBuilderTest {

    private String CPP_APP_URL = "cppAppUrl/";

    @InjectMocks
    @Spy
    private UpdateCppUrlRequestDataBuilder target;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(target, "cppAppUrl", CPP_APP_URL);
    }

    @Test
    public void shouldBuildCppUrlWithAgreementIdAndContractType() throws Exception {

        String agreementId = "18436c04-de60-422d-bab6-b03d6930604c";
        String contractType = "ICMDistributionAgreementRegional";

        String actualCppUrl = target.buildCppLink(agreementId, contractType, UpdateCppUrlRequestDataBuilder.LAUNCH_CPP_TXT,
                UpdateCppUrlRequestDataBuilder.CPP_RESOURCE);

        String expectedcppUrl = CPP_APP_URL + UpdateCppUrlRequestDataBuilder.CPP_RESOURCE + "?" + "agreementId=" + agreementId + "&contractType="
                + contractType;

        assertThat(actualCppUrl, equalTo("<a target='_blank' class='urlStyle' href='" + expectedcppUrl + "'>Launch CPP</a>"));
    }

    @Test
    public void shouldBuildCppUrlWithAgreementIdAndContractTypeForAmendment() throws Exception {

        String agreementId = "18436c04-de60-422d-bab6-b03d6930604c";
        String contractType = "ICMDistributionAgreementRegional";

        String actualCppUrl = target.buildCppLink(agreementId, contractType, UpdateCppUrlRequestDataBuilder.LAUNCH_CPP_AMENDMENT_TXT,
                UpdateCppUrlRequestDataBuilder.CPP_RESOURCE);

        String expectedcppUrl = CPP_APP_URL + UpdateCppUrlRequestDataBuilder.CPP_RESOURCE + "?" + "agreementId=" + agreementId + "&contractType="
                + contractType;

        assertThat(actualCppUrl, equalTo("<a target='_blank' class='urlStyle' href='" + expectedcppUrl + "'>"
                + UpdateCppUrlRequestDataBuilder.LAUNCH_CPP_AMENDMENT_TXT + "</a>"));
    }

    @Test
    public void shouldBuildCppUrlWithAgreementIdAndContractTypeForFurtherance() throws Exception {

        String agreementId = "18436c04-de60-422d-bab6-b03d6930604c";
        String contractType = "ICMDistributionAgreementRegional";

        String actualCppUrl = target.buildCppLink(agreementId, contractType, UpdateCppUrlRequestDataBuilder.LAUNCH_FURTHERANCE_TXT,
                UpdateCppUrlRequestDataBuilder.CPP_FURTHERANCE_RESOURCE);

        String expectedcppUrl = CPP_APP_URL + UpdateCppUrlRequestDataBuilder.CPP_FURTHERANCE_RESOURCE + "?" + "agreementId=" + agreementId
                + "&contractType=" + contractType;

        assertThat(actualCppUrl, equalTo("<a target='_blank' class='urlStyle' href='" + expectedcppUrl + "'>"
                + UpdateCppUrlRequestDataBuilder.LAUNCH_FURTHERANCE_TXT + "</a>"));
    }

    @Test
    public void shouldBuildUpdateRequestWithAllData() throws Exception {
        String agreementId = "18436c04-de60-422d-bab6-b03d6930604c";
        String contractType = "ICMDistributionAgreementRegional";
        String cppUrl = "launch cpp";

        String agreementData = "{\"ICMIsPricingRequired\":\"Yes\",\"ICMCPPLink\":\"url\",\"ICMIsAmendment\":false}";

        doReturn(cppUrl).when(target).buildCppLink(agreementId, contractType, UpdateCppUrlRequestDataBuilder.LAUNCH_CPP_TXT,
                UpdateCppUrlRequestDataBuilder.CPP_RESOURCE);

        String actualRequest = target.buildUpdateUrlRequestBody(agreementData, agreementId, contractType, false);

        JSONObject actualMetaData = new JSONObject(formatClmResponse(actualRequest));

        assertThat(actualMetaData.get(UpdateCppUrlRequestDataBuilder.ICMCPP_LINK_KEY).toString(), equalTo(cppUrl));

    }

    @Test
    public void shouldUpdateFurtheranceLinkToDefaultWhenFoundCppFurtheranceTxt() throws Exception {
        String agreementId = "18436c04-de60-422d-bab6-b03d6930604c";
        String contractType = "ICMDistributionAgreementRegional";
        String cppUrl = "launch cpp";

        String agreementData = "{\"ICMIsPricingRequired\":\"Yes\",\"ICMCPPLink\":\"url\",\"ICMIsAmendment\":false,\""
                + UpdateCppUrlRequestDataBuilder.ICMCPP_FURTHERANCE_LINK_KEY + "\":\"" + UpdateCppUrlRequestDataBuilder.CPP_FURTHERANCE_RESOURCE
                + "\"}";

        doReturn(cppUrl).when(target).buildCppLink(agreementId, contractType, UpdateCppUrlRequestDataBuilder.LAUNCH_CPP_TXT,
                UpdateCppUrlRequestDataBuilder.CPP_RESOURCE);

        String actualRequest = target.buildUpdateUrlRequestBody(agreementData, agreementId, contractType, false);

        JSONObject actualMetaData = new JSONObject(formatClmResponse(actualRequest));
        String updatedFurtheranceUrl = actualMetaData.get(UpdateCppUrlRequestDataBuilder.ICMCPP_FURTHERANCE_LINK_KEY).toString();

        assertThat(updatedFurtheranceUrl.contains(UpdateCppUrlRequestDataBuilder.DEFAULT_LINK), equalTo(true));
        assertThat(updatedFurtheranceUrl.contains(UpdateCppUrlRequestDataBuilder.DEFAULT_TEXT), equalTo(true));

    }

    private String formatClmResponse(String response) {
        String responseBody = StringUtils.defaultIfEmpty(response, StringUtils.EMPTY).replaceAll("\\\\\"", "\"");
        responseBody = StringUtils.defaultIfEmpty(responseBody, StringUtils.EMPTY).replaceAll("\\\\\\\\", "\\\\");
        int indexOf = responseBody.indexOf('{');
        responseBody = responseBody.substring(indexOf);
        return responseBody;
    }

    @Test
    public void shouldBuildUpdateRequestWithAllDataForAmendment() throws Exception {
        String agreementId = "18436c04-de60-422d-bab6-b03d6930604c";
        String contractType = "ICMDistributionAgreementRegional";
        String cppUrl = "launch cpp";

        String agreementData = "{\"ICMIsPricingRequired\":\"Yes\",\"ICMCPPAmendmentLink\":\"url\",\"ICMIsAmendment\":true}";
        doReturn(cppUrl).when(target).buildCppLink(agreementId, contractType, UpdateCppUrlRequestDataBuilder.LAUNCH_CPP_AMENDMENT_TXT,
                UpdateCppUrlRequestDataBuilder.CPP_RESOURCE);

        String actualRequest = target.buildUpdateUrlRequestBody(agreementData, agreementId, contractType, true);

        JSONObject actualMetaData = new JSONObject(formatClmResponse(actualRequest));

        assertThat(actualMetaData.get(UpdateCppUrlRequestDataBuilder.ICMCPP_AMENDMENT_LINK_KEY).toString(), equalTo(cppUrl));

    }

    @Test
    public void shouldBuildUpdateRequestWithFurtheranceURL() throws Exception {
        String agreementId = "18436c04-de60-422d-bab6-b03d6930604c";
        String contractType = "ICMDistributionAgreementRegional";
        String cppUrl = "launch furtherance";

        String agreementData = "{\"ICMIsPricingRequired\":\"Yes\",\"ICMCPPLink\":\"url\",\"ICMIsAmendment\":false}";

        doReturn(cppUrl).when(target).buildCppLink(agreementId, contractType, UpdateCppUrlRequestDataBuilder.LAUNCH_FURTHERANCE_TXT,
                UpdateCppUrlRequestDataBuilder.CPP_FURTHERANCE_RESOURCE);

        String actualRequest = target.buildFurtheranceUrlRequestBody(agreementData, agreementId, contractType);

        JSONObject actualMetaData = new JSONObject(formatClmResponse(actualRequest));

        assertThat(actualMetaData.get(UpdateCppUrlRequestDataBuilder.ICMCPP_FURTHERANCE_LINK_KEY).toString(), equalTo(cppUrl));

        verify(target).buildCppLink(agreementId, contractType, UpdateCppUrlRequestDataBuilder.LAUNCH_FURTHERANCE_TXT,
                UpdateCppUrlRequestDataBuilder.CPP_FURTHERANCE_RESOURCE);

    }

}
