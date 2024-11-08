package com.michael.dal.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javafx.application.Platform;

public class SingleTaskExecutor {
  private final ExecutorService executorService;
  private Future<?> currentTask;

  public SingleTaskExecutor() {
    this.executorService = Executors.newSingleThreadExecutor();
  }

  public synchronized void submitTask(final Runnable task) {
    Platform.runLater(() -> submitTaskInternal(task));
  }

  private void submitTaskInternal(final Runnable task) {
    if (currentTask != null && !currentTask.isDone()) {
      currentTask.cancel(true); // Cancel the current task
    }
    currentTask = executorService.submit(task);
  }
}
