package com.gfs.cpp.component.clm;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;

import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.component.clm.ClmContractTypeChecker;
import com.gfs.cpp.data.contractpricing.ClmContractTypeRepository;

@RunWith(MockitoJUnitRunner.class)
public class ClmContractTypeCheckerTest {

    @InjectMocks
    private ClmContractTypeChecker target;

    @Mock
    private ClmContractTypeRepository contractTypeRepository;

    @Test
    public void shouldReturnTrueForPricingContractType() throws Exception {
        doReturn(Collections.singletonList("ICMContractType")).when(contractTypeRepository).getAllContractTypes();
        assertThat(target.isPricingContractType("ICMContractType"), equalTo(true));
    }

    @Test
    public void shouldReturnFalseWhenNotContainsInContractType() throws Exception {
        doReturn(Collections.singletonList("ICMContractType")).when(contractTypeRepository).getAllContractTypes();
        assertThat(target.isPricingContractType("ICMContractTypeNO"), equalTo(false));
    }

}
