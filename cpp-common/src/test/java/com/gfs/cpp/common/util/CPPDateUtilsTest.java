package com.gfs.cpp.common.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.exception.CPPRuntimeException;

@RunWith(MockitoJUnitRunner.class)
public class CPPDateUtilsTest {

    @InjectMocks
    @Spy
    private CPPDateUtils target;

    @Test
    public void shouldReturnPreviousDay() throws Exception {

        LocalDate sysdateLocal = new LocalDate(2018, 04, 13);
        doReturn(sysdateLocal).when(target).getCurrentDateAsLocalDate();

        Date actual = target.oneDayPreviousCurrentDate();

        assertThat(actual, equalTo(new LocalDate(2018, 04, 12).toDate()));

    }

    @Test
    public void shouldReturnPreviousDayForDate() throws Exception {

        LocalDate sysdateLocal = new LocalDate(2018, 04, 13);
        doReturn(sysdateLocal).when(target).getCurrentDateAsLocalDate();
        Date inputDate = new LocalDate(2018, 04, 02).toDate();

        Date actual = target.getPreviousDate(inputDate);

        assertThat(actual, equalTo(new LocalDate(2018, 04, 01).toDate()));

    }

    @Test
    public void shouldConvertStringToDate() throws Exception {

        String inputDate = "04/02/2018";

        Date actual = target.parseStringToDate(inputDate);

        assertThat(actual, equalTo(new LocalDate(2018, 04, 02).toDate()));

    }

    @Test(expected = CPPRuntimeException.class)
    public void shouldConvertStringToDateExceptionTest() {

        String inputDate = "abcd";
        target.parseStringToDate(inputDate);
    }

    @Test
    public void shouldConvertStringToDateWhenNull() throws Exception {

        String inputDate = "";
        Date actual = target.parseStringToDate(inputDate);
        assertThat(actual, equalTo(null));
    }

    @Test
    public void shouldReturnLocalDate() throws ParseException {

        LocalDate actual = target.getCurrentDateAsLocalDate();

        assertThat(actual.toDate(), equalTo((new LocalDate().toDate())));

    }

    @Test
    public void shouldReturnFutureDate() throws Exception {

        Date actual = target.getFutureDate();

        assertThat(actual, equalTo(new LocalDate(9999, 01, 01).toDate()));

    }

    @Test
    public void shouldReturnCurrentDate() throws Exception {

        LocalDate sysdateLocal = new LocalDate();

        Date actual = target.getCurrentDate();

        assertThat(actual, equalTo(sysdateLocal.toDate()));

    }

    @Test
    public void shouldFormatDateToString() throws Exception {

        Date date = new Date();

        String actual = target.formatDateToString(date);

        assertThat(actual, equalTo(new SimpleDateFormat(CPPConstants.DATE_FORMAT).format(date)));

    }

    @Test
    public void shouldFormatDateToStringCondition2() throws Exception {

        Date date = null;

        String actual = target.formatDateToString(date);

        assertThat(actual, equalTo(null));

    }

    @Test
    public void shouldReturnDaysPreviousCurrentDate() {

        LocalDate sysdateLocal = new LocalDate(2018, 04, 13);
        doReturn(sysdateLocal).when(target).getCurrentDateAsLocalDate();

        Date actual = target.daysPreviousCurrentDate(1);

        assertThat(actual, equalTo(new LocalDate(2018, 04, 12).toDate()));

    }

    @Test
    public void shouldReturnDaysBeyondCurrentDate() {

        LocalDate sysdateLocal = new LocalDate(2018, 04, 13);
        doReturn(sysdateLocal).when(target).getCurrentDateAsLocalDate();

        Date actual = target.daysBeyondCurrentDate(1);

        assertThat(actual, equalTo(new LocalDate(2018, 04, 14).toDate()));

    }

}
