package controller;

import entity.Tbugfile;
import entity.Towner;
import entity.Tproject;
import entity.Tprojectfile;
import entity.Tprojecttree;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import model.owner_dao;
import model.projectFile_dao;
import model.projectTree_dao;
import model.project_dao;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

@ManagedBean(name = "projectControl")
@SessionScoped
public class projectController implements Serializable {

    @ManagedProperty(value = "#{loginControl}")
    private loginController loginControl;

    @ManagedProperty(value = "#{bugControl}")
    private bugController bugControl;

    @ManagedProperty(value = "#{dashboardControl}")
    private dashboardController dashboardControl;

    @ManagedProperty(value = "#{executeTestcaseControl}")
    private executeTestcaseController executeTestcaseControl;

    @ManagedProperty(value = "#{iterationControl}")
    private iterationController iterationControl;

    @ManagedProperty(value = "#{projectTreeControl}")
    private projectTreeController projectTreeControl;

    @ManagedProperty(value = "#{projecttreetypeControl}")
    private projecttreetypeController projectTreeTypeControl;

    @ManagedProperty(value = "#{requirementControl}")
    private requirementController requirementControl;

    @ManagedProperty(value = "#{testcaseControl}")
    private testcaseController testcaseControl;

    @ManagedProperty(value = "#{versionControl}")
    private versionController versionControl;

    private List<Tproject> projectList = new ArrayList();
    private List<Tprojectfile> projectFiles = new ArrayList();
    private Tproject project = new Tproject();
    private BigDecimal ownerID;
    private String newName;
    private BigDecimal selectedProjectID;

    private String destination = "/app/darkoob/uploadedFiles/ProjectFiles/";

    @PostConstruct
    public void init() {
        selectedProjectID = BigDecimal.valueOf(-1);
        refreshList();
        onSelectedProjectChange();
        dashboardControl.refreshDashboard();
    }

    public void viewNewProjectDialog() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        options.put("contentHeight", 440);
        resetInputs();
        RequestContext.getCurrentInstance().openDialog("createProject", options, null);
    }

    public void viewEditProjectDialog(Tproject project) {
        this.project = project;
        newName = project.getName();
        ownerID = project.getTowner().getId();
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        options.put("contentHeight", 320);
        RequestContext.getCurrentInstance().openDialog("editProject", options, null);
    }

    public void viewAttachmentDialog(Tproject project) {
        this.project = project;
        System.out.println("hereee");
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        options.put("contentHeight", 440);
        RequestContext.getCurrentInstance().openDialog("attachFileToProject", options, null);
    }

    public void closeDialog(String name) {
        RequestContext.getCurrentInstance().closeDialog(name);
    }

    public String insertProject() {

        project.setName(project.getName().trim());
        project.setDescription(project.getDescription().trim());

        if (!project.getName().equals("")) {
            if (project.getName().length() <= 50) {
                if (project.getDescription().length() <= 500) {

                    project_dao dao = new project_dao();
                    owner_dao ownerDao = new owner_dao();

                    Tproject projectFromDB = dao.getProjectByName(project.getName());
                    if (projectFromDB == null) {
                        if (ownerID != null) {
                            Towner owner = ownerDao.getOwnerByID(ownerID);
                            project.setTowner(owner);
                        }
                        project.setTuser(loginControl.getEnteredUser());
                        dao.createProject(project);

                        projectTree_dao projectTreeDao = new projectTree_dao();
                        Tprojecttree projectTree = new Tprojecttree();
                        projectTree.setName(project.getName());
                        projectTree.setTuser(loginControl.getEnteredUser());
                        projectTree.setTproject(project);
                        projectTreeDao.createProjecttree(projectTree);

                        Tprojecttree rootInList = new Tprojecttree();
                        rootInList.setName(project.getName());
                        rootInList.setTuser(loginControl.getEnteredUser());
                        rootInList.setTprojecttree(projectTree);
                        rootInList.setTproject(project);
                        projectTreeDao.createProjecttree(rootInList);

                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "عملیات موفق", "عملیات با موفقیت انجام شد."));
                        refreshList();
                        closeDialog("createProject");
                        return "";
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام پروژه وارد شده تکراری است."));
                        return "";
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "توضیحات پروژه باید حداکثر 500 کاراکتر باشد."));
                    return "";
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام پروژه باید حداکثر 50 کاراکتر باشد."));
                return "";
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام پروژه را وارد کنید."));
            return "";
        }
    }

    public String update() {

        newName = newName.trim();
        project.setDescription(project.getDescription().trim());

        if (!newName.equals("")) {
            if (newName.length() <= 50) {
                if (project.getDescription().length() <= 500) {
                    project_dao dao = new project_dao();
                    owner_dao ownerDao = new owner_dao();
                    if (!project.getName().equals(newName)) {
                        projectTree_dao projectTreeDao = new projectTree_dao();
                        project.setName(newName);
                        Tprojecttree projecttreeRoot = projectTreeDao.getProjecttreeRootByProjectID(project.getId());
                        projecttreeRoot.setName(newName);
                        List<Tprojecttree> childrenByParentID = projectTreeDao.getChildrenByParentID(projecttreeRoot.getId());
                        if (childrenByParentID != null) {
                            Tprojecttree visibleRoot = childrenByParentID.get(0);
                            visibleRoot.setName(newName);
                            projectTreeDao.updateProjecttree(visibleRoot);
                        }
                        projectTreeDao.updateProjecttree(projecttreeRoot);
                    }
                    Towner owner = ownerDao.getOwnerByID(ownerID);
                    project.setTowner(owner);
                    Tproject projectFromDB = dao.getProjectByName(project.getName());
                    if (projectFromDB == null || projectFromDB.getId().compareTo(project.getId()) == 0) {
                        dao.updateProject(project);
                        refreshList();
                        resetInputs();
                        closeDialog("editProject");
                        return "";
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام پروژه وارد شده تکراری است."));
                        return "";
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "توضیحات پروژه باید حداکثر 500 کاراکتر باشد."));
                    return "";
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام پروژه باید حداکثر 50 کاراکتر باشد."));
                return "";
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام پروژه را وارد کنید."));
            return "";
        }

    }

    public void removeProject(Tproject project) {
        project_dao dao = new project_dao();
        projectTree_dao projectTreeDao = new projectTree_dao();
        List<Tprojecttree> projecttreeByProjectID = projectTreeDao.getProjecttreeByProjectID(project.getId());
        if (projecttreeByProjectID == null || projecttreeByProjectID.size() <= 1) {
            if (projecttreeByProjectID != null && projecttreeByProjectID.size() <= 1) {
                Tprojecttree projecttreeRoot = projectTreeDao.getProjecttreeRootByProjectID(project.getId());
                projecttreeRoot.setDto(new Date());
                projectTreeDao.updateProjecttree(projecttreeRoot);
            }
            project.setDto(new Date());
            dao.updateProject(project);
            refreshList();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "امکان حذف وجود ندارد", "برای این پروژه درخت تعریف شده است."));
        }
    }

    public void upload(FileUploadEvent event) {
        try {
            projectFile_dao dao = new projectFile_dao();
            Tprojectfile projectFile = new Tprojectfile();
            String fileName = event.getFile().getFileName();
            String fileType = fileName.substring(fileName.indexOf("."));
            projectFile.setFilename(fileName);
            projectFile.setTproject(project);
            projectFile.setCreatedbyid(loginControl.getEnteredUser().getId());
            projectFile.setFilepath(destination);
            projectFile.setCreationdate(new Date());
            BigDecimal testcaseFileID = dao.createProjectFile(projectFile);
            saveUploadedFile(String.valueOf(testcaseFileID) + fileType, event.getFile().getInputstream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void saveUploadedFile(String fileName, InputStream in) {
        try {
            OutputStream out = new FileOutputStream(new File(destination + fileName));
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            in.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void download(Tbugfile selectedFile) {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext externalContext = context.getExternalContext();

            String type = selectedFile.getFilename().substring(selectedFile.getFilename().indexOf(".") + 1);
            externalContext.responseReset();
            externalContext.setResponseContentType("image/" + type);
            externalContext.setResponseHeader("Content-Disposition", "attachment;filename=" + selectedFile.getId() + "." + type);

            FileInputStream inputStream = new FileInputStream(new File(selectedFile.getFilepath() + selectedFile.getId() + "." + type));

            OutputStream outputStream = externalContext.getResponseOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            inputStream.close();
            context.responseComplete();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void remove(Tprojectfile selectedFile) {
        projectFile_dao dao = new projectFile_dao();
        selectedFile.setDto(new Date());
        dao.updateProjectFile(selectedFile);
    }

    public void onSelectedProjectChange() {

        if (selectedProjectID != null && selectedProjectID.compareTo(BigDecimal.valueOf(-1)) != 0) {
            dashboardControl.setProjectID(selectedProjectID);
        }

        bugControl.setProjectID(selectedProjectID);
        bugControl.onProjectChange();
        bugControl.refreshList();

        executeTestcaseControl.setProjectID(selectedProjectID);
        executeTestcaseControl.setIterationItems(executeTestcaseControl.getIterationComboItem());
        executeTestcaseControl.setIterationID(null);
        executeTestcaseControl.refreshList();
        executeTestcaseControl.onProjectChange();

        iterationControl.setProjectID(selectedProjectID);
        iterationControl.onProjectChange();

        projectTreeControl.setProjectID(selectedProjectID);
        projectTreeControl.onProjectChange();

        projectTreeTypeControl.setProjectID(selectedProjectID);
        projectTreeTypeControl.onProjectChange();

        requirementControl.setProjectID(selectedProjectID);
        requirementControl.onProjectChange();
        requirementControl.refreshList();

        testcaseControl.setProjectID(selectedProjectID);
        testcaseControl.onProjectChange();
        testcaseControl.refreshList();

        versionControl.setProjectID(selectedProjectID);
        versionControl.onProjectChange();
        versionControl.refreshList();

    }

    public void resetInputs() {
        newName = "";
        project.setName("");
        project.setDescription("");
    }

    public void refreshList() {
        project_dao dao = new project_dao();
        projectList = dao.getAllProjects();
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
    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public dashboardController getDashboardControl() {
        return dashboardControl;
    }

    public void setDashboardControl(dashboardController dashboardControl) {
        this.dashboardControl = dashboardControl;
    }

    public bugController getBugControl() {
        return bugControl;
    }

    public void setBugControl(bugController bugControl) {
        this.bugControl = bugControl;
    }

    public executeTestcaseController getExecuteTestcaseControl() {
        return executeTestcaseControl;
    }

    public void setExecuteTestcaseControl(executeTestcaseController executeTestcaseControl) {
        this.executeTestcaseControl = executeTestcaseControl;
    }

    public iterationController getIterationControl() {
        return iterationControl;
    }

    public void setIterationControl(iterationController iterationControl) {
        this.iterationControl = iterationControl;
    }

    public projectTreeController getProjectTreeControl() {
        return projectTreeControl;
    }

    public void setProjectTreeControl(projectTreeController projectTreeControl) {
        this.projectTreeControl = projectTreeControl;
    }

    public requirementController getRequirementControl() {
        return requirementControl;
    }

    public void setRequirementControl(requirementController requirementControl) {
        this.requirementControl = requirementControl;
    }

    public testcaseController getTestcaseControl() {
        return testcaseControl;
    }

    public void setTestcaseControl(testcaseController testcaseControl) {
        this.testcaseControl = testcaseControl;
    }

    public versionController getVersionControl() {
        return versionControl;
    }

    public void setVersionControl(versionController versionControl) {
        this.versionControl = versionControl;
    }

    public loginController getLoginControl() {
        return loginControl;
    }

    public void setLoginControl(loginController loginControl) {
        this.loginControl = loginControl;
    }

    public BigDecimal getSelectedProject() {
        return selectedProjectID;
    }

    public void setSelectedProject(BigDecimal selectedProject) {
        this.selectedProjectID = selectedProject;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public BigDecimal getOwnerID() {
        if (!project.getName().equals("")) {
            ownerID = project.getTowner().getId();
        }
        return ownerID;
    }

    public void setOwnerID(BigDecimal ownerID) {
        this.ownerID = ownerID;
    }

    public Tproject getProject() {
        return project;
    }

    public void setProject(Tproject project) {
        this.project = project;
    }

    public List<Tproject> getProjectList() {
        return projectList;
    }

    public projecttreetypeController getProjectTreeTypeControl() {
        return projectTreeTypeControl;
    }

    public void setProjectTreeTypeControl(projecttreetypeController projectTreeTypeControl) {
        this.projectTreeTypeControl = projectTreeTypeControl;
    }

    public void setProjectList(List projectList) {
        this.projectList = projectList;
    }

    public List<Tprojectfile> getProjectFiles() {
        if (project.getId() != null) {
            projectFile_dao dao = new projectFile_dao();
            projectFiles = dao.getProjectFileByProjectID(project.getId());
            return projectFiles;
        } else {
            return new ArrayList();
        }
    }

    public List<SelectItem> getAllOwnersList() {
        owner_dao dao = new owner_dao();
        List<SelectItem> items = new ArrayList<SelectItem>();
        List<Towner> ownersList = dao.getAllOwners();
        for (Towner owner : ownersList) {
            items.add(new SelectItem(owner.getId(), owner.getName()));
        }
        return items;
    }

    public List<SelectItem> getAllProjectList() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        items.add(new SelectItem(-1, "همه ی پروژه ها"));
        if (projectList != null) {
            for (Tproject pro : projectList) {
                items.add(new SelectItem(pro.getId(), pro.getName()));
            }
        }
        return items;
    }

}
