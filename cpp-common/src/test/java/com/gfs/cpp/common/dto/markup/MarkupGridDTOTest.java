package com.gfs.cpp.common.dto.markup;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.markup.ItemLevelMarkupDTO;
import com.gfs.cpp.common.dto.markup.MarkupGridDTO;
import com.gfs.cpp.common.dto.markup.ProductTypeMarkupDTO;
import com.gfs.cpp.common.dto.markup.SubgroupMarkupDTO;

@RunWith(MockitoJUnitRunner.class)
public class MarkupGridDTOTest {

    @InjectMocks
    private MarkupGridDTO dto;

    @Test
    public void testMethods() {
        List<ProductTypeMarkupDTO> markup = null;
        List<ItemLevelMarkupDTO> item = null;
        List<SubgroupMarkupDTO> subgroup = null;
        dto.setProductTypeMarkups(markup);
        dto.setMarkupName("Test");
        dto.setItemMarkups(item);
        dto.setGfsCustomerId("test");
        dto.setSubgroupMarkups(subgroup);

        final MarkupGridDTO actual = SerializationUtils.clone(dto);

        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));
        assertThat(actual.getProductTypeMarkups(), is(dto.getProductTypeMarkups()));
        assertThat(actual.getItemMarkups(), is(dto.getItemMarkups()));
        assertThat(actual.getMarkupName(), is(dto.getMarkupName()));
        assertThat(actual.getGfsCustomerId(), is(dto.getGfsCustomerId()));
        assertThat(actual.getSubgroupMarkups(), is(dto.getSubgroupMarkups()));
    }

}
