package com.gfs.cpp.component.furtherance;

import java.io.File;
import java.io.IOException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.furtherance.FurtheranceDocumentDTO;
import com.gfs.cpp.common.dto.markup.MarkupGridDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.document.documentgenerator.DocumentBuildHelper;
import com.gfs.cpp.component.document.documentgenerator.ExhibitDocxFormatter;
import com.gfs.cpp.component.document.documentgenerator.ExhibitDocxGenerator;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;

@Component
public class FurtheranceDocumentGenerator {

    private static final Logger logger = LoggerFactory.getLogger(FurtheranceDocumentGenerator.class);

    @Autowired
    private DocumentBuildHelper documentBuildHelper;

    @Autowired
    private ExhibitDocxFormatter exhibitDocxFormatter;

    @Autowired
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Autowired
    private Environment environment;

    @Autowired
    private CPPDateUtils cppDateUtils;

    @Autowired
    private ExhibitDocxGenerator exhibitDocxGenerator;

    @Autowired
    private FurtheranceDocumentDataFetcher furtheranceDocumentDataFetcher;

    public File createFurtheranceDocument(int cppFurtheranceSeq) {
        File file = null;
        try {
            FurtheranceDocumentDTO furtheranceDocumentDTO = furtheranceDocumentDataFetcher.fetchFurtheranceDocumentData(cppFurtheranceSeq);
            file = generateFurtheranceDocument(furtheranceDocumentDTO);
        } catch (InvalidFormatException | IOException e) {
            logger.error("Request to Create Furtherance Docx Service failed", e);
            throw new CPPRuntimeException(CPPExceptionType.SERVICE_FAILED, "IO exception occured when processing the furtherance document");
        }
        return file;
    }

    File generateFurtheranceDocument(FurtheranceDocumentDTO furtheranceDocumentDTO) throws InvalidFormatException, IOException {
        XWPFDocument doc = exhibitDocxGenerator.createNewXWPFDocument();
        XWPFStyles styles = doc.createStyles();
        exhibitDocxGenerator.setCustomStyle(styles);
        String defaultContractName = StringUtils.EMPTY;
        if (CollectionUtils.isNotEmpty(furtheranceDocumentDTO.getMarkupDTO().getMarkupGridDTOs())) {
            defaultContractName = furtheranceDocumentDTO.getMarkupDTO().getMarkupGridDTOs().get(0).getMarkupName();
        }
        int contractPriceProfileId = contractPriceProfileRepository
                .fetchContractPriceProfileId(furtheranceDocumentDTO.getFurtheranceInformationDTO().getContractPriceProfileSeq());
        buildFurtheranceInformationSection(doc, furtheranceDocumentDTO);
        buildFurtheranceMarkupSection(doc, furtheranceDocumentDTO);
        buildFurtheranceSplitcaseSection(doc, furtheranceDocumentDTO, defaultContractName);
        doc.enforceReadonlyProtection(environment.getProperty(CPPConstants.DOCX_PSWD), null);
        String docxName = CPPConstants.DOCX_FURTHERANCE_NAME
                + furtheranceDocumentDTO.getFurtheranceInformationDTO().getFurtheranceEffectiveDate().toString() + CPPConstants.HYPHEN
                + contractPriceProfileId;
        return exhibitDocxFormatter.setDocxProperty(doc, docxName + CPPConstants.DOCX);
    }

    void buildFurtheranceMarkupSection(XWPFDocument doc, FurtheranceDocumentDTO furtheranceDocumentDTO) {
        documentBuildHelper.setParagraphRun(doc, ParagraphAlignment.LEFT, CPPConstants.DOCX_FURTHERANCE_MARK_UP, true, true, false, false);
        for (MarkupGridDTO markupGridDTO : furtheranceDocumentDTO.getMarkupDTO().getMarkupGridDTOs()) {
            exhibitDocxGenerator.buildMarkupTableSection(doc, markupGridDTO);
        }
    }

    void buildFurtheranceSplitcaseSection(XWPFDocument doc, FurtheranceDocumentDTO furtheranceDocumentDTO, String contractName) {

        if (null != furtheranceDocumentDTO.getSplitCaseDTO()
                && CollectionUtils.isNotEmpty(furtheranceDocumentDTO.getSplitCaseDTO().getSplitCaseFeeValues())) {
            documentBuildHelper.setParagraphRun(doc, ParagraphAlignment.LEFT, CPPConstants.DOCX_FURTHERANCE_SPLITCASE_FEE, true, true, false, false);
            exhibitDocxGenerator.buildSplitCaseFeeTableSection(doc, furtheranceDocumentDTO.getSplitCaseDTO(), contractName);
        }

    }

    void buildFurtheranceInformationSection(XWPFDocument doc, FurtheranceDocumentDTO furtheranceDocumentDTO) {
        documentBuildHelper.setParagraphRun(doc, ParagraphAlignment.CENTER, CPPConstants.DOCX_FURTHERANCE, true, true, true, false);
        documentBuildHelper.setParagraphRun(doc, ParagraphAlignment.LEFT, CPPConstants.DOCX_FURTHERANCE_EFFECTIVE_DATE, true, false, false, false);
        documentBuildHelper.setParagraphRun(doc, ParagraphAlignment.LEFT,
                (cppDateUtils.formatDateToString(furtheranceDocumentDTO.getFurtheranceInformationDTO().getFurtheranceEffectiveDate())), false, true,
                false, true);
        documentBuildHelper.setParagraphRun(doc, ParagraphAlignment.LEFT, CPPConstants.DOCX_FURTHERANCE_REASON_FOR_CHANGE, true, false, false, false);
        documentBuildHelper.setParagraphRun(doc, ParagraphAlignment.LEFT, furtheranceDocumentDTO.getFurtheranceInformationDTO().getChangeReasonTxt(),
                false, true, false, false);
        documentBuildHelper.setParagraphRun(doc, ParagraphAlignment.LEFT, CPPConstants.DOCX_FURTHERANCE_CONTACT_REFERENCE, true, false, false, false);
        documentBuildHelper.setParagraphRun(doc, ParagraphAlignment.LEFT,
                furtheranceDocumentDTO.getFurtheranceInformationDTO().getContractReferenceTxt(), false, true, false, false);
    }

}
