package controller;

import entity.Tbugtype;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import model.bugtype_dao;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "bugTypeControl")
@SessionScoped
public class bugTypeController implements Serializable {

	@ManagedProperty(value = "#{loginControl}")
	private loginController loginControl;

	private Tbugtype bugType;

	public void viewNewBugTypeDialog() {
		Map<String, Object> options = new HashMap<>();
		options.put("modal", true);
		options.put("resizable", false);
		options.put("scrollabale", false);
		resetInputs();
		bugType = new Tbugtype();
		RequestContext.getCurrentInstance().openDialog("createBugType", options, null);
	}

	public void addNewBugType() {
		bugtype_dao setbugtype = new bugtype_dao();
		bugType.setName(bugType.getName().trim());
		bugType.setDescription(bugType.getDescription().trim());
		if (bugType.getName().length() < 200) {
			if (bugType.getName() != null) {
				if (setbugtype.getBugtypeByName(bugType.getName()) == null) {
					if (bugType.getDescription().length() < 2000) {
						bugType.setCreatedbyid(loginControl.getEnteredUser().getId());
						setbugtype.createBugtype(bugType);
						closeDialog("createBugType");
					} else {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "توضيحات وارد شده بيش از حد مجاز است"));

					}
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "عنوان وارد شده تکراري مي باشد"));

				}

			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "فيلم عنوان را پر نماييد"));

			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "تعداد کاراکتر وارد شده بيش از حد مجاز است"));

		}
	}

	public Tbugtype getBuggType() {
		return bugType;
	}

	public void setBuggType(Tbugtype buggtype) {
		this.bugType = buggtype;
	}

	public void resetInputs() {
		bugType = new Tbugtype();
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

}
