package com.example.examplemod.livecode;

import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class LiveCodeStorage {
    private LiveCodeStorage() {
    }

    public static Path scriptPath() {
        return FMLPaths.CONFIGDIR.get().resolve("examplemod-livecode.txt");
    }

    public static String readScriptOrNull() {
        Path p = scriptPath();
        if (!Files.exists(p)) {
            return null;
        }
        try {
            return Files.readString(p, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return null;
        }
    }

    public static boolean writeScript(String source) {
        Path p = scriptPath();
        try {
            Files.createDirectories(p.getParent());
            Files.writeString(p, source == null ? "" : source, StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean exportTo(Path path, String source) {
        if (path == null) {
            return false;
        }
        try {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.writeString(path, source == null ? "" : source, StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
