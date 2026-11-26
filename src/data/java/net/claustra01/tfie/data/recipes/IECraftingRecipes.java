package net.claustra01.tfie.data.recipes;

import net.claustra01.tfie.common.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;

public final class IECraftingRecipes extends RecipeProvider
{
    private static final TagKey<Item> WROUGHT_IRON_INGOTS = tag("c:ingots/wrought_iron");
    private static final TagKey<Item> WROUGHT_IRON_RODS = tag("c:rods/wrought_iron");
    private static final TagKey<Item> WROUGHT_IRON_PLATES = tag("c:plates/wrought_iron");
    private static final TagKey<Item> TREATED_WOOD = tag("immersiveengineering:treated_wood");
    private static final TagKey<Item> TREATED_WOOD_RODS = tag("c:rods/treated_wood");
    private static final TagKey<Item> TREATED_WOOD_SLABS = tag("immersiveengineering:treated_wood_slab");
    private static final TagKey<Item> STONE_BRICKS = tag("tfie:stone_bricks_no_variants");
    private static final TagKey<Item> HEMP_FABRIC = tag("c:fabric_hemp");
    private static final TagKey<Item> HEMP_FIBER = tag("c:fiber_hemp");

    public IECraftingRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output)
    {
        add(output, "basic_engineering", 4, new String[]{"iwi", "w w", "iwi"}, WROUGHT_IRON_INGOTS,
                key('i', WROUGHT_IRON_INGOTS), key('w', TREATED_WOOD));
        add(output, "coil_lv", 1, new String[]{"www", "wiw", "www"}, WROUGHT_IRON_INGOTS,
                key('i', WROUGHT_IRON_INGOTS), key('w', item("immersiveengineering:wirecoil_copper")));
        add(output, "coil_mv", 1, new String[]{"www", "wiw", "www"}, WROUGHT_IRON_INGOTS,
                key('i', WROUGHT_IRON_INGOTS), key('w', item("immersiveengineering:wirecoil_electrum")));
        add(output, "coil_hv", 1, new String[]{"www", "wiw", "www"}, WROUGHT_IRON_INGOTS,
                key('i', WROUGHT_IRON_INGOTS), key('w', item("immersiveengineering:wirecoil_steel")));
        add(output, "dynamo", 1, new String[]{"rcr", "ili"}, WROUGHT_IRON_INGOTS,
                key('c', item("immersiveengineering:component_iron")), key('i', WROUGHT_IRON_INGOTS),
                key('l', item("immersiveengineering:coil_lv")), key('r', tag("c:dusts/redstone")));
        add(output, "windmill", 1, new String[]{"ppp", "pip", "ppp"}, WROUGHT_IRON_INGOTS,
                key('i', WROUGHT_IRON_INGOTS), key('p', item("immersiveengineering:windmill_blade")));
        add(output, "current_transformer", 1, new String[]{" m ", " b ", "iei"}, WROUGHT_IRON_INGOTS,
                key('b', tag("immersiveengineering:connector_insulator")),
                key('e', item("immersiveengineering:component_electronic")),
                key('i', WROUGHT_IRON_INGOTS), key('m', item("immersiveengineering:voltmeter")));
        add(output, "electromagnet", 1, new String[]{"pcp", "wiw", "pwp"}, WROUGHT_IRON_INGOTS,
                key('c', item("immersiveengineering:component_electronic")), key('i', WROUGHT_IRON_INGOTS),
                key('p', tag("c:rods/steel")), key('w', item("immersiveengineering:wirecoil_copper")));
        add(output, "floodlight", 1, new String[]{"sii", "pes", "sci"}, WROUGHT_IRON_INGOTS,
                key('c', item("immersiveengineering:component_iron")),
                key('e', item("immersiveengineering:light_bulb")), key('i', WROUGHT_IRON_INGOTS),
                key('p', tag("c:glass_panes")), key('s', tag("c:plates/silver")));
        add(output, "transformer", 1, new String[]{"lm", "eb", "ii"}, WROUGHT_IRON_INGOTS,
                key('b', item("immersiveengineering:coil_mv")),
                key('e', item("immersiveengineering:component_electronic")), key('i', WROUGHT_IRON_INGOTS),
                key('l', item("immersiveengineering:connector_lv")),
                key('m', item("immersiveengineering:connector_mv")));
        add(output, "transformer_hv", 1, new String[]{"mh", "eb", "ii"}, WROUGHT_IRON_INGOTS,
                key('b', item("immersiveengineering:coil_hv")),
                key('e', item("immersiveengineering:component_electronic")),
                key('h', item("immersiveengineering:connector_hv")), key('i', WROUGHT_IRON_INGOTS),
                key('m', item("immersiveengineering:connector_mv")));

        add(output, "conveyor_basic", 8, new String[]{"lll", "iri"}, WROUGHT_IRON_INGOTS,
                key('i', WROUGHT_IRON_INGOTS), key('l', tag("c:leathers")), key('r', tag("c:dusts/redstone")));
        add(output, "conveyor_splitter", 3, new String[]{"cic", " c "}, WROUGHT_IRON_INGOTS,
                key('c', item("immersiveengineering:conveyor_basic")), key('i', WROUGHT_IRON_INGOTS));
        add(output, "pipe_valve", 1, new String[]{"pc", "sr"}, WROUGHT_IRON_RODS,
                key('c', item("immersiveengineering:component_iron")),
                key('p', item("immersiveengineering:fluid_pipe")), key('r', tag("c:dusts/redstone")),
                key('s', WROUGHT_IRON_RODS));
        add(output, "hatch", 1, new String[]{"rr", "pp"}, WROUGHT_IRON_PLATES,
                key('p', WROUGHT_IRON_PLATES), key('r', WROUGHT_IRON_RODS));
        add(output, "treated_post", 1, new String[]{"f", "f", "s"}, STONE_BRICKS,
                key('f', item("immersiveengineering:treated_fence")), key('s', STONE_BRICKS));
        add(output, "steel_post", 1, new String[]{"f", "f", "s"}, STONE_BRICKS,
                key('f', item("immersiveengineering:steel_fence")), key('s', STONE_BRICKS));
        add(output, "alu_post", 1, new String[]{"f", "f", "s"}, STONE_BRICKS,
                key('f', item("immersiveengineering:alu_fence")), key('s', STONE_BRICKS));
        add(output, "craftingtable", 1, new String[]{"sss", "rcr", "r r"}, TREATED_WOOD_RODS,
                key('c', tag("c:player_workstations/crafting_tables")), key('r', TREATED_WOOD_RODS),
                key('s', ModItems.TREATED_WOOD_LUMBER.get()));
        add(output, "workbench", 1, new String[]{"iss", "c f"}, WROUGHT_IRON_INGOTS,
                key('c', item("immersiveengineering:craftingtable")),
                key('f', item("immersiveengineering:treated_fence")), key('i', WROUGHT_IRON_INGOTS),
                key('s', TREATED_WOOD_SLABS));

        add(output, "hammer", 1, new String[]{"h", "s"}, tag("c:rods/wooden"),
                key('h', ModItems.IE_HAMMER_HEAD.get()), key('s', tag("c:rods/wooden")));
        add(output, "wirecutter", 1, new String[]{"si", " s"}, tag("c:rods/wooden"),
                key('i', ModItems.WIRECUTTER_HEAD.get()), key('s', tag("c:rods/wooden")));
        add(output, "screwdriver", 1, new String[]{" i", "s "}, WROUGHT_IRON_RODS,
                key('i', WROUGHT_IRON_RODS), key('s', tag("c:rods/wooden")));
        add(output, "earmuffs", 1, new String[]{" S ", "S S", "W W"}, WROUGHT_IRON_RODS,
                key('S', WROUGHT_IRON_RODS), key('W', tag("minecraft:wool")));
        add(output, "speedloader", 1, new String[]{"sd", "dc"}, WROUGHT_IRON_RODS,
                key('c', item("immersiveengineering:component_iron")), key('d', tag("c:plates/plastic")),
                key('s', WROUGHT_IRON_RODS));
        add(output, "toolupgrade_buzzsaw_spareblades", 1, new String[]{"rht", "rt "}, WROUGHT_IRON_RODS,
                key('h', HEMP_FIBER), key('r', WROUGHT_IRON_RODS), key('t', TREATED_WOOD));
        add(output, "toolupgrade_powerpack_magnet", 1, new String[]{"rer", "wiw", " w "},
                WROUGHT_IRON_INGOTS, key('e', item("immersiveengineering:component_electronic_adv")),
                key('i', WROUGHT_IRON_INGOTS), key('r', tag("c:rods/steel")),
                key('w', item("immersiveengineering:wirecoil_copper")));
        add(output, "toolupgrade_shield_magnet", 1, new String[]{"  l", "lc ", "lil"}, WROUGHT_IRON_INGOTS,
                key('c', item("immersiveengineering:coil_lv")), key('i', WROUGHT_IRON_INGOTS),
                key('l', tag("c:leathers")));
        add(output, "toolupgrade_shield_shock", 1, new String[]{"crc", "crc", "crc"}, WROUGHT_IRON_RODS,
                key('c', item("immersiveengineering:connector_lv")), key('r', WROUGHT_IRON_RODS));

        add(output, "balloon", 2, new String[]{" f ", "ftf", " s "}, HEMP_FABRIC,
                key('f', HEMP_FABRIC), key('s', TREATED_WOOD_SLABS), key('t', item("minecraft:torch")));
        add(output, "cushion", 3, new String[]{"fff", "f f", "fff"}, HEMP_FABRIC,
                key('f', HEMP_FABRIC));
        add(output, "strip_curtain", 3, new String[]{"sss", "fff", "fff"}, HEMP_FABRIC,
                key('f', HEMP_FABRIC), key('s', tag("c:rods/all_metal")));
        add(output, "wirecoil_structure_rope", 4, new String[]{" w ", "wsw", " w "}, HEMP_FIBER,
                key('s', tag("c:rods/wooden")), key('w', HEMP_FIBER));
        add(output, "gunpowder_barrel", 1, new String[]{"gfg", "gbg"}, HEMP_FIBER,
                key('b', item("immersiveengineering:wooden_barrel")), key('f', HEMP_FIBER),
                key('g', tag("c:gunpowders")));
    }

    private static void add(RecipeOutput output, String path, int count, String[] pattern, TagKey<Item> unlock,
                            Key... keys)
    {
        ShapedRecipeBuilder builder = ShapedRecipeBuilder.shaped(RecipeCategory.MISC,
                item("immersiveengineering:" + path), count);
        for(Key key : keys)
        {
            if(key.tag() != null)
                builder.define(key.symbol(), key.tag());
            else
                builder.define(key.symbol(), key.item());
        }
        for(String row : pattern)
            builder.pattern(row);
        builder.unlockedBy("has_material", has(unlock)).save(output,
                ResourceLocation.fromNamespaceAndPath("immersiveengineering", "crafting/" + path));
    }

    private static Key key(char symbol, TagKey<Item> tag)
    {
        return new Key(symbol, tag, null);
    }

    private static Key key(char symbol, ItemLike item)
    {
        return new Key(symbol, null, item);
    }

    private static Item item(String id)
    {
        return BuiltInRegistries.ITEM.get(ResourceLocation.parse(id));
    }

    private static TagKey<Item> tag(String id)
    {
        return TagKey.create(Registries.ITEM, ResourceLocation.parse(id));
    }

    private record Key(char symbol, TagKey<Item> tag, ItemLike item)
    {
    }
}
