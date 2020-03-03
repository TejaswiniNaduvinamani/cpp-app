package com.gfs.cpp.data.contractpricing;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.contractpricing.CMGCustomerResponseDTO;
import com.gfs.cpp.data.contractpricing.CMGCustomerResponseRowMapper;

@RunWith(MockitoJUnitRunner.class)
public class CMGCustomerResponseRowMapperTest {

    @InjectMocks
    private CMGCustomerResponseRowMapper target;

    @Mock
    private ResultSet rs;

    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
    private static final String DEFAULT_CUSTOMER_IND = "DEFAULT_CUSTOMER_IND";
    private static final String CPP_CUSTOMER_SEQ = "CPP_CUSTOMER_SEQ";

    @Test
    public void shouldMapRowIntoCMGCustomerResponseDTO() throws SQLException {
        final String gfsCustomerId = "100";
        final int gfsCustomerTypeCode = 31;
        final int defaultIndicatorId = 1;
        final int cppCustomerSeq =2;

        when(rs.getString(GFS_CUSTOMER_ID)).thenReturn(gfsCustomerId);
        when(rs.getInt(GFS_CUSTOMER_TYPE_CODE)).thenReturn(gfsCustomerTypeCode);
        when(rs.getInt(DEFAULT_CUSTOMER_IND)).thenReturn(defaultIndicatorId);
        when(rs.getInt(CPP_CUSTOMER_SEQ)).thenReturn(cppCustomerSeq);
        
        final CMGCustomerResponseDTO actual = target.mapRow(rs, 0);

        assertEquals(actual.getId(), gfsCustomerId);
        assertEquals(actual.getTypeCode(), gfsCustomerTypeCode);
        assertEquals(actual.getDefaultCustomerInd(), defaultIndicatorId);
        assertEquals(actual.getCppCustomerSeq(), cppCustomerSeq);
    }
}
