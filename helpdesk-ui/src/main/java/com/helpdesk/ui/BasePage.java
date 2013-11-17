package com.helpdesk.ui;

import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.RepeatingView;

import com.helpdesk.ui.user.ProfilePage;
import com.helpdesk.ui.utils.HelpDeskSession;

public class BasePage extends WebPage {
	private static final long serialVersionUID = 1L;

	@Override
	protected void onInitialize() {
		super.onInitialize();
		RepeatingView repeatingView = new RepeatingView("menuItems");
		repeatingView.add(menuItem(repeatingView.newChildId(), ProfilePage.class, "Personal info"));
		add(initLogOffLink("logOff"));
		add(repeatingView);
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

	private BookmarkablePageLink<Void> menuItem(String wicketId, Class<? extends BasePage> page, String name) {
		BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(wicketId, page);
		pageLink.add(new Label("label", name));
		pageLink.setVisible(show(page));
		return pageLink;
	}
	
	private boolean show(Class<? extends BasePage> page) {
		return true;
	}
	
	public Roles getRole() {
		return ((HelpDeskSession) getSession()).getRoles();
	}
	
}
