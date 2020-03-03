package com.gfs.cpp.component.markup;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.common.dto.markup.MarkupRequestDTO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.markup.MarkupOnSellUpdater;
import com.gfs.cpp.data.contractpricing.ContractPriceProfCustomerRepository;
import com.gfs.cpp.data.markup.CustomerItemDescPriceRepository;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;
import com.gfs.cpp.data.markup.PrcProfPricingRuleOvrdRepository;

@RunWith(MockitoJUnitRunner.class)
public class MarkupOnSellUpdaterTest {

    private static final Date futureDate = new LocalDate(9999, 01, 01).toDate();
    private static final String GFS_CUSTOMER_ID = "1";
    private static final String userName = "vc71u";

    @InjectMocks
    private MarkupOnSellUpdater target;

    @Mock
    private PrcProfPricingRuleOvrdRepository prcProfPricingRuleOvrdRepository;

    @Mock
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Mock
    private CustomerItemDescPriceRepository customerItemDescPriceRepository;

    @Mock
    private CPPDateUtils cppDateUtils;

    @Mock
    private ContractPriceProfCustomerRepository contractPriceProfCustomerRepository;

    @Test
    public void shouldUpdateMarkupOnSell() {

        int contractPriceProfileSeq = 1;
        MarkupRequestDTO markupRequest = new MarkupRequestDTO();
        markupRequest.setContractPriceProfileSeq(contractPriceProfileSeq);
        markupRequest.setMarkupOnSell(true);
        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId(GFS_CUSTOMER_ID);
        cmgCustomerResponseDTO.setTypeCode(31);

        when(contractPriceProfCustomerRepository.fetchDefaultCmg(contractPriceProfileSeq)).thenReturn(cmgCustomerResponseDTO);
        when(customerItemPriceRepository.fetchSellPriceIndCount(contractPriceProfileSeq)).thenReturn(1);
        when(customerItemDescPriceRepository.fetchMarkupAmountTypeCodeCount(contractPriceProfileSeq)).thenReturn(1);
        when(prcProfPricingRuleOvrdRepository.fetchSellPriceCount(contractPriceProfileSeq)).thenReturn(1);
        when(cppDateUtils.getFutureDate()).thenReturn(futureDate);

        target.updateMarkupOnSell(contractPriceProfileSeq, userName);

        verify(customerItemPriceRepository).fetchSellPriceIndCount(contractPriceProfileSeq);
        verify(customerItemDescPriceRepository).fetchMarkupAmountTypeCodeCount(contractPriceProfileSeq);
        verify(prcProfPricingRuleOvrdRepository).fetchSellPriceCount(contractPriceProfileSeq);
        verify(cppDateUtils, atLeastOnce()).getFutureDate();
        verify(prcProfPricingRuleOvrdRepository).updateMarkupOnSellIndicator(markupRequest, GFS_CUSTOMER_ID, 31, userName, futureDate, futureDate);
    }

    @Test
    public void shouldSaveMarkupOnSell() {

        int contractPriceProfileSeq = 1;

        MarkupRequestDTO markupRequest = new MarkupRequestDTO();
        markupRequest.setContractPriceProfileSeq(contractPriceProfileSeq);
        CMGCustomerResponseDTO cmgCustomerResponseDTO = new CMGCustomerResponseDTO();
        cmgCustomerResponseDTO.setId(GFS_CUSTOMER_ID);
        cmgCustomerResponseDTO.setTypeCode(31);

        when(customerItemPriceRepository.fetchSellPriceIndCount(contractPriceProfileSeq)).thenReturn(0);
        when(customerItemDescPriceRepository.fetchMarkupAmountTypeCodeCount(contractPriceProfileSeq)).thenReturn(0);
        when(prcProfPricingRuleOvrdRepository.fetchSellPriceCount(contractPriceProfileSeq)).thenReturn(0);
        when(contractPriceProfCustomerRepository.fetchDefaultCmg(contractPriceProfileSeq)).thenReturn(cmgCustomerResponseDTO);
        when(cppDateUtils.getFutureDate()).thenReturn(futureDate);

        target.updateMarkupOnSell(contractPriceProfileSeq, userName);

        verify(customerItemPriceRepository).fetchSellPriceIndCount(contractPriceProfileSeq);
        verify(customerItemDescPriceRepository).fetchMarkupAmountTypeCodeCount(contractPriceProfileSeq);
        verify(prcProfPricingRuleOvrdRepository).fetchSellPriceCount(contractPriceProfileSeq);
        verify(cppDateUtils, atLeastOnce()).getFutureDate();
        verify(prcProfPricingRuleOvrdRepository).saveMarkupOnSellIndicator(markupRequest, GFS_CUSTOMER_ID, 31, userName, futureDate, futureDate);
    }
}
