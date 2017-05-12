package com.learning.estimator.persistencesd;

import com.learning.estimator.model.statistics.*;
import com.learning.estimator.persistence.config.statistics.StatisticsPersistenceConfig;
import com.learning.estimator.persistence.facade.statistics.StatisticsFacade;
import com.learning.estimator.persistencesd.config.JpaPersistenceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

/**
 * Statistics persistence tests
 *
 * @author rolea
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {JpaPersistenceConfig.class, StatisticsPersistenceConfig.class})
//refresh H2 after each test
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles(profiles = {"test"})
public class StatisticsPersistenceTest {

    @Autowired
    private StatisticsFacade facade;

    @Test
    public void testPersistence() {
        List<ProjectDimension> projects = new LinkedList<>();
        projects.add(facade.saveProject(new ProjectDimension(new Long(1), "Project1")));
        projects.add(facade.saveProject(new ProjectDimension(new Long(2), "Project2")));

        List<UserDimension> users = new LinkedList<>();
        users.add(facade.saveUser(new UserDimension(new Long(1), "User1")));
        users.add(facade.saveUser(new UserDimension(new Long(2), "User1")));
        users.add(facade.saveUser(new UserDimension(new Long(3), "User1")));

        List<GroupDimension> groups = new LinkedList<>();
        groups.add(facade.saveGroup(new GroupDimension(new Long(1), "Group1")));
        groups.add(facade.saveGroup(new GroupDimension(new Long(2), "Group1")));

        List<TimeDimension> times = new LinkedList<>();
        times.add(facade.saveTime(new TimeDimension(1, 1, 2000)));
        times.add(facade.saveTime(new TimeDimension(1, 1, 2010)));
        times.add(facade.saveTime(new TimeDimension(1, 1, 2011)));
        times.add(facade.saveTime(new TimeDimension(1, 1, 2012)));
        times.add(facade.saveTime(new TimeDimension(1, 1, 2015)));

        assertThat(facade.countProjects()).as("There are two projects dimensions into the database").isEqualTo(2);
        assertThat(facade.countUsers()).as("There are three user dimensions into the database").isEqualTo(3);
        assertThat(facade.countGroups()).as("There are two group dimensions into the database").isEqualTo(2);
        assertThat(facade.countTimes()).as("There are five time dimensions into the database").isEqualTo(5);

        users.forEach(user -> {
            projects.forEach(project -> {
                groups.forEach(group -> {
                    times.forEach(time -> {
                        facade.saveEstimateAccuracy(new EstimateAccuracyFact(1.2, user, project, group, time));
                        facade.saveLoggedTime(new LoggedTimeFact(1.5, user, project, group, time));
                        facade.saveVelocityPoint(new VelocityPointFact(2.5, user, project, group, time));
                    });
                });
            });
        });

        //each user has 20 tasks , 10 in each project , 5 in each group and 1 in each time

        assertThat(facade.countEstimateAccuracies()).as("There are 60 estimate accuracy facts into the database").isEqualTo(60);
        assertThat(facade.countLoggedTimes()).as("There are 60 logged time facts into the database").isEqualTo(60);
        assertThat(facade.countVelocityPoints()).as("There are 60 velocity point facts into the database").isEqualTo(60);

        EstimateAccuracyFact estimateAccuracy = facade.findEstimateAccuracy(new Long(1));
        VelocityPointFact velocityPoint = facade.findVelocityPoint(new Long(1));
        LoggedTimeFact loggedTime = facade.findLoggedTime(new Long(1));

        //ensure lazy loading
        try {
            System.out.println(estimateAccuracy.getGroup());
            fail();
        } catch (Exception e) {
            System.out.println("Lazy load works for group");
        }
        try {
            System.out.println(estimateAccuracy.getUser());
            fail();
        } catch (Exception e) {
            System.out.println("Lazy load works for user");
        }
        try {
            System.out.println(estimateAccuracy.getProject());
            fail();
        } catch (Exception e) {
            System.out.println("Lazy load works for project");
        }
        try {
            System.out.println(estimateAccuracy.getTime());
            System.out.println("Eager load works for time");
        } catch (Exception e) {
            fail();
        }

        try {
            System.out.println(velocityPoint.getGroup());
            fail();
        } catch (Exception e) {
            System.out.println("Lazy load works for group");
        }
        try {
            System.out.println(velocityPoint.getUser());
            fail();
        } catch (Exception e) {
            System.out.println("Lazy load works for user");
        }
        try {
            System.out.println(velocityPoint.getProject());
            fail();
        } catch (Exception e) {
            System.out.println("Lazy load works for project");
        }
        try {
            System.out.println(velocityPoint.getTime());
            System.out.println("Eager load works for time");
        } catch (Exception e) {
            fail();
        }

        try {
            System.out.println(loggedTime.getGroup());
            fail();
        } catch (Exception e) {
            System.out.println("Lazy load works for group");
        }
        try {
            System.out.println(loggedTime.getUser());
            fail();
        } catch (Exception e) {
            System.out.println("Lazy load works for user");
        }
        try {
            System.out.println(loggedTime.getProject());
            fail();
        } catch (Exception e) {
            System.out.println("Lazy load works for project");
        }
        try {
            System.out.println(loggedTime.getTime());
            System.out.println("Eager load works for time");
        } catch (Exception e) {
            fail();
        }

        //test paging and lazy load without criteria
        for (int i = 0; i < 6; i++) {
            List<EstimateAccuracyFact> estCurrentPage = facade.findEstimateAccuracies(i, 10);
            assertThat(estCurrentPage.size()).as("Page size is 10").isEqualTo(10);
            if (i == 3) {
                estCurrentPage.forEach(estAccuracy -> {
                    try {
                        System.out.println(estAccuracy.getGroup());
                        fail();
                    } catch (Exception e) {
                        System.out.println("Lazy load works for group");
                    }
                    try {
                        System.out.println(estAccuracy.getUser());
                        fail();
                    } catch (Exception e) {
                        System.out.println("Lazy load works for user");
                    }
                    try {
                        System.out.println(estAccuracy.getProject());
                        fail();
                    } catch (Exception e) {
                        System.out.println("Lazy load works for project");
                    }
                    try {
                        System.out.println(estAccuracy.getTime());
                        System.out.println("Eager load works for time");
                    } catch (Exception e) {
                        fail();
                    }
                });
            }

            List<VelocityPointFact> velocityPointPage = facade.findVelocityPoints(i, 10);
            assertThat(velocityPointPage.size()).as("Page size is 10").isEqualTo(10);
            if (i == 3) {
                velocityPointPage.forEach(velPoint -> {
                    try {
                        System.out.println(velPoint.getGroup());
                        fail();
                    } catch (Exception e) {
                        System.out.println("Lazy load works for group");
                    }
                    try {
                        System.out.println(velPoint.getUser());
                        fail();
                    } catch (Exception e) {
                        System.out.println("Lazy load works for user");
                    }
                    try {
                        System.out.println(velPoint.getProject());
                        fail();
                    } catch (Exception e) {
                        System.out.println("Lazy load works for project");
                    }
                    try {
                        System.out.println(velPoint.getTime());
                        System.out.println("Eager load works for time");
                    } catch (Exception e) {
                        fail();
                    }
                });
            }

            List<LoggedTimeFact> logCurrentPage = facade.findLoggedTimes(i, 10);
            assertThat(logCurrentPage.size()).as("Page size is 10").isEqualTo(10);
            if (i == 3) {
                logCurrentPage.forEach(logTime -> {
                    try {
                        System.out.println(logTime.getGroup());
                        fail();
                    } catch (Exception e) {
                        System.out.println("Lazy load works for group");
                    }
                    try {
                        System.out.println(logTime.getUser());
                        fail();
                    } catch (Exception e) {
                        System.out.println("Lazy load works for user");
                    }
                    try {
                        System.out.println(logTime.getProject());
                        fail();
                    } catch (Exception e) {
                        System.out.println("Lazy load works for project");
                    }
                    try {
                        System.out.println(logTime.getTime());
                        System.out.println("Eager load works for time");
                    } catch (Exception e) {
                        fail();
                    }
                });
            }
        }

        List<VelocityPointFact> velocityPoints = facade.findAllVelocityPoinysByPredicate(new Long(1), new Long(1), new Long(1), LocalDate.of(2000, 1, 1), LocalDate.of(2011, 1, 1));
        assertThat(velocityPoints.size()).as("3 velocity points facts match the predicate").isEqualTo(3);
        velocityPoints.forEach(velPoint -> {
            try {
                System.out.println(velPoint.getGroup());
                fail();
            } catch (Exception e) {
                System.out.println("Lazy load works for group");
            }
            try {
                System.out.println(velPoint.getUser());
                fail();
            } catch (Exception e) {
                System.out.println("Lazy load works for user");
            }
            try {
                System.out.println(velPoint.getProject());
                fail();
            } catch (Exception e) {
                System.out.println("Lazy load works for project");
            }
            try {
                System.out.println(velPoint.getTime());
                System.out.println("Eager load works for time");
            } catch (Exception e) {
                fail();
            }
        });

        List<EstimateAccuracyFact> estimateAccuracies = facade.findAllEstimateAccuraciesByPredicate(new Long(1), new Long(1), new Long(1), LocalDate.of(2000, 1, 1), LocalDate.of(2011, 1, 1));
        assertThat(estimateAccuracies.size()).as("3 estimate accuracy facts match the predicate").isEqualTo(3);
        estimateAccuracies.forEach(estAccuracy -> {
            try {
                System.out.println(estAccuracy.getGroup());
                fail();
            } catch (Exception e) {
                System.out.println("Lazy load works for group");
            }
            try {
                System.out.println(estAccuracy.getUser());
                fail();
            } catch (Exception e) {
                System.out.println("Lazy load works for user");
            }
            try {
                System.out.println(estAccuracy.getProject());
                fail();
            } catch (Exception e) {
                System.out.println("Lazy load works for project");
            }
            try {
                System.out.println(estAccuracy.getTime());
                System.out.println("Eager load works for time");
            } catch (Exception e) {
                fail();
            }
        });

        List<LoggedTimeFact> loggedTimes = facade.findAllLoggedTimesByPredicate(new Long(1), new Long(1), new Long(1), LocalDate.of(2000, 1, 1), LocalDate.of(2011, 1, 1));
        assertThat(loggedTimes.size()).as("3 logged time facts match the predicate").isEqualTo(3);
        loggedTimes.forEach(logTime -> {
            try {
                System.out.println(logTime.getGroup());
                fail();
            } catch (Exception e) {
                System.out.println("Lazy load works for group");
            }
            try {
                System.out.println(logTime.getUser());
                fail();
            } catch (Exception e) {
                System.out.println("Lazy load works for user");
            }
            try {
                System.out.println(logTime.getProject());
                fail();
            } catch (Exception e) {
                System.out.println("Lazy load works for project");
            }
            try {
                System.out.println(logTime.getTime());
                System.out.println("Eager load works for time");
            } catch (Exception e) {
                fail();
            }
        });

        assertThat(facade.countVelocityPointsByPredicate(new Long(1), null, null, LocalDate.of(2000, 1, 1), LocalDate.of(2011, 1, 1)))
                .as("12 velocity points facts match the predicate").isEqualTo(12);
        assertThat(facade.countEstimateAccuraciesByPredicate(new Long(1), null, null, LocalDate.of(2000, 1, 1), LocalDate.of(2011, 1, 1)))
                .as("12 estimate accuracy facts match the predicate").isEqualTo(12);
        assertThat(facade.countLoggedTimesByPredicate(new Long(1), null, null, LocalDate.of(2000, 1, 1), LocalDate.of(2011, 1, 1)))
                .as("12 logged time facts match the predicate").isEqualTo(12);

        for (int i = 0; i < 2; i++) {
            velocityPoints = facade.findVelocityPointsByPredicate(new Long(1), null, null, LocalDate.of(2000, 1, 1), LocalDate.of(2011, 1, 1), i, 6);
            assertThat(velocityPoints.size()).as("Page size is 6").isEqualTo(6);
            velocityPoints.forEach(velPoint -> {
                try {
                    System.out.println(velPoint.getGroup());
                    fail();
                } catch (Exception e) {
                    System.out.println("Lazy load works for group");
                }
                try {
                    System.out.println(velPoint.getUser());
                    fail();
                } catch (Exception e) {
                    System.out.println("Lazy load works for user");
                }
                try {
                    System.out.println(velPoint.getProject());
                    fail();
                } catch (Exception e) {
                    System.out.println("Lazy load works for project");
                }
                try {
                    System.out.println(velPoint.getTime());
                    System.out.println("Eager load works for time");
                } catch (Exception e) {
                    fail();
                }
            });

            estimateAccuracies = facade.findEstimateAccuraciesByPredicate(new Long(1), null, null, LocalDate.of(2000, 1, 1), LocalDate.of(2011, 1, 1), i, 6);
            assertThat(estimateAccuracies.size()).as("Page size is 6").isEqualTo(6);
            estimateAccuracies.forEach(estAccuracy -> {
                try {
                    System.out.println(estAccuracy.getGroup());
                    fail();
                } catch (Exception e) {
                    System.out.println("Lazy load works for group");
                }
                try {
                    System.out.println(estAccuracy.getUser());
                    fail();
                } catch (Exception e) {
                    System.out.println("Lazy load works for user");
                }
                try {
                    System.out.println(estAccuracy.getProject());
                    fail();
                } catch (Exception e) {
                    System.out.println("Lazy load works for project");
                }
                try {
                    System.out.println(estAccuracy.getTime());
                    System.out.println("Eager load works for time");
                } catch (Exception e) {
                    fail();
                }
            });

            loggedTimes = facade.findLoggedTimesByPredicate(new Long(1), null, null, LocalDate.of(2000, 1, 1), LocalDate.of(2011, 1, 1), i, 6);
            assertThat(loggedTimes.size()).as("Page size is 6").isEqualTo(6);
            loggedTimes.forEach(logTime -> {
                try {
                    System.out.println(logTime.getGroup());
                    fail();
                } catch (Exception e) {
                    System.out.println("Lazy load works for group");
                }
                try {
                    System.out.println(logTime.getUser());
                    fail();
                } catch (Exception e) {
                    System.out.println("Lazy load works for user");
                }
                try {
                    System.out.println(logTime.getProject());
                    fail();
                } catch (Exception e) {
                    System.out.println("Lazy load works for project");
                }
                try {
                    System.out.println(logTime.getTime());
                    System.out.println("Eager load works for time");
                } catch (Exception e) {
                    fail();
                }
            });
        }

    }

}
