package com.gfs.cpp.common.dto.furtherance;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.furtherance.FurtheranceInformationDTO;

@RunWith(MockitoJUnitRunner.class)
public class FurtheranceInformationDTOTest {

    @InjectMocks
    private FurtheranceInformationDTO dto;

    @Test
    public void testMethods() {
        dto.setChangeReasonTxt("test-reason");
        dto.setContractPriceProfileSeq(1);
        dto.setContractReferenceTxt("test");
        dto.setCppFurtheranceSeq(1);
        dto.setFurtheranceDocumentGUID("test-doc");
        dto.setFurtheranceStatusCode(1);
        dto.setFurtheranceEffectiveDate(new Date());
        dto.setParentCLMAgreementId("parent-agreement-id");

        final FurtheranceInformationDTO actual = SerializationUtils.clone(dto);
        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));

        assertThat(actual.getChangeReasonTxt(), is(dto.getChangeReasonTxt()));
        assertThat(actual.getContractPriceProfileSeq(), is(dto.getContractPriceProfileSeq()));
        assertThat(actual.getContractReferenceTxt(), is(dto.getContractReferenceTxt()));
        assertThat(actual.getCppFurtheranceSeq(), is(dto.getCppFurtheranceSeq()));
        assertThat(actual.getFurtheranceDocumentGUID(), is(dto.getFurtheranceDocumentGUID()));
        assertThat(actual.getFurtheranceStatusCode(), is(dto.getFurtheranceStatusCode()));
        assertThat(actual.getFurtheranceEffectiveDate(), is(dto.getFurtheranceEffectiveDate()));
        assertThat(actual.getParentCLMAgreementId(), is(dto.getParentCLMAgreementId()));
    }

}
