package com.gfs.cpp.data.contractpricing;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gfs.cpp.data.common.AbstractRepositoryIntegrationTest;
import com.gfs.cpp.data.contractpricing.ClmContractTypeRepository;
import com.github.springtestdbunit.annotation.DatabaseSetup;

@DatabaseSetup(value = "ClmContractTypeRepositoryIntegrationTest.xml")
public class ClmContractTypeRepositoryIntegrationTest extends AbstractRepositoryIntegrationTest {

    @Autowired
    private ClmContractTypeRepository target;

    @Test
    public void shouldReturnAllContractTypes() throws Exception {

        List<String> allContractTypes = target.getAllContractTypes();

        assertThat(allContractTypes.size(), equalTo(2));

        assertThat(allContractTypes.contains("ICMPOC_CPP"), equalTo(true));
        assertThat(allContractTypes.contains("ICMDistributionAgreementStreet"), equalTo(true));
    }

    @Test
    public void shouldFetchContractTypeSequence() throws Exception {
        String contractTypeName = "ICMPOC_CPP";

        int actual = target.fetchContractTypeSequenceByTypeName(contractTypeName);
        assertThat(actual, equalTo(-1002));
    }

    @Test
    public void shouldFetchContractTypeNameBySequence() throws Exception {
        int contractTypeSeq = -1001;

        String actual = target.getContractTypeName(contractTypeSeq);
        assertThat(actual, equalTo("ICMDistributionAgreementStreet"));
    }

}
