package com.helpdesk.ui.utils;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.helpdesk.domain.entity.UserEntity;
import com.helpdesk.domain.service.UserService;

public class HelpDeskSession extends AuthenticatedWebSession {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private UserService userService;
	private UserEntity user;
	
	public HelpDeskSession(Request request) {
		super(request);
		Injector.get().inject(this);
	}

	@Override
	public boolean authenticate(String email, String password) {
		user = userService.findByEmail(email);
		if (user != null && user.getPassword().equals(password)) {
			return true;
		}
		return false;
	}

	@Override
	public Roles getRoles() {
		if (isSignedIn()) {
			return new Roles(user.getRole());
		}
		return null;
	}

}
