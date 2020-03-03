package com.gfs.cpp.data.furtherance;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.furtherance.FurtheranceInformationDTO;
import com.gfs.cpp.data.furtherance.FurtheranceInformationDTORowMapper;

@RunWith(MockitoJUnitRunner.class)
public class FurtheranceInformationDTORowMapperTest {

    @InjectMocks
    private FurtheranceInformationDTORowMapper target;

    @Mock
    private ResultSet rs;

    private static final String CPP_FURTHERANCE_SEQ = "CPP_FURTHERANCE_SEQ";
    private static final String CONTRACT_PRICE_PROFILE_SEQ = "CONTRACT_PRICE_PROFILE_SEQ";
    private static final String FURTHERANCE_STATUS_CODE = "FURTHERANCE_STATUS_CODE";
    private static final String PARENT_CLM_AGREEMENT_ID = "PARENT_CLM_AGREEMENT_ID";
    private static final String FURTHERANCE_EFFECTIVE_DATE = "FURTHERANCE_EFFECTIVE_DATE";
    private static final String CHANGE_REASON_TXT = "CHANGE_REASON_TXT";
    private static final String CONTRACT_REFERENCE_TXT = "CONTRACT_REFERENCE_TXT";
    private static final String FURTHERANCE_DOCUMENT_GUID = "FURTHERANCE_DOCUMENT_GUID";

    @Test
    public void shouldMapRowIntoFurtheranceInformationDTO() throws SQLException {
        String changeReason = "test";
        String referenceText = "reference";
        int cppFurtherenceSeq = 1;
        int cppSeq = 1;
        String docGuid = "test";
        int furtheranceStatusCode = 1;
        String parentAgreementId = "parent-agreement-id";

        when(rs.getString(CHANGE_REASON_TXT)).thenReturn(changeReason);
        when(rs.getString(CONTRACT_REFERENCE_TXT)).thenReturn(referenceText);
        when(rs.getInt(CPP_FURTHERANCE_SEQ)).thenReturn(cppFurtherenceSeq);
        when(rs.getInt(CONTRACT_PRICE_PROFILE_SEQ)).thenReturn(cppSeq);
        when(rs.getString(FURTHERANCE_DOCUMENT_GUID)).thenReturn(docGuid);
        when(rs.getInt(FURTHERANCE_STATUS_CODE)).thenReturn(furtheranceStatusCode);
        when(rs.getDate(FURTHERANCE_EFFECTIVE_DATE)).thenReturn(null);
        when(rs.getString(PARENT_CLM_AGREEMENT_ID)).thenReturn(parentAgreementId);

        final FurtheranceInformationDTO actual = target.mapRow(rs, 0);

        assertThat(actual.getChangeReasonTxt(), is(changeReason));
        assertThat(actual.getContractPriceProfileSeq(), is(cppSeq));
        assertThat(actual.getContractReferenceTxt(), is(referenceText));
        assertThat(actual.getCppFurtheranceSeq(), is(cppFurtherenceSeq));
        assertThat(actual.getFurtheranceDocumentGUID(), is(docGuid));
        assertThat(actual.getFurtheranceStatusCode(), is(furtheranceStatusCode));
        assertThat(actual.getParentCLMAgreementId(), is(parentAgreementId));

    }

}
