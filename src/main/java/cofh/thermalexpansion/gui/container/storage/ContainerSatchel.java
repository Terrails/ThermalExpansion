package cofh.thermalexpansion.gui.container.storage;

import cofh.api.core.ISecurable;
import cofh.core.gui.container.ContainerInventoryItem;
import cofh.core.gui.slot.ISlotValidator;
import cofh.core.gui.slot.SlotLocked;
import cofh.core.gui.slot.SlotValidated;
import cofh.core.network.PacketCore;
import cofh.core.util.CoreUtils;
import cofh.core.util.helpers.MathHelper;
import cofh.core.util.helpers.SecurityHelper;
import cofh.core.util.helpers.StringHelper;
import cofh.thermalexpansion.gui.slot.SlotSatchelCreative;
import cofh.thermalexpansion.gui.slot.SlotSatchelVoid;
import cofh.thermalexpansion.item.ItemSatchel;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerSatchel extends ContainerInventoryItem implements ISecurable, ISlotValidator {

	static final String NAME = "item.thermalexpansion.satchel.name";

	boolean isCreative;
	boolean isVoid;

	int storageIndex;
	int rowSize;

	public ContainerSatchel(ItemStack stack, InventoryPlayer inventory) {

		super(stack, inventory);

		isCreative = ItemSatchel.isCreative(stack);
		isVoid = ItemSatchel.isVoid(stack);

		storageIndex = ItemSatchel.getStorageIndex(stack);
		rowSize = MathHelper.clamp(storageIndex, 9, 14);

		int rows = MathHelper.clamp(storageIndex, 2, 9);
		int slots = rowSize * rows;
		int yOffset = 17;

		bindPlayerInventory(inventory);

		switch (storageIndex) {
			case 0:
				addSlotToContainer(isVoid ? new SlotSatchelVoid(containerWrapper, 0, 80, 26) : new SlotSatchelCreative(this, containerWrapper, 0, 80, 26));
				rowSize = 1;
				break;
			case 1:
				yOffset += 9;
				for (int i = 0; i < 9; i++) {
					addSlotToContainer(new SlotValidated(this, containerWrapper, i, 8 + i % rowSize * 18, yOffset + i / rowSize * 18));
				}
				break;
			default:
				for (int i = 0; i < slots; i++) {
					addSlotToContainer(new SlotValidated(this, containerWrapper, i, 8 + i % rowSize * 18, yOffset + i / rowSize * 18));
				}
				break;
		}
	}

	@Override
	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {

		int xOffset = getPlayerInventoryHorizontalOffset();
		int yOffset = getPlayerInventoryVerticalOffset();

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, xOffset + j * 18, yOffset + i * 18));
			}
		}
		for (int i = 0; i < 9; i++) {
			if (i == inventoryPlayer.currentItem) {
				addSlotToContainer(new SlotLocked(inventoryPlayer, i, xOffset + i * 18, yOffset + 58));
			} else {
				addSlotToContainer(new Slot(inventoryPlayer, i, xOffset + i * 18, yOffset + 58));
			}
		}
	}

	@Override
	public String getInventoryName() {

		return containerWrapper.hasCustomName() ? containerWrapper.getName() : StringHelper.localize(NAME);
	}

	@Override
	protected int getPlayerInventoryVerticalOffset() {

		return 30 + 18 * MathHelper.clamp(storageIndex, 2, 9);
	}

	@Override
	protected int getPlayerInventoryHorizontalOffset() {

		return 8 + 9 * (rowSize - 9);
	}

	/* ISecurable */
	@Override
	public boolean setAccess(AccessMode access) {

		if (SecurityHelper.setAccess(getContainerStack(), access)) {
			onSlotChanged();
			if (CoreUtils.isClient()) {
				PacketCore.sendSecurityPacketToServer(this);
			}
			return true;
		}
		return false;
	}

	@Override
	public AccessMode getAccess() {

		return SecurityHelper.getAccess(getContainerStack());
	}

	@Override
	public String getOwnerName() {

		return SecurityHelper.getOwnerName(getContainerStack());
	}

	@Override
	public GameProfile getOwner() {

		return SecurityHelper.getOwner(getContainerStack());
	}

	@Override
	public boolean canPlayerAccess(EntityPlayer player) {

		throw new UnsupportedOperationException();
	}

	@Override
	public boolean setOwnerName(String name) {

		throw new UnsupportedOperationException();
	}

	@Override
	public boolean setOwner(GameProfile name) {

		throw new UnsupportedOperationException();
	}

	/* ISlotValidator */
	@Override
	public boolean isItemValid(ItemStack stack) {

		return containerWrapper.isItemValidForSlot(0, stack);
	}

}
