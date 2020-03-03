package com.gfs.cpp.acceptanceTests.mocks;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;
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
import com.gfs.cpp.common.dto.clm.ClmContractStatus;

@Component
public class CLMGetAgreementMocker implements Resettable {

    //@formatter:off
    private static final String ICM_HOST_URL = "https://gfsdev-api.icertis.com";
    
    private static final String GET_AGREEMENT_URL = "/api/Instance/GetInstance"
            + "?entityInstanceId={agreementId}"
            + "&entityName={contractType}"
            + "&includeChildCollection=false";
    
    
    //@formatter:on

    @Qualifier("clmRestTemplate")
    @Autowired
    private RestTemplate clmRestTemplate;

    private String isPricingRequired = "Yes";
    private Map<String, Boolean> isAmendment = new HashMap<>();
    private String isAmendmentPricingRequired = "No";
    private boolean isAutoRenewalEnabled = false;
    private Map<String, String> clmContractStatus = new HashMap<>();

    private String contractId;
    private String contractTypeName;
    private String contractName;
    private Date amendmentEffectiveDate;
    private Date contractExpirationDate;

    @Override
    public void reset() {
        resetForGetAgreement();

    }

    @SuppressWarnings("unchecked")
    private void resetForGetAgreement() {

        contractExpirationDate = CukesConstants.CLM_CONTRACT_END_DATE;
        amendmentEffectiveDate = CukesConstants.AMENDMENT_EFFECTIVE_DATE;

        clmContractStatus.put(CukesConstants.AGREEMENT_ID, "Draft");
        clmContractStatus.put(CukesConstants.AMENDMENT_AGREEMENT_ID, "Draft");

        isAmendment.put(CukesConstants.AGREEMENT_ID, false);
        isAmendment.put(CukesConstants.AMENDMENT_AGREEMENT_ID, true);
        isPricingRequired = "Yes";
        contractName = CukesConstants.CONTRACT_NAME;

        doAnswer(new Answer<ResponseEntity<String>>() {

            @Override
            public ResponseEntity<String> answer(InvocationOnMock invocation) throws Throwable {
                Map<Object, Object> urlVariableArgument = invocation.getArgumentAt(4, Map.class);

                return new ResponseEntity<String>(buildAgreeementJsonData(urlVariableArgument), HttpStatus.OK);
            }

            private String buildAgreeementJsonData(Map<Object, Object> urlVariableArgument) {

                StringBuilder builder = new StringBuilder("{");

                contractId = (String) urlVariableArgument.get("agreementId");
                contractTypeName = (String) urlVariableArgument.get("contractType");

                builder.append("\"EntityInstanceId\":\"" + contractId + "\",");
                builder.append("\"Status\":\"" + getClmContractStatus(contractId) + "\",");
                builder.append("\"ICMEffectiveDate\":\"" + convertToClmDateFormat(CukesConstants.CLM_CONTRACT_START_DATE) + "\",");
                builder.append("\"ICMExpiryDate\":\"" + convertToClmDateFormat(contractExpirationDate) + "\",");
                builder.append("\"EntityName\":\"" + contractTypeName + "\",");
                builder.append("\"ICMContractTypeName\":\"" + contractTypeName.replace("Amendmend", "") + "\",");
                builder.append("\"ICMIsPricingRequired\":\"" + isPricingRequired + "\",");
                builder.append("\"Name\":\"" + contractName + "\",");
                builder.append("\"ICMAmendmentEffectiveDate\":\"" + convertToClmDateFormat(amendmentEffectiveDate) + "\",");
                builder.append("\"ParentEntityInstanceID\":\"\",");
                builder.append("\"ICMIsAmendment\":" + isAmendment(contractId) + ",");
                builder.append("\"ICMRenewal\":\"" + isAutoRenewalEnabled + "\",");
                builder.append("\"ICMWillthisamendmentchangepricing2\":\"" + isAmendmentPricingRequired + "\"");
                builder.append("}");

                return builder.toString();
            }
        }).when(clmRestTemplate).exchange(eq(ICM_HOST_URL + GET_AGREEMENT_URL), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class),
                any(Map.class));
    }

    private String convertToClmDateFormat(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public void setPricingRequired(String pricingRequired) {
        this.isPricingRequired = pricingRequired;
    }

    public String getClmContractStatus(String agreementId) {
        return clmContractStatus.get(agreementId);
    }

    public void setClmContractStatus(String agreementId, ClmContractStatus clmContractStatus) {
        this.clmContractStatus.put(agreementId, clmContractStatus.value);
    }

    public void setAmendment(String agreementId, boolean isAmendment) {
        this.isAmendment.put(agreementId, isAmendment);
    }

    public boolean isAmendment(String agreementId) {
        return BooleanUtils.isTrue(isAmendment.get(agreementId));
    }

    public void setAmendmentEffectiveDate(Date amendmentEffectiveDate) {
        this.amendmentEffectiveDate = amendmentEffectiveDate;
    }

    public void setContractExpirationDate(Date contractExpirationDate) {
        this.contractExpirationDate = contractExpirationDate;
    }

    public void setAmendmentPricing(String amendmentPricing) {
        this.isAmendmentPricingRequired = amendmentPricing;

    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

}
