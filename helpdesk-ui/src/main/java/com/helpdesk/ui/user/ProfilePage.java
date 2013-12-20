package com.helpdesk.ui.user;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.helpdesk.domain.entity.UserEntity;
import com.helpdesk.domain.service.UserService;
import com.helpdesk.ui.BasePage;
import com.helpdesk.ui.SingInPage;

public class ProfilePage extends BasePage {
	private static final long serialVersionUID = 1L;
	private static final String USER_ID = "id";
	
	@SpringBean
	private UserService userService;
	
	private UserEntity userEntity;
	
	public static PageParameters parametersWith(int eventId) {
		return new PageParameters().add(USER_ID, eventId);
	}
	
	public ProfilePage(PageParameters params) {
		try {
			userEntity = userService.findById(params.get(USER_ID).toInt());
        } catch (Exception e) {
        	userEntity = null;
        }
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		if (!isSingIn()) {
			setResponsePage(SingInPage.class);
			return;
		} else if (userEntity == null) {
			setResponsePage(HomePage.class);
			return;
		} else if (userEntity.getId() != getLoggedUser().getId()) {
			setResponsePage(HomePage.class);
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
