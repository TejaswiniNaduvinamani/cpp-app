package com.gfs.cpp.web;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Date;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.gfs.cpp.common.dto.assignments.CustomerAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentWrapperDTO;
import com.gfs.cpp.common.dto.clm.ClmContractStatus;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingDTO;
import com.gfs.cpp.common.dto.distributioncenter.DistributionCenterSaveDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceBaseDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceInformationDTO;
import com.gfs.cpp.common.dto.furtherance.SplitCaseGridFurtheranceDTO;
import com.gfs.cpp.common.dto.markup.ExceptionMarkupRenameDTO;
import com.gfs.cpp.common.dto.markup.MarkupRequestDTO;
import com.gfs.cpp.common.dto.markup.MarkupWrapperDTO;
import com.gfs.cpp.common.dto.markup.PrcProfNonBrktCstMdlDTO;
import com.gfs.cpp.common.dto.review.SavePricingExhibitDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseGridDTO;
import com.gfs.cpp.web.controller.activatepricing.ActivatePricingController;
import com.gfs.cpp.web.controller.assignments.AssignmentController;
import com.gfs.cpp.web.controller.contractpricing.ContractPricingController;
import com.gfs.cpp.web.controller.costmodel.CostModelController;
import com.gfs.cpp.web.controller.distributioncenter.DistributionCenterController;
import com.gfs.cpp.web.controller.furtherance.FurtheranceController;
import com.gfs.cpp.web.controller.markup.MarkupController;
import com.gfs.cpp.web.controller.review.ReviewController;
import com.gfs.cpp.web.controller.splitcase.SplitCaseController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppTestConfig.class, ProxyTestConfig.class })
@EnableWebMvc
@WebAppConfiguration
public class PreAuthorizeControllerTest {

    @Autowired
    private ContractPricingController contractPricingController;
    @Autowired
    private DistributionCenterController distributionCenterController;
    @Autowired
    private MarkupController markupController;
    @Autowired
    private SplitCaseController splitCaseController;
    @Autowired
    private ReviewController reviewController;
    @Autowired
    private AssignmentController assignmentController;
    @Autowired
    private ActivatePricingController activatePricingController;
    @Autowired
    private FurtheranceController furtheranceController;
    @Autowired
    private CostModelController costModelController;

    private static final String REAL_CUSTOMER_ID = "9999";
    private static final int REAL_CUSTOMER_TYPE = 10;
    private static final int CONTRACT_PRICE_PROFILE_SEQ = 999;
    private static final int CPP_FURTHERANCE_SEQ = 999;
    private static final String CMG_CUSTOMER_ID = "1001";
    private static final int CMG_CUSTOMER_TYPE = 15;
    private static final String ITEM_ID = "100";
    private static final String SUBGROUP_ID = "10";
    private static final String ITEM_DESC = "chicken";
    private static final String MARKUP_NAME = "test";
    private static final String AGREEMENT_ID = "AgreementId";
    private static final Date pricingEffectiveDate = new LocalDate(2018, 01, 01).toDate();
    private static final Date pricingExpirationDate = new LocalDate(9999, 01, 01).toDate();

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "VIEWUSER")
    public void shouldDenySaveContractPricingForViewUser() {
        contractPricingController.saveContractPricing(new ContractPricingDTO());
    }

    @Test
    @WithMockUser(roles = "POWERUSER")
    public void shouldGrantSaveContractPricingForPowerUser() {
        try {
            contractPricingController.saveContractPricing(new ContractPricingDTO());
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test
    @WithMockUser(roles = "ACCOUNTMGR")
    public void shouldGrantSaveContractPricingForAccountManager() {
        try {
            contractPricingController.saveContractPricing(new ContractPricingDTO());
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "VIEWUSER")
    public void shouldDenySaveDistributionCentersForViewUser() {
        distributionCenterController.saveDistributionCenters(new DistributionCenterSaveDTO());
    }

    @Test
    @WithMockUser(roles = "POWERUSER")
    public void shouldGrantSaveDistributionCentersForPowerUser() {
        try {
            distributionCenterController.saveDistributionCenters(new DistributionCenterSaveDTO());
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test
    @WithMockUser(roles = "ACCOUNTMGR")
    public void shouldGrantSaveDistributionCentersForAccountManager() {
        try {
            distributionCenterController.saveDistributionCenters(new DistributionCenterSaveDTO());
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "VIEWUSER")
    public void shouldDenySaveMarkupForViewUser() {
        markupController.saveMarkup(new MarkupWrapperDTO());
    }

    @Test
    @WithMockUser(roles = "POWERUSER")
    public void shouldGrantSaveMarkupForPowerUser() {
        try {
            markupController.saveMarkup(new MarkupWrapperDTO());
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test
    @WithMockUser(roles = "ACCOUNTMGR")
    public void shouldGrantSaveMarkupForAccountManager() {
        try {
            markupController.saveMarkup(new MarkupWrapperDTO());
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "VIEWUSER")
    public void shouldDenyAddExceptionForViewUser() {
        markupController.addException(CONTRACT_PRICE_PROFILE_SEQ, MARKUP_NAME, pricingEffectiveDate, pricingExpirationDate);
    }

    @Test
    @WithMockUser(roles = "POWERUSER")
    public void shouldGrantAddExceptionForPowerUser() {
        try {
            markupController.addException(CONTRACT_PRICE_PROFILE_SEQ, MARKUP_NAME, pricingEffectiveDate, pricingExpirationDate);
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test
    @WithMockUser(roles = "ACCOUNTMGR")
    public void shouldGrantAddExceptionForAccountManager() {
        try {
            markupController.addException(CONTRACT_PRICE_PROFILE_SEQ, MARKUP_NAME, pricingEffectiveDate, pricingExpirationDate);
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "VIEWUSER")
    public void shouldDenySaveMarkupIndicatorsForViewUser() {
        markupController.saveMarkupIndicators(new MarkupRequestDTO());
    }

    @Test
    @WithMockUser(roles = "POWERUSER")
    public void shouldGrantSaveMarkupIndicatorsForPowerUser() {
        try {
            markupController.saveMarkupIndicators(new MarkupRequestDTO());
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test
    @WithMockUser(roles = "ACCOUNTMGR")
    public void shouldGrantSaveMarkupIndicatorsForAccountManager() {
        try {
            markupController.saveMarkupIndicators(new MarkupRequestDTO());
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "VIEWUSER")
    public void shouldDenyRenameMarkupExceptionForViewUser() {
        markupController.renameMarkupException(new ExceptionMarkupRenameDTO());
    }

    @Test
    @WithMockUser(roles = "POWERUSER")
    public void shouldGrantRenameMarkupExceptionForPowerUser() {
        try {
            markupController.renameMarkupException(new ExceptionMarkupRenameDTO());
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test
    @WithMockUser(roles = "ACCOUNTMGR")
    public void shouldGrantRenameMarkupExceptionForAccountManager() {
        try {
            markupController.renameMarkupException(new ExceptionMarkupRenameDTO());
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "VIEWUSER")
    public void shouldDenyDeleteExceptionForViewUser() {
        markupController.deleteException(CONTRACT_PRICE_PROFILE_SEQ, CMG_CUSTOMER_ID, MARKUP_NAME);
    }

    @Test
    @WithMockUser(roles = "POWERUSER")
    public void shouldGrantDeleteExceptionForPowerUser() {
        try {
            markupController.deleteException(CONTRACT_PRICE_PROFILE_SEQ, CMG_CUSTOMER_ID, MARKUP_NAME);
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test
    @WithMockUser(roles = "ACCOUNTMGR")
    public void shouldGrantDeleteExceptionForAccountManager() {
        try {
            markupController.deleteException(CONTRACT_PRICE_PROFILE_SEQ, CMG_CUSTOMER_ID, MARKUP_NAME);
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "VIEWUSER")
    public void shouldDenyCreateDefaultItemLevelMarkupForViewUser() {
        markupController.createDefaultItemLevelMarkup(CONTRACT_PRICE_PROFILE_SEQ, pricingExpirationDate, pricingEffectiveDate);
    }

    @Test
    @WithMockUser(roles = "POWERUSER")
    public void shouldGrantCreateDefaultItemLevelMarkupForPowerUser() {
        try {
            markupController.createDefaultItemLevelMarkup(CONTRACT_PRICE_PROFILE_SEQ, pricingExpirationDate, pricingEffectiveDate);
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test
    @WithMockUser(roles = "ACCOUNTMGR")
    public void shouldGrantCreateDefaultItemLevelMarkupForAccountManager() {
        try {
            markupController.createDefaultItemLevelMarkup(CONTRACT_PRICE_PROFILE_SEQ, pricingExpirationDate, pricingEffectiveDate);
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "VIEWUSER")
    public void shouldDenyDeleteItemLevelMarkupForViewUser() {
        markupController.deleteItemLevelMarkup(CONTRACT_PRICE_PROFILE_SEQ, CMG_CUSTOMER_ID, CMG_CUSTOMER_TYPE, ITEM_ID, ITEM_DESC);
    }

    @Test
    @WithMockUser(roles = "POWERUSER")
    public void shouldGrantDeleteItemLevelMarkupForPowerUser() {
        try {
            markupController.deleteItemLevelMarkup(CONTRACT_PRICE_PROFILE_SEQ, CMG_CUSTOMER_ID, CMG_CUSTOMER_TYPE, ITEM_ID, ITEM_DESC);
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "VIEWUSER")
    public void shouldDenyAddingSubgroupForViewUser() {
        markupController.createDefaultSubgroupMarkup(pricingExpirationDate, pricingEffectiveDate);
    }

    @Test
    @WithMockUser(roles = "POWERUSER")
    public void shouldGrantToAddSubgroupForPowerUser() {
        try {
            markupController.createDefaultSubgroupMarkup(pricingExpirationDate, pricingEffectiveDate);
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test
    @WithMockUser(roles = "ACCOUNTMGR")
    public void shouldGrantToAddSubgroupForAccountManager() {
        try {
            markupController.createDefaultSubgroupMarkup(pricingExpirationDate, pricingEffectiveDate);
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test
    @WithMockUser(roles = "ACCOUNTMGR")
    public void shouldGrantDeleteItemLevelMarkupForAccountManager() {
        try {
            markupController.deleteItemLevelMarkup(CONTRACT_PRICE_PROFILE_SEQ, CMG_CUSTOMER_ID, CMG_CUSTOMER_TYPE, ITEM_ID, ITEM_DESC);
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test
    @WithMockUser(roles = "ACCOUNTMGR")
    public void shouldGrantDeleteSubgroupMarkupForAccountManager() {
        try {
            markupController.deleteSubgroupMarkup(CONTRACT_PRICE_PROFILE_SEQ, CMG_CUSTOMER_ID, CMG_CUSTOMER_TYPE, SUBGROUP_ID);
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "VIEWUSER")
    public void shouldDenyDeleteSubgroupMarkupForViewUser() {
        markupController.deleteSubgroupMarkup(CONTRACT_PRICE_PROFILE_SEQ, CMG_CUSTOMER_ID, CMG_CUSTOMER_TYPE, SUBGROUP_ID);
    }

    @Test
    @WithMockUser(roles = "POWERUSER")
    public void shouldGrantDeleteSubgroupMarkupForPowerUser() {
        try {
            markupController.deleteSubgroupMarkup(CONTRACT_PRICE_PROFILE_SEQ, CMG_CUSTOMER_ID, CMG_CUSTOMER_TYPE, SUBGROUP_ID);
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "VIEWUSER")
    public void shouldDenyCreateExhibitDocumentForViewUser() {
        reviewController.createExhibitDocument(CONTRACT_PRICE_PROFILE_SEQ, null);
    }

    @Test
    @WithMockUser(roles = "POWERUSER")
    public void shouldGrantCreateExhibitDocumentForPowerUser() {
        try {
            reviewController.createExhibitDocument(CONTRACT_PRICE_PROFILE_SEQ, null);
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test
    @WithMockUser(roles = "ACCOUNTMGR")
    public void shouldGrantCreateExhibitDocumentForAccountManager() {
        try {
            reviewController.createExhibitDocument(CONTRACT_PRICE_PROFILE_SEQ, null);
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "VIEWUSER")
    public void shouldDenySavePringExhibitForViewUser() {
        reviewController.savePricingExhibit(new SavePricingExhibitDTO());
    }

    @Test
    @WithMockUser(roles = "POWERUSER")
    public void shouldGrantSavePricingExhibitForPowerUser() {
        try {
            reviewController.savePricingExhibit(new SavePricingExhibitDTO());
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test
    @WithMockUser(roles = "ACCOUNTMGR")
    public void shouldGrantSavePricingExhibitForAccountManager() {
        try {
            reviewController.savePricingExhibit(new SavePricingExhibitDTO());
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "VIEWUSER")
    public void shouldDenySaveSplitCaseForViewUser() {
        splitCaseController.saveSplitCase(new SplitCaseGridDTO());
    }

    @Test
    @WithMockUser(roles = "POWERUSER")
    public void shouldGrantSaveSplitCaseForPowerUser() {
        try {
            splitCaseController.saveSplitCase(new SplitCaseGridDTO());
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test
    @WithMockUser(roles = "ACCOUNTMGR")
    public void shouldGrantSaveSplitCaseForAccountManager() {
        try {
            splitCaseController.saveSplitCase(new SplitCaseGridDTO());
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "VIEWUSER")
    public void shouldDenySaveAssignmentsForViewUser() {
        assignmentController.saveAssignments(new CustomerAssignmentDTO());
    }

    @Test
    @WithMockUser(roles = "POWERUSER")
    public void shouldGrantSaveAssignmentsForPowerUser() {
        try {
            assignmentController.saveAssignments(new CustomerAssignmentDTO());
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "ACCOUNTMGR")
    public void shouldDenySaveAssignmentsForAccountManager() {
        assignmentController.saveAssignments(new CustomerAssignmentDTO());
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "VIEWUSER")
    public void shouldDenyDeleteCustomerAssignmentForViewUser() {
        assignmentController.deleteCustomerAssignment(REAL_CUSTOMER_ID, REAL_CUSTOMER_TYPE, CONTRACT_PRICE_PROFILE_SEQ, CMG_CUSTOMER_ID,
                CMG_CUSTOMER_TYPE);
    }

    @Test
    @WithMockUser(roles = "POWERUSER")
    public void shouldGrantDeleteCustomerAssignmentForPowerUser() {
        try {
            assignmentController.deleteCustomerAssignment(REAL_CUSTOMER_ID, REAL_CUSTOMER_TYPE, CONTRACT_PRICE_PROFILE_SEQ, CMG_CUSTOMER_ID,
                    CMG_CUSTOMER_TYPE);
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "ACCOUNTMGR")
    public void shouldDenyDeleteCustomerAssignmentForAccountManager() {
        assignmentController.deleteCustomerAssignment(REAL_CUSTOMER_ID, REAL_CUSTOMER_TYPE, CONTRACT_PRICE_PROFILE_SEQ, CMG_CUSTOMER_ID,
                CMG_CUSTOMER_TYPE);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "VIEWUSER")
    public void shouldDenyAssignItemsForViewUser() {
        assignmentController.assignItems(new ItemAssignmentWrapperDTO());
    }

    @Test
    @WithMockUser(roles = "POWERUSER")
    public void shouldGrantAssignItemsForPowerUser() {
        try {
            assignmentController.assignItems(new ItemAssignmentWrapperDTO());
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "ACCOUNTMGR")
    public void shouldDenyAssignItemsForAccountManager() {
        assignmentController.assignItems(new ItemAssignmentWrapperDTO());
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "VIEWUSER")
    public void shouldDenyDeleteItemAssignmentForViewUser() {
        assignmentController.deleteItemAssignment(CONTRACT_PRICE_PROFILE_SEQ, CMG_CUSTOMER_ID, CMG_CUSTOMER_TYPE, ITEM_ID, ITEM_DESC);
    }

    @Test
    @WithMockUser(roles = "POWERUSER")
    public void shouldGrantDeleteItemAssignmentForPowerUser() {
        try {
            assignmentController.deleteItemAssignment(CONTRACT_PRICE_PROFILE_SEQ, CMG_CUSTOMER_ID, CMG_CUSTOMER_TYPE, ITEM_ID, ITEM_DESC);
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "ACCOUNTMGR")
    public void shouldDenyDeleteItemAssignmentForAccountManager() {
        assignmentController.deleteItemAssignment(CONTRACT_PRICE_PROFILE_SEQ, CMG_CUSTOMER_ID, CMG_CUSTOMER_TYPE, ITEM_ID, ITEM_DESC);
    }

    @Test
    @WithMockUser(roles = "POWERUSER")
    public void shouldGrantDeletePricingExhibitForPowerUser() {
        try {
            contractPricingController.deletePricingExhibit(AGREEMENT_ID);
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test
    @WithMockUser(roles = "ACCOUNTMGR")
    public void shouldGrantDeletePricingExhibitForAccountManager() {
        try {
            contractPricingController.deletePricingExhibit(AGREEMENT_ID);
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "ViewUser")
    public void shouldDenyDeletePricingExhibitForViewUser() {
        contractPricingController.deletePricingExhibit(AGREEMENT_ID);
    }

    @Test
    @WithMockUser(roles = "POWERUSER")
    public void shouldEnableActivatePricingForPowerUser() {
        try {
            activatePricingController.enableActivatePricing(CONTRACT_PRICE_PROFILE_SEQ);
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "ACCOUNTMGR")
    public void shouldEnableActivatePricingForAccountManager() {
        activatePricingController.enableActivatePricing(CONTRACT_PRICE_PROFILE_SEQ);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "ViewUser")
    public void shouldEnableActivatePricingForViewUser() {
        activatePricingController.enableActivatePricing(CONTRACT_PRICE_PROFILE_SEQ);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "VIEWUSER")
    public void shouldDenyActivatePricingForAmendmentForViewUser() {
        activatePricingController.activatePricing(CONTRACT_PRICE_PROFILE_SEQ, true, ClmContractStatus.EXECUTED.value);
    }

    @Test
    @WithMockUser(roles = "POWERUSER")
    public void shouldGrantActivatePricingForAmendmentForPowerUser() {
        try {
            activatePricingController.activatePricing(CONTRACT_PRICE_PROFILE_SEQ, true, ClmContractStatus.EXECUTED.value);
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "ACCOUNTMGR")
    public void shouldDenyActivatePricingForAmendmentForAccountManager() {
        activatePricingController.activatePricing(CONTRACT_PRICE_PROFILE_SEQ, true, ClmContractStatus.EXECUTED.value);
    }

    @Test
    @WithMockUser(roles = "POWERUSER")
    public void shouldGrantDeleteItemForPowerUser() {
        try {
            furtheranceController.deleteItemLevelMarkup(CONTRACT_PRICE_PROFILE_SEQ, CPP_FURTHERANCE_SEQ, CMG_CUSTOMER_ID, CMG_CUSTOMER_TYPE, ITEM_ID,
                    ITEM_DESC);
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test
    @WithMockUser(roles = "ACCOUNTMGR")
    public void shouldGrantDeleteItemForAccountManager() {
        try {
            furtheranceController.deleteItemLevelMarkup(CONTRACT_PRICE_PROFILE_SEQ, CPP_FURTHERANCE_SEQ, CMG_CUSTOMER_ID, CMG_CUSTOMER_TYPE, ITEM_ID,
                    ITEM_DESC);
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "VIEWUSER")
    public void shouldDenySaveFurtheranceInformationForViewUser() {
        furtheranceController.saveFurtheranceInformation(new FurtheranceInformationDTO());
    }

    @Test
    @WithMockUser(roles = "POWERUSER")
    public void shouldGrantSaveFurtheranceInformationForPowerUser() {
        try {
            furtheranceController.saveFurtheranceInformation(new FurtheranceInformationDTO());
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test
    @WithMockUser(roles = "ACCOUNTMGR")
    public void shouldGrantSaveFurtheranceInformationForAccountManager() {
        try {
            furtheranceController.saveFurtheranceInformation(new FurtheranceInformationDTO());
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "VIEWUSER")
    public void shouldDenySaveSplitCaseForViewUserForFurtherance() {
        furtheranceController.saveSplitCaseFeeForFurtherance(new SplitCaseGridFurtheranceDTO());
    }

    @Test
    @WithMockUser(roles = "POWERUSER")
    public void shouldGrantSaveSplitCaseForPowerUserForFurtherance() {
        try {
            furtheranceController.saveSplitCaseFeeForFurtherance(new SplitCaseGridFurtheranceDTO());
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "VIEWUSER")
    public void shouldDenyCreateFurtheranceDocumentForViewUser() {
        furtheranceController.createFurtheranceDocument(CPP_FURTHERANCE_SEQ, null);
    }

    @Test
    @WithMockUser(roles = "ACCOUNTMGR")
    public void shouldGrantCreateFurtheranceDocumentForAccountUser() {
        try {
            furtheranceController.createFurtheranceDocument(CPP_FURTHERANCE_SEQ, null);
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test
    @WithMockUser(roles = "ACCOUNTMGR")
    public void shouldGrantSaveSplitCaseForAccountManagerForFurtherance() {
        try {
            furtheranceController.saveSplitCaseFeeForFurtherance(new SplitCaseGridFurtheranceDTO());
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test
    @WithMockUser(roles = "POWERUSER")
    public void shouldGrantCreateFurtheranceDocumentForPowerUser() {
        try {
            furtheranceController.createFurtheranceDocument(CPP_FURTHERANCE_SEQ, null);
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "VIEWUSER")
    public void shouldDenyShouldPricingDocumentForFurtheranceForViewUser() {
        furtheranceController.savePricingDocumentForFurtherance(new FurtheranceBaseDTO());
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "VIEWUSER")
    public void shouldDenyDeleteMappedItemForFurtheranceForViewUser() {
        furtheranceController.deleteMappedItemForFurtherance(CONTRACT_PRICE_PROFILE_SEQ, CPP_FURTHERANCE_SEQ, CMG_CUSTOMER_ID, CMG_CUSTOMER_TYPE,
                ITEM_ID, ITEM_DESC);
    }

    @Test
    @WithMockUser(roles = "POWERUSER")
    public void shouldGrantDeleteMappedItemForFurtheranceForPowerUser() {
        try {
            furtheranceController.deleteMappedItemForFurtherance(CONTRACT_PRICE_PROFILE_SEQ, CPP_FURTHERANCE_SEQ, CMG_CUSTOMER_ID, CMG_CUSTOMER_TYPE,
                    ITEM_ID, ITEM_DESC);
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test
    @WithMockUser(roles = "ACCOUNTMGR")
    public void shouldGrantDeleteMappedItemForFurtheranceForAccountManager() {
        try {
            furtheranceController.deleteMappedItemForFurtherance(CONTRACT_PRICE_PROFILE_SEQ, CPP_FURTHERANCE_SEQ, CMG_CUSTOMER_ID, CMG_CUSTOMER_TYPE,
                    ITEM_ID, ITEM_DESC);
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test
    @WithMockUser(roles = "ACCOUNTMGR")
    public void shouldGrantShouldPricingDocumentForFurtheranceForAccountUser() {
        try {
            furtheranceController.savePricingDocumentForFurtherance(new FurtheranceBaseDTO());
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test
    @WithMockUser(roles = "POWERUSER")
    public void shouldGrantShouldPricingDocumentForFurtheranceForPowerUser() {
        try {
            furtheranceController.savePricingDocumentForFurtherance(new FurtheranceBaseDTO());
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test
    @WithMockUser(roles = "POWERUSER")
    public void shouldGrantAssignItemsForFurtheranceForPowerUser() {
        try {
            furtheranceController.assignItemsForFurtherance(new ItemAssignmentWrapperDTO());
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "VIEWUSER")
    public void shouldDenyAssignItemsForFurtheranceForViewUser() {
        furtheranceController.assignItemsForFurtherance(new ItemAssignmentWrapperDTO());
    }

    @Test
    @WithMockUser(roles = "ACCOUNTMGR")
    public void shouldGrantAssignItemsForFurtheranceForAccountManager() {
        try {
            furtheranceController.assignItemsForFurtherance(new ItemAssignmentWrapperDTO());
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "VIEWUSER")
    public void shouldDenySaveMarkupForFurtheranceForViewUser() {
        furtheranceController.saveMarkupForFurtherance(new MarkupWrapperDTO());
    }

    @Test
    @WithMockUser(roles = "POWERUSER")
    public void shouldGrantSaveMarkupForFurtheranceForPowerUser() {
        try {
            furtheranceController.saveMarkupForFurtherance(new MarkupWrapperDTO());
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test
    @WithMockUser(roles = "ACCOUNTMGR")
    public void shouldGrantSaveMarkupForFurtheranceForAccountManager() {
        try {
            furtheranceController.saveMarkupForFurtherance(new MarkupWrapperDTO());
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "VIEWUSER")
    public void shouldDenySaveUpdatedCostModelForViewUser() {
        costModelController.saveUpdatedCostModel(new ArrayList<PrcProfNonBrktCstMdlDTO>());
    }

    @Test
    @WithMockUser(roles = "POWERUSER")
    public void shouldGrantSaveUpdatedCostModelForPowerUser() {
        try {
            costModelController.saveUpdatedCostModel(new ArrayList<PrcProfNonBrktCstMdlDTO>());
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }

    @Test
    @WithMockUser(roles = "ACCOUNTMGR")
    public void shouldGrantSaveUpdatedCostModelForAccountManager() {
        try {
            costModelController.saveUpdatedCostModel(new ArrayList<PrcProfNonBrktCstMdlDTO>());
        } catch (final Exception e) {
            assertThat(e, not(instanceOf(AccessDeniedException.class)));
        }
    }
}
