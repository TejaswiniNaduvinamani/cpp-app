package com.gfs.cpp.common.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

import org.junit.Test;

import com.gfs.cpp.common.util.MarkupTypeEnum;

public class MarkupTypeEnumTest {

    @Test
    public void shouldReturnNameByCode() throws Exception {

        assertThat(MarkupTypeEnum.getNameByCode(2), equalTo("Per Case"));

    }

    @Test
    public void shouldReturnNullWhenCodeIsNotValidOne() throws Exception {
        assertThat(MarkupTypeEnum.getNameByCode(4), nullValue());
    }
}
