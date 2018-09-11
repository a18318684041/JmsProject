package producer;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.connection.SingleConnectionFactory;

public interface ProducerService {
	
	void sendMessage(String message);
	
}
