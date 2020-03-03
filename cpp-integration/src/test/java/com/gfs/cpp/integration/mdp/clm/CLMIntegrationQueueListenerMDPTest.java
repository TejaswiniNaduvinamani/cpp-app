package com.gfs.cpp.integration.mdp.clm;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfs.cpp.common.dto.clm.ClmContractChangeEventDTO;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.component.clm.ClmContractTypeChecker;
import com.gfs.cpp.component.statusprocessor.ClmContractChangeProcessor;
import com.gfs.cpp.integration.mdp.clm.CLMIntegrationQueueListenerMDP;

@RunWith(MockitoJUnitRunner.class)
public class CLMIntegrationQueueListenerMDPTest {

    @InjectMocks
    private CLMIntegrationQueueListenerMDP target;
    @Mock
    private ClmContractChangeProcessor clmContractChangeProcessor;
    @Mock
    private TextMessage message;
    @Mock
    private ClmContractTypeChecker clmContractTypeChecker;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Captor
    private ArgumentCaptor<ClmContractChangeEventDTO> clmContractChangeEventCaptor;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(target, "objectMapper", objectMapper);
    }

    @Test
    public void shouldProcessMessage() throws Exception {

        StringBuilder builder = new StringBuilder("{");
        builder.append("\"Data\":{\"InstanceStatus\":\"Draft\",\"EntityTypeName\":\"Agreement\"},");
        builder.append("\"Action\":\"Published\",");
        builder.append("\"InstanceId\":\"6faa275f-442d-4d14-aead-690756c4f25a\",");
        builder.append("\"EventType\":\"ICMPOC_CPP\"");
        builder.append("}");

        String messageString = builder.toString();
        doReturn(messageString).when(message).getText();
        doReturn(true).when(clmContractTypeChecker).isPricingContractType("ICMPOC_CPP");

        target.onMessage(message);

        verify(clmContractChangeProcessor).processChangeEvent(clmContractChangeEventCaptor.capture());

        ClmContractChangeEventDTO actualClmContractChangeEvent = clmContractChangeEventCaptor.getValue();

        assertThat(actualClmContractChangeEvent.getAction(), equalTo("Published"));
        assertThat(actualClmContractChangeEvent.getAgreementId(), equalTo("6faa275f-442d-4d14-aead-690756c4f25a"));
        assertThat(actualClmContractChangeEvent.getContractType(), equalTo("ICMPOC_CPP"));

    }

    @Test
    public void shouldNotPrcoessWhenNotPricingAgreement() throws Exception {
        StringBuilder builder = new StringBuilder("{");
        builder.append("\"Data\":{\"InstanceStatus\":\"Draft\",\"EntityTypeName\":\"Agreement\"},");
        builder.append("\"Action\":\"Published\",");
        builder.append("\"InstanceId\":\"6faa275f-442d-4d14-aead-690756c4f25a\",");
        builder.append("\"EventType\":\"ICMPOC_CPP_NO\"");
        builder.append("}");

        String messageString = builder.toString();
        doReturn(messageString).when(message).getText();
        doReturn(false).when(clmContractTypeChecker).isPricingContractType("ICMPOC_CPP_NO");

        target.onMessage(message);

        verify(clmContractChangeProcessor, never()).processChangeEvent(any(ClmContractChangeEventDTO.class));
    }

    @Test(expected = CPPRuntimeException.class)
    public void shouldNotProcessWhenJmsMessageException() throws Exception {

        doThrow(JMSException.class).when(message).getText();

        target.onMessage(message);

        verify(clmContractChangeProcessor, never()).processChangeEvent(any(ClmContractChangeEventDTO.class));
    }

}
