package controller;

import entity.Tbug;
import entity.Titeration;
import entity.Tproject;
import entity.Ttestcase;
import entity.TtestcaseIter;
import entity.Tversion;
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
import model.bug_dao;
import model.iteration_dao;
import model.project_dao;
import model.testcaseIteration_dao;
import model.testcase_dao;
import model.version_dao;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "iterationControl")
@SessionScoped
public class iterationController implements Serializable {

    @ManagedProperty(value = "#{loginControl}")
    private loginController loginControl;

    List<SelectItem> projectItems = new ArrayList<SelectItem>();
    List<SelectItem> versionItems = new ArrayList<SelectItem>();

    private List<Titeration> iterationList = new ArrayList();
    private List<Ttestcase> testcaseListInIteraion = new ArrayList();
    private List<Ttestcase> selectedTestcaseList = new ArrayList();
    private Titeration iteration = new Titeration();
    private Ttestcase selectedTestcase = new Ttestcase();

    private BigDecimal projectID;
    private BigDecimal subsystemID;
    private BigDecimal versionID;

    public void viewNewIterationDialog() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        options.put("scrollabale", false);
        resetInputs();
        onProjectChange();
        options.put("contentHeight", 550);
        RequestContext.getCurrentInstance().openDialog("createIteration", options, null);
    }

    public void viewEditIterationDialog(Titeration iteration) {
        testcase_dao testcaseDao = new testcase_dao();
        this.iteration = iteration;
        projectID = iteration.getTversion().getTprojecttree().getTproject().getId();
        versionID = iteration.getTversion().getId();
        onProjectChange();

        projectID = iteration.getTversion().getTprojecttree().getTproject().getId();
        versionID = iteration.getTversion().getId();

        selectedTestcaseList = testcaseDao.getTestcaseByIterationID(iteration.getId());
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        options.put("contentHeight", 550);
        RequestContext.getCurrentInstance().openDialog("editIteration", options, null);
    }

    public void viewTestcaseOfIterationDialog(Titeration iteration) {
        testcase_dao testcaseDao = new testcase_dao();
        this.iteration = iteration;

        testcaseListInIteraion = testcaseDao.getTestcaseByIterationID(iteration.getId());

        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        options.put("scrollabale", false);
        options.put("contentHeight", 500);
        RequestContext.getCurrentInstance().openDialog("viewIteration", options, null);
    }

    public void closeDialog(String name) {
        RequestContext.getCurrentInstance().closeDialog(name);
    }

//        public void activeDialog() {
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        Map map = facesContext.getExternalContext().getRequestParameterMap();
//        String submitedValue = (String) map.get("activeDialog");
//        FacesMessage facesMessage = new FacesMessage(submitedValue);
//        facesContext.addMessage(null, facesMessage);
//    }
    public String insertIteration() {

        iteration.setName(iteration.getName().trim());
        iteration.setDescription(iteration.getDescription().trim());

        if (projectID != null && projectID.compareTo(BigDecimal.valueOf(-1)) != 0) {
            if (versionID != null && versionID.compareTo(BigDecimal.valueOf(-1)) != 0) {
                if (!iteration.getName().equals("")) {
                    if (iteration.getName().length() <= 50) {
                        if (iteration.getDescription().length() <= 500) {
                            iteration_dao iterationDao = new iteration_dao();
                            version_dao versionDao = new version_dao();
                            testcaseIteration_dao testcaseIterationDao = new testcaseIteration_dao();
                            Tversion version = versionDao.getVersionByID(versionID);
                            iteration.setTuser(loginControl.getEnteredUser());
                            iteration.setCreationdate(new Date());
                            iteration.setTversion(version);
                            BigDecimal iterationID = iterationDao.createIteration(iteration);
                            Titeration iteration = iterationDao.getIterationByID(iterationID);
                            for (int i = 0; i < selectedTestcaseList.size(); i++) {
                                TtestcaseIter testcaseiteration = new TtestcaseIter(selectedTestcaseList.get(i), iteration);
                                testcaseIterationDao.createTestcaseIteration(testcaseiteration);
                            }
                            refreshList();
                            closeDialog("createIteration");
                            resetInputs();
                            return "";
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "توضیحات تکرار تست باید حداکثر 500 کاراکتر باشد."));
                            return "";
                        }
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام تکرار تست باید حداکثر 50 کاراکتر باشد."));
                        return "";
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام تکرار تست را وارد کنید."));
                    return "";
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", " نسخه را انتخاب کنید."));
                return "";
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "پروژه را انتخاب کنید."));
            return "";
        }

    }

    public String update() {

        iteration.setName(iteration.getName().trim());
        iteration.setDescription(iteration.getDescription().trim());

        if (projectID != null && projectID.compareTo(BigDecimal.valueOf(-1)) != 0) {
            if (versionID != null && versionID.compareTo(BigDecimal.valueOf(-1)) != 0) {
                if (!iteration.getName().equals("")) {
                    if (iteration.getName().length() <= 50) {
                        if (iteration.getDescription().length() <= 500) {
                            iteration_dao iterationDao = new iteration_dao();
                            testcaseIteration_dao testcaseIterationDao = new testcaseIteration_dao();
                            TtestcaseIter testcaseIterationByTestdate = testcaseIterationDao.getTestcaseIterationByIterationIDANDTestdate(iteration.getId(), null);
                            if (testcaseIterationByTestdate == null) {
                                version_dao versionDAO = new version_dao();
                                Tversion versionByID = versionDAO.getVersionByID(versionID);
                                iteration.setTversion(versionByID);
                                iterationDao.updateIteration(iteration);
                                testcaseIterationDao.deleteByIterationID(iteration.getId());
                                for (int i = 0; i < selectedTestcaseList.size(); i++) {
                                    TtestcaseIter testcaseIter = new TtestcaseIter(selectedTestcaseList.get(i), iteration);
                                    testcaseIterationDao.createTestcaseIteration(testcaseIter);
                                }
                                refreshList();
                                closeDialog("editIteration");
                                resetInputs();
                                return "";
                            } else {
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "امکان ویرایش وجود ندارد. سناریویی از این تکرار اجرا شده است."));
                                return "";
                            }
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "توضیحات تکرار تست باید حداکثر 500 کاراکتر باشد."));
                            return "";
                        }
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام تکرار تست باید حداکثر 50 کاراکتر باشد."));
                        return "";
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام تکرار تست را وارد کنید."));
                    return "";
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", " نسخه را انتخاب کنید."));
                return "";
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "پروژه را انتخاب کنید."));
            return "";
        }
    }

    public void removeIteration(Titeration iteration) {
        iteration_dao dao = new iteration_dao();
        bug_dao bugDAO = new bug_dao();
        testcaseIteration_dao testcaseIterationDAO = new testcaseIteration_dao();
        List<BigDecimal> bugByIterationID = bugDAO.getBugsByIterationID(iteration.getId());
        if (bugByIterationID == null) {
            List<BigDecimal> testcaseByIterationID = testcaseIterationDAO.getTestedTestcaseIterationByIterationID(iteration.getId());
            if (testcaseByIterationID == null) {
                testcaseIterationDAO.deleteByIterationID(iteration.getId());
                iteration.setDto(new Date());
                dao.updateIteration(iteration);
                refreshList();
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "امکان حذف وجود ندارد", "مورد تستی از این تکرار تکمیل شده است. لیست مورد تست ها: " + testcaseByIterationID));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "امکان حذف وجود ندارد", "خطایی بر روی این تکرار تست ثبت شده است. لیست خطاها: " + bugByIterationID));
        }

    }

    public void onProjectChange() {
        testcase_dao testcaseDao = new testcase_dao();
        versionItems = getVersionComboItem();
        testcaseListInIteraion = testcaseDao.getTestcaseListByProjectID(projectID);
        refreshList();
    }

    public void resetInputs() {
        iteration.setName(null);
        iteration.setDescription(null);
        iteration.setEnddate(null);
        iteration.setStartdate(null);
        iteration.setState(null);
        iteration.setTversion(null);
        versionID = null;
        selectedTestcaseList.clear();
        testcaseListInIteraion = new ArrayList();
    }

    public void refreshList() {
        iteration_dao dao = new iteration_dao();
        if (projectID == null || projectID.equals(BigDecimal.valueOf(-1))) {
            iterationList = dao.getAllIterations();
        } else {
            iterationList = dao.getIterationsByProjectID(projectID);
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
    public loginController getLoginControl() {
        return loginControl;
    }

    public void setLoginControl(loginController loginControl) {
        this.loginControl = loginControl;
    }

    public Ttestcase getSelectedTestcase() {
        return selectedTestcase;
    }

    public void setSelectedTestcase(Ttestcase selectedTestcase) {
        this.selectedTestcase = selectedTestcase;
    }

    public List<Ttestcase> getScenarioListInIteraion() {
        return testcaseListInIteraion;
    }

    public void setScenarioListInIteraion(List<Ttestcase> testcaseListInIteraion) {
        this.testcaseListInIteraion = testcaseListInIteraion;
    }

    public List<Ttestcase> getSelectedTestcaseList() {
        return selectedTestcaseList;
    }

    public void setSelectedTestcaseList(List<Ttestcase> selectedTestcaseList) {
        this.selectedTestcaseList = selectedTestcaseList;
    }

    public List<SelectItem> getProjectItems() {
        return projectItems;
    }

    public List<SelectItem> getVersionItems() {
        return versionItems;
    }

    public BigDecimal getProjectID() {
        return projectID;
    }

    public void setProjectID(BigDecimal projectID) {
        this.projectID = projectID;
    }

    public BigDecimal getVersionID() {
        return versionID;
    }

    public void setVersionID(BigDecimal versionID) {
        this.versionID = versionID;
    }

    public BigDecimal getSubsystemID() {
        return subsystemID;
    }

    public void setSubsystemID(BigDecimal subsystemID) {
        this.subsystemID = subsystemID;
    }

    public Titeration getIteration() {
        return iteration;
    }

    public void setIteration(Titeration iteration) {
        this.iteration = iteration;
    }

    public List<Titeration> getIterationList() {
        return iterationList;

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

    public List<SelectItem> getVersionComboItem() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        version_dao dao = new version_dao();
        List<Tversion> versionList = dao.getVersionByProjectID(projectID);
        items.add(new SelectItem(-1, ""));
        if (versionList != null) {
            for (Tversion version : versionList) {
                items.add(new SelectItem(version.getId(), version.getName()));
            }
        }
        return items;
    }

    public List<SelectItem> getIterationComboItem() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        version_dao dao = new version_dao();
        items.add(new SelectItem(-1, ""));
        if (iterationList != null) {
            for (Titeration iter : iterationList) {
                items.add(new SelectItem(iter.getId(), iter.getName()));
            }
        }
        return items;
    }

}
