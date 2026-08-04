package io.github.technicfan.letterboxed;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;

public class Letterboxed implements ClientModInitializer {
    public static final String MOD_ID = "letterboxed";
    private static long handle;
    private static final File config = FabricLoader.getInstance().getConfigDir().resolve("letterboxed").toFile();
    public static int guiScale, height, width, guiScaledHeightOff, guiScaledWidthOff, guiScaledWidth, guiScaledHeight, fakeWidth, fakeHeight, fakeGuiWidth, fakeGuiHeight;
    public static double heightOff, widthOff, actualRatio, targetRatio;

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(
            ClientCommandManager.literal("letterboxed")
                .then(ClientCommandManager.literal("set")
                    .then(ClientCommandManager.argument("ratio", DoubleArgumentType.doubleArg())
                        .executes(Letterboxed::setLiteral))
                    .then(ClientCommandManager.argument("width", IntegerArgumentType.integer())
                        .then(ClientCommandManager.argument("height", IntegerArgumentType.integer())
                            .executes(Letterboxed::set))))
                .then(ClientCommandManager.literal("reset")
                    .executes(Letterboxed::reset))
        ));
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            handle = client.getWindow().handle();
        });
        targetRatio = load();
    }

    public static int leftOff() {
        return guiScaledWidthOff % 2 == 0 ? guiScaledWidthOff / 2 : guiScaledWidthOff / 2 + 1;
    }

    public static int rightOff() {
        return guiScaledWidthOff / 2;
    }

    public static int topOff() {
        return guiScaledHeightOff % 2 == 0 ? guiScaledHeightOff / 2 : guiScaledHeightOff / 2 + 1;
    }

    public static int bottomOff() {
        return guiScaledHeightOff / 2;
    }

    private static double load() {
        if (config.exists()) {
            try (Scanner scanner = new Scanner(config)) {
                if (scanner.hasNext()) {
                    double ratio = Double.parseDouble(scanner.next());
                    if (ratio >= 0) return ratio;
                }
            } catch (Exception e) {}
        }
        return 0;
    }

    private static void store() {
        try {
            if (!config.exists()) config.createNewFile();
            Writer writer = new FileWriter(config);
            writer.write(Double.toString(targetRatio));
            writer.flush();
            writer.close();
        } catch (IOException e) {}
    }

    private static int set(CommandContext<FabricClientCommandSource> commandContext) {
        int width = IntegerArgumentType.getInteger(commandContext, "width");
        int height = IntegerArgumentType.getInteger(commandContext, "height");
        if (height != 0) {
            double newRatio = (double) width / height;
            if (newRatio >= 0) {
                targetRatio = newRatio;
                refresh();
                store();
            }
        }
        return 1;
    }

    private static int setLiteral(CommandContext<FabricClientCommandSource> commandContext) {
        double newRatio = DoubleArgumentType.getDouble(commandContext, "ratio");
        if (newRatio >= 0) {
            targetRatio = newRatio;
            refresh();
            store();
        }
        return 1;
    }

    private static int reset(CommandContext<FabricClientCommandSource> commandContext) {
        targetRatio = 0;
        refresh();
        store();
        return 1;
    }

    public static void refresh() {
        fixRatio(handle);
        applyScale(handle);
    }

    public static void fixRatio(long handle) {
        if (handle != Letterboxed.handle) return;
        actualRatio = (float) width / height;
        fakeWidth = width;
        fakeHeight = height;
        widthOff = 0;
        heightOff = 0;
        if (targetRatio == 0 || targetRatio == actualRatio) return;
        if (targetRatio > actualRatio) {
            fakeHeight = (int) (width / targetRatio);
            heightOff = (height - fakeHeight) / 2f;
        } else {
            fakeWidth = (int) (height * targetRatio);
            widthOff = (width - fakeWidth) / 2f;
        }
    }

    public static void applyScale(long handle) {
        if (handle != Letterboxed.handle) return;
        double scale = (double) guiScale;
        int intWidth = (int)((double) fakeWidth / scale);
        fakeGuiWidth = (double) fakeWidth / scale > (double) intWidth ? intWidth + 1 : intWidth;
        int intHeight = (int)((double) fakeHeight / scale);
        fakeGuiHeight = (double) fakeHeight / scale > (double) intHeight ? intHeight + 1 : intHeight;
        guiScaledHeightOff = guiScaledHeight - fakeGuiHeight;
        guiScaledWidthOff = guiScaledWidth - fakeGuiWidth;
    }
}
