package com.helpdesk.ui.user;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.apache.wicket.validation.validator.StringValidator;

import com.helpdesk.domain.entity.RequestEntity;
import com.helpdesk.domain.entity.TypeEntity;
import com.helpdesk.domain.service.TypeService;
import com.helpdesk.ui.BasePage;

public class AddRequestPage extends BasePage {
	private static final long serialVersionUID = 1L;

	private RequestEntity requestEntity;

	@SpringBean
	private TypeService typeService;

	@Override
	protected void onInitialize() {
		super.onInitialize();
		Form<?> form = initForm("requsetForm");		
		form.add(initTypeDropDown("type", typeService.getAll(), "requestEntity.typeEntity"));
		form.add(initTextArea("requestText", "requestEntity.requestText"));
		add(form);
	}
	
	

	private Form<?> initForm(String wicketId) {
		final Form<?> form = new Form<Void>(wicketId);
		form.add(new AjaxFormSubmitBehavior(form, "onsubmit") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				System.out.println("Succes");
				System.out.println(requestEntity.getRequestText());
				System.out.println(requestEntity.getTypeEntity());
			}
			
			@Override
			protected void onError(AjaxRequestTarget target) {
				form.visitFormComponents(new IVisitor<FormComponent<?>, Object>() {

					@Override
					public void component(FormComponent<?> object, IVisit<Object> visit) {
						System.out.println(object.isValid());
						
						
					}
					
				});
				System.out.println("ValidationError");

			}
			
		});
		return form;
	}
	
	private TextArea<String> initTextArea(String wicketId, String expression) {
		 TextArea<String> textAria = new TextArea<String>(wicketId,
	                new PropertyModel<String>(this, expression));
		 textAria.add(new StringValidator(5, 9000));
		 textAria.setRequired(true);
		 return textAria;
	}
	
	private ListChoice<TypeEntity> initTypeDropDown(String wicketId,
			List<TypeEntity> list, String expression) {
		ListChoice<TypeEntity> types = new ListChoice<TypeEntity>(wicketId,
				new PropertyModel<TypeEntity>(this, expression), list) {
			private static final long serialVersionUID = 1L;

			@Override
			protected CharSequence getDefaultChoice(String selectedValue) {
				return "";
			}
		};
		types.setRequired(true);
		types.add(new AttributeModifier("size", "1"));
		return types;
	}

}
