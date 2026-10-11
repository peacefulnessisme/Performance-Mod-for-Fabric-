package com.example.perfboost;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Path;
import java.util.Properties;

public class PerfBoost implements ClientModInitializer {
    public static boolean enabled = false;
    private static final Path CONFIG = FabricLoader.getInstance().getConfigDir().resolve("perfboost.properties");

    @Override
    public void onInitializeClient() {
        Properties p = new Properties();
        try (InputStream in = new FileInputStream(CONFIG.toFile())) {
            p.load(in);
            enabled = Boolean.parseBoolean(p.getProperty("enabled", "false"));
        } catch (IOException ignored) {}
    }

    public static void setEnabled(boolean on) {
        enabled = on;
        Properties p = new Properties();
        p.setProperty("enabled", String.valueOf(on));
        try (OutputStream out = new FileOutputStream(CONFIG.toFile())) {
            p.store(out, "PerfBoost");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
