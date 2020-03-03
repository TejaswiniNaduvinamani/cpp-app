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

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.search.ContractSearchResultDTO;
import com.gfs.cpp.data.contractpricing.ContractSearchResultDTORowMapper;

@RunWith(MockitoJUnitRunner.class)
public class ContractSearchResultDTORowMapperTest {

    @InjectMocks
    private ContractSearchResultDTORowMapper contractSearchResultDTORowMapper;

    @Mock
    private ResultSet rs;

    private static final String VERSION_NBR = "VERSION_NBR";
    private static final String CLM_CONTRACT_NAME = "CLM_CONTRACT_NAME";
    private static final String CLM_AGREEMENT_ID = "CLM_AGREEMENT_ID";
    private static final String CONTRACT_PRC_PROF_STATUS_CODE = "CONTRACT_PRC_PROF_STATUS_CODE";
    private static final String PRC_EFFECTIVE_DATE = "PRC_EFFECTIVE_DATE";
    private static final String CONTRACT_PRICE_PROFILE_ID = "CONTRACT_PRICE_PROFILE_ID";
    private static final String CPP_FURTHERANCE_SEQ = "CPP_FURTHERANCE_SEQ";
    private static final String CLM_CONTRACT_TYPE_SEQ = "CLM_CONTRACT_TYPE_SEQ";
    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
    private static final String PARENT_CLM_AGREEMENT_ID = "PARENT_CLM_AGREEMENT_ID";

    private static final java.util.Date effectiveDate = new LocalDate(2018, 01, 01).toDate();

    @Test
    public void shouldMapRowIntoContractSearchResultDTO() throws SQLException {
        String agreementId = "agreement-id";
        String contractName = "test contract";
        int contractPriceProfileId = 12;
        String cppFurtheranceSeq = "1";
        String contractType = "Regional-Original";
        int versionNumber = 1;
        String gfsCustomerId = "test customer id";
        int gfsCustomerTypeCode = 31;
        String parentAgreementId = "parent-agreement-id";

        when(rs.getString(CLM_AGREEMENT_ID)).thenReturn(agreementId);
        when(rs.getString(CLM_CONTRACT_NAME)).thenReturn(contractName);
        when(rs.getInt(CONTRACT_PRICE_PROFILE_ID)).thenReturn(contractPriceProfileId);
        when(rs.getDate(PRC_EFFECTIVE_DATE)).thenReturn(new Date(effectiveDate.getTime()));
        when(rs.getString(CPP_FURTHERANCE_SEQ)).thenReturn(cppFurtheranceSeq);
        when(rs.getInt(CLM_CONTRACT_TYPE_SEQ)).thenReturn(1);
        when(rs.getInt(VERSION_NBR)).thenReturn(versionNumber);
        when(rs.getInt(CONTRACT_PRC_PROF_STATUS_CODE)).thenReturn(10);
        when(rs.getString(GFS_CUSTOMER_ID)).thenReturn(gfsCustomerId);
        when(rs.getInt(GFS_CUSTOMER_TYPE_CODE)).thenReturn(gfsCustomerTypeCode);
        when(rs.getString(PARENT_CLM_AGREEMENT_ID)).thenReturn(parentAgreementId);

        final ContractSearchResultDTO actual = contractSearchResultDTORowMapper.mapRow(rs, 0);

        assertThat(actual.getAgreementId(), equalTo(agreementId));
        assertThat(actual.getContractName(), equalTo(contractName));
        assertThat(actual.getContractPriceProfileId(), equalTo(contractPriceProfileId));
        assertThat(actual.getContractType(), equalTo(contractType));
        assertThat(actual.getEffectiveDate(), equalTo(effectiveDate));
        assertThat(actual.getGfsCustomerId(), equalTo(gfsCustomerId));
        assertThat(actual.getGfsCustomerTypeCode(), equalTo(gfsCustomerTypeCode));
        assertThat(actual.getIsFurtheranceExist(), equalTo(CPPConstants.YES));
        assertThat(actual.getParentAgreementId(), equalTo(parentAgreementId));

    }

}
