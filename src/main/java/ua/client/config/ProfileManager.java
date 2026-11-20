package ua.client.config;

import java.util.LinkedHashMap;
import java.util.Map;
import ua.client.module.Module;
import ua.client.module.ModuleManager;

public class ProfileManager {
    private static final Map<String, ModuleProfile> profiles = new LinkedHashMap<>();
    private static String activeProfile = "Default";

    static {
        saveProfile("Default");
    }

    public static void saveProfile(String profileName) {
        ModuleProfile p = new ModuleProfile(profileName);
        for (Module m : ModuleManager.modules) {
            p.setState(m.name, m.enabled);
        }
        profiles.put(profileName, p);
        activeProfile = profileName;
    }

    public static boolean loadProfile(String profileName) {
        ModuleProfile p = profiles.get(profileName);
        if (p == null) return false;
        for (Module m : ModuleManager.modules) {
            m.setEnabled(p.isEnabled(m.name, m.enabled));
        }
        activeProfile = profileName;
        return true;
    }

    public static String getActiveProfile() {
        return activeProfile;
    }
}
