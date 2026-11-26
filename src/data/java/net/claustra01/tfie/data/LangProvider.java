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
        add("tooltip.tfie.arc_furnace_blueprint.mode", "Mode %s");
        add("tooltip.tfie.arc_furnace_blueprint.use", "Use in the main hand to change mode");
        add("message.tfie.arc_furnace_blueprint.mode", "Arc Furnace mode: %s");
        add("gui.tfie.arc_furnace.invalid_blueprints", "Multiple Arc Furnace Blueprints");
        add("gui.tfie.arc_furnace.invalid_mode", "Invalid Arc Furnace Blueprint mode");
        add("gui.tfie.arc_furnace.duplicate_recipe", "Conflicting Arc Furnace recipes");
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
