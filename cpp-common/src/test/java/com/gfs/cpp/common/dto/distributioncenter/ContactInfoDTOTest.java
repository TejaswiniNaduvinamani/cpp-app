package com.gfs.cpp.common.dto.distributioncenter;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.common.dto.distributioncenter.ContactInfoDTO;

@RunWith(MockitoJUnitRunner.class)
public class ContactInfoDTOTest {
    
    @InjectMocks
    private ContactInfoDTO dto;
    
    @Test
    public void testMethods() {
        dto.setEmailAddress("TEST");
        dto.setFaxNumber("TEST");
        dto.setPhoneNumber("TEST");
        assertTrue(null!=dto.getEmailAddress());
        assertTrue(null!=dto.getFaxNumber());
        assertTrue(null!=dto.getPhoneNumber());
    }
}
