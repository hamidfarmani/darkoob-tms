package controller;

import entity.Tbug;
import entity.TbugVersion;
import entity.Titeration;
import entity.Tproject;
import entity.Tprojecttree;
import entity.Tversion;
import entity.TversionProjecttree;
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
import javax.faces.model.SelectItem;
import model.bugVersion_dao;
import model.bug_dao;
import model.iteration_dao;
import model.projectTree_dao;
import model.project_dao;
import model.versionProjectTree_dao;
import model.version_dao;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

@ManagedBean(name = "versionControl")
@SessionScoped
public class versionController implements Serializable {

    @ManagedProperty(value = "#{loginControl}")
    private loginController loginControl;

    private TreeNode versionOnProjectTree;
    private List<Tbug> bugList = new ArrayList();

    private TreeNode[] extensions;
    private TreeNode[] changes;
    private List<Tbug> bugs;

    private List<BigDecimal> selectedExtentionList = new ArrayList();
    private List<BigDecimal> selectedChangeList = new ArrayList();
    private List<Tversion> versionList = new ArrayList();
    private Tversion version = new Tversion();

    private BigDecimal projectID;

    private TreeNode root;
    private TreeNode changesRoot;
    private TreeNode extensionRoot;
    private Tprojecttree selectedProjectTree;
    private List<Tprojecttree> projectTreeList = new ArrayList();

    public void viewNewVersionDialog() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        options.put("contentHeight", 550);
        options.put("contentWidth", 750);
        resetInputs();
        initProjectTree();
        RequestContext.getCurrentInstance().openDialog("createVersion", options, null);
    }

    public void viewEditVersionDialog(Tversion version) {
        this.version = version;
        projectID = version.getTprojecttree().getTproject().getId();
        selectedProjectTree = version.getTprojecttree();
        getSelectedExtentionList();
        getSelectedChangeList();
        onProjectChange();
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        options.put("contentHeight", 550);
        options.put("contentWidth", 750);
        RequestContext.getCurrentInstance().openDialog("editVersion", options, null);
    }

    public void closeDialog(String name) {
        RequestContext.getCurrentInstance().closeDialog(name);
    }

    public String insertVersion() {
        version_dao dao = new version_dao();

        version.setName(version.getName().trim());
        version.setDesccription(version.getDesccription().trim());

        if (projectID != null && projectID.compareTo(BigDecimal.valueOf(-1)) != 0) {
            if (versionOnProjectTree != null) {
                if (!version.getName().equals("")) {
                    if (version.getName().length() <= 50) {
                        if (version.getDesccription().length() <= 500) {
                            Tprojecttree verOnTree = (Tprojecttree) versionOnProjectTree.getData();
                            version.setTuser(loginControl.getEnteredUser());
                            version.setCreationdate(new Date());
                            version.setTprojecttree(verOnTree);
                            dao.createVersion(version);

                            versionProjectTree_dao versionProjectTreeDao = new versionProjectTree_dao();
                            bugVersion_dao bugVersionDao = new bugVersion_dao();
                            if (extensions.length > 0) {
                                for (int i = 0; i < extensions.length; i++) {
                                    Tprojecttree ext = (Tprojecttree) extensions[i].getData();
                                    TversionProjecttree versionProjectTree = new TversionProjecttree(ext, version);
                                    versionProjectTree.setType("E");
                                    versionProjectTreeDao.createVersionProjectTree(versionProjectTree);
                                }
                            }
                            if (changes.length > 0) {
                                for (int i = 0; i < changes.length; i++) {
                                    Tprojecttree chng = (Tprojecttree) changes[i].getData();
                                    TversionProjecttree versionProjectTree = new TversionProjecttree(chng, version);
                                    versionProjectTree.setType("C");
                                    versionProjectTreeDao.createVersionProjectTree(versionProjectTree);
                                }
                            }
                            if (bugs.size() > 0) {
                                for (int i = 0; i < bugs.size(); i++) {
                                    TbugVersion bugVersion = new TbugVersion();
                                    bugVersion.setTbug(bugs.get(i));
                                    bugVersion.setTversion(version);
                                    bugVersionDao.createBugVersion(bugVersion);
                                }
                            }

                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "عملیات موفق", "عملیات با موفقیت انجام شد."));
                            refreshList();
                            closeDialog("createVersion");
                            resetInputs();
                            return "";

                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "توضیحات نسخه باید حداکثر 500 کاراکتر باشد."));
                            return "";
                        }
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام نسخه باید حداکثر 50 کاراکتر باشد."));
                        return "";
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام نسخه را وارد کنید."));
                    return "";
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "گره مورد نظر را انتخاب کنید."));
                return "";
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "پروژه را انتخاب کنید."));
            return "";
        }
    }

    public String update() {
        version_dao dao = new version_dao();
        versionProjectTree_dao versionProjectTreeDAO = new versionProjectTree_dao();

        version.setName(version.getName().trim());
        version.setDesccription(version.getDesccription().trim());
        if (versionOnProjectTree != null) {
            if (!version.getName().equals("")) {
                if (version.getName().length() <= 50) {
                    if (version.getDesccription().length() <= 500) {
                        Tprojecttree verOnTree = (Tprojecttree) versionOnProjectTree.getData();
                        version.setTprojecttree(verOnTree);
                        dao.updateVersion(version);
                        versionProjectTreeDAO.deleteByVersionID(version.getId());
                        bugVersion_dao bugVersionDao = new bugVersion_dao();
                        if (extensions.length > 0) {
                            for (int i = 0; i < extensions.length; i++) {
                                Tprojecttree ext = (Tprojecttree) extensions[i].getData();
                                TversionProjecttree versionProjectTree = new TversionProjecttree(ext, version);
                                versionProjectTree.setType("E");
                                versionProjectTreeDAO.createVersionProjectTree(versionProjectTree);
                            }
                        }
                        if (changes.length > 0) {
                            for (int i = 0; i < changes.length; i++) {
                                Tprojecttree chng = (Tprojecttree) changes[i].getData();
                                TversionProjecttree versionProjectTree = new TversionProjecttree(chng, version);
                                versionProjectTree.setType("C");
                                versionProjectTreeDAO.createVersionProjectTree(versionProjectTree);
                            }
                        }
                        if (bugs.size() > 0) {
                            for (int i = 0; i < bugs.size(); i++) {
                                TbugVersion bugVersion = new TbugVersion();
                                bugVersion.setTbug(bugs.get(i));
                                bugVersion.setTversion(version);
                                bugVersionDao.createBugVersion(bugVersion);
                            }
                        }

                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "عملیات موفق", "عملیات با موفقیت انجام شد."));
                        refreshList();
                        closeDialog("editVersion");
                        resetInputs();
                        return "";
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "توضیحات نسخه باید حداکثر 500 کاراکتر باشد."));
                        return "";
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام نسخه باید حداکثر 50 کاراکتر باشد."));
                    return "";
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام نسخه را وارد کنید."));
                return "";
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "گره مورد نظر را انتخاب کنید."));
            return "";
        }
    }

    public void removeVersion(Tversion version) {
        version_dao versionDAO = new version_dao();
        iteration_dao iterationDAO = new iteration_dao();
        List<BigDecimal> iterationsByVersionID = iterationDAO.getIterationsByVersionID(version.getId());
        if (iterationsByVersionID == null) {
            version.setDto(new Date());
            versionDAO.updateVersion(version);
            refreshList();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "امکان حذف وجود ندارد", "لیست تکرار تست های تعریف شده بر روی این نسخه: " + iterationsByVersionID));
        }

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

    public void onProjectChange() {
        bug_dao dao = new bug_dao();
        initProjectTree();
        bugList = dao.getBugsByProjectID(projectID);
    }

    public void initProjectTree() {
        projectTree_dao projectTree_dao = new projectTree_dao();
        if (projectID != null && !projectID.equals(BigDecimal.valueOf(-1))) {
            projectTreeList = projectTree_dao.getProjecttreeByProjectID(projectID);
            if (projectTreeList != null) {
                Tprojecttree projectRoot = projectTreeList.get(0);
                root = newNodeWithChildren(projectRoot, null);
                extensionRoot = newNodeWithChildrenExtention(projectRoot, null);
                changesRoot = newNodeWithChildrenChange(projectRoot, null);
            } else {
                root = null;
                extensionRoot = null;
                changesRoot = null;
            }
        }
    }

    public TreeNode newNodeWithChildren(Tprojecttree ttParent, TreeNode parent) {
        TreeNode newNode = new DefaultTreeNode(ttParent, parent);
        Tprojecttree tree = (Tprojecttree) newNode.getData();
        if (selectedProjectTree != null && selectedProjectTree.getId().compareTo(tree.getId()) == 0) {
            newNode.setSelected(true);
            TreeNode t = newNode;
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
        for (int i = 0; i < children.size(); i++) {
            Tprojecttree tt = children.get(i);
            TreeNode newNode2 = newNodeWithChildren(tt, newNode);
        }
        return newNode;
    }

    public TreeNode newNodeWithChildrenChange(Tprojecttree ttParent, TreeNode parent) {
        TreeNode newNode = new DefaultTreeNode(ttParent, parent);
        Tprojecttree tree = (Tprojecttree) newNode.getData();
        if (selectedChangeList != null && selectedChangeList.contains(tree.getId())) {
            newNode.setSelected(true);
            TreeNode t = newNode;
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
        for (int i = 0; i < children.size(); i++) {
            Tprojecttree tt = children.get(i);
            TreeNode newNode2 = newNodeWithChildrenChange(tt, newNode);
        }
        return newNode;
    }

    public TreeNode newNodeWithChildrenExtention(Tprojecttree ttParent, TreeNode parent) {
        TreeNode newNode = new DefaultTreeNode(ttParent, parent);
        Tprojecttree tree = (Tprojecttree) newNode.getData();

        if (selectedExtentionList != null && selectedExtentionList.contains(tree.getId())) {
            newNode.setSelected(true);
            TreeNode t = newNode;
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
        for (int i = 0; i < children.size(); i++) {
            Tprojecttree tt = children.get(i);
            TreeNode newNode2 = newNodeWithChildrenExtention(tt, newNode);
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

    public void resetInputs() {
        version = new Tversion();
        versionOnProjectTree = null;
        extensions = null;
        changes = null;
    }

    public void refreshList() {
        version_dao dao = new version_dao();
        if (projectID == null || projectID.compareTo(BigDecimal.valueOf(-1)) == 0) {
            versionList = dao.getAllVersions();
        } else {
            versionList = dao.getVersionByProjectID(projectID);
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

    //***********************Setters & Getters******************************//
    public TreeNode getChangesRoot() {
        return changesRoot;
    }

    public void setChangesRoot(TreeNode changesRoot) {
        this.changesRoot = changesRoot;
    }

    public TreeNode getExtensionRoot() {
        return extensionRoot;
    }

    public void setExtensionRoot(TreeNode extensionRoot) {
        this.extensionRoot = extensionRoot;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public Tprojecttree getSelectedProjectTree() {
        return selectedProjectTree;
    }

    public void setSelectedProjectTree(Tprojecttree selectedProjectTree) {
        this.selectedProjectTree = selectedProjectTree;
    }

    public List<Tprojecttree> getProjectTreeList() {
        return projectTreeList;
    }

    public void setProjectTreeList(List<Tprojecttree> projectTreeList) {
        this.projectTreeList = projectTreeList;
    }

    public loginController getLoginControl() {
        return loginControl;
    }

    public void setLoginControl(loginController loginControl) {
        this.loginControl = loginControl;
    }

    public BigDecimal getProjectID() {
        return projectID;
    }

    public void setProjectID(BigDecimal projectID) {
        this.projectID = projectID;
    }

    public List<Tbug> getBugs() {
        return bugs;
    }

    public void setBugs(List<Tbug> bugs) {
        this.bugs = bugs;
    }

    public TreeNode getVersionOnProjectTree() {
        return versionOnProjectTree;
    }

    public void setVersionOnProjectTree(TreeNode versionOnProjectTree) {
        this.versionOnProjectTree = versionOnProjectTree;
    }

    public TreeNode[] getExtensions() {
        return extensions;
    }

    public void setExtensions(TreeNode[] extensions) {
        this.extensions = extensions;
    }

    public TreeNode[] getChanges() {
        return changes;
    }

    public void setChanges(TreeNode[] changes) {
        this.changes = changes;
    }

    public Tversion getVersion() {
        return version;
    }

    public void setVersion(Tversion version) {
        this.version = version;
    }

    public List<Tversion> getVersionList() {
        return versionList;

    }

    public List<Tbug> getBugList() {
        return bugList;
    }

    public void setBugList(List<Tbug> bugList) {
        this.bugList = bugList;
    }

    public List<BigDecimal> getSelectedExtentionList() {
        versionProjectTree_dao dao = new versionProjectTree_dao();
        selectedExtentionList = dao.getProjectTreeByVersionID(version.getId(), "E");
        return selectedExtentionList;
    }

    public void setSelectedExtentionList(List<BigDecimal> selectedExtentionList) {
        this.selectedExtentionList = selectedExtentionList;
    }

    public List<BigDecimal> getSelectedChangeList() {
        versionProjectTree_dao dao = new versionProjectTree_dao();
        selectedChangeList = dao.getProjectTreeByVersionID(version.getId(), "C");
        return selectedChangeList;
    }

    public void setSelectedChangeList(List<BigDecimal> selectedChangeList) {
        this.selectedChangeList = selectedChangeList;
    }
}
