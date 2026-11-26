package net.claustra01.tfie.mixin.compat.jei;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public final class TFIEJeiMixinPlugin implements IMixinConfigPlugin
{
    private boolean jeiPresent;

    @Override
    public void onLoad(String mixinPackage)
    {
        try
        {
            Class.forName("mezz.jei.api.IModPlugin", false, getClass().getClassLoader());
            jeiPresent = true;
        }
        catch(ClassNotFoundException ignored)
        {
            jeiPresent = false;
        }
    }

    @Override
    public String getRefMapperConfig()
    {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName)
    {
        return jeiPresent;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets)
    {
    }

    @Override
    public List<String> getMixins()
    {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName,
                         IMixinInfo mixinInfo)
    {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName,
                          IMixinInfo mixinInfo)
    {
    }
}
