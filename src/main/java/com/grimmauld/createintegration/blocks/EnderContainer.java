package com.grimmauld.createintegration.blocks;

import com.grimmauld.createintegration.CreateIntegration;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class EnderContainer extends Container{
	private EnderChestTile tileEntity;
    private IItemHandler playerInventory;

    public EnderContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory) {
        super(ModBlocks.ENDER_CONTAINER, windowId);
        tileEntity = (EnderChestTile) world.getTileEntity(pos);
        this.playerInventory = new InvWrapper(playerInventory);

        
        tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
        	for(int i = 0; i < 3; ++i) {
                for(int j = 0; j < 3; ++j) {
                    addSlot(new SlotItemHandler(h, j + i * 3, 62 + j * 18, 17 + i * 18));
                }
            }
        });
        
        
     
        
        layoutPlayerInventorySlots(8, 84);
    }
    
    private SlotItemHandler createSlotItemHandler(IItemHandler h, int index, int x, int y) {
    	return new SlotItemHandler(h, index, x, y);
    }
    

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), playerIn, ModBlocks.ENDER_CHEST);
    }


    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0 ; i < amount ; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0 ; j < verAmount ; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }
}