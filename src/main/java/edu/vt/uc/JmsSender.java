package edu.vt.uc;

import java.io.Serializable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Singleton
@Startup
public class JmsSender implements Serializable {
	private static final long serialVersionUID = 1276778980060017623L;
		
	private final ScheduledExecutorService scheduler =
		     Executors.newScheduledThreadPool(1);
	
	@Inject
	protected JmsMessageSender sender;
	
	private int count = 1;
	
	@PostConstruct
	public void init() {
		final Runnable msgSender = new Runnable() {
		       public void run() {
		    	 String msgText = "Hi there, this is message " + count++;
				 sender.doSend(msgText);
		       }
		};
		scheduler.scheduleAtFixedRate(msgSender, 500, 500, TimeUnit.MILLISECONDS);
	}
	
//	@Schedule(second="*/1", minute="*", hour="*")
//	public void sendMessage() {
//		String msgText = "Hi there, this is message " + count++;
//		sender.doSend(msgText);
//	}
}
