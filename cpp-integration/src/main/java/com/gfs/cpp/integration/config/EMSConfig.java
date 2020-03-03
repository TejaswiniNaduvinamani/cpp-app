package com.gfs.cpp.integration.config;

import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.QueueConnectionFactory;
import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.jms.support.destination.JndiDestinationResolver;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.jndi.JndiTemplate;

import com.gfs.cpp.integration.mdp.clm.CLMIntegrationQueueListenerMDP;
import com.gfs.util.spring.retry.RetryMessageListenerAdapter;

@Configuration
public class EMSConfig {

    @Value("${clm.integration.cpp.queue}")
    private String mainQueueName;
    @Value("${clm.integration.cpp.retry.queue}")
    private String retryQueueName;
    @Value("${clm.integration.cpp.error.queue}")
    private String errorQueueName;
    @Value("${clm.integration.cpp.context.factory}")
    private String emsContextFactory;
    @Value("${jms.provider.url}")
    private String emsUrl;

    @Bean
    @Primary
    public JndiTemplate jndiTemplate() {
        JndiTemplate jndiTemplate = new JndiTemplate();

        Properties properties = new Properties();
        properties.setProperty("java.naming.factory.initial", emsContextFactory);
        properties.setProperty("java.naming.provider.url", emsUrl);
        jndiTemplate.setEnvironment(properties);

        return jndiTemplate;
    }

    @Bean
    @Primary
    public JndiDestinationResolver jmsDestinationResolver(JndiTemplate jmsJndiTemplate) {
        JndiDestinationResolver resolver = new JndiDestinationResolver();
        resolver.setCache(true);
        resolver.setJndiTemplate(jmsJndiTemplate);
        resolver.setResourceRef(false);

        return resolver;
    }

    @Bean
    @Primary
    public ConnectionFactory jmsConnectionFactory(JndiTemplate jmsJndiTemplate) throws NamingException {
        JndiObjectFactoryBean factory = new JndiObjectFactoryBean();
        factory.setJndiTemplate(jmsJndiTemplate);
        factory.setJndiName("QueueConnectionFactory");
        factory.setLookupOnStartup(false);
        factory.setProxyInterface(QueueConnectionFactory.class);

        factory.afterPropertiesSet();

        return (ConnectionFactory) factory.getObject();
    }

    @Bean("messageListenerContainer")
    public MessageListenerContainer messageListenerContainer(JndiDestinationResolver jndiDestinationResolver, ConnectionFactory connectionFactory,
            RetryMessageListenerAdapter retryMessageListener) {
        DefaultMessageListenerContainer listener = new DefaultMessageListenerContainer();
        listener.setDestinationResolver(jndiDestinationResolver);
        listener.setConcurrentConsumers(1); // Adjust as needed
        listener.setMaxConcurrentConsumers(5); // Adjust as needed
        listener.setSessionTransacted(true);
        listener.setPubSubDomain(false);

        listener.setDestinationName(mainQueueName);
        listener.setConnectionFactory(connectionFactory);
        listener.setMessageListener(retryMessageListener);

        return listener;
    }

    @Bean("retryMessageListenerContainer")
    public MessageListenerContainer retryMessageListenerContainer(JndiDestinationResolver jndiDestinationResolver,
            ConnectionFactory connectionFactory, RetryMessageListenerAdapter retryMessageListener) {
        DefaultMessageListenerContainer listener = new DefaultMessageListenerContainer();
        listener.setDestinationResolver(jndiDestinationResolver);
        listener.setConnectionFactory(connectionFactory);
        listener.setSessionTransacted(true);
        listener.setPubSubDomain(false);

        listener.setDestinationName(retryQueueName);
        listener.setMessageListener(retryMessageListener);

        listener.setConcurrentConsumers(1);
        listener.setMaxConcurrentConsumers(5);
        listener.setReceiveTimeout(60000L);
        listener.setIdleTaskExecutionLimit(5);

        return listener;
    }

    @Bean
    public RetryMessageListenerAdapter retryMessageListenerAdapter(JndiDestinationResolver jndiDestinationResolver,
            CLMIntegrationQueueListenerMDP listenerMDP) {
        RetryMessageListenerAdapter retry = new RetryMessageListenerAdapter();
        retry.setDestinationResolver(jndiDestinationResolver);
        retry.setRetryFor("RuntimeException");
        retry.setDelay(60000);
        retry.setAttempts(3);

        retry.setDelegate(listenerMDP);
        retry.setRetryQueueName(retryQueueName);
        retry.setErrorQueueName(errorQueueName);

        return retry;
    }

}
