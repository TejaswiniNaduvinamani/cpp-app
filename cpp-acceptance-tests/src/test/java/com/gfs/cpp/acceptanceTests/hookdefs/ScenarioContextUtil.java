package com.gfs.cpp.acceptanceTests.hookdefs;

import com.gfs.cpp.acceptanceTests.config.CukesConstants;

/**
 * Util class to get ScenarioContext instance for current scenario. Also provides convenient setter and getter methods to store and retrieve
 * frequently used attributes.
 * 
 * 
 */
public class ScenarioContextUtil {

    private static ThreadLocal<ScenarioContext> threadLocal = new ThreadLocal<ScenarioContext>() {

        @Override
        protected ScenarioContext initialValue() {
            return new ScenarioContext();
        }
    };

    public static ScenarioContext getScenarioContext() {
        return threadLocal.get();
    }

    public static void removeScenarioContext() {
        threadLocal.remove();
    }

    public static void setContractPriceProfileSeq(Integer contractPriceProfileSeq) {
        setAttribute("contractPriceProfileSeq", contractPriceProfileSeq);
    }

    public static void setExistingContractPriceProfileSeq(Integer existingContractPriceProfileSeq) {
        setAttribute("existingContractPriceProfileSeq", existingContractPriceProfileSeq);
    }

    public static Integer getContractPriceProfileSeq() {
        return (Integer) getAttribute("contractPriceProfileSeq");
    }

    public static Integer getExistingContractPriceProfileSeq() {
        return (Integer) getAttribute("existingContractPriceProfileSeq");
    }

    public static void setAmendmentContractPriceProfileSeq(String agreementId, Integer amendmentContractPriceProfileSeq) {
        setAttribute("Amendment" + agreementId, amendmentContractPriceProfileSeq);
    }

    public static Integer getAmendmentContractPriceProfileSeq(String agreementId) {
        return (Integer) getAttribute("Amendment" + agreementId);
    }

    public static Integer getAmendmentContractPriceProfileSeq() {
        return (Integer) getAttribute("Amendment" + CukesConstants.AMENDMENT_AGREEMENT_ID);
    }

    public static void setAttribute(String attributeName, Object attributeValue) {
        getScenarioContext().setAttribute(attributeName, attributeValue);
    }

    public static Object getAttribute(String attributeName) {
        return getScenarioContext().getAttribute(attributeName);
    }
}
