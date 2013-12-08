package com.helpdesk.ui.user;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.helpdesk.domain.entity.RoleEntity;
import com.helpdesk.domain.entity.UserEntity;
import com.helpdesk.domain.service.RoleService;
import com.helpdesk.domain.service.UserService;
import com.helpdesk.ui.BasePage;

public class AddEmployeePage extends BasePage {
	private static final long serialVersionUID = 1L;

	private UserEntity userEntity;
	
	@SpringBean
	private RoleService roleService;
	
	@SpringBean
	private UserService userService;
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		Form<?> form = initForm("employeeForm");
		form.add(initPositionDropDown("position", roleService.getAll(), "userEntity.roleEntity"));
		form.add(initTextField("name", "userEntity.name"));
		form.add(initTextField("surname", "userEntity.surname"));
		form.add(initTextField("email", "userEntity.email"));
		form.add(initTextField("phone", "userEntity.phone"));
		form.add(initPasswordTextField("password", "userEntity.password"));
		add(form);
	}

	private Form<?> initForm(String wicketId) {
		Form<?> form = new Form<Void>(wicketId);
		form.add(new AjaxFormSubmitBehavior("onsubmit") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				System.out.println("jo");
			}
			
			
		});
		return form;
	}
	
	private PasswordTextField initPasswordTextField(String wicketId, String expression) {
	    PasswordTextField passwordField = new PasswordTextField(wicketId,
	    		new PropertyModel<String>(this, expression));
	    return passwordField;
	}
	
	private TextField<String> initTextField(String wicketId, String expression) {
		TextField<String> textField = new TextField<String>(wicketId,
                new PropertyModel<String>(this, expression));
		return textField;
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
	return roles;
	}
	
}
