package com.gfs.cpp.acceptanceTests.common.data;

public enum ChangeTableName {
	MARKUP("CUSTOMER_ITEM_PRICE"), SPLITCASE("PRC_PROF_LESSCASE_RULE");
	
	String tableName;
	
	private ChangeTableName(String tableName) {
		
		this.tableName = tableName;
		
	}

	public String getTableName() {
		
		return tableName;
	}
}
