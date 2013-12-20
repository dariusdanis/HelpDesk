package com.helpdesk.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnLoadHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.protocol.ws.api.IWebSocketConnection;
import org.apache.wicket.protocol.ws.api.IWebSocketConnectionRegistry;
import org.apache.wicket.protocol.ws.api.SimpleWebSocketConnectionRegistry;
import org.apache.wicket.protocol.ws.api.WebSocketBehavior;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.helpdesk.domain.entity.NotificationEntity;
import com.helpdesk.domain.entity.UserEntity;
import com.helpdesk.domain.service.NotificationService;
import com.helpdesk.domain.service.UserService;
import com.helpdesk.ui.request.AddRequestPage;
import com.helpdesk.ui.request.RequestPage;
import com.helpdesk.ui.user.AddUserPage;
import com.helpdesk.ui.user.HomePage;
import com.helpdesk.ui.user.ProfilePage;
import com.helpdesk.ui.utils.Constants;
import com.helpdesk.ui.utils.HelpDeskSession;

public class BasePage extends WebPage {
	private static final long serialVersionUID = 1L;
	
	public static Map<String, Integer> pageMap = new HashMap<String, Integer>();
	
	private List<String> messages = new ArrayList<String>();
	
	@SpringBean
	protected NotificationService notificationService;
	
	@SpringBean
	private UserService userService;
	
	@Override
	public void renderHead(IHeaderResponse response){
		super.renderHead(response);	   
		String javaScript = "Wicket.Event.subscribe('/websocket/message', function(jqEvent, counter) {" +
				"updateNotificationCounter(jqEvent, counter);" +
				"});";
		response.render(OnLoadHeaderItem.forScript(javaScript));
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		if (!isSingIn()) return;
		pageMap.put(getSession().getId(), this.getPageId());
		RepeatingView notificationItems = new RepeatingView("notificationItems");
		notificationItems.setOutputMarkupId(true);
		WebMarkupContainer notificationConteiner = new WebMarkupContainer("notificationConteiner");
		notificationConteiner.add(notificationItems);
		notificationConteiner.setOutputMarkupId(true);
		
		RepeatingView menuItems = new RepeatingView("menuItems");
		menuItems.add(initLink(menuItems.newChildId(), ProfilePage.class, "My Profile"));
		menuItems.add(initLink(menuItems.newChildId(), AddUserPage.class, "Add Employee"));
		menuItems.add(initLink(menuItems.newChildId(), AddRequestPage.class, "Create Request"));
		add(initNotificationCounter("notificationCounter"));
		add(initNotificationsLink("notificationLink", notificationItems, notificationConteiner));
		add(initHomeLink("home"));
		add(initLogOffLink("logOff"));
		add(menuItems);
		add(initWebSocket());
		add(notificationConteiner);
	}

	private Label initNotificationCounter(String wicketId) {
		return new Label(wicketId, notificationService.getCount(getLoggedUser()));
	}

	private AjaxLink<Object> initNotificationsLink(String wicketId, final RepeatingView notificationItems, 
			final WebMarkupContainer notificationConteiner) {
		AjaxLink<Object> link = new AjaxLink<Object>(wicketId) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				notificationConteiner.removeAll();
				notificationItems.removeAll();
				for (NotificationEntity entity : notificationService.getNotificationByUser(getLoggedUser())) {
					notificationItems.add(initNotificationItem(notificationItems.newChildId(), entity));
				}
				notificationConteiner.add(notificationItems);
				target.add(notificationConteiner);
			}
		};
		link.add((new Label("notificationCounter", notificationService.getCount(getLoggedUser()))));
		return link;
	}

	private Link<Object> initNotificationItem(String wicketId, final NotificationEntity notificationEntity) {
		Link<Object> link = new Link<Object>(wicketId) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(RequestPage.class, RequestPage.parametersWith(notificationEntity.
						getRequestEntity().getId()));
				notificationService.remove(notificationEntity);
			}
			
		};
		link.add(new Label("notificationLabel", notificationEntity.getInfoEntity().getNotificationText()));
		return link;
	}

	private Link<Object> initLink(String wicketId, final Class<? extends BasePage> page, String label) {
		Link<Object> link = new Link<Object>(wicketId) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(page);
			}
			
		};
		link.add(new Label("menuLabel", label));
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

	public void sendToUser(UserEntity userEntity) {
		IWebSocketConnection connection = getConnection(userEntity);
		if (connection != null && connection.isOpen()) {
			try {
				connection.sendMessage(messages.get(0));
			} catch (IOException e) {}
		}
	}
	
	private IWebSocketConnection getConnection(UserEntity userEntity) {
		IWebSocketConnectionRegistry registry = new SimpleWebSocketConnectionRegistry();
		List<HelpDeskSession> helpDeskSessions = HelpDeskSession.getHelpDeskSessions();
		for (HelpDeskSession session : helpDeskSessions) {
			if (session.getUser().getId() == userEntity.getId()) {
				messages.clear();
				messages.add(Long.toString(notificationService.getCount(session.getUser())));
				return registry.getConnection(getApplication(), session.getId(), pageMap.get(session.getId()));
			}
		}
		return null;
	}

	public void sendToRole(final String role){
		int index = 0;
		Collection<IWebSocketConnection> wsConnections = getConnectedClients(role);		
		for( IWebSocketConnection wsConnection : wsConnections){
			if (wsConnection != null && wsConnection.isOpen()) {
				try {
					wsConnection.sendMessage(messages.get(index));
				} catch (IOException e) {}
				index++;
			}
		}
	}
	
	private Collection<IWebSocketConnection> getConnectedClients(String role){
		Collection<IWebSocketConnection> connections = new ArrayList<IWebSocketConnection>();
		IWebSocketConnectionRegistry registry = new SimpleWebSocketConnectionRegistry();
		List<HelpDeskSession> helpDeskSessions = HelpDeskSession.getHelpDeskSessions();
		IWebSocketConnection userConnection = null;
		messages.clear();
		for (HelpDeskSession session : helpDeskSessions) {
			if (session.getUser().getRoleEntity().getRole().equals(role)) {
				userConnection = registry.getConnection(getApplication(), session.getId(), pageMap.get(session.getId()));
				if (userConnection != null) {
					messages.add(Long.toString(notificationService.getCount(session.getUser())));
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
	
	public String getRole() {
		return getLoggedUser().getRoleEntity().getRole();
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
				errorMessage = Constants.REQUIRED;
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
			return checkLength((String) obj, Constants.MIN_LENGTH, Constants.MAX_LENGTH) ? 
					lengthMessage(Constants.MIN_LENGTH, Constants.MAX_LENGTH) : null;
		} else if (form.get(index) instanceof TextArea) {
			return checkLength((String) obj, Constants.MIN_LENGTH, Constants.MAX_LENGTH_TAREA) ? 
					lengthMessage(Constants.MIN_LENGTH, Constants.MAX_LENGTH_TAREA) : null;
		} else if (form.get(index) instanceof PasswordTextField) {
			return checkLength((String) obj, Constants.MIN_LENGTH, Constants.MAX_LENGTH) ? 
					lengthMessage(Constants.MIN_LENGTH, Constants.MAX_LENGTH) : null;
		}
		return null;
	}
	
	private boolean checkLength (String data, int minLength, int maxLength) {
		return data.length() < minLength || data.length() > maxLength;
	}
	
	public UserEntity getLoggedUser() {
		return ((HelpDeskSession)getSession()).getUser();
	}
	
	public boolean isSingIn() {
		return ((HelpDeskSession) getSession()).isSignedIn();
	}
	
	private String lengthMessage(int minLength, int maxLength) {
		return "DOTO LATER: minLength " + minLength + " maxLength " + maxLength;
	}
	
	public void appendJavaScript(AjaxRequestTarget target,  Form<?> form, Object index, Object message) {
		target.appendJavaScript("appendError('"+ form.get((Integer) index).getMarkupId() 
				+"','"+message.toString()+"');");
	}
	
	public HelpDeskSession getHDSession() {
		return ((HelpDeskSession) getSession());
	}
	
	public boolean client() {
		return getRole().equals(Constants.Roles.CLIEN.toString());
	}
	
	public boolean admin() {
		return getRole().equals(Constants.Roles.ADMIN.toString());
	}
	
	public boolean engineer() {
		return getRole().equals(Constants.Roles.ENGIN.toString());
	}
	
	public boolean director() {
		return getRole().equals(Constants.Roles.DIREC.toString());
	}
}
