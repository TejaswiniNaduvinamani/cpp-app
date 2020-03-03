package com.gfs.cpp.acceptanceTests.hookdefs;

import org.springframework.beans.factory.annotation.Autowired;

import com.gfs.cpp.acceptanceTests.common.data.CppCucumberRepository;
import com.gfs.cpp.acceptanceTests.config.CukesConstants;

import cucumber.api.java.Before;

public class CPPCucumberDataSetupHook {

    @Autowired
    private CppCucumberRepository cppCucumberRepository;

    @Before
    public void runDataSetup() {

        if (useLocalDB()) {
            cppCucumberRepository.insertContractType(CukesConstants.CLM_CONTRACT_TYPE_REGIIONAL, "cucumber test doc");
            cppCucumberRepository.insertContractType(CukesConstants.CLM_CONTRACT_TYPE_STREET, "cucumber test doc");
            cppCucumberRepository.insertContractType(CukesConstants.CLM_CONTRACT_TYPE_REGIONAL_AMENDMENT, "cucumber test doc");
            cppCucumberRepository.insertCostModel(100, 70, 1, 1, 1, 1);
        }
    }

    private boolean useLocalDB() {
        return "local".equalsIgnoreCase(System.getProperty("runtime.env"));
    }

    @Before("@clm_token-setup")
    public void setupApplicationToken() {
        if (useLocalDB()) {
            cppCucumberRepository.insertApplicationToken();
        }
    }

    @Before("@language-setup")
    public void setupLangugageData() {
        if (useLocalDB()) {
            insertLanguageAttribute();
            insertAttributeMap();
            insertAttributeStatement();
        }
    }

    private void insertAttributeStatement() {

        cppCucumberRepository.insertCppLanguageAttributeStatement(1, "statement 1");
        cppCucumberRepository.insertCppLanguageAttributeStatement(2, "statement 2");
        cppCucumberRepository.insertCppLanguageAttributeStatement(3, "statement 3");
        cppCucumberRepository.insertCppLanguageAttributeStatement(4, "statement 4");
        cppCucumberRepository.insertCppLanguageAttributeStatement(5, "statement 5");
        cppCucumberRepository.insertCppLanguageAttributeStatement(6, "statement 6");
        cppCucumberRepository.insertCppLanguageAttributeStatement(7, "statement 7");
        cppCucumberRepository.insertCppLanguageAttributeStatement(8, "statement 8");
        cppCucumberRepository.insertCppLanguageAttributeStatement(9, "statement 9");
        cppCucumberRepository.insertCppLanguageAttributeStatement(10, "statement 10");
        cppCucumberRepository.insertCppLanguageAttributeStatement(11, "statement 11");
        cppCucumberRepository.insertCppLanguageAttributeStatement(12, "statement 12");
        cppCucumberRepository.insertCppLanguageAttributeStatement(13, "statement 13");
        cppCucumberRepository.insertCppLanguageAttributeStatement(14, "statement 14");
        cppCucumberRepository.insertCppLanguageAttributeStatement(15, "statement 15");
        cppCucumberRepository.insertCppLanguageAttributeStatement(16, "statement 16");
        cppCucumberRepository.insertCppLanguageAttributeStatement(17, "statement 17");
        cppCucumberRepository.insertCppLanguageAttributeStatement(18, "statement 18");
        cppCucumberRepository.insertCppLanguageAttributeStatement(19, "statement 19");

    }

    private void insertAttributeMap() {
        cppCucumberRepository.insertCppLanguageAttributeMap(1, 1, "DEFAULT", 2);
        cppCucumberRepository.insertCppLanguageAttributeMap(2, 1, "W", 3);
        cppCucumberRepository.insertCppLanguageAttributeMap(3, 2, "DEFAULT", 4);
        cppCucumberRepository.insertCppLanguageAttributeMap(4, 2, "70", 5);
        cppCucumberRepository.insertCppLanguageAttributeMap(5, 2, "80", 6);
        cppCucumberRepository.insertCppLanguageAttributeMap(6, 2, "85", 7);
        cppCucumberRepository.insertCppLanguageAttributeMap(7, 2, "82", 8);
        cppCucumberRepository.insertCppLanguageAttributeMap(8, 3, "1", 9);
        cppCucumberRepository.insertCppLanguageAttributeMap(9, 3, "0", 10);
        cppCucumberRepository.insertCppLanguageAttributeMap(10, 4, "1", 11);
        cppCucumberRepository.insertCppLanguageAttributeMap(11, 4, "0", 1);
        cppCucumberRepository.insertCppLanguageAttributeMap(12, 5, "1", 12);
        cppCucumberRepository.insertCppLanguageAttributeMap(13, 5, "0", 1);
        cppCucumberRepository.insertCppLanguageAttributeMap(14, 6, "DEFAULT", 13);
        cppCucumberRepository.insertCppLanguageAttributeMap(15, 7, "1", 15);
        cppCucumberRepository.insertCppLanguageAttributeMap(16, 7, "2", 17);
        cppCucumberRepository.insertCppLanguageAttributeMap(17, 7, "3", 17);
        cppCucumberRepository.insertCppLanguageAttributeMap(18, 8, "2", 18);
        cppCucumberRepository.insertCppLanguageAttributeMap(19, 8, "3", 19);

    }

    private void insertLanguageAttribute() {
        cppCucumberRepository.insertCppLanguageAttribute(1, "PRC_PROF_COST_RUN_SCHED_GROUP.SCHEDULE_GROUP_SEQ", "Cost Schedule Package");
        cppCucumberRepository.insertCppLanguageAttribute(2, "COST_MODEL_MAP.COST_MODEL_ID", "Cost Model");
        cppCucumberRepository.insertCppLanguageAttribute(3, "PRC_PROF_PRICING_RULE_OVRD.PRICING_OVERRIDE_ID", "Markup based on sell?");
        cppCucumberRepository.insertCppLanguageAttribute(4, "PRC_PROF_AUDIT_AUTHORITY.PRC_PROF_AUDIT_AUTHORITY_IND", "Formal Price Audit Privileges");
        cppCucumberRepository.insertCppLanguageAttribute(5, "PRC_PROF_VERIF_PRIVILEGE.PRC_PROF_VERIF_PRIVLG_SEQ", "Price Verification privileges");
        cppCucumberRepository.insertCppLanguageAttribute(6, "PRF_PROF_RULE_CALC_SEQ.PRF_PROF_RULE_CALC_SEQ_SEQ", "Price Calculation Sequence");
        cppCucumberRepository.insertCppLanguageAttribute(7, "CUSTOMER_ITEM_PRICE.MARKUP_UNIT_TYPE_CODE", "Markup Type");
        cppCucumberRepository.insertCppLanguageAttribute(8, "PRC_PROF_LESS_CASE_RULE.LESSCASE_PRICE_RULE_ID", "Split Case Fee Type");
    }

}
