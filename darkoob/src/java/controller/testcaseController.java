package controller;

import entity.Tintegratedpart;
import entity.Tprerequisite;
import entity.Tproject;
import entity.Tprojecttree;
import entity.Ttestcase;
import entity.TtestcaseIter;
import entity.TtestcaseProjecttree;
import entity.Ttestcasefile;
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
import model.integratedParts_dao;
import model.prerequisite_dao;
import model.projectTree_dao;
import model.project_dao;
import model.testcaseFile_dao;
import model.testcaseIteration_dao;
import model.testcaseProjectTree_dao;
import model.testcase_dao;
import model.version_dao;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;

@ManagedBean(name = "testcaseControl")
@SessionScoped
public class testcaseController implements Serializable {

    @ManagedProperty(value = "#{loginControl}")
    private loginController loginControl;

    private StreamedContent file;
    List<SelectItem> projectItems = new ArrayList<SelectItem>();
    List<SelectItem> secondIntegratedCombo = new ArrayList<SelectItem>();

    private TreeNode root;
    private TreeNode[] selectedNodes;
    private List<Ttestcase> prerequisite;
    private List<Tprojecttree> projectTreeList = new ArrayList();
    private List<Ttestcase> testcaseList = new ArrayList();
    private List<Ttestcase> filteredTestcaseList = new ArrayList();
    private Ttestcase testcase = new Ttestcase();

    private TreeNode selectedNode;
    private Tprojecttree selectedProjectTree;
    private List<BigDecimal> selectedProjectTreeList = new ArrayList();

    private List<Ttestcasefile> testcaseFiles = new ArrayList();

    private BigDecimal projectID;
    private BigDecimal firstInteg;
    private BigDecimal secondInteg;

    private String types[];

    private String destination = "/app/darkoob/uploadedFiles/TestcaseFiles/";
    private UploadedFile uploadFile;

    @PostConstruct
    private void init() {
        initProjectTree();
    }

    public void viewAttachmentDialog(Ttestcase testcase) {
        this.testcase = testcase;
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        options.put("contentHeight", 440);
        RequestContext.getCurrentInstance().openDialog("attachFileToTestcase", options, null);
    }

    public void viewTestcaseDialog(Ttestcase testcase) {
        this.testcase = testcase;
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        options.put("scrollabale", false);
        options.put("contentHeight", 520);
        //options.put("draggable", false);

        RequestContext.getCurrentInstance().openDialog("viewTestcase", options, null);
    }

    public void viewNewTestcaseDialog() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        options.put("scrollabale", false);
        options.put("contentHeight", 520);
        resetInputs();
        initProjectTree();
        RequestContext.getCurrentInstance().openDialog("createTestcase", options, null);
    }

    public void viewEditTestcaseDialog(Ttestcase testcase) {
        this.testcase = testcase;
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        options.put("scrollabale", false);
        options.put("contentHeight", 520);
        RequestContext.getCurrentInstance().openDialog("editTestcase", options, null);
    }

    public void viewAssignToTreeDialog(Ttestcase testcase) {
        this.testcase = testcase;
        testcaseProjectTree_dao testcaseProjecttreeDAO = new testcaseProjectTree_dao();
        List<TtestcaseProjecttree> testcaseProjectTree = testcaseProjecttreeDAO.getTestcaseProjectTreeByTestcaseID(testcase.getId());

        onProjectChange();

        if (testcaseProjectTree != null) {
            projectID = testcaseProjectTree.get(0).getTprojecttree().getTproject().getId();
            getSelectedProjectTreeList();
            initProjectTree();
        }
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        options.put("scrollabale", false);
        options.put("contentHeight", 520);
        RequestContext.getCurrentInstance().openDialog("assignTestcase", options, null);
    }

    public void closeDialog(String name) {
        RequestContext.getCurrentInstance().closeDialog(name);
    }

    public String insertTestcase() {
        testcase_dao dao = new testcase_dao();

        testcase.setTitle(testcase.getTitle().trim());
        testcase.setTestscenario(testcase.getTestscenario().trim());
        testcase.setPurpose(testcase.getPurpose().trim());
        testcase.setProspectresult(testcase.getProspectresult().trim());

        if (!testcase.getTitle().equals("")) {
            if (testcase.getTitle().length() <= 100) {
                if (!testcase.getTestscenario().equals("")) {
                    if (testcase.getTestscenario().length() <= 4000) {
                        if (!testcase.getProspectresult().equals("")) {
                            if (testcase.getProspectresult().length() <= 1000) {
                                if (selectedNodes.length > 0 || testcase.getTestcasetype().equals("I")) {
                                    if (types.length == 1) {
                                        testcase.setScenariotype(types[0]);
                                    } else if (types.length == 2) {
                                        if (isArrayContainsString(types, "P") && isArrayContainsString(types, "N")) {
                                            testcase.setScenariotype("M");
                                        } else if (isArrayContainsString(types, "P") && isArrayContainsString(types, "E")) {
                                            testcase.setScenariotype("S");
                                        } else if (isArrayContainsString(types, "N") && isArrayContainsString(types, "E")) {
                                            testcase.setScenariotype("G");
                                        }
                                    } else {
                                        testcase.setScenariotype("F");
                                    }
                                    testcase.setTuser(loginControl.getEnteredUser());
                                    testcase.setCreationdate(new Date());
                                    dao.createTestcase(testcase);

                                    testcaseProjectTree_dao testcaseProjectTreeDao = new testcaseProjectTree_dao();
                                    if (testcase.getTestcasetype().equals("S")) {
                                        for (int i = 0; i < selectedNodes.length; i++) {
                                            Tprojecttree projecttree = (Tprojecttree) selectedNodes[i].getData();
                                            TtestcaseProjecttree testcaseProjecttree = new TtestcaseProjecttree(projecttree, testcase);
                                            testcaseProjectTreeDao.createTestcaseProjectTree(testcaseProjecttree);
                                        }
                                    } else if (testcase.getTestcasetype().equals("I")) {
                                        Tintegratedpart integratedPart;
                                        TtestcaseProjecttree testcaseProjecttree = new TtestcaseProjecttree();
                                        integratedParts_dao integDAO = new integratedParts_dao();
                                        integratedPart = integDAO.getIntegratedPartByFirstAndSecondID(firstInteg, secondInteg);
                                        testcaseProjecttree.setTtestcase(testcase);
                                        testcaseProjecttree.setTintegratedpart(integratedPart);
                                        testcaseProjectTreeDao.createTestcaseProjectTree(testcaseProjecttree);

                                    }

                                    for (int i = 0; i < prerequisite.size(); i++) {
                                        prerequisite_dao prerequisiteDao = new prerequisite_dao();
                                        Tprerequisite prerequis = new Tprerequisite(testcase, prerequisite.get(i));
                                        prerequisiteDao.createPrerequisite(prerequis);
                                    }

                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "عملیات موفق", "عملیات با موفقیت انجام شد."));
                                    refreshList();
                                    closeDialog("createTestcase");
                                    return "";
                                } else {
                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "گره های مورد نظر را انتخاب کنید."));
                                    return "";
                                }
                            } else {
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نتیجه مورد انتظار باید حداکثر 1000 کاراکتر باشد."));
                                return "";
                            }
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نتیجه مورد انتظار را وارد کنید."));
                            return "";
                        }
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "سناریوی تست باید حداکثر 4000 کاراکتر باشد."));
                        return "";
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "سناریو تست را وارد کنید."));
                    return "";
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "عنوان مورد تست باید حداکثر 100 کاراکتر باشد."));
                return "";
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "عنوان مورد تست را وارد کنید."));
            return "";
        }

    }

    public String update() {

        testcase_dao dao = new testcase_dao();

        testcase.setTitle(testcase.getTitle().trim());
        testcase.setTestscenario(testcase.getTestscenario().trim());
        testcase.setPurpose(testcase.getPurpose().trim());
        testcase.setProspectresult(testcase.getProspectresult().trim());

        if (!testcase.getTitle().equals("")) {
            if (testcase.getTitle().length() <= 100) {
                if (!testcase.getTestscenario().equals("")) {
                    if (testcase.getTestscenario().length() <= 4000) {
                        if (!testcase.getProspectresult().equals("")) {
                            if (testcase.getProspectresult().length() <= 1000) {
                                if (types.length == 1) {
                                    testcase.setScenariotype(types[0]);
                                } else if (types.length == 2) {
                                    if (isArrayContainsString(types, "P") && isArrayContainsString(types, "N")) {
                                        testcase.setScenariotype("M");
                                    } else if (isArrayContainsString(types, "P") && isArrayContainsString(types, "E")) {
                                        testcase.setScenariotype("S");
                                    } else if (isArrayContainsString(types, "N") && isArrayContainsString(types, "E")) {
                                        testcase.setScenariotype("G");
                                    }
                                } else {
                                    testcase.setScenariotype("F");
                                }

                                dao.updateTestcase(testcase);

                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "عملیات موفق", "عملیات با موفقیت انجام شد."));
                                refreshList();
                                closeDialog("editTestcase");
                                return "";
                            } else {
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نتیجه مورد انتظار باید حداکثر 1000 کاراکتر باشد."));
                                return "";
                            }
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نتیجه مورد انتظار را وارد کنید."));
                            return "";
                        }
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "سناریوی تست باید حداکثر 4000 کاراکتر باشد."));
                        return "";
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "سناریو تست را وارد کنید."));
                    return "";
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "عنوان مورد تست باید حداکثر 100 کاراکتر باشد."));
                return "";
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "عنوان مورد تست را وارد کنید."));
            return "";
        }

    }

    public String assignTestcaseToTree() {
        testcaseProjectTree_dao testcaseProjectTreeDao = new testcaseProjectTree_dao();
        testcaseProjectTreeDao.deleteByTestcaseID(testcase.getId());
        for (int i = 0; i < selectedNodes.length; i++) {
            Tprojecttree projecttree = (Tprojecttree) selectedNodes[i].getData();
            TtestcaseProjecttree testcaseProjectTree = new TtestcaseProjecttree(projecttree, testcase);
            testcaseProjectTreeDao.createTestcaseProjectTree(testcaseProjectTree);
        }
        refreshList();
        closeDialog("assignTestcase");
        resetInputs();
        return "";
    }

    public void removeTestcase(Ttestcase testcase) {
        testcase_dao testcaseDao = new testcase_dao();
        testcaseProjectTree_dao testcaseProjecttreeDao = new testcaseProjectTree_dao();
        testcaseIteration_dao testcaseIterationDAO = new testcaseIteration_dao();
        List<BigDecimal> testcaseIterationByTestCaseID = testcaseIterationDAO.getTestcaseIterationByTestCaseID(testcase.getId());
        if (testcaseIterationByTestCaseID == null) {
            testcaseProjecttreeDao.deleteByTestcaseID(testcase.getId());
            testcase.setDto(new Date());
            testcaseDao.updateTestcase(testcase);
            refreshList();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "امکان حذف وجود ندارد", "این مورد تست به تکرار تست منتسب شده است. لیست تکرار: " + testcaseIterationByTestCaseID));
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
        projectTree_dao projectTree_dao = new projectTree_dao();
        TreeNode newNode = new DefaultTreeNode(ttParent, parent);
        Tprojecttree projTree = (Tprojecttree) newNode.getData();
        if (selectedProjectTreeList != null && selectedProjectTreeList.contains(projTree.getId())) {
            newNode.setSelected(true);
            newNode.setExpanded(true);
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

    public void download(Ttestcasefile selectedFile) {
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

    public void remove(Ttestcasefile selectedFile) {
        testcaseFile_dao dao = new testcaseFile_dao();
        selectedFile.setDto(new Date());
        dao.updateTestcaseFile(selectedFile);
    }

    public void upload(FileUploadEvent event) {
        try {
            testcaseFile_dao dao = new testcaseFile_dao();
            Ttestcasefile testcaseFile = new Ttestcasefile();
            String fileName = event.getFile().getFileName();
            String fileType = fileName.substring(fileName.indexOf("."));
            testcaseFile.setFilename(fileName);
            testcaseFile.setTtestcase(testcase);
            testcaseFile.setTuser(loginControl.getEnteredUser());
            testcaseFile.setFilepath(destination);
            testcaseFile.setCreationdate(new Date());
            BigDecimal testcaseFileID = dao.createTestcaseFile(testcaseFile);
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
            System.out.println("New file created!");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void onProjectChange() {
        if (projectID != null && !projectID.equals(BigDecimal.valueOf(-1))) {
            initProjectTree();
        } else {
            root = null;
        }
    }

    public void onIntegChange() {
        if (firstInteg != null && !firstInteg.equals(BigDecimal.valueOf(-1))) {
            secondIntegratedCombo = getSecondIntegComboItem();
        }
    }

    public void onNodeChange(Tprojecttree node) {
        testcase_dao testcaseDAO = new testcase_dao();
        if (node != null) {
            selectedProjectTree = node;
            testcaseList = testcaseDAO.getTestcaseByProjecttreeID(selectedProjectTree.getId());
        }
    }

    public void onUnselect() {
        selectedProjectTree = null;
        selectedNode = null;
        refreshList();
    }

    public void resetInputs() {
        testcase = new Ttestcase();
        firstInteg = null;
        secondInteg = null;
        testcase.setExecutationtype("M");
        testcase.setTestcasetype("S");

        if (selectedNodes != null) {
            selectedNodes = null;
            root = getRoot();
        }

        if (prerequisite != null) {
            prerequisite.clear();
        }
    }

    public void refreshList() {
        testcase_dao dao = new testcase_dao();
        if (projectID == null || projectID.compareTo(BigDecimal.valueOf(-1)) == 0) {
            testcaseList = dao.getAllTestcases();
        } else {
            testcaseList = dao.getTestcaseListByProjectID(projectID);
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

    public boolean isArrayContainsString(String[] arr, String str) {
        boolean contain = false;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(str)) {
                contain = true;
                break;
            }
        }
        return contain;
    }

    //***********************Setters & Getters******************************//
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

    public loginController getLoginControl() {
        return loginControl;
    }

    public void setLoginControl(loginController loginControl) {
        this.loginControl = loginControl;
    }

    public List<Tprojecttree> getProjectTreeList() {
        return projectTreeList;
    }

    public void setProjectTreeList(List<Tprojecttree> projectTreeList) {
        this.projectTreeList = projectTreeList;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public List<Ttestcase> getPrerequisite() {
        return prerequisite;
    }

    public void setPrerequisite(List<Ttestcase> prerequisite) {
        this.prerequisite = prerequisite;
    }

    public UploadedFile getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(UploadedFile uploadFile) {
        this.uploadFile = uploadFile;
    }

    public TreeNode getRoot() {
        return root;
    }

    public List<Ttestcase> getFilteredTestcaseList() {
        return filteredTestcaseList;
    }

    public void setFilteredTestcaseList(List<Ttestcase> filteredTestcaseList) {
        this.filteredTestcaseList = filteredTestcaseList;
    }

    public List<BigDecimal> getSelectedProjectTreeList() {
        testcaseProjectTree_dao dao = new testcaseProjectTree_dao();
        selectedProjectTreeList = dao.getProjectTreeByTestcaseID(testcase.getId());
        return selectedProjectTreeList;
    }

    public void setSelectedProjectTreeList(List<BigDecimal> selectedProjectTreeList) {
        this.selectedProjectTreeList = selectedProjectTreeList;
    }

    public List<Ttestcasefile> getTestcaseFiles() {
        if (testcase.getId() != null) {
            testcaseFile_dao dao = new testcaseFile_dao();
            testcaseFiles = dao.getTestcaseFileByTestcaseID(testcase.getId());
            return testcaseFiles;
        } else {
            return new ArrayList();
        }
    }

    public void setTestcaseFiles(List<Ttestcasefile> testcaseFiles) {
        this.testcaseFiles = testcaseFiles;
    }

    public TreeNode[] getSelectedNodes() {
        return selectedNodes;
    }

    public void setSelectedNodes(TreeNode[] selectedNodes) {
        this.selectedNodes = selectedNodes;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String types[]) {
        this.types = types;
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

    public Ttestcase getTestcase() {
        return testcase;
    }

    public void setTestcase(Ttestcase testcase) {
        this.testcase = testcase;
    }

    public List<Ttestcase> getTestcaseList() {
        return testcaseList;
    }

    public StreamedContent getFile() {
        return file;
    }

    public int getSubmitedTestcase(BigDecimal userID) {
        testcase_dao dao = new testcase_dao();
        List<Ttestcase> testcaseByID = dao.getTestcasesByUserID(userID);
        if (testcaseByID != null) {
            return testcaseByID.size();
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

    public List<SelectItem> getFirstIntegComboItem() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        integratedParts_dao dao = new integratedParts_dao();
        if (projectID != null) {
            List<Tprojecttree> integratedParts = dao.getFirstProjectTreeByProjectID(projectID);
            items.add(new SelectItem(-1, ""));
            if (integratedParts != null) {
                for (Tprojecttree integratedPart : integratedParts) {
                    items.add(new SelectItem(integratedPart.getId(), integratedPart.getTprojecttree().getName() + integratedPart.getName()));
                }
            }
        }
        return items;
    }

    public List<SelectItem> getSecondIntegComboItem() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        integratedParts_dao dao = new integratedParts_dao();
        items.add(new SelectItem(-1, ""));
        if (firstInteg != null) {
            List<Tintegratedpart> integratedParts = dao.getIntegratedPartsByFirstIntegrationID(firstInteg);
            if (integratedParts != null) {
                for (Tintegratedpart integratedPart : integratedParts) {
                    items.add(new SelectItem(integratedPart.getTprojecttreeByProjecttreeid2().getId(), integratedPart.getTprojecttreeByProjecttreeid2().getTprojecttree().getName() + integratedPart.getTprojecttreeByProjecttreeid2().getName()));
                }
            }
        }
        return items;
    }

    public BigDecimal getFirstInteg() {
        return firstInteg;
    }

    public void setFirstInteg(BigDecimal firstInteg) {
        this.firstInteg = firstInteg;
    }

    public BigDecimal getSecondInteg() {
        return secondInteg;
    }

    public void setSecondInteg(BigDecimal secondInteg) {
        this.secondInteg = secondInteg;
    }

    public List<SelectItem> getSecondIntegratedCombo() {
        return secondIntegratedCombo;
    }

    public void setSecondIntegratedCombo(List<SelectItem> secondIntegratedCombo) {
        this.secondIntegratedCombo = secondIntegratedCombo;
    }

}
