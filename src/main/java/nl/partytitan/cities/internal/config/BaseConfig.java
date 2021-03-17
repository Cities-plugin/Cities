package nl.partytitan.cities.internal.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Superclass for config classes
 */
public class BaseConfig extends YamlConfiguration {
    protected static final Logger LOGGER = Logger.getLogger("Cities");

    private final AtomicInteger pendingDiskWrites = new AtomicInteger(0);
    protected String templateName = null;

    protected final File configFile;

    private Class<?> resourceClass = BaseConfig.class;

    public BaseConfig(final File configFile) {
        super();
        this.configFile = configFile.getAbsoluteFile();
    }

    public synchronized void load() {
        if (pendingDiskWrites.get() != 0) {
            LOGGER.log(Level.INFO, "File {0} not read, because it''s not yet written to disk.", configFile);
            return;
        }
        if (!configFile.getParentFile().exists()) {
            if (!configFile.getParentFile().mkdirs()) {
                LOGGER.log(Level.SEVERE, "Failed to crate config file", configFile.toString());
            }
        }

        if (!configFile.exists()) {
            if (templateName != null) {
                LOGGER.log(Level.INFO, "Using template: " + templateName + " to create: " + configFile.toString());
                createFromTemplate();
            } else {
                return;
            }
        }

        try {
            super.load(configFile);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        } catch (InvalidConfigurationException e) {
            final File broken = new File(configFile.getAbsolutePath() + ".broken." + System.currentTimeMillis());
            configFile.renameTo(broken);
            LOGGER.log(Level.SEVERE, "The file " + configFile.toString() + " is broken, it has been renamed to " + broken.toString(), e.getCause());
        }

    }

    private void createFromTemplate() {
        InputStream istr = null;
        OutputStream ostr = null;
        try {
            istr = resourceClass.getResourceAsStream(templateName);
            if (istr == null) {
                LOGGER.log(Level.SEVERE, "Could not find template: " + templateName);
                return;
            }
            ostr = new FileOutputStream(configFile);
            final byte[] buffer = new byte[1024];
            int length = 0;
            length = istr.read(buffer);
            while (length > 0) {
                ostr.write(buffer, 0, length);
                length = istr.read(buffer);
            }
        } catch (final IOException ex) {
            LOGGER.log(Level.SEVERE, "Could not write config: " + configFile.toString(), ex);
        } finally {
            try {
                if (istr != null) {
                    istr.close();
                }
            } catch (final IOException ex) {
                Logger.getLogger(BaseConfig.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (ostr != null) {
                    ostr.close();
                }
            } catch (final IOException ex) {
                LOGGER.log(Level.SEVERE, "Could not close config: " + configFile.toString(), ex);
            }
        }
    }

    public void setTemplateName(final String templateName) {
        this.templateName = templateName;
    }
}
