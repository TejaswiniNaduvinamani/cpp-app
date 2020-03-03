package com.gfs.cpp.web.controller.review;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gfs.cpp.common.dto.review.ReviewDTO;
import com.gfs.cpp.common.dto.review.SavePricingExhibitDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.component.review.ReviewService;
import com.gfs.cpp.component.userdetails.CppUserDetailsService;
import com.gfs.cpp.proxy.clm.FileRemover;

@RestController
@RequestMapping(value = "/review")
public class ReviewController {

    @Autowired
    @Qualifier("reviewService")
    private ReviewService reviewService;

    @Autowired
    private CppUserDetailsService gfsUserDetailsService;

    @Autowired
    private FileRemover fileRemover;

    public static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    @GetMapping(value = "/fetchReviewData", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<ReviewDTO> fetchReviewData(@RequestParam int contractPriceProfileSeq) {
        return new ResponseEntity<>(reviewService.fetchReviewData(contractPriceProfileSeq), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole(T(com.gfs.cpp.component.userdetails.RoleConstants).POWER_USER, "
            + "T(com.gfs.cpp.component.userdetails.RoleConstants).ACCOUNT_MANAGER )")
    @GetMapping(value = "/createExhibitDocument")
    public void createExhibitDocument(@RequestParam int contractPriceProfileSeq, HttpServletResponse response) {
        ReviewDTO review = reviewService.fetchReviewData(contractPriceProfileSeq);
        File file = reviewService.createExhibitDocument(review, contractPriceProfileSeq);
        if (file != null) {
            try (FileInputStream fileInput = new FileInputStream(file)) {
                response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                response.setHeader("Content-Disposition", String.format("inline; filename=%s", file.getName()));
                InputStream inputStream = new BufferedInputStream(fileInput);
                FileCopyUtils.copy(inputStream, response.getOutputStream());
                response.flushBuffer();
            } catch (Exception e) {
                logger.error("Service call to create docx failed for contract price profile sequence {}", contractPriceProfileSeq, e);
                throw new CPPRuntimeException(CPPExceptionType.SERVICE_FAILED, "IO exception occured when processing the document");
            } finally {
                boolean isDeleted = fileRemover.deleteFile(file);
                logger.info("Has file {} deleted : {}", file.getName(), isDeleted);
            }
        }
    }

    @PreAuthorize("hasAnyRole(T(com.gfs.cpp.component.userdetails.RoleConstants).POWER_USER, "
            + "T(com.gfs.cpp.component.userdetails.RoleConstants).ACCOUNT_MANAGER )")
    @PostMapping(value = "/savePricingExhibit", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Void> savePricingExhibit(@RequestBody SavePricingExhibitDTO savePricingExhibitDTO) {
        String userName = gfsUserDetailsService.getCurrentUserId();
        reviewService.savePricingExhibit(savePricingExhibitDTO, userName);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
