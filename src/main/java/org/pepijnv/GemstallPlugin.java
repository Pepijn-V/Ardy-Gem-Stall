package org.pepijnv;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import net.runelite.api.*;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import java.util.Set;

@Slf4j
@PluginDescriptor(
        name = "Gem Stall Helper",
        description = "Highlights the gem stall when it’s safe to thieve"
)
public class GemstallPlugin extends Plugin
{
    @Inject private Client client;
    @Inject private GemstallConfig config;
    @Inject private OverlayManager overlayManager;
    @Inject private GemstallOverlay overlay;

    // Guard IDs
    private static final Set<Integer> GUARD_IDS = Set.of(
            11937,
            11938,
            11939,
            6579,
            5418
    );

    // Current gem stall
    private GameObject gemStall;
    private boolean safeToThieve;

    public GameObject getGemStall()
    {
        return gemStall;
    }

    public boolean isSafeToThieve()
    {
        return safeToThieve;
    }

    @Override
    protected void startUp() throws Exception
    {
        log.info("Gem Stall Helper started");
        overlayManager.add(overlay);
    }

    @Override
    protected void shutDown() throws Exception
    {
        overlayManager.remove(overlay);
        gemStall = null;
        safeToThieve = false;
        log.info("Gem Stall Helper stopped");
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event)
    {
        if (event.getGameState() == GameState.LOGGED_IN)
        {
            log.debug("Player logged in, plugin active");
        }
    }

    @Subscribe
    public void onGameTick(GameTick tick)
    {
        if (client.getGameState() != GameState.LOGGED_IN || client.getLocalPlayer() == null)
        {
            safeToThieve = false;
            return;
        }

        // Check if area is safe
        safeToThieve = config.enableGuardCheck()
                ? isAreaSafeFromGuards(config.guardRadius())
                : true;
    }

    @Subscribe
    public void onGameObjectSpawned(GameObjectSpawned event)
    {
        GameObject obj = event.getGameObject();

        // Replace with the actual Gem Stall Object ID(s)
        if (obj.getId() == ObjectID.GEM_STALL || obj.getId() == 11731)
        {
            gemStall = obj;
        }
    }

    @Subscribe
    public void onGameObjectDespawned(GameObjectDespawned event)
    {
        GameObject obj = event.getGameObject();
        if (obj == gemStall)
        {
            gemStall = null;
        }
    }

    /**
     * Returns true if no guards are both in line-of-sight and within the radius
     */
    private boolean isAreaSafeFromGuards(int radius)
    {
        Player me = client.getLocalPlayer();
        if (me == null)
            return true;

        WorldPoint myLoc = me.getWorldLocation();
        int plane = myLoc.getPlane();
        WorldArea playerArea = me.getWorldArea();

        for (NPC npc : client.getNpcs())
        {
            if (npc == null || !GUARD_IDS.contains(npc.getId()))
                continue;

            WorldPoint npcLoc = npc.getWorldLocation();
            if (npcLoc.getPlane() != plane)
                continue;

            // 1) Same-tile check -> unsafe
            if (npcLoc.equals(myLoc))
                return false;

            // 2) Distance check
            if (npcLoc.distanceTo(myLoc) > radius)
                continue;

            // 3) LOS check
            if (playerArea.hasLineOfSightTo(client.getTopLevelWorldView(), npcLoc))
                return false; // guard sees player and is close
        }

        return true; // safe
    }





    /**
     * Simple LOS raycast using tile flags
     */


    @Provides
    GemstallConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(GemstallConfig.class);
    }
}
