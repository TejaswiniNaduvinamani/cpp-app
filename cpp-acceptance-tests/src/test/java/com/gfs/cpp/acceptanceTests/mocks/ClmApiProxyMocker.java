package com.gfs.cpp.acceptanceTests.mocks;

import static com.gfs.cpp.acceptanceTests.hookdefs.ScenarioContextUtil.getAttribute;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.gfs.cpp.acceptanceTests.config.CukesConstants;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Component
public class ClmApiProxyMocker implements Resettable {

    //@formatter:off
    private static final String ICM_HOST_URL = "https://gfsdev-api.icertis.com";
    
    private static final String LAUNCH_CPP_TXT = "Launch CPP";
    private static final String LAUNCH_CPP_AMENDMENT_TXT = "Launch CPP For Amendment";
    private static final String LAUNCH_FURTHERANCE_TXT = "Launch Furtherance";
    
    private static final String EXHIBIT_DOCUMENT_TYPE = "ICMCPPExhibit";
    
    private static final String FURTHERANCE_DOCUMENT_TYPE = "ICMCPPFurtherance";
    
    private static final String UPDATE_AGREEMENT_URL = "/api/Instance/SaveInstance";
    
    
    private static final String SAVE_ASSOCIATED_DOCUMENT_URL = "/api/Agreement/SaveAssociatedDocument"
            + "?parentContractType={parentContractType}"
            + "&parentAgreementId={parentAgreementId}"
            + "&childContractType={childContractType}";
    
    private static final String DELETE_ASSOCIATED_DOCUMENT_URL = "/api/Agreement/DeleteAssociatedDocument"
            + "?entityName={entityName}"
            + "&instanceId={instanceId}";
    
    private static final String GET_PARENT_AGREEMENT_URL = "/api/Agreement/GetParent"
            + "?childId={childId}"
            + "&includeChildCollection={includeChildCollection}"
            + "&parentEntityName={parentEntityName}";
    
    //@formatter:on

    private Class<Map<String, Object>> mapClass = (Class) Map.class;

    private ArgumentCaptor<Map<String, Object>> urlVariableCaptor = ArgumentCaptor.forClass(mapClass);

    private String clmContractStatus = "Draft";

    private String contractId;
    private String contractTypeName;

    @Qualifier("clmRestTemplate")
    @Autowired
    private RestTemplate clmRestTemplate;

    @Override
    public void reset() {
        resetSaveAssoicatedDocument();
        resetGetParentAgreementId();
    }

    public void isCppUrlUpdated() {

        Class<HttpEntity<String>> httpEntityClass = (Class) HttpEntity.class;
        ArgumentCaptor<HttpEntity<String>> httpEntityCaptor = ArgumentCaptor.forClass(httpEntityClass);

        verify(clmRestTemplate).exchange(eq(ICM_HOST_URL + UPDATE_AGREEMENT_URL), eq(HttpMethod.PUT), httpEntityCaptor.capture(), eq(String.class));

        HttpEntity<String> actualContent = httpEntityCaptor.getValue();

        assertThat(actualContent.getBody().contains(CukesConstants.AGREEMENT_ID), equalTo(true));
        assertThat(actualContent.getBody().contains(CukesConstants.CLM_CONTRACT_TYPE_REGIIONAL), equalTo(true));
        assertThat(actualContent.getBody().contains(LAUNCH_CPP_TXT), equalTo(true));
    }

    public void isFurtheranceUrlUpdated() {

        Class<HttpEntity<String>> httpEntityClass = (Class) HttpEntity.class;
        ArgumentCaptor<HttpEntity<String>> httpEntityCaptor = ArgumentCaptor.forClass(httpEntityClass);

        verify(clmRestTemplate).exchange(eq(ICM_HOST_URL + UPDATE_AGREEMENT_URL), eq(HttpMethod.PUT), httpEntityCaptor.capture(), eq(String.class));

        HttpEntity<String> actualContent = httpEntityCaptor.getValue();

        assertThat(actualContent.getBody().contains(CukesConstants.AGREEMENT_ID), equalTo(true));
        assertThat(actualContent.getBody().contains(CukesConstants.CLM_CONTRACT_TYPE_REGIIONAL), equalTo(true));
        assertThat(actualContent.getBody().contains(LAUNCH_FURTHERANCE_TXT), equalTo(true));
    }

    public void isCppUrlNotUpdated() {

        Class<HttpEntity<String>> httpEntityClass = (Class) HttpEntity.class;
        ArgumentCaptor<HttpEntity<String>> httpEntityCaptor = ArgumentCaptor.forClass(httpEntityClass);

        verify(clmRestTemplate, never()).exchange(eq(ICM_HOST_URL + UPDATE_AGREEMENT_URL), eq(HttpMethod.PUT), httpEntityCaptor.capture(),
                eq(String.class));
    }

    public void isExhibitAttachedToClm() {
        Class<HttpEntity<String>> httpEntityClass = (Class) HttpEntity.class;
        ArgumentCaptor<HttpEntity<String>> httpEntityCaptor = ArgumentCaptor.forClass(httpEntityClass);

        verify(clmRestTemplate).exchange(eq(ICM_HOST_URL + SAVE_ASSOCIATED_DOCUMENT_URL), eq(HttpMethod.POST), httpEntityCaptor.capture(),
                eq(String.class), urlVariableCaptor.capture());

        Map<String, Object> actualUrlVariables = urlVariableCaptor.getValue();

        assertThat(actualUrlVariables.get("parentContractType").toString(), equalTo(CukesConstants.CLM_CONTRACT_TYPE_STREET));
        assertThat(actualUrlVariables.get("parentAgreementId").toString(), equalTo(CukesConstants.AGREEMENT_ID));
        assertThat(actualUrlVariables.get("childContractType").toString(), equalTo(EXHIBIT_DOCUMENT_TYPE));

    }

    public void isFurtheranceDocumentAttachedToClm() {
        Class<HttpEntity<String>> httpEntityClass = (Class) HttpEntity.class;
        ArgumentCaptor<HttpEntity<String>> httpEntityCaptor = ArgumentCaptor.forClass(httpEntityClass);

        verify(clmRestTemplate).exchange(eq(ICM_HOST_URL + SAVE_ASSOCIATED_DOCUMENT_URL), eq(HttpMethod.POST), httpEntityCaptor.capture(),
                eq(String.class), urlVariableCaptor.capture());

        Map<String, Object> actualUrlVariables = urlVariableCaptor.getValue();

        assertThat(actualUrlVariables.get("parentContractType").toString(), equalTo(CukesConstants.CLM_CONTRACT_TYPE_REGIIONAL));
        assertThat(actualUrlVariables.get("parentAgreementId").toString(), equalTo(CukesConstants.AGREEMENT_ID));
        assertThat(actualUrlVariables.get("childContractType").toString(), equalTo(FURTHERANCE_DOCUMENT_TYPE));

    }

    public void isAssociatedDocumentDeleted() {

        Class<HttpEntity<Object>> httpEntityClass = (Class) HttpEntity.class;
        ArgumentCaptor<HttpEntity<Object>> httpEntityCaptor = ArgumentCaptor.forClass(httpEntityClass);

        verify(clmRestTemplate).exchange(eq(ICM_HOST_URL + DELETE_ASSOCIATED_DOCUMENT_URL), eq(HttpMethod.POST), httpEntityCaptor.capture(),
                eq(Void.class), urlVariableCaptor.capture());

        Map<String, Object> actualUrlVariables = urlVariableCaptor.getValue();

        String exhibitSysId = (String) getAttribute("exhibitSysId");

        assertThat(actualUrlVariables.get("instanceId").toString(), equalTo(exhibitSysId));
        assertThat(actualUrlVariables.get("entityName").toString(), equalTo(EXHIBIT_DOCUMENT_TYPE));
    }

    private void resetSaveAssoicatedDocument() {
        doAnswer(new Answer<ResponseEntity<String>>() {

            @Override
            public ResponseEntity<String> answer(InvocationOnMock invocation) throws Throwable {

                return new ResponseEntity<String>(CukesConstants.EXHIBIT_SYS_ID, HttpStatus.OK);
            }

        }).when(clmRestTemplate).exchange(eq(ICM_HOST_URL + SAVE_ASSOCIATED_DOCUMENT_URL), eq(HttpMethod.POST), any(HttpEntity.class),
                eq(String.class), any(Map.class));

    }

    private void resetGetParentAgreementId() {
        doAnswer(new Answer<ResponseEntity<String>>() {

            @Override
            public ResponseEntity<String> answer(InvocationOnMock invocation) throws Throwable {
                Map<Object, Object> urlVariableArgument = invocation.getArgumentAt(4, Map.class);

                return new ResponseEntity<String>(buildAgreeementJsonData(urlVariableArgument), HttpStatus.OK);
            }

            private String buildAgreeementJsonData(Map<Object, Object> urlVariableArgument) {

                StringBuilder builder = new StringBuilder("{");

                contractId = CukesConstants.AGREEMENT_ID;
                contractTypeName = (String) urlVariableArgument.get("parentEntityName");

                builder.append("\"EntityInstanceId\":\"" + contractId + "\",");
                builder.append("\"Status\":\"" + clmContractStatus + "\",");
                builder.append("\"EntityName\":\"" + contractTypeName + "\"");
                builder.append("}");

                return builder.toString();
            }

        }).when(clmRestTemplate).exchange(eq(ICM_HOST_URL + GET_PARENT_AGREEMENT_URL), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class),
                any(Map.class));
    }

    public void isCppAmendmentUrlUpdated() {

        Class<HttpEntity<String>> httpEntityClass = (Class) HttpEntity.class;
        ArgumentCaptor<HttpEntity<String>> httpEntityCaptor = ArgumentCaptor.forClass(httpEntityClass);

        verify(clmRestTemplate).exchange(eq(ICM_HOST_URL + UPDATE_AGREEMENT_URL), eq(HttpMethod.PUT), httpEntityCaptor.capture(), eq(String.class));

        HttpEntity<String> actualContent = httpEntityCaptor.getValue();

        assertThat(actualContent.getBody().contains(CukesConstants.AMENDMENT_AGREEMENT_ID), equalTo(true));
        assertThat(actualContent.getBody().contains(CukesConstants.CLM_CONTRACT_TYPE_REGIONAL_AMENDMENT), equalTo(true));
        assertThat(actualContent.getBody().contains(LAUNCH_CPP_AMENDMENT_TXT), equalTo(true));

    }
}
