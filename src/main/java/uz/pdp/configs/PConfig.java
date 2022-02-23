package uz.pdp.configs;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;


public class PConfig {
    private static final PConfig instance = new PConfig();
    private static Properties queryProperties = new Properties();
    private static Properties appProperties = new Properties();

    public void load() {
        queryProperties = new Properties();
        appProperties = new Properties();

        try {
            queryProperties.load(new FileReader("src/main/resources/statics/query.properties"));
            appProperties.load(new FileReader("src/main/resources/statics/app.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getFromQueryProperties(String key) {
        return queryProperties.getProperty(key);
    }

    public String getFromAppProperties(String key) {
        return appProperties.getProperty(key);
    }

    public static PConfig getInstance() {
        return instance;
    }
}
