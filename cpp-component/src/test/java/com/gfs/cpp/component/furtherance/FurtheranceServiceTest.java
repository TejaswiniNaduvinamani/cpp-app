package com.gfs.cpp.component.furtherance;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import com.gfs.corp.component.price.common.types.ItemPriceLevel;
import com.gfs.cpp.common.dto.assignments.FutureItemDescriptionDTO;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentWrapperDTO;
import com.gfs.cpp.common.dto.clm.ClmContractStatus;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceBaseDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceInformationDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceStatus;
import com.gfs.cpp.common.dto.furtherance.SplitCaseGridFurtheranceDTO;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.MarkupWrapperDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.dto.markup.SubgroupMarkupDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.model.markup.FutureItemDO;
import com.gfs.cpp.common.model.markup.MarkupWrapperDO;
import com.gfs.cpp.common.model.markup.ProductTypeMarkupDO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.common.util.StatusDTO;
import com.gfs.cpp.component.assignment.helper.ItemAssignmentBuilder;
import com.gfs.cpp.component.assignment.helper.ItemAssignmentHelper;
import com.gfs.cpp.component.markup.FutureItemDeleteHandler;
import com.gfs.cpp.component.markup.FutureItemUpdater;
import com.gfs.cpp.component.markup.ItemMarkupDeleteHandler;
import com.gfs.cpp.component.splitcase.SplitcasePersister;
import com.gfs.cpp.component.statusprocessor.FurtheranceStatusValidator;
import com.gfs.cpp.data.contractpricing.ClmContractTypeRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.data.furtherance.CppFurtheranceRepository;
import com.gfs.cpp.data.markup.CustomerItemDescPriceRepository;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;
import com.gfs.cpp.proxy.clm.ClmApiProxy;

@RunWith(MockitoJUnitRunner.class)
public class FurtheranceServiceTest {

    private static final String userName = "test user";
    private static final String itemPriceId = "1";
    private static final int gfsCustomerTypeCode = 31;
    private static final String gfsCustomerId = "1";
    private static final int cppFurtheranceSeq = 1;
    private static final int contractPriceProfileSeq = 1;
    private static final Date expirationDate = new LocalDate(2010, 01, 01).toDate();
    private static final String itemDesc = "item desc";

    @InjectMocks
    private FurtheranceService target;

    @Mock
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Mock
    private CPPDateUtils cppDateUtils;

    @Mock
    private FurtheranceDTOBuilder furtheranceDTOBuilder;

    @Mock
    private FutureItemDeleteHandler futureItemDeleteHandler;

    @Mock
    private FurtheranceStatusValidator furtheranceStatusValidator;

    @Mock
    private CppFurtheranceRepository cppFurtheranceRepository;

    @Mock
    private ClmContractTypeRepository clmContractTypeRepository;

    @Mock
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Mock
    private FurtherancePricingProcessor furtherancePricingProcessor;

    @Mock
    private FurtheranceChangeTracker furtheranceChangeTracker;

    @Mock
    private FurtheranceDocumentGenerator furtheranceDocumentGenerator;

    @Mock
    private File file;

    @Mock
    private CustomerItemDescPriceRepository customerItemDescPriceRepository;

    @Mock
    private ItemAssignmentHelper itemAssignmentHelper;

    @Mock
    private ClmApiProxy clmApiProxy;

    @Mock
    private ItemAssignmentBuilder itemAssignmentBuilder;

    @Mock
    private FutureItemUpdater futureItemUpdater;

    @Mock
    private FurtheranceMarkupSaver furtheranceMarkupSaver;

    @Mock
    private SplitcasePersister splitCasePersister;

    @Mock
    private ItemMarkupDeleteHandler itemMarkupDeleteHandler;

    @Test
    public void shouldDeleteItemLevelMarkupOfFutureItem() {

        target.deleteItemLevelMarkup(contractPriceProfileSeq, cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, null, itemDesc, userName);

        verify(futureItemDeleteHandler).deleteFutureItemWithAssignedItemsForFurtherance(contractPriceProfileSeq, cppFurtheranceSeq, gfsCustomerId,
                gfsCustomerTypeCode, itemDesc, userName);

    }

    @Test
    public void shouldDeleteItemLevelMarkup() {

        List<String> expireItemList = new ArrayList<>();
        expireItemList.add(itemPriceId);
        when(cppDateUtils.oneDayPreviousCurrentDate()).thenReturn(expirationDate);
        when(customerItemPriceRepository.fetchAlreadyExistingItemsOrSubgroups(expireItemList, gfsCustomerId, gfsCustomerTypeCode,
                contractPriceProfileSeq, ItemPriceLevel.ITEM.getCode())).thenReturn(expireItemList);

        target.deleteItemLevelMarkup(contractPriceProfileSeq, cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, itemPriceId, itemDesc, userName);

        verify(itemMarkupDeleteHandler).deleteItemMarkupForFurtherance(contractPriceProfileSeq, cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode,
                itemPriceId, userName);

    }

    @Test
    public void shouldFetchInprogressFurtheranceReturnTrueWhenInProgressFurtheranceFound() {
        String parentAgreementId = "parent-clm-agreement-id";
        FurtheranceInformationDTO latestFurtheranceInfo = new FurtheranceInformationDTO();

        doReturn(latestFurtheranceInfo).when(cppFurtheranceRepository).fetchInProgressFurtheranceDetailsByParentAgreementId(parentAgreementId);

        assertThat(target.hasInProgressFurtherance(parentAgreementId), equalTo(true));

        verify(cppFurtheranceRepository).fetchInProgressFurtheranceDetailsByParentAgreementId(parentAgreementId);

    }

    @Test
    public void shouldFetchInprogressFurtheranceReturnFalseWhenInProgressFurtheranceNotFound() {
        String parentAgreementId = "parent-clm-agreement-id";

        doReturn(null).when(cppFurtheranceRepository).fetchInProgressFurtheranceDetailsByParentAgreementId(parentAgreementId);

        assertThat(target.hasInProgressFurtherance(parentAgreementId), equalTo(false));

        verify(cppFurtheranceRepository).fetchInProgressFurtheranceDetailsByParentAgreementId(parentAgreementId);

    }

    @Test
    public void shouldFetchInProgressFurtheranceInfo() throws ParseException {
        int contractPriceProfileSeq = 1000;
        int cppFurtheranceSeq = 2000;
        String parentAgreementId = "parent-clm-agreement-id";
        String clmAgreementId = "clm-agreement-id";
        int contractTypeSeq = 10;
        String contractTypeName = "testContract";

        FurtheranceInformationDTO furtheranceInformationDTO = new FurtheranceInformationDTO();
        furtheranceInformationDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        furtheranceInformationDTO.setCppFurtheranceSeq(cppFurtheranceSeq);
        furtheranceInformationDTO.setParentCLMAgreementId(parentAgreementId);

        ContractPricingResponseDTO contractPriceProfileDto = new ContractPricingResponseDTO();
        contractPriceProfileDto.setClmParentAgreementId(parentAgreementId);
        contractPriceProfileDto.setContractPriceProfileSeq(contractPriceProfileSeq);
        contractPriceProfileDto.setClmContractTypeSeq(contractTypeSeq);
        contractPriceProfileDto.setClmAgreementId(clmAgreementId);

        FurtheranceBaseDTO furtheranceBaseDTO = new FurtheranceBaseDTO();
        furtheranceBaseDTO.setAgreementId(parentAgreementId);
        furtheranceBaseDTO.setCppFurtheranceSeq(cppFurtheranceSeq);
        furtheranceBaseDTO.setContractType(contractTypeName);

        doReturn(furtheranceBaseDTO).when(furtheranceDTOBuilder).buildFurtheranceBaseDTO(clmAgreementId, cppFurtheranceSeq, contractTypeName);
        doReturn(furtheranceInformationDTO).when(cppFurtheranceRepository).fetchInProgressFurtheranceDetailsByParentAgreementId(parentAgreementId);
        doReturn(contractPriceProfileDto).when(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);
        doReturn(contractTypeName).when(clmContractTypeRepository).getContractTypeName(contractTypeSeq);

        FurtheranceBaseDTO result = target.fetchInProgressFurtheranceInfo(parentAgreementId);

        furtheranceInformationDTO.setFurtheranceStatusCode(FurtheranceStatus.FURTHERANCE_INITIATED.getCode());

        assertThat(result.getAgreementId(), equalTo(parentAgreementId));
        assertThat(result.getContractType(), equalTo(contractTypeName));
        assertThat(result.getCppFurtheranceSeq(), equalTo(cppFurtheranceSeq));

        verify(furtheranceDTOBuilder).buildFurtheranceBaseDTO(clmAgreementId, cppFurtheranceSeq, contractTypeName);
        verify(cppFurtheranceRepository).fetchInProgressFurtheranceDetailsByParentAgreementId(parentAgreementId);
        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);
        verify(clmContractTypeRepository).getContractTypeName(contractTypeSeq);
    }

    @Test
    public void shouldCreateNewFurtherance() throws ParseException {
        int contractPriceProfileSeq = 1000;
        int cppFurtheranceSeq = 2000;
        String parentAgreementId = "parent-clm-agreement-id";
        String clmAgreementId = "clm-agreement-id";
        int contractTypeSeq = 10;
        String contractTypeName = "testContract";

        ContractPricingResponseDTO contractPriceProfileDto = new ContractPricingResponseDTO();
        contractPriceProfileDto.setClmParentAgreementId(parentAgreementId);
        contractPriceProfileDto.setContractPriceProfileSeq(contractPriceProfileSeq);
        contractPriceProfileDto.setClmContractTypeSeq(contractTypeSeq);
        contractPriceProfileDto.setClmAgreementId(parentAgreementId);

        FurtheranceBaseDTO furtheranceBaseDTO = new FurtheranceBaseDTO();
        furtheranceBaseDTO.setAgreementId(clmAgreementId);
        furtheranceBaseDTO.setCppFurtheranceSeq(cppFurtheranceSeq);
        furtheranceBaseDTO.setContractType(contractTypeName);

        doReturn(furtheranceBaseDTO).when(furtheranceDTOBuilder).buildFurtheranceBaseDTO(parentAgreementId, cppFurtheranceSeq, contractTypeName);
        doReturn(cppFurtheranceSeq).when(cppFurtheranceRepository).fetchCPPFurtheranceNextSequence();
        doReturn(contractPriceProfileDto).when(contractPriceProfileRepository)
                .fetchContractDetailsForLatestActivatedContractVersion(parentAgreementId);
        doReturn(contractTypeName).when(clmContractTypeRepository).getContractTypeName(contractTypeSeq);

        FurtheranceBaseDTO result = target.createNewFurtherance(parentAgreementId, contractTypeName);

        assertThat(result.getAgreementId(), equalTo(clmAgreementId));
        assertThat(result.getContractType(), equalTo(contractTypeName));
        assertThat(result.getCppFurtheranceSeq(), equalTo(cppFurtheranceSeq));

        verify(furtheranceDTOBuilder).buildFurtheranceBaseDTO(parentAgreementId, cppFurtheranceSeq, contractTypeName);
        verify(cppFurtheranceRepository).fetchCPPFurtheranceNextSequence();
        verify(contractPriceProfileRepository).fetchContractDetailsForLatestActivatedContractVersion(parentAgreementId);
        verify(clmContractTypeRepository).getContractTypeName(contractTypeSeq);
    }

    @Test
    public void shouldThrowExceptionWhenNoActivatedVersionForCreateNewFurtherance() throws ParseException {
        String parentAgreementId = "parent-clm-agreement-id";
        String contractTypeName = "testContract";

        doReturn(null).when(contractPriceProfileRepository).fetchContractDetailsForLatestActivatedContractVersion(parentAgreementId);

        try {
            target.createNewFurtherance(parentAgreementId, contractTypeName);
        } catch (CPPRuntimeException ex) {
            assertThat(ex.getType(), equalTo(CPPExceptionType.CANNOT_CREATE_FURTHERANCE));
            assertThat(ex.getMessage(), equalTo("Contract Status is not valid"));
        }

        verify(contractPriceProfileRepository).fetchContractDetailsForLatestActivatedContractVersion(parentAgreementId);
    }

    @Test
    public void shouldValidateActivatePricingForFurtherance() throws ParseException {
        int cppFurtheranceSeq = 2000;
        FurtheranceInformationDTO furtheranceInformationDTO = buildFurtheranceInformationDTO();

        when(cppFurtheranceRepository.fetchFurtheranceDetailsByFurtheranceSeq(cppFurtheranceSeq)).thenReturn(furtheranceInformationDTO);

        Map<String, Boolean> actual = target.validateActivatePricingForFurtherance(cppFurtheranceSeq, "test");

        assertThat(actual.get("enableActivatePricingButton"), equalTo(true));

        verify(furtheranceStatusValidator).validateIfActivatePricingCanBeEnabledForFurtherance(furtheranceInformationDTO, "test");
        verify(cppFurtheranceRepository).fetchFurtheranceDetailsByFurtheranceSeq(cppFurtheranceSeq);
    }

    @Test
    public void shouldReturnFalseFlagWhenNoFurtheranceInformatioSaved() throws ParseException {
        int cppFurtheranceSeq = 2000;

        when(cppFurtheranceRepository.fetchFurtheranceDetailsByFurtheranceSeq(cppFurtheranceSeq)).thenReturn(null);

        Map<String, Boolean> actual = target.validateActivatePricingForFurtherance(cppFurtheranceSeq, "test");

        assertThat(actual.get("enableActivatePricingButton"), equalTo(false));

        verify(cppFurtheranceRepository).fetchFurtheranceDetailsByFurtheranceSeq(cppFurtheranceSeq);
    }

    @Test
    public void shouldThrowCPPExceptionWhenValidateActivatePricingForFurtherance() throws ParseException {
        int cppFurtheranceSeq = 2000;
        Map<String, Boolean> actual = new HashMap<>();
        FurtheranceInformationDTO furtheranceInformationDTO = buildFurtheranceInformationDTO();

        when(cppFurtheranceRepository.fetchFurtheranceDetailsByFurtheranceSeq(cppFurtheranceSeq)).thenReturn(furtheranceInformationDTO);
        doThrow(new CPPRuntimeException(CPPExceptionType.INVALID_CONTRACT)).when(furtheranceStatusValidator)
                .validateIfActivatePricingCanBeEnabledForFurtherance(furtheranceInformationDTO, "test");

        actual = target.validateActivatePricingForFurtherance(cppFurtheranceSeq, "test");

        assertThat(actual.get("enableActivatePricingButton"), equalTo(false));

        verify(furtheranceStatusValidator).validateIfActivatePricingCanBeEnabledForFurtherance(furtheranceInformationDTO, "test");
        verify(cppFurtheranceRepository).fetchFurtheranceDetailsByFurtheranceSeq(cppFurtheranceSeq);
    }

    @Test
    public void shouldUpdateFurtheranceInformationIfFurtheranceAlreadySaved() throws ParseException {
        String userName = "test";
        int contractPriceProfileSeq = 1000;
        int cppFurtheranceSeq = 2000;
        String parentAgreementId = "parent-clm-agreement-id";

        FurtheranceInformationDTO savedFurtheranceInformationDTO = new FurtheranceInformationDTO();
        savedFurtheranceInformationDTO.setChangeReasonTxt("reason text");
        savedFurtheranceInformationDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        savedFurtheranceInformationDTO.setContractReferenceTxt("reference text");
        savedFurtheranceInformationDTO.setCppFurtheranceSeq(cppFurtheranceSeq);
        savedFurtheranceInformationDTO.setFurtheranceEffectiveDate(new LocalDate(2018, 01, 01).toDate());
        savedFurtheranceInformationDTO.setFurtheranceStatusCode(1);
        savedFurtheranceInformationDTO.setParentCLMAgreementId(parentAgreementId);

        FurtheranceInformationDTO furtheranceInformationDTO = new FurtheranceInformationDTO();
        furtheranceInformationDTO.setChangeReasonTxt("reason text updated");
        furtheranceInformationDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        furtheranceInformationDTO.setContractReferenceTxt("reference text updated");
        savedFurtheranceInformationDTO.setFurtheranceEffectiveDate(new LocalDate(2018, 8, 01).toDate());
        furtheranceInformationDTO.setCppFurtheranceSeq(cppFurtheranceSeq);

        when(cppFurtheranceRepository.fetchFurtheranceDetailsByFurtheranceSeq(cppFurtheranceSeq)).thenReturn(savedFurtheranceInformationDTO);

        target.saveFurtheranceInformation(furtheranceInformationDTO, userName);

        furtheranceInformationDTO.setFurtheranceStatusCode(FurtheranceStatus.FURTHERANCE_INITIATED.getCode());

        verify(cppFurtheranceRepository).fetchFurtheranceDetailsByFurtheranceSeq(cppFurtheranceSeq);
        verify(cppFurtheranceRepository).updateCPPFurtherance(furtheranceInformationDTO, userName);
        verify(cppFurtheranceRepository, times(0)).saveCPPFurtherance(furtheranceInformationDTO, userName);
    }

    @Test
    public void shouldSaveFurtheranceInformationIfNewFurtherance() throws ParseException {
        String userName = "test";
        int cppFurtheranceSeq = 2000;
        String parentAgreementId = "parent-clm-agreement-id";
        FurtheranceInformationDTO furtheranceInformationDTO = new FurtheranceInformationDTO();
        furtheranceInformationDTO.setCppFurtheranceSeq(cppFurtheranceSeq);
        furtheranceInformationDTO.setParentCLMAgreementId(parentAgreementId);

        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setClmParentAgreementId(parentAgreementId);
        contractPricingResponseDTO.setClmAgreementId("clm-agreement-id");
        contractPricingResponseDTO.setContractPriceProfileSeq(100);

        ContractPricingResponseDTO contractPriceProfileDto = new ContractPricingResponseDTO();
        contractPriceProfileDto.setClmParentAgreementId(parentAgreementId);
        contractPriceProfileDto.setContractPriceProfileSeq(100);

        doReturn(contractPriceProfileDto).when(contractPriceProfileRepository)
                .fetchContractDetailsForLatestActivatedContractVersion(parentAgreementId);
        when(cppFurtheranceRepository.fetchFurtheranceDetailsByFurtheranceSeq(cppFurtheranceSeq)).thenReturn(null);

        target.saveFurtheranceInformation(furtheranceInformationDTO, userName);

        furtheranceInformationDTO.setFurtheranceStatusCode(FurtheranceStatus.FURTHERANCE_INITIATED.getCode());

        verify(cppFurtheranceRepository).fetchFurtheranceDetailsByFurtheranceSeq(cppFurtheranceSeq);
        verify(cppFurtheranceRepository).saveCPPFurtherance(furtheranceInformationDTO, userName);
        verify(cppFurtheranceRepository, times(0)).updateCPPFurtherance(furtheranceInformationDTO, userName);
    }

    @Test
    public void shouldActivatePricingForFurtherance() {
        int cppFurtheranceSeq = -1;
        String userName = "test";
        String clmContractStatus = ClmContractStatus.EXECUTED.value;
        Date expirationDateForExistingPricing = new LocalDate(2017, 12, 31).toDate();
        Date newEffectiveDate = new LocalDate(2018, 01, 01).toDate();
        Date currentDate = new LocalDate(2018, 01, 01).toDate();
        Date pricingExpirationDate = new LocalDate(2019, 01, 01).toDate();

        FurtheranceInformationDTO furtheranceInformationDTO = buildFurtheranceInformationDTO();

        ContractPricingResponseDTO contractDetails = getContractPricingResponseDTO();

        when(cppDateUtils.getCurrentDateAsLocalDate()).thenReturn(new LocalDate(2018, 01, 01));
        when(cppDateUtils.getPreviousDate(newEffectiveDate)).thenReturn(expirationDateForExistingPricing);
        when(contractPriceProfileRepository.fetchContractDetailsByCppSeq(furtheranceInformationDTO.getContractPriceProfileSeq()))
                .thenReturn(contractDetails);
        when(cppFurtheranceRepository.fetchFurtheranceDetailsByFurtheranceSeq(cppFurtheranceSeq)).thenReturn(furtheranceInformationDTO);
        when(cppDateUtils.getCurrentDate()).thenReturn(currentDate);

        target.activatePricingForFurtherance(cppFurtheranceSeq, userName, clmContractStatus);

        verify(cppFurtheranceRepository).fetchFurtheranceDetailsByFurtheranceSeq(cppFurtheranceSeq);
        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(furtheranceInformationDTO.getContractPriceProfileSeq());
        verify(furtheranceStatusValidator).validateIfPricingCanBeActivated(pricingExpirationDate, clmContractStatus, currentDate,
                furtheranceInformationDTO);

    }

    @Test
    public void shouldFetchDefaultFurtheranceInformation() {
        String parentAgreementId = "parent-clm-agreement-id";
        int cppFurtheranceSeq = 1001;

        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setClmAgreementId(parentAgreementId);
        contractPricingResponseDTO.setClmContractEndDate(new LocalDate(2018, 01, 01).toDate());
        contractPricingResponseDTO.setClmContractStartDate(new LocalDate(2018, 01, 01).toDate());
        contractPricingResponseDTO.setClmContractTypeSeq(1);
        contractPricingResponseDTO.setClmParentAgreementId(parentAgreementId);
        contractPricingResponseDTO.setContractPriceProfileId(1);
        contractPricingResponseDTO.setContractPriceProfStatusCode(2);
        contractPricingResponseDTO.setExpireLowerLevelInd(1);
        contractPricingResponseDTO.setPricingEffectiveDate(new LocalDate(2018, 01, 01).toDate());
        contractPricingResponseDTO.setPricingExhibitSysId("1234");
        contractPricingResponseDTO.setPricingExpirationDate(new LocalDate(2018, 01, 01).toDate());
        contractPricingResponseDTO.setVersionNumber(1);

        FurtheranceInformationDTO furtheranceInformationDTO = new FurtheranceInformationDTO();
        furtheranceInformationDTO.setCppFurtheranceSeq(cppFurtheranceSeq);
        furtheranceInformationDTO.setParentCLMAgreementId(parentAgreementId);
        furtheranceInformationDTO.setFurtheranceStatusCode(1);

        when(cppFurtheranceRepository.fetchFurtheranceDetailsByFurtheranceSeq(cppFurtheranceSeq)).thenReturn(null);
        when(furtheranceDTOBuilder.buildDefaultFurtheranceDTO(parentAgreementId, cppFurtheranceSeq)).thenReturn(furtheranceInformationDTO);

        FurtheranceInformationDTO actual = target.fetchFurtheranceInformation(parentAgreementId, 1001);

        assertThat(actual.getCppFurtheranceSeq(), equalTo(1001));
        assertThat(actual.getParentCLMAgreementId(), equalTo(parentAgreementId));

        verify(cppFurtheranceRepository).fetchFurtheranceDetailsByFurtheranceSeq(cppFurtheranceSeq);
        verify(furtheranceDTOBuilder).buildDefaultFurtheranceDTO(parentAgreementId, cppFurtheranceSeq);

    }

    @Test
    public void shouldFetchSavedFurtheranceInformation() {
        String parentAgreementId = "parent-clm-agreement-id";
        FurtheranceInformationDTO furtheranceInformationDTO = new FurtheranceInformationDTO();
        furtheranceInformationDTO.setChangeReasonTxt("reason text");
        furtheranceInformationDTO.setContractPriceProfileSeq(1);
        furtheranceInformationDTO.setContractReferenceTxt("reference text");
        furtheranceInformationDTO.setCppFurtheranceSeq(1);
        furtheranceInformationDTO.setFurtheranceDocumentGUID("1234");
        furtheranceInformationDTO.setFurtheranceEffectiveDate(new LocalDate(2018, 01, 01).toDate());
        furtheranceInformationDTO.setFurtheranceStatusCode(2);
        furtheranceInformationDTO.setParentCLMAgreementId(parentAgreementId);

        when(cppFurtheranceRepository.fetchFurtheranceDetailsByFurtheranceSeq(1)).thenReturn(furtheranceInformationDTO);

        FurtheranceInformationDTO actual = target.fetchFurtheranceInformation(parentAgreementId, 1);

        assertThat(actual.getChangeReasonTxt(), equalTo("reason text"));
        assertThat(actual.getContractPriceProfileSeq(), equalTo(1));
        assertThat(actual.getContractReferenceTxt(), equalTo("reference text"));
        assertThat(actual.getCppFurtheranceSeq(), equalTo(1));
        assertThat(actual.getFurtheranceDocumentGUID(), equalTo("1234"));
        assertThat(actual.getFurtheranceEffectiveDate(), equalTo(new LocalDate(2018, 01, 01).toDate()));
        assertThat(actual.getFurtheranceStatusCode(), equalTo(2));
        assertThat(actual.getParentCLMAgreementId(), equalTo(parentAgreementId));

        verify(cppFurtheranceRepository).fetchFurtheranceDetailsByFurtheranceSeq(1);
    }

    @Test
    public void shouldActivatePricingForFurtheranceExceptionTest() {
        int cppFurtheranceSeq = -1;
        String userName = "test";
        String clmContractStatus = ClmContractStatus.APPROVED.value;
        Date expirationDateForExistingPricing = new LocalDate(2017, 12, 31).toDate();
        Date newEffectiveDate = new LocalDate(2018, 01, 01).toDate();
        Date pricingExpirationDate = new LocalDate(2019, 01, 01).toDate();
        Date currentDate = new LocalDate(2018, 01, 01).toDate();

        FurtheranceInformationDTO furtheranceInformationDTO = buildFurtheranceInformationDTO();

        ContractPricingResponseDTO contractDetails = getContractPricingResponseDTO();

        when(cppDateUtils.getCurrentDateAsLocalDate()).thenReturn(new LocalDate(2018, 01, 01));
        when(cppDateUtils.getPreviousDate(newEffectiveDate)).thenReturn(expirationDateForExistingPricing);
        when(contractPriceProfileRepository.fetchContractDetailsByCppSeq(furtheranceInformationDTO.getContractPriceProfileSeq()))
                .thenReturn(contractDetails);
        when(cppFurtheranceRepository.fetchFurtheranceDetailsByFurtheranceSeq(cppFurtheranceSeq)).thenReturn(furtheranceInformationDTO);

        when(cppDateUtils.getCurrentDate()).thenReturn(currentDate);

        doThrow(new CPPRuntimeException(CPPExceptionType.INVALID_CONTRACT)).when(furtheranceStatusValidator)
                .validateIfPricingCanBeActivated(pricingExpirationDate, clmContractStatus, currentDate, furtheranceInformationDTO);

        try {
            target.activatePricingForFurtherance(cppFurtheranceSeq, userName, clmContractStatus);
            fail("Expected exception");
        } catch (CPPRuntimeException ex) {
            assertThat(ex.getType(), equalTo(CPPExceptionType.INVALID_CONTRACT));
        }
        verify(cppFurtheranceRepository).fetchFurtheranceDetailsByFurtheranceSeq(cppFurtheranceSeq);
        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(furtheranceInformationDTO.getContractPriceProfileSeq());
    }

    @Test
    public void shouldSaveSplitcaseFee() throws Exception {
        SplitCaseGridFurtheranceDTO splitCaseGridDTO = new SplitCaseGridFurtheranceDTO();
        String userName = "vc71u";
        splitCaseGridDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        splitCaseGridDTO.setCppFurtheranceSeq(cppFurtheranceSeq);

        target.saveSplitCaseFeeForFurtherance(splitCaseGridDTO, userName);

        verify(furtheranceStatusValidator).validateIfFurtheranceEditableStatus(contractPriceProfileSeq, cppFurtheranceSeq);
        verify(splitCasePersister).saveSplicateForFurtherance(splitCaseGridDTO, userName);

    }

    @Test
    public void shouldCreateFurtheranceDocument() throws InvalidFormatException, IOException {
        int cppFurtheranceSeq = 1;
        when(furtheranceDocumentGenerator.createFurtheranceDocument(cppFurtheranceSeq)).thenReturn(file);

        File resultFile = target.createFurtheranceDocument(cppFurtheranceSeq);

        assertThat(resultFile, equalTo(file));

        verify(furtheranceDocumentGenerator).createFurtheranceDocument(cppFurtheranceSeq);
    }

    @Test
    public void shouldSavePricingDocumentForFurtherance() throws InvalidFormatException, IOException {
        FurtheranceBaseDTO furtheranceBaseDTO = buildFurtheranceBaseDTO();
        FurtheranceInformationDTO furtheranceInformationDTO = buildFurtheranceInformationDTO();
        String furtheranceDocumentGuid = "Test Furtherance Document GUID";
        String userName = "test user";

        when(furtheranceDocumentGenerator.createFurtheranceDocument(cppFurtheranceSeq)).thenReturn(file);
        when(cppFurtheranceRepository.fetchFurtheranceDetailsByFurtheranceSeq(cppFurtheranceSeq)).thenReturn(furtheranceInformationDTO);
        when(clmApiProxy.saveFurtheranceExhibit(file, furtheranceBaseDTO.getAgreementId(), furtheranceDocumentGuid,
                furtheranceBaseDTO.getContractType())).thenReturn(furtheranceDocumentGuid);

        target.savePricingDocumentForFurtherance(furtheranceBaseDTO, userName);

        verify(furtheranceDocumentGenerator).createFurtheranceDocument(cppFurtheranceSeq);
        verify(cppFurtheranceRepository, atLeastOnce()).fetchFurtheranceDetailsByFurtheranceSeq(furtheranceBaseDTO.getCppFurtheranceSeq());
        verify(clmApiProxy).saveFurtheranceExhibit(file, furtheranceBaseDTO.getAgreementId(), furtheranceDocumentGuid,
                furtheranceBaseDTO.getContractType());
        verify(cppFurtheranceRepository).updateFurtheranceStatusToSavedWithGUID(furtheranceBaseDTO.getCppFurtheranceSeq(), furtheranceDocumentGuid,
                userName);

    }

    @Test
    public void shouldThrowExceptionForSavePricingDocumentForFurtheranceWhenNoFileGenerated() throws InvalidFormatException, IOException {
        FurtheranceBaseDTO furtheranceBaseDTO = buildFurtheranceBaseDTO();
        FurtheranceInformationDTO furtheranceInformationDTO = buildFurtheranceInformationDTO();

        when(furtheranceDocumentGenerator.createFurtheranceDocument(cppFurtheranceSeq)).thenReturn(null);

        when(cppFurtheranceRepository.fetchFurtheranceDetailsByFurtheranceSeq(furtheranceBaseDTO.getCppFurtheranceSeq()))
                .thenReturn(furtheranceInformationDTO);
        try {
            target.savePricingDocumentForFurtherance(furtheranceBaseDTO, userName);
        } catch (CPPRuntimeException e) {
            assertThat(e.getErrorCode(), equalTo(CPPExceptionType.SAVE_EXHIBIT_SERVICE_FAILED.getErrorCode()));

            verify(furtheranceDocumentGenerator).createFurtheranceDocument(cppFurtheranceSeq);
        }
    }

    @Test
    public void shouldFetchMappedItemsForFurtherance() {

        int contractPriceProfileSeq = 100;
        String gfsCustomerId = "1001";
        int gfsCustomerTypeCode = 31;
        String itemDesc = "chicken";
        String itemId = "1234";

        FutureItemDescriptionDTO futureItemDescriptionDTO = buildFutureItemDescriptionDTO();
        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = buildItemAssignmentWrapperDTO(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTypeCode,
                itemId);

        doReturn(futureItemDescriptionDTO).when(customerItemDescPriceRepository).fetchFutureItemForFurtherance(contractPriceProfileSeq, gfsCustomerId,
                gfsCustomerTypeCode, itemDesc);
        doReturn(itemAssignmentWrapperDTO).when(itemAssignmentBuilder).buildItemAssignmentWrapperDTO(futureItemDescriptionDTO,
                contractPriceProfileSeq);

        ItemAssignmentWrapperDTO result = target.fetchMappedItemsForFurtherance(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTypeCode,
                itemDesc);

        ItemAssignmentDTO itemAssignmentDTO = result.getItemAssignmentList().get(0);

        assertThat(result.getContractPriceProfileSeq(), equalTo(contractPriceProfileSeq));
        assertThat(result.getFutureItemDesc(), equalTo(itemDesc));
        assertThat(result.getGfsCustomerId(), equalTo(gfsCustomerId));
        assertThat(result.getIsFutureItemSaved(), equalTo(true));
        assertThat(itemAssignmentDTO.getIsItemSaved(), equalTo(true));
        assertThat(itemAssignmentDTO.getItemDescription(), equalTo("test"));
        assertThat(itemAssignmentDTO.getItemId(), equalTo(itemId));

        verify(customerItemDescPriceRepository).fetchFutureItemForFurtherance(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTypeCode, itemDesc);
        verify(itemAssignmentBuilder).buildItemAssignmentWrapperDTO(futureItemDescriptionDTO, contractPriceProfileSeq);

    }

    @Test
    public void shouldDeleteMappedItemForFurtherance() {

        List<String> expireItemList = new ArrayList<>();
        expireItemList.add(itemPriceId);
        when(cppDateUtils.oneDayPreviousCurrentDate()).thenReturn(expirationDate);

        FutureItemDescriptionDTO futureItemDescriptionDTO = buildFutureItemDescriptionDTO();

        doReturn(futureItemDescriptionDTO).when(customerItemDescPriceRepository).fetchFutureItemForFurtherance(contractPriceProfileSeq, gfsCustomerId,
                gfsCustomerTypeCode, itemDesc);

        target.deleteMappedItemForFurtherance(contractPriceProfileSeq, cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, itemPriceId, itemDesc,
                userName);

        verify(itemMarkupDeleteHandler).deleteMappledItemMarkupForFurtherance(contractPriceProfileSeq, cppFurtheranceSeq, gfsCustomerId,
                gfsCustomerTypeCode, itemPriceId, itemDesc, userName);
        verify(furtheranceStatusValidator).validateIfFurtheranceEditableStatus(contractPriceProfileSeq, cppFurtheranceSeq);

    }

    @Test
    public void shouldThrowExceptionWithNotValidStatusWhenDeleteMappedItemForFurtherance() {

        List<String> expireItemList = new ArrayList<>();
        expireItemList.add(itemPriceId);

        doThrow(new CPPRuntimeException(CPPExceptionType.NOT_VALID_STATUS)).when(furtheranceStatusValidator)
                .validateIfFurtheranceEditableStatus(contractPriceProfileSeq, cppFurtheranceSeq);

        try {
            target.deleteMappedItemForFurtherance(contractPriceProfileSeq, cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, itemPriceId,
                    itemDesc, userName);
        } catch (CPPRuntimeException ex) {
            assertThat(ex.getType(), equalTo(CPPExceptionType.NOT_VALID_STATUS));
        }

    }

    @Test
    public void shouldAssignItemsForFurtherance() {

        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = buildItemAssignmentWrapperDTO(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTypeCode,
                itemPriceId);
        List<String> itemIdList = new ArrayList<>();
        itemIdList.add(itemPriceId);

        StatusDTO statusDTO = target.assignItemsForFurtherance(itemAssignmentWrapperDTO, userName);

        assertThat(statusDTO.getErrorCode(), equalTo(HttpStatus.OK.value()));

        verify(furtheranceStatusValidator).validateIfFurtheranceEditableStatus(contractPriceProfileSeq, cppFurtheranceSeq);
        verify(itemAssignmentHelper).assignItemsWithFutureItem(itemAssignmentWrapperDTO, userName);
        verify(furtheranceChangeTracker).addTrackingForMarkupAdd(cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, itemIdList, userName);

    }

    @Test
    public void shouldThrowExceptionWithDuplicateItemWhenAssignItemsForFurtherance() {

        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = buildItemAssignmentWrapperDTO(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTypeCode,
                itemPriceId);
        List<String> itemIdList = new ArrayList<>();
        itemIdList.add(itemPriceId);

        doThrow(new CPPRuntimeException(CPPExceptionType.ITEM_ALREADY_EXIST)).when(itemAssignmentHelper)
                .assignItemsWithFutureItem(itemAssignmentWrapperDTO, userName);

        StatusDTO statusDTO = target.assignItemsForFurtherance(itemAssignmentWrapperDTO, userName);

        assertThat(statusDTO.getErrorCode(), equalTo(CPPExceptionType.ITEM_ALREADY_EXIST.getErrorCode()));

        verify(furtheranceStatusValidator).validateIfFurtheranceEditableStatus(contractPriceProfileSeq, cppFurtheranceSeq);
        verify(itemAssignmentHelper).assignItemsWithFutureItem(itemAssignmentWrapperDTO, userName);

    }

    @Test
    public void shouldThrowExceptionWithNotValidStatusWhenAssignItemsForFurtherance() {

        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = buildItemAssignmentWrapperDTO(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTypeCode,
                itemPriceId);
        List<String> itemIdList = new ArrayList<>();
        itemIdList.add(itemPriceId);

        doThrow(new CPPRuntimeException(CPPExceptionType.NOT_VALID_STATUS)).when(furtheranceStatusValidator)
                .validateIfFurtheranceEditableStatus(contractPriceProfileSeq, cppFurtheranceSeq);

        try {
            target.assignItemsForFurtherance(itemAssignmentWrapperDTO, userName);
        } catch (CPPRuntimeException ex) {
            assertThat(ex.getType(), equalTo(CPPExceptionType.NOT_VALID_STATUS));
        }
    }

    @Test
    public void shouldSaveMarkupForFurtheranceWhenNoFutureItemFound() {
        MarkupWrapperDTO markupWrapperDTO = buildMarkupWrapperDTO();
        String userName = "test user";

        target.saveMarkupForFurtherance(markupWrapperDTO, userName);
        verify(furtheranceStatusValidator).validateIfFurtheranceEditableStatus(markupWrapperDTO.getContractPriceProfileSeq(),
                markupWrapperDTO.getCppFurtheranceSeq());
        verify(furtheranceMarkupSaver).saveMarkupForFurtherance(markupWrapperDTO, userName);
    }

    @Test
    public void shouldSaveMarkupForFurtherance() {
        MarkupWrapperDTO markupWrapperDTO = buildMarkupWrapperDTO();
        String userName = "test user";
        MarkupWrapperDO markupWrapperDO = buildMarkupWrapperDO();
        List<FutureItemDO> futureItemList = new ArrayList<>();
        FutureItemDO futureItemDO = new FutureItemDO();
        futureItemDO.setContractPriceProfileSeq(1);
        futureItemList.add(futureItemDO);
        markupWrapperDO.setFutureItemList(futureItemList);

        target.saveMarkupForFurtherance(markupWrapperDTO, userName);

        verify(furtheranceStatusValidator).validateIfFurtheranceEditableStatus(markupWrapperDTO.getContractPriceProfileSeq(),
                markupWrapperDTO.getCppFurtheranceSeq());
        verify(furtheranceMarkupSaver).saveMarkupForFurtherance(markupWrapperDTO, userName);
    }

    private MarkupWrapperDTO buildMarkupWrapperDTO() {
        MarkupWrapperDTO markupWrapperDTO = new MarkupWrapperDTO();
        markupWrapperDTO.setContractPriceProfileSeq(1);
        markupWrapperDTO.setCppFurtheranceSeq(1);
        markupWrapperDTO.setGfsCustomerId("user");
        markupWrapperDTO.setGfsCustomerType(1);
        markupWrapperDTO.setItemLevelMarkupList(new ArrayList<ItemLevelMarkupDTO>());
        markupWrapperDTO.setMarkupName("test markup");
        markupWrapperDTO.setProductMarkupList(new ArrayList<ProductTypeMarkupDTO>());
        markupWrapperDTO.setSubgroupMarkupList(new ArrayList<SubgroupMarkupDTO>());
        return markupWrapperDTO;
    }

    private MarkupWrapperDO buildMarkupWrapperDO() {
        MarkupWrapperDO markupWrapperDO = new MarkupWrapperDO();
        markupWrapperDO.setContractPriceProfileSeq(1);
        markupWrapperDO.setFutureItemList(new ArrayList<FutureItemDO>());
        markupWrapperDO.setMarkupList(new ArrayList<ProductTypeMarkupDO>());
        markupWrapperDO.setUserName("test user");
        return markupWrapperDO;
    }

    private FutureItemDescriptionDTO buildFutureItemDescriptionDTO() {
        FutureItemDescriptionDTO futureItemDescriptionDTO = new FutureItemDescriptionDTO();
        futureItemDescriptionDTO.setCustomerItemDescSeq(1);
        futureItemDescriptionDTO.setFutureItemDesc("chcicken");
        futureItemDescriptionDTO.setGfsCustomerId("1001");
        futureItemDescriptionDTO.setGfsCustomerTypeCode(31);
        return futureItemDescriptionDTO;
    }

    private ItemAssignmentWrapperDTO buildItemAssignmentWrapperDTO(int contractPriceProfileSeq, String gfsCustomerId, int gfsCustomerTypeCode,
            String itemId) {
        ItemAssignmentWrapperDTO itemAssignmentWrapperDTO = new ItemAssignmentWrapperDTO();
        itemAssignmentWrapperDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        itemAssignmentWrapperDTO.setExceptionName("test");
        itemAssignmentWrapperDTO.setFutureItemDesc("chicken");
        itemAssignmentWrapperDTO.setGfsCustomerId(gfsCustomerId);
        itemAssignmentWrapperDTO.setGfsCustomerTypeCode(gfsCustomerTypeCode);
        itemAssignmentWrapperDTO.setIsFutureItemSaved(true);
        itemAssignmentWrapperDTO.setCppFurtheranceSeq(1);
        List<ItemAssignmentDTO> itemAssignmentDTOList = new ArrayList<>();
        ItemAssignmentDTO itemAssignmentDTO = new ItemAssignmentDTO();
        itemAssignmentDTO.setItemDescription("test");
        itemAssignmentDTO.setItemId(itemId);
        itemAssignmentDTO.setIsItemSaved(true);
        itemAssignmentDTOList.add(itemAssignmentDTO);
        itemAssignmentWrapperDTO.setItemAssignmentList(itemAssignmentDTOList);
        return itemAssignmentWrapperDTO;
    }

    private ContractPricingResponseDTO getContractPricingResponseDTO() {
        String parentCLMAgreementId = "parent-clm-agreement-id";
        ContractPricingResponseDTO contractDetails = new ContractPricingResponseDTO();
        contractDetails.setClmAgreementId(parentCLMAgreementId);
        contractDetails.setClmContractTypeSeq(1);
        contractDetails.setContractPriceProfileId(2);
        contractDetails.setContractPriceProfileSeq(1);
        contractDetails.setContractPriceProfStatusCode(1);
        contractDetails.setPricingEffectiveDate(new LocalDate(2018, 01, 01).toDate());
        contractDetails.setPricingExhibitSysId("SysId");
        contractDetails.setPricingExpirationDate(new LocalDate(2019, 01, 01).toDate());
        contractDetails.setClmParentAgreementId("ParentAgreementId");
        contractDetails.setClmContractEndDate(new LocalDate(2019, 01, 01).toDate());
        return contractDetails;
    }

    private FurtheranceInformationDTO buildFurtheranceInformationDTO() {
        int contractPriceProfileSeq = 1;
        int cppFurtheranceSeq = 11;
        String fieldText = "FieldText";
        String parentCLMAgreementId = "parent-clm-agreement-id";
        FurtheranceInformationDTO furtheranceInformationDTO = new FurtheranceInformationDTO();
        furtheranceInformationDTO.setChangeReasonTxt("Reason");
        furtheranceInformationDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        furtheranceInformationDTO.setCppFurtheranceSeq(cppFurtheranceSeq);
        furtheranceInformationDTO.setContractReferenceTxt(fieldText);
        furtheranceInformationDTO.setFurtheranceDocumentGUID("Test Furtherance Document GUID");
        furtheranceInformationDTO.setFurtheranceEffectiveDate(new LocalDate(2018, 01, 01).toDate());
        furtheranceInformationDTO.setFurtheranceStatusCode(1);
        furtheranceInformationDTO.setParentCLMAgreementId(parentCLMAgreementId);
        return furtheranceInformationDTO;
    }

    private FurtheranceBaseDTO buildFurtheranceBaseDTO() {
        FurtheranceBaseDTO furtheranceBaseDTO = new FurtheranceBaseDTO();
        furtheranceBaseDTO.setAgreementId("parent-clm-agreement-id");
        furtheranceBaseDTO.setCppFurtheranceSeq(cppFurtheranceSeq);
        furtheranceBaseDTO.setContractType("testContract");
        return furtheranceBaseDTO;
    }

}
