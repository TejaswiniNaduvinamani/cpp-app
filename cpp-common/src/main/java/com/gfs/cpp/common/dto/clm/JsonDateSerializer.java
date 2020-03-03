package com.gfs.cpp.common.dto.clm;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class JsonDateSerializer extends JsonSerializer<Date> {

    @Override
    public void serialize(Date date, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        gen.writeString(formattedDate);

    }

}
