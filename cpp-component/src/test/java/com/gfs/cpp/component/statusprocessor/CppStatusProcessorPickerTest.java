package com.gfs.cpp.component.statusprocessor;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.clm.ClmContractStatus;

@RunWith(MockitoJUnitRunner.class)
public class CppStatusProcessorPickerTest {

    @InjectMocks
    private CppStatusProcessorPicker target;

    @Mock
    private DraftStatusProcessor draftStatusProcessor;
    @Mock
    private HoldStatusProcessor holdStatusProcessor;
    @Mock
    private WaitingForApprovalStatusProcessor waitingForApprovalStatusProcessor;
    @Mock
    private ContractApprovedStatusProcessor contractApprovedStatusProcessor;
    @Mock
    private TerminatedStatusProcessor terminatedStatusProcessor;
    @Mock
    private CancelledStatusProcessor cancelledStatusProcessor;
    @Mock
    private ExpiredStatusProcessor expiredStatusProcessor;

    @Test
    public void shouldReturnDraftStatusProcessorWhenClmStatusIsDraft() throws Exception {
        assertThat(target.pickStatusChangeProcessor(ClmContractStatus.DRAFT.value), equalTo((CppStatusChangeProcessor) draftStatusProcessor));
    }

    @Test
    public void shouldReturnWaitingForApprovalStatusProcessorWhenClmStatusIsWaitingForApproval() throws Exception {
        assertThat(target.pickStatusChangeProcessor(ClmContractStatus.WAITING_FOR_APPROVAL.value),
                equalTo((CppStatusChangeProcessor) waitingForApprovalStatusProcessor));
    }

    @Test
    public void shouldReturnHoldStatusProcessorWhenClmStatusIsOnHold() throws Exception {
        assertThat(target.pickStatusChangeProcessor(ClmContractStatus.ON_HOLD.value), equalTo((CppStatusChangeProcessor) holdStatusProcessor));
    }

    @Test
    public void shouldReturnHoldStatusProcessorWhenClmStatusIsReviewPendingHold() throws Exception {
        assertThat(target.pickStatusChangeProcessor(ClmContractStatus.REVIEW_PENDING.value), equalTo((CppStatusChangeProcessor) holdStatusProcessor));
    }

    @Test
    public void shouldReturnContractApprovedStatusProcessorWhenClmStatusIsApproved() throws Exception {
        assertThat(target.pickStatusChangeProcessor(ClmContractStatus.APPROVED.value),
                equalTo((CppStatusChangeProcessor) contractApprovedStatusProcessor));
    }

    @Test
    public void shouldReturnContractApprovedStatusProcessorWhenClmStatusIsWaitingForInternalSignature() throws Exception {
        assertThat(target.pickStatusChangeProcessor(ClmContractStatus.WAITING_FOR_INTERNAL_SIGNATURE.value),
                equalTo((CppStatusChangeProcessor) contractApprovedStatusProcessor));
    }

    @Test
    public void shouldReturnContractApprovedStatusProcessorWhenClmStatusIsWaitingForExternalSignature() throws Exception {
        assertThat(target.pickStatusChangeProcessor(ClmContractStatus.WAITING_FOR_EXTERNAL_SIGNATURE.value),
                equalTo((CppStatusChangeProcessor) contractApprovedStatusProcessor));
    }

    @Test
    public void shouldReturnContractApprovedStatusProcessorWhenClmStatusIsExecuted() throws Exception {
        assertThat(target.pickStatusChangeProcessor(ClmContractStatus.EXECUTED.value),
                equalTo((CppStatusChangeProcessor) contractApprovedStatusProcessor));
    }

    @Test
    public void shouldReturnCancelledStatusProcessorWhenClmStatusIsCancelled() throws Exception {
        assertThat(target.pickStatusChangeProcessor(ClmContractStatus.CANCELLED.value), equalTo((CppStatusChangeProcessor) cancelledStatusProcessor));
    }

    @Test
    public void shouldReturnTerminatedStatusProcessorWhenClmStatusIsTerminated() throws Exception {
        assertThat(target.pickStatusChangeProcessor(ClmContractStatus.TERMINATED.value),
                equalTo((CppStatusChangeProcessor) terminatedStatusProcessor));
    }

    @Test
    public void shouldReturnNullWhenClmStatusIsDeleted() throws Exception {
        assertThat(target.pickStatusChangeProcessor(ClmContractStatus.DELETED.value), nullValue());
    }

    @Test
    public void shouldReturnExpiredStatusProcessorWhenClmStatusIsExpired() throws Exception {
        assertThat(target.pickStatusChangeProcessor(ClmContractStatus.EXPIRED.value), equalTo((CppStatusChangeProcessor) expiredStatusProcessor));
    }

}
