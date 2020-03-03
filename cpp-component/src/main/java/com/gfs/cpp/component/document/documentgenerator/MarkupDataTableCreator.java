package com.gfs.cpp.component.document.documentgenerator;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.corp.component.price.common.types.AmountType;
import com.gfs.cpp.common.constants.CPPConstants;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.MarkupGridDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.dto.markup.SubgroupMarkupDTO;
import com.gfs.cpp.common.util.MarkupTypeEnum;

@Component
public class MarkupDataTableCreator {

    @Autowired
    private DocumentBuildHelper documentBuildHelper;

    public void createMarkupDataTable(MarkupGridDTO markupGridDTO, XWPFTable table) {
        createProductTable(markupGridDTO, table);
        createSubgroupTable(markupGridDTO, table);
        createItemTable(markupGridDTO, table);
    }

    private void createSubgroupTable(MarkupGridDTO markupGridDTO, XWPFTable table) {
        if (CollectionUtils.isNotEmpty(markupGridDTO.getSubgroupMarkups())) {
            for (SubgroupMarkupDTO subGroupMarkupDTO : markupGridDTO.getSubgroupMarkups()) {
                createMarkupTable(table, subGroupMarkupDTO.getSubgroupDesc(), subGroupMarkupDTO.getUnit(), subGroupMarkupDTO.getMarkup(),
                        subGroupMarkupDTO.getMarkupType());
            }
        }
    }

    private void createItemTable(MarkupGridDTO markupGridDTO, XWPFTable table) {
        if (CollectionUtils.isNotEmpty(markupGridDTO.getItemMarkups())) {
            for (ItemLevelMarkupDTO itemLevelMarkupDTO : markupGridDTO.getItemMarkups()) {
                createMarkupTable(table, itemLevelMarkupDTO.getItemDesc(), itemLevelMarkupDTO.getUnit(), itemLevelMarkupDTO.getMarkup(),
                        itemLevelMarkupDTO.getMarkupType());
            }
        }
    }

    private void createProductTable(MarkupGridDTO markupGridDTO, XWPFTable table) {

        for (ProductTypeMarkupDTO productTypeMarkupDTO : markupGridDTO.getProductTypeMarkups()) {
            XWPFTableRow productRow = table.createRow();
            documentBuildHelper.buildTableCell(0, 0, WordUtils.capitalizeFully(productTypeMarkupDTO.getProductType()) + CPPConstants.DOCX_CATEGORY,
                    productRow);
            if ((AmountType.PERCENT.getCode()).equals(productTypeMarkupDTO.getUnit())) {
                documentBuildHelper.buildTableCell(1, 0, productTypeMarkupDTO.getMarkup() + productTypeMarkupDTO.getUnit(), productRow);
            } else {
                documentBuildHelper.buildTableCell(1, 0, productTypeMarkupDTO.getUnit() + productTypeMarkupDTO.getMarkup(), productRow);
            }
            documentBuildHelper.buildTableCell(2, 0, " " + MarkupTypeEnum.getNameByCode(productTypeMarkupDTO.getMarkupType()), productRow);
        }
    }

    private void createMarkupTable(XWPFTable table, String itemDesc, String unit, String markup, int markupType) {
        XWPFTableRow itemRow = table.createRow();
        documentBuildHelper.buildTableCell(0, 0, itemDesc, itemRow);
        if ((AmountType.PERCENT.getCode()).equals(unit)) {
            documentBuildHelper.buildTableCell(1, 0, markup + unit, itemRow);

        } else {
            documentBuildHelper.buildTableCell(1, 0, unit + markup, itemRow);
        }
        documentBuildHelper.buildTableCell(2, 0, " " + MarkupTypeEnum.getNameByCode(markupType), itemRow);
    }
}
