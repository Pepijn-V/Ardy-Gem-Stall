package org.pepijnv;

import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

import javax.inject.Inject;
import java.awt.*;

public class GemstallOverlay extends Overlay
{
    private final Client client;
    private final GemstallPlugin plugin;

    @Inject
    public GemstallOverlay(Client client, GemstallPlugin plugin)
    {
        this.client = client;
        this.plugin = plugin;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        GameObject stall = plugin.getGemStall();
        if (stall != null)
        {
            // Try to get the exact clickbox
            Shape polygon = stall.getClickbox();

            // Fallback: tile area if clickbox is null
            if (polygon == null)
            {
                LocalPoint lp = stall.getLocalLocation();
                if (lp != null)
                {
                    polygon = Perspective.getCanvasTileAreaPoly(client, lp, 1); // size 1 assumed
                }
            }

            if (polygon != null)
            {
                // Fill color based on safe/unsafe
                Color fillColor = plugin.isSafeToThieve()
                        ? new Color(0, 255, 0, 50)
                        : new Color(255, 0, 0, 50);

                // Border color
                Color borderColor = plugin.isSafeToThieve() ? Color.GREEN : Color.RED;

                // Draw border
                graphics.setColor(borderColor);
                graphics.setStroke(new BasicStroke(3));
                graphics.draw(polygon);

                // Draw fill
                graphics.setColor(fillColor);
                graphics.fill(polygon);
            }
        }
        return null;
    }
}
