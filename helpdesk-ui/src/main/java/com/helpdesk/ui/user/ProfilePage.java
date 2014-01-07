package com.helpdesk.ui.user;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.helpdesk.domain.entity.UserEntity;
import com.helpdesk.domain.service.UserService;
import com.helpdesk.ui.BasePage;
import com.helpdesk.ui.SingInPage;
import com.helpdesk.ui.utils.Constants;

public class ProfilePage extends BasePage {
    private static final long serialVersionUID = 1L;
    private static final String USER_ID = "id";
    
    private String passwordNew;
    private String passwordConfirm;
    
    @SpringBean
    private UserService userService;
    
    private UserEntity userEntity;
    
    public static PageParameters parametersWith(int userId) {
        return new PageParameters().add(USER_ID, userId);
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
            if (!director()) {
                setResponsePage(HomePage.class);
                return;
            }
        }
        
        add(initReadOnlyInput("name", userEntity.getName()));
        add(initReadOnlyInput("surname", userEntity.getSurname()));
        add(initReadOnlyInput("phone", userEntity.getPhone()));
        add(initReadOnlyInput("email", userEntity.getEmail()));
        add(initReadOnlyInput("company", userEntity.getCompanyEntity().getComapanyName()));
        Form<?> form = initForm("passwordForm");
        add(form);
        form.add(initPasswordNewTextField("newPassword"));
        form.add(initPasswordConfirmTextField("confirmPassword"));

    }

    private TextField<String> initReadOnlyInput(String wicketId, String text) {
        return new TextField<String>(wicketId, Model.of(text));
        
    }
    
    
    private Form<?> initForm(String wicketId) {
        final Form<?> form = new Form<Void>(wicketId);
        form.add(new AjaxFormSubmitBehavior("onsubmit") {
            private static final long serialVersionUID = 1L;
            
            @Override
            protected void onSubmit(AjaxRequestTarget target) {                
                
                if (passwordNew != null) {
                    if (passwordNew.equals(passwordConfirm)) {
                        userEntity.setPassword(passwordNew);
                        userEntity = userService.merge(userEntity);
                        getSession().info("Password successfully changed");
                        setResponsePage(HomePage.class);                        
                    }
                    else        
                        appendJavaScript(target, "confirmPassword", Constants.MUST_MATCH);
                } else {
                    appendJavaScript(target, "newPassword", Constants.NOT_EMPTY);
                }
            }
            
        });
        return form;
    }
    
    private TextField<String> initPasswordNewTextField(String wicketId) {
        TextField<String> emailTF = new TextField<String>(wicketId,
                new PropertyModel<String>(this, "passwordNew"));
        return emailTF;
    }
    
    private TextField<String> initPasswordConfirmTextField(String wicketId) {
        TextField<String> emailTF = new TextField<String>(wicketId,
                new PropertyModel<String>(this, "passwordConfirm"));
        return emailTF;
    }
    
}