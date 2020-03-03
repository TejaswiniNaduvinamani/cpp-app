package com.gfs.cpp.data.contractpricing;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.data.contractpricing.ContractPricingResponseDTORowMapper;

@RunWith(MockitoJUnitRunner.class)
public class ContractPricingResponseDTORowMapperTest {

    @InjectMocks
    private ContractPricingResponseDTORowMapper contractPricingDAOMapper;

    @Mock
    private ResultSet rs;

    private static final String CLM_CONTRACT_END_DATE = "CLM_CONTRACT_END_DATE";
    private static final String CLM_CONTRACT_START_DATE = "CLM_CONTRACT_START_DATE";
    private static final String PARENT_CLM_AGREEMENT_ID = "PARENT_CLM_AGREEMENT_ID";
    private static final String PRC_EFFECTIVE_DATE = "PRC_EFFECTIVE_DATE";
    private static final String PRC_EXPIRATION_DATE = "PRC_EXPIRATION_DATE";
    private static final String CLM_CONTRACT_TYPE_SEQ = "CLM_CONTRACT_TYPE_SEQ";
    private static final String CONTRACT_PRC_PROF_STATUS_CODE = "CONTRACT_PRC_PROF_STATUS_CODE";
    private static final String CLM_AGREEMENT_ID = "CLM_AGREEMENT_ID";
    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String PRICING_EXHIBIT_GUID = "PRICING_EXHIBIT_GUID";
    private static final String CONTRACT_PRICE_PROFILE_ID = "CONTRACT_PRICE_PROFILE_ID";
    private static final String VERSION_NBR = "VERSION_NBR";
    private static final String CLM_CONTRACT_NAME="CLM_CONTRACT_NAME";
    
    private static final java.util.Date effectiveDate = new LocalDate(2018, 01, 01).toDate();
    private static final java.util.Date expirationDate = new LocalDate(2019, 01, 01).toDate();
    private static final java.util.Date contractStartDate = new LocalDate(2018, 01, 01).toDate();
    private static final java.util.Date contractEndDate = new LocalDate(2019, 01, 01).toDate();

    @Test
    public void shouldMapRowIntoContractPricingResponseDTO() throws SQLException {
        String parentAgreementId = "a452asdfasf";
        int clmContractTypeSeq = 1;
        Integer statusCode = 20;
        int cppSeq = 101;
        String agreementId = "agreeemtnId";
        String exhibitSysId = "exhibitSysId";
        int cppPriceProfileId = 102;
        String contractName = "testContract";
        int versionNumber = 1;

        when(rs.getDate(PRC_EFFECTIVE_DATE)).thenReturn(new Date(effectiveDate.getTime()));
        when(rs.getDate(PRC_EXPIRATION_DATE)).thenReturn(new Date(expirationDate.getTime()));
        when(rs.getInt(CLM_CONTRACT_TYPE_SEQ)).thenReturn(clmContractTypeSeq);
        when(rs.getInt(CONTRACT_PRC_PROF_STATUS_CODE)).thenReturn(statusCode);
        when(rs.getInt(CONTRACT_PRICE_PROFILE_SEQ)).thenReturn(cppSeq);
        when(rs.getString(CLM_AGREEMENT_ID)).thenReturn(agreementId);
        when(rs.getString(PRICING_EXHIBIT_GUID)).thenReturn(exhibitSysId);
        when(rs.getInt(CONTRACT_PRICE_PROFILE_ID)).thenReturn(cppPriceProfileId);
        when(rs.getDate(CLM_CONTRACT_START_DATE)).thenReturn(new Date(contractStartDate.getTime()));
        when(rs.getDate(CLM_CONTRACT_END_DATE)).thenReturn(new Date(contractEndDate.getTime()));
        when(rs.getString(PARENT_CLM_AGREEMENT_ID)).thenReturn(parentAgreementId);
        when(rs.getInt(VERSION_NBR)).thenReturn(versionNumber);
        when(rs.getString(CLM_CONTRACT_NAME)).thenReturn(contractName);

        final ContractPricingResponseDTO actual = contractPricingDAOMapper.mapRow(rs, 0);

        assertThat(actual.getClmContractTypeSeq(), equalTo(clmContractTypeSeq));
        assertThat(actual.getPricingEffectiveDate(), equalTo(effectiveDate));
        assertThat(actual.getPricingExpirationDate(), equalTo(expirationDate));
        assertThat(actual.getContractPriceProfStatusCode(), equalTo(statusCode));
        assertThat(actual.getClmAgreementId(), equalTo(agreementId));
        assertThat(actual.getContractPriceProfileSeq(), equalTo(cppSeq));
        assertThat(actual.getPricingExhibitSysId(), equalTo(exhibitSysId));
        assertThat(actual.getContractPriceProfileId(), equalTo(cppPriceProfileId));
        assertThat(actual.getClmContractStartDate(), equalTo(contractStartDate));
        assertThat(actual.getClmContractEndDate(), equalTo(contractEndDate));
        assertThat(actual.getClmParentAgreementId(), equalTo(parentAgreementId));
        assertThat(actual.getVersionNumber(), equalTo(versionNumber));
        assertThat(actual.getClmContractName(), equalTo(contractName));
    }
}
