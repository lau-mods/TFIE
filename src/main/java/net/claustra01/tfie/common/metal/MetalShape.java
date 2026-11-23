package net.claustra01.tfie.common.metal;

public enum MetalShape
{
    INGOT("ingot", "ingots"),
    PLATE("plate", "plates"),
    SHEET("sheet", "sheets"),
    ROD("rod", "rods"),
    WIRE("wire", "wires"),
    DOUBLE_SHEET("double_sheet", "double_sheets");

    private final String id;
    private final String tagFolder;

    MetalShape(String id, String tagFolder)
    {
        this.id = id;
        this.tagFolder = tagFolder;
    }

    public String id()
    {
        return id;
    }

    public String tagFolder()
    {
        return tagFolder;
    }

}
