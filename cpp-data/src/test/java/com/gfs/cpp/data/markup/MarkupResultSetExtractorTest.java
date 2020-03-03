package com.gfs.cpp.data.markup;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.furtherance.FurtheranceAction;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.data.markup.MarkupResultSetExtractor;

@RunWith(MockitoJUnitRunner.class)
public class MarkupResultSetExtractorTest {

    @InjectMocks
    private MarkupResultSetExtractor target;
    
    @Mock
    private CPPDateUtils cppDateUtils;

    @Mock
    private ResultSet rs;

    private static final String GFS_CUSTOMER_ID = "GFS_CUSTOMER_ID";
    private static final String GFS_CUSTOMER_TYPE_CODE = "GFS_CUSTOMER_TYPE_CODE";
    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String FURTHERANCE_ACTION_CODE = "FURTHERANCE_ACTION_CODE";
    private static final String ITEM_PRICE_ID = "ITEM_PRICE_ID";
    private static final String EFFECTIVE_DATE = "EFFECTIVE_DATE";
    private static final String EXPIRATION_DATE = "EXPIRATION_DATE";
    private static final String COST_MARKUP_AMT = "COST_MARKUP_AMT";
    private static final String MARKUP_UNIT_TYPE_CODE = "MARKUP_UNIT_TYPE_CODE";
    private static final String MARKUP_AMOUNT_TYPE_CODE = "MARKUP_AMOUNT_TYPE_CODE";
    private static final String ITEM_PRICE_LEVEL_CODE = "ITEM_PRICE_LEVEL_CODE";
    private static final String PRICE_LOCKED_IN_TYPE_CODE = "PRICE_LOCKED_IN_TYPE_CODE";
    private static final String HOLD_COST_FIRM_IND = "HOLD_COST_FIRM_IND";
    private static final String PRICE_LOCKIN_REASON_CODE = "PRICE_LOCKIN_REASON_CODE";
    private static final String PRICE_MAINTENANCE_SOURCE_CODE = "PRICE_MAINTENANCE_SOURCE_CODE";
    private static final String CUSTOMER_ITEM_PRICE_SEQ = "CUSTOMER_ITEM_PRICE_SEQ";

    @Test
    public void extractData_returnsNull_whenTheNoMarkupIsEdited() throws SQLException {

        when(rs.next()).thenReturn(false);

        final Map<Integer, List<ProductTypeMarkupDTO>> actual = target.extractData(rs);

        assertThat(actual.size(), equalTo(0));
    }

    @Test
    public void shouldBuildMapWithFurtheranceActionAsKeyforMarkupFurtheranceChanges() throws SQLException {
        final String gfsCustomerId = "100";
        final int gfsCustomerTypeCode = 31;
        final int contractPriceProfileSeq = 1;
        final java.util.Date effectiveDate = new LocalDate(2018, 1, 1).toDate();
        final java.util.Date expirationDate = new LocalDate(2019, 1, 1).toDate();

        String costMarkupAmount = "$";
        int markupUnitTypeCode = 2;
        int furtheranceActionCode = FurtheranceAction.UPDATED.getCode();
        int itemPriceId = 22;
        int itemPriceLevelCode = 0;
        int priceLockinTypeCode = 34;
        int holdCostFirm = 5;
        int priceLockInReasonCode = 5;
        int priceMainReasonCode = 5;
        int cipSeq = 34;
        String markupAmountTypeCode = "%";
        
        when(cppDateUtils.getCurrentDate()).thenReturn(new LocalDate(2018, 01, 01).toDate());
        when(rs.getInt(FURTHERANCE_ACTION_CODE)).thenReturn(furtheranceActionCode);
        when(rs.getInt(GFS_CUSTOMER_TYPE_CODE)).thenReturn(gfsCustomerTypeCode);
        when(rs.getString(GFS_CUSTOMER_ID)).thenReturn(gfsCustomerId);
        when(rs.getInt(ITEM_PRICE_ID)).thenReturn(itemPriceId);
        when(rs.getDate(EFFECTIVE_DATE)).thenReturn(new Date(effectiveDate.getTime()));
        when(rs.getDate(EXPIRATION_DATE)).thenReturn(new Date(expirationDate.getTime()));
        when(rs.getString(COST_MARKUP_AMT)).thenReturn(costMarkupAmount);
        when(rs.getInt(MARKUP_UNIT_TYPE_CODE)).thenReturn(markupUnitTypeCode);
        when(rs.getString(MARKUP_AMOUNT_TYPE_CODE)).thenReturn(markupAmountTypeCode);
        when(rs.getInt(ITEM_PRICE_LEVEL_CODE)).thenReturn(itemPriceLevelCode);
        when(rs.getInt(CONTRACT_PRICE_PROFILE_SEQ)).thenReturn(contractPriceProfileSeq);
        when(rs.getInt(PRICE_LOCKED_IN_TYPE_CODE)).thenReturn(priceLockinTypeCode);
        when(rs.getInt(HOLD_COST_FIRM_IND)).thenReturn(holdCostFirm);
        when(rs.getInt(PRICE_LOCKIN_REASON_CODE)).thenReturn(priceLockInReasonCode);
        when(rs.getInt(PRICE_MAINTENANCE_SOURCE_CODE)).thenReturn(priceMainReasonCode);
        when(rs.getInt(CUSTOMER_ITEM_PRICE_SEQ)).thenReturn(cipSeq);

        Mockito.when(rs.next()).thenReturn(true).thenReturn(false);

        final Map<Integer, List<ProductTypeMarkupDTO>> actual = target.extractData(rs);
        
        verify(cppDateUtils).getCurrentDate();
      
        List<ProductTypeMarkupDTO> addedItems = actual.get(FurtheranceAction.ADDED.getCode());
        List<ProductTypeMarkupDTO> deleteddItems = actual.get(FurtheranceAction.DELETED.getCode());
        List<ProductTypeMarkupDTO> updatedItems = actual.get(FurtheranceAction.UPDATED.getCode());

        assertThat(addedItems, equalTo(null));
        assertThat(updatedItems.size(), equalTo(1));
        assertThat(deleteddItems, equalTo(null));

        assertThat(updatedItems.get(0).getGfsCustomerTypeCode(), equalTo(gfsCustomerTypeCode));
        assertThat(updatedItems.get(0).getGfsCustomerId(), equalTo(gfsCustomerId));
        assertThat(updatedItems.get(0).getProductType(), equalTo(String.valueOf(itemPriceLevelCode)));
        assertThat(updatedItems.get(0).getPriceLockedInTypeCode(), equalTo(priceLockinTypeCode));
        assertThat(updatedItems.get(0).getContractPriceProfileSeq(), equalTo(contractPriceProfileSeq));
        assertThat(updatedItems.get(0).getCustomerItemPriceSeq(), equalTo(cipSeq));
        assertThat(updatedItems.get(0).getPriceMaintenanceSourceCode(), equalTo(priceMainReasonCode));
        assertThat(updatedItems.get(0).getPriceLockinReasonCode(), equalTo(priceLockInReasonCode));
        assertThat(updatedItems.get(0).getHoldCostFirmInd(), equalTo(holdCostFirm));

    }
}
