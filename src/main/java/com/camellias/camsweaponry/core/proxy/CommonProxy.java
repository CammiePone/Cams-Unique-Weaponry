package com.camellias.camsweaponry.core.proxy;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CommonProxy
{
	public void registerItemRenderer(Item item, int meta, String id)
	{

	}

	public IThreadListener getThreadListener(final MessageContext context)
	{
		if(context.side.isServer())
		{
			return context.getServerHandler().player.getServer();
		} else {
			throw new WrongSideException("Tried to get the IThreadListener from a client-side MessageContext on the dedicated server");
		}
	}

	@Nullable
	public EntityLivingBase getEntityLivingBase(MessageContext context, int entityID)
	{
		if(context.side.isServer())
		{
			Entity entity = context.getServerHandler().player.world.getEntityByID(entityID);
			return entity instanceof EntityLivingBase ? (EntityLivingBase) entity : null;
		}
		throw new WrongSideException("Tried to get the player from a client-side MessageContext on the dedicated server");
	}

	class WrongSideException extends RuntimeException
	{
		public WrongSideException(final String message)
		{
			super(message);
		}

		public WrongSideException(final String message, final Throwable cause)
		{
			super(message, cause);
		}
	}
}
