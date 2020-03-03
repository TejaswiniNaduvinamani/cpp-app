package com.gfs.cpp.common.dto.clm;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.clm.ClmContractChangeEventDTO;

@RunWith(MockitoJUnitRunner.class)
public class ClmContractChangeEventDTOTest {

    @InjectMocks
    private ClmContractChangeEventDTO target;

    @Test
    public void validateContractChangeEvent() throws Exception {

        target.setAction("Create");
        target.setAgreementId("12345");
        target.setContractType("NationalAgreement");

        ClmContractChangeEventDTO actual = SerializationUtils.clone(target);

        assertThat(actual, equalTo(target));
        assertThat(actual.hashCode(), equalTo(target.hashCode()));
        assertThat(actual.toString(), notNullValue());

        assertThat(target.getAction(), equalTo("Create"));
        assertThat(target.getAgreementId(), equalTo("12345"));
        assertThat(target.getContractType(), equalTo("NationalAgreement"));

    }

}
