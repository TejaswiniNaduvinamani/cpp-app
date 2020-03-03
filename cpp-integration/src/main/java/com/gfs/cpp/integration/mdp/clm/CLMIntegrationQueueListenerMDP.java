package com.gfs.cpp.integration.mdp.clm;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfs.cpp.common.dto.clm.ClmContractChangeEventDTO;
import com.gfs.cpp.common.exception.CPPRuntimeException;
import com.gfs.cpp.component.clm.ClmContractTypeChecker;
import com.gfs.cpp.component.statusprocessor.ClmContractChangeProcessor;

@Component
public class CLMIntegrationQueueListenerMDP implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(CLMIntegrationQueueListenerMDP.class);
    @Autowired
    private ClmContractChangeProcessor clmContractChangeProcessor;
    @Autowired
    private ClmContractTypeChecker clmContractTypeChecker;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message) {

        TextMessage textMessage = (TextMessage) message;

        try {

            ClmContractChangeEventDTO clmContractChangeEvent = objectMapper.readValue(textMessage.getText(), ClmContractChangeEventDTO.class);

            if (isPricingContractType(clmContractChangeEvent.getContractType())) {
                clmContractChangeProcessor.processChangeEvent(clmContractChangeEvent);
                logger.info("Processing for contract id {} is completed ", clmContractChangeEvent.getAgreementId());
            }

        } catch (JMSException | IOException e) {
            logger.error("Error when processing message " + message.toString());
            throw new CPPRuntimeException("Error Processing text message", e);
        }

    }

    private boolean isPricingContractType(String contractType) {
        return clmContractTypeChecker.isPricingContractType(contractType);
    }

}
