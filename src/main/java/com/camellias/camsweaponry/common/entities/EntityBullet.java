package com.camellias.camsweaponry.common.entities;

import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityBullet extends EntityThrowable implements IProjectile
{
	protected EntityPlayer owner;
	private String ownerName;
	
	public EntityBullet(World world)
	{
		super(world);
		this.setSize(0.5F, 0.5F);
	}
	
	public EntityBullet(World world, EntityPlayer player)
	{
		super(world, player);
		this.owner = player;
	}
	
	@Override
	public void onImpact(RayTraceResult result)
	{
		if(!world.isRemote)
		{
			if(result.typeOfHit == Type.ENTITY)
			{
				Entity entity = result.entityHit;
				
				if(entity != getOwner())
				{
					entity.attackEntityFrom(DamageSource.causePlayerDamage(getOwner()), 20);
					setDead();
				}
			}
			if(result.typeOfHit == Type.BLOCK)
			{
				BlockPos pos = result.getBlockPos();
				Block block = world.getBlockState(pos).getBlock();
				
				if(block.isPassable(world, pos))
				{
					
				}
				else
				{
					if(world.getBlockState(pos).getMaterial() == Material.GLASS)
					{
						world.setBlockState(pos, Blocks.AIR.getDefaultState());
						world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 2F, 1F, true);
					}
					
					setDead();
				}
			}
		}
	}
	
	@Override
	protected void entityInit()
	{
		setEntityInvulnerable(true);
		setNoGravity(true);
	}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		
		if(world.isRemote)
		{
			for(int i = 0; i < 5; i++)
			{
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY, this.posZ, 0, 0, 0);
			}
		}
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound tag)
	{
		super.writeEntityToNBT(tag);
		
		if((this.ownerName == null || this.ownerName.isEmpty()) && this.owner instanceof EntityPlayer)
		{
			this.ownerName = this.owner.getName();
		}
		
		tag.setString("ownerName", this.ownerName == null ? "" : this.ownerName);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound tag)
	{
		super.readEntityFromNBT(tag);
		
		this.owner = null;
		this.ownerName = tag.getString("ownerName");
		
		if(this.ownerName != null && this.ownerName.isEmpty())
		{
			this.ownerName = null;
		}
		
		this.owner = this.getOwner();
	}
	
	public EntityPlayer getOwner()
	{
		if(this.owner == null && this.ownerName != null && !this.ownerName.isEmpty())
		{
			this.owner = this.world.getPlayerEntityByName(this.ownerName);
			
			if(this.owner == null && this.world instanceof WorldServer)
			{
				try
				{
					Entity entity = ((WorldServer)this.world).getEntityFromUuid(UUID.fromString(this.ownerName));
					
					if(entity instanceof EntityPlayer)
					{
						this.owner = (EntityPlayer)entity;
					}
				}
				catch(Throwable var2)
				{
					this.owner = null;
				}
			}
		}
		
		return this.owner;
	}
}