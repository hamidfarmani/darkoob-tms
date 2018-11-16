package controller;

import entity.Tbugstate;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import model.bugstate_dao;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "bugStateControl")
@SessionScoped
public class bugStateController implements Serializable {

	@ManagedProperty(value = "#{loginControl}")
	private loginController loginControl;

	private Tbugstate bugState;
	
	public void viewEditBugDialog(Tbugstate bugState) {
		this.bugState = bugState;
		
		Map<String, Object> options = new HashMap<>();
		options.put("modal", true);
		options.put("resizable", false);
		options.put("scrollabale", false);
		RequestContext.getCurrentInstance().openDialog("editBugState", options, null);
	}

	public void viewNewBugStateDialog() {
		Map<String, Object> options = new HashMap<>();
		options.put("modal", true);
		options.put("resizable", false);
		options.put("scrollabale", false);
		resetInputs();
		RequestContext.getCurrentInstance().openDialog("createBugState", options, null);
	}
	
	public String insertBugState() {
		boolean isError = true;
		String errMessage = "";

		bugstate_dao stateDao = new bugstate_dao();

		bugState.setName(bugState.getName().trim());
		bugState.setDescription(bugState.getDescription().trim());

		if (bugState.getName().isEmpty()) {
			errMessage = "نام را وارد کنید.";
		} else if (bugState.getName().length() > 50) {
			errMessage = "نام باید حداکثر 50 کاراکتر باشد.";
		} else if (stateDao.getBugstateByName(bugState.getName()) != null) {
			errMessage = "نام وارد شده تکراری است";
		} else if (bugState.getDescription().length() > 500) {
			errMessage = "توضیحات باید حداکثر 500 کاراکتر باشد.";
		} else {
			isError = false;
		}

		if (isError) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", errMessage));
		} else {
			bugState.setCreatedbyid(loginControl.getEnteredUser().getId());

			stateDao.createBugstate(bugState);
			closeDialog("createBugState");
		}
		
		return "";
	}
	
	public String updateBugState() {
		boolean isError = true;
		String errMessage = "";

		bugstate_dao stateDao = new bugstate_dao();

		bugState.setName(bugState.getName().trim());
		bugState.setDescription(bugState.getDescription().trim());

		if (bugState.getName().isEmpty()) {
			errMessage = "نام را وارد کنید.";
		} else if (bugState.getName().length() > 50) {
			errMessage = "نام باید حداکثر 50 کاراکتر باشد.";
		} else if (stateDao.getBugstateByName(bugState.getName()) != null) {
			errMessage = "نام وارد شده تکراری است";
		} else if (bugState.getDescription().length() > 500) {
			errMessage = "توضیحات باید حداکثر 500 کاراکتر باشد.";
		} else {
			isError = false;
		}

		if (isError) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", errMessage));
		} else {
			bugState.setCreatedbyid(loginControl.getEnteredUser().getId());

			stateDao.updateBugstate(bugState);
			closeDialog("editBugState");
		}
		
		return "";
	}
	
	public void resetInputs() {
		bugState = new Tbugstate();
	}
	
	public void closeDialog(String name) {
		RequestContext.getCurrentInstance().closeDialog(name);
	}
	
	public loginController getLoginControl() {
		return loginControl;
	}

	public void setLoginControl(loginController loginControl) {
		this.loginControl = loginControl;
	}
	
	public Tbugstate getBugState() {
		return bugState;
	}

	public void setBugStateModel(Tbugstate bugState) {
		this.bugState = bugState;
	}
	
}
