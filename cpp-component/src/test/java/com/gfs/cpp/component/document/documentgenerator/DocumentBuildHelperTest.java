package com.gfs.cpp.component.document.documentgenerator;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.component.document.documentgenerator.DocumentBuildHelper;

@RunWith(MockitoJUnitRunner.class)
public class DocumentBuildHelperTest {

    @InjectMocks
    private DocumentBuildHelper target;

    @Mock
    private XWPFTableRow tableRow;
    @Mock
    private XWPFTableCell xWPFTableCell;
    @Mock
    private XWPFParagraph xWPFParagraph;
    @Mock
    private XWPFRun xWPFRun;
    @Mock
    private XWPFFooter xwpfFooter;
    @Spy
    private XWPFDocument xwpfDocument;

    @Test
    public void shouldBuildTableCell() throws Exception {
        int cellPos = 1;
        int paragraphPos = 2;
        String cellContent = "content";
        doReturn(xWPFTableCell).when(tableRow).getCell(cellPos);
        doReturn(xWPFParagraph).when(xWPFTableCell).addParagraph();
        doReturn(xWPFRun).when(xWPFParagraph).createRun();
        doReturn(xWPFParagraph).when(xWPFTableCell).getParagraphArray(paragraphPos);

        target.buildTableCell(cellPos, paragraphPos, cellContent, tableRow);

        verify(xWPFTableCell).removeParagraph(paragraphPos);
        verify(xWPFParagraph).setSpacingBefore(120);
        verify(xWPFParagraph).setIndentationLeft(100);
        verify(xWPFRun).setText(cellContent);
        verify(xWPFRun).setBold(false);
        verify(xWPFRun).setFontSize(CPPConstants.DOCX_FONT_SIZE);
        verify(xWPFRun).setFontFamily(CPPConstants.DOCX_FONT);

    }

    @Test
    public void shouldRunProperties() throws Exception {
        String content = "content";
        boolean isBold = false;

        target.setRun(xWPFRun, content, false, true, false);

        verify(xWPFRun).setText(content);
        verify(xWPFRun).setBold(isBold);
    }

    @Test
    public void shouldSetFooter() {

        target.setFooter(xwpfDocument, 0);

        verify(xwpfDocument, atLeastOnce()).getDocument();
    }

    @Test
    public void shouldSetBorders() {
        target.setBorders(xwpfDocument.createTable());
    }

    @Test
    public void shouldSetParagraphRun() {
        doReturn(xWPFRun).when(xWPFParagraph).createRun();
        doReturn(xWPFParagraph).when(xwpfDocument).createParagraph();

        target.setParagraphRun(xwpfDocument, ParagraphAlignment.LEFT, "content", false, false, false, false);

        verify(xWPFParagraph).createRun();
        verify(xwpfDocument).createParagraph();
    }

    @Test
    public void shouldSetParagraphRunForFurtheranceToDisplayInSameLine() {
        doReturn(xWPFRun).when(xWPFParagraph).createRun();
        doReturn(xWPFParagraph).when(xwpfDocument).getLastParagraph();

        target.setParagraphRun(xwpfDocument, ParagraphAlignment.LEFT, "content", false, false, false, true);

        verify(xWPFParagraph).createRun();
        verify(xwpfDocument).getLastParagraph();
    }

    @Test
    public void shouldRunPropertiesForFurtheranceHeader() throws Exception {

        boolean isBold = false;

        target.setRun(xWPFRun, CPPConstants.DOCX_FURTHERANCE, false, true, true);

        verify(xWPFRun).setText(CPPConstants.DOCX_FURTHERANCE);
        verify(xWPFRun).setBold(isBold);
    }

    @Test
    public void shouldFormatParagraph() {

        String text = "\"ABC\"DEF";
        target.formatParagraph(xwpfDocument, text);
    }
}
