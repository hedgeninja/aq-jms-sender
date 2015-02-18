package edu.vt.uc;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@Stateless
public class JmsMessageSender implements Serializable {
	private static final long serialVersionUID = -5049186116920101912L;

//	@Resource
//	UserTransaction userTransaction;

	@Resource(mappedName = "java:/GenericJmsXA")
	protected ConnectionFactory connectionFactory;
	
	@Resource(mappedName = "java:/jms/queue/jms_text_que")
	protected Queue queue;

	Connection connection;
	
	Session session;

	public void doSend(String msgText) {
		try {
		try {
//			userTransaction.begin();
			if ((queue == null) || (connectionFactory == null)) {
				InitialContext ctx = new InitialContext();
				queue = (Queue)ctx.lookup("java:/jms/queue/jms_text_que");
				connectionFactory = (ConnectionFactory)ctx.lookup("java:/GenericJmsXA");
			}
			if (connection == null) {
				connection = connectionFactory.createConnection();
			}
			if (session == null) {
				session = connection.createSession(true, Session.SESSION_TRANSACTED);
			}
			TextMessage message = session.createTextMessage(msgText);
			
			MessageProducer producer = session.createProducer(queue);
			producer.send(message);
			producer.close();
			session.close();
			session = null;
			System.out.println("SENDER:  I have sent message: " + msgText);
//			userTransaction.commit();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		catch (JMSException e) {
			e.printStackTrace();
		}
		finally {
//			userTransaction.rollback();
		}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}
}
