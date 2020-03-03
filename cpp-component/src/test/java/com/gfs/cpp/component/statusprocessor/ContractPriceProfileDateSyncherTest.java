package com.gfs.cpp.component.statusprocessor;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Date;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.clm.ClmContractResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;
import com.gfs.cpp.data.contractpricing.PrcProfVerificationPrivlgRepository;

@RunWith(MockitoJUnitRunner.class)
public class ContractPriceProfileDateSyncherTest {

    @InjectMocks
    private ContractPriceProfileDateSyncher target;

    @Mock
    private ContractPriceProfileRepository contractPriceProfileRepository;
    @Mock
    private PrcProfVerificationPrivlgRepository prcProfVerificationPrivlgRepository;

    @Test
    public void shouldUpdateDatesWhenContractEndDateIsChanged() throws Exception {

        Date contractStartDate = new LocalDate(2018, 01, 01).toDate();
        Date contractEndDate = new LocalDate(2019, 12, 31).toDate();
        Date newContractEndDate = new LocalDate(2020, 12, 31).toDate();
        int contractPriceProfileSeq = -1001;
        Date pricingEffectiveDate = new LocalDate(2018, 05, 01).toDate();

        ClmContractResponseDTO latestAgreementDataFromClm = new ClmContractResponseDTO();
        latestAgreementDataFromClm.setContractEffectiveDate(contractStartDate);
        latestAgreementDataFromClm.setContractExpirationDate(newContractEndDate);

        ContractPricingResponseDTO contractDataInCpp = new ContractPricingResponseDTO();
        contractDataInCpp.setClmContractStartDate(contractStartDate);
        contractDataInCpp.setClmContractEndDate(contractEndDate);
        contractDataInCpp.setPricingEffectiveDate(pricingEffectiveDate);
        contractDataInCpp.setContractPriceProfileSeq(contractPriceProfileSeq);

        target.synchClmContractDatesWithCpp(latestAgreementDataFromClm, contractDataInCpp, ClmContractChangeProcessor.LAST_UDDATE_USER_ID);

        verify(contractPriceProfileRepository).updateContractPriceProfileDates(contractPriceProfileSeq, pricingEffectiveDate, contractStartDate,
                newContractEndDate, ClmContractChangeProcessor.LAST_UDDATE_USER_ID);
        verify(prcProfVerificationPrivlgRepository, never()).updatePriceProfileVerficationPrivlgEffectiveDate(anyInt(), any(Date.class), anyString());

    }

    @Test
    public void shouldUpdateDatesWhenStartDateIsChanged() throws Exception {

        Date contractStartDate = new LocalDate(2018, 01, 01).toDate();
        Date contractEndDate = new LocalDate(2019, 12, 31).toDate();
        Date newContractStartDate = new LocalDate(2018, 07, 31).toDate();
        int contractPriceProfileSeq = -1001;
        Date pricingEffectiveDate = new LocalDate(2018, 05, 01).toDate();

        ClmContractResponseDTO latestAgreementDataFromClm = new ClmContractResponseDTO();
        latestAgreementDataFromClm.setContractEffectiveDate(newContractStartDate);
        latestAgreementDataFromClm.setContractExpirationDate(contractEndDate);

        ContractPricingResponseDTO contractDataInCpp = new ContractPricingResponseDTO();
        contractDataInCpp.setClmContractStartDate(contractStartDate);
        contractDataInCpp.setClmContractEndDate(contractEndDate);
        contractDataInCpp.setPricingEffectiveDate(pricingEffectiveDate);
        contractDataInCpp.setContractPriceProfileSeq(contractPriceProfileSeq);

        target.synchClmContractDatesWithCpp(latestAgreementDataFromClm, contractDataInCpp, ClmContractChangeProcessor.LAST_UDDATE_USER_ID);

        verify(contractPriceProfileRepository).updateContractPriceProfileDates(contractPriceProfileSeq, newContractStartDate, newContractStartDate,
                contractEndDate, ClmContractChangeProcessor.LAST_UDDATE_USER_ID);
        verify(prcProfVerificationPrivlgRepository).updatePriceProfileVerficationPrivlgEffectiveDate(contractPriceProfileSeq, newContractStartDate,
                ClmContractChangeProcessor.LAST_UDDATE_USER_ID);

    }

    @Test
    public void shouldUpdateDatesWhenAmendmentEffectiveDateIsChangedForAmendmentContract() throws Exception {

        Date contractStartDate = new LocalDate(2018, 01, 01).toDate();
        Date contractEndDate = new LocalDate(2019, 12, 31).toDate();
        Date newContractStartDate = new LocalDate(2018, 07, 31).toDate();
        int contractPriceProfileSeq = -1001;
        Date pricingEffectiveDate = new LocalDate(2018, 05, 01).toDate();

        ClmContractResponseDTO latestAgreementDataFromClm = new ClmContractResponseDTO();
        latestAgreementDataFromClm.setContractEffectiveDate(contractStartDate);
        latestAgreementDataFromClm.setContractExpirationDate(contractEndDate);
        latestAgreementDataFromClm.setAmendmentEffectiveDate(newContractStartDate);
        latestAgreementDataFromClm.setAmendment(true);

        ContractPricingResponseDTO contractDataInCpp = new ContractPricingResponseDTO();
        contractDataInCpp.setClmContractStartDate(contractStartDate);
        contractDataInCpp.setClmContractEndDate(contractEndDate);
        contractDataInCpp.setPricingEffectiveDate(pricingEffectiveDate);
        contractDataInCpp.setContractPriceProfileSeq(contractPriceProfileSeq);

        target.synchClmContractDatesWithCpp(latestAgreementDataFromClm, contractDataInCpp, ClmContractChangeProcessor.LAST_UDDATE_USER_ID);

        verify(contractPriceProfileRepository).updateContractPriceProfileDates(contractPriceProfileSeq, newContractStartDate, newContractStartDate,
                contractEndDate, ClmContractChangeProcessor.LAST_UDDATE_USER_ID);
        verify(prcProfVerificationPrivlgRepository).updatePriceProfileVerficationPrivlgEffectiveDate(contractPriceProfileSeq, newContractStartDate,
                ClmContractChangeProcessor.LAST_UDDATE_USER_ID);
    }

    @Test
    public void shouldNotUpdateDatesWhenNoDateIsChanged() throws Exception {

        Date contractStartDate = new LocalDate(2018, 01, 01).toDate();
        Date contractEndDate = new LocalDate(2019, 12, 31).toDate();
        int contractPriceProfileSeq = -1001;
        Date pricingEffectiveDate = new LocalDate(2018, 05, 01).toDate();
        Date pricingExpirationDate = new LocalDate(2019, 12, 31).toDate();

        ClmContractResponseDTO latestAgreementDataFromClm = new ClmContractResponseDTO();
        latestAgreementDataFromClm.setContractEffectiveDate(contractStartDate);
        latestAgreementDataFromClm.setContractExpirationDate(contractEndDate);
        latestAgreementDataFromClm.setAmendmentEffectiveDate(contractStartDate);
        latestAgreementDataFromClm.setAmendment(true);

        ContractPricingResponseDTO contractDataInCpp = new ContractPricingResponseDTO();
        contractDataInCpp.setClmContractStartDate(contractStartDate);
        contractDataInCpp.setClmContractEndDate(contractEndDate);
        contractDataInCpp.setPricingEffectiveDate(pricingEffectiveDate);
        contractDataInCpp.setPricingExpirationDate(pricingExpirationDate);
        contractDataInCpp.setContractPriceProfileSeq(contractPriceProfileSeq);

        target.synchClmContractDatesWithCpp(latestAgreementDataFromClm, contractDataInCpp, ClmContractChangeProcessor.LAST_UDDATE_USER_ID);

        verify(contractPriceProfileRepository, never()).updateContractPriceProfileDates(eq(contractPriceProfileSeq), any(Date.class), any(Date.class),
                any(Date.class), eq(ClmContractChangeProcessor.LAST_UDDATE_USER_ID));
        verify(prcProfVerificationPrivlgRepository, never()).updatePriceProfileVerficationPrivlgEffectiveDate(anyInt(), any(Date.class), anyString());
    }

}
