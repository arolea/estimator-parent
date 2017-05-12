package com.learning.estimator.persistenceservice;

//import static org.junit.Assert.assertEquals;
//
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Random;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Import;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//import com.learning.estimator.common.logger.ILogger;
//import com.learning.estimator.common.logger.LogManager;
//import com.learning.estimator.model.entities.Project;
//import com.learning.estimator.model.entities.User;
//import com.learning.estimator.model.entities.UserGroup;
//import com.learning.estimator.model.entities.UserRole;
//import com.learning.estimator.model.statistics.EstimateAccuracyFact;
//import com.learning.estimator.model.statistics.GroupDimension;
//import com.learning.estimator.model.statistics.LoggedTimeFact;
//import com.learning.estimator.model.statistics.ProjectDimension;
//import com.learning.estimator.model.statistics.TimeDimension;
//import com.learning.estimator.model.statistics.UserDimension;
//import com.learning.estimator.model.statistics.VelocityPointFact;
//import com.learning.estimator.persistence.config.client.CorePersistenceConfigClientSide;
//import com.learning.estimator.persistence.config.statistics.StatisticsPersistenceConfig;
//import com.learning.estimator.persistence.facade.client.PersistenceFacadeClientSide;
//import com.learning.estimator.persistence.facade.statistics.StatisticsFacade;
//import com.learning.estimator.persistencesd.config.JpaPersistenceConfig;
//
//@SpringBootApplication
//@Import(value={JpaPersistenceConfig.class,StatisticsPersistenceConfig.class})
//public class DatabasePopulator implements CommandLineRunner {
//	
//	@Autowired private PersistenceFacadeClientSide infrastructurePersistence;
//	@Autowired private StatisticsFacade facade;
//	
//	private BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();
//	private static final ILogger LOG = LogManager.getLogger(DatabasePopulator.class);
//	
//	public static void main(String[] args) {
//		SpringApplication.run(DatabasePopulator.class, args);
//	}
//
//	@Override
//	public void run(String... args) throws Exception {
//		addUser("admin","admin",UserRole.ADMIN);
//		addUser("user1","user1",UserRole.USER);
//		addUser("user2","user2",UserRole.USER);
//		
//		UserGroup group1 = infrastructurePersistence.saveUserGroup(new UserGroup("Group1"));
//		UserGroup group2 = infrastructurePersistence.saveUserGroup(new UserGroup("Group2"));
//		
//		infrastructurePersistence.saveProject(new Project(group1, "Project1", "Project1"));
//		infrastructurePersistence.saveProject(new Project(group2, "Project2", "Project2"));
//		
//		List<ProjectDimension> projects = new LinkedList<>();
//		projects.add(facade.saveProject(new ProjectDimension(new Long(1), "Project1")));
//		projects.add(facade.saveProject(new ProjectDimension(new Long(2), "Project2")));
//		
//		List<UserDimension> users = new LinkedList<>();
//		users.add(facade.saveUser(new UserDimension(new Long(1), "User1")));
//		users.add(facade.saveUser(new UserDimension(new Long(2), "User1")));
//		users.add(facade.saveUser(new UserDimension(new Long(3), "User1")));
//		
//		List<GroupDimension> groups = new LinkedList<>();
//		groups.add(facade.saveGroup(new GroupDimension(new Long(1), "Group1")));
//		groups.add( facade.saveGroup(new GroupDimension(new Long(2), "Group2")));
//		
//		List<TimeDimension> times = new LinkedList<>();
//		times.add(facade.saveTime(new TimeDimension(1, 1, 2000)));
//		times.add(facade.saveTime(new TimeDimension(1, 1, 2001)));
//		times.add(facade.saveTime(new TimeDimension(1, 1, 2002)));
//		times.add(facade.saveTime(new TimeDimension(1, 1, 2003)));
//		times.add(facade.saveTime(new TimeDimension(1, 1, 2004)));
//		times.add(facade.saveTime(new TimeDimension(1, 1, 2005)));
//		times.add(facade.saveTime(new TimeDimension(1, 1, 2006)));
//		times.add(facade.saveTime(new TimeDimension(1, 1, 2007)));
//		times.add(facade.saveTime(new TimeDimension(1, 1, 2008)));
//		times.add(facade.saveTime(new TimeDimension(1, 1, 2009)));
//		times.add(facade.saveTime(new TimeDimension(1, 1, 2010)));
//		times.add(facade.saveTime(new TimeDimension(1, 1, 2011)));
//		times.add(facade.saveTime(new TimeDimension(1, 1, 2012)));
//		times.add(facade.saveTime(new TimeDimension(1, 1, 2013)));
//		times.add(facade.saveTime(new TimeDimension(1, 1, 2014)));
//		times.add(facade.saveTime(new TimeDimension(1, 1, 2015)));
//		times.add(facade.saveTime(new TimeDimension(1, 1, 2016)));
//		
//		Random random = new Random();
//		 
//		for(int i = 0 ; i < 1000 ; i ++ ) {
//		System.out.println("Ineration " + i);
//		users.forEach(user->{
//			projects.forEach(project->{
//				groups.forEach(group->{
//					times.forEach(time->{
//						facade.saveEstimateAccuracy(new EstimateAccuracyFact(random.nextDouble()*100, user, project, group, time));
//						facade.saveLoggedTime(new LoggedTimeFact(random.nextDouble()*100, user, project, group, time));
//						facade.saveVelocityPoint(new VelocityPointFact(random.nextDouble()*100, user, project, group, time));
//					});
//				});
//			});
//		});
//		}
//	}
//	
//	private User addUser(String username, String password, UserRole... roles) {
//		User user = new User(username, bcryptEncoder.encode(password));
//		for (UserRole role : roles) {
//			user.addUserRole(role);
//		}
//		user = infrastructurePersistence.saveUser(user);
//		facade.saveUser(new UserDimension(user.getUserId(), user.getUsername()));
//		return user;
//	}
//
//}
