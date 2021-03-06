package cofh.thermalexpansion.plugins;

import cofh.core.util.ModPlugin;
import cofh.thermalexpansion.ThermalExpansion;
import cofh.thermalexpansion.util.managers.machine.CompactorManager;
import cofh.thermalexpansion.util.managers.machine.InsolatorManager;
import cofh.thermalexpansion.util.managers.machine.InsolatorManager.Type;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

public class PluginExU2 extends ModPlugin {

	public static final String MOD_ID = "extrautils2";
	public static final String MOD_NAME = "Extra Utilities 2";

	public PluginExU2() {

		super(MOD_ID, MOD_NAME);
	}

	/* IInitializer */
	@Override
	public boolean initialize() {

		String category = "Plugins";
		String comment = "If TRUE, support for " + MOD_NAME + " is enabled.";
		enable = Loader.isModLoaded(MOD_ID) && ThermalExpansion.CONFIG.getConfiguration().getBoolean(MOD_NAME, category, true, comment);

		if (!enable) {
			return false;
		}
		return !error;
	}

	@Override
	public boolean register() {

		if (!enable) {
			return false;
		}
		try {
			ItemStack[] cobblestone = new ItemStack[8];
			ItemStack[] dirt = new ItemStack[4];
			ItemStack[] sand = new ItemStack[2];
			ItemStack[] gravel = new ItemStack[2];
			ItemStack[] netherrack = new ItemStack[6];

			for (int i = 0; i < cobblestone.length; i++) {
				cobblestone[i] = getItemStack("compressedcobblestone", 1, i);
			}
			for (int i = 0; i < dirt.length; i++) {
				dirt[i] = getItemStack("compresseddirt", 1, i);
			}
			for (int i = 0; i < sand.length; i++) {
				sand[i] = getItemStack("compressedsand", 1, i);
			}
			for (int i = 0; i < gravel.length; i++) {
				gravel[i] = getItemStack("compressedgravel", 1, i);
			}
			for (int i = 0; i < netherrack.length; i++) {
				netherrack[i] = getItemStack("compressednetherrack", 1, i);
			}
			ItemStack enderLily = getItemStack("enderlilly", 1);
			ItemStack redOrchid = getItemStack("redorchid", 1);

			/* PULVERIZER */
			{

			}

			/* INSOLATOR */
			{
				InsolatorManager.addDefaultRecipe(InsolatorManager.DEFAULT_ENERGY * 4, InsolatorManager.DEFAULT_FLUID * 4, enderLily, new ItemStack(Items.ENDER_PEARL), enderLily, 100, 105, 110, Type.STANDARD);
				InsolatorManager.addDefaultRecipe(InsolatorManager.DEFAULT_ENERGY * 10, InsolatorManager.DEFAULT_FLUID * 4, redOrchid, new ItemStack(Items.REDSTONE), redOrchid, 100, 105, 110, Type.STANDARD);
			}

			/* COMPACTOR */
			{
				CompactorManager.addDefaultStorageRecipe(new ItemStack(Blocks.COBBLESTONE), cobblestone[0]);
				CompactorManager.addDefaultStorageRecipe(new ItemStack(Blocks.DIRT), dirt[0]);
				// CompactorManager.addDefaultStorageRecipe(new ItemStack(Blocks.SAND), sand[0]);
				CompactorManager.addDefaultStorageRecipe(new ItemStack(Blocks.GRAVEL), gravel[0]);
				CompactorManager.addDefaultStorageRecipe(new ItemStack(Blocks.NETHERRACK), netherrack[0]);

				for (int i = 0; i < cobblestone.length - 1; i++) {
					CompactorManager.addDefaultStorageRecipe(cobblestone[i], cobblestone[i + 1]);
				}
				for (int i = 0; i < dirt.length - 1; i++) {
					CompactorManager.addDefaultStorageRecipe(dirt[i], dirt[i + 1]);
				}
				for (int i = 0; i < sand.length - 1; i++) {
					CompactorManager.addDefaultStorageRecipe(sand[i], sand[i + 1]);
				}
				for (int i = 0; i < gravel.length - 1; i++) {
					CompactorManager.addDefaultStorageRecipe(gravel[i], gravel[i + 1]);
				}
				for (int i = 0; i < netherrack.length - 1; i++) {
					CompactorManager.addDefaultStorageRecipe(netherrack[i], netherrack[i + 1]);
				}
			}
		} catch (Throwable t) {
			ThermalExpansion.LOG.error("Thermal Expansion: " + MOD_NAME + " Plugin encountered an error:", t);
			error = true;
		}
		if (!error) {
			ThermalExpansion.LOG.info("Thermal Expansion: " + MOD_NAME + " Plugin Enabled.");
		}
		return !error;
	}

}
