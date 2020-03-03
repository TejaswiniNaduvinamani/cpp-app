package com.gfs.cpp.component.furtherance;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.furtherance.CPPFurtheranceTrackingDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceBaseDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceInformationDTO;
import com.gfs.cpp.component.furtherance.FurtheranceDTOBuilder;

@RunWith(MockitoJUnitRunner.class)
public class FurtheranceDTOBuilderTest {

    @InjectMocks
    private FurtheranceDTOBuilder target;

    @Test
    public void shouldBuildFurtheranceBaseDTO() {

        int cppFurtheranceSeq = 2000;
        String parentAgreementId = "parent-clm-agreement-id";
        String contractTypeName = "testContract";

        FurtheranceBaseDTO furtheranceBaseDTO = target.buildFurtheranceBaseDTO(parentAgreementId, cppFurtheranceSeq, contractTypeName);

        assertThat(furtheranceBaseDTO.getAgreementId(), equalTo(parentAgreementId));
        assertThat(furtheranceBaseDTO.getContractType(), equalTo(contractTypeName));
        assertThat(furtheranceBaseDTO.getCppFurtheranceSeq(), equalTo(cppFurtheranceSeq));
    }

    @Test
    public void shouldFetchSavedFurtheranceInfo() {

        String parentAgreementId = "parent-clm-agreement-id";
        int cppFurtheranceSeq = 2000;

        FurtheranceInformationDTO actual = target.buildDefaultFurtheranceDTO(parentAgreementId, cppFurtheranceSeq);

        assertThat(actual.getContractPriceProfileSeq(), equalTo(0));
        assertThat(actual.getCppFurtheranceSeq(), equalTo(cppFurtheranceSeq));
        assertThat(actual.getParentCLMAgreementId(), equalTo(parentAgreementId));
    }

    @Test
    public void shouldBuildFurtheranceWrapper() {
        String itemPriceId = "1";
        int cmgCustomerTypeCode = 31;
        String cmgCustomerId = "1";
        int cppFurtheranceSeq = 1;
        int itemPriceLevelCode = 1;
        String changeTableName = "table";
        int action = 1;

        CPPFurtheranceTrackingDTO result = target.buildCPPFurtheranceTrackingDTO(cppFurtheranceSeq, cmgCustomerId, cmgCustomerTypeCode, itemPriceId,
                itemPriceLevelCode, changeTableName, action);

        assertThat(result.getGfsCustomerId(), equalTo(cmgCustomerId));
        assertThat(result.getGfsCustomerTypeCode(), equalTo(cmgCustomerTypeCode));
        assertThat(result.getCppFurtheranceSeq(), equalTo(cppFurtheranceSeq));
        assertThat(result.getItemPriceId(), equalTo(itemPriceId));
        assertThat(result.getItemPriceLevelCode(), equalTo(itemPriceLevelCode));
        assertThat(result.getChangeTableName(), equalTo(changeTableName));
        assertThat(result.getFurtheranceActionCode(), equalTo(action));

    }
}
