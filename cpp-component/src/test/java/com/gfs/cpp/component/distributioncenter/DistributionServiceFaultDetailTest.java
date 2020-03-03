package com.gfs.cpp.component.distributioncenter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.gfs.cpp.component.distributioncenter.DistributionServiceFaultDetail;

@RunWith(MockitoJUnitRunner.class)
public class DistributionServiceFaultDetailTest {
    
    @InjectMocks
    DistributionServiceFaultDetail distributionServiceFaultDetail;
    
    @Test
    public void testMethods() {
        
        new DistributionServiceFaultDetail("error");
        distributionServiceFaultDetail.setErrorMessage("error");
        distributionServiceFaultDetail.setRequestID("1");
        final DistributionServiceFaultDetail actual = SerializationUtils.clone(distributionServiceFaultDetail);
        assertThat(actual.getErrorMessage(), is(distributionServiceFaultDetail.getErrorMessage()));
        assertThat(actual.getRequestID(), is(distributionServiceFaultDetail.getRequestID()));
    }
    
    

}
