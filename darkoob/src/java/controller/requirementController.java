package controller;

import entity.Tbugfile;
import entity.Tproject;
import entity.Tprojectfile;
import entity.Tprojecttree;
import entity.TprojecttreeReq;
import entity.Trequirement;
import entity.Trequirementfile;
import entity.Ttestcase;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import model.projectFile_dao;
import model.projectTree_dao;
import model.project_dao;
import model.requirementFile_dao;
import model.requirementProjectTree_dao;
import model.requirement_dao;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

@ManagedBean(name = "requirementControl")
@SessionScoped
public class requirementController implements Serializable {

    @ManagedProperty(value = "#{loginControl}")
    private loginController loginControl;

    @ManagedProperty(value = "#{projectTreeControl}")
    private projectTreeController projectTreeContInReq;

    private List<Trequirementfile> requirementFiles = new ArrayList();
    private String destination = "/app/darkoob/uploadedFiles/RequirementFiles/";

    private List<Trequirement> requirementList = new ArrayList();
    private Trequirement requirement = new Trequirement();
    private List<BigDecimal> selectedProjectTreeList = new ArrayList();

    private TreeNode root;
    private TreeNode selectedNode;
    private TreeNode[] selectedNodes;
    private List<Tprojecttree> projectTreeList = new ArrayList();
    private Tprojecttree selectedProjectTree;

    private BigDecimal projectID;

    public void viewAttachmentDialog(Trequirement requirement) {
        this.requirement = requirement;
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        options.put("contentHeight", 440);
        RequestContext.getCurrentInstance().openDialog("attachFileToRequirement", options, null);
    }

    public void viewNewRequirementDialog() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        options.put("contentHeight", 500);
        resetInputs();
        RequestContext.getCurrentInstance().openDialog("createRequirement", options, null);
    }

    public void viewEditRequirementDialog(Trequirement requirement) {
        this.requirement = requirement;
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        options.put("contentHeight", 300);
        RequestContext.getCurrentInstance().openDialog("editRequirement", options, null);
    }

    public void viewRequirementDialog(Trequirement requirement) {
        this.requirement = requirement;
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        options.put("contentHeight", 500);
        RequestContext.getCurrentInstance().openDialog("viewRequirement", options, null);
    }

    public void viewAssignToTreeDialog(Trequirement requirement) {
        this.requirement = requirement;
        requirementProjectTree_dao requirementProjecttreeDAO = new requirementProjectTree_dao();
        List<TprojecttreeReq> reqProjectTree = requirementProjecttreeDAO.getProjectTreeRequirementByRequirementID(requirement.getId());

        if (reqProjectTree != null) {
            projectID = reqProjectTree.get(0).getTprojecttree().getTproject().getId();
            onProjectChange();
            selectedProjectTreeList = getSelectedProjectTreeList();
            initProjectTree();
        }

        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        options.put("contentHeight", 500);
        RequestContext.getCurrentInstance().openDialog("assignRequirement", options, null);
    }

    public void closeDialog(String name) {
        RequestContext.getCurrentInstance().closeDialog(name);
    }

    public String assignToProjectTree() {
        requirementProjectTree_dao reqTreeDAO = new requirementProjectTree_dao();
        reqTreeDAO.deleteByRequirementID(requirement.getId());
        if (selectedNodes.length > 0) {
            requirementProjectTree_dao projectTreeReqDao = new requirementProjectTree_dao();
            for (int i = 0; i < selectedNodes.length; i++) {
                Tprojecttree projectTree = (Tprojecttree) selectedNodes[i].getData();
                TprojecttreeReq projectTreeReq = new TprojecttreeReq(projectTree, requirement);
                projectTreeReqDao.createProjectTreeRequirement(projectTreeReq);
            }
        }
        refreshList();
        closeDialog("assignRequirement");
        resetInputs();
        return "";
    }

    public String insertRequirement() {

        requirement.setSymbol(requirement.getSymbol().trim());
        requirement.setDescription(requirement.getDescription().trim());

        if (requirement.getSymbol().length() <= 50) {
            if (!requirement.getDescription().equals("")) {
                if (requirement.getDescription().length() <= 1000) {
                    if (projectID != null && projectID.compareTo(BigDecimal.valueOf(-1)) != 0) {
                        requirement_dao dao = new requirement_dao();
                        project_dao projectDao = new project_dao();
                        Trequirement requirementFromDB = dao.getRequirementsBySymbol(requirement.getSymbol());
                        if (requirement.getSymbol().equals("") || requirementFromDB == null) {

                            Tproject project = projectDao.getProjectByID(projectID);
                            requirement.setTproject(project);

                            requirement.setTuser(loginControl.getEnteredUser());
                            dao.createRequirement(requirement);

                            if (selectedNodes.length > 0) {
                                requirementProjectTree_dao projectTreeReqDao = new requirementProjectTree_dao();
                                for (int i = 0; i < selectedNodes.length; i++) {
                                    Tprojecttree projectTree = (Tprojecttree) selectedNodes[i].getData();
                                    TprojecttreeReq projectTreeReq = new TprojecttreeReq(projectTree, requirement);
                                    projectTreeReqDao.createProjectTreeRequirement(projectTreeReq);
                                }
                            }
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "عملیات موفق", "عملیات با موفقیت انجام شد."));
                            refreshList();
                            closeDialog("createRequirement");
                            return "";
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نشانه نیازمندی تکراری است."));
                            return "";
                        }
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "پروژه را انتخاب کنید."));
                        return "";
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "توصیف نیازمندی باید حداکثر 1000 کاراکتر باشد."));
                    return "";
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "توصیف نیازمندی را وارد کنید."));
                return "";
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام نیازمندی باید حداکثر 50 کاراکتر باشد."));
            return "";
        }

    }

    public String update() {

        requirement.setSymbol(requirement.getSymbol().trim());
        requirement.setDescription(requirement.getDescription().trim());

        if (requirement.getSymbol().length() <= 50) {
            if (!requirement.getDescription().equals("")) {
                if (requirement.getDescription().length() <= 1000) {
                    requirement_dao dao = new requirement_dao();
                    Trequirement requirementFromDB = dao.getRequirementsBySymbol(requirement.getSymbol());
                    if (requirementFromDB == null || requirement.getSymbol().equals("") || requirementFromDB.getId().compareTo(requirement.getId()) == 0) {
                        dao.updateRequirement(requirement);
                        refreshList();
                        closeDialog("editRequirement");
                        resetInputs();
                        return "";
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نشانه نیازمندی تکراری است."));
                        return "";
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "توصیف نیازمندی باید حداکثر 500 کاراکتر باشد."));
                    return "";
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "توصیف نیازمندی را وارد کنید."));
                return "";
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام نیازمندی باید حداکثر 50 کاراکتر باشد."));
            return "";
        }
    }

    public void removeRequirement(Trequirement requirement) {
        requirement_dao dao = new requirement_dao();
        requirementProjectTree_dao reqProjecttreeDAO = new requirementProjectTree_dao();
        reqProjecttreeDAO.deleteByRequirementID(requirement.getId());
        requirement.setDto(new Date());
        dao.updateRequirement(requirement);
        refreshList();
    }

    public void initProjectTree() {
        projectTree_dao projectTree_dao = new projectTree_dao();
        if (projectID != null && !projectID.equals(BigDecimal.valueOf(-1))) {
            projectTreeList = projectTree_dao.getProjecttreeByProjectID(projectID);
            if (projectTreeList != null) {
                Tprojecttree projectRoot = projectTreeList.get(0);
                root = newNodeWithChildren(projectRoot, null);
            } else {
                root = null;
            }
        }
    }

    public TreeNode newNodeWithChildren(Tprojecttree ttParent, TreeNode parent) {
        projectTree_dao projectTree_dao = new projectTree_dao();
        TreeNode newNode = new DefaultTreeNode(ttParent, parent);
        Tprojecttree projTree = (Tprojecttree) newNode.getData();
        if (selectedProjectTreeList != null && selectedProjectTreeList.contains(projTree.getId())) {
            TreeNode t = newNode;
            newNode.setSelected(true);
            while (t.getParent() != null) {
                t.setExpanded(true);
                t = t.getParent();
            }
        }

        List<Tprojecttree> children = getChildrenByParentID(ttParent.getId());
        if (children == null) {
            TreeNode n = new DefaultTreeNode("", null);
            return n;
        }
        for (Tprojecttree tt : children) {
            TreeNode newNode2 = newNodeWithChildren(tt, newNode);
        }
        return newNode;
    }

    public List<Tprojecttree> getChildrenByParentID(BigDecimal parenID) {
        List<Tprojecttree> childerenList = new ArrayList();
        for (int i = 1; i < projectTreeList.size(); i++) {
            Tprojecttree tt = projectTreeList.get(i);
            if (tt.getTprojecttree().getId().equals(parenID)) {
                childerenList.add(tt);
            }
        }
        return childerenList;
    }

    public void upload(FileUploadEvent event) {
        try {
            requirementFile_dao dao = new requirementFile_dao();
            Trequirementfile requirmentFile = new Trequirementfile();
            String fileName = event.getFile().getFileName();
            String fileType = fileName.substring(fileName.indexOf("."));
            requirmentFile.setFilename(fileName);
            requirmentFile.setTrequirement(requirement);
            requirmentFile.setCreatedbyid(loginControl.getEnteredUser().getId());
            requirmentFile.setFilepath(destination);
            requirmentFile.setCreationdate(new Date());
            BigDecimal reqFileID = dao.createRequirementFile(requirmentFile);
            saveUploadedFile(String.valueOf(reqFileID) + fileType, event.getFile().getInputstream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void saveUploadedFile(String fileName, InputStream in) {
        try {
            OutputStream out = new FileOutputStream(new File(destination + fileName));
            int read = 0;
            byte[] bytes = new byte[5000000];
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            in.close();
            out.flush();
            out.close();
            System.out.println("New file created!");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void download(Trequirementfile selectedFile) {
        try {
            System.out.println("File path: " + new File("darkoob").getAbsolutePath());
            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext externalContext = context.getExternalContext();

            String type = selectedFile.getFilename().substring(selectedFile.getFilename().indexOf(".") + 1);
            System.out.println("TYPE: " + type);
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

    public void remove(Trequirementfile selectedFile) {
        requirementFile_dao dao = new requirementFile_dao();
        selectedFile.setDto(new Date());
        dao.updateRequirementFile(selectedFile);
    }

    public TreeNode[] getSelectedNodes() {
//        if (requirement != null) {
//            int nodesCounter = 0;
//            requirementProjectTree_dao projecttreeReqDao = new requirementProjectTree_dao();
//            projectTree_dao projecttreeDao = new projectTree_dao();
//            List<TprojecttreeReq> projectTreeRequirementList = projecttreeReqDao.getProjectTreeRequirementByRequirementID(requirement.getId());
//            for (int i = 0; i < projectTreeRequirementList.size(); i++) {
//                Tprojecttree projecttree = new Tprojecttree();
//                projecttree = projecttreeDao.getProjecttreeByID(projectTreeRequirementList.get(i).getTprojecttree().getId());
//                selectedNodes[nodesCounter] = (TreeNode) projecttree;
//                selectedNodes[1].
//                nodesCounter++;
//            }
//        }
        return selectedNodes;
    }

    public void onProjectChange() {
        if (projectID != null && !projectID.equals(BigDecimal.valueOf(-1))) {
            selectedProjectTree = null;
            initProjectTree();
        } else {
            root = null;
        }
    }

    public void onNodeChange(Tprojecttree node) {
        requirement_dao reqDAO = new requirement_dao();
        if (node != null) {
            selectedProjectTree = node;
            requirementList = reqDAO.getRequirementByProjecttreeID(selectedProjectTree.getId());
        }
    }

    public void onUnselect() {
        selectedProjectTree = null;
        selectedNode = null;
        refreshList();
    }

    public void resetInputs() {
        requirement.setSymbol("");
        requirement.setDescription("");
        requirement.setTproject(null);
        if (selectedNodes != null) {
            onProjectChange();
            selectedNodes = null;
        }
    }

    public void refreshList() {
        requirement_dao dao = new requirement_dao();
        if (projectID == null || projectID.compareTo(BigDecimal.valueOf(-1)) == 0) {
            requirementList = dao.getAllRequirements();
        } else {
            requirementList = dao.getRequirementByProjectID(projectID);
        }
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

    public String fromParentToNode(BigDecimal node) {
        projectTree_dao dao = new projectTree_dao();
        String result = "";
        List<String> nodes = new ArrayList();
        Tprojecttree projecttreeByID = dao.getProjecttreeByID(node);
        while (projecttreeByID != null && projecttreeByID.getTprojecttree() != null) {
            nodes.add(projecttreeByID.getName());
            projecttreeByID = projecttreeByID.getTprojecttree();
        }
        //Collections.reverse(nodes);
        for (int i = nodes.size()-1; i > -1; i--) {
            result = result + nodes.get(i);
            if (!result.equals("") && i > 0) {
                result = result + " - ";
            }
        }
        return result;
    }

    //***********************Setters & Getters******************************//
    public List<Trequirement> getRequirementList() {
        return requirementList;
    }

    public void setRequirementList(List requirementList) {
        this.requirementList = requirementList;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public loginController getLoginControl() {
        return loginControl;
    }

    public void setLoginControl(loginController loginControl) {
        this.loginControl = loginControl;
    }

    public Tprojecttree getSelectedProjectTree() {
        return selectedProjectTree;
    }

    public void setSelectedProjectTree(Tprojecttree selectedProjectTree) {
        this.selectedProjectTree = selectedProjectTree;
    }

    public projectTreeController getProjectTreeContInReq() {
        return projectTreeContInReq;
    }

    public void setProjectTreeContInReq(projectTreeController projectTreeContInReq) {
        this.projectTreeContInReq = projectTreeContInReq;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public List<Tprojecttree> getProjectTreeList() {
        return projectTreeList;
    }

    public void setProjectTreeList(List<Tprojecttree> projectTreeList) {
        this.projectTreeList = projectTreeList;
    }

    public void setSelectedNodes(TreeNode[] selectedNodes) {
        this.selectedNodes = selectedNodes;
    }

    public BigDecimal getProjectID() {
        return projectID;
    }

    public void setProjectID(BigDecimal projectID) {
        this.projectID = projectID;
    }

    public Trequirement getRequirement() {
        return requirement;
    }

    public void setRequirement(Trequirement requirement) {
        this.requirement = requirement;
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

    public List<Trequirementfile> getRequirementFiles() {
        if (requirement.getId() != null) {
            requirementFile_dao dao = new requirementFile_dao();
            requirementFiles = dao.getRequirementFileByRequirementID(requirement.getId());
            return requirementFiles;
        } else {
            return new ArrayList();
        }
    }

    public List<BigDecimal> getSelectedProjectTreeList() {
        requirementProjectTree_dao dao = new requirementProjectTree_dao();
        List<TprojecttreeReq> projectTreeRequirementByRequirementID = dao.getProjectTreeRequirementByRequirementID(requirement.getId());
        selectedProjectTreeList.clear();
        if (projectTreeRequirementByRequirementID != null) {
            for (Iterator<TprojecttreeReq> iterator = projectTreeRequirementByRequirementID.iterator(); iterator.hasNext();) {
                TprojecttreeReq projReq = iterator.next();
                selectedProjectTreeList.add(projReq.getTprojecttree().getId());
            }
        }
        return selectedProjectTreeList;
    }
}
