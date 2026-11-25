package net.claustra01.tfie.mixin;

import blusunrize.immersiveengineering.api.energy.IRotationAcceptor;
import blusunrize.immersiveengineering.common.blocks.IEBaseBlockEntity;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IStateBasedDirectional;
import blusunrize.immersiveengineering.common.blocks.metal.DynamoBlockEntity;
import blusunrize.immersiveengineering.common.blocks.ticking.IEServerTickableBE;
import net.claustra01.tfie.config.TFIEConfig;
import net.dries007.tfc.common.blockentities.rotation.RotationSinkBlockEntity;
import net.dries007.tfc.util.rotation.Node;
import net.dries007.tfc.util.rotation.Rotation;
import net.dries007.tfc.util.rotation.RotationNetworkManager;
import net.dries007.tfc.util.rotation.SinkNode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = DynamoBlockEntity.class, remap = false)
public abstract class DynamoBlockEntityMixin extends IEBaseBlockEntity
        implements IEServerTickableBE, RotationSinkBlockEntity, IStateBasedDirectional
{
    @Shadow
    @Final
    private IRotationAcceptor rotationCap;

    @Unique
    private Node tfie$rotationNode;

    protected DynamoBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    @Override
    public Node getRotationNode()
    {
        return tfie$rotationNode;
    }

    @Override
    public void tickServer()
    {
        if(tfie$rotationNode==null)
            return;
        Rotation rotation = tfie$rotationNode.rotation();
        if(rotation!=null)
            rotationCap.inputRotation(Math.abs(rotation.speed()*500F
                    *TFIEConfig.TFC_ROTATIONAL_ENERGY_MODIFIER.getAsDouble()));
    }

    @Override
    public void onLoad()
    {
        super.onLoad();
        if(level!=null&&!level.isClientSide&&tfie$rotationNode==null)
            tfie$addToRotationNetwork(((DynamoBlockEntity)(Object)this).getFacing());
    }

    @Override
    public void afterRotation(Direction oldDir, Direction newDir)
    {
        if(tfie$rotationNode!=null&&oldDir!=newDir)
        {
            tfie$removeFromRotationNetwork();
            tfie$addToRotationNetwork(newDir);
        }
    }

    @Override
    public void onChunkUnloaded()
    {
        tfie$removeFromRotationNetwork();
        super.onChunkUnloaded();
    }

    @Override
    public void setRemovedIE()
    {
        tfie$removeFromRotationNetwork();
    }

    @Unique
    private void tfie$removeFromRotationNetwork()
    {
        if(level!=null&&!level.isClientSide&&tfie$rotationNode!=null)
        {
            RotationNetworkManager.get(level).remove(tfie$rotationNode);
            tfie$rotationNode = null;
        }
    }

    @Unique
    private void tfie$addToRotationNetwork(Direction facing)
    {
        if(level!=null&&!level.isClientSide&&tfie$rotationNode==null)
        {
            tfie$rotationNode = new SinkNode(worldPosition, facing)
            {
            };
            RotationNetworkManager.get(level).add(tfie$rotationNode);
        }
    }
}
