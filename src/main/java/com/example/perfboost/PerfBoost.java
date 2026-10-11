package com.example.perfboost;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.GraphicsMode;
import net.minecraft.client.option.ParticlesMode;

import java.io.*;
import java.nio.file.Path;
import java.util.Properties;

public class PerfBoost implements ClientModInitializer {
    public static boolean enabled = false;
    private static final Path CONFIG = FabricLoader.getInstance().getConfigDir().resolve("perfboost.properties");

    private static GraphicsMode oldGraphics;
    private static ParticlesMode oldParticles;
    private static boolean oldShadows, oldAo;
    private static double oldEntityDist;
    private static boolean hasSaved = false;

    @Override
    public void onInitializeClient() {
        Properties p = new Properties();
        try (InputStream in = new FileInputStream(CONFIG.toFile())) {
            p.load(in);
            enabled = Boolean.parseBoolean(p.getProperty("enabled", "false"));
        } catch (IOException ignored) {}
        if (enabled) apply(true);
    }

    public static void setEnabled(boolean on) {
        apply(on);
        enabled = on;
        Properties p = new Properties();
        p.setProperty("enabled", String.valueOf(on));
        try (OutputStream out = new FileOutputStream(CONFIG.toFile())) {
            p.store(out, "PerfBoost");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void apply(boolean on) {
        MinecraftClient mc = MinecraftClient.getInstance();
        GameOptions o = mc.options;

        if (on) {
            if (!hasSaved) {
                oldGraphics = o.getGraphicsMode().getValue();
                oldParticles = o.getParticles().getValue();
                oldShadows = o.getEntityShadows().getValue();
                oldAo = o.getAo().getValue();
                oldEntityDist = o.getEntityDistanceScaling().getValue();
                hasSaved = true;
            }
            o.getGraphicsMode().setValue(GraphicsMode.FAST);
            o.getParticles().setValue(ParticlesMode.MINIMAL);
            o.getEntityShadows().setValue(false);
            o.getAo().setValue(false);
            o.getEntityDistanceScaling().setValue(0.5);
        } else if (hasSaved) {
            o.getGraphicsMode().setValue(oldGraphics);
            o.getParticles().setValue(oldParticles);
            o.getEntityShadows().setValue(oldShadows);
            o.getAo().setValue(oldAo);
            o.getEntityDistanceScaling().setValue(oldEntityDist);
        }
        o.write();
        mc.worldRenderer.reload();
    }
}
