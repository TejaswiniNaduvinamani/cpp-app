package com.gfs.cpp.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;

@Component
public class CPPDateUtils {

    static final Logger logger = LoggerFactory.getLogger(CPPDateUtils.class);

    public Date oneDayPreviousCurrentDate() {
        return getCurrentDateAsLocalDate().minusDays(1).toDate();
    }

    public Date daysPreviousCurrentDate(int subDays) {
        return getCurrentDateAsLocalDate().minusDays(subDays).toDate();
    }

    public Date daysBeyondCurrentDate(int addDays) {
        return getCurrentDateAsLocalDate().plusDays(addDays).toDate();
    }

    public LocalDate getCurrentDateAsLocalDate() {
        return new LocalDate();
    }

    public Date getCurrentDate() {
        return new LocalDate().toDate();
    }

    public Date getPreviousDate(Date inputDate) {
        return DateUtils.addDays(inputDate, -1);
    }

    public Date parseStringToDate(String date) {
        if (StringUtils.isNotEmpty(date)) {
            try {
                return new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(date);
            } catch (ParseException e) {
                throw new CPPRuntimeException(CPPExceptionType.SERVICE_FAILED, "Date Parsing failed for Parsing String to Date.");
            }
        }
        return null;
    }

    public Date getFutureDate() {
        try {
            return new SimpleDateFormat(CPPConstants.DATE_FORMAT).parse(CPPConstants.FUTURE_DATE);
        } catch (ParseException e) {
            throw new CPPRuntimeException(CPPExceptionType.SERVICE_FAILED, "Date Parsing failed for Parsing String to future Date.");
        }
    }

    public String formatDateToString(Date date) {
        if (date != null) {
            return new SimpleDateFormat(CPPConstants.DATE_FORMAT).format(date);
        }
        return null;
    }
}
