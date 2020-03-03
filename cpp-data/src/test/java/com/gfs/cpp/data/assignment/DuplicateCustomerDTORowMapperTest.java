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

import com.gfs.cpp.common.dto.assignments.DuplicateCustomerDTO;
import com.gfs.cpp.data.assignment.DuplicateCustomerDTORowMapper;

@RunWith(MockitoJUnitRunner.class)
public class DuplicateCustomerDTORowMapperTest {

    @InjectMocks
    private DuplicateCustomerDTORowMapper sut;

    @Mock
    private ResultSet resultSet;

    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";

    @Test
    public void shouldMapRealCustomerValidationDTO() throws SQLException {
        final String gfsCustomerId = "9999999";
        final int gfsCustomerTypeCode = 1;
        final int contractPriceProfileSeq = 1;

        when(resultSet.getString(GFS_CUSTOMER_ID)).thenReturn(new String(gfsCustomerId));
        when(resultSet.getInt(GFS_CUSTOMER_TYPE_CODE)).thenReturn(gfsCustomerTypeCode);
        when(resultSet.getInt(CONTRACT_PRICE_PROFILE_SEQ)).thenReturn(contractPriceProfileSeq);

        final DuplicateCustomerDTO actual = sut.mapRow(resultSet, 0);

        assertEquals(actual.getCustomerId(), new String(gfsCustomerId));
        assertEquals(actual.getCustomerType(), gfsCustomerTypeCode);
        assertEquals(actual.getContractPriceProfileSeq(), contractPriceProfileSeq);

    }

}
