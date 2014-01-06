package com.helpdesk.ui;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.protocol.ws.api.IWebSocketConnection;
import org.apache.wicket.protocol.ws.api.IWebSocketConnectionRegistry;
import org.apache.wicket.protocol.ws.api.SimpleWebSocketConnectionRegistry;
import org.apache.wicket.protocol.ws.api.WebSocketBehavior;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.helpdesk.domain.entity.NotificationEntity;
import com.helpdesk.domain.entity.RequestEntity;
import com.helpdesk.domain.entity.UserEntity;
import com.helpdesk.domain.service.NotificationService;
import com.helpdesk.domain.service.UserService;
import com.helpdesk.reports.ReportsPage;
import com.helpdesk.ui.request.AddRequestPage;
import com.helpdesk.ui.request.RequestPage;
import com.helpdesk.ui.statistic.StatisticsPage;
import com.helpdesk.ui.user.AddUserPage;
import com.helpdesk.ui.user.AllUserPage;
import com.helpdesk.ui.user.HomePage;
import com.helpdesk.ui.user.ProfilePage;
import com.helpdesk.ui.utils.Constants;
import com.helpdesk.ui.utils.HelpDeskSession;

public class BasePage extends WebPage {
	private static final long serialVersionUID = 1L;
	
	public static Map<String, Integer> pageMap = new HashMap<String, Integer>();
	
	public List<String> messages = new ArrayList<String>();
	
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
		menuItems.add(initProfileLink(menuItems.newChildId(), "My Profile"));
		menuItems.add(initEmployeeLink(menuItems.newChildId(), "Add User"));
		menuItems.add(initAllUserLink(menuItems.newChildId(), "All Users"));
		menuItems.add(initPageStatisticsLink(menuItems.newChildId(), "Page Statistics"));
		menuItems.add(initRequestLinkLink(menuItems.newChildId(), "Create Request"));
		menuItems.add(initReportsLink(menuItems.newChildId(), "View Reports"));
		add(initNotificationCounter("notificationCounter"));
		add(initNotificationsLink("notificationLink", notificationItems, notificationConteiner));
		add(initHomeLink("home"));
		add(initLogOffLink("logOff"));
		add(menuItems);
		add(initWebSocket());
		add(notificationConteiner);
	}

	private Link<Object> initReportsLink(String wicketId, String label) {
		Link<Object> link = new Link<Object>(wicketId) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(ReportsPage.class);
			}
		};
		
		link.add(new Label("menuLabel", label));
		link.setVisible(director());
		return link;
	}

	private Link<Object> initPageStatisticsLink(String wicketId, String label) {
		Link<Object> link = new Link<Object>(wicketId) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(StatisticsPage.class);
			}
		};
		link.add(new Label("menuLabel", label));
		link.setVisible(director());
		return link;
	}

	private Link<Object> initAllUserLink(String wicketId, String label) {
		Link<Object> link = new Link<Object>(wicketId) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(AllUserPage.class);
			}
		};
		link.add(new Label("menuLabel", label));
		link.setVisible(director());
		return link;
	}

	private Link<Object> initRequestLinkLink(String wicketId, String label) {
		Link<Object> link = new Link<Object>(wicketId) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(AddRequestPage.class);
			}
		};
		link.add(new Label("menuLabel", label));
		link.setVisibilityAllowed(!engineer());
		return link;
	}

	private Link<Object> initEmployeeLink(String wicketId, String label) {
		Link<Object> link = new Link<Object>(wicketId) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(AddUserPage.class);
			}
		};
		link.add(new Label("menuLabel", label));
		link.setVisible(director());
		return link;
	}

	private Link<Object> initProfileLink(String wicketId, String label) {
		Link<Object> link = new Link<Object>(wicketId) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(ProfilePage.class, ProfilePage.parametersWith(getLoggedUser().getId()));
			}
		};
		link.add(new Label("menuLabel", label));
		return link;
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

	public void sendToUser(UserEntity userEntity, String message) {
		IWebSocketConnection connection = getConnection(userEntity);
		if (connection != null && connection.isOpen()) {
			try {
				connection.sendMessage(message);
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
	
	public String getRole() {
		return getLoggedUser().getRoleEntity().getRole();
	}
	
	public List<Object> validateRequsetForm(List<String> fieldsToValidate, RequestEntity requestEntity) {
		Field[] fields = RequestEntity.class.getDeclaredFields();
		
		for (String fieldName : fieldsToValidate) {
			for (Field field : fields) {
				if (fieldName.equals(field.getName())) {
					try {
						field.setAccessible(true);
						if (field.get(requestEntity) == null) {
							return Arrays.asList(new Object[]{fieldName, Constants.REQUIRED});
						} else if (field.get(requestEntity) instanceof String) {
							if (field.getName().equals("summary")) {
								if (checkLength(field.get(requestEntity).toString(), Constants.MIN_LENGTH, 
										Constants.MAX_LENGTH)) {
									return Arrays.asList(new Object[]{fieldName, 
											lengthMessage(Constants.MIN_LENGTH, Constants.MAX_LENGTH)});
								}
							} else {
								if (checkLength(field.get(requestEntity).toString(), Constants.MIN_LENGTH, 
										Constants.MAX_LENGTH_TAREA)) {
									return Arrays.asList(new Object[]{fieldName, 
											lengthMessage(Constants.MIN_LENGTH, Constants.MAX_LENGTH)});
								}
							}
						}
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}
	
	protected boolean checkLength (String data, int minLength, int maxLength) {
		return data.length() < minLength || data.length() > maxLength;
	}
	
	public UserEntity getLoggedUser() {
		return ((HelpDeskSession)getSession()).getUser();
	}
	
	public boolean isSingIn() {
		return ((HelpDeskSession) getSession()).isSignedIn();
	}
	
	protected String lengthMessage(int minLength, int maxLength) {
		return "DOTO LATER: minLength " + minLength + " maxLength " + maxLength;
	}
	
	public void appendJavaScript(AjaxRequestTarget target, Object id, Object message) {
		target.appendJavaScript("appendError('"+ id +"','"+message.toString()+"');");
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
