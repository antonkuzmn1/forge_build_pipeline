package com.example.examplemod.livecode;

import net.minecraftforge.common.ForgeConfigSpec;

public final class LiveCodeConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.ConfigValue<String> EXPORT_PATH = BUILDER
            .define("exportPath", "");

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    private LiveCodeConfig() {
    }
}
