package com.gfs.cpp.component.statusprocessor;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.util.ContractPriceProfileStatus;

@RunWith(MockitoJUnitRunner.class)
public class CancelledStatusProcessorTest {

    @InjectMocks
    private CancelledStatusProcessor target;

    @Test
    public void shouldReturnTrueWhenCurrentCppStatusIsOnHold() throws Exception {
        assertThat(target.shouldUpdateStatus(ContractPriceProfileStatus.HOLD), equalTo(true));
    }

    @Test
    public void shouldReturnTrueWhenCurrentCppStatusIsWaitingForApproval() throws Exception {
        assertThat(target.shouldUpdateStatus(ContractPriceProfileStatus.WAITING_FOR_APPROVAL), equalTo(true));
    }

    @Test
    public void shouldReturnTrueWhenCurrentCppStatusIsContractApproved() throws Exception {
        assertThat(target.shouldUpdateStatus(ContractPriceProfileStatus.CONTRACT_APPROVED), equalTo(true));
    }

    @Test
    public void shouldReturnTrueWhenCurrentCppStatusIsDraft() throws Exception {
        assertThat(target.shouldUpdateStatus(ContractPriceProfileStatus.DRAFT), equalTo(true));
    }

    @Test
    public void shouldReturnFalseWhenCurrentCppStatusIsActivated() throws Exception {
        assertThat(target.shouldUpdateStatus(ContractPriceProfileStatus.CANCELLED), equalTo(false));
    }

    @Test
    public void shouldReturnDraftStatusForToUpdateStatus() throws Exception {
        assertThat(target.getUpdateToStatus(), equalTo(ContractPriceProfileStatus.CANCELLED));
    }

    @Test
    public void shouldReturUpdateUser() throws Exception {
        assertThat(target.getUpdateUserId(), equalTo(CancelledStatusProcessor.UPDATE_USER_ID));
    }

    @Test
    public void shouldReturnTrueForExpireRequiredAndPricingActivated() throws Exception {
        assertThat(target.expireRequired(ContractPriceProfileStatus.PRICING_ACTIVATED), equalTo(true));
    }

    @Test
    public void shouldReturnTrueeForExpireRequiredAndPricingNotActivated() throws Exception {
        assertThat(target.expireRequired(ContractPriceProfileStatus.DRAFT), equalTo(false));
    }

}
