package com.star.frame.component.jms.test;

import com.star.frame.component.jms.MessageProducer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class JmsSenderApp {

	public static void main(String[] args) {

		ApplicationContext ac = new FileSystemXmlApplicationContext("applicationContext.xml");

		MessageProducer producer = ac.getBean(MessageProducer.class);
		System.out.println("begin");
		producer.send("=================");
		System.out.println("end");
	}
}
