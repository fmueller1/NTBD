package freelook.freelook;

import freelook.freelook.config.FreeLookConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class FreeLookMod implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Freelook");
    public static final FreeLookConfig config = new FreeLookConfig();
    public static final CannonMath cannonMath = new CannonMath();
    public static boolean isFreeLooking = false;
    private static Perspective lastPerspective;
    private KeyBinding freeLookKeyBind;
    private KeyBinding freeLookScreenKeyBind;
    boolean wasPressed;

    @Override
    public void onInitializeClient() {
        config.load();
        this.freeLookKeyBind = KeyBindingHelper.registerKeyBinding(new KeyBinding("freelook.key.activate", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_ALT, "freelook.key.category"));
        this.freeLookScreenKeyBind = KeyBindingHelper.registerKeyBinding(new KeyBinding("freelook.key.menu", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_M, "freelook.key.category"));
        ClientTickEvents.END_CLIENT_TICK.register(this::onTickEnd);
    }

    private void onTickEnd(MinecraftClient client) {
        if (freeLookScreenKeyBind.isPressed()) {
            Screen screen = new FreelookScreen();
            client.setScreen(screen);
        }
        if (config.isToggle()) {
            freelookToggle(client);
        } else{
            freelookHold(client);
        }
    }

    private void freelookHold(MinecraftClient client){
        if (freeLookKeyBind.isPressed() && !isFreeLooking) {
            startFreeLooking(client);
        } else if (!freeLookKeyBind.isPressed() && isFreeLooking) {
            stopFreeLooking(client);
        }
    }

    private void freelookToggle(MinecraftClient client){
        boolean buttonDown = freeLookKeyBind.isPressed() && !wasPressed;
        wasPressed = freeLookKeyBind.isPressed();
        if(buttonDown && !isFreeLooking){
            startFreeLooking(client);
        } else if(buttonDown){
            stopFreeLooking(client);
        }
    }

    private void startFreeLooking(MinecraftClient client) {
        if(config.getPerspective() == Perspective.FIRST_PERSON) {
            client.player.setInvisible(true);
        }
        lastPerspective = client.options.getPerspective();
        // only switch to configured perspective if in first person, looks weird otherwise
        if (lastPerspective == Perspective.FIRST_PERSON) {
            client.options.setPerspective(config.getPerspective());
        }
        isFreeLooking = true;
    }

    private void stopFreeLooking(MinecraftClient client) {
        client.player.setInvisible(false);
        isFreeLooking = false;
        client.options.setPerspective(lastPerspective);
    }

}
