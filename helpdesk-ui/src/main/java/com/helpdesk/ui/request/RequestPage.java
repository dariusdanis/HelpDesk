package com.helpdesk.ui.request;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
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
	
	private String action, timeSpend;
	
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
		
		add(initEngineerConteiner("engineerConteiner", requestEntity));
		Form<?> form = initForm("requsetForm");
		form.add(initSubbmitConteiner("subbmitConteiner"));
		form.add(initActionSelect("action", "action", getActions()));
		form.add(initAnswerTextArea("whatWasDone","requestEntity.requestSolution"));
		form.add(initTimeSpendInput("time", "timeSpend"));
		add(form);
	}

	private List<String> getActions() {
		if (engineer() || (director() && requestEntity.getAdministratorEntity() != null 
				&& requestEntity.getAdministratorEntity().getId() != getLoggedUser().getId())) {
			return Constants.ACTIONS_ENG;
		} else {
			return Constants.ACTIONS_ADM;
		}
	}

	private WebMarkupContainer initSubbmitConteiner(String wicketId) {
		WebMarkupContainer subbmitConteiner = new WebMarkupContainer(wicketId);
		boolean visible = false;
		if ((requestEntity.getEngineerEntity() != null && 
				requestEntity.getEngineerEntity().getId() == getLoggedUser().getId())) {
			if (!requestEntity.getStatus().equals(Constants.Status.SOLVED.toString()) &&
					!requestEntity.getStatus().equals(Constants.Status.WONT_SOLVE.toString())) {
				visible = true;
			}
		}
		
		subbmitConteiner.setVisible(visible);
		return subbmitConteiner;
	}

	private TextArea<String> initAnswerTextArea(String wicketId, String expression) {
		if (client() && (requestEntity.getStatus().equals(Constants.Status.ASSIGNED.toString()) || 
				requestEntity.getStatus().equals(Constants.Status.NOT_ASSIGNED.toString()))) {
			requestEntity.setRequestSolution("");
		}
		TextArea<String> textAria = new TextArea<String>(wicketId,
	               new PropertyModel<String>(this, expression));
		textAria.setOutputMarkupId(true);
		return textAria;
	}

	private WebMarkupContainer initTimeSpendInput(String wicketId, String expression) {
		TextField<String> textField = new TextField<String>(wicketId,
				new PropertyModel<String>(this, expression));
		WebMarkupContainer timeConteiner = new WebMarkupContainer("timeConteiner");
		timeConteiner.add(textField);
		boolean visible = false;
		if ((requestEntity.getEngineerEntity() != null && 
				requestEntity.getEngineerEntity().getId() == getLoggedUser().getId())) {
			if (!requestEntity.getStatus().equals(Constants.Status.SOLVED.toString()) &&
					!requestEntity.getStatus().equals(Constants.Status.WONT_SOLVE.toString())) {
				visible = true;
			}
		}
		timeConteiner.setVisible(visible);
		return timeConteiner;
	}

	private WebMarkupContainer initActionSelect(String wicketId, String expression,
			List<String> actions) {
		WebMarkupContainer actionConteiner = new WebMarkupContainer("actionConteiner");
		DropDownChoice<String> action = new DropDownChoice<String>(wicketId,
				new PropertyModel<String>(this, expression), actions) {
			private static final long serialVersionUID = 1L;

			@Override
			protected CharSequence getDefaultChoice(String selectedValue) {
				return "";
			}
		};
		actionConteiner.add(action);
		boolean visible = false;
		if ((requestEntity.getEngineerEntity() != null && 
				requestEntity.getEngineerEntity().getId() == getLoggedUser().getId())) {
			if (!requestEntity.getStatus().equals(Constants.Status.SOLVED.toString()) &&
					!requestEntity.getStatus().equals(Constants.Status.WONT_SOLVE.toString())) {
				visible = true;
			}
		}
		actionConteiner.setVisible(visible);
		return actionConteiner;
	}

	private Form<?> initForm(String wicketId) {
		final Form<?> form = new Form<Void>(wicketId);
		form.add(new AjaxFormSubmitBehavior("onsubmit") {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				if (requestEntity.getEngineerEntity() != null && 
						requestEntity.getEngineerEntity().getId() == getLoggedUser().getId()) {
					List<Object> validationError = validateRequsetForm( 
							Arrays.asList(new String[]{"requestSolution", "timeSpend", "whatWasDone"}));
					if (validationError != null) {
						appendJavaScript(target, validationError.get(0), validationError.get(1));
					} else {
						switch (action) {
						case "Back To Administration!":
							requestEntity.setStatus(Constants.Status.NOT_ASSIGNED.toString());
							requestEntity.setEngineerEntity(null);
							requestService.merge(requestEntity);
							notificationService.merge(requestEntity, Arrays.asList(new UserEntity[]{requestEntity.getAdministratorEntity()}),
									Constants.BACK_TO_ADMIN);
							sendToUser(requestEntity.getAdministratorEntity(),
									Constants.BACK_TO_ADMIN);
							getSession().info("Request successfully returned!");
							setResponsePage(HomePage.class);
							break;
						case "Solved!":
							if (requestEntity.getTimeSpend() == null){
								requestEntity.setTimeSpend(timeSpend);
							} else {
								requestEntity.setTimeSpend(String.valueOf(Integer.parseInt(requestEntity.getTimeSpend()) + 
										Integer.parseInt(timeSpend)));
							}
							requestEntity.setStatus(Constants.Status.SOLVED.toString());
							requestEntity.setSolveDate(BasePage.getSysteDate());
							notificationHandler(requestService.merge(requestEntity), 
									Constants.REQUEST_SOLVE);
							getSession().info("Request successfully solved!");
							setResponsePage(HomePage.class);
							break;
						case "Woun't Solve!":
							requestEntity.setStatus(Constants.Status.WONT_SOLVE.toString());
							notificationHandler(requestService.merge(requestEntity), Constants.WOUNT_SOLVE);
							getSession().info("Request not solved");
							setResponsePage(HomePage.class);
							break;
						}
					}
				}
			}
			
		});
		
		return form;
	}

	protected List<Object> validateRequsetForm(List<String> asList) {
		if (action == null) {
			return Arrays.asList(new Object[]{"action", Constants.REQUIRED});
		}
		
		if (timeSpend == null) {
			return Arrays.asList(new Object[]{"time", Constants.REQUIRED});
		} else {
			try {
				Integer.valueOf(timeSpend);
			} catch (Exception e) {
				return Arrays.asList(new Object[]{"time", "Bad time!"});
			}
		}
		if (requestEntity.getRequestSolution() == null) {
			return Arrays.asList(new Object[]{"whatWasDone", Constants.REQUIRED});
		} else {
			if (checkLength(action, Constants.MIN_LENGTH, Constants.MAX_LENGTH_TAREA)) {
				return Arrays.asList(new Object[]{"whatWasDone", 
						lengthMessage(Constants.MIN_LENGTH, Constants.MAX_LENGTH)});
			}
		}
		return null;
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
	
	private String getClientInfo(RequestEntity requestEntity) {
		String clientName = requestEntity.getRequestBelongsTo().getName();
		String clientNumber = requestEntity.getRequestBelongsTo().getPhone();
		String clientEmail = requestEntity.getRequestBelongsTo().getEmail();
		return requestEntity.getReceiptMethod().equals(Constants.ReceiptMethod.SELF_SERVICE.toString()) ?  clientName : 
			requestEntity.getReceiptMethod().equals(Constants.ReceiptMethod.EMAIL.toString()) ? clientName + " (phone: "+ clientNumber + ")" :
				clientName + " (email: " + clientEmail +")";
	}
	
	private boolean authorize(RequestEntity requestEntity, UserEntity userEntity) {
		if (requestEntity.getCreatorEntity() != null && 
				requestEntity.getCreatorEntity().getId() == userEntity.getId() ||
				requestEntity.getRequestBelongsTo().getId() == userEntity.getId()) {
			return true;
		} else if (admin() || director()) {
			return true;
		} else if (requestEntity.getEngineerEntity() != null && 
				userEntity.getId() == requestEntity.getEngineerEntity().getId()) {
			return true;
		}
		return false;
	}
	
	protected void notificationHandler(RequestEntity requestEntity, String message) {
		if (requestEntity.getCreatorEntity().getId() == requestEntity.getRequestBelongsTo().getId()) {
			sendNotifications(new UserEntity[]{requestEntity.getCreatorEntity()}, message);
			sendToUser(requestEntity.getCreatorEntity(), message);
		} else {
			sendNotifications(new UserEntity[]{requestEntity.getCreatorEntity(), 
					requestEntity.getRequestBelongsTo()}, message);
			sendToUser(requestEntity.getCreatorEntity(), message);
			sendToUser(requestEntity.getRequestBelongsTo(), message);
		}
	}

	private void sendNotifications(UserEntity[] entities, String message) {
		notificationService.merge(requestEntity, Arrays.asList(entities), message);
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
						if (userEntity.getId() != getLoggedUser().getId()) {
							notificationService.merge(requestEntity, Arrays.asList(new UserEntity[]{userEntity}), Constants.NEW_ASSIGMENT);
							sendToUser(userEntity, Constants.NEW_ASSIGMENT);
						}
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
	
}
