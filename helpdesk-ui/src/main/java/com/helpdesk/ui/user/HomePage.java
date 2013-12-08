package com.helpdesk.ui.user;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.helpdesk.domain.entity.RequestEntity;
import com.helpdesk.domain.entity.TypeEntity;
import com.helpdesk.domain.service.RequestService;
import com.helpdesk.domain.service.TypeService;
import com.helpdesk.ui.BasePage;

public class HomePage extends BasePage {
	private static final long serialVersionUID = 1L;

	private RequestEntity requestEntity;
	
	@SpringBean
	private TypeService typeService;

	@SpringBean
	private RequestService requestService;
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		requestEntity = new RequestEntity();
		Form<?> form = initForm("requsetForm");
		form.add(initTextArea("requestText", "requestEntity.requestText"));
		add(form);
	}

	private Form<?> initForm(String wicketId) {
		final Form<?> form = new Form<Void>(wicketId);
		form.add(new AjaxFormSubmitBehavior("onsubmit") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				System.out.println("sub");
			}
			
			
		});
		return form;
	}
	
	private TextArea<String> initTextArea(String wicketId, String expression) {
		 TextArea<String> textAria = new TextArea<String>(wicketId,
	                new PropertyModel<String>(this, expression));
		 textAria.setOutputMarkupId(true);
		 return textAria;
	}
	
	
	private DropDownChoice<TypeEntity> initTypeDropDown(String wicketId,
			List<TypeEntity> list, String expression) {
		DropDownChoice<TypeEntity> types = new DropDownChoice<TypeEntity>(wicketId,
				new PropertyModel<TypeEntity>(this, expression), list) {
			private static final long serialVersionUID = 1L;

			@Override
			protected CharSequence getDefaultChoice(String selectedValue) {
				return "";
			}
		};
		types.setOutputMarkupId(true);
		return types;
	}
	
}
