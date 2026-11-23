package net.claustra01.tfie.datagen;

import net.claustra01.tfie.TFIE;
import net.claustra01.tfie.common.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public final class LangProvider extends LanguageProvider
{
    public LangProvider(PackOutput output)
    {
        super(output, TFIE.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations()
    {
        addItem(ModItems.TREATED_WOOD_LUMBER, "Treated Wood Lumber");
        addItem(ModItems.IE_HAMMER_HEAD, "Engineer's Hammer Head");
        addItem(ModItems.WIRECUTTER_HEAD, "Wirecutter Head");
        add("tfie.creative_tab.main", "TFC x IE");
    }
}
