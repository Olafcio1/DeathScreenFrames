package pl.olafcio.deathscreenframes;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

public final class Main implements ModInitializer, ClientModInitializer {
    public static boolean loaded = false;
    public static ArrayList<Identifier> frames;

    @Override
    public void onInitialize() {
        LoggerFactory.getLogger("deathscreenframes").info("[Coded by » Olafcio] [Resources by » Uh i forgot]");
    }

    @Override
    public void onInitializeClient() {
        loadFrames();
        ClientPlayConnectionEvents.INIT.register((packetListener, mc) -> {
            attackers.clear();
        });
    }

    private void loadFrames() {
        frames = new ArrayList<>();

        try (var flResource = getClass().getResourceAsStream("/assets/deathscreenframes/framelist.txt")) {
            assert flResource != null;

            var flData = flResource.readAllBytes();
            var lines = new String(flData).split("\n");

            for (var line : lines) {
                line = line.trim();

                if (!line.startsWith("#") && !line.isEmpty())
                    frames.add(Identifier.fromNamespaceAndPath("deathscreenframes", "assets/deathscreenframes/frames/" + line));
            }

            loaded = true;
        } catch (Throwable e) {
            var logger = LoggerFactory.getLogger("deathscreenframes");

            logger.error("Failed to load framelist.txt!");
            logger.error("Here's the error ({}):", e.getMessage());

            e.printStackTrace();
        }
    }

    public static HashMap<Entity, Long> attackers = new HashMap<>();
    public static ArrayList<Entity> toRemove = new ArrayList<>();

    public static void attackersReduce() {
        var now = System.currentTimeMillis();
        var entries = attackers.entrySet();

        for (var entry : entries)
            if (!entry.getKey().isAlive() || entry.getValue() < now)
                toRemove.add(entry.getKey());

        if (!toRemove.isEmpty()) {
            for (var entity : toRemove)
                attackers.remove(entity);

            toRemove.clear();
        }
    }
}
