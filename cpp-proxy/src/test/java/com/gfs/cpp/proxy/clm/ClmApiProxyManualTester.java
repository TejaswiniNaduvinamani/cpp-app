package com.gfs.cpp.proxy.clm;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.gfs.cpp.common.dto.clm.ClmContractResponseDTO;
import com.gfs.cpp.proxy.integration.config.ClmIntegrationTestConfig;

public class ClmApiProxyManualTester {

    private static final String AGREEMENT_TYPE = "ICMDistributionAgreementStreet";
    private static final String AGREEMENT_ID = "0e022d81-a11c-4e59-b431-0dfec1c92a38";

    private static final Logger logger = LoggerFactory.getLogger(ClmApiProxy.class);

    @SuppressWarnings("resource")
    public static void main(String[] args) throws URISyntaxException, IOException {

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();

        ctx.register(ClmIntegrationTestConfig.class);
        ctx.refresh();

        ClmApiProxy clmApiProxy = ctx.getBean(ClmApiProxy.class);

        logger.info("Enter input to test (1.get,2.update,3.saveDoc,4.deleteDoc,5.GetParentAgreementId,6.updateFurtheranceURL,7.attachFurtherance):");
        Scanner reader = new Scanner(System.in);
        int inputNumber = reader.nextInt();
        logger.info("Input is {}", inputNumber);
        if (inputNumber == 1) {
            testGetAgreement(clmApiProxy);
        } else if (inputNumber == 2) {
            clmApiProxy.updateCppUrlForPricingContract(AGREEMENT_ID, AGREEMENT_TYPE);
        } else if (inputNumber == 3) {
            saveAssociatedDocumentTest(clmApiProxy);
        } else if (inputNumber == 4) {
            clmApiProxy.deletePricingExhibit("dcb3d304-7373-4128-b2f7-8351af00d910");
        } else if (inputNumber == 5) {
            String parentAgreementId = clmApiProxy.getParentAgreementId(AGREEMENT_ID, AGREEMENT_TYPE);
            logger.info("Parent Id is {}", parentAgreementId);
        } else if (inputNumber == 6) {
            clmApiProxy.updateFurtheranceUrlForPricingContract(AGREEMENT_ID, AGREEMENT_TYPE);
        } else if (inputNumber == 7) {
            saveFurtheranceDocumentTest(clmApiProxy);
        }

    }

    private static void saveAssociatedDocumentTest(ClmApiProxy clmApiProxy) throws URISyntaxException {
        URL systemResource = ClassLoader.getSystemResource("saveAssociatedDocTest.docx");
        File file = new File(systemResource.toURI());
        String savePricingExhibit = clmApiProxy.savePricingExhibit(file, AGREEMENT_ID, null, AGREEMENT_TYPE);

        logger.info("SysID of document:" + savePricingExhibit);
    }

    private static void saveFurtheranceDocumentTest(ClmApiProxy clmApiProxy) throws URISyntaxException {
        URL systemResource = ClassLoader.getSystemResource("saveAssociatedDocTest.docx");
        File file = new File(systemResource.toURI());
        String savePricingExhibit = clmApiProxy.saveFurtheranceExhibit(file, AGREEMENT_ID, null, AGREEMENT_TYPE);

        logger.info("SysID of document:" + savePricingExhibit);
    }

    private static void testGetAgreement(ClmApiProxy clmApiProxy) {
        ClmContractResponseDTO agreementData = clmApiProxy.getAgreementData(AGREEMENT_ID, AGREEMENT_TYPE);

        logger.info(agreementData.toString());
    }

}
