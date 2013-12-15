package com.helpdesk.ui.request;

import java.util.Date;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.helpdesk.domain.entity.FacilityEntity;
import com.helpdesk.domain.entity.RequestEntity;
import com.helpdesk.domain.entity.TypeEntity;
import com.helpdesk.domain.entity.UserEntity;
import com.helpdesk.domain.service.FacilityService;
import com.helpdesk.domain.service.RequestService;
import com.helpdesk.domain.service.TypeService;
import com.helpdesk.domain.service.UserService;
import com.helpdesk.ui.BasePage;
import com.helpdesk.ui.SingInPage;
import com.helpdesk.ui.user.HomePage;
import com.helpdesk.ui.utils.Constants;

public class AddRequestPage extends BasePage {
	private static final long serialVersionUID = 1L;

	private RequestEntity requestEntity;
	
	@SpringBean
	private TypeService typeService;

	@SpringBean
	private UserService userService;
	
	@SpringBean
	private RequestService requestService;
	
	@SpringBean
	private FacilityService facilityService;
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		if (!isSingIn()) {
			setResponsePage(SingInPage.class);
			return;
		}
		requestEntity = new RequestEntity();
		Form<?> form = initForm("requsetForm");
		form.add(initInputField("summary", "requestEntity.summary"));
		form.add(initTextArea("requestText", "requestEntity.requestText"));
		form.add(initTypeDropDown("type", typeService.getAll(), "requestEntity.typeEntity"));
		form.add(initFacilityDropDown("facility", facilityService.getAllByCompany(
				getLoggedUser().getCompanyEntity(), 
				getLoggedUser().getCompanyEntity().getComapanyName().equals(Constants.HPCompany)),
				"requestEntity.facilityEntity"));
		form.add(initParentDropDown("parent", requestService.getAllByCreator(getLoggedUser()), 
				"requestEntity.parentRequsetId"));
		form.add(initClientDropDown("client", userService.findAllByRole(Constants.Roles.CLIEN.toString()), 
				"requestEntity.creatorEntity"));
		form.add(initReceiptDropDown("receipt", Constants.receiptMethodsList, "requestEntity.receiptMethod"));
		add(form);
	}

	private Form<?> initForm(String wicketId) {
		final Form<?> form = new Form<Void>(wicketId);
		form.add(new AjaxFormSubmitBehavior("onsubmit") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				List<Object> validationError = validateForm(form, requestEntity.getSummary(),
						requestEntity.getRequestText(), requestEntity.getTypeEntity(), 
						requestEntity.getFacilityEntity());
				
				if (validationError != null) {
					appendJavaScript(target, form, validationError.get(0), validationError.get(1));
				} else {
					if (!client()) {
						if (requestEntity.getCreatorEntity() == null) {
							appendJavaScript(target, form, 5, Constants.REQUED);
							return;
						} else if (requestEntity.getReceiptMethod() == null) {
							appendJavaScript(target, form, 6, Constants.REQUED);
							return;
						}
					} else {
						requestEntity.setCreatorEntity(getLoggedUser());
						requestEntity.setReceiptMethod(Constants.ReceiptMethod.SELF_SERVICE.toString());
					}
					requestEntity.setRequestDate(new Date());
					requestEntity.setStatus(Constants.Status.NOT_ASSIGNED.toString());
					requestEntity = requestService.merge(requestEntity);
					notificationService.merge(requestEntity, userService.findAllByRole("ADMIN"), Constants.NEW_REQUEST);
					sendToRole(Constants.Roles.ADMIN.toString());
					setResponsePage(HomePage.class);
				}
			}
			
		});
		return form;
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
	
	private DropDownChoice<FacilityEntity> initFacilityDropDown(String wicketId,
			List<FacilityEntity> list, String expression) {
		DropDownChoice<FacilityEntity> types = new DropDownChoice<FacilityEntity>(wicketId,
				new PropertyModel<FacilityEntity>(this, expression), list) {
			private static final long serialVersionUID = 1L;

			@Override
			protected CharSequence getDefaultChoice(String selectedValue) {
				return "";
			}
		};
		types.setOutputMarkupId(true);
		return types;
	}
	
	private WebMarkupContainer initClientDropDown(String wicketId,
			List<UserEntity> list, String expression) {
		WebMarkupContainer clientConteiner = new WebMarkupContainer("clientConteiner");
		DropDownChoice<UserEntity> types = new DropDownChoice<UserEntity>(wicketId,
				new PropertyModel<UserEntity>(this, expression), list) {
			private static final long serialVersionUID = 1L;

			@Override
			protected CharSequence getDefaultChoice(String selectedValue) {
				return "";
			}
		};
		types.setOutputMarkupId(true);
		clientConteiner.add(types);
		clientConteiner.setVisible(admin());
		return clientConteiner;
	}
	
	private DropDownChoice<RequestEntity> initParentDropDown(String wicketId,
			List<RequestEntity> list, String expression) {
		DropDownChoice<RequestEntity> types = new DropDownChoice<RequestEntity>(wicketId,
				new PropertyModel<RequestEntity>(this, expression), list) {
			private static final long serialVersionUID = 1L;

			@Override
			protected CharSequence getDefaultChoice(String selectedValue) {
				return "";
			}
		};
		types.setOutputMarkupId(true);
		return types;
	}
	
	private WebMarkupContainer initReceiptDropDown(String wicketId,
			List<String> list, String expression) {
		WebMarkupContainer receiptConteiner = new WebMarkupContainer("receiptConteiner");
		DropDownChoice<String> types = new DropDownChoice<String>(wicketId,
				new PropertyModel<String>(this, expression), list) {
			private static final long serialVersionUID = 1L;

			@Override
			protected CharSequence getDefaultChoice(String selectedValue) {
				return "";
			}
		};
		types.setOutputMarkupId(true);
		receiptConteiner.add(types);
		receiptConteiner.setVisible(admin());
		return receiptConteiner;
	}
	
	private TextField<String> initInputField(String wicketId, String expression) {
		TextField<String> textField = new TextField<String>(wicketId,
				new PropertyModel<String>(this, expression));
		textField.setOutputMarkupId(true);
		return textField;
	}
	
	private TextArea<String> initTextArea(String wicketId, String expression) {
		 TextArea<String> textAria = new TextArea<String>(wicketId,
	                new PropertyModel<String>(this, expression));
		 textAria.setOutputMarkupId(true);
		 return textAria;
	}

}
