package utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Settings {
    private Map<String, String> properties = new HashMap<>();
    private static Settings instance;

    public static Settings getInstance() {
        if (instance == null) {
            Properties settings = new Properties();
            try {
                settings.load(new FileReader("C:\\Users\\rauld\\Documents\\GitHub\\InchiriereMasini\\JavaFX\\settings.properties"));
            } catch (IOException e) {
                throw new RuntimeException("Error loading settings", e);
            }

            instance = new Settings();
            for (String name : settings.stringPropertyNames()) {
                instance.properties.put(name, settings.getProperty(name));
            }
        }
        return instance;
    }

    public String getProperty(String key) {
        return properties.get(key);
    }
}
