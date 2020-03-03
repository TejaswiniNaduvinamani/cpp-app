package com.gfs.cpp.common.model.assignments;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.model.assignments.CustomerAssignmentDO;

@RunWith(MockitoJUnitRunner.class)
public class CustomerAssignmentDOTest {

    @InjectMocks
    private CustomerAssignmentDO dto;

    @Test
    public void testSaveAssignmentDO() {
        dto.setCppCustomerSeq(1);
        dto.setGfsCustomerId("1");
        dto.setGfsCustomerType(22);

        final CustomerAssignmentDO actual = SerializationUtils.clone(dto);

        assertThat(dto.equals(actual), is(true));
        assertThat(dto.hashCode(), is(actual.hashCode()));
        assertThat(dto.toString() != null, is(true));
        assertThat(dto.getCppCustomerSeq(), is(actual.getCppCustomerSeq()));
        assertThat(dto.getGfsCustomerId(), is(actual.getGfsCustomerId()));
        assertThat(dto.getGfsCustomerType(), is(actual.getGfsCustomerType()));
    }
}
