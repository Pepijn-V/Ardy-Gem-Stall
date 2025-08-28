package org.pepijnv;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup("gemstall")
public interface GemstallConfig extends Config
{

    @ConfigItem(
            keyName = "enableGuardCheck",
            name = "Enable guard check",
            description = "Only highlight when no guards are nearby"
    )
    default boolean enableGuardCheck()
    {
        return true;
    }

    @Range(min = 1, max = 15)
    @ConfigItem(
            keyName = "guardRadius",
            name = "Guard radius (tiles)",
            description = "Do not highlight if a guard is within this many tiles"
    )
    default int guardRadius()
    {
        return 6;
    }

}
