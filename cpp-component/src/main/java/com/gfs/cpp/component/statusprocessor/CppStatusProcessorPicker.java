package com.gfs.cpp.component.statusprocessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gfs.cpp.common.dto.clm.ClmContractStatus;

@Component
public class CppStatusProcessorPicker {

    @Autowired
    private DraftStatusProcessor draftStatusProcessor;
    @Autowired
    private HoldStatusProcessor holdStatusProcessor;
    @Autowired
    private WaitingForApprovalStatusProcessor waitingForApprovalStatusProcessor;
    @Autowired
    private ContractApprovedStatusProcessor contractApprovedStatusProcessor;
    @Autowired
    private TerminatedStatusProcessor terminatedStatusProcessor;
    @Autowired
    private CancelledStatusProcessor cancelledStatusProcessor;
    @Autowired
    private ExpiredStatusProcessor expiredStatusProcessor;

    public CppStatusChangeProcessor pickStatusChangeProcessor(String clmStatus) {

        if (ClmContractStatus.DRAFT.value.equals(clmStatus)) {
            return draftStatusProcessor;
        } else if (ClmContractStatus.WAITING_FOR_APPROVAL.value.equals(clmStatus)) {
            return waitingForApprovalStatusProcessor;
        } else if (ClmContractStatus.REVIEW_PENDING.value.equals(clmStatus) || ClmContractStatus.ON_HOLD.value.equals(clmStatus)) {
            return holdStatusProcessor;
        } else if (anyApprovedStatus(clmStatus)) {
            return contractApprovedStatusProcessor;
        } else if (ClmContractStatus.CANCELLED.value.equals(clmStatus)) {
            return cancelledStatusProcessor;
        } else if (ClmContractStatus.TERMINATED.value.equals(clmStatus)) {
            return terminatedStatusProcessor;
        } else if (ClmContractStatus.EXPIRED.value.equals(clmStatus)) {
            return expiredStatusProcessor;
        }
        return null;

    }

    private boolean anyApprovedStatus(String clmStatus) {
        return ClmContractStatus.APPROVED.value.equals(clmStatus) || ClmContractStatus.WAITING_FOR_EXTERNAL_SIGNATURE.value.equals(clmStatus)
                || ClmContractStatus.WAITING_FOR_INTERNAL_SIGNATURE.value.equals(clmStatus) || ClmContractStatus.EXECUTED.value.equals(clmStatus);
    }

}
