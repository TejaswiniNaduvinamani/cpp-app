package com.gfs.cpp.data.contractpricing;

import static com.github.springtestdbunit.assertion.DatabaseAssertionMode.NON_STRICT_UNORDERED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.data.common.AbstractRepositoryIntegrationTest;
import com.gfs.cpp.data.contractpricing.ContractPriceProfCustomerRepository;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;

@DatabaseSetup(value = "ContractPriceProfCustomerRepositoryIntegrationTest.xml")
public class ContractPriceProfCustomerRepositoryIntegrationTest extends AbstractRepositoryIntegrationTest {

    @Autowired
    private ContractPriceProfCustomerRepository target;

    private static final String USER_NAME = "test user";

    @ExpectedDatabase(value = "ContractPriceProfCustomerRepositoryIntegrationTest.shouldInsertContractPricingProfCustomer.xml", assertionMode = NON_STRICT_UNORDERED)
    @Test
    public void shouldInsertContractPricingProfCustomer() throws Exception {
        int contractPriceProfileSeq = -1003;
        String gfsCustomerId = "-1001";
        int customerTypeCode = 32;
        int cppCustomerSeq = -110;
        
        target.saveContractPriceProfCustomer(contractPriceProfileSeq, gfsCustomerId, customerTypeCode, USER_NAME, 1, cppCustomerSeq);
    }

    @Test
    public void shouldFetchGFSCustomerDetailsList() throws Exception {
        int contractPriceProfileSeq = -1001;

        List<CMGCustomerResponseDTO> CMGCustomerResponseList = target.fetchGFSCustomerDetailsList(contractPriceProfileSeq);

        assertThat(CMGCustomerResponseList.size(), equalTo(3));
        assertThat(CMGCustomerResponseList.get(0).getId(), equalTo("-1001"));
        assertThat(CMGCustomerResponseList.get(0).getTypeCode(), equalTo(31));
        assertThat(CMGCustomerResponseList.get(0).getDefaultCustomerInd(), equalTo(1));
        assertThat(CMGCustomerResponseList.get(1).getId(), equalTo("-1002"));
        assertThat(CMGCustomerResponseList.get(1).getTypeCode(), equalTo(31));
        assertThat(CMGCustomerResponseList.get(1).getDefaultCustomerInd(), equalTo(0));
    }

    @Test
    public void shouldFetchGFSCustomerDetailForDefault() throws Exception {
        int contractPriceProfileSeq = -1001;

        CMGCustomerResponseDTO cmgCustomer = target.fetchDefaultCmg(contractPriceProfileSeq);

        assertThat(cmgCustomer.getId(), equalTo("-1001"));
        assertThat(cmgCustomer.getTypeCode(), equalTo(31));
        assertThat(cmgCustomer.getDefaultCustomerInd(), equalTo(1));
    }

    @Test
    public void shouldFetchGFSCustomerDetailOfLatestExceptionCmg() throws Exception {
        int contractPriceProfileSeq = -1001;

        CMGCustomerResponseDTO cmgCustomer = target.fetchLatestCreatedExceptionCmg(contractPriceProfileSeq);

        assertThat(cmgCustomer.getId(), equalTo("-1005"));
        assertThat(cmgCustomer.getTypeCode(), equalTo(31));
        assertThat(cmgCustomer.getDefaultCustomerInd(), equalTo(0));
    }

    @Test
    public void shouldFetchGFSCustomerDetailForCustomer() throws Exception {
        int contractPriceProfileSeq = -1001;
        String gfsCustomerId = "-1001";
        int gfsCustomerTyeCode = 31;

        CMGCustomerResponseDTO cmgCustomer = target.fetchGFSCustomerDetailForCustomer(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTyeCode);

        assertThat(cmgCustomer.getId(), equalTo("-1001"));
        assertThat(cmgCustomer.getTypeCode(), equalTo(31));
        assertThat(cmgCustomer.getDefaultCustomerInd(), equalTo(1));
    }

    @ExpectedDatabase(value = "ContractPriceProfCustomerRepositoryIntegrationTest.shouldDeleteCPPCustomer.xml", assertionMode = NON_STRICT_UNORDERED)
    @Test
    public void shouldDeleteCPPCustomer() throws Exception {
        int contractPriceProfileSeq = -1001;
        String gfsCustomerId = "-1001";

        target.deleteCPPCustomer(contractPriceProfileSeq, gfsCustomerId);
    }

    @DatabaseSetup(value = "ContractPriceProfCustomerRepositoryIntegrationTest.shouldFetchGFSCustomerIdTrue.xml")
    @Test
    public void shouldFetchGfsCustomerIdTrue() throws Exception {
        int contractPriceProfileSeq = -1001;
        String actual = target.fetchGfsCustomerId(contractPriceProfileSeq, true);
        assertThat(actual, equalTo("-1001"));
    }

    @DatabaseSetup(value = "ContractPriceProfCustomerRepositoryIntegrationTest.shouldFetchGFSCustomerIdFalse.xml")
    @Test
    public void shouldFetchGfsCustomerIdFalse() throws Exception {
        int contractPriceProfileSeq = -1001;
        String actual = target.fetchGfsCustomerId(contractPriceProfileSeq, false);
        assertThat(actual, equalTo("-1001"));
    }

    @Test
    public void shouldFetchCPPCustomerSeq() throws Exception {
        String gfsCustomerId = "-1001";
        int gfsCustomerTypeCodeId = 31;
        int contractPriceProfSeq = -1001;
        int cppCustomerSeq = target.fetchCPPCustomerSeq(gfsCustomerId, gfsCustomerTypeCodeId, contractPriceProfSeq);
        assertThat(cppCustomerSeq, equalTo(-101));
    }

    @Test
    public void shouldFetchNextSequence() throws Exception {
        int actual = target.fetchCPPCustomerNextSequence();
        assertThat(actual, equalTo(1000));

    }

}
