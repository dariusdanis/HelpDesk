package com.helpdesk.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnLoadHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.protocol.ws.api.IWebSocketConnection;
import org.apache.wicket.protocol.ws.api.IWebSocketConnectionRegistry;
import org.apache.wicket.protocol.ws.api.SimpleWebSocketConnectionRegistry;
import org.apache.wicket.protocol.ws.api.WebSocketBehavior;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.helpdesk.domain.entity.UserEntity;
import com.helpdesk.domain.service.NotificationService;
import com.helpdesk.ui.user.AddEmployeePage;
import com.helpdesk.ui.user.AddRequestPage;
import com.helpdesk.ui.user.HomePage;
import com.helpdesk.ui.utils.HelpDeskSession;

public class BasePage extends WebPage {
	private static final long serialVersionUID = 1L;
	
	public static Map<String, Integer> pageMap = new HashMap<String, Integer>();
	
	public static final int MIN_LENGTH = 5;
	public static final int MAX_LENGTH= 40;
	public static final int MAX_LENGTH_TAREA = 9000;
	
	@SpringBean
	protected NotificationService notificationService;
	
	@Override
	public void renderHead(IHeaderResponse response){
		super.renderHead(response);	   
		String javaScript = "Wicket.Event.subscribe('/websocket/message', function(jqEvent, counter) {" +
				"updateNotificationCounter(jqEvent, counter);" +
				"});";
		response.render( OnLoadHeaderItem.forScript( javaScript));
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		pageMap.put(getSession().getId(), this.getPageId());
		RepeatingView repeatingView = new RepeatingView("menuItems");
		repeatingView.add(initLink(repeatingView.newChildId(), AddEmployeePage.class, "Add Employee"));
		repeatingView.add(initLink(repeatingView.newChildId(), AddRequestPage.class, "Create Request"));
		add(new Label("notificationCounter", notificationService.getCount(getLoggedUser())));
		add(initHomeLink("home"));
		add(initLogOffLink("logOff"));
		add(repeatingView);
		add(initWebSocket());
	}
	
	private Link<Object> initLink(String wicketId, final Class<? extends BasePage> page, String label) {
		Link<Object> link = new Link<Object>(wicketId) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(page);
			}
			
		};
		link.add(new Label("label", label));
		link.setVisible(show(page));
		return link;
	}
	
	private Link<Object> initHomeLink(String wicketId) {
		Link<Object> link = new Link<Object>(wicketId) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(HomePage.class);
			}
			
		};
		return link;
	}
	
	private WebSocketBehavior initWebSocket() {
		return new WebSocketBehavior() {
			private static final long serialVersionUID = 1L;	
		};
	}

	public void sendToRole(String message, String role){	
		Collection<IWebSocketConnection> wsConnections = getConnectedClients(role);
		for( IWebSocketConnection wsConnection : wsConnections){
			if (wsConnection != null && wsConnection.isOpen()) {
				try {
					wsConnection.sendMessage(message);
				} catch (IOException e) {}
			}
		}
	}
	
	private Collection<IWebSocketConnection> getConnectedClients(String role){
		Collection<IWebSocketConnection> connections = new ArrayList<IWebSocketConnection>();
		IWebSocketConnectionRegistry registry = new SimpleWebSocketConnectionRegistry();
		List<HelpDeskSession> helpDeskSessions = HelpDeskSession.getHelpDeskSessions();
		IWebSocketConnection userConnection = null;
		for (HelpDeskSession session : helpDeskSessions) {
			if (session.getUser().getRoleEntity().getRole().equals(role)) {
				userConnection = registry.getConnection(getApplication(), session.getId(), pageMap.get(session.getId()));
				if (userConnection != null) {
					connections.add(userConnection);
					userConnection = null;
				}
			}
		}
		return connections;	
	}
	
	private Link<Object> initLogOffLink(String wicketId) {	
		return new Link<Object>(wicketId) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				((HelpDeskSession) getSession()).invalidate();
				setResponsePage(SingInPage.class);
			}
			
		};
	}
	
	private boolean show(Class<? extends BasePage> page) {
		return true;
	}
	
	public Roles getRole() {
		return ((HelpDeskSession) getSession()).getRoles();
	}
	
	
	/*
	 * Custom validation. Why? Because I CAN!!!!
	 */
	public List<Object> validateForm(Form<?> form, Object... objects) {
		List<Object> list = new ArrayList<Object>();
		String errorMessage = null;
		Integer counter = 0;
		for (Object obj : objects) {
			if (obj == null) {
				errorMessage = "Field are requed!";
				break;
			} else if (obj instanceof String && (errorMessage = checkClass(obj, form, counter)) != null) {
				break;
			}
			counter++;
		}
		if (errorMessage == null) {
			return null;
		} else {
			list.add(counter);
			list.add(errorMessage);
			return list;
		}
	}
	
	private String checkClass(Object obj, Form<?> form, Integer index) {
		if (form.get(index) instanceof TextField) {
			return checkLength((String) obj, MIN_LENGTH, MAX_LENGTH) ? 
					"DOTO LATER: minLength " + MIN_LENGTH + " maxLength " + MAX_LENGTH : null;
		} else if (form.get(index) instanceof TextArea) {
			return checkLength((String) obj, MIN_LENGTH, MAX_LENGTH_TAREA) ? 
					"DOTO LATER: minLength " + MIN_LENGTH + " maxLength " + MAX_LENGTH_TAREA : null;
		}
		return null;
	}
	
	private boolean checkLength (String data, int minLength, int maxLength) {
		return data.length() < minLength || data.length() > maxLength;
	}
	
	public UserEntity getLoggedUser() {
		return ((HelpDeskSession)getSession()).getUser();
	}
	
}
