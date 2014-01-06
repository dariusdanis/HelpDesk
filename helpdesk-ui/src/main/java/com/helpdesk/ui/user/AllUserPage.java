package com.helpdesk.ui.user;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.helpdesk.domain.entity.UserEntity;
import com.helpdesk.domain.service.UserService;
import com.helpdesk.ui.BasePage;

public class AllUserPage extends BasePage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private UserService userService;
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		ListView<UserEntity> listView = initRequestTable("repeatingView", userService.findAll());
		add(initLinkToNewUserPage("linkToNewUserPage"));
		add(listView);
	}

	private Link<Object> initLinkToNewUserPage(String wicketId) {
		return new Link<Object>(wicketId) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(AddUserPage.class);
			}
		};
	}

	private ListView<UserEntity> initRequestTable(String wicketId, List<UserEntity> users) {
		ListModel<UserEntity> requsetModel = new ListModel<UserEntity>(users);
		ListView<UserEntity> listView =  new ListView<UserEntity>(wicketId, requsetModel) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void populateItem(ListItem<UserEntity> item) {
				UserEntity entity = item.getModelObject();
				Link<Object> linkToProfile = initLink("linkToUserProfile", entity.getId());
				linkToProfile.add(new Label("userId", entity.getId()));
				item.add(linkToProfile);
				item.add(new Label("name", entity.getName()));
				item.add(new Label("surname", entity.getSurname()));
				item.add(new Label("email", entity.getEmail()));
				item.add(new Label("company", entity.getCompanyEntity().getComapanyName()));
				item.add(new Label("active", entity.isActive()));
			}

		};
		listView.setOutputMarkupId(true);
		return listView;
	}

	protected Link<Object> initLink(String wicketId, final int id) {
		return new Link<Object>(wicketId){
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(ProfilePage.class, ProfilePage.parametersWith(id));
			}
			
		};
	}
	
}
