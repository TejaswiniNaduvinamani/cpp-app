package com.gfs.cpp.web.controller.furtherance;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentWrapperDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceBaseDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceInformationDTO;
import com.gfs.cpp.common.dto.furtherance.SplitCaseGridFurtheranceDTO;
import com.gfs.cpp.common.dto.markup.MarkupWrapperDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.util.StatusDTO;
import com.gfs.cpp.component.furtherance.FurtheranceService;
import com.gfs.cpp.component.userdetails.CppUserDetailsService;
import com.gfs.cpp.proxy.clm.FileRemover;

@Controller
@RequestMapping(value = "/furtherance")
public class FurtheranceController {

    @Autowired
    private FurtheranceService furtheranceService;

    @Autowired
    private CppUserDetailsService gfsUserDetailsService;

    @Autowired
    private FileRemover fileRemover;

    public static final Logger logger = LoggerFactory.getLogger(FurtheranceController.class);

    @GetMapping(value = "/hasInProgressFurtherance", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Map<String, Boolean>> hasInProgressFurtherance(@RequestParam String parentAgreementId) {

        Map<String, Boolean> response = new HashMap<>();

        response.put("hasInProgressFurtherance", furtheranceService.hasInProgressFurtherance(parentAgreementId));

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping(value = "/fetchInProgressFurtheranceInfo", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<FurtheranceBaseDTO> fetchInProgressFurtheranceInfo(@RequestParam String parentAgreementId) {

        return new ResponseEntity<>(furtheranceService.fetchInProgressFurtheranceInfo(parentAgreementId), HttpStatus.OK);
    }

    @GetMapping(value = "/fetchFurtheranceInformation", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<FurtheranceInformationDTO> fetchFurtheranceInformation(@RequestParam String parentAgreementId,
            @RequestParam int cppFurtheranceSeq) {

        return new ResponseEntity<>(furtheranceService.fetchFurtheranceInformation(parentAgreementId, cppFurtheranceSeq), HttpStatus.OK);
    }

    @GetMapping(value = "/createNewFurtherance", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<FurtheranceBaseDTO> createNewFurtherance(@RequestParam String parentAgreementId, @RequestParam String contractType) {

        return new ResponseEntity<>(furtheranceService.createNewFurtherance(parentAgreementId, contractType), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole(T(com.gfs.cpp.component.userdetails.RoleConstants).POWER_USER, "
            + "T(com.gfs.cpp.component.userdetails.RoleConstants).ACCOUNT_MANAGER )")
    @PostMapping(value = "/saveFurtheranceInformation", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Void> saveFurtheranceInformation(@RequestBody FurtheranceInformationDTO furtheranceInformationDTO) {

        String userName = gfsUserDetailsService.getCurrentUserId();
        furtheranceService.saveFurtheranceInformation(furtheranceInformationDTO, userName);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole(T(com.gfs.cpp.component.userdetails.RoleConstants).POWER_USER, "
            + "T(com.gfs.cpp.component.userdetails.RoleConstants).ACCOUNT_MANAGER )")
    @PostMapping(value = "/saveMarkupForFurtherance", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Void> saveMarkupForFurtherance(@RequestBody MarkupWrapperDTO markupWrapperDTO) {
        String userName = gfsUserDetailsService.getCurrentUserId();
        furtheranceService.saveMarkupForFurtherance(markupWrapperDTO, userName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole(T(com.gfs.cpp.component.userdetails.RoleConstants).POWER_USER)")
    @GetMapping(value = "/activatePricingForFurtherance", params = { "cppFurtheranceSeq", "clmContractStatus" }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Map<String, String>> activatePricingForFurtherance(@RequestParam int cppFurtheranceSeq,
            @RequestParam String clmContractStatus) {
        Map<String, String> activatePricingResponseMap = new HashMap<>();
        String userId = gfsUserDetailsService.getCurrentUserId();
        furtheranceService.activatePricingForFurtherance(cppFurtheranceSeq, userId, clmContractStatus);
        activatePricingResponseMap.put(CPPConstants.MESSAGE, "Pricing activated for the contract");
        return new ResponseEntity<>(activatePricingResponseMap, HttpStatus.OK);
    }

    @GetMapping(value = "/fetchMappedItemsForFurtherance", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<ItemAssignmentWrapperDTO> fetchMappedItemsForFurtherance(@RequestParam int contractPriceProfileSeq,
            @RequestParam String gfsCustomerId, @RequestParam int gfsCustomerTypeCode, @RequestParam String itemDesc) {
        return new ResponseEntity<>(
                furtheranceService.fetchMappedItemsForFurtherance(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTypeCode, itemDesc),
                HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole(T(com.gfs.cpp.component.userdetails.RoleConstants).POWER_USER, "
            + "T(com.gfs.cpp.component.userdetails.RoleConstants).ACCOUNT_MANAGER )")
    @DeleteMapping(value = "/deleteItemLevelMarkup", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Void> deleteItemLevelMarkup(@RequestParam int contractPriceProfileSeq, @RequestParam int cppFurtheranceSeq,
            @RequestParam String gfsCustomerId, @RequestParam int gfsCustomerTypeCode, @RequestParam String itemId, @RequestParam String itemDesc) {
        String userName = gfsUserDetailsService.getCurrentUserId();
        furtheranceService.deleteItemLevelMarkup(contractPriceProfileSeq, cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, itemId, itemDesc,
                userName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole(T(com.gfs.cpp.component.userdetails.RoleConstants).POWER_USER, "
            + "T(com.gfs.cpp.component.userdetails.RoleConstants).ACCOUNT_MANAGER )")
    @PostMapping(value = "/saveSplitCaseFee", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Void> saveSplitCaseFeeForFurtherance(@RequestBody SplitCaseGridFurtheranceDTO splitCaseGridFurtheranceDTO) {
        String userName = gfsUserDetailsService.getCurrentUserId();
        furtheranceService.saveSplitCaseFeeForFurtherance(splitCaseGridFurtheranceDTO, userName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole(T(com.gfs.cpp.component.userdetails.RoleConstants).POWER_USER, "
            + "T(com.gfs.cpp.component.userdetails.RoleConstants).ACCOUNT_MANAGER )")
    @GetMapping(value = "/createFurtheranceDocument")
    public void createFurtheranceDocument(@RequestParam int cppFurtheranceSeq, HttpServletResponse response) {
        File file = furtheranceService.createFurtheranceDocument(cppFurtheranceSeq);
        if (file != null) {
            try (FileInputStream fileInput = new FileInputStream(file)) {
                response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                response.setHeader("Content-Disposition", String.format("inline; filename=%s", file.getName()));
                InputStream inputStream = new BufferedInputStream(fileInput);
                FileCopyUtils.copy(inputStream, response.getOutputStream());
                response.flushBuffer();
            } catch (Exception e) {
                logger.error("Service call to create furtherance docx failed for furtherance sequence {}", cppFurtheranceSeq, e);
                throw new CPPRuntimeException(CPPExceptionType.SERVICE_FAILED, "IO exception occured when processing the furtherance document");
            } finally {
                boolean isDeleted = fileRemover.deleteFile(file);
                logger.info("Has file {} deleted : {}", file.getName(), isDeleted);

            }
        }
    }

    @PreAuthorize("hasAnyRole(T(com.gfs.cpp.component.userdetails.RoleConstants).POWER_USER, "
            + "T(com.gfs.cpp.component.userdetails.RoleConstants).ACCOUNT_MANAGER )")
    @PostMapping(value = "/savePricingDocumentForFurtherance", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Void> savePricingDocumentForFurtherance(@RequestBody FurtheranceBaseDTO furtheranceBaseDTO) {
        String userName = gfsUserDetailsService.getCurrentUserId();
        furtheranceService.savePricingDocumentForFurtherance(furtheranceBaseDTO, userName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/enableActivatePricingForFurtherance", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Map<String, Boolean>> enableActivatePricingForFurtherance(@RequestParam int cppFurtheranceSeq,
            @RequestParam String clmContractStatus) {
        return new ResponseEntity<>(furtheranceService.validateActivatePricingForFurtherance(cppFurtheranceSeq, clmContractStatus), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole(T(com.gfs.cpp.component.userdetails.RoleConstants).POWER_USER, "
            + "T(com.gfs.cpp.component.userdetails.RoleConstants).ACCOUNT_MANAGER )")
    @DeleteMapping(value = "/deleteMappedItemForFurtherance", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Void> deleteMappedItemForFurtherance(@RequestParam int contractPriceProfileSeq, @RequestParam int cppFurtheranceSeq,
            @RequestParam String gfsCustomerId, @RequestParam int gfsCustomerTypeCode, @RequestParam String itemId, @RequestParam String itemDesc) {

        String userName = gfsUserDetailsService.getCurrentUserId();
        furtheranceService.deleteMappedItemForFurtherance(contractPriceProfileSeq, cppFurtheranceSeq, gfsCustomerId, gfsCustomerTypeCode, itemId,
                itemDesc, userName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole(T(com.gfs.cpp.component.userdetails.RoleConstants).POWER_USER, "
            + "T(com.gfs.cpp.component.userdetails.RoleConstants).ACCOUNT_MANAGER )")
    @PostMapping(value = "/assignItemsForFurtherance", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
            MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<StatusDTO> assignItemsForFurtherance(@RequestBody ItemAssignmentWrapperDTO itemAssignmentWrapperDTO) {

        String userName = gfsUserDetailsService.getCurrentUserId();

        return new ResponseEntity<>(furtheranceService.assignItemsForFurtherance(itemAssignmentWrapperDTO, userName), HttpStatus.OK);
    }
}
