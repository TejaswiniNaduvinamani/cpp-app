package com.gfs.cpp.web.controller.review;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingReviewDTO;
import com.gfs.cpp.common.dto.markup.MarkupGridDTO;
import com.gfs.cpp.common.dto.markup.MarkupReviewDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.dto.review.ReviewDTO;
import com.gfs.cpp.common.dto.review.SavePricingExhibitDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseReviewDTO;
import com.gfs.cpp.component.review.ReviewService;
import com.gfs.cpp.component.userdetails.CppUserDetailsService;
import com.gfs.cpp.proxy.clm.FileRemover;
import com.gfs.cpp.web.controller.review.ReviewController;
import com.gfs.cpp.web.util.BaseTestClass;

@RunWith(SpringJUnit4ClassRunner.class)
public class ReviewControllerTest extends BaseTestClass {

    public static final String FETCH_REVIEW_URL = "/review/fetchReviewData?contractPriceProfileSeq=1";
    public static final String CREATE_EXHIBIT_URL = "/review/createExhibitDocument?contractPriceProfileSeq=1";
    public static final String SAVE_EXHIBIT_URL = "/review/savePricingExhibit";

    @InjectMocks
    private ReviewController reviewController;

    @Mock
    private ReviewService reviewService;

    @Mock
    private File tst;

    @Mock
    private FileInputStream fileInputStream;

    @Mock
    private CppUserDetailsService gfsUserDetailsService;

    @Mock
    private FileRemover fileRemover;

    @Before
    public void setUp() {
        mockMvc = standaloneSetup(reviewController).build();
    }

    @Test
    public void fetchReviewDataTest() throws Exception {
        int contractPriceProfileSeq = 1;

        ResultActions result = mockMvc.perform(get(FETCH_REVIEW_URL).contentType(MediaType.APPLICATION_JSON_VALUE));
        result.andExpect(status().isOk());

        verify(reviewService).fetchReviewData(contractPriceProfileSeq);
    }

    @Test
    public void createExhibitDocumentTest() throws Exception {
        ReviewDTO review = new ReviewDTO();
        buildReviewDTO(review);
        File file = new File(CPPConstants.DEFAULT_DOCX_NAME + CPPConstants.DOCX);
        file.createNewFile();

        when(reviewService.fetchReviewData(eq(1))).thenReturn(review);
        when(reviewService.createExhibitDocument(Matchers.any(review.getClass()), eq(1))).thenReturn(file);

        ResultActions result = mockMvc.perform(get(CREATE_EXHIBIT_URL));
        result.andExpect(status().isOk());

        verify(reviewService).createExhibitDocument(review, 1);
        verify(fileRemover, atLeastOnce()).deleteFile(file);

    }

    @Test
    public void savePricingExhibitTest() throws Exception {

        int contractPriceProfileSeq = 1;
        String contractAgeementId = "Test";
        String contractTypeName = "DAN";
        String userName = "vc71u";

        SavePricingExhibitDTO savePricingExhibitDTO = new SavePricingExhibitDTO();
        savePricingExhibitDTO.setContractAgeementId(contractAgeementId);
        savePricingExhibitDTO.setContractPriceProfileSeq(contractPriceProfileSeq);
        savePricingExhibitDTO.setContractTypeName(contractTypeName);
        String json = new ObjectMapper().writeValueAsString(savePricingExhibitDTO);

        when(gfsUserDetailsService.getCurrentUserId()).thenReturn(userName);

        ResultActions result = mockMvc.perform(post(SAVE_EXHIBIT_URL).requestAttr("markupSaveDTO", savePricingExhibitDTO).content(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        result.andExpect(status().isOk());

        verify(reviewService).savePricingExhibit(savePricingExhibitDTO, userName);
    }

    private void buildReviewDTO(ReviewDTO review) {
        List<MarkupGridDTO> markupGridDTOList = new ArrayList<>();
        ContractPricingReviewDTO contractPricingReviewDTO = new ContractPricingReviewDTO();
        List<String> distributionCenter = new ArrayList<>();
        List<SplitCaseDTO> splitCaseFeeValues = new ArrayList<>();
        String dc = "TEST";
        distributionCenter.add(dc);
        MarkupGridDTO markupGridDTO = new MarkupGridDTO();
        markupGridDTO.setMarkupName("TEST");
        markupGridDTOList.add(markupGridDTO);
        MarkupReviewDTO markupReviewDTO = new MarkupReviewDTO();
        markupReviewDTO.setMarkupGridDTOs(markupGridDTOList);
        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setMarkupType(1);
        List<ProductTypeMarkupDTO> productTypeMarkupList = new ArrayList<>();
        productTypeMarkupList.add(productTypeMarkupDTO);
        markupGridDTO.setProductTypeMarkups(productTypeMarkupList);
        List<MarkupGridDTO> markupGridDTOs = new ArrayList<>();
        markupGridDTOs.add(markupGridDTO);
        markupReviewDTO.setMarkupGridDTOs(markupGridDTOs);
        SplitCaseDTO splitCaseGridDTO = new SplitCaseDTO();
        splitCaseFeeValues.add(splitCaseGridDTO);
        SplitCaseReviewDTO splitCaseReviewDTO = new SplitCaseReviewDTO();
        splitCaseReviewDTO.setSplitCaseFeeValues(splitCaseFeeValues);
        review.setContractPricingReviewDTO(contractPricingReviewDTO);
        review.setDistributionCenter(distributionCenter);
        review.setMarkupReviewDTO(markupReviewDTO);
        review.setSplitCaseReviewDTO(splitCaseReviewDTO);

    }

}
