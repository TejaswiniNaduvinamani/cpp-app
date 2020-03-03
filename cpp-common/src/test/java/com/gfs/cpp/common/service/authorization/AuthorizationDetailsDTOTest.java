package com.gfs.cpp.common.service.authorization;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import com.gfs.cpp.common.service.authorization.AuthorizationDetailsDTO;

public class AuthorizationDetailsDTOTest {

    private AuthorizationDetailsDTO target = new AuthorizationDetailsDTO();

    @Test
    public void shouldValidateAuthorizationDetailsDTO() throws Exception {

        target.setCustomerAssignmentEditable(true);
        target.setPriceProfileEditable(true);
        target.setPowerUser(true);
        target.setCppStatus("Draft");
        target.setItemAssignmentEditable(true);
        target.setCostModelEditable(true);

        AuthorizationDetailsDTO actual = SerializationUtils.clone(target);

        assertThat(actual, equalTo(target));
        assertThat(actual.hashCode(), equalTo(target.hashCode()));
        assertThat(actual.toString(), notNullValue());

        assertThat(actual.isPriceProfileEditable(), equalTo(true));
        assertThat(actual.isCustomerAssignmentEditable(), equalTo(true));
        assertThat(actual.isItemAssignmentEditable(), equalTo(true));
        assertThat(actual.isCostModelEditable(), equalTo(true));
        assertThat(actual.getIsPowerUser(), equalTo(true));
        assertThat(actual.getCppStatus(), equalTo("Draft"));
    }

}
