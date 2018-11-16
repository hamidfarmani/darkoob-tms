package controller;

import entity.Tbug;
import entity.Tbugcomment;
import entity.Tbugfile;
import entity.Tbughistoy;
import entity.Tbugstate;
import entity.Tbugtype;
import entity.Titeration;
import entity.Tproject;
import entity.Tprojecttree;
import entity.Ttestcase;
import entity.TtestcaseIter;
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
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import model.bugComment_dao;
import model.bugFile_dao;
import model.bugHistory_dao;
import model.bug_dao;
import model.bugstate_dao;
import model.bugtype_dao;
import model.iteration_dao;
import model.projectTree_dao;
import model.project_dao;
import model.testcaseIteration_dao;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

@ManagedBean(name = "bugControl")
@SessionScoped
public class bugController implements Serializable {

    @ManagedProperty(value = "#{loginControl}")
    private loginController loginControl;

    List<SelectItem> projectItems = new ArrayList<SelectItem>();
    List<SelectItem> iterationItems = new ArrayList<SelectItem>();
    List<SelectItem> testcaseItems = new ArrayList<SelectItem>();
    List<Ttestcase> testcaseList = new ArrayList<Ttestcase>();
    List<SelectItem> bugSteteItems = new ArrayList<SelectItem>();

    private List<Tbugcomment> bugComments = new ArrayList();
    private List<Tbug> bugList = new ArrayList();
    private List<Tbugfile> bugFiles = new ArrayList();
    private List<Tprojecttree> projectTreeList = new ArrayList();

    private Tbug bug = new Tbug();
    private Tbugcomment comment = new Tbugcomment();
    private Ttestcase selectedTestcase = new Ttestcase();

    private String destination = "/app/darkoob/uploadedFiles/BugFiles/";
    private String assignType;
    private String bugComment;
    private String bugType;

    private BigDecimal projectID;
    private BigDecimal iterationID;
    private BigDecimal typeID;
    private BigDecimal bugState;
    private BigDecimal selectedTestcaseID;

    private TreeNode root;
    private TreeNode selectedNode;
    private Tprojecttree selectedProjectTree;

    public void viewAttachmentDialog(Tbug bug) {
        this.bug = bug;
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        options.put("contentHeight", 440);
        RequestContext.getCurrentInstance().openDialog("attachFileToBug", options, null);
    }

    public void viewBugDialog(Tbug bug) {
        this.bug = bug;
        bugComment_dao dao = new bugComment_dao();
        bugComments = dao.getBugCommentByBugID(bug.getId());
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        options.put("scrollabale", false);
        options.put("contentHeight", 520);
        options.put("contentWidth", 700);
        RequestContext.getCurrentInstance().openDialog("viewBug", options, null);
    }

    public void viewNewBugDialog() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        options.put("scrollabale", false);
        options.put("contentHeight", 520);
        resetInputs();
        RequestContext.getCurrentInstance().openDialog("createBug", options, null);
    }

    public void viewNewBugDialog(BigDecimal projectID, BigDecimal iterID, Ttestcase testcase) {
        resetInputs();
        this.projectID = projectID;
        onProjectChange();
        this.iterationID = iterID;
        onIterationChange();
        selectedTestcaseID = testcase.getId();
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        options.put("scrollabale", false);
        options.put("contentHeight", 520);
        bug = new Tbug();
        //    bug.setType("N");
        RequestContext.getCurrentInstance().openDialog("createBug", options, null);
    }

    public void viewEditBugDialog(Tbug bug) {
        this.bug = bug;
        projectID = bug.getTiterationByIterationid().getTversion().getTprojecttree().getTproject().getId();
        onProjectChange();
        iterationID = bug.getTiterationByIterationid().getId();
        onIterationChange();
        if (bug.getTtestcaseIter() != null) {
            selectedTestcaseID = bug.getTtestcaseIter().getTtestcase().getId();
            assignType = "C";
        } else {
            selectedProjectTree = bug.getTprojecttree();
            initProjectTree();
            assignType = "T";
        }
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        options.put("scrollabale", false);
        options.put("contentHeight", 520);
        RequestContext.getCurrentInstance().openDialog("editBug", options, null);
    }

    public void viewChangeBugStateDialog(Tbug bug) {
        this.bug = bug;
        bugState = bug.getTbugstate().getId();
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        options.put("scrollabale", false);
        options.put("contentHeight", 300);
        RequestContext.getCurrentInstance().openDialog("changeBugState", options, null);
    }

    public void viewEditCommentDialog(Tbugcomment comment) {
        this.comment = comment;
        bugComment = comment.getBugcomment();
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        options.put("scrollabale", false);
        options.put("contentHeight", 200);
        RequestContext.getCurrentInstance().openDialog("editComment", options, null);
    }

    public void closeDialog(String name) {
        RequestContext.getCurrentInstance().closeDialog(name);
    }

    public void onNodeChange(Tprojecttree node) {
        bug_dao dao = new bug_dao();
        if (node != null) {
            selectedProjectTree = node;
            bugList = dao.getBugsByProjecttreeID(selectedProjectTree.getId());
        }
    }

    public void onUnselect() {
        selectedProjectTree = null;
        selectedNode = null;
        refreshList();
    }

    public void onProjectChange() {
        if (projectID != null && !projectID.equals(BigDecimal.valueOf(-1))) {
            initProjectTree();
            iterationItems = getIterationComboItem();
        } else {
            root = null;
        }
    }

    public void onIterationChange() {
        testcaseIteration_dao dao = new testcaseIteration_dao();
        if (iterationID != null) {
            testcaseList = dao.getTestcaseListByIterationID(iterationID);
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
        Tprojecttree projTree = (Tprojecttree) newNode.getData();
        if (selectedProjectTree != null && selectedProjectTree.getId().compareTo(projTree.getId()) == 0) {
            TreeNode t = newNode;
            newNode.setSelected(true);
            while (t.getParent() != null) {
                newNode.getParent().setExpanded(true);
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

    public String insertBug() {
        bug_dao bugDao = new bug_dao();
        testcaseIteration_dao testcaseIterationDao = new testcaseIteration_dao();
        iteration_dao iterationDao = new iteration_dao();

        bug.setTitle(bug.getTitle().trim());
        bug.setResult(bug.getResult().trim());
        bug.setDescription(bug.getDescription().trim());

        if (typeID != null) {
            bugtype_dao bugtypeDAO = new bugtype_dao();
            Tbugtype bugtypeByID = bugtypeDAO.getBugtypeByID(typeID);
            bug.setTbugtype(bugtypeByID);
        }

        if (selectedNode != null) {
            selectedProjectTree = (Tprojecttree) selectedNode.getData();
        }

        if (!bug.getTitle().equals("")) {
            if (bug.getTitle().length() <= 50) {
                if (!bug.getResult().equals("")) {
                    if (bug.getResult().length() <= 1000) {
                        if (projectID != null && projectID.compareTo(BigDecimal.valueOf(-1)) != 0) {
                            if (iterationID != null && iterationID.compareTo(BigDecimal.valueOf(-1)) != 0) {
                                bugstate_dao bugstateDAO = new bugstate_dao();
                                Titeration iteration = iterationDao.getIterationByID(iterationID);
                                bug.setTiterationByIterationid(iteration);
                                Tbugstate initBugState = bugstateDAO.getBugstateByName("در حال بررسی");
                                bug.setTuser(loginControl.getEnteredUser());
                                bug.setTbugstate(initBugState);
                                bug.setCreationdate(new Date());
                                if (selectedTestcaseID != null && !selectedTestcaseID.equals(BigDecimal.valueOf(-1))) {
                                    TtestcaseIter testcaseIteration = testcaseIterationDao.getTestcaseIterationByTestcaseIDAndIterationID(selectedTestcaseID, iterationID);
                                    bug.setTtestcaseIter(testcaseIteration);
                                    bugDao.createBug(bug);
                                    refreshList();
                                    closeDialog("createBug");
                                }
                                if (selectedProjectTree != null) {
                                    bug.setTprojecttree(selectedProjectTree);
                                    bugDao.createBug(bug);
                                    refreshList();
                                    closeDialog("createBug");
                                }

                                if ((selectedTestcaseID == null || selectedTestcaseID.compareTo(BigDecimal.valueOf(-1)) == 0) && selectedProjectTree == null) {
                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "لطفا خطا را به یک مورد تست یا یک گره درخت انتساب دهید."));
                                }
                            } else {
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "تکرار تست را انتخاب کنید."));
                            }
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "پروژه را انتخاب کنید."));
                        }
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نتیجه مشاهده شده باید حداکثر 1000 کاراکتر باشد."));
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نتیجه مشاهده شده را وارد کنید."));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "عنوان باید حداکثر 50 کاراکتر باشد."));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "عنوان خطا را وارد کنید."));
        }

        return "";
    }

    public String insertComment() {
        bugComment_dao commentDao = new bugComment_dao();
        comment.setBugcomment(bugComment);
        comment.setTuser(loginControl.getEnteredUser());
        comment.setCreationdate(new Date());
        comment.setTbug(bug);
        commentDao.createBugComment(comment);
        bugComment_dao dao = new bugComment_dao();
        bugComments = dao.getBugCommentByBugID(bug.getId());
        bugComment = "";
        return "";
    }

    public String updateBug() {
        bug_dao bugDao = new bug_dao();
        testcaseIteration_dao testcaseIterationDao = new testcaseIteration_dao();
        iteration_dao iterationDao = new iteration_dao();
        bugHistory_dao bugHistoryDao = new bugHistory_dao();
        Tbughistoy bugHistory = new Tbughistoy();

        bug.setTitle(bug.getTitle().trim());
        bug.setResult(bug.getResult().trim());
        bug.setDescription(bug.getDescription().trim());

        if (typeID != null) {
            bugtype_dao bugtypeDAO = new bugtype_dao();
            Tbugtype bugtypeByID = bugtypeDAO.getBugtypeByID(typeID);
            bug.setTbugtype(bugtypeByID);
        }

        if (selectedNode != null) {
            selectedProjectTree = (Tprojecttree) selectedNode.getData();
        }

        if (!bug.getTitle().equals("")) {
            if (bug.getTitle().length() <= 50) {
                if (bug.getDescription().length() <= 4000) {
                    if (!bug.getResult().equals("")) {
                        if (bug.getResult().length() <= 1000) {
                            if (projectID != null && projectID.compareTo(BigDecimal.valueOf(-1)) != 0) {
                                if (iterationID != null && iterationID.compareTo(BigDecimal.valueOf(-1)) != 0) {
                                    Titeration iteration = iterationDao.getIterationByID(iterationID);
                                    bug.setTiterationByIterationid(iteration);
                                    bugHistory.setTbug(bug);
                                    bugHistory.setCreationdate(new Date());
                                    bugHistory.setState(bug.getTbugstate().getName());
                                    bugHistory.setTuser(loginControl.getEnteredUser());
                                    //    bugHistory.setType(bug.getType());
                                    bugHistoryDao.createBughistoyHistory(bugHistory);

                                    if (selectedTestcaseID != null && !selectedTestcaseID.equals(BigDecimal.valueOf(-1))) {
                                        TtestcaseIter testcaseIteration = testcaseIterationDao.getTestcaseIterationByTestcaseIDAndIterationID(selectedTestcaseID, iterationID);
                                        bug.setTtestcaseIter(testcaseIteration);
                                        bugDao.updateBug(bug);
                                        refreshList();
                                        closeDialog("editBug");
                                        resetInputs();
                                    }
                                    if (selectedProjectTree != null) {
                                        bug.setTprojecttree(selectedProjectTree);
                                        bugDao.updateBug(bug);
                                        refreshList();
                                        closeDialog("editBug");
                                        resetInputs();
                                    }

                                    if ((selectedTestcaseID == null || selectedTestcaseID.compareTo(BigDecimal.valueOf(-1)) == 0) && selectedProjectTree == null) {
                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "لطفا خطا را به یک مورد تست یا یک گره درخت انتساب دهید."));
                                    }

                                } else {
                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "تکرار تست را انتخاب کنید."));
                                }
                            } else {
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "پروژه را انتخاب کنید."));
                            }
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نتیجه مشاهده شده باید حداکثر 1000 کاراکتر باشد."));
                        }
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نتیجه مشاهده شده را وارد کنید."));
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "توضیحات باید حداکثر 4000 کاراکتر باشد."));
                    return "";
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "عنوان باید حداکثر 50 کاراکتر باشد."));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "عنوان خطا را وارد کنید."));
        }

        return "";
    }

    public String updateComment() {
        bugComment_dao dao = new bugComment_dao();
        comment.setBugcomment(bugComment);
        dao.updateBugComment(comment);

        bugComments = dao.getBugCommentByBugID(bug.getId());
        bugComment = "";
        closeDialog("editComment");
        return "";
    }

    public void removeComment(Tbugcomment comment) {
        bugComment_dao commentDao = new bugComment_dao();
        this.comment = comment;
        comment.setDto(new Date());
        commentDao.updateBugComment(comment);
        bugComments = commentDao.getBugCommentByBugID(bug.getId());
    }

    public void removeBug(Tbug bug) {
        bug_dao dao = new bug_dao();
        String state = bug.getTbugstate().getName();
        if (state.equals("در حال بررسی")) {
            if (loginControl.getEnteredUser().getId().compareTo(bug.getTuser().getId()) == 0) {
                bug.setDto(new Date());
                dao.updateBug(bug);
                refreshList();
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "امکان حذف وجود ندارد", "امکان حذف خطا فقط برای کاربری که آن را ساخته است وجود دارد."));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "امکان حذف وجود ندارد", "در صورتی که وضعیت خطا در حال بررسی باشد می توان خطا را حذف کرد."));
        }

    }

    public void upload(FileUploadEvent event) {
        try {
            bugFile_dao dao = new bugFile_dao();
            Tbugfile bugFile = new Tbugfile();
            String fileName = event.getFile().getFileName();
            String fileType = fileName.substring(fileName.indexOf("."));
            bugFile.setFilename(fileName);
            bugFile.setTbug(bug);
            bugFile.setTuser(loginControl.getEnteredUser());
            bugFile.setFilepath(destination);
            bugFile.setCreationdate(new Date());
            BigDecimal testcaseFileID = dao.createBugFile(bugFile);
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

    public void changeBugState() {
        bug_dao bugDao = new bug_dao();
        bugstate_dao bugStateDAO = new bugstate_dao();
        bugHistory_dao bugHistoryDao = new bugHistory_dao();
        Tbughistoy bugHistory = new Tbughistoy();

        bugHistory.setTbug(bug);
        bugHistory.setCreationdate(new Date());
        bugHistory.setState(bug.getTbugstate().getName());
        bugHistory.setTuser(loginControl.getEnteredUser());
//        bugHistory.setType(bug.getType());
        bugHistoryDao.createBughistoyHistory(bugHistory);

        Tbugstate bugstateByName = bugStateDAO.getBugstateByID(bugState);
        bug.setTbugstate(bugstateByName);
        bugDao.updateBug(bug);
        refreshList();
        closeDialog("changeBugState");
    }

    public void resetInputs() {
        bug = new Tbug();
        //bug.setType("N");

        if (selectedTestcase != null) {
            selectedTestcase = null;
        }
        iterationID = BigDecimal.valueOf(-1);
        selectedNode = null;

        if (selectedProjectTree != null) {
            selectedProjectTree = null;
        }
    }

    public void remove(Tbugfile selectedFile) {
        bugFile_dao dao = new bugFile_dao();
        selectedFile.setDto(new Date());
        dao.updateBugFile(selectedFile);
    }

    public void refreshList() {
        bug_dao dao = new bug_dao();
        if (projectID == null || projectID.compareTo(BigDecimal.valueOf(-1)) == 0) {
            bugList = dao.getAllBugs();
        } else {
            bugList = dao.getBugsByProjectID(projectID);
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
    public String getBugComment() {
        return bugComment;
    }

    public void setBugComment(String bugComment) {
        this.bugComment = bugComment;
    }

    public loginController getLoginControl() {
        return loginControl;
    }

    public void setLoginControl(loginController loginControl) {
        this.loginControl = loginControl;
    }

    public String getAssignType() {
        return assignType;
    }

    public void setAssignType(String assignType) {
        this.assignType = assignType;
    }

    public BigDecimal getSelectedTestcaseID() {
        return selectedTestcaseID;
    }

    public void setSelectedTestcaseID(BigDecimal selectedTestcaseID) {
        this.selectedTestcaseID = selectedTestcaseID;
    }

    public List<SelectItem> getTestcaseItems() {
        return testcaseItems;
    }

    public void setTestcaseItems(List<SelectItem> testcaseItems) {
        this.testcaseItems = testcaseItems;
    }

    public List<Tprojecttree> getProjectTreeList() {
        return projectTreeList;
    }

    public Ttestcase getSelectedTestcase() {
        return selectedTestcase;
    }

    public void setSelectedTestcase(Ttestcase selectedTestcase) {
        this.selectedTestcase = selectedTestcase;
    }

    public void setProjectTreeList(List<Tprojecttree> projectTreeList) {
        this.projectTreeList = projectTreeList;
    }

    public BigDecimal getTypeID() {
        return typeID;
    }

    public void setTypeID(BigDecimal typeID) {
        this.typeID = typeID;
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

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public List<Tbugfile> getBugFiles() {
        if (bug.getId() != null) {
            bugFile_dao dao = new bugFile_dao();
            bugFiles = dao.getBugFileByBugID(bug.getId());
            return bugFiles;
        } else {
            return new ArrayList();
        }
    }

    public void setBugFiles(List<Tbugfile> bugFiles) {
        this.bugFiles = bugFiles;
    }

    public String getBugType() {
        return bugType;
    }

    public void setBugType(String bugType) {
        this.bugType = bugType;
    }

    public BigDecimal getBugState() {
        return bugState;
    }

    public void setBugState(BigDecimal bugState) {
        this.bugState = bugState;
    }

    public Tbugcomment getComment() {
        return comment;
    }

    public void setComment(Tbugcomment comment) {
        this.comment = comment;
    }

    public List<Tbugcomment> getBugComments() {
        return bugComments;
    }

    public void setBugComments(List<Tbugcomment> bugComments) {
        this.bugComments = bugComments;
    }

    public List<SelectItem> getIterationItems() {
        return iterationItems;
    }

    public void setIterationItems(List<SelectItem> iterationItems) {
        this.iterationItems = iterationItems;
    }

    public void setBugList(List<Tbug> bugList) {
        this.bugList = bugList;
    }

    public BigDecimal getIterationID() {
        return iterationID;
    }

    public void setIterationID(BigDecimal iterationID) {
        this.iterationID = iterationID;
    }

    public List<Ttestcase> getTestcaseList() {
        return testcaseList;
    }

    public void setTestcaseList(List<Ttestcase> testcaseList) {
        this.testcaseList = testcaseList;
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

    public Tbug getBug() {
        return bug;
    }

    public void setBug(Tbug bug) {
        this.bug = bug;
    }

    public List<Tbug> getBugList() {
        return bugList;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public int getReportedBug(BigDecimal userID) {
        bug_dao dao = new bug_dao();
        List<Tbug> bugsByUserID = dao.getBugsByUserID(userID);
        if (bugsByUserID != null) {
            return bugsByUserID.size();
        } else {
            return 0;
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

    public List<SelectItem> getBugStateComboItem() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        bugstate_dao dao = new bugstate_dao();
        List<Tbugstate> bugStates = dao.getAllBugstates();
        for (Tbugstate state : bugStates) {
            items.add(new SelectItem(state.getId(), state.getName()));
        }
        return items;
    }

    public List<SelectItem> getBugTypeForFilter() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        bugtype_dao dao = new bugtype_dao();
        List<Tbugtype> bugStates = dao.getAllBugtypes();
        for (Tbugtype type : bugStates) {
            items.add(new SelectItem(type.getName(), type.getName()));
        }
        return items;
    }

    public List<SelectItem> getBugStateForFilter() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        bugstate_dao dao = new bugstate_dao();
        List<Tbugstate> bugStates = dao.getAllBugstates();
        for (Tbugstate state : bugStates) {
            items.add(new SelectItem(state.getName(), state.getName()));
        }
        return items;
    }

    public List<SelectItem> getTestcaseItem() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        items.add(new SelectItem(BigDecimal.valueOf(-1), ""));
        if (testcaseList != null) {
            for (Ttestcase testcase : testcaseList) {
                items.add(new SelectItem(testcase.getId(), testcase.getTitle()));
            }
        }
        return items;
    }

    public List<SelectItem> getIterationComboItem() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        iteration_dao dao = new iteration_dao();
        List<Titeration> iterationList = dao.getIterationsByProjectID(projectID);
        items.add(new SelectItem(-1, ""));
        if (iterationList != null) {
            for (Titeration iteration : iterationList) {
                items.add(new SelectItem(iteration.getId(), iteration.getName()));
            }
        }
        return items;
    }

    public List<SelectItem> getBugtypeComboItem() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        bugtype_dao dao = new bugtype_dao();
        List<Tbugtype> bugTypeList = dao.getAllBugtypes();
        if (bugTypeList != null) {
            for (Tbugtype bt : bugTypeList) {
                items.add(new SelectItem(bt.getId(), bt.getName()));
            }
        }
        return items;
    }
}
