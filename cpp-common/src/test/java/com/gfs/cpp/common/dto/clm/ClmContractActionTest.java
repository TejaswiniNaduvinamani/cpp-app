package com.gfs.cpp.common.dto.clm;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import org.junit.Test;

import com.gfs.cpp.common.dto.clm.ClmContractAction;

public class ClmContractActionTest {

    @Test
    public void shouldReturnActionByValue() throws Exception {
        ClmContractAction actual = ClmContractAction.getByValue("SentForApproval");
        assertThat(actual, equalTo(ClmContractAction.SENT_FOR_APPROVAL));
    }

}
