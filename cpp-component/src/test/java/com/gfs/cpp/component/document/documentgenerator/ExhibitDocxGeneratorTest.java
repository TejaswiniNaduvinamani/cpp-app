package com.gfs.cpp.component.document.documentgenerator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageProperties;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles;
import org.springframework.core.env.Environment;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingReviewDTO;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.MarkupGridDTO;
import com.gfs.cpp.common.dto.markup.MarkupReviewDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.dto.review.ReviewDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseReviewDTO;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.document.documentgenerator.DocumentBuildHelper;
import com.gfs.cpp.component.document.documentgenerator.ExhibitDocxFormatter;
import com.gfs.cpp.component.document.documentgenerator.ExhibitDocxGenerator;
import com.gfs.cpp.component.document.documentgenerator.MarkupDataTableCreator;
import com.gfs.cpp.component.splitcase.SplitCaseService;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;

@RunWith(MockitoJUnitRunner.class)
public class ExhibitDocxGeneratorTest {

    private static final String FILE_NAME = "TEST_DOCX-1.docx";

    @InjectMocks
    @Spy
    private ExhibitDocxGenerator target;

    @Mock
    private SplitCaseService splitCaseService;

    @Mock
    private Environment environment;

    @Mock
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Mock
    private DocumentBuildHelper documentBuildHelper;

    @Mock
    private MarkupDataTableCreator markupDataTableCreator;

    @Mock
    private File file;

    @Captor
    private ArgumentCaptor<XWPFDocument> xwpfDocumentCaptor;

    @Captor
    private ArgumentCaptor<XWPFTableCell> xwpfTableCellCaptor;

    @Mock
    private ExhibitDocxFormatter exhibitDocxFormatter;

    @Mock
    private FileOutputStream fileOutputStream;

    @Mock
    private OPCPackage opc;

    @Mock
    XWPFDocument doc;

    @Mock
    private PackageProperties packageProperties;

    @Mock
    private XWPFTable table;

    @Mock
    private XWPFTableRow row;

    @Mock
    private CPPDateUtils cppDateUtils;

    @Mock
    private XWPFStyles xwpfStyles;

    @Test
    public void shouldCreateExhibitDocumentWithPriceVerificationForEmptyContractName() throws InvalidFormatException, IOException {

        int contractPriceProfileSeq = 1;
        ReviewDTO review = new ReviewDTO();
        reviewDTOBuilder(review, "priceVerificationLanguage", null, "%");

        review.getMarkupReviewDTO().getMarkupGridDTOs().get(0).setMarkupName(StringUtils.EMPTY);
        int paragraphNumber = 1;

        doReturn(doc).when(target).createNewXWPFDocument();
        doReturn(paragraphNumber).when(target).buildContractPricingSection(doc, review, paragraphNumber);
        doReturn(paragraphNumber).when(target).buildDistributionCenterSection(doc, review, paragraphNumber);
        doReturn(paragraphNumber).when(target).buildMarkupSection(review, doc, paragraphNumber);
        doNothing().when(target).buildSplitcaseSection(eq(doc), eq(review), eq(paragraphNumber),
                eq((String) buildContractPriceProfileDetails().getClmContractName()));
        when(doc.createStyles()).thenReturn(xwpfStyles);
        doReturn(file).when(exhibitDocxFormatter).setDocxProperty(xwpfDocumentCaptor.capture(),
                eq((String) buildContractPriceProfileDetails().getClmContractName() + "-1.docx"));
        doReturn(opc).when(exhibitDocxFormatter).openFileWithOPCPackage(file);
        doReturn(fileOutputStream).when(exhibitDocxFormatter).createFileOutputStream(file);
        doReturn(packageProperties).when(opc).getPackageProperties();

        when(environment.getProperty(CPPConstants.DOCX_PSWD)).thenReturn("password");

        when(contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq)).thenReturn(buildContractPriceProfileDetails());

        assertThat(target.generateExhibitDocument(review, contractPriceProfileSeq), equalTo(file));

        verify(environment).getProperty(CPPConstants.DOCX_PSWD);
        verify(documentBuildHelper).setFooter(xwpfDocumentCaptor.capture(), eq(1));
        verify(doc).createStyles();
        verify(exhibitDocxFormatter).setDocxProperty(xwpfDocumentCaptor.capture(),
                eq((String) buildContractPriceProfileDetails().getClmContractName() + "-1.docx"));
        verify(target).createNewXWPFDocument();
        verify(target).buildContractPricingSection(doc, review, paragraphNumber);
        verify(target).buildDistributionCenterSection(doc, review, paragraphNumber);
        verify(target).buildMarkupSection(review, doc, paragraphNumber);
        verify(target).buildSplitcaseSection(eq(doc), eq(review), eq(paragraphNumber),
                eq((String) buildContractPriceProfileDetails().getClmContractName()));
        verify(xwpfStyles).setStyles(any(CTStyles.class));
        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);

    }

    @Test
    public void shouldCreateExhibitDocumentWithPriceVerification() throws InvalidFormatException, IOException {

        int contractPriceProfileSeq = 1;
        ReviewDTO review = new ReviewDTO();
        reviewDTOBuilder(review, "priceVerificationLanguage", null, "%");
        int paragraphNumber = 1;

        doReturn(doc).when(target).createNewXWPFDocument();
        doReturn(paragraphNumber).when(target).buildContractPricingSection(doc, review, paragraphNumber);
        doReturn(paragraphNumber).when(target).buildDistributionCenterSection(doc, review, paragraphNumber);
        doReturn(paragraphNumber).when(target).buildMarkupSection(review, doc, paragraphNumber);
        doNothing().when(target).buildSplitcaseSection(eq(doc), eq(review), eq(paragraphNumber),
                eq((String) buildContractPriceProfileDetails().getClmContractName()));

        doReturn(file).when(exhibitDocxFormatter).setDocxProperty(xwpfDocumentCaptor.capture(),
                eq((String) buildContractPriceProfileDetails().getClmContractName() + "-1.docx"));
        doReturn(opc).when(exhibitDocxFormatter).openFileWithOPCPackage(file);
        doReturn(fileOutputStream).when(exhibitDocxFormatter).createFileOutputStream(file);
        doReturn(packageProperties).when(opc).getPackageProperties();
        when(contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq)).thenReturn(buildContractPriceProfileDetails());
        when(environment.getProperty(CPPConstants.DOCX_PSWD)).thenReturn("password");
        when(doc.createStyles()).thenReturn(xwpfStyles);
        when(contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq)).thenReturn(buildContractPriceProfileDetails());

        assertThat(target.generateExhibitDocument(review, contractPriceProfileSeq), equalTo(file));

        verify(environment).getProperty(CPPConstants.DOCX_PSWD);
        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);
        verify(documentBuildHelper).setFooter(xwpfDocumentCaptor.capture(), eq(1));
        verify(exhibitDocxFormatter).setDocxProperty(xwpfDocumentCaptor.capture(),
                eq((String) buildContractPriceProfileDetails().getClmContractName() + "-1.docx"));
        verify(target).createNewXWPFDocument();
        verify(target).buildContractPricingSection(doc, review, paragraphNumber);
        verify(target).buildDistributionCenterSection(doc, review, paragraphNumber);
        verify(target).buildMarkupSection(review, doc, paragraphNumber);
        verify(target).buildSplitcaseSection(eq(doc), eq(review), eq(paragraphNumber),
                eq((String) buildContractPriceProfileDetails().getClmContractName()));
        verify(doc).createStyles();
        verify(xwpfStyles).setStyles(any(CTStyles.class));

        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);
    }

    @Test
    public void shouldBuildMarkupSection() throws InvalidFormatException, IOException {

        int paragraphNumber = 1;
        ReviewDTO review = new ReviewDTO();
        XWPFDocument doc = new XWPFDocument();
        reviewDTOBuilder(review, "priceVerificationLanguage", null, "%");

        doReturn(file).when(exhibitDocxFormatter).setDocxProperty(doc, FILE_NAME);

        int result = target.buildMarkupSection(review, doc, paragraphNumber);

        assertThat(result, equalTo(3));

        verify(documentBuildHelper).setParagraphRun(xwpfDocumentCaptor.capture(), eq(ParagraphAlignment.LEFT), eq(1 + CPPConstants.DOCX_SELL_PRICE),
                eq(true), eq(true), eq(false), eq(false));

        verify(documentBuildHelper).formatParagraph(xwpfDocumentCaptor.capture(),
                eq(review.getMarkupReviewDTO().getMarkupBasedOnSellContractLanguage1() + " "
                        + review.getSplitCaseReviewDTO().getFeeTypeContractLanguage()));

        verify(documentBuildHelper).formatParagraph(xwpfDocumentCaptor.capture(),
                eq(review.getMarkupReviewDTO().getMarkupBasedOnSellContractLanguage2()));

        verify(documentBuildHelper).formatParagraph(xwpfDocumentCaptor.capture(),
                eq(review.getMarkupReviewDTO().getMarkupTypeDefinitionPerCaseLanguage()));

        verify(documentBuildHelper).formatParagraph(xwpfDocumentCaptor.capture(),
                eq(review.getMarkupReviewDTO().getMarkupTypeDefinitionPerWeightLanguage()));

        verify(documentBuildHelper).formatParagraph(xwpfDocumentCaptor.capture(),
                eq(review.getMarkupReviewDTO().getMarkupTypeDefinitionSellUnitLanguage()));

        verify(documentBuildHelper).setParagraphRun(xwpfDocumentCaptor.capture(), eq(ParagraphAlignment.LEFT), eq(2 + CPPConstants.DOCX_MARKUPS),
                eq(true), eq(true), eq(false), eq(false));

        doc = xwpfDocumentCaptor.getValue();

        assertThat(doc.getTables().size(), equalTo(1));

    }

    @Test
    public void shouldBuildSplitCaseSection() throws InvalidFormatException, IOException {
        int paragraphNumber = 1;
        ReviewDTO review = new ReviewDTO();
        reviewDTOBuilder(review, "priceVerificationLanguage", null, "%");

        doReturn(file).when(exhibitDocxFormatter).setDocxProperty(doc, FILE_NAME);
        doReturn(table).when(doc).createTable();
        doReturn(row).when(table).getRow(0);

        target.buildSplitcaseSection(doc, review, paragraphNumber, CPPConstants.DEFAULT_DOCX_NAME);

        verify(documentBuildHelper).setParagraphRun(xwpfDocumentCaptor.capture(), eq(ParagraphAlignment.LEFT),
                eq(1 + CPPConstants.DOCX_SPLITCASE_FEE), eq(true), eq(true), eq(false), eq(false));

        verify(documentBuildHelper).setParagraphRun(xwpfDocumentCaptor.capture(), eq(ParagraphAlignment.LEFT), eq(CPPConstants.DEFAULT_DOCX_NAME),
                eq(true), eq(true), eq(false), eq(false));

        verify(documentBuildHelper).setTableHeader(xwpfTableCellCaptor.capture(), eq(CPPConstants.DOCX_ITEM), eq(true), eq(false));

        doc = xwpfDocumentCaptor.getValue();

    }

    @Test
    public void shouldBuildDistributionCenter() throws InvalidFormatException, IOException {
        int paragraphNumber = 1;
        ReviewDTO review = new ReviewDTO();
        XWPFDocument doc = new XWPFDocument();
        reviewDTOBuilder(review, "priceVerificationLanguage", null, "%");

        target.buildDistributionCenterSection(doc, review, paragraphNumber);

        verify(documentBuildHelper).setParagraphRun(xwpfDocumentCaptor.capture(), eq(ParagraphAlignment.LEFT), eq(1 + CPPConstants.DOCX_DIST),
                eq(true), eq(true), eq(false), eq(false));

        doc = xwpfDocumentCaptor.getValue();

        assertThat(doc.getBodyElements().size(), equalTo(1));

    }

    @Test
    public void shouldBuildContarctPricingSection() throws InvalidFormatException, IOException {
        int paragraphNumber = 1;
        ReviewDTO review = new ReviewDTO();
        XWPFDocument doc = new XWPFDocument();
        reviewDTOBuilder(review, "priceVerificationLanguage", "priceAuditLanguage", "%");

        target.buildContractPricingSection(doc, review, paragraphNumber);

        verify(documentBuildHelper).setParagraphRun(xwpfDocumentCaptor.capture(), eq(ParagraphAlignment.CENTER), eq(CPPConstants.DOCX_PRICING),
                eq(true), eq(true), eq(false), eq(false));

        verify(documentBuildHelper).setParagraphRun(xwpfDocumentCaptor.capture(), eq(ParagraphAlignment.LEFT), eq(1 + CPPConstants.DOCX_DURATION),
                eq(true), eq(true), eq(false), eq(false));

        verify(documentBuildHelper).formatParagraph(xwpfDocumentCaptor.capture(),
                eq(review.getContractPricingReviewDTO().getCostOfProductsContractLanguage()));

        verify(documentBuildHelper).formatParagraph(xwpfDocumentCaptor.capture(),
                eq(review.getContractPricingReviewDTO().getPriceVerificationContractLanguage()));

        doc = xwpfDocumentCaptor.getValue();

        assertThat(doc.getBodyElements().size(), equalTo(0));
    }

    @Test
    public void shouldBuildContarctPricingSectionWithPriceVerfLanguage() throws InvalidFormatException, IOException {
        int paragraphNumber = 1;
        ReviewDTO review = new ReviewDTO();
        XWPFDocument doc = new XWPFDocument();
        reviewDTOBuilder(review, "priceVerificationLanguage", null, "%");

        target.buildContractPricingSection(doc, review, paragraphNumber);

        verify(documentBuildHelper).setParagraphRun(xwpfDocumentCaptor.capture(), eq(ParagraphAlignment.CENTER), eq(CPPConstants.DOCX_PRICING),
                eq(true), eq(true), eq(false), eq(false));

        verify(documentBuildHelper).setParagraphRun(xwpfDocumentCaptor.capture(), eq(ParagraphAlignment.LEFT), eq(1 + CPPConstants.DOCX_DURATION),
                eq(true), eq(true), eq(false), eq(false));

        verify(documentBuildHelper).formatParagraph(xwpfDocumentCaptor.capture(),
                eq(review.getContractPricingReviewDTO().getPriceVerificationContractLanguage()));

        doc = xwpfDocumentCaptor.getValue();

        assertThat(doc.getBodyElements().size(), equalTo(0));
    }

    @Test
    public void shouldBuildContarctPricingSectionWithPriceAuditLanguage() throws InvalidFormatException, IOException {
        int paragraphNumber = 1;
        ReviewDTO review = new ReviewDTO();
        XWPFDocument doc = new XWPFDocument();
        reviewDTOBuilder(review, null, "priceAuditLanguage", "%");

        target.buildContractPricingSection(doc, review, paragraphNumber);

        verify(documentBuildHelper).setParagraphRun(xwpfDocumentCaptor.capture(), eq(ParagraphAlignment.CENTER), eq(CPPConstants.DOCX_PRICING),
                eq(true), eq(true), eq(false), eq(false));

        verify(documentBuildHelper).setParagraphRun(xwpfDocumentCaptor.capture(), eq(ParagraphAlignment.LEFT), eq(1 + CPPConstants.DOCX_DURATION),
                eq(true), eq(true), eq(false), eq(false));

        verify(documentBuildHelper).formatParagraph(xwpfDocumentCaptor.capture(),
                eq(review.getContractPricingReviewDTO().getFormalPriceAuditContractLanguage()));

        doc = xwpfDocumentCaptor.getValue();

        assertThat(doc.getBodyElements().size(), equalTo(0));
    }

    @Test
    public void shouldCreateExhibitDocumentWithPriceVerificationAndFormalPriceAudit() throws InvalidFormatException, IOException {

        int contractPriceProfileSeq = 1;
        ReviewDTO review = new ReviewDTO();
        reviewDTOBuilder(review, "priceVerificationLanguage", "formalPriceAuditLanguage", "%");
        int paragraphNumber = 1;

        doReturn(doc).when(target).createNewXWPFDocument();
        doReturn(paragraphNumber).when(target).buildContractPricingSection(doc, review, paragraphNumber);
        doReturn(paragraphNumber).when(target).buildDistributionCenterSection(doc, review, paragraphNumber);
        doReturn(paragraphNumber).when(target).buildMarkupSection(review, doc, paragraphNumber);
        doNothing().when(target).buildSplitcaseSection(eq(doc), eq(review), eq(paragraphNumber),
                eq((String) buildContractPriceProfileDetails().getClmContractName()));

        doReturn(file).when(exhibitDocxFormatter).setDocxProperty(xwpfDocumentCaptor.capture(),
                eq((String) buildContractPriceProfileDetails().getClmContractName() + "-1.docx"));
        doReturn(opc).when(exhibitDocxFormatter).openFileWithOPCPackage(file);
        doReturn(fileOutputStream).when(exhibitDocxFormatter).createFileOutputStream(file);
        doReturn(packageProperties).when(opc).getPackageProperties();
        when(contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq)).thenReturn(buildContractPriceProfileDetails());
        when(environment.getProperty(CPPConstants.DOCX_PSWD)).thenReturn("password");
        when(doc.createStyles()).thenReturn(xwpfStyles);
        assertThat(target.generateExhibitDocument(review, contractPriceProfileSeq), equalTo(file));

        verify(environment).getProperty(CPPConstants.DOCX_PSWD);
        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);
        verify(documentBuildHelper).setFooter(xwpfDocumentCaptor.capture(), eq(1));
        verify(doc).createStyles();
        verify(exhibitDocxFormatter).setDocxProperty(xwpfDocumentCaptor.capture(),
                eq((String) buildContractPriceProfileDetails().getClmContractName() + "-1.docx"));
        verify(target).buildContractPricingSection(doc, review, paragraphNumber);
        verify(target).buildDistributionCenterSection(doc, review, paragraphNumber);
        verify(target).buildMarkupSection(review, doc, paragraphNumber);
        verify(target).buildSplitcaseSection(eq(doc), eq(review), eq(paragraphNumber),
                eq((String) buildContractPriceProfileDetails().getClmContractName()));

    }

    @Test
    public void shouldCreateExhibitDocumentWithFormalPriceAudit() throws InvalidFormatException, IOException {

        int contractPriceProfileSeq = 1;
        ReviewDTO review = new ReviewDTO();
        reviewDTOBuilder(review, null, "formalPriceAuditLanguage", "$");
        int paragraphNumber = 1;

        doReturn(doc).when(target).createNewXWPFDocument();
        doReturn(paragraphNumber).when(target).buildContractPricingSection(doc, review, paragraphNumber);
        doReturn(paragraphNumber).when(target).buildDistributionCenterSection(doc, review, paragraphNumber);
        doReturn(paragraphNumber).when(target).buildMarkupSection(review, doc, paragraphNumber);
        doNothing().when(target).buildSplitcaseSection(eq(doc), eq(review), eq(paragraphNumber),
                eq((String) buildContractPriceProfileDetails().getClmContractName()));

        doReturn(file).when(exhibitDocxFormatter).setDocxProperty(xwpfDocumentCaptor.capture(),
                eq((String) buildContractPriceProfileDetails().getClmContractName() + "-1.docx"));
        doReturn(opc).when(exhibitDocxFormatter).openFileWithOPCPackage(file);
        doReturn(fileOutputStream).when(exhibitDocxFormatter).createFileOutputStream(file);
        doReturn(packageProperties).when(opc).getPackageProperties();
        when(contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq)).thenReturn(buildContractPriceProfileDetails());
        when(environment.getProperty(CPPConstants.DOCX_PSWD)).thenReturn("password");
        when(doc.createStyles()).thenReturn(xwpfStyles);

        assertThat(target.generateExhibitDocument(review, contractPriceProfileSeq), equalTo(file));

        verify(environment).getProperty(CPPConstants.DOCX_PSWD);
        verify(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);
        verify(documentBuildHelper).setFooter(xwpfDocumentCaptor.capture(), eq(1));
        verify(doc).createStyles();
        verify(exhibitDocxFormatter).setDocxProperty(xwpfDocumentCaptor.capture(),
                eq((String) buildContractPriceProfileDetails().getClmContractName() + "-1.docx"));
        verify(target).createNewXWPFDocument();
        verify(target).buildContractPricingSection(doc, review, paragraphNumber);
        verify(target).buildDistributionCenterSection(doc, review, paragraphNumber);
        verify(target).buildMarkupSection(review, doc, paragraphNumber);
        verify(target).buildSplitcaseSection(eq(doc), eq(review), eq(paragraphNumber),
                eq((String) buildContractPriceProfileDetails().getClmContractName()));
    }

    private void reviewDTOBuilder(ReviewDTO review, String priceVerification, String formalPriceAudit, String unit) {

        ContractPricingReviewDTO contractPricingReviewDTO = buildContractPricingReviewDTO(priceVerification, formalPriceAudit);
        List<String> distributionCenter = buildDistributionCenter();
        MarkupReviewDTO markupDTO = buildMarkupDTO(unit);
        List<SplitCaseDTO> splitCaseFeeValues = buildSplitcaseFee(unit);
        SplitCaseReviewDTO splitCaseReviewDTO = new SplitCaseReviewDTO();
        splitCaseReviewDTO.setFeeType(2);
        splitCaseReviewDTO.setFeeTypeContractLanguage("Contract Language Fee Type");
        splitCaseReviewDTO.setSplitCaseFeeValues(splitCaseFeeValues);
        review.setContractPricingReviewDTO(contractPricingReviewDTO);
        review.setDistributionCenter(distributionCenter);
        review.setMarkupReviewDTO(markupDTO);
        review.setSplitCaseReviewDTO(splitCaseReviewDTO);
    }

    private List<SplitCaseDTO> buildSplitcaseFee(String unit) {
        List<SplitCaseDTO> splitCaseFee = new ArrayList<>();
        SplitCaseDTO splitCaseDTO = new SplitCaseDTO();
        splitCaseDTO.setUnit(unit);
        splitCaseFee.add(splitCaseDTO);
        return splitCaseFee;
    }

    private List<String> buildDistributionCenter() {
        List<String> distributionCenter = new ArrayList<>();
        String dc = "TEST";
        distributionCenter.add(dc);
        return distributionCenter;
    }

    private MarkupReviewDTO buildMarkupDTO(String unit) {
        MarkupReviewDTO markupDTO = new MarkupReviewDTO();
        MarkupGridDTO markupGridDTO = new MarkupGridDTO();
        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setMarkupType(1);
        productTypeMarkupDTO.setUnit(unit);
        List<ProductTypeMarkupDTO> productTypeMarkup = new ArrayList<>();
        List<ItemLevelMarkupDTO> itemMarkup = new ArrayList<>();
        ItemLevelMarkupDTO itemLevelMarkupDTO = new ItemLevelMarkupDTO();
        itemLevelMarkupDTO.setMarkupType(1);
        itemLevelMarkupDTO.setUnit(unit);
        itemMarkup.add(itemLevelMarkupDTO);
        productTypeMarkup.add(productTypeMarkupDTO);
        markupGridDTO.setProductTypeMarkups(productTypeMarkup);
        markupGridDTO.setItemMarkups(itemMarkup);
        markupGridDTO.setMarkupName("TEST_DOCX");
        List<MarkupGridDTO> markupGridDTOs = new ArrayList<>();
        markupGridDTOs.add(markupGridDTO);
        markupDTO.setMarkupGridDTOs(markupGridDTOs);
        markupDTO.setMarkupTypeDefinitionPerCaseLanguage("Per Case");
        markupDTO.setMarkupTypeDefinitionPerWeightLanguage("Per Weight");
        markupDTO.setMarkupTypeDefinitionSellUnitLanguage("Sell Unit");
        markupDTO.setMarkupBasedOnSellContractLanguage2("Sell Contract Language two");
        markupDTO.setMarkupBasedOnSellContractLanguage1("Sell Contract Language One");
        return markupDTO;
    }

    private ContractPricingReviewDTO buildContractPricingReviewDTO(String priceVerification, String formalPriceAudit) {
        ContractPricingReviewDTO contractPricingReviewDTO = new ContractPricingReviewDTO();
        contractPricingReviewDTO.setScheduleForCostContractLanguage("\"Schedule\" contract language");
        contractPricingReviewDTO.setCostOfProductsContractLanguage("Cost of Products");
        contractPricingReviewDTO.setPriceVerificationContractLanguage(priceVerification);
        contractPricingReviewDTO.setFormalPriceAuditContractLanguage(formalPriceAudit);
        return contractPricingReviewDTO;
    }

    private ContractPricingResponseDTO buildContractPriceProfileDetails() {
        String clmContractName = "clmContractName";
        int contractPriceProfileId = 1;
        ContractPricingResponseDTO contractPriceProfileDetails = new ContractPricingResponseDTO();

        contractPriceProfileDetails.setContractPriceProfileId(contractPriceProfileId);
        contractPriceProfileDetails.setClmContractName(clmContractName);
        return contractPriceProfileDetails;
    }
}
