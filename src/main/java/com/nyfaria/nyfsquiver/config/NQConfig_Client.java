package com.nyfaria.nyfsquiver.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class NQConfig_Client {

    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final ClientConfig CLIENT;


    static {
        Pair<ClientConfig, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        CLIENT_SPEC = pair.getRight();
        CLIENT = pair.getLeft();
    }

    public static boolean animates() {
        return CLIENT.ANIMATE.get();
    }

    public static boolean hides() {
        return CLIENT.HIDE.get();
    }

    public static int getHorizontalOffset() {
        return CLIENT.HORIZONTAL_OFFSET.get();
    }

    public static Anchor getAnchor() {
        return CLIENT.ANCHOR.get();
    }
    public static int getVerticalOffset() {
        return CLIENT.VERTICAL_OFFSET.get();
    }

    public static double getGUIScale() {
        return CLIENT.GUI_SCALE.get();
    }

    public static class ClientConfig {
        public final ForgeConfigSpec.BooleanValue ANIMATE;
        public final ForgeConfigSpec.BooleanValue HIDE;
        public final ForgeConfigSpec.EnumValue<Anchor> ANCHOR;
        public final ForgeConfigSpec.IntValue HORIZONTAL_OFFSET;
        public final ForgeConfigSpec.IntValue VERTICAL_OFFSET;
        public final ForgeConfigSpec.DoubleValue GUI_SCALE;

        public ClientConfig(ForgeConfigSpec.Builder builder) {
            builder.push("QuiverHUD Settings");
            this.ANIMATE = builder.comment("Animate the HUD Quiver showing and hiding.").translation("hud_quiver.config.animate").define("ANIMATE", true);
            this.HIDE = builder.comment("Hide the HUD Quiver when not selecting a shootable item.").translation("hud_quiver.config.hide").define("HIDE", true);
            this.HORIZONTAL_OFFSET = builder.comment("The margin on the left of the HUD Quiver.").translation("hud_quiver.config.horizontal_offset").defineInRange("HORIZONTAL_OFFSET", 0, -1000, 1000);
            this.VERTICAL_OFFSET = builder.comment("The margin on the top of the HUD Quiver.").translation("hud_quiver.config.vertical_offset").defineInRange("VERTICAL_OFFSET", 0, -1000, 1000);
            this.GUI_SCALE = builder.comment("Scale of the GUI").translation("model_quiver.old_quiver").defineInRange("gui_scale", 1,0.1d,200d);
            this.ANCHOR = builder.comment("The anchor point of the HUD Quiver.").translation("hud_quiver.config.anchor").defineEnum("ANCHOR", Anchor.TOP_LEFT);
            builder.pop();
        }
    }
}
