package producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AppProducer {
			
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:producer.xml");
		ProducerService producerService = context.getBean(ProducerService.class);
		for (int i = 0; i < 10; i++) {
			//发送消息
			producerService.sendMessage("queue-test"+i);
		}
		//关闭连接
		context.close();
	}

}
