package com.gfs.cpp.data.contractpricing;

import static com.github.springtestdbunit.assertion.DatabaseAssertionMode.NON_STRICT_UNORDERED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.customerinfo.CPPInformationDTO;
import com.gfs.cpp.common.dto.search.ContractSearchResultDTO;
import com.gfs.cpp.common.model.contractpricing.ContractPricingDO;
import com.gfs.cpp.common.util.ContractPriceProfileStatus;
import com.gfs.cpp.common.util.ContractTypeForSearchEnum;
import com.gfs.cpp.data.common.AbstractRepositoryIntegrationTest;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;

public class ContractPriceProfileRepositoryIntegrationTest extends AbstractRepositoryIntegrationTest {

    private static final String USER_NAME = "test user";

    @Autowired
    private ContractPriceProfileRepository target;

    @ExpectedDatabase(value = "ContractPriceProfileRepositoryIntegrationTest.shouldUpdateContractPricing-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "ContractPriceProfileRepositoryIntegrationTest.xml")
    @Test
    public void shouldUpdateContractPricing() throws Exception {
        int contractPriceProfileSeq = -1001;
        ContractPricingDO contractPricingDO = new ContractPricingDO();
        contractPricingDO.setContractPriceProfileId(-1001);
        contractPricingDO.setContractPriceProfileSeq(-1001);
        contractPricingDO.setEffectiveDateFuture(new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999"));
        contractPricingDO.setExpirationDateFuture(new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999"));
        contractPricingDO.setPricingEffectiveDate(new SimpleDateFormat("MM/dd/yyyy").parse("05/05/9999"));
        contractPricingDO.setTransferFeeInd(1);
        contractPricingDO.setLabelAssesmentInd(1);

        target.updateContractPriceProfile(contractPriceProfileSeq, contractPricingDO.getPricingEffectiveDate(), USER_NAME,
                contractPricingDO.getTransferFeeInd(), contractPricingDO.getLabelAssesmentInd());
    }

    @ExpectedDatabase(value = "ContractPriceProfileRepositoryIntegrationTest.shouldUpdateContractPricingDates-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "ContractPriceProfileRepositoryIntegrationTest.xml")
    @Test
    public void shouldUpdateContractPricingDates() throws Exception {
        int contractPriceProfileSeq = -1001;

        Date pricingEffectiveDate = new LocalDate(2018, 05, 31).toDate();
        Date contractStartDate = new LocalDate(2018, 03, 31).toDate();
        Date contractEndDate = new LocalDate(2018, 07, 31).toDate();
        target.updateContractPriceProfileDates(contractPriceProfileSeq, pricingEffectiveDate, contractStartDate, contractEndDate, USER_NAME);
    }

    @Test
    public void shouldFetchNextSequence() throws Exception {
        int actual = target.fetchCPPNextSequence();
        assertThat(actual, equalTo(1000));

    }

    @DatabaseSetup(value = "ContractPriceProfileRepositoryIntegrationTest.xml")
    @Test
    public void shouldFetchPricingDTOByCppSeq() throws Exception {
        int contractPriceProfileSeq = -1001;

        ContractPricingResponseDTO contractPricingResponseDTO = target.fetchContractDetailsByCppSeq(contractPriceProfileSeq);

        assertThat(contractPricingResponseDTO.getClmContractTypeSeq(), equalTo(-101));
        assertThat(contractPricingResponseDTO.getPricingEffectiveDate(), equalTo(new LocalDate(9999, 01, 01).toDate()));
        assertThat(contractPricingResponseDTO.getPricingExpirationDate(), equalTo(new LocalDate(9999, 01, 01).toDate()));
        assertThat(contractPricingResponseDTO.getContractPriceProfStatusCode(), equalTo(20));
        assertThat(contractPricingResponseDTO.getContractPriceProfileSeq(), equalTo(contractPriceProfileSeq));
        assertThat(contractPricingResponseDTO.getClmAgreementId(), equalTo("agreement-id"));
        assertThat(contractPricingResponseDTO.getPricingExhibitSysId(), equalTo("exhibit-sysid"));
        assertThat(contractPricingResponseDTO.getContractPriceProfileId(), equalTo(-1001));
        assertThat(contractPricingResponseDTO.getClmParentAgreementId(), equalTo("parent-agreement-id"));
        assertThat(contractPricingResponseDTO.getClmContractStartDate(), equalTo(new LocalDate(2018, 1, 1).toDate()));
        assertThat(contractPricingResponseDTO.getClmContractEndDate(), equalTo(new LocalDate(2019, 1, 1).toDate()));

    }

    @Test
    public void shouldReturnNullWhenFetchByCppSeqFoundNoRecords() throws Exception {
        ContractPricingResponseDTO contractPricingResponseDTO = target.fetchContractDetailsByCppSeq(-1001);
        assertThat(contractPricingResponseDTO, nullValue());
    }

    @DatabaseSetup(value = "ContractPriceProfileRepositoryIntegrationTest.xml")
    @Test
    public void shouldFetchPricingDTOByAgreementId() throws Exception {

        ContractPricingResponseDTO contractPricingResponseDTO = target.fetchContractDetailsByAgreementId("agreement-id");

        assertThat(contractPricingResponseDTO.getClmContractTypeSeq(), equalTo(-101));
        assertThat(contractPricingResponseDTO.getPricingEffectiveDate(), equalTo(new LocalDate(9999, 01, 01).toDate()));
        assertThat(contractPricingResponseDTO.getPricingExpirationDate(), equalTo(new LocalDate(9999, 01, 01).toDate()));
        assertThat(contractPricingResponseDTO.getContractPriceProfStatusCode(), equalTo(20));
        assertThat(contractPricingResponseDTO.getContractPriceProfileSeq(), equalTo(-1001));
        assertThat(contractPricingResponseDTO.getClmAgreementId(), equalTo("agreement-id"));
        assertThat(contractPricingResponseDTO.getPricingExhibitSysId(), equalTo("exhibit-sysid"));
        assertThat(contractPricingResponseDTO.getContractPriceProfileId(), equalTo(-1001));
    }

    @DatabaseSetup(value = "ContractPriceProfileRepositoryIntegrationTest.xml")
    @Test
    public void shouldFetchByAgreementIdReturnNullWhenNoAgreementIdPresent() throws Exception {

        assertThat(target.fetchContractDetailsByAgreementId("agreement-id-no"), nullValue());

    }

    @DatabaseSetup(value = "ContractPriceProfileRepositoryIntegrationTest.xml")
    @Test
    public void shouldFetchCPPSequence() throws Exception {
        int contractPriceProfileId = -1001;

        int actual = target.fetchCPPSequence(contractPriceProfileId);
        assertThat(actual, equalTo(-1001));
    }

    @DatabaseSetup(value = "ContractPriceProfileRepositoryIntegrationTest.shouldFetchEmptyCPPSequence.xml")
    @Test
    public void shouldFetchCPPSequenceEmptyResultException() throws Exception {
        int contractPriceProfileId = -1001;

        int actual = target.fetchCPPSequence(contractPriceProfileId);
        assertThat(actual, equalTo(0));
    }

    @DatabaseSetup(value = "ContractPriceProfileRepositoryIntegrationTest.xml")
    @Test
    public void shouldFetchContractPriceProfileId() throws Exception {
        int contractPriceProfileSeq = -1001;

        int actual = target.fetchContractPriceProfileId(contractPriceProfileSeq);
        assertThat(actual, equalTo(-1001));
    }

    @ExpectedDatabase(value = "ContractPriceProfileRepositoryIntegrationTest.shouldUpdateContractStatusWithLastUpdateUserIdByCppSeq-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "ContractPriceProfileRepositoryIntegrationTest.xml")
    @Test
    public void shouldUpdateContractStatusWithLastUpdateUserIdByCppSeq() throws Exception {
        target.updateContractStatusWithLastUpdateUserIdByCppSeq(-1001, 40, "updated user");
    }

    @ExpectedDatabase(value = "ContractPriceProfileRepositoryIntegrationTest.shouldUpdateToPriceActivateStatus-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "ContractPriceProfileRepositoryIntegrationTest.xml")
    @Test
    public void shouldUpdateToPriceActivateStatus() throws Exception {
        target.updateToPriceActivateStatus(-1001, "updated user");
    }

    @DatabaseSetup(value = "ContractPriceProfileRepositoryIntegrationTest.xml")
    @Test
    public void shouldFetchCPPInformation() throws Exception {

        CPPInformationDTO actual = target.fetchCPPInformation("agreement-id");

        assertThat(actual.getContractPriceProfileId(), equalTo(-1001));
        assertThat(actual.getContractPriceProfileSeq(), equalTo(-1001));
        assertThat(actual.getVersionNumber(), equalTo(1));
    }

    @DatabaseSetup(value = "ContractPriceProfileRepositoryIntegrationTest.shouldFetchEmptyCPPSequence.xml")
    @Test
    public void shouldFetchCPPInformationEmptyResultException() throws Exception {
        CPPInformationDTO actual = target.fetchCPPInformation("agreement-id");

        assertThat(actual, nullValue());
    }

    @Test
    public void shouldFetchContractPriceProfileIdSeq() throws Exception {
        int contractPricieProfileId = target.fetchContractPriceProfileIdSeq();

        assertThat(contractPricieProfileId, equalTo(1000));
    }

    @ExpectedDatabase(value = "ContractPriceProfileRepositoryIntegrationTest.shouldUpdateExpireLowerIndicatorForMarkup-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "ContractPriceProfileRepositoryIntegrationTest.shouldUpdateExpireLowerIndicatorForMarkup.xml")
    @Test
    public void shouldUpdateExpireLowerIndicatorForMarkup() throws Exception {
        int contractPriceProfileSeq = -1001;
        String userName = "test user";
        boolean isExpireLower = true;

        target.updateExpireLowerIndicator(contractPriceProfileSeq, isExpireLower, userName);

        target.updateExpireLowerIndicator(-1002, false, userName);
    }

    @ExpectedDatabase(value = "ContractPriceProfileRepositoryIntegrationTest.shouldUpdatePricingExhibitGuid-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "ContractPriceProfileRepositoryIntegrationTest.xml")
    @Test
    public void shouldUpdatePricingExhibitGuid() throws Exception {
        int contractPriceProfileSeq = -1001;
        String exhibitSysId = "new-exhibit-sysid";
        target.updatePricingExhibitGuid(contractPriceProfileSeq, exhibitSysId, USER_NAME);
    }

    @DatabaseSetup(value = "ContractPriceProfileRepositoryIntegrationTest.shouldUpdateExpireLowerIndicatorForMarkup.xml")
    @Test
    public void shouldFetchxpireLowerIndicatorTrueForMarkup() throws Exception {
        int contractPriceProfileSeq = -1002;

        int actual = target.fetchExpireLowerIndicator(contractPriceProfileSeq);

        assertThat(actual, equalTo(1));
    }

    @DatabaseSetup(value = "ContractPriceProfileRepositoryIntegrationTest.xml")
    @Test
    public void shouldFetchxpireLowerIndicatorFalseForMarkup() throws Exception {
        int contractPriceProfileSeq = -1001;

        int actual = target.fetchExpireLowerIndicator(contractPriceProfileSeq);

        assertThat(actual, equalTo(0));
    }

    @DatabaseSetup(value = "ContractPriceProfileRepositoryIntegrationTest.shouldFetchContractDetailsForLatestActivatedContractVersion.xml")
    @Test
    public void shouldFetchContractDetailsForLatestActivatedContractVersion() {
        String parentAgreementId = "parent-clm-agreement-id";

        ContractPricingResponseDTO actual = target.fetchContractDetailsForLatestActivatedContractVersion(parentAgreementId);

        assertThat(actual.getClmAgreementId(), equalTo("agreement-id"));
        assertThat(actual.getContractPriceProfileSeq(), equalTo(-1003));
    }

    @DatabaseSetup(value = "ContractPriceProfileRepositoryIntegrationTest.shouldFetchContractDetailsForLatestActivatedContractVersion.xml")
    @Test
    public void shouldThrowExceptionOnFetchLatestActivatedContractVersion() {
        String parentAgreementId = "clm-agreement-id";

        ContractPricingResponseDTO actual = target.fetchContractDetailsForLatestActivatedContractVersion(parentAgreementId);

        assertThat(actual, equalTo(null));
    }

    @ExpectedDatabase(value = "ContractPriceProfileRepositoryIntegrationTest.shouldSaveContractPricingDetails-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @Test
    public void shouldSaveContractPricingDetails() throws Exception {
        int contractTypeSeq = -101;
        int contractPriceProfileSeq = -1001;
        int versionNbr = 2;
        int expireLowerInd = 0;
        String parentAgreementId = "parent-agreement-id";
        String contractTypeCode = "-101";

        ContractPricingDO contractPricingDO = new ContractPricingDO();
        contractPricingDO.setContractPriceProfileSeq(contractPriceProfileSeq);
        contractPricingDO.setContractPriceProfileId(contractPriceProfileSeq);
        contractPricingDO.setVersionNbr(versionNbr);
        contractPricingDO.setExpireLowerLevelInd(expireLowerInd);
        contractPricingDO.setClmContractStartDate(new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999"));
        contractPricingDO.setClmContractEndDate(new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999"));
        contractPricingDO.setParentAgreementId(parentAgreementId);
        contractPricingDO.setPricingEffectiveDate(new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999"));
        contractPricingDO.setPricingExpirationDate(new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999"));
        contractPricingDO.setExpirationDateFuture(new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999"));
        contractPricingDO.setContractTypeCode(contractTypeCode);
        contractPricingDO.setAgreementId("agreement-id");
        contractPricingDO.setContractTypeCode("-101");
        contractPricingDO.setContractName("test contract 1");

        target.saveContractPricingDetails(contractPricingDO, USER_NAME, contractTypeSeq, contractPriceProfileSeq);

    }

    @DatabaseSetup(value = "ContractPriceProfileRepositoryIntegrationTest.shouldFetchInProgressContractVersion.xml")
    @Test
    public void shouldFetchInProgressContractVersion() throws Exception {
        String parentAgreementId = "A3bc02nl40619n982j8";

        int result = target.fetchInProgressContractVersionCount(parentAgreementId);

        assertThat(result, CoreMatchers.is(4));
    }

    @DatabaseSetup(value = "ContractPriceProfileRepositoryIntegrationTest.shouldFetchInProgressContractVersion.xml")
    @Test
    public void shouldNotFetchInProgressContractVersion() throws Exception {
        String parentAgreementId = "A3bc02nl40619n982j89";

        int result = target.fetchInProgressContractVersionCount(parentAgreementId);
        assertThat(result, CoreMatchers.is(0));
    }

    @DatabaseSetup(value = "ContractPriceProfileRepositoryIntegrationTest.shouldFetchInProgressContractVersion.xml")
    @Test
    public void shouldFetchLatestContractVersion() throws Exception {
        String parentAgreementId = "A3bc02nl40619n982j8";

        int result = target.fetchLatestContractVersionNumber(parentAgreementId);

        assertThat(result, CoreMatchers.is(8));
    }

    @DatabaseSetup(value = "ContractPriceProfileRepositoryIntegrationTest.xml")
    @Test
    public void shouldFetchPricingDTOByParentAgreementId() throws Exception {

        int count = target.fetchInProgressContractVersionCount("parent-agreement-id");

        assertThat(count, equalTo(1));

    }

    @ExpectedDatabase(value = "ContractPriceProfileRepositoryIntegrationTest.shouldUpdateContractPriceProfileContractName-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "ContractPriceProfileRepositoryIntegrationTest.shouldUpdateContractPriceProfileContractName.xml")
    @Test
    public void shouldUpdateContractPriceProfileContractName() throws Exception {
        int contractPriceProfileSeq = -1001;
        String contractName = "updated-contract-name";
        target.updateContractPriceProfileContractName(contractPriceProfileSeq, contractName, USER_NAME);
    }

    @DatabaseSetup(value = "ContractPriceProfileRepositoryIntegrationTest.shouldFetchContractDetails.xml")
    @Test
    public void shouldFetchContractDetailsByContractName() throws Exception {

        List<ContractSearchResultDTO> result = target.fetchContractDetailsByContractName("test");

        assertThat(result.size(), equalTo(2));

        assertThat(result.get(0).getAgreementId(), equalTo("agreement-id-2"));
        assertThat(result.get(0).getContractName(), equalTo("test contract 2"));
        assertThat(result.get(0).getEffectiveDate(), equalTo(new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999")));
        assertThat(result.get(0).getContractPriceProfileId(), equalTo(-1002));
        assertThat(result.get(0).getIsFurtheranceExist(), equalTo(CPPConstants.NO));
        assertThat(result.get(0).getGfsCustomerId(), equalTo(null));
        assertThat(result.get(0).getStatus(), equalTo(ContractPriceProfileStatus.PRICING_ACTIVATED.getDesc()));
        assertThat(result.get(0).getVersion(), equalTo(2));
        assertThat(result.get(0).getContractType(), equalTo(ContractTypeForSearchEnum.getDescByCode(6)));

        assertThat(result.get(1).getAgreementId(), equalTo("agreement-id"));
        assertThat(result.get(1).getContractName(), equalTo("test contract 1"));
        assertThat(result.get(1).getEffectiveDate(), equalTo(new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999")));
        assertThat(result.get(1).getContractPriceProfileId(), equalTo(-1001));
        assertThat(result.get(1).getIsFurtheranceExist(), equalTo(CPPConstants.YES));
        assertThat(result.get(1).getGfsCustomerId(), equalTo("-1001"));
        assertThat(result.get(1).getStatus(), equalTo(ContractPriceProfileStatus.DRAFT.getDesc()));
        assertThat(result.get(1).getVersion(), equalTo(1));
        assertThat(result.get(1).getContractType(), equalTo(ContractTypeForSearchEnum.getDescByCode(1)));

    }

    @DatabaseSetup(value = "ContractPriceProfileRepositoryIntegrationTest.shouldFetchContractDetails.xml")
    @Test
    public void shouldFetchContractDetailsByCustomer() throws Exception {

        List<ContractSearchResultDTO> result = target.fetchContractDetailsByCustomer("-1001", 36);

        assertThat(result.size(), equalTo(1));

        assertThat(result.get(0).getAgreementId(), equalTo("agreement-id"));
        assertThat(result.get(0).getContractName(), equalTo("test contract 1"));
        assertThat(result.get(0).getEffectiveDate(), equalTo(new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999")));
        assertThat(result.get(0).getContractPriceProfileId(), equalTo(-1001));
        assertThat(result.get(0).getIsFurtheranceExist(), equalTo(CPPConstants.YES));
        assertThat(result.get(0).getGfsCustomerId(), equalTo("-1001"));
        assertThat(result.get(0).getStatus(), equalTo(ContractPriceProfileStatus.DRAFT.getDesc()));
        assertThat(result.get(0).getVersion(), equalTo(1));
        assertThat(result.get(0).getContractType(), equalTo(ContractTypeForSearchEnum.getDescByCode(1)));

    }

    @DatabaseSetup(value = "ContractPriceProfileRepositoryIntegrationTest.shouldFetchContractDetails.xml")
    @Test
    public void shouldFetchContractDetailsByCPPId() throws Exception {

        List<ContractSearchResultDTO> result = target.fetchContractDetailsbyCPPId(-1001);

        assertThat(result.size(), equalTo(1));

        assertThat(result.get(0).getAgreementId(), equalTo("agreement-id"));
        assertThat(result.get(0).getContractName(), equalTo("test contract 1"));
        assertThat(result.get(0).getEffectiveDate(), equalTo(new SimpleDateFormat("MM/dd/yyyy").parse("01/01/9999")));
        assertThat(result.get(0).getContractPriceProfileId(), equalTo(-1001));
        assertThat(result.get(0).getIsFurtheranceExist(), equalTo(CPPConstants.YES));
        assertThat(result.get(0).getGfsCustomerId(), equalTo("-1001"));
        assertThat(result.get(0).getStatus(), equalTo(ContractPriceProfileStatus.DRAFT.getDesc()));
        assertThat(result.get(0).getVersion(), equalTo(1));
        assertThat(result.get(0).getContractType(), equalTo(ContractTypeForSearchEnum.getDescByCode(1)));

    }
}
