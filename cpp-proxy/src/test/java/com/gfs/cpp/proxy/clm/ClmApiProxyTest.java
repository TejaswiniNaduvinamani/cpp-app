package com.gfs.cpp.proxy.clm;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfs.cpp.common.dto.clm.ClmContractResponseDTO;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.proxy.clm.FileRemover;

@RunWith(MockitoJUnitRunner.class)
public class ClmApiProxyTest {

    @InjectMocks
    @Spy
    private ClmApiProxy target;

    private String ICM_HOST_URL = "host_url";
    private String ICM_AUTH_TOKEN = "authtoken";
    private int LOGGED_IN_USERID = 321;
    static final String EXHIBIT_DOCUMENT_TYPE = "ICMCPPExhibit";
    static final String FURTHERANCE_DOCUMENT_TYPE = "ICMCPPFurtherance";

    @Mock
    private RestTemplate clmRestTemplate;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private SaveAssociatedDocumentRequestBuilder saveAssociatedDocumentRequestBuilder;
    @Mock
    private UpdateCppUrlRequestDataBuilder updateCppUrlRequestDataBuilder;
    @Mock
    private File exhibitDocument;
    @Mock
    private ClmApplicationTokenFetcher clmApplicationTokenFetcher;
    @Mock
    private FileRemover fileRemover;
    @Captor
    private ArgumentCaptor<HttpEntity<String>> httpEntityCaptor;
    @Captor
    private ArgumentCaptor<Map<String, Object>> urlVariableCaptor;
    @Captor
    private ArgumentCaptor<JSONObject> jsonObjectCaptor;

    @Before
    public void setup() {

        ReflectionTestUtils.setField(target, "icmHostUrl", ICM_HOST_URL);
        ReflectionTestUtils.setField(target, "cppExhibitTypeName", EXHIBIT_DOCUMENT_TYPE);
        ReflectionTestUtils.setField(target, "cppFurtheranceExhibitTypeName", FURTHERANCE_DOCUMENT_TYPE);
        doReturn(LOGGED_IN_USERID).when(clmApplicationTokenFetcher).getLoggedInUserId();
    }

    @Test
    public void shouldGetAgreementData() throws Exception {

        String agreementId = "18436c04-de60-422d-bab6-b03d6930604c";
        String contractType = "ICMDistributionAgreementRegional";
        ClmContractResponseDTO response = new ClmContractResponseDTO();

        String agreementMetaData = "agreementMetaData";

        doReturn(agreementMetaData).when(target).getAgreementMetaData(agreementId, contractType);
        doReturn(response).when(objectMapper).readValue(agreementMetaData, ClmContractResponseDTO.class);

        assertThat(target.getAgreementData(agreementId, contractType), equalTo(response));

        verify(target).getAgreementMetaData(agreementId, contractType);
        verify(objectMapper).readValue(agreementMetaData, ClmContractResponseDTO.class);
        verify(target, never()).getParentAgreementId(anyString(), anyString());
    }

    @Test(expected = CPPRuntimeException.class)
    public void shouldThrowCppRuntimeExceptionWhenReadValueFails() throws Exception {

        String agreementId = "18436c04-de60-422d-bab6-b03d6930604c";
        String contractType = "ICMDistributionAgreementRegional";
        ClmContractResponseDTO response = new ClmContractResponseDTO();

        String agreementMetaData = "agreementMetaData";

        doReturn(agreementMetaData).when(target).getAgreementMetaData(agreementId, contractType);
        doThrow(IOException.class).when(objectMapper).readValue(agreementMetaData, ClmContractResponseDTO.class);

        assertThat(target.getAgreementData(agreementId, contractType), equalTo(response));

    }

    @Test
    public void shoudlUpdateCppUrlWhenPricingRequiredIsYes() throws Exception {

        String agreementId = "18436c04-de60-422d-bab6-b03d6930604c";
        String contractType = "ICMDistributionAgreementRegional";
        String updateRequestBody = "updateRequestBody";

        String agreementMetaData = "{\"ICMIsPricingRequired\":\"Yes\",\"ICMCPPLink\":\"url\",\"ICMIsAmendment\":false}";
        doReturn(agreementMetaData).when(target).getAgreementMetaData(agreementId, contractType);
        doReturn(updateRequestBody).when(updateCppUrlRequestDataBuilder).buildUpdateUrlRequestBody(agreementMetaData, agreementId, contractType,
                false);

        target.updateCppUrlForPricingContract(agreementId, contractType);

        verify(clmRestTemplate).exchange(eq(ICM_HOST_URL + ClmApiProxy.UPDATE_AGREEMENT_URL), eq(HttpMethod.PUT), httpEntityCaptor.capture(),
                eq(String.class));

        HttpEntity<String> actualRequestBodyEntity = httpEntityCaptor.getValue();
        assertThat(actualRequestBodyEntity.getBody(), equalTo(updateRequestBody));

        verify(updateCppUrlRequestDataBuilder).buildUpdateUrlRequestBody(agreementMetaData, agreementId, contractType, false);

    }

    @Test
    public void shoudlUpdateCppUrlWhenPricingRequiredIsYesForAmendment() throws Exception {

        String agreementId = "18436c04-de60-422d-bab6-b03d6930604c";
        String contractType = "ICMDistributionAgreementRegional";
        String updateRequestBody = "updateRequestBody";

        String agreementMetaData = "{\"ICMIsPricingRequired\":\"No\",\"ICMWillthisamendmentchangepricing2\":yes,\"ICMCPPLink\":\"url\",\"ICMIsAmendment\":true}";

        doReturn(agreementMetaData).when(target).getAgreementMetaData(agreementId, contractType);
        doReturn(updateRequestBody).when(updateCppUrlRequestDataBuilder).buildUpdateUrlRequestBody(agreementMetaData, agreementId, contractType,
                true);

        target.updateCppUrlForPricingContract(agreementId, contractType);

        verify(clmRestTemplate).exchange(eq(ICM_HOST_URL + ClmApiProxy.UPDATE_AGREEMENT_URL), eq(HttpMethod.PUT), httpEntityCaptor.capture(),
                eq(String.class));

        HttpEntity<String> actualRequestBodyEntity = httpEntityCaptor.getValue();
        assertThat(actualRequestBodyEntity.getBody(), equalTo(updateRequestBody));

        verify(updateCppUrlRequestDataBuilder).buildUpdateUrlRequestBody(agreementMetaData, agreementId, contractType, true);

    }

    @Test
    public void shoudlNotUpdateCppUrlWhenPricingRequiredIsNoForAmendment() throws Exception {

        String agreementId = "18436c04-de60-422d-bab6-b03d6930604c";
        String contractType = "ICMDistributionAgreementRegional";
        String updateRequestBody = "updateRequestBody";

        String agreementMetaData = "{\"ICMIsPricingRequired\":\"Yes\",\"ICMWillthisamendmentchangepricing2\":no,\"ICMCPPLink\":\"url\",\"ICMIsAmendment\":true}";

        doReturn(agreementMetaData).when(target).getAgreementMetaData(agreementId, contractType);
        doReturn(updateRequestBody).when(updateCppUrlRequestDataBuilder).buildUpdateUrlRequestBody(agreementMetaData, agreementId, contractType,
                true);

        target.updateCppUrlForPricingContract(agreementId, contractType);

        verify(clmRestTemplate, never()).exchange(anyString(), eq(HttpMethod.PUT), httpEntityCaptor.capture(), eq(String.class),
                urlVariableCaptor.capture());

    }

    @Test
    public void shoudlUpdateCppUrlWhenPricingRequiredIsNotAvailableForGPOContract() throws Exception {

        String agreementId = "18436c04-de60-422d-bab6-b03d6930604c";
        String contractType = "ICMGPOMasterAgreement";
        String updateRequestBody = "updateRequestBody";

        String agreementMetaData = "{\"ICMIsAmendment\":false,\"ICMCPPLink\":\"url\"}";
        doReturn(agreementMetaData).when(target).getAgreementMetaData(agreementId, contractType);
        doReturn(updateRequestBody).when(updateCppUrlRequestDataBuilder).buildUpdateUrlRequestBody(agreementMetaData, agreementId, contractType,
                false);

        target.updateCppUrlForPricingContract(agreementId, contractType);

        verify(clmRestTemplate).exchange(eq(ICM_HOST_URL + ClmApiProxy.UPDATE_AGREEMENT_URL), eq(HttpMethod.PUT), httpEntityCaptor.capture(),
                eq(String.class));

        HttpEntity<String> actualRequestBodyEntity = httpEntityCaptor.getValue();
        assertThat(actualRequestBodyEntity.getBody(), equalTo(updateRequestBody));

        verify(updateCppUrlRequestDataBuilder).buildUpdateUrlRequestBody(agreementMetaData, agreementId, contractType, false);

    }

    @Test
    public void shouldUpdateFUrtheranceUrl() throws Exception {
        String agreementId = "18436c04-de60-422d-bab6-b03d6930604c";
        String contractType = "ICMGPOMasterAgreement";
        String updateRequestBody = "updateRequestBody";

        String agreementMetaData = "{\"ICMIsAmendment\":false,\"ICMCPPLink\":\"url\"}";
        doReturn(agreementMetaData).when(target).getAgreementMetaData(agreementId, contractType);
        doReturn(updateRequestBody).when(updateCppUrlRequestDataBuilder).buildFurtheranceUrlRequestBody(agreementMetaData, agreementId, contractType);

        target.updateFurtheranceUrlForPricingContract(agreementId, contractType);

        verify(clmRestTemplate).exchange(eq(ICM_HOST_URL + ClmApiProxy.UPDATE_AGREEMENT_URL), eq(HttpMethod.PUT), httpEntityCaptor.capture(),
                eq(String.class));

        HttpEntity<String> actualRequestBodyEntity = httpEntityCaptor.getValue();
        assertThat(actualRequestBodyEntity.getBody(), equalTo(updateRequestBody));

        verify(updateCppUrlRequestDataBuilder).buildFurtheranceUrlRequestBody(agreementMetaData, agreementId, contractType);

    }

    @Test
    public void shoudlUpdateCppUrlWhenPricingRequiredIsNotAvailableForNationalContract() throws Exception {

        String agreementId = "18436c04-de60-422d-bab6-b03d6930604c";
        String contractType = "ICMDistributionAgreementNational";
        String updateRequestBody = "updateRequestBody";

        String agreementMetaData = "{\"ICMCPPLink\":\"url\",\"ICMIsAmendment\":false}";
        doReturn(agreementMetaData).when(target).getAgreementMetaData(agreementId, contractType);
        doReturn(updateRequestBody).when(updateCppUrlRequestDataBuilder).buildUpdateUrlRequestBody(agreementMetaData, agreementId, contractType,
                false);

        target.updateCppUrlForPricingContract(agreementId, contractType);

        verify(clmRestTemplate).exchange(eq(ICM_HOST_URL + ClmApiProxy.UPDATE_AGREEMENT_URL), eq(HttpMethod.PUT), httpEntityCaptor.capture(),
                eq(String.class));

        HttpEntity<String> actualRequestBodyEntity = httpEntityCaptor.getValue();
        assertThat(actualRequestBodyEntity.getBody(), equalTo(updateRequestBody));

        verify(updateCppUrlRequestDataBuilder).buildUpdateUrlRequestBody(agreementMetaData, agreementId, contractType, false);

    }

    @Test
    public void shouldNotUpdateCppUrlWhenPriceingRequestIsNotYes() throws Exception {

        String agreementId = "18436c04-de60-422d-bab6-b03d6930604c";
        String contractType = "ICMDistributionAgreementRegional";

        String agreementMetaData = "{\"ICMIsPricingRequired\":\"No\",\"ICMCPPLink\":\"url\",\"ICMIsAmendment\":false}";
        doReturn(agreementMetaData).when(target).getAgreementMetaData(agreementId, contractType);

        target.updateCppUrlForPricingContract(agreementId, contractType);

        verify(clmRestTemplate, never()).exchange(anyString(), eq(HttpMethod.PUT), httpEntityCaptor.capture(), eq(String.class),
                urlVariableCaptor.capture());

    }

    @Test
    public void shouldGetMetaDataFromClm() throws Exception {

        String agreementId = "18436c04-de60-422d-bab6-b03d6930604c";
        String contractType = "ICMDistributionAgreementRegional";

        String agreementResponse = "\"{\\\"EntityInstanceId\\\"";
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(agreementResponse, HttpStatus.OK);

        doReturn(responseEntity).when(clmRestTemplate).exchange(eq(ICM_HOST_URL + ClmApiProxy.GET_AGREEMENT_URL), eq(HttpMethod.GET),
                httpEntityCaptor.capture(), eq(String.class), urlVariableCaptor.capture());

        String agreementMetaData = target.getAgreementMetaData(agreementId, contractType);

        verify(clmRestTemplate).exchange(eq(ICM_HOST_URL + ClmApiProxy.GET_AGREEMENT_URL), eq(HttpMethod.GET), httpEntityCaptor.capture(),
                eq(String.class), urlVariableCaptor.capture());

        assertThat(agreementMetaData, equalTo(agreementResponse));

    }

    @Test
    public void shouldSaveAssociatedDocument() throws Exception {

        String agreementId = "18436c04-de60-422d-bab6-b03d6930604c";
        String contractType = "ICMDistributionAgreementRegional";
        String exhibitSysId = "84323423-de60-422d-bab6-b03d6930604c";
        String requestData = "data";

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(exhibitSysId, HttpStatus.OK);
        doReturn(responseEntity).when(clmRestTemplate).exchange(eq(ICM_HOST_URL + ClmApiProxy.SAVE_ASSOCIATED_DOCUMENT_URL), eq(HttpMethod.POST),
                httpEntityCaptor.capture(), eq(String.class), urlVariableCaptor.capture());
        doReturn(requestData).when(saveAssociatedDocumentRequestBuilder).buildSaveAssociateDocumentRequest(exhibitDocument, exhibitSysId);

        target.savePricingExhibit(exhibitDocument, agreementId, exhibitSysId, contractType);

        verify(clmRestTemplate).exchange(eq(ICM_HOST_URL + ClmApiProxy.SAVE_ASSOCIATED_DOCUMENT_URL), eq(HttpMethod.POST), httpEntityCaptor.capture(),
                eq(String.class), urlVariableCaptor.capture());

        Map<String, Object> actualUrlVariables = urlVariableCaptor.getValue();

        assertThat(actualUrlVariables.get("parentAgreementId").toString(), equalTo(agreementId));
        assertThat(actualUrlVariables.get("parentContractType").toString(), equalTo(contractType));
        assertThat(actualUrlVariables.get("childContractType").toString(), equalTo(EXHIBIT_DOCUMENT_TYPE));

        HttpEntity<String> actualHttpEntity = httpEntityCaptor.getValue();
        assertThat(actualHttpEntity.getBody(), equalTo(requestData));
        verify(target, never()).buildGuid();
        verify(fileRemover, atLeastOnce()).deleteFile(exhibitDocument);
    }

    @Test
    public void shouldSaveFurtheranceAssociatedDocument() throws Exception {

        String agreementId = "18436c04-de60-422d-bab6-b03d6930604c";
        String contractType = "ICMDistributionAgreementRegional";
        String exhibitSysId = "84323423-de60-422d-bab6-b03d6930604c";

        doReturn(exhibitSysId).when(target).saveAssociation(exhibitDocument, agreementId, null, contractType, FURTHERANCE_DOCUMENT_TYPE);

        String actualExhibitId = target.saveFurtheranceExhibit(exhibitDocument, agreementId, null, contractType);

        assertThat(actualExhibitId, equalTo(exhibitSysId));
        verify(target).saveAssociation(exhibitDocument, agreementId, null, contractType, FURTHERANCE_DOCUMENT_TYPE);
    }

    @Test
    public void shouldBuildGuidIfExhibitSysIdIsNull() throws Exception {

        String agreementId = "18436c04-de60-422d-bab6-b03d6930604c";
        String contractType = "ICMDistributionAgreementRegional";
        String exhibitSysId = "84323423-de60-422d-bab6-b03d6930604c";
        String requestData = "\"data\"";

        doReturn(exhibitSysId).when(target).buildGuid();
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(exhibitSysId, HttpStatus.OK);
        doReturn(responseEntity).when(clmRestTemplate).exchange(eq(ICM_HOST_URL + ClmApiProxy.SAVE_ASSOCIATED_DOCUMENT_URL), eq(HttpMethod.POST),
                httpEntityCaptor.capture(), eq(String.class), urlVariableCaptor.capture());
        doReturn(requestData).when(saveAssociatedDocumentRequestBuilder).buildSaveAssociateDocumentRequest(exhibitDocument, exhibitSysId);

        target.savePricingExhibit(exhibitDocument, agreementId, null, contractType);

        verify(clmRestTemplate).exchange(eq(ICM_HOST_URL + ClmApiProxy.SAVE_ASSOCIATED_DOCUMENT_URL), eq(HttpMethod.POST), httpEntityCaptor.capture(),
                eq(String.class), urlVariableCaptor.capture());

        Map<String, Object> actualUrlVariables = urlVariableCaptor.getValue();

        assertThat(actualUrlVariables.get("parentAgreementId").toString(), equalTo(agreementId));
        assertThat(actualUrlVariables.get("parentContractType").toString(), equalTo(contractType));
        assertThat(actualUrlVariables.get("childContractType").toString(), equalTo(EXHIBIT_DOCUMENT_TYPE));

        HttpEntity<String> actualHttpEntity = httpEntityCaptor.getValue();
        assertThat(actualHttpEntity.getBody(), equalTo(requestData));
        verify(target).buildGuid();

    }

    @Test
    public void shouldDeleteAssociatedDocument() throws Exception {

        String exhibitSysId = "84323423-de60-422d-bab6-b03d6930604c";

        target.deletePricingExhibit(exhibitSysId);

        verify(clmRestTemplate).exchange(eq(ICM_HOST_URL + ClmApiProxy.DELETE_ASSOCIATED_DOCUMENT_URL), eq(HttpMethod.POST),
                httpEntityCaptor.capture(), eq(Void.class), urlVariableCaptor.capture());

        Map<String, Object> actualUrlVariables = urlVariableCaptor.getValue();

        assertThat(actualUrlVariables.get("instanceId").toString(), equalTo(exhibitSysId));
        assertThat(actualUrlVariables.get("entityName").toString(), equalTo(EXHIBIT_DOCUMENT_TYPE));
    }

    @Test
    public void shouldBuildHeaderWihAuthToken() throws Exception {

        doReturn(ICM_AUTH_TOKEN).when(clmApplicationTokenFetcher).getApplicationToken();

        HttpHeaders actualHeader = target.buildHeader();

        List<String> actualAuthToken = actualHeader.get("IcmAuthToken");
        assertThat(actualAuthToken.get(0), equalTo(ICM_AUTH_TOKEN));
        verify(clmApplicationTokenFetcher).getApplicationToken();

    }

    @Test
    public void shouldGetParentAgreementData() throws Exception {

        String childContractAgreementId = "18436c04-de60-422d-bab6-b03d6930603c";
        String contractTypeName = "ICMDistributionAgreementNational";
        String parentEntityInstanceId = "18436c04-de60-422d-bab6-b03d6930604c";

        String parentAgreementMetaData = "{\"EntityInstanceId\": \"" + parentEntityInstanceId + "\"}";
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(parentAgreementMetaData, HttpStatus.OK);
        ClmContractResponseDTO parentAgreementResponse = new ClmContractResponseDTO();
        parentAgreementResponse.setContractAgreementId(parentEntityInstanceId);
        doReturn(parentAgreementResponse).when(objectMapper).readValue(parentAgreementMetaData, ClmContractResponseDTO.class);

        doReturn(responseEntity).when(clmRestTemplate).exchange(eq(ICM_HOST_URL + ClmApiProxy.GET_PARENT_AGREEMENT_URL), eq(HttpMethod.GET),
                httpEntityCaptor.capture(), eq(String.class), urlVariableCaptor.capture());

        String actualParentAgreementId = target.getParentAgreementId(childContractAgreementId, contractTypeName);

        assertThat(actualParentAgreementId, equalTo(actualParentAgreementId));

        verify(clmRestTemplate).exchange(eq(ICM_HOST_URL + ClmApiProxy.GET_PARENT_AGREEMENT_URL), eq(HttpMethod.GET), httpEntityCaptor.capture(),
                eq(String.class), urlVariableCaptor.capture());

        Map<String, Object> actualUrlVariables = urlVariableCaptor.getValue();

        assertThat(actualUrlVariables.get("childId").toString(), equalTo(childContractAgreementId));
        assertThat(actualUrlVariables.get("parentEntityName").toString(), equalTo("ICMDistributionAgreementNational"));
    }

    @Test
    public void shouldSetParentAgreementIdWhenAgreementIsAmendment() throws Exception {

        String agreementId = "18436c04-de60-422d-bab6-b03d6930604c";
        String contractType = "ICMDistributionAgreementRegionalAmendment";
        String parentContractType = "ICMDistributionAgreementRegional";
        String parentEntityInstanceId = "18436c04-de60-422d-bab6-b03d6930604c";

        ClmContractResponseDTO response = new ClmContractResponseDTO();
        response.setAmendment(true);
        response.setContractAgreementId(agreementId);
        response.setContractTypeName(contractType);
        response.setParentContractTypeName(parentContractType);

        String agreementMetaData = "agreementMetaData";

        doReturn(agreementMetaData).when(target).getAgreementMetaData(agreementId, contractType);
        doReturn(response).when(objectMapper).readValue(agreementMetaData, ClmContractResponseDTO.class);
        doReturn(parentEntityInstanceId).when(target).getParentAgreementId(agreementId, parentContractType);

        ClmContractResponseDTO actualResponse = target.getAgreementData(agreementId, contractType);

        assertThat(actualResponse.getParentAgreementId(), equalTo(parentEntityInstanceId));

        verify(target).getAgreementMetaData(agreementId, contractType);
        verify(objectMapper).readValue(agreementMetaData, ClmContractResponseDTO.class);
        verify(target).getParentAgreementId(agreementId, parentContractType);

    }

}
