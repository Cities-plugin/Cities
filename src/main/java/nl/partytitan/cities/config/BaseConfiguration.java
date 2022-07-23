package nl.partytitan.cities.config;

import com.google.inject.Inject;
import nl.partytitan.cities.utils.FileUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class BaseConfiguration extends YamlConfiguration {
    @Inject
    private JavaPlugin javaPlugin;

    private final AtomicInteger pendingDiskWrites = new AtomicInteger(0);

    private File file;

    /**
     * This method must be called in all inheriting classes
     */
    public void init() {
        file = new File(filePath());

        if (pendingDiskWrites.get() != 0) {
            javaPlugin.getLogger().info(String.format("File {0} not read, because it''s not yet written to disk.", file));
            return;
        }
        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                javaPlugin.getLogger().severe("Failed to create config file: " + file.toString());
            }
        }

        if (!file.exists()) {
            if (templatePath() != null) {
                javaPlugin.getLogger().info("Using template: " + templatePath() + " to create: " + file.toString());
                createFromTemplate();
            } else {
                return;
            }
        }

        try {
            super.load(file);
        } catch (IOException e) {
            javaPlugin.getLogger().severe(e.getMessage());
        } catch (InvalidConfigurationException e) {
            final File broken = new File(file.getAbsolutePath() + ".broken." + System.currentTimeMillis());
            file.renameTo(broken);
            javaPlugin.getLogger().severe("The file " + file.toString() + " is broken, it has been renamed to " + broken.toString());
        }
    }

    private void createFromTemplate() {
        FileUtils.copyResourceToFile(file, templatePath());
    }

    protected abstract String filePath();
    protected abstract String templatePath();
}
