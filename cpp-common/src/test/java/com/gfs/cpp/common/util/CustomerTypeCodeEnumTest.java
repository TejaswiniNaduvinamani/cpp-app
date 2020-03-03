package com.gfs.cpp.common.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.gfs.cpp.common.util.CustomerTypeCodeEnum;

public class CustomerTypeCodeEnumTest {

    @Test
    public void shouldReturnNameByCode() throws Exception {
        assertThat(CustomerTypeCodeEnum.getNameByCode(31), equalTo("CMG"));
    }

    @Test
    public void shouldReturnNullWhenCodeIsNotValidOne() throws Exception {
        assertThat(CustomerTypeCodeEnum.getNameByCode(4), nullValue());
    }

    @Test
    public void shouldReturnTypeNameByCode() throws Exception {
        assertThat(CustomerTypeCodeEnum.getTypeNameByCode(31), equalTo("Contract Management Group"));
    }

    @Test
    public void shouldReturnNullWhenCodeIsNotValid() throws Exception {
        assertThat(CustomerTypeCodeEnum.getTypeNameByCode(4), nullValue());
    }
}
