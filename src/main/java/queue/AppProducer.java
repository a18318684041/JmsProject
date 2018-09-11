package queue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

//队列模式生产者
public class AppProducer {
	
	//默认端口：61616
	private static final String url = "tcp://127.0.0.1:61616";
	private static final String queueName = "queue-test";
	
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
		Destination destination = session.createQueue(queueName);
		//创建一个生产者
		MessageProducer messageProducer = session.createProducer(destination);
		
		for (int i = 0; i < 10; i++) {
			//创建消息
			TextMessage textMessage = session.createTextMessage("test"+i);
			messageProducer.send(textMessage);
			System.out.println("send success"+textMessage);
		}
		//关闭连接
		connection.close();
		
		
	}
	
}
