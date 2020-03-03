package com.gfs.cpp.component.document.documentgenerator;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.Collections;

import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.corp.component.price.common.types.AmountType;
import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.MarkupGridDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.dto.markup.SubgroupMarkupDTO;
import com.gfs.cpp.component.document.documentgenerator.DocumentBuildHelper;
import com.gfs.cpp.component.document.documentgenerator.MarkupDataTableCreator;

@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class MarkupDataTableCreatorTest {

    @InjectMocks
    private MarkupDataTableCreator target;

    @Mock
    private DocumentBuildHelper documentBuildHelper;

    @Mock
    private XWPFTable xwpfTable;
    @Mock
    private XWPFTableRow XWPFTableRow;

    @Test
    public void shouldAddItemDesc() throws Exception {

        MarkupGridDTO markupGridDTO = new MarkupGridDTO();

        ItemLevelMarkupDTO itemLevelMarkupDTO = new ItemLevelMarkupDTO();
        itemLevelMarkupDTO.setMarkupType(1);
        itemLevelMarkupDTO.setItemDesc("item desc");

        markupGridDTO.setItemMarkups(Collections.singletonList(itemLevelMarkupDTO));
        markupGridDTO.setSubgroupMarkups(Collections.EMPTY_LIST);
        markupGridDTO.setProductTypeMarkups(Collections.EMPTY_LIST);

        doReturn(XWPFTableRow).when(xwpfTable).createRow();

        target.createMarkupDataTable(markupGridDTO, xwpfTable);

        verify(documentBuildHelper).buildTableCell(0, 0, "item desc", XWPFTableRow);

    }

    @Test
    public void shouldAddUnitInEndWhenMarkupIsPercentForItemMarkup() throws Exception {

        MarkupGridDTO markupGridDTO = new MarkupGridDTO();

        ItemLevelMarkupDTO itemLevelMarkupDTO = new ItemLevelMarkupDTO();
        itemLevelMarkupDTO.setMarkupType(1);
        itemLevelMarkupDTO.setMarkup("10");
        itemLevelMarkupDTO.setUnit(AmountType.PERCENT.code);

        markupGridDTO.setItemMarkups(Collections.singletonList(itemLevelMarkupDTO));
        markupGridDTO.setSubgroupMarkups(Collections.EMPTY_LIST);
        markupGridDTO.setProductTypeMarkups(Collections.EMPTY_LIST);

        doReturn(XWPFTableRow).when(xwpfTable).createRow();

        target.createMarkupDataTable(markupGridDTO, xwpfTable);

        verify(documentBuildHelper).buildTableCell(1, 0, "10%", XWPFTableRow);

    }

    @Test
    public void shouldAddMarkupAtEndWhenMarkupIsNotPercentForItemMarkup() throws Exception {

        MarkupGridDTO markupGridDTO = new MarkupGridDTO();

        ItemLevelMarkupDTO itemLevelMarkupDTO = new ItemLevelMarkupDTO();
        itemLevelMarkupDTO.setMarkupType(1);
        itemLevelMarkupDTO.setMarkup("10");
        itemLevelMarkupDTO.setUnit(AmountType.DOLLAR.code);

        markupGridDTO.setItemMarkups(Collections.singletonList(itemLevelMarkupDTO));
        markupGridDTO.setSubgroupMarkups(Collections.EMPTY_LIST);
        markupGridDTO.setProductTypeMarkups(Collections.EMPTY_LIST);

        doReturn(XWPFTableRow).when(xwpfTable).createRow();

        target.createMarkupDataTable(markupGridDTO, xwpfTable);

        verify(documentBuildHelper).buildTableCell(1, 0, "$10", XWPFTableRow);

    }

    @Test
    public void shouldAddMarkupTypeNameForItemMarkup() throws Exception {

        MarkupGridDTO markupGridDTO = new MarkupGridDTO();

        ItemLevelMarkupDTO itemLevelMarkupDTO = new ItemLevelMarkupDTO();
        itemLevelMarkupDTO.setMarkupType(1);
        itemLevelMarkupDTO.setMarkup("10");
        itemLevelMarkupDTO.setUnit(AmountType.DOLLAR.code);

        markupGridDTO.setItemMarkups(Collections.singletonList(itemLevelMarkupDTO));
        markupGridDTO.setSubgroupMarkups(Collections.EMPTY_LIST);
        markupGridDTO.setProductTypeMarkups(Collections.EMPTY_LIST);

        doReturn(XWPFTableRow).when(xwpfTable).createRow();

        target.createMarkupDataTable(markupGridDTO, xwpfTable);

        verify(documentBuildHelper).buildTableCell(2, 0, " Sell Unit", XWPFTableRow);
    }

    @Test
    public void shouldAddProductTypeForProductMarkup() {

        MarkupGridDTO markupGridDTO = new MarkupGridDTO();

        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setMarkupType(2);
        productTypeMarkupDTO.setUnit("%");
        productTypeMarkupDTO.setProductType("product type name");

        markupGridDTO.setProductTypeMarkups(Collections.singletonList(productTypeMarkupDTO));
        markupGridDTO.setSubgroupMarkups(Collections.EMPTY_LIST);
        markupGridDTO.setItemMarkups(Collections.EMPTY_LIST);

        doReturn(XWPFTableRow).when(xwpfTable).createRow();

        target.createMarkupDataTable(markupGridDTO, xwpfTable);

        verify(documentBuildHelper).buildTableCell(0, 0, "Product Type Name" + CPPConstants.DOCX_CATEGORY, XWPFTableRow);
    }

    @Test
    public void shouldAddUnitAtEndWhenMarkupIsPercentForProductMarkup() {

        MarkupGridDTO markupGridDTO = new MarkupGridDTO();

        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setMarkupType(2);
        productTypeMarkupDTO.setUnit(AmountType.PERCENT.code);
        productTypeMarkupDTO.setMarkup("5");
        productTypeMarkupDTO.setProductType("product type name");

        markupGridDTO.setProductTypeMarkups(Collections.singletonList(productTypeMarkupDTO));
        markupGridDTO.setSubgroupMarkups(Collections.EMPTY_LIST);
        markupGridDTO.setItemMarkups(Collections.EMPTY_LIST);

        doReturn(XWPFTableRow).when(xwpfTable).createRow();

        target.createMarkupDataTable(markupGridDTO, xwpfTable);

        verify(documentBuildHelper).buildTableCell(1, 0, "5%", XWPFTableRow);
    }

    @Test
    public void shouldAddMarkupAtEndWhenMarkupIsNotPercentForProductMarkup() {

        MarkupGridDTO markupGridDTO = new MarkupGridDTO();

        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setMarkupType(2);
        productTypeMarkupDTO.setUnit(AmountType.DOLLAR.code);
        productTypeMarkupDTO.setMarkup("5");
        productTypeMarkupDTO.setProductType("product type name");

        markupGridDTO.setProductTypeMarkups(Collections.singletonList(productTypeMarkupDTO));
        markupGridDTO.setSubgroupMarkups(Collections.EMPTY_LIST);
        markupGridDTO.setItemMarkups(Collections.EMPTY_LIST);

        doReturn(XWPFTableRow).when(xwpfTable).createRow();

        target.createMarkupDataTable(markupGridDTO, xwpfTable);

        verify(documentBuildHelper).buildTableCell(1, 0, "$5", XWPFTableRow);
    }

    @Test
    public void shouldAddMarkupTypeForProductMarkup() {

        MarkupGridDTO markupGridDTO = new MarkupGridDTO();

        ProductTypeMarkupDTO productTypeMarkupDTO = new ProductTypeMarkupDTO();
        productTypeMarkupDTO.setMarkupType(2);
        productTypeMarkupDTO.setUnit(AmountType.DOLLAR.code);
        productTypeMarkupDTO.setMarkup("5");
        productTypeMarkupDTO.setProductType("product type name");

        markupGridDTO.setProductTypeMarkups(Collections.singletonList(productTypeMarkupDTO));
        markupGridDTO.setSubgroupMarkups(Collections.EMPTY_LIST);
        markupGridDTO.setItemMarkups(Collections.EMPTY_LIST);

        doReturn(XWPFTableRow).when(xwpfTable).createRow();

        target.createMarkupDataTable(markupGridDTO, xwpfTable);

        verify(documentBuildHelper).buildTableCell(2, 0, " Per Case", XWPFTableRow);
    }

    @Test
    public void shouldAddSubgroupDesc() throws Exception {

        MarkupGridDTO markupGridDTO = new MarkupGridDTO();
        String subgroupDesc = "subgroup desc";

        SubgroupMarkupDTO subgroupMarkupDTO = new SubgroupMarkupDTO();
        subgroupMarkupDTO.setMarkupType(1);
        subgroupMarkupDTO.setSubgroupDesc(subgroupDesc);
        subgroupMarkupDTO.setMarkup("10");
        subgroupMarkupDTO.setUnit(AmountType.DOLLAR.code);

        markupGridDTO.setItemMarkups(Collections.EMPTY_LIST);
        markupGridDTO.setSubgroupMarkups(Collections.singletonList(subgroupMarkupDTO));
        markupGridDTO.setProductTypeMarkups(Collections.EMPTY_LIST);

        doReturn(XWPFTableRow).when(xwpfTable).createRow();

        target.createMarkupDataTable(markupGridDTO, xwpfTable);

        verify(documentBuildHelper).buildTableCell(0, 0, subgroupDesc, XWPFTableRow);
        verify(documentBuildHelper).buildTableCell(1, 0, "$10", XWPFTableRow);

    }

    @Test
    public void shouldAddUnitInEndWhenMarkupIsPercentForSubgroup() throws Exception {

        MarkupGridDTO markupGridDTO = new MarkupGridDTO();
        String subgroupDesc = "subgroup desc";

        SubgroupMarkupDTO subgroupMarkupDTO = new SubgroupMarkupDTO();
        subgroupMarkupDTO.setMarkupType(1);
        subgroupMarkupDTO.setSubgroupDesc(subgroupDesc);
        subgroupMarkupDTO.setMarkup("10");
        subgroupMarkupDTO.setUnit(AmountType.PERCENT.code);

        markupGridDTO.setItemMarkups(Collections.EMPTY_LIST);
        markupGridDTO.setSubgroupMarkups(Collections.singletonList(subgroupMarkupDTO));
        markupGridDTO.setProductTypeMarkups(Collections.EMPTY_LIST);

        doReturn(XWPFTableRow).when(xwpfTable).createRow();

        target.createMarkupDataTable(markupGridDTO, xwpfTable);

        verify(documentBuildHelper).buildTableCell(0, 0, subgroupDesc, XWPFTableRow);
        verify(documentBuildHelper).buildTableCell(1, 0, "10%", XWPFTableRow);

    }
}
