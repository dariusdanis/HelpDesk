package com.helpdesk.ui;

import org.apache.wicket.Page;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.stereotype.Component;

import com.helpdesk.ui.user.ProfilePage;
import com.helpdesk.ui.user.UserMainPage;
import com.helpdesk.ui.utils.HelpDeskSession;

@Component("wicketApplication")
public class Application extends AuthenticatedWebApplication {

	@Override
	public Class<? extends Page> getHomePage() {
		return UserMainPage.class;
	}

	@Override
	protected void init() {
		super.init();
		getRequestCycleSettings().setResponseRequestEncoding("UTF-8");
		getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
		getComponentInstantiationListeners().add(
				new SpringComponentInjector(this));
		mountPage("/login", SingInPage.class);
		mountPage("/profile", ProfilePage.class);
	}

	@Override
	protected Class<? extends WebPage> getSignInPageClass() {
		return SingInPage.class;
	}

	@Override
	protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
		return HelpDeskSession.class;
	}

}
