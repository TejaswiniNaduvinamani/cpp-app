package com.gfs.cpp.common.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.gfs.cpp.common.util.ContractTypeForSearchEnum;

public class ContractTypeForSearchEnumTest {

    @Test
    public void shouldReturnDescByCode() throws Exception {
        assertThat(ContractTypeForSearchEnum.getDescByCode(3), equalTo("Street-Original"));
    }

    @Test
    public void shouldReturnNullDescWhenCodeIsNotValid() throws Exception {
        assertThat(ContractTypeForSearchEnum.getDescByCode(13), nullValue());
    }

    @Test
    public void shouldReturnTypeByCode() throws Exception {
        assertThat(ContractTypeForSearchEnum.getTypeByCode(4), equalTo("ICMDistributionAgreementNational"));
    }

    @Test
    public void shouldReturnNullTypeWhenCodeIsNotValid() throws Exception {
        assertThat(ContractTypeForSearchEnum.getTypeByCode(14), nullValue());
    }

    @Test
    public void shouldReturnTypeByDesc() throws Exception {
        assertThat(ContractTypeForSearchEnum.getTypeByDesc("Regional-Amendment"), equalTo("ICMDistributionAgreementRegionalAmendment"));
    }

    @Test
    public void shouldReturnNullTypeWhenDescIsNotValid() throws Exception {
        assertThat(ContractTypeForSearchEnum.getTypeByDesc("dummyContract"), nullValue());
    }
}
