package com.gfs.cpp.data.contractpricing;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gfs.cpp.data.common.AbstractRepositoryIntegrationTest;
import com.gfs.cpp.data.contractpricing.CostModelMapRepository;
import com.github.springtestdbunit.annotation.DatabaseSetup;

public class CostModelMapRepositoryIntegrationTest extends AbstractRepositoryIntegrationTest {

    @Autowired
    private CostModelMapRepository target;

    @Test
    @DatabaseSetup(value = "CostModelMapRepositoryIntegrationTest.shouldFetchCostModelId.xml")
    public void shouldFetchCostModelId() throws Exception {
        int transferFee = 1;
        int labelAssessmentInd = 1;
        int priceAuditInd = 1;
        int prcProfVerificationInd = 1;
        Integer actual = target.fetchCostModelId(transferFee, labelAssessmentInd, priceAuditInd, prcProfVerificationInd);

        assertThat(actual, equalTo(1));
    }

    @Test
    public void shouldFetchCostModelIdEmpty() throws Exception {
        int transferFee = 1;
        int labelAssessmentInd = 1;
        int priceAuditInd = 1;
        int prcProfVerificationInd = 1;

        Integer actual = target.fetchCostModelId(transferFee, labelAssessmentInd, priceAuditInd, prcProfVerificationInd);

        assertThat(actual, equalTo(null));
    }

}
