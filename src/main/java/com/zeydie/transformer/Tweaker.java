package com.zeydie.transformer;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class Tweaker implements ITweaker {
    @Override
    public void acceptOptions(
            @NotNull final List<String> args,
            @NotNull final File gameDir,
            @NotNull final File assetsDir,
            @NotNull final String profile
    ) {
    }

    @Override
    public void injectIntoClassLoader(@NotNull final LaunchClassLoader classLoader) {
        classLoader.registerTransformer(Transformer.class.getName());
    }

    @NotNull
    @Override
    public String getLaunchTarget() {
        return "net.minecraft.client.main.Main";
    }

    @NotNull
    @Override
    public String[] getLaunchArguments() {
        return new String[0];
    }
}
