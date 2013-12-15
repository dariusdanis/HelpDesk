package com.helpdesk.ui.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.helpdesk.domain.entity.UserEntity;
import com.helpdesk.domain.service.UserService;
import com.helpdesk.ui.BasePage;

public class HelpDeskSession extends AuthenticatedWebSession  {
	private static final long serialVersionUID = 1L;

	private static List<HelpDeskSession> helpDeskSessions = new ArrayList<HelpDeskSession>();
	
	@SpringBean
	private UserService userService;
	private UserEntity user;
	
	public HelpDeskSession(Request request) {
		super(request);
		Injector.get().inject(this);
	}

	public boolean authenticate(String email, String password) {
		user = userService.findByEmail(email);
		if (user != null && user.getPassword().equals(password)) {
			helpDeskSessions.add(this);
			return true;
		}
		return false;
	}

	@Override
	public Roles getRoles() {
		if (isSignedIn()) {
			return new Roles(user.getRoleEntity().getRole());
		}
		return null;
	}

	@Override
	public void invalidate() {
		helpDeskSessions.remove(this);
		BasePage.pageMap.remove(this.getId());
		super.invalidate();
	}
	
	@Override
	public boolean equals(Object obj) {
		 if (obj == null)return false;
		 if (!(obj instanceof HelpDeskSession))return false;
		 if (((HelpDeskSession) obj).getUser().getId() == this.user.getId()) return true;
		 return false;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public static List<HelpDeskSession> getHelpDeskSessions() {
		return helpDeskSessions;
	}

	public static void setHelpDeskSessions(List<HelpDeskSession> helpDeskSessions) {
		HelpDeskSession.helpDeskSessions = helpDeskSessions;
	}
	
}
