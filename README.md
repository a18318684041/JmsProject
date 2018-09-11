# JmsProject

1、实现JMS与activemq的使用。

（1）实现了Point-to-Point Messaging Domain （点对点即P2P），队列模式。

    每个消息最好只有一个消费者，如果使用多个消费者，每个消费者只能随机接收其中的几条。
    
    消费者可以随时消费队列中的消息，没有时间依赖性。
    
    客户端包括生产者和消费者。

（2）实现了Publish/Subscribe Messaging Domain （发布/订阅模式即PUB/SUB），主题模式。  

    客户端包括发布者和订阅者。
    
    消费者不能接受到订阅之前发布的消息，消息接收具有时间依赖性。
    
    主题中的所有消息可以被所有消费者消费，不会随机分配。
 
 （3）生产者简单的调用过程：
    
    //创建连接工厂，创建对应中间键的连接工厂，这里使用的是activemq 的连接工厂
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
    
		//创建connection
		Connection connection = connectionFactory.createConnection();
    
		//启动连接
		connection.start();
    
		//创建会话,false指不使用事务
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    
		//创建一个目标（这里创建的是队列模式的目标，如要创建主题模式的可以使用createProducer方法）
		Destination destination = session.createQueue(queueName);
    
		//创建一个生产者
		MessageProducer messageProducer = session.createProducer(destination);
    
	  //向activemq发送10条信息
		for (int i = 0; i < 10; i++) {
			//创建消息
			TextMessage textMessage = session.createTextMessage("test"+i);
			messageProducer.send(textMessage);
			System.out.println("send success"+textMessage);
		}
		//关闭连接
		connection.close();
   
   （4） 消费者简单的调用过程：
   
    //创建连接工厂，创建对应中间键的连接工厂，这里使用的是activemq 的连接工厂
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
    
		//创建connection
		Connection connection = connectionFactory.createConnection();
    
		//启动连接
		connection.start();
    
		//创建会话,false指不使用事务
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    
		//创建一个目标（这里创建的是队列模式的目标，如要创建主题模式的可以使用createProducer方法）
		Destination destination = session.createQueue(queueName);
    
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
2、实现了JMS、activemq与spring的整合。
 
 （1）common.xml（生产者与消费者公共的配置代码）
   <!-- Activemq提供的ConnectionFactory -->
	<bean name="targetConnectionFactory" class="org.apache.activemq.ActiveMQConnectionFactory">
		<property name="brokerURL" value="tcp://127.0.0.1:61616"></property>
	</bean>
		
	<!-- SpringJms提供的 -->
	<bean id="connectionFactory" class="org.springframework.jms.connection.SingleConnectionFactory">
		<property name="targetConnectionFactory" ref="targetConnectionFactory"></property>
	</bean>
	
	<!-- 队列模式目的地，P2P -->
	<bean id="queueDestination" class="org.apache.activemq.command.ActiveMQQueue">
		<!-- 队列名字 -->
		<constructor-arg value="queue-springtest"></constructor-arg>
	</bean>
 
 （2）生产者代码
 
 	<context:component-scan base-package="producer"></context:component-scan>
	
	<import resource="common.xml"/>
			
	<!-- 开启注解 -->
	<context:annotation-config></context:annotation-config>	
	
	<!-- 配置jsmTemplate用于发送消息 -->
	<bean id="jmsTemplate" class="org.springframework.jms.core.JmsTemplate">
		<property name="connectionFactory" ref="connectionFactory"/>
	</bean>
  
  （3）消费者代码
  
  <!-- 开启注解 -->
	<context:annotation-config></context:annotation-config>	
  
	<import resource="classpath:common.xml"/>
	
	<!-- 配置消息监听器 -->
	<bean id="consumerMessageListener" class="consumer.ConsumerMessageListener"></bean>
  
	<!--  配置消息容器 -->
	<bean id="jmsContainer" class="org.springframework.jms.listener.DefaultMessageListenerContainer">
		<property name="connectionFactory" ref="connectionFactory"></property>	
    
		<!-- 队列模式 -->
		<property name="destination" ref="queueDestination"></proper ty>	
    
		<!-- 主题模式 -->
		<!-- <property name="destination" ref="topicDestination"></property>	 -->
		<property name="messageListener" ref="consumerMessageListener"></property>
	</bean>
