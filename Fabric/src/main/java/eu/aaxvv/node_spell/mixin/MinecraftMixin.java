package eu.aaxvv.node_spell.mixin;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.client.NodeSpellClient;
import eu.aaxvv.node_spell.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow
    public LocalPlayer player;

    // trigger spell selection overlay when left-clicking wand
    @Inject(method = "startAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/HitResult;getType()Lnet/minecraft/world/phys/HitResult$Type;", shift = At.Shift.BEFORE), cancellable = true)
    private void afterAttack(CallbackInfoReturnable<Boolean> cir) {
        ItemStack itemstack = this.player.getItemInHand(InteractionHand.MAIN_HAND);
        if (itemstack.getItem() != ModItems.WAND) {
            return;
        }

        NodeSpellClient.getSpellSelectionOverlay().activate();
        cir.cancel();
    }
}
