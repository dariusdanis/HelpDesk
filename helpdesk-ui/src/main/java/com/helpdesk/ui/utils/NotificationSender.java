package com.helpdesk.ui.utils;

import java.io.IOException;
import java.util.List;

import org.apache.wicket.Application;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.protocol.ws.api.IWebSocketConnection;
import org.apache.wicket.protocol.ws.api.IWebSocketConnectionRegistry;
import org.apache.wicket.protocol.ws.api.SimpleWebSocketConnectionRegistry;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.helpdesk.domain.entity.UserEntity;
import com.helpdesk.domain.service.NotificationService;
import com.helpdesk.ui.BasePage;

public class NotificationSender {

	@SpringBean
	protected NotificationService notificationService;
	
	public NotificationSender() {
		Injector.get().inject(this);
	}

	public void sendToUser(final UserEntity userEntity, final String message, 
			final List<String> messages, final Application application) {
		new Thread() {
			
			 @Override
	            public void run() {
				 IWebSocketConnection connection = getConnection(userEntity, messages, application);
					if (connection != null && connection.isOpen()) {
						try {
							connection.sendMessage(message);
						} catch (IOException e) {}
					}
			 }
			 
		}.start();
	}
	
	private IWebSocketConnection getConnection(UserEntity userEntity, List<String> messages, 
			Application application) {
		IWebSocketConnectionRegistry registry = new SimpleWebSocketConnectionRegistry();
		List<HelpDeskSession> helpDeskSessions = HelpDeskSession.getHelpDeskSessions();
		for (HelpDeskSession session : helpDeskSessions) {
			if (session.getUser().getId() == userEntity.getId()) {
				messages.clear();
				messages.add(Long.toString(notificationService.getCount(session.getUser())));
				return registry.getConnection(application, 
						session.getId(), BasePage.pageMap.get(session.getId()));
			}
		}
		return null;
	}
	
}
