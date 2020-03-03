package com.gfs.cpp.common.dto.furtherance;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.gfs.cpp.common.dto.markup.MarkupReviewDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseReviewDTO;

public class FurtheranceDocumentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private FurtheranceInformationDTO furtheranceInformationDTO;
    private MarkupReviewDTO markupDTO;
    private SplitCaseReviewDTO splitCaseDTO;

    public FurtheranceInformationDTO getFurtheranceInformationDTO() {
        return furtheranceInformationDTO;
    }

    public void setFurtheranceInformationDTO(FurtheranceInformationDTO furtheranceInformationDTO) {
        this.furtheranceInformationDTO = furtheranceInformationDTO;
    }

    public MarkupReviewDTO getMarkupDTO() {
        return markupDTO;
    }

    public void setMarkupDTO(MarkupReviewDTO markupDTO) {
        this.markupDTO = markupDTO;
    }

    public SplitCaseReviewDTO getSplitCaseDTO() {
        return splitCaseDTO;
    }

    public void setSplitCaseDTO(SplitCaseReviewDTO splitCaseDTO) {
        this.splitCaseDTO = splitCaseDTO;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, false);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
