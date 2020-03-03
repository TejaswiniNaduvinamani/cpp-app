package com.gfs.cpp.common.dto.clm;

import java.util.Date;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.gfs.cpp.common.dto.clm.JsonDateSerializer;

@RunWith(MockitoJUnitRunner.class)
public class JsonDateSerializerTest {

    @InjectMocks
    private JsonDateSerializer target;

    @Mock
    private JsonGenerator gen;

    @Mock
    private SerializerProvider serializers;

    @Mock
    private Date date;

    @Test
    public void shouldSerializeDate() throws Exception {

        Date date = new LocalDate(2018, 01, 01).toDate();
        target.serialize(date, gen, serializers);

    }

}
