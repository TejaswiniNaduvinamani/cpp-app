package com.gfs.cpp.component.distributioncenter;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.component.distributioncenter.DistributionServiceException;
import com.gfs.cpp.component.distributioncenter.DistributionServiceFaultDetail;

@RunWith(MockitoJUnitRunner.class)
public class DistributionServiceExceptionTest {
    
    @InjectMocks
    DistributionServiceException distributionServiceException;
    
    @Mock
    DistributionServiceFaultDetail faultDetail;
    
    @Test
    public void testMethods() {
        new DistributionServiceException(this.faultDetail);
        assertTrue(null!=distributionServiceException.getFaultInfo());
    }
    
    

}
