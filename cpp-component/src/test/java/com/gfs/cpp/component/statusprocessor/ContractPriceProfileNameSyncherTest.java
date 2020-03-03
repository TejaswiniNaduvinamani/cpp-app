package com.gfs.cpp.component.statusprocessor;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;

@RunWith(MockitoJUnitRunner.class)
public class ContractPriceProfileNameSyncherTest {

    @InjectMocks
    private ContractPriceProfileNameSyncher target;
    @Mock
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Test
    public void shouldUpdateNameIfNewContractNameDoesntMatch() throws Exception {

        String contractName = "clm contract name";
        String cppContractName = "cppContractName";
        int contractPriceProfileSeq = -1001;

        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setClmContractName(cppContractName);

        doReturn(contractPricingResponseDTO).when(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);

        target.synchCLMContractNameWithCPPContractName(contractName, contractPriceProfileSeq, ClmContractChangeProcessor.LAST_UDDATE_USER_ID);

        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);
        verify(contractPriceProfileRepository).updateContractPriceProfileContractName(eq(contractPriceProfileSeq), eq(contractName), anyString());

    }

    @Test
    public void shouldNotUpdateNameIfClmNameMatchesCustomerName() throws Exception {

        String contractName = "clm contract name";
        int contractPriceProfileSeq = -1001;

        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setClmContractName(contractName);

        doReturn(contractPricingResponseDTO).when(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);

        target.synchCLMContractNameWithCPPContractName(contractName, contractPriceProfileSeq, ClmContractChangeProcessor.LAST_UDDATE_USER_ID);

        verify(contractPriceProfileRepository, never()).updateContractPriceProfileContractName(anyInt(), anyString(), anyString());

    }
    
    @Test
    public void shouldNotUpdateNameIfClmNameMatchesCustomerNameTillMaxAllowedCharacters() throws Exception {

        String contractName = "Testing_Agreement_name_to_be_greater_then_TWO_HUNDRED_ANDFIFTY_FIVE_characters_in_CPP_for_DEFECT_CPP-1290_Testing_Agreement_name_to_be_greater_then_TWO_HUNDRED_ANDFIFTY_FIVE_characters_in_CPP_for_DEFECT_CPP-1290_Testing_Agreement_name_to_be_grea";
       String clmContractName = "Testing_Agreement_name_to_be_greater_then_TWO_HUNDRED_ANDFIFTY_FIVE_characters_in_CPP_for_DEFECT_CPP-1290_Testing_Agreement_name_to_be_greater_then_TWO_HUNDRED_ANDFIFTY_FIVE_characters_in_CPP_for_DEFECT_CPP-1290_Testing_Agreement_name_to_be_greater_then_TWO_HUNDRED_ANDFIFTY_FIVE_characters_in_CPP_for_DEFECT_CPP-1290_Testing_Agreement_name_to_be_greater_then_TWO_HUNDRED_ANDFIFTY_FIVE_characters_in_CPP_for_DEFECT_CPP-1290_Testing_Agreement_name_to_be_greater_then_TWO_HUNDRED_ANDFIFTY_FIVE_characters_in_CPP_for_DEFECT_CPP-1290";
        int contractPriceProfileSeq = -1001;

        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setClmContractName(contractName);

        doReturn(contractPricingResponseDTO).when(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);

        target.synchCLMContractNameWithCPPContractName(clmContractName, contractPriceProfileSeq, ClmContractChangeProcessor.LAST_UDDATE_USER_ID);

        verify(contractPriceProfileRepository, never()).updateContractPriceProfileContractName(anyInt(), anyString(), anyString());

    }


}
