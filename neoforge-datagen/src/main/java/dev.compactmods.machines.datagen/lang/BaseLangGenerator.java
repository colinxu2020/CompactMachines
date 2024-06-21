package dev.compactmods.machines.datagen.lang;

import dev.compactmods.machines.api.CompactMachines;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.apache.commons.lang3.StringUtils;

public abstract class BaseLangGenerator extends LanguageProvider {

    private final String locale;

    public BaseLangGenerator(DataGenerator gen, String locale) {
        super(gen.getPackOutput(), CompactMachines.MOD_ID, locale);
        this.locale = locale;
    }

    protected String getDirectionTranslation(Direction dir) {
        return StringUtils.capitalize(dir.getSerializedName());
    }

    protected String getMachineTranslation() {
        return "Compact Machine";
    }

    @Override
    protected void addTranslations() {
        // Direction Names
        for (var dir : Direction.values()) {
            add(Util.makeDescriptionId("direction", CompactMachines.modRL(dir.getSerializedName())), getDirectionTranslation(dir));
        }
    }

    protected void addCreativeTab(ResourceLocation id, String translation) {
        add(Util.makeDescriptionId("itemGroup", id), translation);
    }

    protected void advancement(ResourceLocation adv, String title, String desc) {
        add(Util.makeDescriptionId("advancement", adv), title);
        add(Util.makeDescriptionId("advancement", adv) + ".desc", desc != null ? desc : "");
    }
}
