package controller;

import entity.Tintegratedpart;
import entity.Tprojecttreetype;
import entity.Tproject;
import entity.Tprojecttree;
import entity.Ttestcase;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.faces.bean.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import model.integratedParts_dao;
import model.projecttreetype_dao;
import model.projectTree_dao;
import model.project_dao;
import model.requirementProjectTree_dao;
import model.testcaseProjectTree_dao;
import model.testcase_dao;
import model.versionProjectTree_dao;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

@ManagedBean(name = "projectTreeControl")
@SessionScoped
public class projectTreeController implements Serializable {

    @ManagedProperty(value = "#{loginControl}")
    private loginController loginControl;

    private TreeNode root;

    private TreeNode selectedNode;
    private Tprojecttree selectedProjectTree;

    private TreeNode[] selectedNodes;

    List<SelectItem> projectItems = new ArrayList<SelectItem>();
    List<SelectItem> projecttreetypeItems = new ArrayList<SelectItem>();

    private List<Tprojecttree> projectTreeList = new ArrayList();
    private List<Tprojecttree> selectedProjectTreeList = new ArrayList();
    private List<Tprojecttreetype> projecttreetypeList = new ArrayList();
    private List<Ttestcase> testcases = new ArrayList();
    private List<Tintegratedpart> integratedParts = new ArrayList();
    private Ttestcase testcase = new Ttestcase();
    private Tprojecttree projectTree = new Tprojecttree();
    private BigDecimal projecttreetypeID;

    private BigDecimal projectID;

    public void viewNewIntegratedPartsDialog() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        options.put("contentHeight", 500);
        RequestContext.getCurrentInstance().openDialog("createIntegratedParts", options, null);
    }

    public void viewEditDialog() {
        this.projectTree = (Tprojecttree) selectedNode.getData();
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        if (projectTree.getTprojecttreetype() != null) {
            projecttreetypeID = projectTree.getTprojecttreetype().getId();
        }
        RequestContext.getCurrentInstance().openDialog("editProjecttree", options, null);
    }

    public void viewNewNodeDialog() {
        resetInputs();
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        RequestContext.getCurrentInstance().openDialog("createNodeForProjecttree", options, null);
    }

    public void closeDialog(String name) {
        RequestContext.getCurrentInstance().closeDialog(name);
    }

    public String insertProjectTree() {
        projectTree_dao projectTree_dao = new projectTree_dao();
        project_dao project_dao = new project_dao();
        projecttreetype_dao projecttreetype_dao = new projecttreetype_dao();

        projectTree.setName(projectTree.getName().trim());
        projectTree.setDescription(projectTree.getDescription().trim());

        if (!projectTree.getName().equals("")) {
            if (projectTree.getName().length() <= 50) {
                if (projectTree.getDescription().length() <= 500) {
                    Tproject project = project_dao.getProjectByID(projectID);
                    if (projecttreetypeID != null) {
                        Tprojecttreetype projecttreetype = projecttreetype_dao.getProjecttreetypeByID(projecttreetypeID);
                        projectTree.setTprojecttreetype(projecttreetype);
                        if (selectedProjectTree == null) {
                            projectTree.setTprojecttree(projectTree_dao.getProjecttreeRootByProjectID(projectID));
                        } else {
                            projectTree.setTprojecttree(selectedProjectTree);
                        }
                        projectTree.setTproject(project);
                        projectTree.setTuser(loginControl.getEnteredUser());
                        projectTree_dao.createProjecttree(projectTree);
                        initProjectTree();
                        resetInputs();
                        closeDialog("createNodeForProjecttree");
                    } else {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "هشدار", "لطفا نوع را انتخاب کنید.");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "هشدار", "توضیحات گره درخت پروژه باید حداکثر 500 کاراکتر باشد.");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "هشدار", "نام گره درخت پروژه باید حداکثر 50 کاراکتر باشد.");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "هشدار", "نام گره درخت پروژه را وارد کنید.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return "";
    }

    public String insertIntegratedParts() {
        integratedParts_dao integratedPartDAO = new integratedParts_dao();
        Tintegratedpart integratedPart = new Tintegratedpart();

        if (selectedNode != null) {
            integratedPart.setTprojecttreeByProjecttreeid1((Tprojecttree) selectedNode.getData());
            if (selectedNodes.length > 0) {
                for (int i = 0; i < selectedNodes.length; i++) {
                    integratedPart.setTprojecttreeByProjecttreeid2((Tprojecttree) selectedNodes[i].getData());
                    integratedPartDAO.createIntegratedPart(integratedPart);
                }
                resetInputs();
                closeDialog("createIntegratedParts");
                return "";
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "هشدار", "گره های مرتبط را انتخاب کنید.");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return "";
            }
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "هشدار", "گره مرجع را انتخاب کنید.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "";
        }
    }

    public String update() {

        projectTree_dao projectTree_dao = new projectTree_dao();
        project_dao project_dao = new project_dao();
        projecttreetype_dao projecttreetype_dao = new projecttreetype_dao();

        projectTree.setName(projectTree.getName().trim());
        projectTree.setDescription(projectTree.getDescription().trim());

        if (!projectTree.getName().equals("")) {
            if (projectTree.getName().length() <= 50) {
                if (projectTree.getDescription().length() <= 500) {
                    if (projecttreetypeID != null) {
                        Tprojecttreetype projecttreetype = projecttreetype_dao.getProjecttreetypeByID(projecttreetypeID);
                        projectTree.setTprojecttreetype(projecttreetype);
                        projectTree_dao.updateProjecttree(projectTree);
                        initProjectTree();
                        resetInputs();
                        closeDialog("editProjecttree");
                        return "";
                    } else {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "هشدار", "لطفا نوع را انتخاب کنید.");
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "هشدار", "توضیحات گره درخت پروژه باید حداکثر 500 کاراکتر باشد.");
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "هشدار", "نام گره درخت پروژه باید حداکثر 50 کاراکتر باشد.");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "هشدار", "نام گره درخت پروژه را وارد کنید.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        return "";

    }

    public void removeProjectTreeNode() {
        Tprojecttree projectTreeNode = (Tprojecttree) selectedNode.getData();
        if (selectedNode != null) {
            if (selectedNode.getChildCount() < 1) {
                projectTree_dao dao = new projectTree_dao();
                testcaseProjectTree_dao testcaseProjecttreeDAO = new testcaseProjectTree_dao();
                List<BigDecimal> testcaseProjectTreeByProjectTreeID = testcaseProjecttreeDAO.getTestcaseProjectTreeByProjectTreeID(projectTreeNode.getId());
                if (testcaseProjectTreeByProjectTreeID == null) {
                    versionProjectTree_dao versionProjectTreeDAO = new versionProjectTree_dao();
                    List<BigDecimal> versionProjectTreeByProjectTreeID = versionProjectTreeDAO.getVersionProjectTreeByProjectTreeID(projectTreeNode.getId());
                    if (versionProjectTreeByProjectTreeID == null) {
                        requirementProjectTree_dao reqProjecttreeDAO = new requirementProjectTree_dao();
                        List<BigDecimal> projectTreeRequirementByProjectTreeID = reqProjecttreeDAO.getProjectTreeRequirementByProjectTreeID(projectTreeNode.getId());
                        if (projectTreeRequirementByProjectTreeID == null) {
                            projectTreeNode.setDto(new Date());
                            dao.updateProjecttree(projectTreeNode);
                            selectedNode.getChildren().clear();
                            selectedNode.getParent().getChildren().remove(selectedNode);
                            selectedNode.setParent(null);
                            selectedNode = null;
                        } else {
                            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "امکان حذف وجود ندارد", "لیست نیازمندی های منتسب شده به این گره: " + projectTreeRequirementByProjectTreeID);
                            FacesContext.getCurrentInstance().addMessage(null, message);
                        }
                    } else {
                        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "امکان حذف وجود ندارد", "لیست نسخه های منتسب به این گره: " + versionProjectTreeByProjectTreeID);
                        FacesContext.getCurrentInstance().addMessage(null, message);
                    }
                } else {
                    FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "امکان حذف وجود ندارد", "لیست مورد تست های منتسب به این گره: " + testcaseProjectTreeByProjectTreeID);
                    FacesContext.getCurrentInstance().addMessage(null, message);
                }

            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "هشدار", "این گره دارای فرزند می باشد. ابتدا فرزندان را حذف نمایید.");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "هشدار", "لطفا یک مورد را انتخاب کنید.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void removeIntegratedPart(Tintegratedpart integratedpart) {
        integratedParts_dao dao = new integratedParts_dao();
        testcaseProjectTree_dao testcaseProjecttreeDAO = new testcaseProjectTree_dao();
        List<BigDecimal> testcaseProjectTreeByIntegID = testcaseProjecttreeDAO.getTestcaseProjectTreeByIntegratedpatID(integratedpart.getId());
        if (testcaseProjectTreeByIntegID == null) {
            integratedpart.setDto(new Date());
            dao.updateIntegratedPart(integratedpart);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "امکان حذف وجود ندارد", "لیست مورد تست های منتسب به این رابطه: " + testcaseProjectTreeByIntegID);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
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
        TreeNode newNode = new DefaultTreeNode(ttParent, parent);
        List<Tprojecttree> children = getChildrenByParentID(ttParent.getId());
        if (children == null) {
            TreeNode n = new DefaultTreeNode("", null);
            return n;
        }
        for (int i = 0; i < children.size(); i++) {
            Tprojecttree tt = children.get(i);
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

    public void onNodeChange(Tprojecttree node) {
        testcase_dao dao = new testcase_dao();
        selectedProjectTree = node;
        testcases = dao.getTestcaseByProjecttreeID(selectedProjectTree.getId());
    }

    public void onUnselect() {
        selectedProjectTree = null;
        selectedNode = null;
        testcases = new ArrayList();
    }

    public void onProjectChange() {
        selectedProjectTree = null;
        initProjectTree();
    }

    public void resetInputs() {
        projectTree.setName(null);
        projectTree.setDescription(null);
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

    public TreeNode[] getSelectedNodes() {
        return selectedNodes;
    }

    public void setSelectedNodes(TreeNode[] selectedNodes) {
        this.selectedNodes = selectedNodes;
    }

    public List<Tprojecttree> getSelectedProjectTreeList() {
        return selectedProjectTreeList;
    }

    public void setSelectedProjectTreeList(List<Tprojecttree> selectedProjectTreeList) {
        this.selectedProjectTreeList = selectedProjectTreeList;
    }

    public Ttestcase getTestcase() {
        return testcase;
    }

    public void setTestcase(Ttestcase testcase) {
        this.testcase = testcase;
    }

    public List<Ttestcase> getTestcases() {
        return testcases;
    }

    public void setTestcases(List<Ttestcase> testcases) {
        this.testcases = testcases;
    }

    public Tprojecttree getSelectedProjectTree() {
        return selectedProjectTree;
    }

    public void setSelectedProjectTree(Tprojecttree selectedProjectTree) {
        this.selectedProjectTree = selectedProjectTree;
    }

    public List<SelectItem> getProjecttreetypeItems() {
        return projecttreetypeItems;
    }

    public void setProjecttreetypeItems(List<SelectItem> projecttreetypeItems) {
        this.projecttreetypeItems = projecttreetypeItems;
    }

    public List<Tprojecttreetype> getProjecttreetypeList() {
        return projecttreetypeList;
    }

    public void setProjecttreetypeList(List<Tprojecttreetype> projecttreetypeList) {
        this.projecttreetypeList = projecttreetypeList;
    }

    public BigDecimal getProjecttreetypeID() {
        return projecttreetypeID;
    }

    public void setProjecttreetypeID(BigDecimal projecttreetypeID) {
        this.projecttreetypeID = projecttreetypeID;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
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

    public Tprojecttree getProjectTree() {
        return projectTree;
    }

    public void setProjectTree(Tprojecttree projectTree) {
        this.projectTree = projectTree;
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

    public List<Tintegratedpart> getIntegratedParts() {
        integratedParts_dao dao = new integratedParts_dao();
        if (projectID != null) {
            integratedParts = dao.getIntegratedPartsByProjectID(projectID);
        }
        return integratedParts;
    }

    public void setIntegratedParts(List<Tintegratedpart> integratedParts) {
        this.integratedParts = integratedParts;
    }

}
