package com.gfs.cpp.data.splitcase;

import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.model.splitcase.PrcProfLessCaseRuleDO;
import com.gfs.cpp.data.splitcase.PrcProfLessCaseRuleRowMapper;

@RunWith(MockitoJUnitRunner.class)
public class PrcProfLessCaseRuleRowMapperTest {

    @InjectMocks
    private PrcProfLessCaseRuleRowMapper target;

    @Mock
    private ResultSet rs;

    private static final String ITEM_PRICE_ID = "ITEM_PRICE_ID";
    private static final String NON_CW_MARKUP_AMT = "NON_CW_MARKUP_AMT";
    private static final String NON_CW_MARKUP_AMOUNT_TYPE_CODE = "NON_CW_MARKUP_AMOUNT_TYPE_CODE";
    private static final String ITEM_PRICE_LEVEL_CODE = "ITEM_PRICE_LEVEL_CODE";
    private static final String LESSCASE_PRICE_RULE_ID = "LESSCASE_PRICE_RULE_ID";
    private static final String CW_MARKUP_AMT = "CW_MARKUP_AMT";
    private static final String CW_MARKUP_AMOUNT_TYPE_CODE = "CW_MARKUP_AMOUNT_TYPE_CODE";
    private static final String MARKUP_APPLIED_BEFORE_DIV_IND = "MARKUP_APPLIED_BEFORE_DIV_IND";
    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
    private static final String EFFECTIVE_DATE = "EFFECTIVE_DATE";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";

    @Test
    public void shouldMapRowIntoPrcProfLessCaseRuleDO() throws SQLException {
        String itemPriceId = "1";
        int itemPriceLevelCode = 0;
        int cwMarkupAmnt = 2;
        String cwmarkupAmountTypeCode = "$";
        int priceRuleId = 2;
        int markupAppliedBeforeDivInd = 1;
        int nonCWMarkupAmnt = 5;
        String nonCWmarkupAmountTypeCode = "%";
        int cppSeq = -1;
        String gfsCustId = "-234";
        int gfsCustomerTypeCode = -31;
        final Date effectiveDate = randomDate();
        final Date expirationDate = randomDate();

        when(rs.getInt(CONTRACT_PRICE_PROFILE_SEQ)).thenReturn(cppSeq);
        when(rs.getInt(CW_MARKUP_AMT)).thenReturn(cwMarkupAmnt);
        when(rs.getString(CW_MARKUP_AMOUNT_TYPE_CODE)).thenReturn(cwmarkupAmountTypeCode);
        when(rs.getInt(LESSCASE_PRICE_RULE_ID)).thenReturn(priceRuleId);
        when(rs.getInt(MARKUP_APPLIED_BEFORE_DIV_IND)).thenReturn(markupAppliedBeforeDivInd);
        when(rs.getInt(NON_CW_MARKUP_AMT)).thenReturn(nonCWMarkupAmnt);
        when(rs.getString(NON_CW_MARKUP_AMOUNT_TYPE_CODE)).thenReturn(nonCWmarkupAmountTypeCode);
        when(rs.getString(ITEM_PRICE_ID)).thenReturn(itemPriceId);
        when(rs.getInt(ITEM_PRICE_LEVEL_CODE)).thenReturn(itemPriceLevelCode);
        when(rs.getString(GFS_CUSTOMER_ID)).thenReturn(gfsCustId);
        when(rs.getInt(GFS_CUSTOMER_TYPE_CODE)).thenReturn(gfsCustomerTypeCode);
        when(rs.getDate(EFFECTIVE_DATE)).thenReturn(effectiveDate);
        when(rs.getDate(EXPIRATION_DATE)).thenReturn(expirationDate);

        final PrcProfLessCaseRuleDO actual = target.mapRow(rs, 0);

        assertThat(actual.getContractPriceProfileSeq(), is(cppSeq));
        assertThat(actual.getCwMarkupAmnt(), is(cwMarkupAmnt));
        assertThat(actual.getCwMarkupAmountTypeCode(), is(cwmarkupAmountTypeCode));
        assertThat(actual.getLesscaseRuleId(), is(priceRuleId));
        assertThat(actual.getMarkupAppliedBeforeDivInd(), is(markupAppliedBeforeDivInd));
        assertThat(actual.getNonCwMarkupAmnt(), is(nonCWMarkupAmnt));
        assertThat(actual.getNonCwMarkupAmntTypeCode(), is(nonCWmarkupAmountTypeCode));
        assertThat(actual.getItemPriceId(), is(itemPriceId));
        assertThat(actual.getItemPriceLevelCode(), is(itemPriceLevelCode));
        assertThat(actual.getGfsCustomerId(), is(gfsCustId));
        assertThat(actual.getGfsCustomerTypeCode(), is(gfsCustomerTypeCode));
        assertThat(actual.getEffectiveDate().toString(), is(effectiveDate.toString()));
        assertThat(actual.getExpirationDate().toString(), is(expirationDate.toString()));
    }

    private Date randomDate() {
        return new Date(randomLong());
    }

    private Long randomLong() {
        return nextLong(0L, 1000L);
    }
}
