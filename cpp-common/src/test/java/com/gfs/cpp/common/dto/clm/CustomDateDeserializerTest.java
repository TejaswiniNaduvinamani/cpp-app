package com.gfs.cpp.common.dto.clm;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;

import java.util.Date;

import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.gfs.cpp.common.dto.clm.CustomDateDeserializer;

@RunWith(MockitoJUnitRunner.class)
public class CustomDateDeserializerTest {

    @InjectMocks
    private CustomDateDeserializer target;

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    @Test
    public void shouldFormatToDate() throws Exception {

        Date expectedDate = new LocalDateTime(2018, 3, 15, 14, 37, 28).toDate();

        doReturn("2018-03-15T14:37:28").when(jsonParser).getText();
        Date actualDate = target.deserialize(jsonParser, deserializationContext);

        assertThat(actualDate, equalTo(expectedDate));
    }

    @Test
    public void shouldReturnNullWhenThereIsParseException() throws Exception {

        doReturn("notvalidtime").when(jsonParser).getText();
        Date actualDate = target.deserialize(jsonParser, deserializationContext);

        assertThat(actualDate, nullValue());

    }

}
