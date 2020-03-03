package com.gfs.cpp.component.customerinfo;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.clm.ClmContractResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.customerinfo.CPPInformationDTO;
import com.gfs.cpp.common.model.contractpricing.ContractPricingDO;
import com.gfs.cpp.common.model.distributioncenter.DistributionCenterDO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.customerinfo.ContractCustomerCopier;
import com.gfs.cpp.component.customerinfo.ContractDataCopier;
import com.gfs.cpp.component.customerinfo.ContractDataCopyHelper;
import com.gfs.cpp.component.customerinfo.ContractItemCopier;
import com.gfs.cpp.component.customerinfo.ContractPrcProfCopier;
import com.gfs.cpp.component.userdetails.CppUserDetailsService;
import com.gfs.cpp.data.contractpricing.ClmContractTypeRepository;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.data.distributioncenter.ContractPriceProfShipDcRepository;

@RunWith(MockitoJUnitRunner.class)
public class ContractDataCopierTest {

    @InjectMocks
    private ContractDataCopier target;

    @Mock
    private CppUserDetailsService gfsUserDetailsService;

    @Mock
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Mock
    private ContractDataCopyHelper contractDataCopyHelper;

    @Mock
    private ContractPriceProfShipDcRepository contractPriceProfShipDcRepository;

    @Mock
    private ContractCustomerCopier contractCustomerCopier;

    @Mock
    private ContractItemCopier contractItemCopier;

    @Mock
    private CPPDateUtils cppDateUtils;

    @Mock
    private ContractPrcProfCopier contractPrcProfCopier;

    @Mock
    private ClmContractTypeRepository clmContractTypeRepository;

    String userId = "Test User";
    Date farOutDate = new LocalDate(999, 1, 1).toDate();

    @Before
    public void setup() {
        when(cppDateUtils.getFutureDate()).thenReturn(farOutDate);
        when(gfsUserDetailsService.getCurrentUserId()).thenReturn(userId);
    }

    @Test
    public void shouldCopyContractPriceDateAndDistributionRecord() {

        int existingContractPriceProfileSeq = 1289;
        Date effectiveDate = new LocalDate(2019, 1, 1).toDate();
        Date expirationDate = new LocalDate(2019, 12, 31).toDate();
        Date amendmentEffectiveDate = new LocalDate(2019, 6, 30).toDate();
        int contractPriceProfileSequence = 1290;
        int contractTypeSeq = 2;

        ContractPricingResponseDTO contractDetailsOfLatestVersion = new ContractPricingResponseDTO();
        contractDetailsOfLatestVersion.setContractPriceProfileSeq(existingContractPriceProfileSeq);

        ClmContractResponseDTO agreementData = new ClmContractResponseDTO();
        agreementData.setContractEffectiveDate(effectiveDate);
        agreementData.setContractExpirationDate(expirationDate);
        agreementData.setAmendmentEffectiveDate(amendmentEffectiveDate);

        CPPInformationDTO cppInformationDTO = new CPPInformationDTO();
        cppInformationDTO.setContractPriceProfileSeq(contractPriceProfileSequence);

        ContractPricingDO contractPricingDO = new ContractPricingDO();

        List<String> shipDCNumberList = Collections.singletonList("DC1");

        DistributionCenterDO dcDO = new DistributionCenterDO();
        dcDO.setContractPriceProfileSeq(contractPriceProfileSequence);
        dcDO.setCreateUserID(userId);
        dcDO.setDcCodes(shipDCNumberList);
        dcDO.setEffectiveDate(amendmentEffectiveDate);
        dcDO.setExpirationDate(expirationDate);

        when(contractDataCopyHelper.buildContractPricingDO(agreementData, contractDetailsOfLatestVersion, cppInformationDTO))
                .thenReturn(contractPricingDO);
        when(contractPriceProfShipDcRepository.fetchDistributionCentersbyContractPriceProfileSeq(existingContractPriceProfileSeq))
                .thenReturn(shipDCNumberList);
        when(clmContractTypeRepository.fetchContractTypeSequenceByTypeName(eq(agreementData.getContractTypeName()))).thenReturn(contractTypeSeq);

        target.copyContractDataToNewVersion(contractDetailsOfLatestVersion, agreementData, cppInformationDTO);

        verify(contractDataCopyHelper).buildContractPricingDO(agreementData, contractDetailsOfLatestVersion, cppInformationDTO);
        verify(contractPriceProfShipDcRepository).saveDistributionCenter(eq(dcDO), eq(userId));
        verify(clmContractTypeRepository).fetchContractTypeSequenceByTypeName(eq(agreementData.getContractTypeName()));

        verify(contractPriceProfileRepository).saveContractPricingDetails(eq(contractPricingDO), eq(userId), eq(contractTypeSeq),
                eq(contractPriceProfileSequence));

    }

    @Test
    public void shouldCopyAllItemMapping() throws Exception {

        int existingContractPriceProfileSeq = 1289;
        Date effectiveDate = new LocalDate(2019, 1, 1).toDate();
        Date expirationDate = new LocalDate(2019, 12, 31).toDate();
        Date amendmentEffectiveDate = new LocalDate(2019, 6, 30).toDate();
        int contractPriceProfileSequence = 1290;

        ClmContractResponseDTO agreementData = new ClmContractResponseDTO();
        agreementData.setContractEffectiveDate(effectiveDate);
        agreementData.setContractExpirationDate(expirationDate);
        agreementData.setAmendmentEffectiveDate(amendmentEffectiveDate);

        CPPInformationDTO cppInformationDTO = new CPPInformationDTO();
        cppInformationDTO.setContractPriceProfileSeq(contractPriceProfileSequence);

        ContractPricingResponseDTO contractDetailsOfLatestVersion = new ContractPricingResponseDTO();
        contractDetailsOfLatestVersion.setContractPriceProfileSeq(existingContractPriceProfileSeq);

        target.copyContractDataToNewVersion(contractDetailsOfLatestVersion, agreementData, cppInformationDTO);

        verify(contractItemCopier).copyAllItemAndMappings(eq(contractPriceProfileSequence), eq(existingContractPriceProfileSeq), eq(userId),
                eq(expirationDate), eq(farOutDate));

    }

    @Test
    public void shouldCopyCustomerAndCustomerMapping() throws Exception {

        int existingContractPriceProfileSeq = 1289;
        Date effectiveDate = new LocalDate(2019, 1, 1).toDate();
        Date expirationDate = new LocalDate(2019, 12, 31).toDate();
        Date amendmentEffectiveDate = new LocalDate(2019, 6, 30).toDate();
        int contractPriceProfileSequence = 1290;

        ClmContractResponseDTO agreementData = new ClmContractResponseDTO();
        agreementData.setContractEffectiveDate(effectiveDate);
        agreementData.setContractExpirationDate(expirationDate);
        agreementData.setAmendmentEffectiveDate(amendmentEffectiveDate);

        CPPInformationDTO cppInformationDTO = new CPPInformationDTO();
        cppInformationDTO.setContractPriceProfileSeq(contractPriceProfileSequence);

        ContractPricingResponseDTO contractDetailsOfLatestVersion = new ContractPricingResponseDTO();
        contractDetailsOfLatestVersion.setContractPriceProfileSeq(existingContractPriceProfileSeq);

        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        List<CMGCustomerResponseDTO> cmgCustomerResponseDTOListForLatestContractVer = Collections.singletonList(cmgCustomerResponseDTO);

        when(contractCustomerCopier.fetchGFSCustomerDetailsListForLatestContractVersion(existingContractPriceProfileSeq))
                .thenReturn(cmgCustomerResponseDTOListForLatestContractVer);
        when(contractCustomerCopier.retrieveDefaultCMGForLatestContractVer(cmgCustomerResponseDTOListForLatestContractVer))
                .thenReturn(cmgCustomerResponseDTO);

        target.copyContractDataToNewVersion(contractDetailsOfLatestVersion, agreementData, cppInformationDTO);

        verify(contractCustomerCopier).copyCustomerAndCustomerMapping(eq(contractPriceProfileSequence), eq(userId),
                eq(cmgCustomerResponseDTOListForLatestContractVer));
        verify(contractCustomerCopier).fetchGFSCustomerDetailsListForLatestContractVersion(eq(existingContractPriceProfileSeq));
        verify(contractCustomerCopier).retrieveDefaultCMGForLatestContractVer(eq(cmgCustomerResponseDTOListForLatestContractVer));

    }

    @Test
    public void shouldCopyAllPricingProf() throws Exception {

        int existingContractPriceProfileSeq = 1289;
        Date effectiveDate = new LocalDate(2019, 1, 1).toDate();
        Date expirationDate = new LocalDate(2019, 12, 31).toDate();
        Date amendmentEffectiveDate = new LocalDate(2019, 6, 30).toDate();
        int contractPriceProfileSequence = 1290;

        ClmContractResponseDTO agreementData = new ClmContractResponseDTO();
        agreementData.setContractEffectiveDate(effectiveDate);
        agreementData.setContractExpirationDate(expirationDate);
        agreementData.setAmendmentEffectiveDate(amendmentEffectiveDate);

        CPPInformationDTO cppInformationDTO = new CPPInformationDTO();
        cppInformationDTO.setContractPriceProfileSeq(contractPriceProfileSequence);

        ContractPricingResponseDTO contractDetailsOfLatestVersion = new ContractPricingResponseDTO();
        contractDetailsOfLatestVersion.setContractPriceProfileSeq(existingContractPriceProfileSeq);

        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        List<CMGCustomerResponseDTO> cmgCustomerResponseDTOListForLatestContractVer = Collections.singletonList(cmgCustomerResponseDTO);

        when(contractCustomerCopier.fetchGFSCustomerDetailsListForLatestContractVersion(existingContractPriceProfileSeq))
                .thenReturn(cmgCustomerResponseDTOListForLatestContractVer);
        when(contractCustomerCopier.retrieveDefaultCMGForLatestContractVer(cmgCustomerResponseDTOListForLatestContractVer))
                .thenReturn(cmgCustomerResponseDTO);

        target.copyContractDataToNewVersion(contractDetailsOfLatestVersion, agreementData, cppInformationDTO);

        verify(contractPrcProfCopier).copyPrcProfEntries(contractPriceProfileSequence, existingContractPriceProfileSeq, userId,
                amendmentEffectiveDate, expirationDate, cmgCustomerResponseDTO, farOutDate);

    }

    @Test
    public void shouldCopyCPPShipDC() {

        int contractPriceProfileSequence = 3;
        int cppSeqForLatestContractVersion = 2;
        String userId = "Test";
        Date effectiveDate = new Date();
        Date expirationDate = new Date();

        ArrayList<String> shipDCNumberList = new ArrayList<>();
        shipDCNumberList.add("DC1");

        DistributionCenterDO dcDO = new DistributionCenterDO();
        dcDO.setContractPriceProfileSeq(contractPriceProfileSequence);
        dcDO.setCreateUserID(userId);
        dcDO.setDcCodes(shipDCNumberList);
        dcDO.setEffectiveDate(effectiveDate);
        dcDO.setExpirationDate(expirationDate);

        when(contractPriceProfShipDcRepository.fetchDistributionCentersbyContractPriceProfileSeq(cppSeqForLatestContractVersion))
                .thenReturn(shipDCNumberList);

        target.copyCPPShipDC(contractPriceProfileSequence, cppSeqForLatestContractVersion, userId, effectiveDate, expirationDate);
        verify(contractPriceProfShipDcRepository).saveDistributionCenter(eq(dcDO), eq(userId));

    }
}
