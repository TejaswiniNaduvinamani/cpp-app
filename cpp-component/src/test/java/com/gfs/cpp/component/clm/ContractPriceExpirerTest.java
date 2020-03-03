package com.gfs.cpp.component.clm;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.util.CPPDateUtils;
import com.gfs.cpp.component.clm.ContractPriceExpirer;
import com.gfs.cpp.component.contractpricing.ContractPriceProfileExpirer;
import com.gfs.cpp.component.contractpricing.CustomerItemPriceExpirer;

@RunWith(MockitoJUnitRunner.class)
public class ContractPriceExpirerTest {

    private static final String UPDATE_USER_ID = "update user";

    @InjectMocks
    private ContractPriceExpirer target;

    @Mock
    private ContractPriceProfileExpirer contractPriceProfileExpirer;
    @Mock
    private CustomerItemPriceExpirer customerItemPriceExpirer;
    @Mock
    private CPPDateUtils cppDateUtils;

    @Test
    public void shouldExpireAllPriceProfileData() throws Exception {

        int contractPriceProfileSeq = -101;
        Date expirationDate = new Date();
        doReturn(expirationDate).when(cppDateUtils).oneDayPreviousCurrentDate();

        target.expirePriceForContract(contractPriceProfileSeq, UPDATE_USER_ID);

        verify(contractPriceProfileExpirer).expireAllNonCmgPriceProfileDataForContract(contractPriceProfileSeq, expirationDate, UPDATE_USER_ID);

    }

    @Test
    public void shouldExpireCustomerItemPriceForAllContract() throws Exception {

        int contractPriceProfileSeq = -101;
        Date expirationDate = new Date();

        doReturn(expirationDate).when(cppDateUtils).oneDayPreviousCurrentDate();

        target.expirePriceForContract(contractPriceProfileSeq, UPDATE_USER_ID);

        verify(customerItemPriceExpirer).expireNonCMGCustomerItemPrice(contractPriceProfileSeq, expirationDate, UPDATE_USER_ID);

    }

    @Test
    public void shouldExpirePriceProfileForCPPSequence() throws Exception {

        int contractPriceProfileSeq = -101;
        Date expirationDate = new Date();
        target.expirePriceProfileForCPPSequence(contractPriceProfileSeq, expirationDate, UPDATE_USER_ID);
        verify(contractPriceProfileExpirer).expireAllNonCmgPriceProfileDataForContract(contractPriceProfileSeq, expirationDate, UPDATE_USER_ID);
    }

    @Test
    public void shouldExpireCustomerItemPricingForCPPSequence() throws Exception {

        int contractPriceProfileSeq = -101;
        Date expirationDate = new Date();

        target.expireCustomerItemPricingForCPPSequence(contractPriceProfileSeq, expirationDate, UPDATE_USER_ID);

        verify(customerItemPriceExpirer).expireNonCMGCustomerItemPrice(contractPriceProfileSeq, expirationDate, UPDATE_USER_ID);

    }

}
