package com.helpdesk.ui.user;

import org.apache.wicket.authroles.authorization.strategies.role.Roles;

import com.helpdesk.ui.BasePage;
import com.helpdesk.ui.SingInPage;

public class ProfilePage extends BasePage {
	private static final long serialVersionUID = 1L;

	@Override
	protected void onInitialize() {
		super.onInitialize();
		Roles role = getRole();
		if (role != null) {
			
		} else {
			setResponsePage(SingInPage.class);
		}
		
	}
	
}
