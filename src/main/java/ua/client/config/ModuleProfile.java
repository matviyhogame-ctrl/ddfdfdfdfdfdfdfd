package ua.client.config;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModuleProfile {
    public final String name;
    public final Map<String, Boolean> enabledStates = new LinkedHashMap<>();

    public ModuleProfile(String name) {
        this.name = name;
    }

    public void setState(String moduleName, boolean enabled) {
        enabledStates.put(moduleName, enabled);
    }

    public boolean isEnabled(String moduleName, boolean fallback) {
        return enabledStates.getOrDefault(moduleName, fallback);
    }
}
