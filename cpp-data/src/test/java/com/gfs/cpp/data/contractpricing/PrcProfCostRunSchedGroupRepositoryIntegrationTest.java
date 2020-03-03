package com.gfs.cpp.data.contractpricing;

import static com.github.springtestdbunit.assertion.DatabaseAssertionMode.NON_STRICT_UNORDERED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.markup.PrcProfCostSchedulePkgScheduledGroupDTO;
import com.gfs.cpp.common.model.markup.PrcProfCostSchedulePkgScheduledGroupDO;
import com.gfs.cpp.data.common.AbstractRepositoryIntegrationTest;
import com.gfs.cpp.data.contractpricing.PrcProfCostRunSchedGroupRepository;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;

public class PrcProfCostRunSchedGroupRepositoryIntegrationTest extends AbstractRepositoryIntegrationTest {

    @Autowired
    private PrcProfCostRunSchedGroupRepository target;

    @ExpectedDatabase(value = "PrcProfCostRunSchedGroupRepositoryIntegrationTest.shouldDeleteCostScheduleRunGroup-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "PrcProfCostRunSchedGroupRepositoryIntegrationTest.shouldDeleteCostScheduleRunGroup.xml")
    @Test
    public void shouldDeleteScheduleForGregorian() throws Exception {

        int contractPriceProfileSeq = -2001;

        target.deleteScheduleForGregorian(contractPriceProfileSeq);
    }

    @ExpectedDatabase(value = "PrcProfCostRunSchedGroupRepositoryIntegrationTest.shouldInsertSchedule.xml", assertionMode = NON_STRICT_UNORDERED)
    @Test
    public void shouldInsertSchedule() throws Exception {
        int contractPriceProfileSeq = -2001;
        int costSchedulePkgSeq = -2001;
        target.insertSchedule(contractPriceProfileSeq, costSchedulePkgSeq, CPPConstants.SCHEDULE_GROUP_SEQ_GREGORIAN_W,
                CPPConstants.SCHEDULE_GROUP_SEQ_GREGORIAN_M);
    }

    @ExpectedDatabase(value = "PrcProfCostRunSchedGroupRepositoryIntegrationTest.shouldSavePrcProfNonBrktCstMdlForCustomer.xml", assertionMode = NON_STRICT_UNORDERED)
    @Test
    public void shouldSavePrcProfNonBrktCstMdlForCustomer() throws Exception {

        List<PrcProfCostSchedulePkgScheduledGroupDO> prcProfCostSchedulePkgScheduledGroupDOList = new ArrayList<>();
        PrcProfCostSchedulePkgScheduledGroupDO prcProfCostSchedulePkgScheduledGroupDO = new PrcProfCostSchedulePkgScheduledGroupDO();
        prcProfCostSchedulePkgScheduledGroupDO.setPrcProfCostSchedulePkgSeq(-2001);
        prcProfCostSchedulePkgScheduledGroupDO.setContractPriceSeq(-2001);
        prcProfCostSchedulePkgScheduledGroupDO.setCostRunFrequencyCode("M");
        prcProfCostSchedulePkgScheduledGroupDO.setScheduleGroup(-105);
        prcProfCostSchedulePkgScheduledGroupDOList.add(prcProfCostSchedulePkgScheduledGroupDO);

        target.savePrcProfCostRunSchedGroupForCustomer(prcProfCostSchedulePkgScheduledGroupDOList);

    }

    @DatabaseSetup(value = "PrcProfCostRunSchedGroupRepositoryIntegrationTest.xml")
    @Test
    public void shouldFetchPrcProfCostRunSchedGroupForCPPSeq() throws Exception {
        int contractPriceProfileSeq = -2001;
        int prcProfCostScheduledPkgSeq = -1;

        List<PrcProfCostSchedulePkgScheduledGroupDTO> actual = target.fetchPrcProfCostRunSchedGroupForCPPSeq(contractPriceProfileSeq,
                prcProfCostScheduledPkgSeq);

        assertThat(actual.get(0).getCostRunFrequencyCode(), equalTo("W"));
        assertThat(actual.get(0).getScheduleGroup(), equalTo(-20001));

    }

    @DatabaseSetup(value = "PrcProfCostRunSchedGroupRepositoryIntegrationTest.shouldInsertScheduleForGFSFiscal.xml")
    @Test
    public void shouldFetchGfsMonthlyCostScheduleCost() throws Exception {
        int contractPriceProfileSeq = -2001;
        String gfsCustomerId = "-1";
        int gfsCustomerTypeCode = 31;

        int actual = target.fetchGfsMonthlyCostScheduleCost(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTypeCode);

        assertThat(actual, is(CPPConstants.SCHEDULE_GROUP_SEQ_GFS_FISCAL_M));
    }

    @ExpectedDatabase(value = "PrcProfCostRunSchedGroupRepositoryIntegrationTest.shouldUpdateScheduleGroupSeqMonthly-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "PrcProfCostRunSchedGroupRepositoryIntegrationTest.shouldInsertScheduleForGFSFiscal.xml")
    @Test
    public void shouldUpdateScheduleGroupSeqMonthly() throws Exception {

        int contractPriceProfileSeq = -2001;
        int scheduleGroupSeqMonthly = 105;

        target.updateScheduleGroupSeqMonthly(scheduleGroupSeqMonthly, contractPriceProfileSeq);
    }

}
