package com.gfs.cpp.common.dto.assignments;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.assignments.ItemAssignmentValidationDTO;

@RunWith(MockitoJUnitRunner.class)
public class ItemAssignmentValidationDTOTest {

    @InjectMocks
    private ItemAssignmentValidationDTO dto;

    @Test
    public void testMethods() {
        dto.setIsItemAlreadyExist(true);
        dto.setDuplicateItemIdList(new ArrayList<String>());

        final ItemAssignmentValidationDTO actual = SerializationUtils.clone(dto);
        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));

        assertThat(dto.getIsItemAlreadyExist(), is(actual.getIsItemAlreadyExist()));
        assertThat(dto.getDuplicateItemIdList(), is(actual.getDuplicateItemIdList()));
    }

}
