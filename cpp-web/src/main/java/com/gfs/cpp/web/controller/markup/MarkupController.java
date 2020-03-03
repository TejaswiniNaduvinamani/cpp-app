package com.gfs.cpp.web.controller.markup;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gfs.cpp.common.dto.markup.ExceptionMarkupRenameDTO;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.MarkupRequestDTO;
import com.gfs.cpp.common.dto.markup.MarkupWrapperDTO;
import com.gfs.cpp.common.dto.markup.SubgroupMarkupDTO;
import com.gfs.cpp.common.dto.markup.SubgroupResponseDTO;
import com.gfs.cpp.component.markup.MarkupService;
import com.gfs.cpp.component.userdetails.CppUserDetailsService;

@RestController
@RequestMapping(value = "/markup")
public class MarkupController {

    @Autowired
    @Qualifier("markupService")
    MarkupService markupService;

    @Autowired
    CppUserDetailsService gfsUserDetailsService;

    @GetMapping(value = "/fetchMarkups", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<List<MarkupWrapperDTO>> fetchMarkups(@RequestParam int contractPriceProfileSeq, @RequestParam Date pricingExpirationDate,
            @RequestParam Date pricingEffectiveDate) {
        return new ResponseEntity<>(markupService.fetchMarkups(contractPriceProfileSeq, pricingExpirationDate, pricingEffectiveDate), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole(T(com.gfs.cpp.component.userdetails.RoleConstants).POWER_USER, "
            + "T(com.gfs.cpp.component.userdetails.RoleConstants).ACCOUNT_MANAGER )")
    @PostMapping(value = "/saveMarkup", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Void> saveMarkup(@RequestBody MarkupWrapperDTO markupWrapperDTO) {
        String userName = gfsUserDetailsService.getCurrentUserId();
        markupService.saveMarkup(markupWrapperDTO, userName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole(T(com.gfs.cpp.component.userdetails.RoleConstants).POWER_USER, "
            + "T(com.gfs.cpp.component.userdetails.RoleConstants).ACCOUNT_MANAGER )")
    @GetMapping(value = "/addException", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<MarkupWrapperDTO> addException(@RequestParam int contractPriceProfileSeq, @RequestParam String exceptionName,
            @RequestParam Date pricingEffectiveDate, @RequestParam Date pricingExpirationDate) {
        String userName = gfsUserDetailsService.getCurrentUserId();
        return new ResponseEntity<>(
                markupService.addExceptionMarkup(contractPriceProfileSeq, exceptionName, pricingEffectiveDate, pricingExpirationDate, userName),
                HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole(T(com.gfs.cpp.component.userdetails.RoleConstants).POWER_USER, "
            + "T(com.gfs.cpp.component.userdetails.RoleConstants).ACCOUNT_MANAGER )")
    @PostMapping(value = "/saveMarkupIndicators", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Void> saveMarkupIndicators(@RequestBody MarkupRequestDTO markupRequest) {
        String userName = gfsUserDetailsService.getCurrentUserId();
        markupService.saveMarkupIndicators(markupRequest, userName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole(T(com.gfs.cpp.component.userdetails.RoleConstants).POWER_USER, "
            + "T(com.gfs.cpp.component.userdetails.RoleConstants).ACCOUNT_MANAGER )")
    @PutMapping(value = "/renameMarkupException", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Void> renameMarkupException(@RequestBody ExceptionMarkupRenameDTO renameExceptionDTO) {
        markupService.renameExceptionMarkup(renameExceptionDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole(T(com.gfs.cpp.component.userdetails.RoleConstants).POWER_USER, "
            + "T(com.gfs.cpp.component.userdetails.RoleConstants).ACCOUNT_MANAGER )")
    @DeleteMapping(value = "/deleteMarkupException", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Void> deleteException(@RequestParam int contractPriceProfileSeq, @RequestParam String gfsCustomerId,
            @RequestParam String markupName) {
        String userName = gfsUserDetailsService.getCurrentUserId();
        markupService.deleteExceptionMarkup(contractPriceProfileSeq, gfsCustomerId, markupName, userName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole(T(com.gfs.cpp.component.userdetails.RoleConstants).POWER_USER, "
            + "T(com.gfs.cpp.component.userdetails.RoleConstants).ACCOUNT_MANAGER )")
    @GetMapping(value = "/createDefaultItemLevelMarkup", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<ItemLevelMarkupDTO> createDefaultItemLevelMarkup(@RequestParam int contractPriceProfileSeq,
            @RequestParam Date pricingExpirationDate, @RequestParam Date pricingEffectiveDate) {
        return new ResponseEntity<>(markupService.createDefaultItemLevelMarkup(contractPriceProfileSeq, pricingExpirationDate, pricingEffectiveDate),
                HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole(T(com.gfs.cpp.component.userdetails.RoleConstants).POWER_USER, "
            + "T(com.gfs.cpp.component.userdetails.RoleConstants).ACCOUNT_MANAGER )")
    @GetMapping(value = "/createDefaultSubgroupMarkup", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<SubgroupMarkupDTO> createDefaultSubgroupMarkup(@RequestParam Date pricingExpirationDate,
            @RequestParam Date pricingEffectiveDate) {
        return new ResponseEntity<>(markupService.createDefaultSubgroupMarkup(pricingExpirationDate, pricingEffectiveDate), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole(T(com.gfs.cpp.component.userdetails.RoleConstants).POWER_USER, "
            + "T(com.gfs.cpp.component.userdetails.RoleConstants).ACCOUNT_MANAGER )")
    @DeleteMapping(value = "/deleteItemLevelMarkup", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Void> deleteItemLevelMarkup(@RequestParam int contractPriceProfileSeq, @RequestParam String gfsCustomerId,
            @RequestParam int gfsCustomerType, @RequestParam String itemId, @RequestParam String itemDesc) {
        String userName = gfsUserDetailsService.getCurrentUserId();
        markupService.deleteItemLevelMarkup(contractPriceProfileSeq, gfsCustomerId, gfsCustomerType, itemId, itemDesc, userName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/fetchMarkupIndicators", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Map<String, Boolean>> fetchMarkupIndicators(@RequestParam int contractPriceProfileSeq) {
        return new ResponseEntity<>(markupService.fetchMarkupIndicators(contractPriceProfileSeq), HttpStatus.OK);
    }

    @GetMapping(value = "/findItemSubgroupInformation", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<SubgroupResponseDTO> findItemSubgroupInformation(@RequestParam String subgroupId, @RequestParam String gfsCustomerId,
            @RequestParam int gfsCustomerType, @RequestParam int contractPriceProfileSeq) {
        return new ResponseEntity<>(markupService.findItemSubgroupInformation(subgroupId, gfsCustomerId, gfsCustomerType, contractPriceProfileSeq),
                HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole(T(com.gfs.cpp.component.userdetails.RoleConstants).POWER_USER, "
            + "T(com.gfs.cpp.component.userdetails.RoleConstants).ACCOUNT_MANAGER )")
    @DeleteMapping(value = "/deleteSubgroupMarkup", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Void> deleteSubgroupMarkup(@RequestParam int contractPriceProfileSeq, @RequestParam String gfsCustomerId,
            @RequestParam int gfsCustomerType, @RequestParam String subgroupId) {
        String userName = gfsUserDetailsService.getCurrentUserId();
        markupService.deleteSubgroupMarkup(contractPriceProfileSeq, gfsCustomerId, gfsCustomerType, subgroupId, userName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
