package controller;

import entity.Tprojecttreetype;
import entity.Tproject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import model.projecttreetype_dao;
import model.project_dao;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "projecttreetypeControl")
@SessionScoped
public class projecttreetypeController implements Serializable {

    @ManagedProperty(value = "#{loginControl}")
    private loginController loginControl;

    List<SelectItem> projectItems = new ArrayList<SelectItem>();

    private List<Tprojecttreetype> projecttreetypeList = new ArrayList();
    private Tprojecttreetype projecttreetype = new Tprojecttreetype();
    private BigDecimal projectID;

    @ManagedProperty(value = "#{projectTreeControl}")
    private projectTreeController projectTreeController;

    public void viewNewProjecttreetypeDialog() {
        System.out.println("pro: " + projectID);
        if (projectID != null && projectID.compareTo(BigDecimal.valueOf(-1)) != 0) {
            Map<String, Object> options = new HashMap<String, Object>();
            options.put("modal", true);
            options.put("resizable", false);
            resetInputs();
            RequestContext.getCurrentInstance().openDialog("createProjecttreeType", options, null);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "هشدار", "ابتدا پروژه را انتخاب کنید.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void viewEditDialog() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        RequestContext.getCurrentInstance().openDialog("editProjecttreetype", options, null);
    }

    public void closeDialog(String name) {
        RequestContext.getCurrentInstance().closeDialog(name);
    }

    public String insertProjecttreetype() {
        projecttreetype_dao projecttreetype_dao = new projecttreetype_dao();
        project_dao project_dao = new project_dao();
        Tproject project = project_dao.getProjectByID(projectTreeController.getProjectID());
        projecttreetype.setTproject(project);
        projecttreetype.setCreatedbyid(loginControl.getEnteredUser().getId());
        projecttreetype_dao.createProjecttreetype(projecttreetype);
        resetInputs();
        closeDialog("createProjecttreeType");
        return "";
    }

    public String update() {
        projecttreetype_dao dao = new projecttreetype_dao();
        dao.updateProjecttreetype(projecttreetype);
        closeDialog("editProjecttreetype");
        return "";
    }

    public void removeProjecttreetypeNode() {

        //          FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "هشدار", "لطفا یک مورد را انتخاب کنید.");
        //         FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void resetInputs() {
        projecttreetype.setName(null);
        projecttreetype.setDescription(null);
        projecttreetype.setTproject(null);
    }

    public void onProjectChange() {
        projecttreetype_dao projecttreeType = new projecttreetype_dao();
        projecttreetypeList = projecttreeType.getProjecttreetypeByProjectID(projectID);
    }

    //***********************Setters & Getters******************************//
    public projectTreeController getProjectTreeController() {
        return projectTreeController;
    }

    public loginController getLoginControl() {
        return loginControl;
    }

    public void setLoginControl(loginController loginControl) {
        this.loginControl = loginControl;
    }

    public void setProjectTreeController(projectTreeController projectTreeController) {
        this.projectTreeController = projectTreeController;
    }

    public List<SelectItem> getProjectItems() {
        return projectItems;
    }

    public BigDecimal getProjectID() {
        return projectID;
    }

    public void setProjectID(BigDecimal projectID) {
        this.projectID = projectID;
    }

    public Tprojecttreetype getProjecttreetype() {
        return projecttreetype;
    }

    public void setProjecttreetype(Tprojecttreetype projecttreetype) {
        this.projecttreetype = projecttreetype;
    }

    public List<Tprojecttreetype> getProjecttreetypeList() {
        projecttreetype_dao dao = new projecttreetype_dao();
        return dao.getAllProjecttreetypes();
    }

    public List<SelectItem> getProjectComboItem() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        project_dao dao = new project_dao();
        List<Tproject> projectList = dao.getAllProjects();
        items.add(new SelectItem(-1, ""));
        for (Tproject project : projectList) {
            items.add(new SelectItem(project.getId(), project.getName()));
        }
        return items;
    }

    public List<SelectItem> getProjecttreetypeComboItem() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        items.clear();
        projecttreetype_dao dao = new projecttreetype_dao();
        try {
            items.add(new SelectItem(-1, ""));
            if (projectID != null && projectID.compareTo(BigDecimal.valueOf(-1)) != 0) {
                List<Tprojecttreetype> typeList = dao.getProjecttreetypeByProjectID(projectID);
                for (Tprojecttreetype type : typeList) {
                    items.add(new SelectItem(type.getId(), type.getName()));
                }
            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "هشدار", "ابتدا نوع گره را تعریف کنید.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            items.clear();
        }

        return items;
    }
}
