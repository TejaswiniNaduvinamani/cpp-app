package com.gfs.cpp.component.furtherance;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.furtherance.FurtheranceDocumentDTO;
import com.gfs.cpp.common.dto.furtherance.FurtheranceInformationDTO;
import com.gfs.cpp.common.dto.markup.MarkupGridDTO;
import com.gfs.cpp.common.dto.markup.MarkupReviewDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseDTO;
import com.gfs.cpp.common.dto.splitcase.SplitCaseReviewDTO;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.document.documentgenerator.DocumentBuildHelper;
import com.gfs.cpp.component.document.documentgenerator.ExhibitDocxFormatter;
import com.gfs.cpp.component.document.documentgenerator.ExhibitDocxGenerator;
import com.gfs.cpp.component.furtherance.FurtheranceDocumentDataFetcher;
import com.gfs.cpp.component.furtherance.FurtheranceDocumentGenerator;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;

@RunWith(MockitoJUnitRunner.class)
public class FurtheranceDocumentGeneratorTest {

    @InjectMocks
    @Spy
    private FurtheranceDocumentGenerator target;

    @Mock
    private Environment environment;

    @Mock
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Mock
    private DocumentBuildHelper documentBuildHelper;

    @Mock
    private File file;

    @Captor
    private ArgumentCaptor<XWPFDocument> xwpfDocumentCaptor;

    @Mock
    private ExhibitDocxFormatter exhibitDocxFormatter;

    @Mock
    private ExhibitDocxGenerator exhibitDocxGenerator;

    @Mock
    XWPFDocument doc;

    @Mock
    private XWPFTable table;

    @Mock
    private XWPFTableRow row;

    @Mock
    private CPPDateUtils cppDateUtils;

    @Mock
    private XWPFStyles xwpfStyles;
    @Mock
    private FurtheranceDocumentDataFetcher furtheranceDocumentDataFetcher;

    @Test
    public void shouldCreateFileFurtheranceDocumentData() throws Exception {

        int cppFurtheranceSeq = -101;

        FurtheranceDocumentDTO furtheranceDocumentDTO = new FurtheranceDocumentDTO();
        doReturn(furtheranceDocumentDTO).when(furtheranceDocumentDataFetcher).fetchFurtheranceDocumentData(cppFurtheranceSeq);
        doReturn(file).when(target).generateFurtheranceDocument(furtheranceDocumentDTO);

        File actual = target.createFurtheranceDocument(cppFurtheranceSeq);

        assertThat(actual, equalTo(file));
        verify(target).generateFurtheranceDocument(furtheranceDocumentDTO);
        verify(furtheranceDocumentDataFetcher).fetchFurtheranceDocumentData(cppFurtheranceSeq);
    }

    @Test
    public void shouldReturnCppRuntimeExceptionWhenThereIsIOException() throws Exception {

        int cppFurtheranceSeq = -101;

        FurtheranceDocumentDTO furtheranceDocumentDTO = new FurtheranceDocumentDTO();
        doReturn(furtheranceDocumentDTO).when(furtheranceDocumentDataFetcher).fetchFurtheranceDocumentData(cppFurtheranceSeq);
        doThrow(IOException.class).when(target).generateFurtheranceDocument(furtheranceDocumentDTO);

        try {
            target.createFurtheranceDocument(cppFurtheranceSeq);
        } catch (CPPRuntimeException re) {
            assertThat(re.getType(), equalTo(CPPExceptionType.SERVICE_FAILED));
        }

        verify(target).generateFurtheranceDocument(furtheranceDocumentDTO);
        verify(furtheranceDocumentDataFetcher).fetchFurtheranceDocumentData(cppFurtheranceSeq);

    }

    @Test
    public void shouldReturnCppRuntimeExceptionWhenThereIsInvalidFormatException() throws Exception {

        int cppFurtheranceSeq = -101;

        FurtheranceDocumentDTO furtheranceDocumentDTO = new FurtheranceDocumentDTO();
        doReturn(furtheranceDocumentDTO).when(furtheranceDocumentDataFetcher).fetchFurtheranceDocumentData(cppFurtheranceSeq);
        doThrow(InvalidFormatException.class).when(target).generateFurtheranceDocument(furtheranceDocumentDTO);

        try {
            target.createFurtheranceDocument(cppFurtheranceSeq);
        } catch (CPPRuntimeException re) {
            assertThat(re.getType(), equalTo(CPPExceptionType.SERVICE_FAILED));
        }

        verify(target).generateFurtheranceDocument(furtheranceDocumentDTO);
        verify(furtheranceDocumentDataFetcher).fetchFurtheranceDocumentData(cppFurtheranceSeq);

    }

    @Test
    public void shouldCreateFurtheranceExhibitDocument() throws InvalidFormatException, IOException {
        FurtheranceDocumentDTO furtheranceDocumentDTO = buildFurtheranceDocumentDTOBuilder();
        String furtheranceEffectiveDate = "01/01/9999";
        String docxName = CPPConstants.DOCX_FURTHERANCE_NAME
                + furtheranceDocumentDTO.getFurtheranceInformationDTO().getFurtheranceEffectiveDate().toString() + CPPConstants.HYPHEN + 1 + ".docx";

        when(exhibitDocxGenerator.createNewXWPFDocument()).thenReturn(doc);
        doReturn(furtheranceEffectiveDate).when(cppDateUtils)
                .formatDateToString(furtheranceDocumentDTO.getFurtheranceInformationDTO().getFurtheranceEffectiveDate());
        doReturn(file).when(exhibitDocxFormatter).setDocxProperty(xwpfDocumentCaptor.capture(), eq(docxName));
        when(contractPriceProfileRepository.fetchContractPriceProfileId(1)).thenReturn(1);
        when(environment.getProperty(CPPConstants.DOCX_PSWD)).thenReturn("password");
        when(doc.createStyles()).thenReturn(xwpfStyles);

        assertThat(target.generateFurtheranceDocument(furtheranceDocumentDTO), equalTo(file));

        XWPFDocument xwpfDocument = xwpfDocumentCaptor.getValue();

        assertThat(doc, equalTo(xwpfDocument));
        assertThat(doc.getStyles(), equalTo(xwpfDocument.getStyles()));
        assertThat(doc.getParagraphs(), equalTo(xwpfDocument.getParagraphs()));
        assertThat(doc.getTables(), equalTo(xwpfDocument.getTables()));

        verify(exhibitDocxGenerator).createNewXWPFDocument();

        verify(exhibitDocxFormatter).setDocxProperty(xwpfDocumentCaptor.capture(),
                eq(CPPConstants.DOCX_FURTHERANCE_NAME + furtheranceDocumentDTO.getFurtheranceInformationDTO().getFurtheranceEffectiveDate().toString()
                        + CPPConstants.HYPHEN + 1 + ".docx"));
        verify(contractPriceProfileRepository).fetchContractPriceProfileId(1);
        verify(environment).getProperty(CPPConstants.DOCX_PSWD);
        verify(doc).createStyles();

        verify(documentBuildHelper).setParagraphRun(xwpfDocumentCaptor.capture(), eq(ParagraphAlignment.CENTER), eq(CPPConstants.DOCX_FURTHERANCE),
                eq(true), eq(true), eq(true), eq(false));

        verify(documentBuildHelper).setParagraphRun(xwpfDocumentCaptor.capture(), eq(ParagraphAlignment.LEFT),
                eq(CPPConstants.DOCX_FURTHERANCE_EFFECTIVE_DATE), eq(true), eq(false), eq(false), eq(false));

        verify(documentBuildHelper).setParagraphRun(xwpfDocumentCaptor.capture(), eq(ParagraphAlignment.LEFT), eq(furtheranceEffectiveDate), eq(false),
                eq(true), eq(false), eq(true));

        verify(documentBuildHelper).setParagraphRun(xwpfDocumentCaptor.capture(), eq(ParagraphAlignment.LEFT),
                eq(CPPConstants.DOCX_FURTHERANCE_REASON_FOR_CHANGE), eq(true), eq(false), eq(false), eq(false));

        verify(documentBuildHelper).setParagraphRun(xwpfDocumentCaptor.capture(), eq(ParagraphAlignment.LEFT),
                eq(furtheranceDocumentDTO.getFurtheranceInformationDTO().getChangeReasonTxt()), eq(false), eq(true), eq(false), eq(false));

        verify(documentBuildHelper).setParagraphRun(xwpfDocumentCaptor.capture(), eq(ParagraphAlignment.LEFT),
                eq(CPPConstants.DOCX_FURTHERANCE_CONTACT_REFERENCE), eq(true), eq(false), eq(false), eq(false));

        verify(documentBuildHelper).setParagraphRun(xwpfDocumentCaptor.capture(), eq(ParagraphAlignment.LEFT),
                eq(furtheranceDocumentDTO.getFurtheranceInformationDTO().getContractReferenceTxt()), eq(false), eq(true), eq(false), eq(false));

        verify(documentBuildHelper).setParagraphRun(xwpfDocumentCaptor.capture(), eq(ParagraphAlignment.LEFT),
                eq(CPPConstants.DOCX_FURTHERANCE_MARK_UP), eq(true), eq(true), eq(false), eq(false));

        verify(documentBuildHelper).setParagraphRun(xwpfDocumentCaptor.capture(), eq(ParagraphAlignment.LEFT),
                eq(CPPConstants.DOCX_FURTHERANCE_SPLITCASE_FEE), eq(true), eq(true), eq(false), eq(false));
    }

    private FurtheranceInformationDTO buildFurtheranceInformationDTO() {
        FurtheranceInformationDTO furtheranceInformationDTO = new FurtheranceInformationDTO();
        furtheranceInformationDTO.setCppFurtheranceSeq(1);
        furtheranceInformationDTO.setChangeReasonTxt("change reason");
        furtheranceInformationDTO.setContractPriceProfileSeq(1);
        furtheranceInformationDTO.setContractReferenceTxt("reference text");
        furtheranceInformationDTO.setFurtheranceEffectiveDate(new LocalDate(9999, 01, 01).toDate());
        return furtheranceInformationDTO;
    }

    private FurtheranceDocumentDTO buildFurtheranceDocumentDTOBuilder() {
        FurtheranceDocumentDTO furtheranceDocumentDTO = new FurtheranceDocumentDTO();
        furtheranceDocumentDTO.setFurtheranceInformationDTO(buildFurtheranceInformationDTO());
        furtheranceDocumentDTO.setMarkupDTO(buildMarkupDTO("$"));
        List<SplitCaseDTO> splitCaseFeeValues = buildSplitcaseFee("$");
        SplitCaseReviewDTO splitCaseDTO = new SplitCaseReviewDTO();
        splitCaseDTO.setSplitCaseFeeValues(splitCaseFeeValues);
        furtheranceDocumentDTO.setSplitCaseDTO(splitCaseDTO);
        return furtheranceDocumentDTO;
    }

    private List<SplitCaseDTO> buildSplitcaseFee(String unit) {
        List<SplitCaseDTO> splitCaseFee = new ArrayList<>();
        SplitCaseDTO splitCaseDTO = new SplitCaseDTO();
        splitCaseDTO.setUnit(unit);
        splitCaseFee.add(splitCaseDTO);
        return splitCaseFee;
    }

    private MarkupReviewDTO buildMarkupDTO(String unit) {
        MarkupReviewDTO markupDTO = new MarkupReviewDTO();
        MarkupGridDTO markupGridDTO = new MarkupGridDTO();
        markupGridDTO.setMarkupName("TEST_DOCX");
        List<MarkupGridDTO> markupGridDTOs = new ArrayList<>();
        markupGridDTOs.add(markupGridDTO);
        markupDTO.setMarkupGridDTOs(markupGridDTOs);
        return markupDTO;
    }
}
