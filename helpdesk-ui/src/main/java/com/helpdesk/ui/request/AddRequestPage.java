package com.helpdesk.ui.request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
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
		} else if (authorize(getLoggedUser())) {
			setResponsePage(HomePage.class);
			return;
		}
		requestEntity = new RequestEntity();
		Form<?> form = initForm("requsetForm");
		form.add(initInputField("summary", "requestEntity.summary"));
		form.add(initTextArea("requestText", "requestEntity.requestText"));
		form.add(initTypeDropDown("type", typeService.getAll(), "requestEntity.typeEntity"));
		DropDownChoice<FacilityEntity> facilityDropDown = initFacilityDropDown("facility", "requestEntity.facilityEntity");
		DropDownChoice<RequestEntity> parentDropDown = initParentDropDown("parent", "requestEntity.parentRequsetId");
		WebMarkupContainer parentAndFacilityContainer = new WebMarkupContainer("parentAndFacilityContainer");
		parentAndFacilityContainer.setOutputMarkupId(true);
		form.add(initClientDropDown("client", userService.findAllByRole(Constants.Roles.CLIEN.toString()), 
				"requestEntity.requestBelongsTo", parentAndFacilityContainer));
		form.add(initReceiptDropDown("receipt", Constants.receiptMethodsList, "requestEntity.receiptMethod"));
		parentAndFacilityContainer.add(facilityDropDown);
		parentAndFacilityContainer.add(parentDropDown);
		form.add(parentAndFacilityContainer);
		add(form);
	}

	private Form<?> initForm(String wicketId) {
		final Form<?> form = new Form<Void>(wicketId);
		form.add(new AjaxFormSubmitBehavior("onsubmit") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit(AjaxRequestTarget target) {				
				List<Object> validationError = validateRequsetForm(getFieldToValidate(), requestEntity);

				if (validationError != null) {
					appendJavaScript(target, validationError.get(0), validationError.get(1));
				} else {
					requestEntity.setCreatorEntity(getLoggedUser());
					if (client()) {
						requestEntity.setReceiptMethod(Constants.ReceiptMethod.SELF_SERVICE.toString());
						requestEntity.setRequestBelongsTo(getLoggedUser());
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
	
	private DropDownChoice<FacilityEntity> initFacilityDropDown(String wicketId, String expression) {
		List<FacilityEntity> list;
		if (client()) {
			list = facilityService.getAllByCompany(getLoggedUser().getCompanyEntity());
		} else if (requestEntity.getRequestBelongsTo() != null) {
			list = facilityService.getAllByCompany(requestEntity.getRequestBelongsTo().getCompanyEntity());
		} else {
			list = new ArrayList<FacilityEntity>();
		}
		
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
			List<UserEntity> list, String expression, WebMarkupContainer parentAndFacilityContainer) {
		WebMarkupContainer clientConteiner = new WebMarkupContainer("clientConteiner");
		DropDownChoice<UserEntity> clients = new DropDownChoice<UserEntity>(wicketId,
				new PropertyModel<UserEntity>(this, expression), list) {
			private static final long serialVersionUID = 1L;

			@Override
			protected CharSequence getDefaultChoice(String selectedValue) {
				return "";
			}
		};
		clients.setOutputMarkupId(true);
		clients.add(initChangeBehaviour(parentAndFacilityContainer));
		clientConteiner.add(clients);
		clientConteiner.setVisible(director() || admin());
		return clientConteiner;
	}
	
	private OnChangeAjaxBehavior initChangeBehaviour(final WebMarkupContainer parentAndFacilityContainer) {
		return new OnChangeAjaxBehavior() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				parentAndFacilityContainer.removeAll();
				DropDownChoice<FacilityEntity> facilityDropDown = initFacilityDropDown("facility", "requestEntity.facilityEntity");
				DropDownChoice<RequestEntity> parentDropDown = initParentDropDown("parent", "requestEntity.parentRequsetId");
				
				parentAndFacilityContainer.add(facilityDropDown);
				parentAndFacilityContainer.add(parentDropDown);
				target.add(parentAndFacilityContainer);
				target.appendJavaScript("refreshSelects('" + facilityDropDown.getMarkupId() + "', '" 
						+ parentDropDown.getMarkupId() + "');");
			}
			
		};
	}

	private DropDownChoice<RequestEntity> initParentDropDown(String wicketId, String expression) {
		List<RequestEntity> list;
		if (client()) {
			list = requestService.getAllByCreatOrBelongsTo(getLoggedUser());
		} else if (requestEntity.getRequestBelongsTo() != null) {
			list = requestService.getAllByCreatOrBelongsTo(requestEntity.getRequestBelongsTo());
		} else {
			list = new ArrayList<RequestEntity>();
		}
		
		DropDownChoice<RequestEntity> parent = new DropDownChoice<RequestEntity>(wicketId,
				new PropertyModel<RequestEntity>(this, expression), list) {
			private static final long serialVersionUID = 1L;

			@Override
			protected CharSequence getDefaultChoice(String selectedValue) {
				return "";
			}
		};
		parent.setOutputMarkupId(true);
		return parent;
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
		receiptConteiner.setVisible(director() || admin());
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

	private boolean authorize(UserEntity loggedUser) {
		return loggedUser.getRoleEntity().getRole().equals(Constants.Roles.ENGIN.toString());
	}
	
	private List<String> getFieldToValidate() {
		if (client()) {
			return Arrays.asList(new String[]{"summary","typeEntity","facilityEntity","requestText"});
		} else {
			return Arrays.asList(new String[]{"summary", "receiptMethod", "typeEntity",
					"requestBelongsTo", "facilityEntity", "requestText"});
		}
	}
	
}
