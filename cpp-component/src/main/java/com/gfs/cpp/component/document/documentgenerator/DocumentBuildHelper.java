package com.gfs.cpp.component.document.documentgenerator;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabStop;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabs;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc.Enum;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.constants.CPPConstants;

@Component
public class DocumentBuildHelper {

    public void buildTableCell(int cellPos, int paragraphPos, String cellContent, XWPFTableRow tableRow) {
        XWPFRun runCellOne = tableRow.getCell(cellPos).addParagraph().createRun();
        tableRow.getCell(cellPos).removeParagraph(paragraphPos);
        tableRow.getCell(cellPos).getParagraphArray(paragraphPos).setSpacingBefore(120);
        tableRow.getCell(cellPos).getParagraphArray(paragraphPos).setIndentationLeft(100);
        setRun(runCellOne, cellContent, false, false, false);
    }

    public void setRun(XWPFRun run, String text, boolean bold, boolean addCR, boolean underline) {
        run.setBold(bold);
        run.setText(text);
        run.setFontSize(text.equals(CPPConstants.DOCX_PRICING) ? CPPConstants.DOCX_HEADER_FONT_SIZE : CPPConstants.DOCX_FONT_SIZE);
        run.setFontFamily(CPPConstants.DOCX_FONT);
        if (addCR) {
            run.addCarriageReturn();
        }
        if (underline) {
            run.setUnderline(UnderlinePatterns.SINGLE);
        }
    }

    public void setTableCellAlignment(XWPFTableCell cell, ParagraphAlignment align) {
        XWPFParagraph para = cell.getParagraphs().get(0);
        para.setAlignment(align);
    }

    public void setTableHeader(XWPFTableCell cell, String content, boolean bold, boolean addCR) {
        XWPFRun run = cell.addParagraph().createRun();
        cell.removeParagraph(0);
        cell.getParagraphArray(0).setSpacingBefore(120);
        setRun(run, content, bold, addCR, false);
    }

    private void setTabStop(XWPFParagraph paragraph, Enum forString, BigInteger pos1) {
        CTP oCTP = paragraph.getCTP();
        CTPPr oPPr = oCTP.getPPr();
        if (oPPr == null)
            oPPr = oCTP.addNewPPr();

        CTTabs oTabs = oPPr.getTabs();
        if (oTabs == null)
            oTabs = oPPr.addNewTabs();

        CTTabStop oTabStop = oTabs.addNewTab();
        oTabStop.setVal(forString);
        oTabStop.setPos(pos1);
    }

    public void setParagraphRun(XWPFDocument document, ParagraphAlignment align, String text, boolean bold, boolean addCR, boolean underline,
            boolean sameline) {
        XWPFParagraph paragraph;
        if (sameline) {
            paragraph = document.getLastParagraph();
        } else {
            paragraph = document.createParagraph();
        }

        paragraph.setAlignment(align);
        XWPFRun run = paragraph.createRun();
        setRun(run, text, bold, addCR, underline);
    }

    public void setBorders(XWPFTable table) {
        table.getCTTbl().getTblPr().getTblBorders().getTop().setSz(BigInteger.valueOf(CPPConstants.DOCX_BORDER_SIZE));
        table.getCTTbl().getTblPr().getTblBorders().getLeft().setSz(BigInteger.valueOf(CPPConstants.DOCX_BORDER_SIZE));
        table.getCTTbl().getTblPr().getTblBorders().getRight().setSz(BigInteger.valueOf(CPPConstants.DOCX_BORDER_SIZE));
        table.getCTTbl().getTblPr().getTblBorders().getBottom().setSz(BigInteger.valueOf(CPPConstants.DOCX_BORDER_SIZE));
        table.getCTTbl().getTblPr().getTblBorders().getInsideH().setSz(BigInteger.valueOf(CPPConstants.DOCX_BORDER_SIZE));
        table.getCTTbl().getTblPr().getTblBorders().getInsideV().setSz(BigInteger.valueOf(CPPConstants.DOCX_BORDER_SIZE));
    }

    public void setFooter(XWPFDocument document, int contractPriceProfileId) {
        CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
        XWPFHeaderFooterPolicy policy = new XWPFHeaderFooterPolicy(document, sectPr);
        XWPFFooter footer = policy.createFooter(XWPFHeaderFooterPolicy.DEFAULT);
        XWPFParagraph paragraph = footer.getParagraphArray(0);
        if (paragraph == null)
            paragraph = footer.createParagraph();
        XWPFRun run;
        setFooterRun(CPPConstants.DOCX_FOOTER_VERSION + contractPriceProfileId, paragraph);
        paragraph.setAlignment(ParagraphAlignment.LEFT);

        paragraph = footer.getParagraphArray(0);
        if (paragraph == null)
            paragraph = footer.createParagraph();

        run = paragraph.createRun();
        run.getCTR().addNewFldChar().setFldCharType(org.openxmlformats.schemas.wordprocessingml.x2006.main.STFldCharType.BEGIN);

        run = paragraph.createRun();
        run.addTab();
        run.getCTR().addNewFldChar().setFldCharType(org.openxmlformats.schemas.wordprocessingml.x2006.main.STFldCharType.END);
        run.addTab();

        paragraph = footer.getParagraphArray(0);
        if (paragraph == null)
            paragraph = footer.createParagraph();

        SimpleDateFormat formatter = new SimpleDateFormat(CPPConstants.DATE_FORMAT);
        Date date = new Date();
        setFooterRun(formatter.format(date), paragraph);

        BigInteger pos1 = BigInteger.valueOf(CPPConstants.DOCX_FOOTER_TAB_ONE_LENGTH);
        setTabStop(paragraph, STTabJc.Enum.forString(CPPConstants.DOCX_CENTER), pos1);
        BigInteger pos2 = BigInteger.valueOf(CPPConstants.DOCX_FOOTER_TAB_TWO_LENGTH);
        setTabStop(paragraph, STTabJc.Enum.forString(CPPConstants.DOCX_RIGHT), pos2);

    }

    public void formatParagraph(XWPFDocument doc, String text) {

        // This regex is used to fetch all string present within quotations, i.e. "ABC" is returned from the string '"ABC"DEF'.
        String regex = "\"[0-9a-zA-Z\\s !@#$%^&*(),.?:{}|<>-]+\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        List<String> doubleQuotedStringList = new ArrayList<>();
        while (matcher.find()) {
            doubleQuotedStringList.add(matcher.group());
        }
        String[] arg = text.split(regex);
        for (int i = 0; i < arg.length; i++) {
            boolean addCR = (i == arg.length - 1 ? true : false);
            setParagraphRun(doc, ParagraphAlignment.BOTH, arg[i], false, addCR, false, (i == 0 ? false : true));
            if (i < doubleQuotedStringList.size()) {
                setParagraphRun(doc, ParagraphAlignment.BOTH, doubleQuotedStringList.get(i), true, addCR, false, true);
            }
        }
    }

    private void setFooterRun(String content, XWPFParagraph paragraph) {
        XWPFRun run = paragraph.createRun();
        run.setText(content);
        run.setFontSize(CPPConstants.DOCX_FOOTER_SIZE);
        run.addTab();
    }

}
