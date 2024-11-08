package com.michael.dal.base.adapter.services.terminal.impl;

import com.michael.dal.services.terminal.TerminalExecutor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CMDExecutor implements TerminalExecutor {
  private static final Logger logger = LoggerFactory.getLogger(CMDExecutor.class);

  @Override
  public String executeCurlCommand(final String command) {
    logger.trace("Executing the curl command: {}", command);
    String flattenedCommand = flattenCommand(command);
    StringBuilder result = new StringBuilder();
    try {
      buildAndExecuteProcess(flattenedCommand, result);
    } catch (IOException e) {
      logger.error("Problem executing curl command %s, defaulting to empty result", e);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      logger.error("Problem executing curl command %s, defaulting to empty result", e);
    }
    logger.info("Curl command executed {} with result {}", command, result);
    return result.toString();
  }

  private static void buildAndExecuteProcess(
      final String flattenedCommand, final StringBuilder result)
      throws IOException, InterruptedException {
    ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", flattenedCommand);
    Process process = processBuilder.start();
    try (BufferedReader reader =
        new BufferedReader(new InputStreamReader(process.getInputStream()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        result.append(line).append(System.lineSeparator());
      }
    }
    process.waitFor();
  }

  private static String flattenCommand(final String command) {
    // 1) Replace all spaces with a single space, this flattens the command
    // 2) Replace all double quotes with escaped double quotes
    // 3) Replace all single quotes with double quotes <- this must be after 2
    // 4) Trim the command
    return command.replaceAll("\\s+", " ").replace("\"", "\\\"").replace("'", "\"").trim();
  }
}
