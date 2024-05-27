package dev.compactmods.machines.datagen.lang;

import dev.compactmods.machines.api.CompactMachinesApi;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.LanguageProvider;

import static org.apache.commons.lang3.StringUtils.capitalize;

public abstract class BaseLangGenerator extends LanguageProvider {

    private final String locale;

    public BaseLangGenerator(DataGenerator gen, String locale) {
        super(gen.getPackOutput(), CompactMachinesApi.MOD_ID, locale);
        this.locale = locale;
    }

    protected String getDirectionTranslation(Direction dir) {
        return capitalize(dir.getSerializedName());
    }

    protected String getMachineTranslation() {
        return "Compact Machine";
    }

    @Override
    protected void addTranslations() {
        // Direction Names
        for (var dir : Direction.values()) {
            add(Util.makeDescriptionId("direction", CompactMachinesApi.modRL(dir.getSerializedName())), getDirectionTranslation(dir));
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
