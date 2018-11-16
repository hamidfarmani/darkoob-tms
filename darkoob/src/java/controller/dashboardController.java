package controller;

import entity.Tbug;
import entity.Titeration;
import entity.Trequirement;
import entity.TtestcaseIter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import model.bug_dao;
import model.iteration_dao;
import model.projectTree_dao;
import model.requirement_dao;
import model.testcaseIteration_dao;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.PieChartModel;

@ManagedBean(name = "dashboardControl")
@SessionScoped
public class dashboardController implements Serializable {

    private BigDecimal selectedIterationID;
    private BigDecimal projectID;

    private LineChartModel iterationBugLineChart;
    private BarChartModel iterationBugBarChart;
    private BarChartModel lastIterBugTypes;

    private PieChartModel bugStates;
    private PieChartModel implementedRequirement;
    private PieChartModel assignedReq;
    private PieChartModel executionStatus;

    @PostConstruct
    public void init() {
        refreshDashboard();
    }

    public void refreshDashboard() {
        createIterationCharts();
        createAssignedReq();
        createExecutionStatus();
        createBugStates();
        createImplementedRequirements();
        createBugStatus();
    }

    private void createBugStatus() {
        lastIterBugTypes = createIterBugsStatus();

        lastIterBugTypes.setTitle("وضعیت خطاها در تکرار");
        lastIterBugTypes.setShowDatatip(false);
        lastIterBugTypes.setShowPointLabels(true);
        Axis xAxis = lastIterBugTypes.getAxis(AxisType.X);
        xAxis.setTickAngle(-90);
        xAxis.setLabel("وضعیت خطا");

        Axis yAxis = lastIterBugTypes.getAxis(AxisType.Y);
        yAxis.setLabel("تعداد خطا");
        lastIterBugTypes.setAnimate(true);

    }

    private void createBugStates() {
        bugStates = new PieChartModel();
        bug_dao bugDAO = new bug_dao();
        int critical = 0;
        int major = 0;
        int normal = 0;
        int minor = 0;
        int nullTypes = 0;
        if (projectID != null && projectID.compareTo(BigDecimal.valueOf(-1)) != 0) {
            List<Tbug> bugsList = bugDAO.getBugsByProjectID(projectID);
            if (bugsList != null) {
                for (Iterator<Tbug> iterator = bugsList.iterator(); iterator.hasNext();) {
                    Tbug next = iterator.next();
                    String type = next.getTbugtype().getName(); //need to be change. bug type is a
                                                                // table now.
                    if (type != null) {
                        if (type.equals("C")) {
                            critical++;
                        } else if (type.equals("J")) {
                            major++;
                        } else if (type.equals("N")) {
                            normal++;
                        } else if (type.equals("M")) {
                            minor++;
                        } else {
                            nullTypes++;
                        }
                    } else {
                        nullTypes++;
                    }
                }
            }
        }

        bugStates.set("بحرانی", critical);
        bugStates.set("اصلی", major);
        bugStates.set("معمولی", normal);
        bugStates.set("پایین", minor);
        bugStates.set("متفرقه", nullTypes);

        bugStates.setTitle("انواع خطاها در پروژه");
        bugStates.setExtender("pieExtender");
        bugStates.setLegendPosition("e");
        bugStates.setFill(true);
        bugStates.setShowDataLabels(true);
        bugStates.setMouseoverHighlight(true);
        bugStates.setDiameter(150);
    }

    private void createAssignedReq() {
        assignedReq = new PieChartModel();
        BigDecimal selectedProject = projectID;
        requirement_dao requirementDAO = new requirement_dao();
        int allReqs = 0;
        int assigned = 0;
        int notAssigned = 0;

        if (selectedProject != null && selectedProject.compareTo(BigDecimal.valueOf(-1)) != 0) {
            List<Trequirement> notAssignedRequirementByProjectID = requirementDAO.getNotAssignedRequirementByProjectID(selectedProject);
            List<Trequirement> requirementByProjectID = requirementDAO.getRequirementByProjectID(selectedProject);
            if (requirementByProjectID != null) {
                allReqs = requirementByProjectID.size();
                if (notAssignedRequirementByProjectID != null) {
                    notAssigned = notAssignedRequirementByProjectID.size();
                }
            }
        }
        assigned = allReqs - notAssigned;

        assignedReq.set("منتسب شده", assigned);
        assignedReq.set("منتسب نشده", notAssigned);
        assignedReq.setExtender("pieExtender");
        assignedReq.setTitle("وضعیت انتساب نیازمندی های پروژه");
        assignedReq.setShowDataLabels(true);
        assignedReq.setLegendPosition("w");
    }

    private void createExecutionStatus() {
        executionStatus = new PieChartModel();
        testcaseIteration_dao testcaseIterationDAO = new testcaseIteration_dao();
        int testedTCs = 0;
        int notTestedTCs = 0;
        if (projectID != null && projectID.compareTo(BigDecimal.valueOf(-1)) != 0) {
            List<TtestcaseIter> testcaseIters = testcaseIterationDAO.getTestcaseIterationByIterationID(selectedIterationID);
            if (testcaseIters != null) {
                for (Iterator<TtestcaseIter> iterator = testcaseIters.iterator(); iterator.hasNext();) {
                    TtestcaseIter next = iterator.next();
                    if (next.getTestdate() != null) {
                        testedTCs++;
                    }
                }
                notTestedTCs = testcaseIters.size() - testedTCs;
            }
        }

        executionStatus.set("تست شده", testedTCs);
        executionStatus.set("تست نشده", notTestedTCs);

        executionStatus.setTitle("آخرین وضعیت اجرای تکرار");
        executionStatus.setExtender("pieExtender");
        executionStatus.setLegendPosition("e");
        executionStatus.setFill(true);
        executionStatus.setShowDataLabels(true);
        executionStatus.setMouseoverHighlight(true);
        executionStatus.setDiameter(150);
    }

    private void createImplementedRequirements() {
        implementedRequirement = new PieChartModel();
        requirement_dao requirementDAO = new requirement_dao();
        int implemented = 0;
        int notImplemented = 0;
        int allReqs = 0;
        if (projectID != null && projectID.compareTo(BigDecimal.valueOf(-1)) != 0) {
            List<Trequirement> requirementByProjectID = requirementDAO.getRequirementByProjectID(projectID);
            if (requirementByProjectID != null) {
                allReqs = requirementByProjectID.size();
            }
        }

        if (projectID != null && projectID.compareTo(BigDecimal.valueOf(-1)) != 0) {
            BigDecimal implementedRequirementsByProjectID = requirementDAO.getImplementedRequirementsByProjectID(projectID);
            if (implementedRequirementsByProjectID != null) {
                implemented = implementedRequirementsByProjectID.intValue();

            }
        }
        notImplemented = allReqs - implemented;
        implementedRequirement.set("پیاده سازی شده", implemented);
        implementedRequirement.set("پیاده سازی نشده", notImplemented);

        implementedRequirement.setTitle("وضعیت پیاده سازی نیازمندی ها");
        implementedRequirement.setExtender("pieExtender");
        implementedRequirement.setLegendPosition("e");
        implementedRequirement.setFill(true);
        implementedRequirement.setShowDataLabels(true);
        implementedRequirement.setMouseoverHighlight(true);
        implementedRequirement.setDiameter(150);
    }

    private void createIterationCharts() {
        iterationBugLineChart = initIterationBugLinearModel();
        iterationBugLineChart.setTitle("تعداد خطا در هر تکرار");
        iterationBugLineChart.setAnimate(true);
        iterationBugLineChart.setShowPointLabels(false);
        iterationBugLineChart.getAxes().put(AxisType.X, new CategoryAxis("نام تکرار"));
        Axis xAxis = iterationBugLineChart.getAxis(AxisType.X);
        xAxis.setTickAngle(-90);
        Axis yAxis = iterationBugLineChart.getAxis(AxisType.Y);
        yAxis.setLabel("تعداد خطا");

        iterationBugBarChart = initIterationBugBarModel();
        iterationBugBarChart.setTitle("تعداد مورد تست های منتسب شده به گره های درخت");
        iterationBugBarChart.setAnimate(true);
        iterationBugBarChart.setShowDatatip(false);
        iterationBugBarChart.setShowPointLabels(true);
        Axis iter_bugxAxis = iterationBugBarChart.getAxis(AxisType.X);
        iter_bugxAxis.setTickAngle(-90);
        iterationBugBarChart.setLegendPosition("ne");

    }

    private BarChartModel createIterBugsStatus() {
        BarChartModel model = new BarChartModel();
        bug_dao bugDAO = new bug_dao();

        ChartSeries bugStates = new ChartSeries();
        bugStates.set(0, 0);
        bugStates.setLabel("وضعیت خطا در تکرار");
        if (selectedIterationID != null && selectedIterationID.compareTo(BigDecimal.valueOf(-1)) != 0) {
            List<Object[]> bugsOfIteration = bugDAO.getItersBugStatusByIterationID(selectedIterationID);
            if (bugsOfIteration != null) {
                bugStates = new ChartSeries();
                for (Object[] obj : bugsOfIteration) {
                    BigDecimal bugStateID = (BigDecimal) obj[0];
                    String name = String.valueOf(obj[1]);
                    int numOfBugs = Integer.valueOf(String.valueOf(obj[2]));
                    bugStates.set(name, numOfBugs);
                }
            }
        }

        model.addSeries(bugStates);
        return model;
    }

    private BarChartModel initIterationBugBarModel() {
        BarChartModel model = new BarChartModel();
        projectTree_dao projecttreeDAO = new projectTree_dao();

        List<Object[]> numberOfTestcases = projecttreeDAO.getNumberOfTestcaseOfProjecttrees(BigDecimal.valueOf(39350));

        ChartSeries serie1 = new ChartSeries();
        serie1.setLabel("Series 1");
        for (Object[] obj : numberOfTestcases) {
            BigDecimal firstName = (BigDecimal) obj[0];
            //projecttreeDAO.getProjecttreeByProjectID(firstName).get(0).getName();
            int id = Integer.valueOf(String.valueOf(obj[2]));
            serie1.set(firstName, id);
        }

        model.addSeries(serie1);

        return model;
    }

    private LineChartModel initIterationBugLinearModel() {
        LineChartModel model = new LineChartModel();

        ChartSeries bugs_iter = new ChartSeries();
        iteration_dao iterationDAO = new iteration_dao();
        projectTree_dao projecttreeDAO = new projectTree_dao();
        bugs_iter.set(0, 0);

        if (projectID != null && projectID.compareTo(BigDecimal.valueOf(-1)) != 0) {
            List<Object[]> bugsOfIteration = iterationDAO.getBugOfIterationsByProjectID(projectID);
            if (bugsOfIteration != null) {
                bugs_iter = new ChartSeries();
                for (Object[] obj : bugsOfIteration) {
                    BigDecimal iterID = (BigDecimal) obj[0];
                    String name = iterationDAO.getIterationByID(iterID).getName();
                    int numOfBugs = Integer.valueOf(String.valueOf(obj[1]));
                    bugs_iter.set(name, numOfBugs);
                }
            }
        }

        bugs_iter.setLabel("تعداد خطا");
        model.addSeries(bugs_iter);

        return model;
    }

    public void onIterationChange() {

    }

    //***********************Setters & Getters******************************//
    public BigDecimal getProjectID() {
        return projectID;
    }

    public void setProjectID(BigDecimal projectID) {
        this.projectID = projectID;
    }

    public BigDecimal getSelectedIterationID() {
        return selectedIterationID;
    }

    public void setSelectedIterationID(BigDecimal selectedIterationID) {
        this.selectedIterationID = selectedIterationID;
    }

    public PieChartModel getImplementedRequirement() {
        return implementedRequirement;
    }

    public void setImplementedRequirement(PieChartModel implementedRequirement) {
        this.implementedRequirement = implementedRequirement;
    }

    public BarChartModel getLastIterBugTypes() {
        return lastIterBugTypes;
    }

    public void setLastIterBugTypes(BarChartModel lastIterBugTypes) {
        this.lastIterBugTypes = lastIterBugTypes;
    }

    public PieChartModel getBugStates() {
        return bugStates;
    }

    public void setBugStates(PieChartModel bugStates) {
        this.bugStates = bugStates;
    }

    public LineChartModel getIterationBugLineChart() {
        return iterationBugLineChart;
    }

    public void setIterationBugLineChart(LineChartModel iterationBugLineChart) {
        this.iterationBugLineChart = iterationBugLineChart;
    }

    public BarChartModel getIterationBugBarChart() {
        return iterationBugBarChart;
    }

    public void setIterationBugBarChart(BarChartModel iterationBugBarChart) {
        this.iterationBugBarChart = iterationBugBarChart;
    }

    public PieChartModel getAssignedReq() {
        return assignedReq;
    }

    public PieChartModel getExecutionStatus() {
        return executionStatus;
    }

    public List<SelectItem> getAllIterationList() {
        iteration_dao dao = new iteration_dao();
        List<Titeration> iterationList = null;
        List<SelectItem> items = new ArrayList<SelectItem>();
        if (projectID != null && projectID.compareTo(BigDecimal.valueOf(-1)) != 0) {
            iterationList = dao.getIterationsByProjectID(projectID);
        }
        items.add(new SelectItem(-1, ""));
        if (iterationList != null) {
            for (Titeration iter : iterationList) {
                items.add(new SelectItem(iter.getId(), iter.getName()));
            }
        }
        System.out.println("iterationList: " + iterationList);
        return items;
    }
}
