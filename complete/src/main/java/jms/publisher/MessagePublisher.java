package jms.publisher;

/**
 * Created by Ashkan on 6/19/2015.
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

public class MessagePublisher {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendMessage(final String message) {
        System.out.println("\n The MessagePublisher publish JSON result into 'IN_TOPIC' topic:" + message + "\n");
        jmsTemplate.convertAndSend(message);
    }

}
