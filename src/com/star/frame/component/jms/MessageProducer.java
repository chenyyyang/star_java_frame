package com.star.frame.component.jms;

import java.io.Serializable;

//import javax.jms.Destination;
//import javax.jms.JMSException;
//import javax.jms.Message;
//import javax.jms.Session;

public class MessageProducer implements Serializable {

	private static final long serialVersionUID = 5159372177821106929L;

	public void send(final String message) {

//		Destination destination = SpringContextLoader.getBean("topicDestinatin", Destination.class);
//		JmsTemplate template = SpringContextLoader.getBean(JmsTemplate.class);
//
//		template.send(destination, new MessageCreator() {
//			public Message createMessage(Session session) throws JMSException {
//				Message m = session.createObjectMessage(message);
//				return m;
//			}
//		});
	}

}
