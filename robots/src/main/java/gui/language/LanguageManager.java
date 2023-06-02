package gui.language;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class LanguageManager {
    private static final HashMap<AppLanguage, ResourceBundle> allBundles = new HashMap<>();
    private AppLanguage currentLanguage;
    public LanguageManager(String systemLanguage){
        for(AppLanguage lang: AppLanguage.values()){
            ResourceBundle bundle = ResourceBundle.getBundle("language", Locale.of(lang.locale));
            allBundles.put(lang, bundle);
        }
        try {
            currentLanguage = AppLanguage.valueOf(systemLanguage.toUpperCase());
        }
        catch (IllegalArgumentException e){
            currentLanguage = AppLanguage.EN;
        }
    }

    public String getLocaleValue(String name){
        try{
            return new String(allBundles.get(currentLanguage).getString(name).getBytes(StandardCharsets.UTF_8));
        }
        catch (NullPointerException e){
            return "ERROR";
        }
    }

    public void changeLanguage(AppLanguage language){
        currentLanguage = language;
    }

    public AppLanguage getCurrentLanguage(){
        return currentLanguage;
    }
}
