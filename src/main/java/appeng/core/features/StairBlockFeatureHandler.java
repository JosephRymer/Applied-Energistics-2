/*
 * This file is part of Applied Energistics 2.
 * Copyright (c) 2013 - 2014, AlgorithmX2, All rights reserved.
 *
 * Applied Energistics 2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Applied Energistics 2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Applied Energistics 2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */

package appeng.core.features;


import java.util.EnumSet;

import com.google.common.base.Optional;

import net.minecraft.block.BlockStairs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import appeng.api.definitions.IBlockDefinition;
import appeng.core.CommonHelper;
import appeng.core.CreativeTab;


public class StairBlockFeatureHandler implements IFeatureHandler
{
	private final BlockStairs stairs;
	private final FeatureNameExtractor extractor;
	private final boolean enabled;
	private final BlockDefinition definition;

	public StairBlockFeatureHandler( final EnumSet<AEFeature> features, final BlockStairs stairs, final Optional<String> subName )
	{
		final ActivityState state = new FeaturedActiveChecker( features ).getActivityState();

		this.stairs = stairs;
		this.extractor = new FeatureNameExtractor( stairs.getClass(), subName );
		this.enabled = state == ActivityState.Enabled;
		this.definition = new BlockDefinition( stairs.getClass().getSimpleName(), stairs, state );
	}

	@Override
	public final boolean isFeatureAvailable()
	{
		return this.enabled;
	}

	@Override
	public final IBlockDefinition getDefinition()
	{
		return this.definition;
	}

	@Override
	public final void register( final Side side )
	{
		if( this.enabled )
		{
			final String name = this.extractor.get();
			this.stairs.setCreativeTab( CreativeTab.instance );
			this.stairs.setUnlocalizedName( "appliedenergistics2." + name );

			GameRegistry.registerBlock( this.stairs, name );

			if( side == Side.CLIENT )
			{
				final Item item = ItemBlock.getItemFromBlock( this.stairs );
				CommonHelper.proxy.configureIcon( item, name );
			}
		}
	}
}
