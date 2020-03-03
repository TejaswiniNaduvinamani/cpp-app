package com.gfs.cpp.data.contractpricing;

import static com.github.springtestdbunit.assertion.DatabaseAssertionMode.NON_STRICT_UNORDERED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.markup.PrcProfNonBrktCstMdlDTO;
import com.gfs.cpp.common.model.contractpricing.ContractPricingDO;
import com.gfs.cpp.common.model.markup.PrcProfNonBrktCstMdlDO;
import com.gfs.cpp.data.common.AbstractRepositoryIntegrationTest;
import com.gfs.cpp.data.contractpricing.PrcProfNonBrktCstMdlRepository;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;

public class PrcProfNonBrktCstMdlRepositoryIntegrationTest extends AbstractRepositoryIntegrationTest {

    @Autowired
    private PrcProfNonBrktCstMdlRepository target;

    private static final String USER_NAME = "test user";

    @ExpectedDatabase(value = "PrcProfNonBrktCstMdlRepositoryIntegrationTest.shouldDeletePriceProfileCostModel-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "PrcProfNonBrktCstMdlRepositoryIntegrationTest.shouldDeletePriceProfileCostModel.xml")
    @Test
    public void shouldDeletePriceProfileCostModal() throws Exception {

        int contractPriceProfileSeq = -2001;

        target.deletePriceProfileCostModel(contractPriceProfileSeq);
    }

    @ExpectedDatabase(value = "PrcProfNonBrktCstMdlRepositoryIntegrationTest.shouldInsertCostModelData-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @Test
    public void shouldInsertCostModelData() throws Exception {
        int costModelId = -2001;
        ContractPricingDO contractPricingDO = new ContractPricingDO();

        contractPricingDO.setGfsCustomerId("-2001");
        contractPricingDO.setContractPriceProfileSeq(-2001);
        contractPricingDO.setEffectiveDateFuture(new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999"));
        contractPricingDO.setContractTypeCode("DAN");
        contractPricingDO.setExpirationDateFuture(new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999"));
        contractPricingDO.setCustomerTypeCode(31);

        target.saveCostModelData(USER_NAME, contractPricingDO, costModelId, costModelId, costModelId, setOfProductTypes());
    }

    @DatabaseSetup(value = "PrcProfNonBrktCstMdlRepositoryIntegrationTest.xml")
    @Test
    public void shouldFetchCostModelId() throws Exception {
        int contractPriceProfileSeq = -2001;

        Integer actual = target.fetchCostModelId(contractPriceProfileSeq);

        assertThat(actual, equalTo(1));
    }

    @ExpectedDatabase(value = "PrcProfNonBrktCstMdlRepositoryIntegrationTest.shouldExpireNonBrktCostModelForContract-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "PrcProfNonBrktCstMdlRepositoryIntegrationTest.shouldExpireNonBrktCostModelForContract.xml")
    @Test
    public void shouldExpireNonBrktCostModelForContract() throws Exception {

        target.expireNonCmgPriceProfileCostModelForContract(-3001, new LocalDate(2018, 04, 15).toDate(), "updated user");
    }

    @DatabaseSetup(value = "PrcProfNonBrktCstMdlRepositoryIntegrationTest.shouldExpirePriceProfileCostModelForRealCust.xml")
    @Test
    public void shouldExpirePriceProfileCostModelForRealCust() throws Exception {
        Date expirationDateForExistingPricing = new LocalDate(2018, 04, 04).toDate();
        Date newEeffectiveDate = new LocalDate(2018, 04, 05).toDate();
        Date newExpirationDate = new LocalDate(2018, 04, 15).toDate();
        String customerId = "-10000012";
        int customerTypeCode = 36;

        target.expirePriceProfileCostModelForRealCust(expirationDateForExistingPricing, "updated user", customerId, customerTypeCode,
                newEeffectiveDate, newExpirationDate);

    }

    @ExpectedDatabase(value = "PrcProfNonBrktCstMdlRepositoryIntegrationTest.shouldSavePrcProfNonBrktCstMdlForCustomer.xml", assertionMode = NON_STRICT_UNORDERED)
    @Test
    public void shouldSavePrcProfNonBrktCstMdlForCustomer() throws Exception {
        List<PrcProfNonBrktCstMdlDO> prcProfNonBrktCstMdlDOList = new ArrayList<>();
        PrcProfNonBrktCstMdlDO prcProfNonBrktCstMdlDO = new PrcProfNonBrktCstMdlDO();
        prcProfNonBrktCstMdlDO.setContractPriceProfileSeq(-2002);
        prcProfNonBrktCstMdlDO.setCostModelId(-2001);
        prcProfNonBrktCstMdlDO.setCreateUserId("test user");
        prcProfNonBrktCstMdlDO.setEffectiveDate(new LocalDate(9999, 01, 01).toDate());
        prcProfNonBrktCstMdlDO.setExpirationDate(new LocalDate(9999, 01, 01).toDate());
        prcProfNonBrktCstMdlDO.setGfsCustomerId("-2001");
        prcProfNonBrktCstMdlDO.setGfsCustomerTypeCode(31);
        prcProfNonBrktCstMdlDO.setLastUpdateUserId("test user");
        prcProfNonBrktCstMdlDOList.add(prcProfNonBrktCstMdlDO);

        target.savePrcProfNonBrktCstMdlForCustomer(prcProfNonBrktCstMdlDOList);

    }

    @DatabaseSetup(value = "PrcProfNonBrktCstMdlRepositoryIntegrationTest.xml")
    @Test
    public void shouldFetchPrcProfNonBrktCstMdlForCPPSeq() throws Exception {
        int contractPriceProfileSeq = -2001;
        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId("-2001");
        cmgCustomerResponseDTO.setTypeCode(31);

        List<PrcProfNonBrktCstMdlDTO> actual = target.fetchPrcProfNonBrktCstMdlForCPPSeq(contractPriceProfileSeq, cmgCustomerResponseDTO);

        assertThat(actual.get(0).getContractPriceProfileSeq(), equalTo(-2001));
        assertThat(actual.get(0).getCostModelId(), equalTo(1));
        assertThat(actual.get(0).getItemPriceId(), equalTo("-1001"));
        assertThat(actual.get(0).getItemPriceLevelCode(), equalTo(-1001));

    }

    @DatabaseSetup(value = "PrcProfNonBrktCstMdlRepositoryIntegrationTest.shouldSaveDefaultCostModelData.xml")
    @ExpectedDatabase(value = "PrcProfNonBrktCstMdlRepositoryIntegrationTest.shouldUpdateCostModelData-results.xml", assertionMode = NON_STRICT_UNORDERED)
    @Test
    public void shouldUpdateCostModelData() throws Exception {
        int contractPriceProfileSeq = -2001;
        String userName = "updated user";
        int costModelId = 10;

        target.updateCostModelData(userName, costModelId, costModelId, costModelId, contractPriceProfileSeq);

    }

    @DatabaseSetup(value = "PrcProfNonBrktCstMdlRepositoryIntegrationTest.shouldUpdateCostModel.xml")
    @ExpectedDatabase(value = "PrcProfNonBrktCstMdlRepositoryIntegrationTest.shouldUpdateCostModel-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @Test
    public void shouldUpdateCostModel() throws Exception {
        int contractPriceProfileSeq = -2001;
        String userName = "updated user";
        List<PrcProfNonBrktCstMdlDTO> costModelTypeDTOList = new ArrayList<>();
        PrcProfNonBrktCstMdlDTO prcProfNonBrktCstMdlDTO = new PrcProfNonBrktCstMdlDTO();
        prcProfNonBrktCstMdlDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        prcProfNonBrktCstMdlDTO.setCostModelId(20);
        prcProfNonBrktCstMdlDTO.setGfsCustomerId("-2001");
        prcProfNonBrktCstMdlDTO.setGfsCustomerTypeCode(31);
        prcProfNonBrktCstMdlDTO.setItemPriceId("10");
        prcProfNonBrktCstMdlDTO.setItemPriceLevelCode(0);
        costModelTypeDTOList.add(prcProfNonBrktCstMdlDTO);
        PrcProfNonBrktCstMdlDTO prcProfNonBrktCstMdlDTO1 = new PrcProfNonBrktCstMdlDTO();
        prcProfNonBrktCstMdlDTO1.setContractPriceProfileSeq(contractPriceProfileSeq);
        prcProfNonBrktCstMdlDTO1.setCostModelId(30);
        prcProfNonBrktCstMdlDTO1.setGfsCustomerId("-2001");
        prcProfNonBrktCstMdlDTO1.setGfsCustomerTypeCode(31);
        prcProfNonBrktCstMdlDTO1.setItemPriceLevelCode(1);
        costModelTypeDTOList.add(prcProfNonBrktCstMdlDTO1);

        target.updateCostModel(costModelTypeDTOList, contractPriceProfileSeq, userName);

    }
    
    private Set<Integer> setOfProductTypes(){
        Integer arr[] = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}; 
        return new HashSet<>(Arrays.asList(arr)); 
    }

}
