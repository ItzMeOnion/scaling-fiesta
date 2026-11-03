package com.example.butterflyknife;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ButterflyKnifeClient implements ClientModInitializer {

    /** Length of one spin, in nanoseconds (0.6 seconds). */
    private static final long SPIN_DURATION_NANOS = 600_000_000L;

    private static final KeyBinding SPIN_KEY = new KeyBinding(
            "key.butterfly_knife.spin",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            "category.butterfly_knife"
    );

    /** System.nanoTime() when the current spin started, or 0 if none has started. */
    private static long spinStartNanos = 0L;

    public static boolean isSpinning() {
        return spinStartNanos != 0L && System.nanoTime() - spinStartNanos < SPIN_DURATION_NANOS;
    }

    /** 0.0 at the start of a spin, 1.0 at the end. Returns 0 when not spinning. */
    public static float spinProgress() {
        if (!isSpinning()) {
            return 0f;
        }
        return (System.nanoTime() - spinStartNanos) / (float) SPIN_DURATION_NANOS;
    }

    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(SPIN_KEY);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (SPIN_KEY.wasPressed()) {
                if (!isSpinning()) {
                    spinStartNanos = System.nanoTime();
                }
            }
        });
    }
}
