package com.gfs.cpp.common.dto.furtherance;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.gfs.cpp.common.dto.splitcase.SplitCaseDTO;

public class SplitCaseGridFurtheranceDTO implements Serializable{

    private static final long serialVersionUID = 1L;
    private int contractPriceProfileSeq;
    private List<SplitCaseDTO> splitCaseFeeValues;
    private int cppFurtheranceSeq;    

    public int getContractPriceProfileSeq() {
        return contractPriceProfileSeq;
    }

    public void setContractPriceProfileSeq(int contractPriceProfileSeq) {
        this.contractPriceProfileSeq = contractPriceProfileSeq;
    }

    public List<SplitCaseDTO> getSplitCaseFeeValues() {
        return splitCaseFeeValues;
    }

    public void setSplitCaseFeeValues(List<SplitCaseDTO> splitCaseFeeValues) {
        this.splitCaseFeeValues = splitCaseFeeValues;
    }

    public int getCppFurtheranceSeq() {
		return cppFurtheranceSeq;
	}

	public void setCppFurtheranceSeq(int cppFurtheranceSeq) {
		this.cppFurtheranceSeq = cppFurtheranceSeq;
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
