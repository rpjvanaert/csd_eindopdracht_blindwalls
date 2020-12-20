package logo.philist.csd_blindwalls_location_aware.Models;

import java.util.Locale;

public class Language {

    public static final int EN = 0;
    public static final int NL = 1;

    public static int getSystemLanguage(){
        int language = Language.EN; // if language isn't included, use english;
        switch (Locale.getDefault().getLanguage()){
            case "en":
                language = Language.EN;
                break;
            case "nl":
                language = Language.NL;
        }

        return language;
    }
}
