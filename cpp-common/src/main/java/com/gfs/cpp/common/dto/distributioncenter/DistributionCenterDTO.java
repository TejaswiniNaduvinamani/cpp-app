package com.gfs.cpp.common.dto.distributioncenter;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class DistributionCenterDTO implements Serializable{
    
    private static final long serialVersionUID = 1L;
    private String dcNumber;
    private String name;
    private String shortName;
    private String wmsDBInstanceId;
    /**
     * @return the dcNumber
     */
    public String getDcNumber() {
        return dcNumber;
    }
    /**
     * @param dcNumber the dcNumber to set
     */
    public void setDcNumber(String dcNumber) {
        this.dcNumber = dcNumber;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the shortName
     */
    public String getShortName() {
        return shortName;
    }
    /**
     * @param shortName the shortName to set
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
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
    public String getWmsDBInstanceId() {
        return wmsDBInstanceId;
    }
    public void setWmsDBInstanceId(String wmsDBInstanceId) {
        this.wmsDBInstanceId = wmsDBInstanceId;
    }

}
