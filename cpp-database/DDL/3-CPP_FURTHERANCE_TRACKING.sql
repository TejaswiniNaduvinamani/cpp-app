CREATE SEQUENCE PRICE_ADMIN.CPP_FURTHERANCE_TRACKING_SEQ MINVALUE 1 MAXVALUE 999999999 INCREMENT by 1 CACHE 20;


CREATE TABLE PRICE_ADMIN.CPP_FURTHERANCE_TRACKING (
	CPP_FURTHERANCE_TRACKING_SEQ			NUMBER(9) NOT NULL,
	CPP_FURTHERANCE_SEQ						NUMBER(9) NOT NULL,
	FURTHERANCE_ACTION_CODE					NUMBER (1) NOT NULL,
	ITEM_PRICE_ID                  			VARCHAR2(50) NOT NULL,
	ITEM_PRICE_LEVEL_CODE					NUMBER NOT NULL,
	GFS_CUSTOMER_ID							VARCHAR2(9) NOT NULL,
	GFG_CUSTOMER_TYPE_CODE					NUMBER NOT NULL,
	CHANGE_TABLE_NAME						VARCHAR2(30)  NOT NULL,
	CREATE_USER_ID							VARCHAR2(100) NOT NULL,
	CREATE_TMSTMP							TIMESTAMP(6) WITH TIME ZONE default systimestamp NOT NULL,
	LAST_UPDATE_USER_ID						VARCHAR2(100) NOT NULL,
	LAST_UPDATE_TMSTMP						TIMESTAMP(6) WITH TIME ZONE default systimestamp NOT NULL,
	constraint CPP_FURTHERANCE_TRACKING_PK primary key (CPP_FURTHERANCE_TRACKING_SEQ),
	constraint FK_CPP_FURTHERANCE_ACTION_01 foreign key(FURTHERANCE_ACTION_CODE)
		references PRICE_ADMIN.CPP_FURTHERANCE_ACTION(FURTHERANCE_ACTION_CODE),
	constraint FK_CPP_FURTHERANCE_01 foreign key(CPP_FURTHERANCE_SEQ)
		references PRICE_ADMIN.CPP_FURTHERANCE(CPP_FURTHERANCE_SEQ),
	constraint FK_ITEM_PRICE_LEVEL_01 foreign key(ITEM_PRICE_LEVEL_CODE)
		references PRICE_ADMIN.ITEM_PRICE_LEVEL(ITEM_PRICE_LEVEL_CODE),
	constraint FK_GFS_CUSTOMER_01 foreign key(GFS_CUSTOMER_ID, GFS_CUSTOMER_TYPE_CODE)
		references CUST_ADMIN.GFS_CUSTOMER(GFS_CUSTOMER_ID, GFS_CUSTOMER_TYPE_CODE),
	CONSTRAINT CPP_FURTHERANCE_TRACKING_UK1 UNIQUE (CPP_FURTHERANCE_SEQ,ITEM_PRICE_ID,ITEM_PRICE_LEVEL_CODE,GFS_CUSTOMER_ID,GFS_CUSTOMER_TYPE_CODE,CREATE_TMSTMP)	
);

