package com.gfs.cpp.component.customerinfo;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.customerinfo.CPPInformationDTO;
import com.gfs.cpp.component.customerinfo.CppVersionCreator;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;

@RunWith(MockitoJUnitRunner.class)
public class CppVersionCreatorTest {

    @InjectMocks
    private CppVersionCreator target;

    @Mock
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Test
    public void shouldCreateInitialVersion() {
        int cppSequence = 21;
        int cppIdSequence = 344;
        when(contractPriceProfileRepository.fetchContractPriceProfileIdSeq()).thenReturn(cppIdSequence);
        when(contractPriceProfileRepository.fetchCPPNextSequence()).thenReturn(cppSequence);

        CPPInformationDTO actual = target.createInitialVersion();
        assertThat(actual.getContractPriceProfileId(), equalTo(cppIdSequence));
        assertThat(actual.getContractPriceProfileSeq(), equalTo(cppSequence));
        assertThat(actual.getVersionNumber(), equalTo(1));
        verify(contractPriceProfileRepository).fetchContractPriceProfileIdSeq();
        verify(contractPriceProfileRepository).fetchCPPNextSequence();
    }

    @Test
    public void shouldCreateNextCppVersion() {
        int latestVersionNumber = 2;
        int cppSequence = 21;
        int contractPriceProfileId = 231;
        when(contractPriceProfileRepository.fetchCPPNextSequence()).thenReturn(cppSequence);

        CPPInformationDTO actual = target.createNextCppVersion(latestVersionNumber, contractPriceProfileId);
        assertThat(actual.getContractPriceProfileSeq(), equalTo(cppSequence));
        assertThat(actual.getContractPriceProfileId(), equalTo(contractPriceProfileId));
        assertThat(actual.getVersionNumber(), equalTo(3));
        verify(contractPriceProfileRepository).fetchCPPNextSequence();
    }

}
