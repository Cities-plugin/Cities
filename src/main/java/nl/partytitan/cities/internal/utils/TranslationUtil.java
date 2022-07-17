package nl.partytitan.cities.internal.utils;

import nl.partytitan.cities.internal.config.enums.Languages;
import nl.partytitan.cities.internal.utils.server.FileUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class TranslationUtil {

    private static YamlConfiguration translation;
    private static File languageFile;
    private static Languages currentLanguage;
    private static String currentLanguageTemplate;

    public static void loadLanguage(File dataFolder, Languages language) {
        LoggingUtil.sendMsg("Loading language: " + language.name());

        currentLanguage = language;

        String resourceName = language.name() + ".yml";
        currentLanguageTemplate = "/" + resourceName;

        languageFile = new File(dataFolder, resourceName);

        loadTranslation();
    }

    private static void brokenTranslation(){
        final File broken = new File(languageFile.getAbsolutePath() + ".broken." + System.currentTimeMillis());
        languageFile.renameTo(broken);
        LoggingUtil.sendErrorMsg("The file " + languageFile.toString() + " is broken, it has been renamed to " + broken.toString());
    }

    private static void loadTranslation(){
        if (!languageFile.exists() || !languageFile.isFile()) {
            FileUtils.copyResourceToFile(languageFile, currentLanguageTemplate);
        }

        try {
            translation = new YamlConfiguration();
            translation.load(languageFile);
        } catch (IOException e) {
            LoggingUtil.sendErrorMsg(e.getMessage(), e);
        } catch (InvalidConfigurationException e) {
            brokenTranslation();
        }
    }

    public static String of(String key) {
        String translatedString = translation.getString(key.toLowerCase());
        if(translatedString == null){
            brokenTranslation();
            loadTranslation();
            // retry
            translatedString = translation.getString(key.toLowerCase());
        }
        return ChatColor.translateAlternateColorCodes('&', translatedString);
    }

    public static String of(String key, Object... args) {
        return String.format(of(key), args);
    }
}
