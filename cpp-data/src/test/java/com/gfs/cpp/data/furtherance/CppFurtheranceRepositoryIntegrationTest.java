package com.gfs.cpp.data.furtherance;

import static com.github.springtestdbunit.assertion.DatabaseAssertionMode.NON_STRICT_UNORDERED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gfs.cpp.common.dto.furtherance.FurtheranceInformationDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceStatus;
import com.gfs.cpp.data.common.AbstractRepositoryIntegrationTest;
import com.gfs.cpp.data.furtherance.CppFurtheranceRepository;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;

public class CppFurtheranceRepositoryIntegrationTest extends AbstractRepositoryIntegrationTest {

    private static final String USER_NAME = "test user";

    @Autowired
    private CppFurtheranceRepository target;

    @DatabaseSetup(value = "CppFurtheranceRepositoryIntegrationTest.xml")
    @Test
    public void shouldFetchFurtheranceDetailsByParentAgreementId() throws Exception {
        String parentAgreementId = "parent-clm-agreement-id";

        FurtheranceInformationDTO actualFurtheranceInfo = target.fetchInProgressFurtheranceDetailsByParentAgreementId(parentAgreementId);

        assertThat(actualFurtheranceInfo.getCppFurtheranceSeq(), equalTo(-1000));
        assertThat(actualFurtheranceInfo.getContractPriceProfileSeq(), equalTo(-2000));
        assertThat(actualFurtheranceInfo.getFurtheranceStatusCode(), equalTo(1));
        assertThat(actualFurtheranceInfo.getParentCLMAgreementId(), equalTo("parent-clm-agreement-id"));
        assertThat(actualFurtheranceInfo.getContractReferenceTxt(), equalTo("reference"));
        assertThat(actualFurtheranceInfo.getChangeReasonTxt(), equalTo("change-reason"));
        assertThat(actualFurtheranceInfo.getFurtheranceDocumentGUID(), equalTo("doc"));
        assertThat(actualFurtheranceInfo.getFurtheranceEffectiveDate(), equalTo(new LocalDate(2018, 1, 1).toDate()));

    }

    @DatabaseSetup(value = "CppFurtheranceRepositoryIntegrationTest.xml")
    @Test
    public void shouldNotFetchFurtheranceDetailsByNullParentAgreementId() throws Exception {
        String parentAgreementId = "parent-clm-agreement-id-return-null";

        FurtheranceInformationDTO actualFurtheranceInfo = target.fetchInProgressFurtheranceDetailsByParentAgreementId(parentAgreementId);

        assertThat(actualFurtheranceInfo, nullValue());

    }

    @DatabaseSetup(value = "CppFurtheranceRepositoryIntegrationTest.shouldFetchFurtheranceDetailsByFurtheranceSeq.xml")
    @Test
    public void shouldFetchFurtheranceDetailsByFurtheranceSeq() throws Exception {
        int furtheranceSeq = -1002;

        FurtheranceInformationDTO actualFurtheranceInfo = target.fetchFurtheranceDetailsByFurtheranceSeq(furtheranceSeq);

        assertThat(actualFurtheranceInfo, notNullValue());

        assertThat(actualFurtheranceInfo.getCppFurtheranceSeq(), equalTo(-1002));
        assertThat(actualFurtheranceInfo.getContractPriceProfileSeq(), equalTo(-2002));
        assertThat(actualFurtheranceInfo.getFurtheranceStatusCode(), equalTo(1));
        assertThat(actualFurtheranceInfo.getParentCLMAgreementId(), equalTo("parent-clm-agreement-id1"));
        assertThat(actualFurtheranceInfo.getContractReferenceTxt(), equalTo("reference1"));
        assertThat(actualFurtheranceInfo.getChangeReasonTxt(), equalTo("change-reason"));
        assertThat(actualFurtheranceInfo.getFurtheranceDocumentGUID(), equalTo("doc"));
        assertThat(actualFurtheranceInfo.getFurtheranceEffectiveDate(), equalTo(new LocalDate(2018, 1, 31).toDate()));
    }

    @DatabaseSetup(value = "CppFurtheranceRepositoryIntegrationTest.shouldFetchFurtheranceDetailsByFurtheranceSeq.xml")
    @Test
    public void shouldFetchFurtheranceDetailsByFurtheranceSeqReturnNull() throws Exception {
        int furtheranceSeq = -1012;

        FurtheranceInformationDTO actualFurtheranceInfo = target.fetchFurtheranceDetailsByFurtheranceSeq(furtheranceSeq);

        assertThat(actualFurtheranceInfo, nullValue());

    }

    @Test
    public void shouldFetchCPPFurtheranceNextSequence() {

        int actual = target.fetchCPPFurtheranceNextSequence();

        assertThat(actual, is(1000));
    }

    @ExpectedDatabase(value = "CppFurtheranceRepositoryIntegrationTest.shouldUpdateCPPFurtherance-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "CppFurtheranceRepositoryIntegrationTest.xml")
    @Test
    public void shouldUpdateCPPFurtherance() throws Exception {
        int furtheranceStatusCode = 1;
        String parentAgreementId = "parent-clm-agreement-id";
        int cppFurtheranceSeq = -1000;
        String contractReferenceTxt = "referenceModified";
        int contractPriceProfileSeq = -2000;
        String changeReasonTxt = "change-reason-Modified";

        FurtheranceInformationDTO furtheranceInformationDTO = new FurtheranceInformationDTO();
        furtheranceInformationDTO.setChangeReasonTxt(changeReasonTxt);
        furtheranceInformationDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        furtheranceInformationDTO.setContractReferenceTxt(contractReferenceTxt);
        furtheranceInformationDTO.setCppFurtheranceSeq(cppFurtheranceSeq);
        furtheranceInformationDTO.setFurtheranceEffectiveDate(new SimpleDateFormat("MM/dd/yyyy").parse("01/11/9999"));
        furtheranceInformationDTO.setFurtheranceStatusCode(furtheranceStatusCode);
        furtheranceInformationDTO.setParentCLMAgreementId(parentAgreementId);

        target.updateCPPFurtherance(furtheranceInformationDTO, USER_NAME);

    }

    @ExpectedDatabase(value = "CppFurtheranceRepositoryIntegrationTest.shouldUpdateFurtheranceDocumentGuidAndStatus-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "CppFurtheranceRepositoryIntegrationTest.xml")
    @Test
    public void shouldUpdateFurtheranceDocumentGuidAndStatus() throws Exception {
        int cppFurtheranceSeq = -1000;
        String furtheranceDocumentGuid = "GUID Updated";
        String userName = "updated user";

        target.updateFurtheranceStatusToSavedWithGUID(cppFurtheranceSeq, furtheranceDocumentGuid, userName);

    }

    @ExpectedDatabase(value = "CppFurtheranceRepositoryIntegrationTest.shouldSaveCPPFurtherance-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "CppFurtheranceRepositoryIntegrationTest.xml")
    @Test
    public void shouldSaveCPPFurtherance() throws Exception {
        int furtheranceStatusCode = 1;
        String parentAgreementId = "parent-clm-agreement-id";
        int cppFurtheranceSeq = -1001;
        String contractReferenceTxt = "reference";
        int contractPriceProfileSeq = -2000;
        String changeReasonTxt = "change-reason";

        FurtheranceInformationDTO furtheranceInformationDTO = new FurtheranceInformationDTO();
        furtheranceInformationDTO.setChangeReasonTxt(changeReasonTxt);
        furtheranceInformationDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        furtheranceInformationDTO.setContractReferenceTxt(contractReferenceTxt);
        furtheranceInformationDTO.setCppFurtheranceSeq(cppFurtheranceSeq);
        furtheranceInformationDTO.setFurtheranceEffectiveDate(new SimpleDateFormat("MM/dd/yyyy").parse("01/11/9999"));
        furtheranceInformationDTO.setFurtheranceStatusCode(furtheranceStatusCode);
        furtheranceInformationDTO.setParentCLMAgreementId(parentAgreementId);

        target.saveCPPFurtherance(furtheranceInformationDTO, USER_NAME);
    }

    @DatabaseSetup(value = "CppFurtheranceRepositoryIntegrationTest.shouldFetchFurtheranceDetailsByCppFurtheranceSeq.xml")
    @Test
    public void shouldFetchFurtheranceDetailsByCppFurtheranceSeq() throws Exception {
        FurtheranceInformationDTO result = target.fetchFurtheranceDetailsByFurtheranceSeq(-1000);
        assertThat(result.getContractPriceProfileSeq(), is(-2000));
        assertThat(result.getFurtheranceStatusCode(), is(FurtheranceStatus.FURTHERANCE_ACTIVATED.getCode()));
        assertThat(result.getParentCLMAgreementId(), is("parent-clm-agreement-id"));
    }

    @ExpectedDatabase(value = "CppFurtheranceRepositoryIntegrationTest.shouldUpdateCPPFurtheranceStatusToPricingActivated-result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "CppFurtheranceRepositoryIntegrationTest.shouldFetchFurtheranceDetailsByCppFurtheranceSeq.xml")
    @Test
    public void shouldUpdateCPPFurtheranceStatusToPricingActivated() throws ParseException {
        FurtheranceInformationDTO furtheranceInformationDTO = buildFurtheranceInformationDTO();
        String userName = "UpdatedUser";
        target.updateCPPFurtheranceStatusToPricingActivated(furtheranceInformationDTO, userName);

    }

    @DatabaseSetup(value = "CppFurtheranceRepositoryIntegrationTest.shouldFetchFurtheranceDetailsByFurtheranceSeq.xml")
    @Test
    public void shouldFetchFurtheranceStatusByFurtheranceSeq() throws Exception {
        int furtheranceSeq = -1002;
        int contractPriceProfileSeq = -2002;
        Integer actualFurtheranceInfo = target.fetchCPPFurtheranceStatus(contractPriceProfileSeq, furtheranceSeq);

        assertThat(actualFurtheranceInfo, notNullValue());

        assertThat(actualFurtheranceInfo, equalTo(1));
    }

    @DatabaseSetup(value = "CppFurtheranceRepositoryIntegrationTest.shouldFetchFurtheranceDetailsByFurtheranceSeq.xml")
    @Test
    public void shouldFetchFurtheranceStatusByFurtheranceSeqReturnNull() throws Exception {
        int furtheranceSeq = -1012;
        int contractPriceProfileSeq = -1212;
        Integer actualFurtheranceInfo = target.fetchCPPFurtheranceStatus(contractPriceProfileSeq, furtheranceSeq);

        assertThat(actualFurtheranceInfo, nullValue());

    }

    private FurtheranceInformationDTO buildFurtheranceInformationDTO() throws ParseException {
        int furtheranceStatusCode = 1;
        String parentAgreementId = "parent-clm-agreement-id";
        int cppFurtheranceSeq = -1001;
        String contractReferenceTxt = "reference";
        int contractPriceProfileSeq = -2000;
        String changeReasonTxt = "change-reason";
        FurtheranceInformationDTO furtheranceInformationDTO = new FurtheranceInformationDTO();
        furtheranceInformationDTO.setChangeReasonTxt(changeReasonTxt);
        furtheranceInformationDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        furtheranceInformationDTO.setContractReferenceTxt(contractReferenceTxt);
        furtheranceInformationDTO.setCppFurtheranceSeq(cppFurtheranceSeq);
        furtheranceInformationDTO.setFurtheranceEffectiveDate(new SimpleDateFormat("MM/dd/yyyy").parse("01/11/9999"));
        furtheranceInformationDTO.setFurtheranceStatusCode(furtheranceStatusCode);
        furtheranceInformationDTO.setParentCLMAgreementId(parentAgreementId);
        return furtheranceInformationDTO;
    }
}
