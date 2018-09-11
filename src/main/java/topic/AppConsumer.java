package topic;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

//队列模式消费者
public class AppConsumer {

	//默认端口：61616
	private static final String url = "tcp://127.0.0.1:61616";
	private static final String topicName = "topic-test";
	
	public static void main(String[] args) throws JMSException {
		//创建连接工厂
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
		//创建connection
		Connection connection = connectionFactory.createConnection();
		//启动连接
		connection.start();
		//创建会话,false指不使用事务
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//创建一个目标
		Destination destination = session.createTopic(topicName);
		//创建消费者
		MessageConsumer messageConsumer = session.createConsumer(destination);
		//创建监听器(异步的过程，不能提前关闭)
		messageConsumer.setMessageListener(new MessageListener() {
			
			public void onMessage(Message message) {
				TextMessage textMessage = (TextMessage) message;
				try {
					System.out.println("receive message:"+textMessage.getText());
				} catch (JMSException e) {				
					e.printStackTrace();
				}
			}
			
		});
		//关闭连接
		//connection.close();		
	}

}
