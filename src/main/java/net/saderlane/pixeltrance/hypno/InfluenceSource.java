package net.saderlane.pixeltrance.hypno;

import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Nullable;

public class InfluenceSource {
    private final int focusGain;
    private final int tranceGain;

    @Nullable
    private String descriptionId;

    // Constructor
    public InfluenceSource(int focusGain, int tranceGain) {
        this.focusGain = focusGain;
        this.tranceGain = tranceGain;
    }

    // Return source's focus gain
    public int getFocusGain() {
        return this.focusGain;
    }

    // Return source's trance gain
    public int getTranceGain() {
        return this.tranceGain;
    }


    // Look up registry ID and build key
    public String getDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("influence_source", ModInfluenceSources.REGISTRY.getKey(this));
        }
        return this.descriptionId;
    }

    // Translate display name
    public MutableComponent getName() {
        return Component.translatable(this.getDescriptionId());
    }
}