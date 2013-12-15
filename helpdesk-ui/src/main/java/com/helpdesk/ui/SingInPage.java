package com.helpdesk.ui;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import com.helpdesk.ui.user.HomePage;
import com.helpdesk.ui.utils.HelpDeskSession;

public class SingInPage extends WebPage {
	private static final long serialVersionUID = 1L;

	private String email;
	private String password;
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		if (((HelpDeskSession)getSession()).isSignedIn()) {
			setResponsePage(HomePage.class);
			return;
		}
		Form<?> loginForm = new Form<Void>("loginForm");
		FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
		feedbackPanel.setOutputMarkupId(true);
		loginForm.add(initPasswordTextField("passwordTF"));
		loginForm.add(initEmailTextField("emailTF"));
		loginForm.add(initLoginButton("signInButton", feedbackPanel));
		add(loginForm);
		add(feedbackPanel);
	}
	
	private TextField<String> initEmailTextField(String wicketId) {
		TextField<String> emailTF = new TextField<String>(wicketId,
                new PropertyModel<String>(this, "email"));
		return emailTF;
	}

	private PasswordTextField initPasswordTextField(String wicketId) {
		PasswordTextField password = new PasswordTextField(wicketId,
				new PropertyModel<String>(this, "password"));
		return password;
	}
	
	private AjaxButton initLoginButton(String wicketId, final FeedbackPanel feedbackPanel) {
		return new AjaxButton(wicketId) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public void onSubmit(AjaxRequestTarget target, Form<?> form) {
        	 if(email.matches(EmailAddressValidator.getInstance().getPattern().toString())) {
                 if (((HelpDeskSession) getSession()).signIn(email, password)) {
                         setResponsePage(HomePage.class);
                 }        
             }
             info("Try again");
             target.add(feedbackPanel);
             target.appendJavaScript("showErrors();");
            }
        };
	}
	
}

