package com.gfs.cpp.common.dto.review;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.gfs.cpp.common.dto.contractpricing.ContractPricingReviewDTO;
import com.gfs.cpp.common.dto.markup.MarkupReviewDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseReviewDTO;

public class ReviewDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private ContractPricingReviewDTO contractPricingReviewDTO;
    private List<String> distributionCenter;
    private MarkupReviewDTO markupReviewDTO;
    private SplitCaseReviewDTO splitCaseReviewDTO;

    public ContractPricingReviewDTO getContractPricingReviewDTO() {
        return contractPricingReviewDTO;
    }

    public void setContractPricingReviewDTO(ContractPricingReviewDTO contractPricingReviewDTO) {
        this.contractPricingReviewDTO = contractPricingReviewDTO;
    }

    public List<String> getDistributionCenter() {
        return distributionCenter;
    }

    public void setDistributionCenter(List<String> distributionCenter) {
        this.distributionCenter = distributionCenter;
    }

    public MarkupReviewDTO getMarkupReviewDTO() {
        return markupReviewDTO;
    }

    public void setMarkupReviewDTO(MarkupReviewDTO markupReviewDTO) {
        this.markupReviewDTO = markupReviewDTO;
    }

    public SplitCaseReviewDTO getSplitCaseReviewDTO() {
        return splitCaseReviewDTO;
    }

    public void setSplitCaseReviewDTO(SplitCaseReviewDTO splitCaseReviewDTO) {
        this.splitCaseReviewDTO = splitCaseReviewDTO;
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
