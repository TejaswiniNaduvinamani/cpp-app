package com.gfs.cpp.data.activatepricing;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gfs.cpp.common.dto.priceactivation.ContractCustomerMappingDTO;
import com.gfs.cpp.data.activatepricing.ContractCustomerMappingRepository;
import com.gfs.cpp.data.common.AbstractRepositoryIntegrationTest;
import com.github.springtestdbunit.annotation.DatabaseSetup;

public class ContractCustomerMappingRepositoryIntegrationTest extends AbstractRepositoryIntegrationTest {

    @Autowired
    private ContractCustomerMappingRepository target;

    @DatabaseSetup(value = "ContractCustomerMappingRepositoryIntegrationTest.shouldFetchUnmappedConceptCount.xml")
    @Test
    public void shouldFetchUnmappedConceptCount() throws Exception {
        int contractPriceProfileSeq = -1001;
        int unmappedContractConcepts = target.fetchUnmappedConceptCount(contractPriceProfileSeq);
        assertThat(unmappedContractConcepts, equalTo(1));

    }
    
    @DatabaseSetup(value = "ContractCustomerMappingRepositoryIntegrationTest.shouldFetchUnmappedConceptCountForExpiredMapping.xml")
    @Test
    public void shouldFetchUnmappedConceptCountForExpiredMapping() throws Exception {
        int contractPriceProfileSeq = -1001;
        int unmappedContractConcepts = target.fetchUnmappedConceptCount(contractPriceProfileSeq);
        assertThat(unmappedContractConcepts, equalTo(1));

    }
    
    @DatabaseSetup(value = "ContractCustomerMappingRepositoryIntegrationTest.xml")
    @Test
    public void shouldFetchUnmappedConceptInContractCount() throws Exception {
        int contractPriceProfileSeq = -1001;
        int unmappedContractConcepts = target.fetchUnmappedConceptCount(contractPriceProfileSeq);
        assertThat(unmappedContractConcepts, equalTo(0));

    }

    @DatabaseSetup(value = "ContractCustomerMappingRepositoryIntegrationTest.xml")
    @Test
    public void shouldFetchAllConceptCustomerMapping() throws Exception {
        int contractPriceProfileSeq = -1001;

        List<ContractCustomerMappingDTO> contractCustomerMappingDTOList = target.fetchAllConceptCustomerMapping(contractPriceProfileSeq);
        
        assertThat(contractCustomerMappingDTOList.get(0).getCppConceptMappingSeq(), equalTo(-100));
        assertThat(contractCustomerMappingDTOList.get(0).getCppCustomerSeq(), equalTo(-102));
        assertThat(contractCustomerMappingDTOList.get(0).getDefaultCustomerInd(), equalTo(0));
        assertThat(contractCustomerMappingDTOList.get(0).getGfsCustomerId(), equalTo("-1001"));
        assertThat(contractCustomerMappingDTOList.get(0).getGfsCustomerTypeCode(), equalTo(36));
    }

}
