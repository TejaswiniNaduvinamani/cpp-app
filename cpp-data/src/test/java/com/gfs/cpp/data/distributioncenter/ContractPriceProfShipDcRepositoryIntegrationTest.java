package com.gfs.cpp.data.distributioncenter;

import static com.github.springtestdbunit.assertion.DatabaseAssertionMode.NON_STRICT_UNORDERED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.model.distributioncenter.DistributionCenterDO;
import com.gfs.cpp.common.model.distributioncenter.DistributionCenterDetailDO;
import com.gfs.cpp.data.common.AbstractRepositoryIntegrationTest;
import com.gfs.cpp.data.distributioncenter.ContractPriceProfShipDcRepository;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;

public class ContractPriceProfShipDcRepositoryIntegrationTest extends AbstractRepositoryIntegrationTest {

    private static final String USER_NAME = "test user";

    @Autowired
    private ContractPriceProfShipDcRepository target;

    @ExpectedDatabase(value = "ContractPriceProfShipDcRepositoryIntegrationTest.shouldInsertDistributionCenters-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @Test
    public void shouldInsertDistributionCenters() throws Exception {
        DistributionCenterDO distributionCenterDOToInsert = new DistributionCenterDO();
        Date effectiveDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE);
        Date expirationDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE);

        ArrayList<String> dcCodes = new ArrayList<String>();
        dcCodes.add("-10001234");

        distributionCenterDOToInsert.setDcCodes(dcCodes);
        distributionCenterDOToInsert.setContractPriceProfileSeq(-201);
        distributionCenterDOToInsert.setEffectiveDate(effectiveDate);
        distributionCenterDOToInsert.setExpirationDate(expirationDate);

        target.saveDistributionCenter(distributionCenterDOToInsert, USER_NAME);

    }

    @ExpectedDatabase(value = "ContractPriceProfShipDcRepositoryIntegrationTest.shouldDeleteDistributionCenters-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "ContractPriceProfShipDcRepositoryIntegrationTest.shouldDeleteDistributionCenters.xml")
    @Test
    public void shouldDeleteDistributionCenters() throws Exception {
        DistributionCenterDO distributionCenterDOToUpdate = new DistributionCenterDO();
        Date effectiveDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/2018");
        Date expirationDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/2018");

        ArrayList<String> dcCodes = new ArrayList<String>();
        dcCodes.add("-10001234");

        distributionCenterDOToUpdate.setDcCodes(dcCodes);
        distributionCenterDOToUpdate.setContractPriceProfileSeq(-201);
        distributionCenterDOToUpdate.setEffectiveDate(effectiveDate);
        distributionCenterDOToUpdate.setExpirationDate(expirationDate);

        target.deleteDistributionCenter(distributionCenterDOToUpdate, USER_NAME);

    }

    @DatabaseSetup(value = "ContractPriceProfShipDcRepositoryIntegrationTest.shouldReturnDistributionCenters.xml")
    @Test
    public void shouldFetchSavedDistributionCenters() throws Exception {
        int contractPriceProfileSeq = -201;
        List<DistributionCenterDetailDO> actual = target.fetchSavedDistributionCenters(contractPriceProfileSeq);

        assertThat(actual.size(), equalTo(1));

        assertThat(actual.get(0).getContractPriceProfileSeq(), equalTo(-201));
        assertThat(actual.get(0).getDcCode(), equalTo("-10001234"));

        Date expectedEffectiveDate = new SimpleDateFormat("yyyy-MM-DD").parse("9999-01-01");
        assertThat(actual.get(0).getEffectiveDate(), equalTo(expectedEffectiveDate));

        Date expectedExpirationDate = new SimpleDateFormat("yyyy-MM-DD").parse("9999-01-01");
        assertThat(actual.get(0).getExpirationDate(), equalTo(expectedExpirationDate));

    }

    @DatabaseSetup(value = "ContractPriceProfShipDcRepositoryIntegrationTest.shouldFetchAllDistributionCenters.xml")
    @Test
    public void shouldFetchAllDistributionCenters() throws Exception {
        int contractPriceProfileSeq = -201;
        List<DistributionCenterDetailDO> actual = target.fetchAllDistributionCenter(contractPriceProfileSeq);

        assertThat(actual.size(), equalTo(2));

        assertThat(actual.get(0).getContractPriceProfileSeq(), equalTo(-201));
        assertThat(actual.get(0).getDcCode(), equalTo("-10001234"));
        assertThat(actual.get(1).getDcCode(), equalTo("-10001235"));

        Date expectedEffectiveDate = new SimpleDateFormat("yyyy-MM-DD").parse("9999-01-01");
        assertThat(actual.get(0).getEffectiveDate(), equalTo(expectedEffectiveDate));
        assertThat(actual.get(1).getEffectiveDate(), equalTo(expectedEffectiveDate));

        Date expectedExpirationDate = new SimpleDateFormat("yyyy-MM-DD").parse("9999-01-01");
        assertThat(actual.get(0).getExpirationDate(), equalTo(expectedExpirationDate));
        assertThat(actual.get(1).getExpirationDate(), equalTo(expectedExpirationDate));

    }

    @DatabaseSetup(value = "ContractPriceProfShipDcRepositoryIntegrationTest.shouldFetchAllDistributionCenters.xml")
    @Test
    public void shouldFetchDistributionCentersbyContractPriceProfileSeq() throws Exception {
        int contractPriceProfileSeq = -201;
        List<String> actual = target.fetchDistributionCentersbyContractPriceProfileSeq(contractPriceProfileSeq);

        assertThat(actual.size(), equalTo(2));
        assertThat(actual.get(0), equalTo("-10001234"));
        assertThat(actual.get(1), equalTo("-10001235"));

    }

}
