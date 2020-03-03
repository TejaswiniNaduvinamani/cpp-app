package com.gfs.cpp.component.document.documentgenerator;

import java.io.File;
import java.io.IOException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.gfs.corp.component.price.common.types.AmountType;
import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.common.dto.markup.MarkupGridDTO;
import com.gfs.cpp.common.dto.review.ReviewDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseReviewDTO;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;

@Component
public class ExhibitDocxGenerator {

    @Autowired
    private DocumentBuildHelper documentBuildHelper;

    @Autowired
    private ExhibitDocxFormatter exhibitDocxFormatter;

    @Autowired
    private MarkupDataTableCreator markupDataTableCreator;

    @Autowired
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Autowired
    private Environment environment;

    public static final Logger logger = LoggerFactory.getLogger(ExhibitDocxGenerator.class);

    public void setCustomStyle(XWPFStyles styles) {

        logger.info("Adding Default Style");

        CTStyles ctStyles = CTStyles.Factory.newInstance();
        String strStyleId = "headline1";
        CTStyle ctStyle = ctStyles.addNewStyle();

        ctStyle.setStyleId(strStyleId);

        styles.setStyles(ctStyles);
    }

    public File generateExhibitDocument(ReviewDTO review, int contractPriceProfileSeq) throws InvalidFormatException, IOException {
        XWPFDocument doc = createNewXWPFDocument();
        XWPFStyles styles = doc.createStyles();
        setCustomStyle(styles);

        int paragraphNumber = 1;
        String defaultContractName = StringUtils.EMPTY;
        ContractPricingResponseDTO contractPriceProfileDetails = contractPriceProfileRepository.fetchContractDetailsByCppSeq(contractPriceProfileSeq);
        int contractPriceProfileId = contractPriceProfileDetails.getContractPriceProfileId();

        if (CollectionUtils.isNotEmpty(review.getMarkupReviewDTO().getMarkupGridDTOs())) {
            defaultContractName = contractPriceProfileDetails.getClmContractName();
            review.getMarkupReviewDTO().getMarkupGridDTOs().get(0).setMarkupName(defaultContractName);
        }

        documentBuildHelper.setFooter(doc, contractPriceProfileId);
        paragraphNumber = buildContractPricingSection(doc, review, paragraphNumber);
        paragraphNumber = buildDistributionCenterSection(doc, review, paragraphNumber);
        paragraphNumber = buildMarkupSection(review, doc, paragraphNumber);
        buildSplitcaseSection(doc, review, paragraphNumber, defaultContractName);
        doc.enforceReadonlyProtection(environment.getProperty(CPPConstants.DOCX_PSWD), null);
        String docxName = CPPConstants.DEFAULT_DOCX_NAME;
        if (null != defaultContractName) {
            docxName = defaultContractName + CPPConstants.HYPHEN + contractPriceProfileId;
            docxName = docxName.replaceAll("[\\\\/:*?\"<>|]", "_");
        }
        return exhibitDocxFormatter.setDocxProperty(doc, docxName + CPPConstants.DOCX);
    }

    public XWPFDocument createNewXWPFDocument() {
        return new XWPFDocument();
    }

    int buildMarkupSection(ReviewDTO review, XWPFDocument doc, int paragraphNumber) {
        documentBuildHelper.setParagraphRun(doc, ParagraphAlignment.LEFT, paragraphNumber + CPPConstants.DOCX_SELL_PRICE, true, true, false, false);
        paragraphNumber++;
        String sellPriceParagraphOne = review.getMarkupReviewDTO().getMarkupBasedOnSellContractLanguage1();
        if (StringUtils.isNotBlank(review.getSplitCaseReviewDTO().getFeeTypeContractLanguage())) {
            sellPriceParagraphOne = sellPriceParagraphOne + " " + review.getSplitCaseReviewDTO().getFeeTypeContractLanguage();
        }
        documentBuildHelper.formatParagraph(doc, sellPriceParagraphOne);
        setMarkupTypeContractLanguageInExhibit(review, doc);
        documentBuildHelper.formatParagraph(doc, review.getMarkupReviewDTO().getMarkupBasedOnSellContractLanguage2());

        documentBuildHelper.setParagraphRun(doc, ParagraphAlignment.LEFT, paragraphNumber + CPPConstants.DOCX_MARKUPS, true, true, false, false);
        paragraphNumber++;
        for (MarkupGridDTO markupGridDTO : review.getMarkupReviewDTO().getMarkupGridDTOs()) {
            buildMarkupTableSection(doc, markupGridDTO);
        }
        return paragraphNumber;
    }

    private void setMarkupTypeContractLanguageInExhibit(ReviewDTO review, XWPFDocument doc) {
        if (review.getMarkupReviewDTO().getMarkupTypeDefinitionSellUnitLanguage() != null) {
            documentBuildHelper.formatParagraph(doc, review.getMarkupReviewDTO().getMarkupTypeDefinitionSellUnitLanguage());

        }
        if (review.getMarkupReviewDTO().getMarkupTypeDefinitionPerCaseLanguage() != null) {
            documentBuildHelper.formatParagraph(doc, review.getMarkupReviewDTO().getMarkupTypeDefinitionPerCaseLanguage());

        }
        if (review.getMarkupReviewDTO().getMarkupTypeDefinitionPerWeightLanguage() != null) {
            documentBuildHelper.formatParagraph(doc, review.getMarkupReviewDTO().getMarkupTypeDefinitionPerWeightLanguage());
        }
    }

    public void buildMarkupTableSection(XWPFDocument doc, MarkupGridDTO markupGridDTO) {
        documentBuildHelper.setParagraphRun(doc, ParagraphAlignment.LEFT, markupGridDTO.getMarkupName(), true, true, false, false);
        XWPFTable table = doc.createTable();
        documentBuildHelper.setBorders(table);

        documentBuildHelper.setTableHeader(table.getRow(0).getCell(0), CPPConstants.DOCX_ITEM, true, false);
        documentBuildHelper.setTableCellAlignment(table.getRow(0).getCell(0), ParagraphAlignment.CENTER);

        XWPFTableCell cell = table.getRow(0).addNewTableCell();
        documentBuildHelper.setTableHeader(cell, CPPConstants.DOCX_MARKUP, true, false);
        documentBuildHelper.setTableCellAlignment(table.getRow(0).getCell(1), ParagraphAlignment.CENTER);

        cell = table.getRow(0).addNewTableCell();
        documentBuildHelper.setTableHeader(cell, CPPConstants.DOCX_MARKUP_TYPE, true, false);
        documentBuildHelper.setTableCellAlignment(table.getRow(0).getCell(2), ParagraphAlignment.CENTER);

        markupDataTableCreator.createMarkupDataTable(markupGridDTO, table);
        doc.createParagraph();
    }

    void buildSplitcaseSection(XWPFDocument doc, ReviewDTO review, int paragraphNumber, String contractName) {
        if (null != review.getSplitCaseReviewDTO() && CollectionUtils.isNotEmpty(review.getSplitCaseReviewDTO().getSplitCaseFeeValues())) {
            documentBuildHelper.setParagraphRun(doc, ParagraphAlignment.LEFT, paragraphNumber + CPPConstants.DOCX_SPLITCASE_FEE, true, true, false,
                    false);
            buildSplitCaseFeeTableSection(doc, review.getSplitCaseReviewDTO(), contractName);
        }
    }

    public void buildSplitCaseFeeTableSection(XWPFDocument doc, SplitCaseReviewDTO splitCaseReviewDTO, String contractName) {
        documentBuildHelper.setParagraphRun(doc, ParagraphAlignment.LEFT, contractName, true, true, false, false);
        XWPFTable table = doc.createTable();
        documentBuildHelper.setBorders(table);
        documentBuildHelper.setTableHeader(table.getRow(0).getCell(0), CPPConstants.DOCX_ITEM, true, false);
        documentBuildHelper.setTableCellAlignment(table.getRow(0).getCell(0), ParagraphAlignment.CENTER);

        XWPFTableCell cell = table.getRow(0).addNewTableCell();
        documentBuildHelper.setTableHeader(cell, CPPConstants.DOCX_SPLITCASE, true, false);
        documentBuildHelper.setTableCellAlignment(table.getRow(0).getCell(1), ParagraphAlignment.CENTER);

        for (SplitCaseDTO splitCaseDTO : splitCaseReviewDTO.getSplitCaseFeeValues()) {
            XWPFTableRow tableRowTwo = table.createRow();
            documentBuildHelper.buildTableCell(0, 0, WordUtils.capitalizeFully(splitCaseDTO.getProductType()) + CPPConstants.DOCX_CATEGORY,
                    tableRowTwo);
            if ((AmountType.PERCENT.getCode()).equals(splitCaseDTO.getUnit())) {
                documentBuildHelper.buildTableCell(1, 0, splitCaseDTO.getSplitCaseFee() + splitCaseDTO.getUnit(), tableRowTwo);
            } else {
                documentBuildHelper.buildTableCell(1, 0, splitCaseDTO.getUnit() + splitCaseDTO.getSplitCaseFee(), tableRowTwo);
            }
        }
    }

    int buildDistributionCenterSection(XWPFDocument doc, ReviewDTO review, int paragraphNumber) {
        if (!review.getDistributionCenter().isEmpty()) {
            documentBuildHelper.setParagraphRun(doc, ParagraphAlignment.LEFT, paragraphNumber + CPPConstants.DOCX_DIST, true, true, false, false);
            paragraphNumber++;

            XWPFParagraph paraTen = doc.createParagraph();
            paraTen.setAlignment(ParagraphAlignment.LEFT);

            XWPFRun runTen = paraTen.createRun();

            for (String distributionCenter : review.getDistributionCenter()) {
                runTen.setText(distributionCenter);
                runTen.addBreak();
            }
            runTen.removeBreak();
            runTen.setFontSize(CPPConstants.DOCX_FONT_SIZE);
            runTen.setFontFamily(CPPConstants.DOCX_FONT);
        }
        return paragraphNumber;
    }

    int buildContractPricingSection(XWPFDocument doc, ReviewDTO review, int paragraphNumber) {
        documentBuildHelper.setParagraphRun(doc, ParagraphAlignment.CENTER, CPPConstants.DOCX_PRICING, true, true, false, false);
        documentBuildHelper.setParagraphRun(doc, ParagraphAlignment.LEFT, paragraphNumber + CPPConstants.DOCX_DURATION, true, true, false, false);
        paragraphNumber++;
        documentBuildHelper.formatParagraph(doc, review.getContractPricingReviewDTO().getScheduleForCostContractLanguage());

        if (null != review.getContractPricingReviewDTO().getFormalPriceAuditContractLanguage()
                || null != review.getContractPricingReviewDTO().getPriceVerificationContractLanguage()) {
            documentBuildHelper.setParagraphRun(doc, ParagraphAlignment.LEFT, paragraphNumber + CPPConstants.DOCX_AUDIT, true, true, false, false);
            paragraphNumber++;

            if (null != review.getContractPricingReviewDTO().getFormalPriceAuditContractLanguage()
                    && null != review.getContractPricingReviewDTO().getPriceVerificationContractLanguage()) {
                documentBuildHelper.formatParagraph(doc, review.getContractPricingReviewDTO().getFormalPriceAuditContractLanguage());
                documentBuildHelper.formatParagraph(doc, review.getContractPricingReviewDTO().getPriceVerificationContractLanguage());
            }
            if (null != review.getContractPricingReviewDTO().getFormalPriceAuditContractLanguage()
                    && null == review.getContractPricingReviewDTO().getPriceVerificationContractLanguage()) {
                documentBuildHelper.formatParagraph(doc, review.getContractPricingReviewDTO().getFormalPriceAuditContractLanguage());
            }
            if (null == review.getContractPricingReviewDTO().getFormalPriceAuditContractLanguage()
                    && null != review.getContractPricingReviewDTO().getPriceVerificationContractLanguage()) {
                documentBuildHelper.formatParagraph(doc, review.getContractPricingReviewDTO().getPriceVerificationContractLanguage());
            }

        }

        documentBuildHelper.setParagraphRun(doc, ParagraphAlignment.LEFT, paragraphNumber + CPPConstants.DOCX_COST, true, true, false, false);
        paragraphNumber++;
        documentBuildHelper.formatParagraph(doc, review.getContractPricingReviewDTO().getCostOfProductsContractLanguage());
        return paragraphNumber;
    }

}
