package com.gfs.cpp.data.distributioncenter;

import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.model.distributioncenter.DistributionCenterDetailDO;
import com.gfs.cpp.data.distributioncenter.DistributionCenterDetailRowMapper;

@RunWith(MockitoJUnitRunner.class)
public class DistributionCenterDetailRowMapperTest {

    @InjectMocks
    private DistributionCenterDetailRowMapper sut;

    @Mock
    private ResultSet resultSet;

    @Test
    public void shouldMapDistributionCenterDetailDORowMapper() throws SQLException {

        final int contractPriceProfileSeq = 1;
        final String shipDcNBR = "1";
        final Date effectiveDate = randomDate();
        final Date expirationDate = randomDate();

        when(resultSet.getInt("CONTRACT_PRICE_PROFILE_SEQ")).thenReturn(contractPriceProfileSeq);
        when(resultSet.getString("SHIP_DC_NBR")).thenReturn(new String(shipDcNBR));
        when(resultSet.getDate("EFFECTIVE_DATE")).thenReturn(effectiveDate);
        when(resultSet.getDate("EXPIRATION_DATE")).thenReturn(expirationDate);

        final DistributionCenterDetailDO actual = sut.mapRow(resultSet, 0);

        assertEquals(actual.getEffectiveDate(), effectiveDate);
        assertEquals(actual.getExpirationDate(), expirationDate);
        assertEquals(actual.getDcCode(), new String(shipDcNBR));
        assertEquals(actual.getContractPriceProfileSeq(), contractPriceProfileSeq);

    }

    private Date randomDate() {
        return new Date(randomLong());
    }

    private Long randomLong() {
        return nextLong(0L, 1000L);
    }
}
