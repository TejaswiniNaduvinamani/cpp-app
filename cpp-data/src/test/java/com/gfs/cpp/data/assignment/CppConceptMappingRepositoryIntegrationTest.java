package com.gfs.cpp.data.assignment;

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
import com.gfs.cpp.common.dto.assignments.DuplicateCustomerDTO;
import com.gfs.cpp.common.dto.assignments.RealCustomerDTO;
import com.gfs.cpp.common.model.assignments.CustomerAssignmentDO;
import com.gfs.cpp.data.assignment.CppConceptMappingRepository;
import com.gfs.cpp.data.common.AbstractRepositoryIntegrationTest;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;

public class CppConceptMappingRepositoryIntegrationTest extends AbstractRepositoryIntegrationTest {

    private static final String USER_NAME = "test user";

    @Autowired
    private CppConceptMappingRepository target;

    @ExpectedDatabase(value = "CppConceptMappingRepositoryIntegrationTest.shouldSaveAssignments.xml", assertionMode = NON_STRICT_UNORDERED)
    @Test
    public void shouldSaveAssignments() throws Exception {

        List<CustomerAssignmentDO> customerAssignmentDOList = new ArrayList<>();

        CustomerAssignmentDO customerAssignmentDO1 = new CustomerAssignmentDO();
        customerAssignmentDO1.setCppCustomerSeq(-101);
        customerAssignmentDO1.setGfsCustomerId("-1001");
        customerAssignmentDO1.setGfsCustomerType(36);

        CustomerAssignmentDO customerAssignmentDO2 = new CustomerAssignmentDO();
        customerAssignmentDO2.setCppCustomerSeq(-102);
        customerAssignmentDO2.setGfsCustomerId("-1002");
        customerAssignmentDO2.setGfsCustomerType(36);

        customerAssignmentDOList.add(customerAssignmentDO1);
        customerAssignmentDOList.add(customerAssignmentDO2);

        target.saveAssignments(customerAssignmentDOList, USER_NAME);
    }

    @DatabaseSetup(value = "CppConceptMappingRepositoryIntegrationTest.shouldFetchRealCustomersMapped.xml")
    @Test
    public void shouldFetchRealCustomersMapped() throws Exception {

        List<RealCustomerDTO> realCustomerDTOList = target.fetchRealCustomersMapped(-101);

        assertThat(realCustomerDTOList.size(), equalTo(2));
        assertThat(realCustomerDTOList.get(0).getRealCustomerId(), equalTo("-1001"));
        assertThat(realCustomerDTOList.get(0).getRealCustomerType(), equalTo(36));
        assertThat(realCustomerDTOList.get(1).getRealCustomerId(), equalTo("-1002"));
        assertThat(realCustomerDTOList.get(1).getRealCustomerType(), equalTo(36));
    }

    @DatabaseSetup(value = "CppConceptMappingRepositoryIntegrationTest.shouldFetchRealCustomersMappedForCppSeq.xml")
    @Test
    public void shouldFetchRealCustomersMappedForCppSeq() throws Exception {

        List<RealCustomerDTO> realCustomerDTOList = target.fetchRealCustomersMappedForCppSeq(-101);

        assertThat(realCustomerDTOList.size(), equalTo(2));
        assertThat(realCustomerDTOList.get(0).getRealCustomerId(), equalTo("-1001"));
        assertThat(realCustomerDTOList.get(0).getRealCustomerType(), equalTo(36));
        assertThat(realCustomerDTOList.get(1).getRealCustomerId(), equalTo("-1002"));
        assertThat(realCustomerDTOList.get(1).getRealCustomerType(), equalTo(36));
    }

    @ExpectedDatabase(value = "CppConceptMappingRepositoryIntegrationTest.shouldDeleteCustomer-Result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "CppConceptMappingRepositoryIntegrationTest.shouldDeleteCustomer.xml")
    @Test
    public void shouldDeleteCustomer() throws Exception {

        CustomerAssignmentDO customerAssignmentDO = new CustomerAssignmentDO();
        customerAssignmentDO.setGfsCustomerId("-1001");
        customerAssignmentDO.setGfsCustomerType(36);

        target.deleteCustomerMapping(customerAssignmentDO);

    }

    @DatabaseSetup(value = "CppConceptMappingRepositoryIntegrationTest.shouldValidateForDefaultConcept.xml")
    @Test
    public void shouldValidateCustomerMappingForDefaultConcept() throws Exception {
        int contractPriceProfileSeq = -1001;

        RealCustomerDTO realCustomerDTO = target.fetchRealCustomerMappedToDefaultConcept(contractPriceProfileSeq);

        assertThat(realCustomerDTO.getRealCustomerId(), equalTo("-1002"));
        assertThat(realCustomerDTO.getRealCustomerType(), equalTo(36));
    }

    @DatabaseSetup(value = "CppConceptMappingRepositoryIntegrationTest.shouldValidateCustomerOnOtherActiveContracts.xml")
    @Test
    public void shouldValidateCustomerOnOtherActiveContractsForDraft() throws Exception {
        int contractPriceProfileId = -1001;
        String gfsCustomerId = "-1001";
        int gfsCustomerType = 36;
        Date clmContractStartDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("02/01/2018");
        Date clmContractEndDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("02/01/2019");

        List<DuplicateCustomerDTO> realCustomerValidationDTOList = target.fetchDuplicateCustomersAcrossOtherActiveContracts(contractPriceProfileId,
                gfsCustomerId, gfsCustomerType, clmContractStartDate, clmContractEndDate);

        assertThat(realCustomerValidationDTOList.get(0).getContractPriceProfileSeq(), equalTo(-1002));
        assertThat(realCustomerValidationDTOList.get(0).getCustomerId(), equalTo("-1001"));
        assertThat(realCustomerValidationDTOList.get(0).getCustomerType(), equalTo(36));
    }

    @DatabaseSetup(value = "CppConceptMappingRepositoryIntegrationTest.shouldValidateCustomerOnOtherActiveContracts.xml")
    @Test
    public void shouldValidateCustomerOnOtherActiveContractsForHoldStatus() throws Exception {
        int contractPriceProfileId = -1008;
        String gfsCustomerId = "-1002";
        int gfsCustomerType = 36;
        Date clmContractStartDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/2018");
        Date clmContractEndDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/2019");

        List<DuplicateCustomerDTO> realCustomerValidationDTOList = target.fetchDuplicateCustomersAcrossOtherActiveContracts(contractPriceProfileId,
                gfsCustomerId, gfsCustomerType, clmContractStartDate, clmContractEndDate);

        assertThat(realCustomerValidationDTOList.get(0).getContractPriceProfileSeq(), equalTo(-1003));
        assertThat(realCustomerValidationDTOList.get(0).getCustomerId(), equalTo("-1002"));
        assertThat(realCustomerValidationDTOList.get(0).getCustomerType(), equalTo(36));
    }

    @DatabaseSetup(value = "CppConceptMappingRepositoryIntegrationTest.shouldValidateCustomerOnOtherActiveContracts.xml")
    @Test
    public void shouldValidateCustomerOnOtherActiveContractsForWaitingForApprovalStatus() throws Exception {
        int contractPriceProfileId = -1009;
        String gfsCustomerId = "-1003";
        int gfsCustomerType = 36;
        Date clmContractStartDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/2018");
        Date clmContractEndDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/2019");

        List<DuplicateCustomerDTO> realCustomerValidationDTOList = target.fetchDuplicateCustomersAcrossOtherActiveContracts(contractPriceProfileId,
                gfsCustomerId, gfsCustomerType, clmContractStartDate, clmContractEndDate);

        assertThat(realCustomerValidationDTOList.get(0).getContractPriceProfileSeq(), equalTo(-1004));
        assertThat(realCustomerValidationDTOList.get(0).getCustomerId(), equalTo("-1003"));
        assertThat(realCustomerValidationDTOList.get(0).getCustomerType(), equalTo(36));
    }

    @DatabaseSetup(value = "CppConceptMappingRepositoryIntegrationTest.shouldValidateCustomerOnOtherActiveContracts.xml")
    @Test
    public void shouldValidateCustomerOnOtherActiveContractsForContractApprovedStatus() throws Exception {
        int contractPriceProfileId = -1010;
        String gfsCustomerId = "-1004";
        int gfsCustomerType = 36;
        Date clmContractStartDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/2018");
        Date clmContractEndDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/2019");

        List<DuplicateCustomerDTO> realCustomerValidationDTOList = target.fetchDuplicateCustomersAcrossOtherActiveContracts(contractPriceProfileId,
                gfsCustomerId, gfsCustomerType, clmContractStartDate, clmContractEndDate);

        assertThat(realCustomerValidationDTOList.get(0).getContractPriceProfileSeq(), equalTo(-1005));
        assertThat(realCustomerValidationDTOList.get(0).getCustomerId(), equalTo("-1004"));
        assertThat(realCustomerValidationDTOList.get(0).getCustomerType(), equalTo(36));
    }

    @DatabaseSetup(value = "CppConceptMappingRepositoryIntegrationTest.shouldValidateCustomerOnOtherActiveContracts.xml")
    @Test
    public void shouldValidateCustomerOnOtherActiveContractsForPricingActivatedStatus() throws Exception {
        int contractPriceProfileId = -1011;
        String gfsCustomerId = "-1005";
        int gfsCustomerType = 36;
        Date clmContractStartDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/2018");
        Date clmContractEndDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/2019");

        List<DuplicateCustomerDTO> realCustomerValidationDTOList = target.fetchDuplicateCustomersAcrossOtherActiveContracts(contractPriceProfileId,
                gfsCustomerId, gfsCustomerType, clmContractStartDate, clmContractEndDate);

        assertThat(realCustomerValidationDTOList.get(0).getContractPriceProfileSeq(), equalTo(-1006));
        assertThat(realCustomerValidationDTOList.get(0).getCustomerId(), equalTo("-1005"));
        assertThat(realCustomerValidationDTOList.get(0).getCustomerType(), equalTo(36));
    }

    @DatabaseSetup(value = "CppConceptMappingRepositoryIntegrationTest.shouldValidateCustomerOnOtherActiveContracts.xml")
    @Test
    public void shouldValidateCustomerOnOtherActiveContractsNoResultsWhenContractExpired() throws Exception {
        int contractPriceProfileId = -1012;
        String gfsCustomerId = "-1006";
        int gfsCustomerType = 36;
        Date clmContractStartDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/2018");
        Date clmContractEndDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/2019");

        List<DuplicateCustomerDTO> realCustomerValidationDTOList = target.fetchDuplicateCustomersAcrossOtherActiveContracts(contractPriceProfileId,
                gfsCustomerId, gfsCustomerType, clmContractStartDate, clmContractEndDate);

        assertThat(realCustomerValidationDTOList.size(), equalTo(0));
    }

    @DatabaseSetup(value = "CppConceptMappingRepositoryIntegrationTest.shouldValidateCustomerOnOtherActiveContracts.xml")
    @Test
    public void shouldValidateCustomerOnOtherActiveContractsNoResultsWhenDateNotOverlapping() throws Exception {
        int contractPriceProfileId = -1011;
        String gfsCustomerId = "-1005";
        int gfsCustomerType = 36;
        Date clmContractStartDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/2020");
        Date clmContractEndDate = new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse("01/01/2021");

        List<DuplicateCustomerDTO> realCustomerValidationDTOList = target.fetchDuplicateCustomersAcrossOtherActiveContracts(contractPriceProfileId,
                gfsCustomerId, gfsCustomerType, clmContractStartDate, clmContractEndDate);

        assertThat(realCustomerValidationDTOList.size(), equalTo(0));
    }

    @DatabaseSetup(value = "CppConceptMappingRepositoryIntegrationTest.shouldValidateForDefaultConcept.xml")
    @Test
    public void shouldValidateAcrossConceptInSameContract() throws Exception {
        int contractPriceProfileSeq = -1001;
        String gfsCustomerId = "-1002";
        int gfsCustomerType = 36;

        List<DuplicateCustomerDTO> realCustomerValidationDTOList = target.fetchDuplicateCustomersAcrossConcepts(contractPriceProfileSeq,
                gfsCustomerId, gfsCustomerType);
        DuplicateCustomerDTO realCustomerValidationDTO = realCustomerValidationDTOList.get(0);

        assertThat(realCustomerValidationDTO.getContractPriceProfileSeq(), equalTo(-1001));
        assertThat(realCustomerValidationDTO.getCustomerId(), equalTo("-1002"));
        assertThat(realCustomerValidationDTO.getCustomerType(), equalTo(36));
    }

    @DatabaseSetup(value = "CppConceptMappingRepositoryIntegrationTest.shouldValidateForDefaultConcept.xml")
    @Test
    public void shouldReturnNullWhenValidateAcrossConceptInSameContract() throws Exception {
        int contractPriceProfileSeq = -1002;
        String gfsCustomerId = "-1001";
        int gfsCustomerType = 36;
        List<DuplicateCustomerDTO> realCustomerValidationDTOList = target.fetchDuplicateCustomersAcrossConcepts(contractPriceProfileSeq,
                gfsCustomerId, gfsCustomerType);

        assertThat(realCustomerValidationDTOList.size(), equalTo(0));
    }

    @ExpectedDatabase(value = "CppConceptMappingRepositoryIntegrationTest.shouldDeleteCustomerMapping-Result.xml", assertionMode = NON_STRICT_UNORDERED)
    @DatabaseSetup(value = "CppConceptMappingRepositoryIntegrationTest.shouldDeleteAllCustomerMapping.xml")
    @Test
    public void shouldDeleteAllCustomerMappings() throws Exception {

        int contractPriceProfileSeq = -1001;

        target.deleteAllCustomerMapping(contractPriceProfileSeq);

    }

    @DatabaseSetup(value = "CppConceptMappingRepositoryIntegrationTest.shouldValidateForDefaultConcept.xml")
    @Test
    public void shouldReturnNullWhenValidateCustomerMappingForDefaultConcept() throws Exception {
        int contractPriceProfileSeq = -1003;

        RealCustomerDTO realCustomerDTO = target.fetchRealCustomerMappedToDefaultConcept(contractPriceProfileSeq);

        assertThat(realCustomerDTO, equalTo(null));
    }

    @DatabaseSetup(value = "CppConceptMappingRepositoryIntegrationTest.shouldFetchRealCustomerMappedToDefaultCmg.xml")
    @Test
    public void shouldFetchRealCustomerMappedToDefaultCmg() throws Exception {

        RealCustomerDTO actual = target.fetchRealCustomerMappedToDefaultConcept(-3001);

        assertThat(actual.getRealCustomerId(), equalTo("-1012"));
        assertThat(actual.getRealCustomerType(), equalTo(15));

    }
}
