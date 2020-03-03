package com.gfs.cpp.common.util;

public enum CustomerTypeCodeEnum {

    // @formatter:off
	CONTRACT_MANAGEMENT_GROUP(31, "Contract Management Group", "CMG"),
	SUPER_STREET_MANAGED(22, "Super Street Managed", "SPMG"),
	SUPER_BID_MANAGED(21, "Super Bid Managed", "SPMG"),
	SUPER_CHAIN_MANAGED_LINK(20, "Super Chain Managed - Link", "SPMG"),
	SUPER_CHAIN_MANAGED_PRICING(19, "Super Chain Managed - Pricing Only", "SPMG"),
	STREET_MANAGED(18, "Street Managed", "PMG"),
	BID_MANAGED(17, "Bid Managed", "PMG"),
	CHAIN_MANAGED_LINK(16, "Chain Managed - Linked", "PMG"),
	CHAIN_MANAGED_PRICING(15, "Chain Managed - Pricing Only", "PMG"),
	LEGACY_PRICING_GROUP(5, "80000 Legacy Pricing Group", "PMG"),
	CUSTOMER_FAMILY(3, "Customer Family", "Family"),
	CUSTOMER_UNIT(0, "Customer Unit", "Unit");

    // @formatter:on
    private final Integer gfsCustomerTypeCode;
    private final String gfsCustomerTypeName;
    private final String groupType;

    CustomerTypeCodeEnum(final Integer gfsCustomerTypeCode, final String gfsCustomerTypeName, final String groupType) {
        this.gfsCustomerTypeCode = gfsCustomerTypeCode;
        this.gfsCustomerTypeName = gfsCustomerTypeName;
        this.groupType = groupType;
    }

    public Integer getGfsCustomerTypeCode() {
        return gfsCustomerTypeCode;
    }

    public String getGfsCustomerTypeName() {
        return gfsCustomerTypeName;
    }

    public String getGroupType() {
        return groupType;
    }

    public static String getNameByCode(Integer typeCode) {
        for (CustomerTypeCodeEnum customerTypeEnum : values()) {
            if (customerTypeEnum.gfsCustomerTypeCode.equals(typeCode)) {
                return customerTypeEnum.groupType;
            }
        }
        return null;
    }

    public static String getTypeNameByCode(Integer typeCode) {
        for (CustomerTypeCodeEnum customerTypeEnum : values()) {
            if (customerTypeEnum.gfsCustomerTypeCode.equals(typeCode)) {
                return customerTypeEnum.gfsCustomerTypeName;
            }
        }
        return null;
    }
}
