package com.helpdesk.ui.request;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.helpdesk.domain.entity.RequestEntity;
import com.helpdesk.domain.entity.UserEntity;
import com.helpdesk.domain.service.RequestService;
import com.helpdesk.domain.service.UserService;
import com.helpdesk.ui.BasePage;
import com.helpdesk.ui.SingInPage;
import com.helpdesk.ui.user.HomePage;
import com.helpdesk.ui.utils.Constants;
import com.helpdesk.ui.utils.HelpDeskSession;

public class RequestPage extends BasePage {
	private static final long serialVersionUID = 1L;
	private static final String REQUSET_ID = "id";
	
	private RequestEntity requestEntity;
	
	private UserEntity userEntity;
	
	@SpringBean
	private RequestService requestService;
	
	@SpringBean
	private UserService userService;
	
	public static PageParameters parametersWith(int eventId) {
		return new PageParameters().add(REQUSET_ID, eventId);
	}
	
	public RequestPage(PageParameters params) {
		try {
			requestEntity = requestService.loadById(params.get(REQUSET_ID).toInt());
        } catch (Exception e) {
        	requestEntity = null;
        }
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		if (!isSingIn()) {
			setResponsePage(SingInPage.class);
			return;
		} else {
			if (requestEntity != null && !authorize(requestEntity, getLoggedUser())) {
				setResponsePage(HomePage.class);
				return;
			}
		}
		if (requestEntity == null) {
			setResponsePage(HomePage.class);
			return;
		}
		add(initReadOnlyInput("client", getClientInfo(requestEntity)));
		add(initReadOnlyInput("summary", requestEntity.getSummary()));
		add(initReadOnlyInput("date", requestEntity.getRequestDate().toString()));
		add(initLink("linkToParent","parentId", requestEntity.getParentRequsetId()));
		add(initReadOnlyTextArea("issue", requestEntity.getRequestText()));
		add(initReadOnlyInput("facility", requestEntity.getFacilityEntity().getName()));
		add(initRequestOptionsConteiner("requestOptions"));
		add(initEngineerConteiner("engineerConteiner", requestEntity));
		Form<?> form = initForm("requsetForm");
		form.add(initTextArea("solution","requestEntity.requestSolution"));
		form.add(initInputField("timeSpend", "requestEntity.timeSpend"));
		form.add(initTextArea("whatWasDone","requestEntity.whatWasDone"));
		form.add(initValidateAndShowLink("validateAndShowModal", form));
		add(form);
	}

	private WebMarkupContainer initValidateAndShowLink(String wicketId, Form<?> form) {
		WebMarkupContainer container = new WebMarkupContainer("validateAndShowModalConteiner");
		AjaxSubmitLink ajaxSubmitLink = new AjaxSubmitLink(wicketId){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				List<Object> validationError = validateForm(form, requestEntity.getRequestSolution());
				if (validationError != null) {
					appendJavaScript(target, form, validationError.get(0), validationError.get(1));
				} else {
					target.appendJavaScript("$('#modal').modal('show')");
				}
			}
		};
		container.add(ajaxSubmitLink);
		container.setVisible(assingedToLoggedUser());
		return container;
	}

	private Form<?> initForm(String wicketId) {
		final Form<?> form = new Form<Void>(wicketId);
		form.add(new AjaxFormSubmitBehavior("onsubmit") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				if (!client() && !admin()) {
					List<Object> validationError = validateForm(form, requestEntity.getRequestSolution(),
							 requestEntity.getTimeSpend(), requestEntity.getWhatWasDone());
					if (validationError != null) {
						appendJavaScript(target, form, validationError.get(0), validationError.get(1));
					} else {
						try {
							Integer.parseInt(requestEntity.getTimeSpend());
							requestEntity.setStatus(Constants.Status.SOLVED.toString());
							requestService.merge(requestEntity);
							notificationService.merge(requestEntity, Arrays.asList(new UserEntity[]{requestEntity.getCreatorEntity()}), Constants.REQUEST_SOLVE);
							sendToUser(requestEntity.getCreatorEntity());
							setResponsePage(HomePage.class);
						} catch (Exception e) {
							appendJavaScript(target, form, 1, "TODO: Bad time!");
						}
					}
				}
			}
			
		});
		
		return form;
	}

	private TextField<String> initReadOnlyInput(String wicketId, String value) {
		TextField<String> readOnlyTextField = new TextField<String>(wicketId);
		readOnlyTextField.add(new AttributeModifier("value",value));
		return readOnlyTextField;
	}
	
	private AjaxLink<Object> initLink(String wicketId, String labelWicketId, final Integer parentId){
		AjaxLink<Object> link = new AjaxLink<Object>(wicketId) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick(AjaxRequestTarget target) {
				if (parentId != 0) {
					setResponsePage(RequestPage.class, RequestPage.parametersWith(parentId));
				}
			}
			
		};
		link.add(new Label(labelWicketId, parentId == 0 ? "-" : "R-" + parentId));
		return link;
	}
	
	private TextArea<String> initReadOnlyTextArea(String wicketId, String text) {
		TextArea<String> textAria = new TextArea<String>(wicketId, Model.of(text));
		return textAria;
	}
	
	private TextArea<String> initTextArea(String wicketId, String expression) {
		 TextArea<String> textAria = new TextArea<String>(wicketId,
	                new PropertyModel<String>(this, expression));
		 textAria.setOutputMarkupId(true);
		 return textAria;
	}
	
	private TextField<String> initInputField(String wicketId, String expression) {
		TextField<String> textField = new TextField<String>(wicketId,
				new PropertyModel<String>(this, expression));
		textField.setOutputMarkupId(true);
		return textField;
	}
	
	private String getClientInfo(RequestEntity requestEntity) {
		String clientName = requestEntity.getCreatorEntity().getName();
		String clientNumber = requestEntity.getCreatorEntity().getPhone();
		String clientEmail = requestEntity.getCreatorEntity().getEmail();
		return requestEntity.getReceiptMethod().equals(Constants.ReceiptMethod.SELF_SERVICE.toString()) ?  clientName : 
			requestEntity.getReceiptMethod().equals(Constants.ReceiptMethod.EMAIL.toString()) ? clientName + " (phone: "+ clientNumber + ")" :
				clientName + " (email: " + clientEmail +")";
	}
	
	private boolean authorize(RequestEntity requestEntity, UserEntity userEntity) {
		if (requestEntity.getCreatorEntity() != null && 
				requestEntity.getCreatorEntity().getId() == userEntity.getId()) {
			return true;
		} else if (admin() || director()) {
			return true;
		} else if (requestEntity.getEngineerEntity() != null && 
				userEntity.getId() == requestEntity.getEngineerEntity().getId()) {
			return true;
		}
		return false;
	}
	
	private WebMarkupContainer initRequestOptionsConteiner(String wicketId) {
		WebMarkupContainer container = new WebMarkupContainer(wicketId);
		container.add(initWountSolveLink());
		container.add(initBackToAdminLink());
		container.setVisible(assingedToLoggedUser());
		return container;
	}

	private Link<Object> initBackToAdminLink() {
		Link<Object> link = new Link<Object>("backToAdmin") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				requestEntity.setStatus(Constants.Status.WONT_SOLVE.toString());
				requestEntity.setEngineerEntity(null);
				requestService.merge(requestEntity);
				notificationService.merge(requestEntity, Arrays.asList(new UserEntity[]{requestEntity.getAdministratorEntity()}),
						Constants.BACK_TO_ADMIN);
				sendToUser(requestEntity.getAdministratorEntity());
				setResponsePage(HomePage.class);
			}
		};
		
		if (engineer() || (director() && requestEntity.getAdministratorEntity() != null 
				&& requestEntity.getAdministratorEntity().getId() != getLoggedUser().getId())) {
			link.setVisible(true);
		} else {
			link.setVisible(false);
		}
		
		return link;
	}

	private  Link<Object> initWountSolveLink() {
		Link<Object> link = new Link<Object>("wountSolve") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				requestEntity.setStatus(Constants.Status.WONT_SOLVE.toString());
				requestService.merge(requestEntity);
				notificationService.merge(requestEntity, Arrays.asList(new UserEntity[]{requestEntity.getCreatorEntity()}),
						Constants.WOUNT_SOLVE);
				sendToUser(requestEntity.getCreatorEntity());
				setResponsePage(HomePage.class);
			}
			
		};
		
		link.setVisible(engineer() || director());
		return link;
	}
	
	private WebMarkupContainer initEngineerConteiner(String wicketId, RequestEntity requestEntity) {
		WebMarkupContainer container = new WebMarkupContainer(wicketId);
		container.setOutputMarkupId(true);
		DropDownChoice<UserEntity> choice;
		TextField<String> readOnlyTextField;
		if (requestEntity.getEngineerEntity() == null) {
			choice = initTypeDropDown("engineerSelect", userService.findAllByRole(Constants.Roles.ENGIN.toString(), 
					Constants.Roles.DIREC.toString()), "userEntity", requestEntity, container);
			readOnlyTextField = initReadOnlyInput("engineer", "-");
			if (client()) {
				choice.setVisible(false);
			} else {
				readOnlyTextField.setVisible(false);
			}
		} else {
			if (director() && requestEntity.getStatus().equals(Constants.Status.ASSIGNED.toString())
					|| requestEntity.getStatus().equals(Constants.Status.NOT_ASSIGNED.toString())) {
				userEntity = requestEntity.getEngineerEntity();
				choice = initTypeDropDown("engineerSelect", userService.findAllByRole(Constants.Roles.ENGIN.toString(), 
						Constants.Roles.DIREC.toString()), "userEntity", requestEntity, container);
				readOnlyTextField = initReadOnlyInput("engineer", "-");
				readOnlyTextField.setVisibilityAllowed(false);
			} else {
				choice = initFakeDropDown("engineerSelect");
				readOnlyTextField = initReadOnlyInput("engineer", requestEntity.getEngineerEntity().toString());
			}
		}
		readOnlyTextField.setOutputMarkupId(true);
		container.add(choice);
		container.add(readOnlyTextField);
		return container;
	}
	
	private DropDownChoice<UserEntity> initTypeDropDown(String wicketId,
			List<UserEntity> list, String expression, RequestEntity entity,
			WebMarkupContainer container) {
		DropDownChoice<UserEntity> choice = new DropDownChoice<UserEntity>(wicketId, 
				new PropertyModel<UserEntity>(this, expression), list) {
					private static final long serialVersionUID = 1L;
			
					@Override
					protected CharSequence getDefaultChoice(String selectedValue) {
						return "";
					}
					
		};
		choice.add(initChangeBehaviour(entity, container));
		choice.setOutputMarkupId(true);
		return choice;
	}
	
	private OnChangeAjaxBehavior initChangeBehaviour(final RequestEntity entity, 
			final WebMarkupContainer container) {
		return new OnChangeAjaxBehavior() {
			private static final long serialVersionUID = 1L;

			@Override
			    protected void onUpdate(AjaxRequestTarget target) {
					entity.setEngineerEntity(userEntity);
					entity.setAdministratorEntity(((HelpDeskSession)getSession()).getUser());
					if (userEntity != null) {
						entity.setStatus(Constants.Status.ASSIGNED.toString());
						notificationService.merge(requestEntity, Arrays.asList(new UserEntity[]{userEntity}), Constants.NEW_ASSIGMENT);
						sendToUser(userEntity);
					} else {
						entity.setStatus(Constants.Status.NOT_ASSIGNED.toString());
					}
					requestService.merge(entity);
					if (!director()) {
						container.removeAll();
						TextField<String> field = initReadOnlyInput("engineer", userEntity.toString());
						field.setOutputMarkupId(true);
						container.add(initFakeDropDown("engineerSelect"));
						container.add(field);
						target.add(container);
					}
			}
			
		};
	}
	
	private DropDownChoice<UserEntity> initFakeDropDown(String wicketId) {
		DropDownChoice<UserEntity> choice = new DropDownChoice<UserEntity>(wicketId);
		choice.setVisible(false);
		choice.setOutputMarkupId(true);
		return choice;
	}
	
	private boolean assingedToLoggedUser() {
		if (requestEntity.getEngineerEntity() == null) {
			return false;
		} else if (!requestEntity.getStatus().equals(Constants.Status.ASSIGNED.toString())) {
			return false;
		} else if (requestEntity.getEngineerEntity().getId() == getLoggedUser().getId()) {		
			return true;
		}
		return false;
	}
	
}
