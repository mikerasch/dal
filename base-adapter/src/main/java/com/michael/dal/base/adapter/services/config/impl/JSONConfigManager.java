package com.michael.dal.base.adapter.services.config.impl;

import com.google.inject.Singleton;
import com.michael.dal.services.auth.models.AuthResult;
import com.michael.dal.services.config.ConfigManager;
import com.michael.dal.services.config.models.Config;
import com.michael.dal.services.config.models.SecurityServiceConnectionConfig;
import com.michael.dal.utils.ObjectMapperProvider;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO BREAK THIS CLASS INTO 2 CLASSES. ONE FOR IO OPERATIONS AND ANOTHER FOR CONFIG OPERATIONS
@Singleton
public class JSONConfigManager implements ConfigManager {
  private static final String DEFAULT_CONFIG_FILE_PATH =
      System.getProperty("user.home") + "\\dal-config.json";
  private static final Logger logger = LoggerFactory.getLogger(JSONConfigManager.class);
  private Config config = null;

  /**
   * Check to see if the configuration file exists. This is used to determine if the application has
   * been configured already.
   */
  @Override
  public boolean configExists() {
    Path path = Path.of(DEFAULT_CONFIG_FILE_PATH);
    return path.toFile().exists();
  }

  /**
   * Upsert the configuration file. If the configuration file already exists, update the existing
   * configuration. If the configuration file does not exist, create a new configuration file.
   *
   * @return true if the configuration was successfully upserted, false otherwise
   */
  @Override
  public boolean upsertConfig(final Config configToUpsert) {
    try {
      if (configExists()) {
        updateConfig(configToUpsert);
      } else {
        createConfig(configToUpsert);
      }
    } catch (IOException e) {
      logger.error("Failed to save configuration to path {}.", DEFAULT_CONFIG_FILE_PATH, e);
      return false;
    }
    resetConfig();
    logger.info("Configuration saved successfully to path {}.", DEFAULT_CONFIG_FILE_PATH);
    return true;
  }

  @Override
  public Config readConfig() {
    if (config != null) {
      return config;
    }
    Path path = Path.of(DEFAULT_CONFIG_FILE_PATH);
    try {
      config = ObjectMapperProvider.getInstance().readValue(Files.readAllBytes(path), Config.class);
    } catch (IOException e) {
      logger.error("Failed to read configuration from path {}.", DEFAULT_CONFIG_FILE_PATH, e);
    }
    return config;
  }

  @Override
  public void updateAuthResults(final List<AuthResult> authResults) {
    Config readConfig = readConfig();
    List<SecurityServiceConnectionConfig> updatedSecurityServiceConnection =
        readConfig.securityServiceConnectionConfig().stream()
            .map(
                securityConfig -> mapToSecurityServiceConnectionConfig(securityConfig, authResults))
            .toList();
    Config updatedConfig =
        new Config(
            updatedSecurityServiceConnection, readConfig.discoveryServiceConnectionConfigs());
    upsertConfig(updatedConfig);
  }

  private void resetConfig() {
    config = null;
  }

  private void createConfig(final Config configToCreate) throws IOException {
    Path path = Path.of(DEFAULT_CONFIG_FILE_PATH);
    Files.createFile(path);

    Files.write(
        path,
        ObjectMapperProvider.getInstance().writeValueAsBytes(configToCreate),
        StandardOpenOption.WRITE);
  }

  private void updateConfig(final Config configToUpdate) throws IOException {
    Path path = Path.of(DEFAULT_CONFIG_FILE_PATH);

    Files.write(
        path,
        ObjectMapperProvider.getInstance().writeValueAsBytes(configToUpdate),
        StandardOpenOption.TRUNCATE_EXISTING);
  }

  private SecurityServiceConnectionConfig mapToSecurityServiceConnectionConfig(
      final SecurityServiceConnectionConfig config, final List<AuthResult> authResults) {
    AuthResult authResult = findAuthResultForEnvironment(config.environment(), authResults);
    return new SecurityServiceConnectionConfig(
        config.environment(), config.url(), authResult != null ? authResult.token() : null);
  }

  private AuthResult findAuthResultForEnvironment(
      final String environment, final List<AuthResult> authResults) {
    return authResults.stream()
        .filter(result -> result.environment().equals(environment))
        .findFirst()
        .orElse(null);
  }
}
