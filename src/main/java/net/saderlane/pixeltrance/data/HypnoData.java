package net.saderlane.pixeltrance.data;

import net.minecraft.world.entity.LivingEntity;
import net.saderlane.pixeltrance.dev.PTLog;

public class HypnoData {

    // Final variables
        // Trance
    private static final float TRANCE_DECAY_AMOUNT = 1f; // Amount trance will decay
        // Focus
    private static final float FOCUS_DECAY_AMOUNT = 1f; // Amount focus will decay

    private final LivingEntity subject; // Target being hypnotized

    // Variables
        // Trance
    private float trance = 0.0f; // Trance value
        // Focus
    private float focus = 0.0f; // Focus value



    // Constructor
    public HypnoData(LivingEntity subject) {
        this.subject = subject;
    }

    // ======= Public Trance Functions =======

    // Return current trance value
    public float getTrance() {
        PTLog.debug("Returned trance: " + trance);
        return trance;
    }

    // Set the trance value
    public void setTrance(float value) {
        this.trance = Math.clamp(value, 0f, 100f); //Set limit to how much trance can be (0-100);
        PTLog.debug("Set trance to: " + trance);
    }

    // Add to the trance value
    public void addTrance(float value) {
        setTrance(trance+value);
        PTLog.debug("Adding to trance: " + trance);
    }

    // Remove trance
    public void subTrance(float value) {
        setTrance(trance-value);
        PTLog.debug("Removing from trance: " + trance);
    }

    // ======= Public Focus Functions =======

    // Return current focus value
    public float getFocus() {
        PTLog.debug("Returned focus: " + focus);
        return focus;
    }

    // Set the focus value
    public void setFocus(float value) {
        this.focus = Math.clamp(value, 0f, 100f); //Set limit to how much focus can be (0-100);
        PTLog.debug("Set focus to: " + focus);
    }

    // Add to the focus value
    public void addFocus(float value) {
        setFocus(focus+value);
        PTLog.debug("Adding to focus: " + focus);
    }

    // Remove focus
    public void subFocus(float value) {
        setFocus(focus-value);
        PTLog.debug("Removing from focus: " + focus);
    }


}
