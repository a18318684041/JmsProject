package consumer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class ConsumerMessageListener implements MessageListener{

	//接收信息
	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		try {
			System.out.println("接收到信息:"+textMessage.getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}	
	}
	
}
