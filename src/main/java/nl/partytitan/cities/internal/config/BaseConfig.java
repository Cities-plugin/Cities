package nl.partytitan.cities.internal.config;

import nl.partytitan.cities.internal.utils.server.FileUtils;
import nl.partytitan.cities.internal.utils.LoggingUtil;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Superclass for config classes
 */
public class BaseConfig extends YamlConfiguration {
    private final AtomicInteger pendingDiskWrites = new AtomicInteger(0);
    protected String templateName = null;

    protected final File configFile;

    public BaseConfig(final File configFile) {
        super();
        this.configFile = configFile.getAbsoluteFile();
    }

    public synchronized void load() {
        if (pendingDiskWrites.get() != 0) {
            LoggingUtil.sendMsg("File {0} not read, because it''s not yet written to disk.", configFile);
            return;
        }
        if (!configFile.getParentFile().exists()) {
            if (!configFile.getParentFile().mkdirs()) {
                LoggingUtil.sendErrorMsg("Failed to crate config file", configFile.toString());
            }
        }

        if (!configFile.exists()) {
            if (templateName != null) {
                LoggingUtil.sendMsg("Using template: " + templateName + " to create: " + configFile.toString());
                createFromTemplate();
            } else {
                return;
            }
        }

        try {
            super.load(configFile);
        } catch (IOException e) {
            LoggingUtil.sendErrorMsg(e.getMessage(), e);
        } catch (InvalidConfigurationException e) {
            final File broken = new File(configFile.getAbsolutePath() + ".broken." + System.currentTimeMillis());
            configFile.renameTo(broken);
            LoggingUtil.sendErrorMsg("The file " + configFile.toString() + " is broken, it has been renamed to " + broken.toString(), e.getCause());
        }

    }

    private void createFromTemplate() {
        FileUtils.copyResourceToFile(configFile, templateName);
    }

    public void setTemplateName(final String templateName) {
        this.templateName = templateName;
    }
}
