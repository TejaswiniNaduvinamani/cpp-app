package com.gfs.cpp.common.dto.clm;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JsonDateDeserializer extends JsonDeserializer<Date> {

    private static final Logger logger = LoggerFactory.getLogger(JsonDateDeserializer.class);

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(jp.getText());
        } catch (ParseException e) {
            logger.error("Failed to parse Date ", e);
        }
        return null;
    }
}
