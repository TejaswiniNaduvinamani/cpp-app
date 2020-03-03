package com.gfs.cpp.component.assignment;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;

import java.util.Date;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.assignments.CustomerAssignmentDTO;
import com.gfs.cpp.common.dto.assignments.RealCustomerDTO;
import com.gfs.cpp.common.dto.contractpricing.ContractPricingResponseDTO;
import com.gfs.cpp.component.assignment.CustomerAssignmentBuilder;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;

@RunWith(MockitoJUnitRunner.class)
public class CustomerAssignmentBuilderTest {

    @InjectMocks
    private CustomerAssignmentBuilder target;

    @Mock
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Test
    public void shouldBuildCustomerAssignmentDto() throws Exception {

        String gfsCustomerId = "1232424";
        int gfsCustomerType = 3;
        int contractPriceProfileSeq = 101;
        String cmgCustomerId = "122";
        int cmgCustomerType = 31;
        int contractPriceProfileId = 201;
        Date clmContractStartDate = new LocalDate(2018, 01, 05).toDate();
        Date clmContractEndDate = new LocalDate(2019, 01, 05).toDate();

        ContractPricingResponseDTO contractPricingResponseDTO = new ContractPricingResponseDTO();
        contractPricingResponseDTO.setContractPriceProfileId(contractPriceProfileId);

        contractPricingResponseDTO.setClmContractStartDate(clmContractStartDate);
        contractPricingResponseDTO.setClmContractEndDate(clmContractEndDate);

        doReturn(contractPricingResponseDTO).when(contractPriceProfileRepository).fetchContractDetailsByCppSeq(contractPriceProfileSeq);

        CustomerAssignmentDTO actual = target.buildCustomerAssignmentDTO(gfsCustomerId, gfsCustomerType, contractPriceProfileSeq, cmgCustomerId,
                cmgCustomerType);

        assertThat(actual.getCmgCustomerId(), equalTo(cmgCustomerId));
        assertThat(actual.getCmgCustomerType(), equalTo(cmgCustomerType));
        assertThat(actual.getRealCustomerDTOList().size(), equalTo(1));
        assertThat(actual.getContractPriceProfileId(), equalTo(contractPriceProfileId));
        assertThat(actual.getClmContractStartDate(), equalTo(clmContractStartDate));
        assertThat(actual.getClmContractEndDate(), equalTo(clmContractEndDate));

        RealCustomerDTO actualRealCustomer = actual.getRealCustomerDTOList().get(0);
        assertThat(actualRealCustomer.getRealCustomerId(), equalTo(gfsCustomerId));
        assertThat(actualRealCustomer.getRealCustomerType(), equalTo(gfsCustomerType));

    }

}
