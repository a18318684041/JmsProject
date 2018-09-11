package producer;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

@Service("ProducerService")
public class ProducerServiceImpl implements ProducerService{
	
	@Autowired
	JmsTemplate jmsTemplate;
	
	//队列模式
	@Resource(name="queueDestination")
	Destination destination;
	
	//主题模式
/*	@Resource(name="topicDestination")
	Destination destination;*/
		
	//发送消息
	public void sendMessage(final String message) {
		//第一个参数目的地，第二个参数消息创建者,使用jmsTemplate发送消息
		jmsTemplate.send(destination,new MessageCreator() {			
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(message);				
				return textMessage;
			}
		});
		System.out.println("发送消息:"+message);
	}

}
