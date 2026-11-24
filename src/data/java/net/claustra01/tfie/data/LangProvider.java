package net.claustra01.tfie.data;

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
        addItem(ModItems.MOLD_SHEET, "Sheet Mold");
        addItem(ModItems.ARC_FURNACE_BLUEPRINT, "Arc Furnace Blueprint");
        addItem(ModItems.DRILLHEAD_BLACK_STEEL, "Black Steel Drill Head");
        addItem(ModItems.DRILLHEAD_BLUE_STEEL, "Blue Steel Drill Head");
        addItem(ModItems.DRILLHEAD_RED_STEEL, "Red Steel Drill Head");
        addItem(ModItems.DRILLHEAD_INVAR, "Invar Drill Head");
        addItem(ModItems.DRILLHEAD_TITANIUM, "Titanium Drill Head");
        addItem(ModItems.DRILLHEAD_NETHERITE, "Netherite Drill Head");
        addItem(ModItems.DRILLHEAD_TUNGSTEN_STEEL, "Tungsten Steel Drill Head");
        addItem(ModItems.WILD_HEMP_CROP, "Wild Hemp");
        add("tfie.creative_tab.main", "TFC x IE");
    }
}
