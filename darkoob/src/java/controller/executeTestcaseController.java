package controller;

import entity.Titeration;
import entity.Tproject;
import entity.Tprojecttree;
import entity.Ttestcase;
import entity.TtestcaseIter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import model.iteration_dao;
import model.projectTree_dao;
import model.project_dao;
import model.testcaseIteration_dao;
import model.testcase_dao;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

@ManagedBean(name = "executeTestcaseControl")
@SessionScoped
public class executeTestcaseController implements Serializable {

    List<SelectItem> projectItems = new ArrayList<SelectItem>();
    List<SelectItem> iterationItems = new ArrayList<SelectItem>();

    private List<Ttestcase> executeTestcaseList = new ArrayList();
    private Ttestcase executeTestcase = new Ttestcase();
    private Ttestcase selectedTestcase = new Ttestcase();

    private BigDecimal projectID;
    private BigDecimal iterationID;

    private TreeNode root;
    private TreeNode selectedNode;
    private Tprojecttree selectedProjectTree;
    private List<Tprojecttree> projectTreeList = new ArrayList();

    public String execute(Ttestcase executeTestcase) {
        testcaseIteration_dao testcaseIterationDao = new testcaseIteration_dao();
        TtestcaseIter testcaseIteration = testcaseIterationDao.getTestcaseIterationByTestcaseIDAndIterationID(executeTestcase.getId(), iterationID);
        testcaseIteration.setTestdate(new Date());
        testcaseIterationDao.updateTestcaseIteration(testcaseIteration);
        refreshList();
        return "";
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
        testcase_dao testcaseDAO = new testcase_dao();
        if (node != null) {
            selectedProjectTree = node;
            refreshList();
        }
    }

    public void onUnselect() {
        selectedProjectTree = null;
        selectedNode = null;
        refreshList();
    }

    public void onProjectChange() {
        if (projectID != null && !projectID.equals(BigDecimal.valueOf(-1))) {
            iterationItems = getIterationComboItem();
            initProjectTree();
        } else {
            root = null;
        }
    }

    public void onIterationChange() {
        testcase_dao dao = new testcase_dao();
        executeTestcaseList = dao.getTestcaseByIterationID(iterationID);
    }

    public void refreshList() {
        testcase_dao dao = new testcase_dao();
        if (iterationID != null) {
            if (selectedProjectTree == null) {
                executeTestcaseList = dao.getTestcaseByIterationID(iterationID);
            } else {
                executeTestcaseList = dao.getTestcaseByIterationAndProjecttreeID(iterationID, selectedProjectTree.getId());
            }
        } else {
            executeTestcaseList = new ArrayList();
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


    /*     Setters & Getters      */
    public Ttestcase getSelectedTestcase() {
        return selectedTestcase;
    }

    public void setSelectedTestcase(Ttestcase selectedTestcase) {
        this.selectedTestcase = selectedTestcase;
    }

    public List<SelectItem> getIterationItems() {
        return iterationItems;
    }

    public void setIterationItems(List<SelectItem> iterationItems) {
        this.iterationItems = iterationItems;
    }

    public BigDecimal getIterationID() {
        return iterationID;
    }

    public void setIterationID(BigDecimal iterationID) {
        this.iterationID = iterationID;
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

    public Ttestcase getExecuteTestcase() {
        return executeTestcase;
    }

    public void setExecuteTestcase(Ttestcase executeTestcase) {
        this.executeTestcase = executeTestcase;
    }

    public List<Ttestcase> getExecuteTestcaseList() {
        return executeTestcaseList;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
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

    public List<SelectItem> getIterationComboItem() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        iteration_dao dao = new iteration_dao();
        List<Titeration> iterationList = dao.getIterationsByProjectID(projectID);
        if (iterationList != null) {
            items.add(new SelectItem(-1, ""));
            for (Titeration iteration : iterationList) {
                items.add(new SelectItem(iteration.getId(), iteration.getName()));
            }
        }
        return items;
    }
}
