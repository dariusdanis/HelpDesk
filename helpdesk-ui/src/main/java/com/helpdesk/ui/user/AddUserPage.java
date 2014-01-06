package com.helpdesk.ui.user;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import com.helpdesk.domain.entity.CompanyEntity;
import com.helpdesk.domain.entity.RoleEntity;
import com.helpdesk.domain.entity.UserEntity;
import com.helpdesk.domain.service.CompanyService;
import com.helpdesk.domain.service.RoleService;
import com.helpdesk.domain.service.UserService;
import com.helpdesk.ui.BasePage;
import com.helpdesk.ui.SingInPage;
import com.helpdesk.ui.utils.Constants;

public class AddUserPage extends BasePage {
	private static final long serialVersionUID = 1L;

	private UserEntity userEntity;
	
	@SpringBean
	private RoleService roleService;
	
	@SpringBean
	private UserService userService;
	
	@SpringBean
	private CompanyService companyService;
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		if (!isSingIn()) {
			setResponsePage(SingInPage.class);
			return;
		} else if (!director()) {
			setResponsePage(HomePage.class);
			return;
		}
		
		userEntity = new UserEntity();
		Form<?> form = initForm("employeeForm");
		form.add(initPositionDropDown("position", roleService.getAll(), "userEntity.roleEntity"));
		form.add(initCompanyDropDown("company", companyService.getAll(), "userEntity.companyEntity"));
		form.add(initTextField("name", "userEntity.name"));
		form.add(initTextField("surname", "userEntity.surname"));
		form.add(initTextField("email", "userEntity.email"));
		form.add(initTextField("phone", "userEntity.phone"));
		form.add(initPasswordField("password", "userEntity.password"));
		add(form);
	}

	private Form<?> initForm(String wicketId) {
		final Form<?> form = new Form<Void>(wicketId);
		form.add(new AjaxFormSubmitBehavior("onsubmit") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				
				List<Object> validationError = validateUserForm(userEntity);

				if (validationError != null) {
					appendJavaScript(target, validationError.get(0), validationError.get(1));
				} else {
					userService.merge(userEntity);
					setResponsePage(HomePage.class);
				}
				
			}

		});
		
		return form;
	}
	
	private TextField<String> initTextField(String wicketId, String expression) {
		TextField<String> textField = new TextField<String>(wicketId,
                new PropertyModel<String>(this, expression));
		textField.setOutputMarkupId(true);
		return textField;
	}
	
	private PasswordTextField initPasswordField(String wicketId, String expression){
		PasswordTextField passwordTextField = new PasswordTextField(wicketId ,
				new PropertyModel<String>(this, expression));
		passwordTextField.setOutputMarkupId(true);
		passwordTextField.setRequired(false);
		return passwordTextField;
	}
	
	private DropDownChoice<RoleEntity> initPositionDropDown(String wicketId, List<RoleEntity> list, String expression) {
		DropDownChoice<RoleEntity> roles = new DropDownChoice<RoleEntity>(wicketId,
				new PropertyModel<RoleEntity>(this, expression), list){
			private static final long serialVersionUID = 1L;

			@Override
            protected CharSequence getDefaultChoice(String selectedValue) {
                return "";
			}
		};
	roles.setOutputMarkupId(true);
	return roles;
	}
	
	private DropDownChoice<CompanyEntity> initCompanyDropDown(String wicketId, List<CompanyEntity> list, String expression) {
		DropDownChoice<CompanyEntity> roles = new DropDownChoice<CompanyEntity>(wicketId,
				new PropertyModel<CompanyEntity>(this, expression), list){
			private static final long serialVersionUID = 1L;

			@Override
            protected CharSequence getDefaultChoice(String selectedValue) {
                return "";
			}
		};
	roles.setOutputMarkupId(true);
	return roles;
	}
	
	private List<Object> validateUserForm(UserEntity userEntity) {
		List<String> list = Arrays.asList(new String[]{"roleEntity", 
				"companyEntity", "name", "surname", "email", "phone", "password"});
		Field[] fields = UserEntity.class.getDeclaredFields();
		
		for (String fieldName : list) {
			for (Field field : fields) {
				if (fieldName.equals(field.getName())) {
					try {
						field.setAccessible(true);
						if (field.get(userEntity) == null) {
							return Arrays.asList(new Object[]{fieldName, Constants.REQUIRED});
						}
						if (fieldName.equals("email")) {
							if (!userEntity.getEmail().matches(EmailAddressValidator.getInstance().getPattern().toString())) {
								return Arrays.asList(new Object[]{"email", Constants.BAD_EMAIL});
							}
						} else if (fieldName.equals("phone")) {
							if (userEntity.getPhone().length() != 9) {
								return Arrays.asList(new Object[]{"phone", Constants.BAD_PHONE});
							} else {
								try {
									Long.parseLong(userEntity.getPhone());
								} catch (Exception e) {
									return Arrays.asList(new Object[]{"phone", Constants.BAD_PHONE});
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
	
}
