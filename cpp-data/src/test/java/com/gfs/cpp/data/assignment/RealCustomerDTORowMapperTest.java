package com.gfs.cpp.data.assignment;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.assignments.RealCustomerDTO;
import com.gfs.cpp.data.assignment.RealCustomerDTORowMapper;

@RunWith(MockitoJUnitRunner.class)
public class RealCustomerDTORowMapperTest {

    @InjectMocks
    private RealCustomerDTORowMapper sut;

    @Mock
    private ResultSet resultSet;

    @Test
    public void shouldMapRealCustomerDTO() throws SQLException {
        final String gfsCustomerId = "9999999";
        final int gfsCustomerTypeCode = 1;

        when(resultSet.getString("GFS_CUSTOMER_ID")).thenReturn(new String(gfsCustomerId));
        when(resultSet.getInt("GFS_CUSTOMER_TYPE_CODE")).thenReturn(gfsCustomerTypeCode);

        final RealCustomerDTO actual = sut.mapRow(resultSet, 0);

        assertEquals(actual.getRealCustomerId(), new String(gfsCustomerId));
        assertEquals(actual.getRealCustomerType(), gfsCustomerTypeCode);
    }
  
}
