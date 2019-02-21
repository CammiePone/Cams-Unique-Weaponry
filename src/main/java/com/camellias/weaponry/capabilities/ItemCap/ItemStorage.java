package com.camellias.weaponry.capabilities.ItemCap;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class ItemStorage implements IStorage<IItemCap> {

	public static final ItemStorage storage = new ItemStorage();

	@Override
	public NBTBase writeNBT(Capability<IItemCap> capability, IItemCap instance, EnumFacing side) {
		final NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("type", instance.Type());
		tag.setBoolean("NightVision", instance.effect());
		return tag;
	}

	@Override
	public void readNBT(Capability<IItemCap> capability, IItemCap instance, EnumFacing side, NBTBase nbt) {
		if (nbt instanceof NBTTagCompound) {
			final NBTTagCompound tag = (NBTTagCompound) nbt;
			if (tag.hasKey("type")) {
				instance.setType(tag.getInteger("type"));
			}
			if (tag.hasKey("effect")) {
				instance.setEffect(tag.getBoolean("effect"));
			}
		}
	}

}
