package com.learning.estimator.persistence.facade.statistics;

import com.learning.estimator.model.statistics.*;
import com.learning.estimator.persistence.service.statistics.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * Facade for statistics persistence related operations
 *
 * @author rolea
 */
@Component
public class StatisticsFacade implements IEstimateAccuracyService, ILoggedTimeService, IVelocityPointService, IGroupDimensionService, IProjectDimensionService, IUserDimensionService, ITimeDimensionService {

    //fact entities
    @Autowired
    private IEstimateAccuracyService estimateAccuracyService;
    @Autowired
    private ILoggedTimeService loggedTimeService;
    @Autowired
    private IVelocityPointService velocityPointService;

    //dimension entities
    @Autowired
    private IGroupDimensionService groupDimensionService;
    @Autowired
    private IProjectDimensionService projectDimensionService;
    @Autowired
    private IUserDimensionService userDimensionService;
    @Autowired
    private ITimeDimensionService timeDimensionService;

    /*
     * Time dimension methods
     */
    @Override
    public TimeDimension saveTime(TimeDimension timeDimension) {
        return timeDimensionService.saveTime(timeDimension);
    }

    @Override
    public TimeDimension findTime(Long id) {
        return timeDimensionService.findTime(id);
    }

    @Override
    public TimeDimension findTime(LocalDate date) {
        return timeDimensionService.findTime(date);
    }

    @Override
    public List<TimeDimension> findAllTimes() {
        return timeDimensionService.findAllTimes();
    }

    @Override
    public List<TimeDimension> findTimes(int pageIndex, int pageSize) {
        return timeDimensionService.findTimes(pageIndex, pageSize);
    }

    @Override
    public void deleteTime(Long id) {
        timeDimensionService.deleteTime(id);
    }

    @Override
    public void deleteAllTimes() {
        timeDimensionService.deleteAllTimes();
    }

    @Override
    public Long countTimes() {
        return timeDimensionService.countTimes();
    }

    /*
     * User dimension methods
     */
    @Override
    public UserDimension saveUser(UserDimension userDimension) {
        return userDimensionService.saveUser(userDimension);
    }

    @Override
    public UserDimension findUser(Long id) {
        return userDimensionService.findUser(id);
    }

    @Override
    public UserDimension findUserByUsername(String username) {
        return userDimensionService.findUserByUsername(username);
    }

    @Override
    public List<UserDimension> findAllUsers() {
        return userDimensionService.findAllUsers();
    }

    @Override
    public List<UserDimension> findUsers(int pageIndex, int pageSize) {
        return userDimensionService.findUsers(pageIndex, pageSize);
    }

    @Override
    public void deleteUser(Long id) {
        userDimensionService.deleteUser(id);
    }

    @Override
    public void deleteAllUsers() {
        userDimensionService.deleteAllUsers();
    }

    @Override
    public Long countUsers() {
        return userDimensionService.countUsers();
    }

    /*
     * Project dimension methods
     */
    @Override
    public ProjectDimension saveProject(ProjectDimension projectDimension) {
        return projectDimensionService.saveProject(projectDimension);
    }

    @Override
    public ProjectDimension findProject(Long id) {
        return projectDimensionService.findProject(id);
    }

    @Override
    public ProjectDimension findProjectByName(String projectName) {
        return projectDimensionService.findProjectByName(projectName);
    }

    @Override
    public List<ProjectDimension> findAllProjects() {
        return projectDimensionService.findAllProjects();
    }

    @Override
    public List<ProjectDimension> findProjects(int pageIndex, int pageSize) {
        return projectDimensionService.findProjects(pageIndex, pageSize);
    }

    @Override
    public void deleteProject(Long id) {
        projectDimensionService.deleteProject(id);
    }

    @Override
    public void deleteAllprojects() {
        projectDimensionService.deleteAllprojects();
    }

    @Override
    public Long countProjects() {
        return projectDimensionService.countProjects();
    }

    /*
     * Group dimension methods
     */
    @Override
    public GroupDimension saveGroup(GroupDimension groupDimension) {
        return groupDimensionService.saveGroup(groupDimension);
    }

    @Override
    public GroupDimension findGroup(Long id) {
        return groupDimensionService.findGroup(id);
    }

    @Override
    public GroupDimension findGroupByName(String groupName) {
        return groupDimensionService.findGroupByName(groupName);
    }

    @Override
    public List<GroupDimension> findAllGroups() {
        return groupDimensionService.findAllGroups();
    }

    @Override
    public List<GroupDimension> findGroups(int pageIndex, int pageSize) {
        return groupDimensionService.findGroups(pageIndex, pageSize);
    }

    @Override
    public void deleteGroup(Long id) {
        groupDimensionService.deleteGroup(id);
    }

    @Override
    public void deleteAllGroups() {
        groupDimensionService.deleteAllGroups();
    }

    @Override
    public Long countGroups() {
        return groupDimensionService.countGroups();
    }

    /*
     * Velocity fact methods
     */
    @Override
    public VelocityPointFact saveVelocityPoint(VelocityPointFact fact) {
        return velocityPointService.saveVelocityPoint(fact);
    }

    @Override
    public VelocityPointFact findVelocityPoint(Long id) {
        return velocityPointService.findVelocityPoint(id);
    }

    @Override
    public List<VelocityPointFact> findAllVelocityPoints() {
        return velocityPointService.findAllVelocityPoints();
    }

    @Override
    public List<VelocityPointFact> findAllVelocityPoinysByPredicate(Long userId, Long projectId, Long groupId, LocalDate beginTime, LocalDate endTime) {
        return velocityPointService.findAllVelocityPoinysByPredicate(userId, projectId, groupId, beginTime, endTime);
    }

    @Override
    public List<VelocityPointFact> findVelocityPoints(int pageIndex, int pageSize) {
        return velocityPointService.findVelocityPoints(pageIndex, pageSize);
    }

    @Override
    public List<VelocityPointFact> findVelocityPointsByPredicate(Long userId, Long projectId, Long groupId, LocalDate beginTime, LocalDate endTime, int pageIndex, int pageSize) {
        return velocityPointService.findVelocityPointsByPredicate(userId, projectId, groupId, beginTime, endTime, pageIndex, pageSize);
    }

    @Override
    public void deleteVelocityPoint(Long id) {
        velocityPointService.deleteVelocityPoint(id);
    }

    @Override
    public void deleteAllVelocityPoints() {
        velocityPointService.deleteAllVelocityPoints();
    }

    @Override
    public Long countVelocityPoints() {
        return velocityPointService.countVelocityPoints();
    }

    @Override
    public Long countVelocityPointsByPredicate(Long userId, Long projectId, Long groupId, LocalDate beginTime, LocalDate endTime) {
        return velocityPointService.countVelocityPointsByPredicate(userId, projectId, groupId, beginTime, endTime);
    }

    /*
     * Logged time fact methods
     */
    @Override
    public LoggedTimeFact saveLoggedTime(LoggedTimeFact fact) {
        return loggedTimeService.saveLoggedTime(fact);
    }

    @Override
    public LoggedTimeFact findLoggedTime(Long id) {
        return loggedTimeService.findLoggedTime(id);
    }

    @Override
    public List<LoggedTimeFact> findAllLoggedTimes() {
        return loggedTimeService.findAllLoggedTimes();
    }

    @Override
    public List<LoggedTimeFact> findAllLoggedTimesByPredicate(Long userId, Long projectId, Long groupId, LocalDate beginTime, LocalDate endTime) {
        return loggedTimeService.findAllLoggedTimesByPredicate(userId, projectId, groupId, beginTime, endTime);
    }

    @Override
    public List<LoggedTimeFact> findLoggedTimes(int pageIndex, int pageSize) {
        return loggedTimeService.findLoggedTimes(pageIndex, pageSize);
    }

    @Override
    public List<LoggedTimeFact> findLoggedTimesByPredicate(Long userId, Long projectId, Long groupId, LocalDate beginTime, LocalDate endTime, int pageIndex, int pageSize) {
        return loggedTimeService.findLoggedTimesByPredicate(userId, projectId, groupId, beginTime, endTime, pageIndex, pageSize);
    }

    @Override
    public void deleteLoggedTime(Long id) {
        loggedTimeService.deleteLoggedTime(id);
    }

    @Override
    public void deleteAllLoggedTimes() {
        loggedTimeService.deleteAllLoggedTimes();
    }

    @Override
    public Long countLoggedTimes() {
        return loggedTimeService.countLoggedTimes();
    }

    @Override
    public Long countLoggedTimesByPredicate(Long userId, Long projectId, Long groupId, LocalDate beginTime, LocalDate endTime) {
        return loggedTimeService.countLoggedTimesByPredicate(userId, projectId, groupId, beginTime, endTime);
    }

    /*
     * Estimate accuracy fact methods
     */
    @Override
    public EstimateAccuracyFact saveEstimateAccuracy(EstimateAccuracyFact fact) {
        return estimateAccuracyService.saveEstimateAccuracy(fact);
    }

    @Override
    public EstimateAccuracyFact findEstimateAccuracy(Long id) {
        return estimateAccuracyService.findEstimateAccuracy(id);
    }

    @Override
    public List<EstimateAccuracyFact> findAllEstimateAccuracies() {
        return estimateAccuracyService.findAllEstimateAccuracies();
    }

    @Override
    public List<EstimateAccuracyFact> findAllEstimateAccuraciesByPredicate(Long userId, Long projectId, Long groupId, LocalDate beginTime, LocalDate endTime) {
        return estimateAccuracyService.findAllEstimateAccuraciesByPredicate(userId, projectId, groupId, beginTime, endTime);
    }

    @Override
    public List<EstimateAccuracyFact> findEstimateAccuracies(int pageIndex, int pageSize) {
        return estimateAccuracyService.findEstimateAccuracies(pageIndex, pageSize);
    }

    @Override
    public List<EstimateAccuracyFact> findEstimateAccuraciesByPredicate(Long userId, Long projectId, Long groupId, LocalDate beginTime, LocalDate endTime, int pageIndex, int pageSize) {
        return estimateAccuracyService.findEstimateAccuraciesByPredicate(userId, projectId, groupId, beginTime, endTime, pageIndex, pageSize);
    }

    @Override
    public void deleteEstimateAccuracy(Long id) {
        estimateAccuracyService.deleteEstimateAccuracy(id);
    }

    @Override
    public void deleteAllEstimateAccuracies() {
        estimateAccuracyService.deleteAllEstimateAccuracies();
    }

    @Override
    public Long countEstimateAccuracies() {
        return estimateAccuracyService.countEstimateAccuracies();
    }

    @Override
    public Long countEstimateAccuraciesByPredicate(Long userId, Long projectId, Long groupId, LocalDate beginTime, LocalDate endTime) {
        return estimateAccuracyService.countEstimateAccuraciesByPredicate(userId, projectId, groupId, beginTime, endTime);
    }

}
