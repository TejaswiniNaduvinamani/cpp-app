package com.gfs.cpp.proxy.clm;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfs.cpp.common.dto.clm.ClmContractResponseDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.proxy.clm.FileRemover;

@Component
public class ClmApiProxy {

    private static final Logger logger = LoggerFactory.getLogger(ClmApiProxy.class);
    private static final Logger clmDataLogger = LoggerFactory.getLogger("clmdata");

    private static final String INCLUDE_CHILD_COLLECTION = "includeChildCollection";
    private static final String PARENT_ENTITY_NAME = "parentEntityName";
    private static final String CHILD_ID = "childId";
    private static final String CONTRACT_TYPE = "contractType";
    private static final String AGREEMENT_ID = "agreementId";

    private static final String GPO_MASTER_AGREEMENT = "ICMGPOMasterAgreement";
    private static final String DISTRIBUTION_AGREEMENT_NATIONAL = "ICMDistributionAgreementNational";

    private static final String PRICING_YES = "Yes";

    static final String ICM_IS_AMENDMENT_KEY = "ICMIsAmendment";
    private static final String ICM_IS_PRICING_REQUIRED_KEY = "ICMIsPricingRequired";
    private static final String ICM_IS_PRICING_REQUIRED_FOR_AMENDMENT_KEY = "ICMWillthisamendmentchangepricing2";

    @Qualifier("clmRestTemplate")
    @Autowired
    private RestTemplate clmRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SaveAssociatedDocumentRequestBuilder saveAssociatedDocumentRequestBuilder;

    @Autowired
    private ClmApplicationTokenFetcher clmApplicationTokenFetcher;

    @Autowired
    private UpdateCppUrlRequestDataBuilder updateCppUrlRequestDataBuilder;

    @Autowired
    private FileRemover fileRemover;

    @Value("${icm.host.url}")
    private String icmHostUrl;

    @Value("${icm.cpp.exhibit.type.name}")
    private String cppExhibitTypeName;

    @Value("${icm.cpp.furtherance.exhibit.type.name}")
    private String cppFurtheranceExhibitTypeName;

  //@formatter:off
    static final String GET_AGREEMENT_URL = "/api/Instance/GetInstance"
            + "?entityInstanceId={agreementId}"
            + "&entityName={contractType}"
            + "&includeChildCollection=false";
            
     static final String GET_PARENT_AGREEMENT_URL = "/api/Agreement/GetParent"
             + "?childId={childId}"
             + "&includeChildCollection={includeChildCollection}"
             + "&parentEntityName={parentEntityName}";
    
     static final String UPDATE_AGREEMENT_URL = "/api/Instance/SaveInstance";
     
     static final String SAVE_ASSOCIATED_DOCUMENT_URL = "/api/Agreement/SaveAssociatedDocument"
             + "?parentContractType={parentContractType}"
             + "&parentAgreementId={parentAgreementId}"
             + "&childContractType={childContractType}";
     
     static final String DELETE_ASSOCIATED_DOCUMENT_URL = "/api/Agreement/DeleteAssociatedDocument"
             + "?entityName={entityName}"
             + "&instanceId={instanceId}";
  //@formatter:on

    public ClmContractResponseDTO getAgreementData(String agreementId, String contractType) {

        String agreementData = getAgreementMetaData(agreementId, contractType);

        ClmContractResponseDTO clmContractResponse = buildResponseData(agreementData);

        if (clmContractResponse.isAmendment()) {
            clmContractResponse.setParentAgreementId(
                    getParentAgreementId(clmContractResponse.getContractAgreementId(), clmContractResponse.getParentContractTypeName()));
        }

        return clmContractResponse;
    }

    public void updateCppUrlForPricingContract(String agreementId, String contractTypeName) {

        String agreementMetaDataString = getAgreementMetaData(agreementId, contractTypeName);
        JSONObject agreementMetaData = new JSONObject(agreementMetaDataString);

        boolean isAmendment = (Boolean) agreementMetaData.get(ICM_IS_AMENDMENT_KEY);

        if (isPricingRequired(agreementMetaData, contractTypeName, isAmendment)) {

            String updateRequestBody = updateCppUrlRequestDataBuilder.buildUpdateUrlRequestBody(agreementMetaDataString, agreementId,
                    contractTypeName, isAmendment);

            clmDataLogger.info("Update Request Body for agreement {} about to update: {}", agreementId, updateRequestBody);

            clmRestTemplate.exchange(icmHostUrl + UPDATE_AGREEMENT_URL, HttpMethod.PUT, buildEntity(updateRequestBody), String.class);
            logger.info("Updated CPP Url for agreement id {} with contractType {}.", agreementId, contractTypeName);
        }
    }

    public void updateFurtheranceUrlForPricingContract(String agreementId, String contractTypeName) {

        String agreementMetaDataString = getAgreementMetaData(agreementId, contractTypeName);

        String updateRequestBody = updateCppUrlRequestDataBuilder.buildFurtheranceUrlRequestBody(agreementMetaDataString, agreementId,
                contractTypeName);

        clmDataLogger.info("Update Request Body for agreement {} about to update: {}", agreementId, updateRequestBody);

        clmRestTemplate.exchange(icmHostUrl + UPDATE_AGREEMENT_URL, HttpMethod.PUT, buildEntity(updateRequestBody), String.class);
        logger.info("Updated Furtherance Url for agreement id {} with contractType {}.", agreementId, contractTypeName);
    }

    public String savePricingExhibit(File exhibitDocument, String contractAgeementId, String exhibitSysId, String contractTypeName) {
        return saveAssociation(exhibitDocument, contractAgeementId, exhibitSysId, contractTypeName, cppExhibitTypeName);
    }

    public String saveFurtheranceExhibit(File exhibitDocument, String contractAgeementId, String exhibitSysId, String contractTypeName) {
        return saveAssociation(exhibitDocument, contractAgeementId, exhibitSysId, contractTypeName, cppFurtheranceExhibitTypeName);
    }

    public void deletePricingExhibit(String exhibitSysId) {

        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("instanceId", exhibitSysId);
        uriVariables.put("entityName", cppExhibitTypeName);

        clmRestTemplate.exchange(icmHostUrl + DELETE_ASSOCIATED_DOCUMENT_URL, HttpMethod.POST, buildEntity(), Void.class, uriVariables);
    }

    String saveAssociation(File exhibitDocument, String contractAgeementId, String exhibitSysId, String contractTypeName,
            String associationTypeName) {

        String buildExhibitSysId = buildExhibitSysId(exhibitSysId);

        try {
            Map<String, Object> uriVariables = new HashMap<>();
            uriVariables.put("parentAgreementId", contractAgeementId);
            uriVariables.put("parentContractType", contractTypeName);
            uriVariables.put("childContractType", associationTypeName);

            String updateRequestBody = saveAssociatedDocumentRequestBuilder.buildSaveAssociateDocumentRequest(exhibitDocument, buildExhibitSysId);

            logger.info("saving exhibit for contract {} of type {} with sysid {}", contractAgeementId, contractTypeName, buildExhibitSysId);

            clmDataLogger.info("Save Associated Document Request Body for agreement {}, with sysId {} about to update: {}", contractAgeementId,
                    buildExhibitSysId, updateRequestBody);

            ResponseEntity<String> saveAssociatedDocumentResponse = clmRestTemplate.exchange(icmHostUrl + SAVE_ASSOCIATED_DOCUMENT_URL,
                    HttpMethod.POST, buildEntity(updateRequestBody), String.class, uriVariables);

            return StringUtils.defaultIfEmpty(saveAssociatedDocumentResponse.getBody(), StringUtils.EMPTY).replaceAll("\"", "");
        } catch (Exception ex) {
            logger.error("save exhibit failed for agreement id {} of type {} with sysid {}", contractAgeementId, contractTypeName, buildExhibitSysId,
                    ex);
            throw new CPPRuntimeException(CPPExceptionType.SAVE_EXHIBIT_SERVICE_FAILED, "Service to attach exhibit failed.");
        } finally {
            if (exhibitDocument != null) {
                boolean isDeleted = fileRemover.deleteFile(exhibitDocument);
                logger.info("Has file {} deleted : {} ", exhibitDocument.getName(), isDeleted);
            }
        }
    }

    String buildGuid() {
        return UUID.randomUUID().toString();
    }

    String getParentAgreementId(String childContractAgreementId, String parentConrtractType) {

        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put(CHILD_ID, childContractAgreementId);
        uriVariables.put(PARENT_ENTITY_NAME, parentConrtractType);
        uriVariables.put(INCLUDE_CHILD_COLLECTION, false);

        ResponseEntity<String> parentMetaData = clmRestTemplate.exchange(icmHostUrl + GET_PARENT_AGREEMENT_URL, HttpMethod.GET, buildEntity(),
                String.class, uriVariables);

        ClmContractResponseDTO parentContractData = buildResponseData(parentMetaData.getBody());

        return parentContractData.getContractAgreementId();
    }

    String getAgreementMetaData(String agreementId, String contractType) {

        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put(AGREEMENT_ID, agreementId);
        uriVariables.put(CONTRACT_TYPE, contractType);

        ResponseEntity<String> response = clmRestTemplate.exchange(icmHostUrl + GET_AGREEMENT_URL, HttpMethod.GET, buildEntity(), String.class,
                uriVariables);

        clmDataLogger.info("Get Agreement Data for {} : {}", agreementId, response.getBody());

        return response.getBody();
    }

    HttpHeaders buildHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("IcmAuthToken", getAuthToken());
        headers.add("SubjectIdentifier", "1");
        return headers;
    }

    private String buildExhibitSysId(String exhibitSysId) {

        String result = exhibitSysId;

        if (StringUtils.isBlank(exhibitSysId)) {
            result = buildGuid();
        }

        return result;
    }

    private boolean isPricingRequired(JSONObject agreementMetaData, String contractTypeName, boolean isAmendment) {

        if (isAmendment) {
            return !agreementMetaData.isNull(ICM_IS_PRICING_REQUIRED_FOR_AMENDMENT_KEY)
                    && PRICING_YES.equalsIgnoreCase(agreementMetaData.getString(ICM_IS_PRICING_REQUIRED_FOR_AMENDMENT_KEY));
        }

        if (GPO_MASTER_AGREEMENT.equals(contractTypeName) || DISTRIBUTION_AGREEMENT_NATIONAL.equals(contractTypeName)) {
            return true;
        }

        return !agreementMetaData.isNull(ICM_IS_PRICING_REQUIRED_KEY)
                && PRICING_YES.equalsIgnoreCase(agreementMetaData.getString(ICM_IS_PRICING_REQUIRED_KEY));
    }

    private HttpEntity<Object> buildEntity() {
        return new HttpEntity<>(buildHeader());
    }

    private HttpEntity<String> buildEntity(String requestBody) {
        return new HttpEntity<>(requestBody, buildHeader());
    }

    private String getAuthToken() {
        return clmApplicationTokenFetcher.getApplicationToken();
    }

    private ClmContractResponseDTO buildResponseData(String agreementData) {
        try {
            return objectMapper.readValue(agreementData, ClmContractResponseDTO.class);
        } catch (IOException e) {
            throw new CPPRuntimeException("Error converting response from CLM data " + agreementData + " ", e);
        }
    }

}
