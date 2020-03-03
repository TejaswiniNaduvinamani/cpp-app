package com.gfs.cpp.web.controller.assignments;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gfs.cpp.common.dto.assignments.CMGCustomerTypeDTO;
import com.gfs.cpp.common.dto.assignments.CustomerAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.ItemAssignmentWrapperDTO;
import com.gfs.cpp.common.dto.assignments.MarkupAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.RealCustomerResponseDTO;
import com.gfs.cpp.common.util.StatusDTO;
import com.gfs.cpp.component.assignment.AssignmentService;
import com.gfs.cpp.component.userdetails.CppUserDetailsService;

@RestController
@RequestMapping(value = "/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private CppUserDetailsService gfsUserDetailsService;

    public static final Logger logger = LoggerFactory.getLogger(AssignmentController.class);

    @GetMapping(value = "/fetchMarkup", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<List<MarkupAssignmentDTO>> fetchMarkup(@RequestParam int contractPriceProfileSeq) {
        return new ResponseEntity<>(assignmentService.fetchMarkupAssignment(contractPriceProfileSeq), HttpStatus.OK);
    }

    @GetMapping(value = "/fetchCustomerTypes", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<List<CMGCustomerTypeDTO>> fetchCustomerTypes() {
        return new ResponseEntity<>(assignmentService.fetchCustomerTypes(), HttpStatus.OK);
    }

    @GetMapping(value = "/findCustomer", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<RealCustomerResponseDTO> findCustomer(@RequestParam String gfsCustomerId, @RequestParam int gfsCustomerType,
            @RequestParam int contractPriceProfileSeq, @RequestParam String cmgCustomerId, @RequestParam int cmgCustomerType) {
        return new ResponseEntity<>(
                assignmentService.findCustomerName(gfsCustomerId, gfsCustomerType, contractPriceProfileSeq, cmgCustomerId, cmgCustomerType),
                HttpStatus.OK);
    }

    @PreAuthorize("hasRole(T(com.gfs.cpp.component.userdetails.RoleConstants).POWER_USER)")
    @PostMapping(value = "/saveAssignments", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<StatusDTO> saveAssignments(@RequestBody CustomerAssignmentDTO saveAssignmentDTO) {
        String userName = gfsUserDetailsService.getCurrentUserId();
        return new ResponseEntity<>(assignmentService.saveAssignments(saveAssignmentDTO, userName), HttpStatus.OK);
    }

    @GetMapping(value = "/fetchAllFutureItems", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<List<ItemAssignmentWrapperDTO>> fetchAllFutureItems(@RequestParam int contractPriceProfileSeq) {
        return new ResponseEntity<>(assignmentService.fetchAllFutureItems(contractPriceProfileSeq), HttpStatus.OK);
    }

    @PreAuthorize("hasRole(T(com.gfs.cpp.component.userdetails.RoleConstants).POWER_USER)")
    @DeleteMapping(value = "/deleteCustomerAssignment", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Void> deleteCustomerAssignment(@RequestParam String realCustomerId, @RequestParam int realCustomerType,
            @RequestParam int contractPriceProfileSeq, @RequestParam String cmgCustomerId, @RequestParam int cmgCustomerType) {
        assignmentService.deleteCustomerAssignment(realCustomerId, realCustomerType, contractPriceProfileSeq, cmgCustomerId, cmgCustomerType);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole(T(com.gfs.cpp.component.userdetails.RoleConstants).POWER_USER)")
    @PostMapping(value = "/assignItems", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<StatusDTO> assignItems(@RequestBody ItemAssignmentWrapperDTO itemAssignmentWrapperDTO) {
        String userName = gfsUserDetailsService.getCurrentUserId();
        return new ResponseEntity<>(assignmentService.assignItems(itemAssignmentWrapperDTO, userName), HttpStatus.OK);
    }

    @PreAuthorize("hasRole(T(com.gfs.cpp.component.userdetails.RoleConstants).POWER_USER)")
    @DeleteMapping(value = "/deleteItemAssignment", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Void> deleteItemAssignment(@RequestParam int contractPriceProfileSeq, @RequestParam String gfsCustomerId,
            @RequestParam int gfsCustomerTypeCode, @RequestParam String itemId, @RequestParam String futureItemDesc) {
        String userName = gfsUserDetailsService.getCurrentUserId();
        assignmentService.deleteItemAssignment(contractPriceProfileSeq, gfsCustomerId, gfsCustomerTypeCode, itemId, futureItemDesc, userName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
