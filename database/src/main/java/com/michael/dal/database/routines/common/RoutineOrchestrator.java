package com.michael.dal.database.routines.common;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class RoutineOrchestrator {
  private final Set<Routine> routines;
  private final ScheduledExecutorService executorService;
  private static final Logger logger = LoggerFactory.getLogger(RoutineOrchestrator.class);

  @Inject
  public RoutineOrchestrator(final Set<Routine> routines) {
    this.routines = routines;
    this.executorService = new ScheduledThreadPoolExecutor(1);
  }

  public void start() {
    for (Routine routine : routines) {
      routine.onStartup();
      TimeInterval timeInterval = routine.getInterval();
      executorService.schedule(
          () -> {
            logger.info("Running routine: {}", routine.getRoutineName());
            routine.run();
          },
          timeInterval.interval(),
          timeInterval.timeUnit());
    }
  }
}
