package com.gfs.cpp.component.statusprocessor;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.util.ContractPriceProfileStatus;
import com.gfs.cpp.data.contractpricing.ContractPriceProfileRepository;

@RunWith(MockitoJUnitRunner.class)
public class ContractApprovedStatusProcessorTest {

    @InjectMocks
    private ContractApprovedStatusProcessor target;

    @Mock
    private ContractPriceProfileRepository contractPriceProfileRepository;

    @Test
    public void shouldReturnTrueWhenCurrentCppStatusIsOnHold() throws Exception {
        assertThat(target.shouldUpdateStatus(ContractPriceProfileStatus.HOLD), equalTo(true));
    }

    @Test
    public void shouldReturnTrueWhenCurrentCppStatusIsWaitingForApproval() throws Exception {
        assertThat(target.shouldUpdateStatus(ContractPriceProfileStatus.WAITING_FOR_APPROVAL), equalTo(true));
    }

    @Test
    public void shouldReturnTrueWhenCurrentCppStatusIsDraft() throws Exception {
        assertThat(target.shouldUpdateStatus(ContractPriceProfileStatus.DRAFT), equalTo(true));
    }

    @Test
    public void shouldReturnTrueWhenCurrentCppStatusIsCancelled() throws Exception {
        assertThat(target.shouldUpdateStatus(ContractPriceProfileStatus.CANCELLED), equalTo(true));
    }

    @Test
    public void shouldReturnFalseWhenCurrentCppStatusIsActivated() throws Exception {
        assertThat(target.shouldUpdateStatus(ContractPriceProfileStatus.PRICING_ACTIVATED), equalTo(false));
    }

    @Test
    public void shouldReturnDraftStatusForToUpdateStatus() throws Exception {
        assertThat(target.getUpdateToStatus(), equalTo(ContractPriceProfileStatus.CONTRACT_APPROVED));
    }

    @Test
    public void shouldReturUpdateUser() throws Exception {
        assertThat(target.getUpdateUserId(), equalTo(ContractApprovedStatusProcessor.UPDATE_USER_ID));
    }

    @Test
    public void shouldReturnFalseForExpireRequired() throws Exception {
        assertThat(target.expireRequired(ContractPriceProfileStatus.CONTRACT_APPROVED), equalTo(false));
    }

}
