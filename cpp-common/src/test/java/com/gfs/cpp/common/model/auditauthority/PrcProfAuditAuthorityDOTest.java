package com.gfs.cpp.common.model.auditauthority;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.model.auditauthority.PrcProfAuditAuthorityDO;

@RunWith(MockitoJUnitRunner.class)
public class PrcProfAuditAuthorityDOTest {

    @InjectMocks
    private PrcProfAuditAuthorityDO prcProfAuditAuthorityDO;

    @Test
    public void testPrcProfAuditAuthorityDO() {
        prcProfAuditAuthorityDO.setContractPriceProfileSeq(1);
        ;
        prcProfAuditAuthorityDO.setGfsCustomerId("1");
        prcProfAuditAuthorityDO.setGfsCustomerTypeCode(22);
        prcProfAuditAuthorityDO.setCreateUserId("test user");
        prcProfAuditAuthorityDO.setEffectiveDate(new LocalDate(2018, 05, 05).toDate());
        prcProfAuditAuthorityDO.setExpirationDate(new LocalDate(2019, 05, 05).toDate());
        prcProfAuditAuthorityDO.setLastUpdateUserId("test user");
        prcProfAuditAuthorityDO.setPrcProfAuditAuthorityInd(1);

        final PrcProfAuditAuthorityDO actual = SerializationUtils.clone(prcProfAuditAuthorityDO);

        assertThat(prcProfAuditAuthorityDO.equals(actual), is(true));
        assertThat(prcProfAuditAuthorityDO.hashCode(), is(actual.hashCode()));
        assertThat(prcProfAuditAuthorityDO.toString() != null, is(true));

        assertThat(prcProfAuditAuthorityDO.getContractPriceProfileSeq(), is(actual.getContractPriceProfileSeq()));
        assertThat(prcProfAuditAuthorityDO.getGfsCustomerId(), is(actual.getGfsCustomerId()));
        assertThat(prcProfAuditAuthorityDO.getGfsCustomerTypeCode(), is(actual.getGfsCustomerTypeCode()));
        assertThat(prcProfAuditAuthorityDO.getCreateUserId(), is(actual.getCreateUserId()));
        assertThat(prcProfAuditAuthorityDO.getEffectiveDate(), is(actual.getEffectiveDate()));
        assertThat(prcProfAuditAuthorityDO.getExpirationDate(), is(actual.getExpirationDate()));
        assertThat(prcProfAuditAuthorityDO.getLastUpdateUserId(), is(actual.getLastUpdateUserId()));
        assertThat(prcProfAuditAuthorityDO.getPrcProfAuditAuthorityInd(), is(actual.getPrcProfAuditAuthorityInd()));

    }

}
