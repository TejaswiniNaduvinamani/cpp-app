package com.gfs.cpp.component.statusprocessor;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.util.ContractPriceProfileStatus;

@RunWith(MockitoJUnitRunner.class)
public class ExpiredStatusProcessorTest {

    @InjectMocks
    private ExpiredStatusProcessor target;

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
    public void shouldReturnTrueWhenCurrentCppStatusIsCancelled() throws Exception {
        assertThat(target.shouldUpdateStatus(ContractPriceProfileStatus.CANCELLED), equalTo(true));
    }

    @Test
    public void shouldReturnFalseWhenCurrentCppStatusIsExpired() throws Exception {
        assertThat(target.shouldUpdateStatus(ContractPriceProfileStatus.EXPIRED), equalTo(false));
    }

    @Test
    public void shouldReturnExpiredStatusForToUpdateStatus() throws Exception {
        assertThat(target.getUpdateToStatus(), equalTo(ContractPriceProfileStatus.EXPIRED));
    }

    @Test
    public void shouldReturUpdateUser() throws Exception {
        assertThat(target.getUpdateUserId(), equalTo(ExpiredStatusProcessor.UPDATE_USER_ID));
    }

    @Test
    public void shouldReturnFalseForExpireRequired() throws Exception {
        assertThat(target.expireRequired(ContractPriceProfileStatus.CONTRACT_APPROVED), equalTo(true));
    }

}
