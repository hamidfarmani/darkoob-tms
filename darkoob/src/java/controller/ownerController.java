package controller;

import entity.Towner;
import entity.Tproject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import model.owner_dao;
import model.project_dao;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "ownerControl")
@SessionScoped
public class ownerController implements Serializable {

    @ManagedProperty(value = "#{loginControl}")
    private loginController loginControl;

    private List<Towner> ownerList = new ArrayList();
    private Towner owner = new Towner();

    public ownerController() {
        owner_dao dao = new owner_dao();
        ownerList = dao.getAllOwners();
    }

    public void viewNewOwnerDialog() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        options.put("contentHeight", 300);
        owner = new Towner();
        RequestContext.getCurrentInstance().openDialog("createOwner", options, null);
    }

    public void viewEditOwnerDialog(Towner owner) {
        this.owner = owner;
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        options.put("contentHeight", 310);
        RequestContext.getCurrentInstance().openDialog("editOwner", options, null);
    }

    public void closeDialog(String name) {
        RequestContext.getCurrentInstance().closeDialog(name);
    }

    public String insertOwner() {
        owner.setName(owner.getName().trim());
        owner.setDescription(owner.getDescription().trim());

        if (!owner.getName().equals("")) {
            if (owner.getName().length() <= 50) {
                if (owner.getDescription().length() <= 500) {
                    owner_dao dao = new owner_dao();
                    Towner ownerFromDB = dao.getOwnerByName(owner.getName());
                    if (ownerFromDB == null) {
                        owner.setTuser(loginControl.getEnteredUser());
                        dao.createOwner(owner);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "عملیات موفق", "عملیات با موفقیت انجام شد."));
                        refreshList();
                        closeDialog("createOwner");
                        return "";
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام کارفرما وارد شده تکراری است."));
                        return "";
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "توضیحات کارفرما باید حداکثر 500 کاراکتر باشد."));
                    return "";
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام کارفرما باید حداکثر 50 کاراکتر باشد."));
                return "";
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام کارفرما را وارد کنید."));
            return "";
        }
    }

    public String update() {

        owner.setName(owner.getName().trim());
        owner.setDescription(owner.getDescription().trim());

        if (!owner.getName().equals("")) {
            if (owner.getName().length() <= 50) {
                if (owner.getDescription().length() <= 500) {
                    owner_dao dao = new owner_dao();
                    Towner ownerFromDB = dao.getOwnerByName(owner.getName());
                    if (ownerFromDB == null || ownerFromDB.getId().compareTo(owner.getId()) == 0) {
                        dao.updateOwner(owner);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "عملیات موفق", "عملیات با موفقیت انجام شد."));
                        refreshList();
                        closeDialog("editOwner");
                        return "";
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام کارفرما وارد شده تکراری است."));
                        return "";
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "توضیحات کارفرما باید حداکثر 500 کاراکتر باشد."));
                    return "";
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام کارفرما باید حداکثر 50 کاراکتر باشد."));
                return "";
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام کارفرما را وارد کنید."));
            return "";
        }
    }

    public void removeOwner(Towner owner) {
        project_dao projectDAO = new project_dao();
        owner_dao ownerDAO = new owner_dao();
        List<BigDecimal> projectsByOwnerID = projectDAO.getProjectsByOwnerID(owner.getId());
        if (projectsByOwnerID == null) {
            owner.setDto(new Date());
            ownerDAO.updateOwner(owner);
            refreshList();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "امکان حذف وجود ندارد", "پروژه ای به این کارفرما منتسب شده است. لیست پروژه ها: " + projectsByOwnerID));
        }

    }

    public void refreshList() {
        owner_dao dao = new owner_dao();
        ownerList = dao.getAllOwners();
    }

    public boolean trimFilterValue(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim();
        if (filterText == null || filterText.equals("")) {
            return true;
        }

        if (value == null) {
            return false;
        }
        String list = value.toString().toLowerCase();
        filterText = filterText.toLowerCase();
        if (list.contains(filterText)) {
            return true;
        } else {
            return false;
        }
    }

    //***********************Setters & Getters******************************//
    public loginController getLoginControl() {
        return loginControl;
    }

    public void setLoginControl(loginController loginControl) {
        this.loginControl = loginControl;
    }

    public Towner getOwner() {
        return owner;
    }

    public void setOwner(Towner owner) {
        this.owner = owner;
    }

    public List<Towner> getOwnerList() {
        return ownerList;
    }

    public void setOwnerList(List ownerList) {
        this.ownerList = ownerList;
    }
}
