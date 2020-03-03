package com.gfs.cpp.common.dto.furtherance;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.furtherance.FurtheranceBaseDTO;

@RunWith(MockitoJUnitRunner.class)
public class FurtheranceBaseDTOTest {

    @InjectMocks
    private FurtheranceBaseDTO dto;

    @Test
    public void testMethods() {
        dto.setAgreementId("agreement-id");
        dto.setContractType("test");
        dto.setCppFurtheranceSeq(10);

        final FurtheranceBaseDTO actual = SerializationUtils.clone(dto);
        assertThat(dto.equals(actual), equalTo(true));
        assertThat(dto.hashCode(), equalTo(actual.hashCode()));
        assertThat(dto.toString() != null, equalTo(true));

        assertThat(actual.getAgreementId(), equalTo(dto.getAgreementId()));
        assertThat(actual.getContractType(), equalTo(dto.getContractType()));
        assertThat(actual.getCppFurtheranceSeq(), equalTo(dto.getCppFurtheranceSeq()));
    }

}
