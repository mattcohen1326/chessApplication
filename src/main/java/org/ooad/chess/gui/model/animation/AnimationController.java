package org.ooad.chess.gui.model.animation;

import java.util.LinkedList;
import java.util.List;

public class AnimationController {

    private final List<Animation> activeAnimations = new LinkedList<>();

    private long lastTick = -1;

    public void tickAnimations() {
        if (lastTick == -1) {
            lastTick = System.currentTimeMillis();
            return;
        }

        long delta = System.currentTimeMillis() - lastTick;
        lastTick = System.currentTimeMillis();

        activeAnimations.forEach(animation -> animation.tick(delta / 1000.0));
    }

    public void removeAnimation(Animation animation) {
        activeAnimations.remove(animation);
    }

    public void addAnimation(Animation animation) {
        activeAnimations.add(animation);
    }
}
