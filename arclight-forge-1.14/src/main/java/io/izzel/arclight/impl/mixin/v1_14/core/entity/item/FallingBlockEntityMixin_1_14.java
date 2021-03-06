package io.izzel.arclight.impl.mixin.v1_14.core.entity.item;

import io.izzel.arclight.impl.mixin.v1_14.core.entity.EntityMixin_1_14;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.util.math.BlockPos;
import org.bukkit.craftbukkit.v.event.CraftEventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin_1_14 extends EntityMixin_1_14 {

    @Shadow private BlockState fallTile;

    @Inject(method = "tick", cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private void arclight$entityChangeBlock(CallbackInfo ci, Block block, BlockPos pos) {
        if (CraftEventFactory.callEntityChangeBlockEvent((FallingBlockEntity) (Object) this, pos, this.fallTile).isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "fall", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z"))
    private void arclight$damageSource(float distance, float damageMultiplier, CallbackInfo ci) {
        CraftEventFactory.entityDamage = (FallingBlockEntity) (Object) this;
    }

    @Inject(method = "fall", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/entity/Entity;attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z"))
    private void arclight$damageSourceReset(float distance, float damageMultiplier, CallbackInfo ci) {
        CraftEventFactory.entityDamage = null;
    }
}
