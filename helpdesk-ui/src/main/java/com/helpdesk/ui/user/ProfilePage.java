package com.helpdesk.ui.user;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;

import com.helpdesk.domain.entity.UserEntity;
import com.helpdesk.ui.BasePage;
import com.helpdesk.ui.SingInPage;

public class ProfilePage extends BasePage {
	private static final long serialVersionUID = 1L;

	private UserEntity userEntity;
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		userEntity = getLoggedUser();
		if (userEntity == null) {
			setResponsePage(SingInPage.class);
			return;
		}
		
		add(initReadOnlyInput("name", userEntity.getName()));
		add(initReadOnlyInput("surname", userEntity.getSurname()));
		add(initReadOnlyInput("phone", userEntity.getPhone()));
		add(initReadOnlyInput("email", userEntity.getEmail()));
		add(initReadOnlyInput("company", userEntity.getCompanyEntity().getComapanyName()));
	}

	private TextField<String> initReadOnlyInput(String wicketId, String text) {
		return new TextField<String>(wicketId, Model.of(text));
		
	}
	
}
