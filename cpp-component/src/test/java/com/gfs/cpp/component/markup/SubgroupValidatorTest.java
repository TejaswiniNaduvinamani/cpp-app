package com.gfs.cpp.component.markup;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.corp.component.price.common.types.ItemPriceLevel;
import com.gfs.cpp.common.exception.CPPExceptionType;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.component.markup.SubgroupValidator;
import com.gfs.cpp.data.markup.CustomerItemPriceRepository;

@RunWith(MockitoJUnitRunner.class)
public class SubgroupValidatorTest {

    @InjectMocks
    private SubgroupValidator target;

    @Mock
    private CustomerItemPriceRepository customerItemPriceRepository;

    @Test
    public void shouldValidateIfSubgroupIsValid() {
        String subgroupId = "11";
        String subgroupDescription = "TABLETOP MISC";

        target.validateIfInvalidSubgroup(subgroupDescription, subgroupId);

    }

    @Test
    public void shouldValidateIfSubgroupIsInValid() {
        String subgroupId = "12";
        String subgroupDescription = null;
        try {
            target.validateIfInvalidSubgroup(subgroupDescription, subgroupId);
            fail("expected exception");
        } catch (CPPRuntimeException re) {
            assertThat(re.getType(), equalTo(CPPExceptionType.INVALID_SUBGROUP));
        }

    }

    @Test
    public void shouldValidateIfSubgroupDoesNotExist() {
        String subgroupId = "11";
        String cmgCustomerId = "12345";
        int cmgCustomerTypeCode = 2;
        int contractPriceProfileSeq = 1000;
        List<String> subgroupIdList = new ArrayList<>();
        subgroupIdList.add(subgroupId);

        when(customerItemPriceRepository.fetchAlreadyExistingItemsOrSubgroups(subgroupIdList, cmgCustomerId, cmgCustomerTypeCode, contractPriceProfileSeq,
                ItemPriceLevel.SUBGROUP.getCode())).thenReturn(null);

        target.validateIfSubgroupAlreadyExist(cmgCustomerId, cmgCustomerTypeCode, contractPriceProfileSeq, subgroupId);

        verify(customerItemPriceRepository).fetchAlreadyExistingItemsOrSubgroups(subgroupIdList, cmgCustomerId, cmgCustomerTypeCode, contractPriceProfileSeq,
                ItemPriceLevel.SUBGROUP.getCode());

    }

    @Test
    public void shouldValidateIfSubgroupAlreadyExist() {
        String subgroupId = "11";
        String cmgCustomerId = "12345";
        int cmgCustomerTypeCode = 2;
        int contractPriceProfileSeq = 1000;
        List<String> subgroupIdList = new ArrayList<>();
        subgroupIdList.add(subgroupId);

        when(customerItemPriceRepository.fetchAlreadyExistingItemsOrSubgroups(subgroupIdList, cmgCustomerId, cmgCustomerTypeCode, contractPriceProfileSeq,
                ItemPriceLevel.SUBGROUP.getCode())).thenReturn(subgroupIdList);

        try {
            target.validateIfSubgroupAlreadyExist(cmgCustomerId, cmgCustomerTypeCode, contractPriceProfileSeq, subgroupId);
            fail("expected exception");
        } catch (CPPRuntimeException re) {
            assertThat(re.getType(), equalTo(CPPExceptionType.SUBGROUP_ALREADY_EXIST));
            verify(customerItemPriceRepository).fetchAlreadyExistingItemsOrSubgroups(subgroupIdList, cmgCustomerId, cmgCustomerTypeCode,
                    contractPriceProfileSeq, ItemPriceLevel.SUBGROUP.getCode());

        }

    }

}
