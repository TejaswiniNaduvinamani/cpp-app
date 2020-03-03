package com.gfs.cpp.common.dto.assignments;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.assignments.MarkupAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.RealCustomerDTO;
import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.dto.markup.SubgroupMarkupDTO;

@RunWith(MockitoJUnitRunner.class)
public class MarkupAssignmentDTOTest {

    @InjectMocks
    private MarkupAssignmentDTO dto;

    @Test
    public void testMethods() {
        List<ProductTypeMarkupDTO> markup = new ArrayList<>();
        List<RealCustomerDTO> realCustomerList = new ArrayList<>();
        dto.setGfsCustomerId("MARKUP_ID");
        dto.setMarkupList(markup);
        dto.setMarkupName("MARKUP_NAME");
        dto.setGfsCustomerType("MARKUP_TYPE");
        dto.setIsDefault(true);
        dto.setIsAssignmentSaved(true);
        dto.setRealCustomerDTOList(realCustomerList);
        dto.setGfsCustomerTypeId(1);
        dto.setItemList(new ArrayList<ItemLevelMarkupDTO>());
        dto.setSubgroupList(new ArrayList<SubgroupMarkupDTO>());
        dto.setExpireLowerInd(1);

        final MarkupAssignmentDTO actual = SerializationUtils.clone(dto);
        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));

        assertThat(dto.getGfsCustomerId(), is(actual.getGfsCustomerId()));
        assertThat(dto.getMarkupList(), is(actual.getMarkupList()));
        assertThat(dto.getMarkupName(), is(actual.getMarkupName()));
        assertThat(dto.getGfsCustomerType(), is(actual.getGfsCustomerType()));
        assertThat(dto.getIsDefault(), is(actual.getIsDefault()));
        assertThat(dto.getIsAssignmentSaved(), is(actual.getIsAssignmentSaved()));
        assertThat(dto.getRealCustomerDTOList(), is(actual.getRealCustomerDTOList()));
        assertThat(dto.getGfsCustomerTypeId(), is(actual.getGfsCustomerTypeId()));
        assertThat(dto.getItemList(), is(actual.getItemList()));
        assertThat(dto.getExpireLowerInd(), is(actual.getExpireLowerInd()));
        assertThat(dto.getSubgroupList(), is(actual.getSubgroupList()));
    }
}
