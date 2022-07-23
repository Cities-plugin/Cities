package nl.partytitan.cities.internal.services;

import com.google.inject.Inject;
import nl.partytitan.cities.config.configs.MainConfig;
import nl.partytitan.cities.dependencyinjection.annotations.PostConstruct;
import nl.partytitan.cities.internal.Constants;
import nl.partytitan.cities.internal.enums.Languages;
import nl.partytitan.cities.utils.FileUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class TranslationService {
    @Inject
    private MainConfig mainConfig;

    private YamlConfiguration translation;
    private File languageFile;
    private Languages currentLanguage;
    private String currentLanguageTemplate;

    @PostConstruct
    public void loadLanguage() {
        currentLanguage = mainConfig.getLanguage();

        String resourceName = currentLanguage.name() + ".yml";
        currentLanguageTemplate = "/languages/" + resourceName;

        languageFile = new File(Constants.DATA_FOLDER_PATH, resourceName);

        loadTranslation();
    }

    private void brokenTranslation(){
        final File broken = new File(languageFile.getAbsolutePath() + ".broken." + System.currentTimeMillis());
        languageFile.renameTo(broken);
     }

    private void loadTranslation(){
        if (!languageFile.exists() || !languageFile.isFile()) {
            FileUtils.copyResourceToFile(languageFile, currentLanguageTemplate);
        }

        try {
            translation = new YamlConfiguration();
            translation.load(languageFile);
        } catch (IOException e) {
        } catch (InvalidConfigurationException e) {
            brokenTranslation();
        }
    }

    public String of(String key) {
        String translatedString = translation.getString(key.toLowerCase());
        if(translatedString == null){
            brokenTranslation();
            loadTranslation();
            // retry
            translatedString = translation.getString(key.toLowerCase());
        }
        return ChatColor.translateAlternateColorCodes('&', translatedString);
    }

    public String of(String key, Object... args) {
        return String.format(of(key), args);
    }
}

