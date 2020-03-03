package com.gfs.cpp.data.activatepricing;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.priceactivation.ContractCustomerMappingDTO;
import com.gfs.cpp.data.activatepricing.ContractCustomerRowMapper;

@RunWith(MockitoJUnitRunner.class)
public class ContractCustomerRowMapperTest {

    @InjectMocks
    private ContractCustomerRowMapper target;

    @Mock
    private ResultSet rs;

    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
    private static final String DEFAULT_CUSTOMER_IND = "DEFAULT_CUSTOMER_IND";
    private static final String CPP_CUSTOMER_SEQ = "CPP_CUSTOMER_SEQ";
    private static final String CPP_CONCEPT_MAPPING_SEQ = "CPP_CONCEPT_MAPPING_SEQ";
    
    @Test
    public void shouldMapConceptsToCustomer() throws SQLException {
        
        final String gfsCustomerId = "Customer Id";
        final int gfsCustomerTypeCode = 31;
        final int defaultIndicatorId = 1;
        final int cppCustomerSeq = -101;
        final int cppcConceptMappingSeq = -100;

        when(rs.getString(GFS_CUSTOMER_ID)).thenReturn(gfsCustomerId);
        when(rs.getInt(GFS_CUSTOMER_TYPE_CODE)).thenReturn(gfsCustomerTypeCode);
        when(rs.getInt(DEFAULT_CUSTOMER_IND)).thenReturn(defaultIndicatorId);
        when(rs.getInt(CPP_CUSTOMER_SEQ)).thenReturn(cppCustomerSeq);
        when(rs.getInt(CPP_CONCEPT_MAPPING_SEQ)).thenReturn(cppcConceptMappingSeq);
  
        final ContractCustomerMappingDTO actual = target.mapRow(rs, 0);

        assertEquals(actual.getGfsCustomerId(), gfsCustomerId);
        assertEquals(actual.getGfsCustomerTypeCode(), gfsCustomerTypeCode);
        assertEquals(actual.getDefaultCustomerInd(), defaultIndicatorId);
        assertEquals(actual.getCppCustomerSeq(), cppCustomerSeq);
        assertEquals(actual.getCppConceptMappingSeq(), cppcConceptMappingSeq);
    }
}