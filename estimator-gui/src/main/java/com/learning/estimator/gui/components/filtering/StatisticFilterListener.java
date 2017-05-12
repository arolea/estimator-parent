package com.learning.estimator.gui.components.filtering;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.learning.estimator.common.logger.ILogger;
import com.learning.estimator.common.logger.LogManager;
import com.learning.estimator.common.statistics.NumericStatistics;
import com.learning.estimator.common.statistics.StatisticsUtils;
import com.learning.estimator.gui.charts.*;
import com.learning.estimator.gui.utils.ChartPanel;
import com.learning.estimator.gui.utils.LogArea;
import com.learning.estimator.gui.utils.LogColor;
import com.learning.estimator.model.entities.Project;
import com.learning.estimator.model.entities.User;
import com.learning.estimator.model.entities.UserGroup;
import com.learning.estimator.model.statistics.AvailableStatistics;
import com.learning.estimator.model.statistics.StatisticsQuery;
import com.learning.estimator.model.statistics.StatisticsResult;
import com.learning.estimator.statisticsclient.service.impl.StatisticsService;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.viritin.button.MButton.MClickListener;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;

/**
 * Listeners for filtering
 *
 * @author rolea
 */
@SpringComponent
@Scope(value = "vaadin-session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class StatisticFilterListener {

    private static final ILogger LOG = LogManager.getLogger(StatisticsFilter.class);
    @Autowired
    private ChartPanel chartPanel;
    @Autowired
    private StatisticsService service;
    @Autowired
    private VaadinSecurity security;
    @Autowired
    private VaadinSession session;
    @Autowired
    private LogArea logArea;
    @Autowired
    private StatisticsChartOptions options;
    @Autowired
    private StatisticsFilter filter;
    private Executor executor;
    private volatile StatisticsResult result;
    private volatile boolean canceled = false;

    @PostConstruct
    public void init() {
        initExecutor();
    }

    private void initExecutor() {
        ThreadFactory factory = new ThreadFactoryBuilder().
                setNameFormat("Statistic computation thread factory").
                setDaemon(true).
                setUncaughtExceptionHandler((thread, throwable) -> {
                    LOG.info("Exception caught in thread " + thread.getName() + " ( id : " + thread.getId() + " ) ");
                    LOG.error(throwable);
                }).
                build();
        executor = Executors.newCachedThreadPool(factory);
    }

    public MClickListener getComputeListener() {
        return () -> {
            computeStatistic();
        };
    }

    public MClickListener getCancelListener() {
        return () -> {
            canceled = true;
            filter.setButtonEnables("compute", true);
        };
    }

    public ValueChangeListener getUserSelectListener() {
        return (event) -> {
            User user = (User) filter.getComboBoxValue("user");
            if (user != null)
                filter.setComboValues("group", user.getGroups());
        };
    }

    private void computeStatistic() {
        filter.setButtonEnables("compute", false);

        AvailableStatistics statistic = (AvailableStatistics) filter.getComboBoxValue("statistic");
        User user = (User) filter.getComboBoxValue("user");
        Project project = (Project) filter.getComboBoxValue("project");
        UserGroup group = (UserGroup) filter.getComboBoxValue("group");

        if (statistic == null) {
            Notification.show("No statistic is selected");
            return;
        }

        StatisticsQuery query = buildQueryObject(statistic, user, project, group);

        long token = service.requestStatisticComputation(query);

        executor.execute(() -> {
            displayChart(statistic, token);
        });
    }

    private void displayChart(AvailableStatistics statistic, long token) {
        do {
            LOG.info("Requesting statistic computation result");
            result = service.requestStatisticResult(token);
            if (AvailableStatistics.NOT_YET_COMPUTED.equals(result.getStatistic())) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    LOG.error(e);
                }
            }
        } while (!canceled && AvailableStatistics.NOT_YET_COMPUTED.equals(result.getStatistic()));
        filter.setButtonEnables("compute", true);
        canceled = false;

        if (AvailableStatistics.NOT_YET_COMPUTED.equals(result.getStatistic())) {
            LOG.info("Computation canceled");
            return;
        }
        switch (result.getStatistic()) {
            case ESTIMATE_ACCURACY_BOX:
            case LOGGED_TIME_BOX:
            case VELOCITY_POINT_BOX:
                displayBoxChart(statistic);
                break;
            case ESTIMATE_ACCURACY_EVOLUTION:
            case LOGGED_TIME_EVOLUTION:
            case VELOCITY_POINTS_EVOLUTION:
                displaySplineChart(statistic);
                break;
            case ESTIMATE_ACCURACY_HISTOGRAM:
            case LOGGED_TIME_HISTOGRAM:
            case VELOCITY_POINT_HISTOGRAM:
                displayBarChart(statistic);
                break;
            default:
                break;
        }
    }

    private StatisticsQuery buildQueryObject(AvailableStatistics statistic, User user, Project project,
                                             UserGroup group) {
        return new StatisticsQuery().withUserId(getUserId(user))
                .withProjectId(getProjectId(project))
                .withGroupId(getGroupId(group))
                .withStartingDate((LocalDate) filter.getDateFieldValue("beginDate"))
                .withEndingDate((LocalDate) filter.getDateFieldValue("endDate"))
                .withBinSize(getBinSize())
                .withStatisticType(statistic);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void displayBarChart(AvailableStatistics statistic) {
        switch (options.getFrequencyRepresentation()) {
            case BAR:
                chartPanel.setChart(BarChartFactory.getBarChart(statistic.toString(), "Bins", "Count", new PlottableMap(getStatisticDescription(), (Map<Object, Number>) result.getData())));
                break;
            case CDF: {
                Map<Object, Number> data = (Map<Object, Number>) result.getData();
                double sum = data.values().stream().collect(Collectors.summingDouble(Number::doubleValue)).doubleValue();
                data.forEach((key, value) -> {
                    data.put(key, value.doubleValue() * 100 / sum);
                });

                List<Object> keys = data.keySet().stream().map(key -> Integer.valueOf((String) key)).sorted(Comparator.comparing(Integer::intValue)).collect(Collectors.toList());

                for (int i = 0; i < keys.size() - 1; i++) {
                    data.put(keys.get((i + 1)) + "",
                            data.get(keys.get(i) + "").doubleValue() +
                                    data.get(keys.get(i + 1) + "").doubleValue());
                }

                chartPanel.setChart(BarChartFactory.getBarChart(statistic.toString(), "Bins", "Percentile Rank", new PlottableMap(getStatisticDescription(), (Map) data)));
                break;
            }
            case DONUT:
                chartPanel.setChart(DonutChartFactory.getDonutChart(statistic.toString(), (Map<Object, Number>) result.getData()));
                break;
            case PIE:
                chartPanel.setChart(PieChartFactory.getPieChart(statistic.toString(), (Map<Object, Number>) result.getData()));
                break;
            case PMF: {
                Map<Object, Number> data = (Map<Object, Number>) result.getData();
                double sum = data.values().stream().collect(Collectors.summingDouble(Number::doubleValue)).doubleValue();
                data.forEach((key, value) -> {
                    data.put(key, value.doubleValue() * 100 / sum);
                });
                chartPanel.setChart(BarChartFactory.getBarChart(statistic.toString(), "Bins", "Probability", new PlottableMap(getStatisticDescription(), data)));
                break;
            }
        }
        printStatisticSummary(((Map<Object, Number>) result.getData()).values());
    }

    private void printStatisticSummary(Collection<? extends Number> collection) {
        Map<NumericStatistics, Double> statistics = StatisticsUtils.getNumericStatistics(collection);
        logArea.logInfo("Statistics : ", LogColor.BLUE);
        if (statistics.get(NumericStatistics.MEAN) != null) {
            logArea.logInfo(NumericStatistics.MEAN.getName() + " : " + statistics.get(NumericStatistics.MEAN), LogColor.BLUE);
        }
        if (statistics.get(NumericStatistics.MEDIAN) != null) {
            logArea.logInfo(NumericStatistics.MEDIAN.getName() + " : " + statistics.get(NumericStatistics.MEDIAN), LogColor.BLUE);
        }
        if (statistics.get(NumericStatistics.VARIANCE) != null) {
            logArea.logInfo(NumericStatistics.VARIANCE.getName() + " : " + statistics.get(NumericStatistics.VARIANCE), LogColor.BLUE);
        }
        if (statistics.get(NumericStatistics.STANDARD_DEVIATION) != null) {
            logArea.logInfo(NumericStatistics.STANDARD_DEVIATION.getName() + " : " + statistics.get(NumericStatistics.STANDARD_DEVIATION), LogColor.BLUE);
        }
        if (statistics.get(NumericStatistics.SKEWNESS_COEFFICIENT) != null) {
            String message = statistics.get(NumericStatistics.SKEWNESS_COEFFICIENT) > 0 ? " ( Distribution is right skewed ) " : " ( Distribution is left skewed ) ";
            logArea.logInfo(NumericStatistics.SKEWNESS_COEFFICIENT.getName() + " : " + statistics.get(NumericStatistics.SKEWNESS_COEFFICIENT) + message, LogColor.BLUE);
        }
        if (statistics.get(NumericStatistics.PEARSONS_COEFFICIENT) != null) {
            String message = statistics.get(NumericStatistics.PEARSONS_COEFFICIENT) > 0 ? " ( Distribution is right skewed ) " : " ( Distribution is left skewed ) ";
            logArea.logInfo(NumericStatistics.PEARSONS_COEFFICIENT.getName() + " : " + statistics.get(NumericStatistics.PEARSONS_COEFFICIENT) + message, LogColor.BLUE);
        }
    }

    @SuppressWarnings("unchecked")
    private void displaySplineChart(AvailableStatistics statistic) {
        Map<Object, Number> newData = new LinkedHashMap<>();
        Map<String, Double> receivedResult = (Map<String, Double>) result.getData();
        receivedResult.keySet().stream().sorted((date1, date2) -> date1.compareTo(date2)).collect(Collectors.toList()).forEach(date -> {
            newData.put(date.toString(), receivedResult.get(date));
        });
        switch (options.getEvolutionRepresentation()) {
            case LINE:
                chartPanel.setChart(LineChartFactory.getLineChart(statistic.toString(), "Time", "Evolution", new PlottableMap(getStatisticDescription(), newData)));
                break;
            case SPLINE:
                chartPanel.setChart(SplineChartFactory.getSplineChart(statistic.toString(), "Time", "Evolution", new PlottableMap(getStatisticDescription(), newData)));
                break;
        }

    }

    @SuppressWarnings("unchecked")
    private void displayBoxChart(AvailableStatistics statistic) {
        chartPanel.setChart(BoxChartFactory.getBoxChart(statistic.toString(), new PlottableList(getStatisticDescription(), (List<Double>) result.getData())));
    }

    private String getStatisticDescription() {
        StringBuilder builder = new StringBuilder();

        User user = (User) filter.getComboBoxValue("user");
        Project project = (Project) filter.getComboBoxValue("project");
        UserGroup group = (UserGroup) filter.getComboBoxValue("group");

        if (user != null)
            builder.append(user.getUsername() + " ");
        if (project != null)
            builder.append(project.getProjectName() + " ");
        if (group != null)
            builder.append(group.getUserGroupName() + " ");

        return builder.toString();
    }

    private int getBinSize() {
        try {
            return Integer.parseInt((String) filter.getTextFieldValue("bin"));
        } catch (Exception e) {
            return 1;
        }
    }

    private Long getGroupId(UserGroup group) {
        return group != null ? group.getUserGroupId() : null;
    }

    private Long getUserId(User user) {
        Long userId = null;
        userId = (user != null ? user.getUserId() : null);
        if (userId == null && !security.hasAuthority("ROLE_ADMIN")) {
            userId = (Long) session.getAttribute("user_id");
        }
        return userId;
    }

    private Long getProjectId(Project project) {
        return project != null ? project.getProjectId() : null;
    }

}
