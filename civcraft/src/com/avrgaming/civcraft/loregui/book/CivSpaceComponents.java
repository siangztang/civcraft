
package com.avrgaming.civcraft.loregui.book;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.config.ConfigSpaceRocket;
import com.avrgaming.civcraft.loregui.GuiAction;
import com.avrgaming.civcraft.loregui.OpenInventoryTask;
import com.avrgaming.civcraft.loregui.book.BookCivSpaceEnded;
import com.avrgaming.civcraft.loregui.book.BookCivSpaceFuture;
import com.avrgaming.civcraft.lorestorage.LoreCraftableMaterial;
import com.avrgaming.civcraft.lorestorage.LoreGuiItem;
import com.avrgaming.civcraft.lorestorage.LoreGuiItemListener;

import com.avrgaming.civcraft.threading.TaskMaster;
import com.avrgaming.civcraft.util.ItemManager;

public class CivSpaceComponents
implements GuiAction {
    @Override
    public void performAction(InventoryClickEvent event, ItemStack stack) {
        Player player = (Player)event.getWhoClicked();
        Inventory guiInventory = Bukkit.getServer().createInventory((InventoryHolder)player, 9, CivSettings.localize.localizedString("bookReborn_civSpaceComponentsHeading"));
        int i = Integer.valueOf(LoreGuiItem.getActionData(stack, "i"));
        boolean fromEnded = Boolean.valueOf(LoreGuiItem.getActionData(stack, "b"));
        ConfigSpaceRocket configSpaceRocket = CivSettings.spaceRocket_name.get(i);
        for (String craftMatID : configSpaceRocket.components.split(":")) {
            int count = Integer.parseInt(craftMatID.replaceAll("[^\\d]", ""));
            String craftMat = craftMatID.replace(String.valueOf(count), "");
            LoreCraftableMaterial itemToGetName = LoreCraftableMaterial.getCraftMaterialFromId(craftMat);
            ItemStack itemStack = LoreGuiItem.build(itemToGetName.getName(), itemToGetName.getConfigMaterial().item_id, itemToGetName.getConfigMaterial().item_data, "\u00a76\u0420\u0430\u043a\u0435\u0442\u043e\u0441\u0442\u0440\u043e\u0435\u043d\u0438\u0435");
            itemStack.setAmount(count);
            guiInventory.addItem(itemStack);
        }
        String backTo = fromEnded ? "'\u0417\u0430\u0432\u0435\u0440\u0448\u0435\u043d\u043d\u044b\u0435 \u043c\u0438\u0441\u0441\u0438'" : "\u0411\u0443\u0434\u0443\u0449\u0438\u0435 \u043c\u0438\u0441\u0441\u0438\u0438";
        ItemStack backButton = LoreGuiItem.build("\u041d\u0430\u0437\u0430\u0434", ItemManager.getId(Material.MAP), 0, "\u041d\u0430\u0437\u0430\u0434 \u043a " + backTo);
        backButton = LoreGuiItem.setAction(backButton, "OpenInventory");
        backButton = LoreGuiItem.setActionData(backButton, "invType", "showGuiInv");
        backButton = LoreGuiItem.setActionData(backButton, "invName", fromEnded ? BookCivSpaceEnded.guiInventory.getName() : BookCivSpaceFuture.guiInventory.getName());
        guiInventory.setItem(8, backButton);
        LoreGuiItemListener.guiInventories.put(guiInventory.getName(), guiInventory);
        TaskMaster.syncTask(new OpenInventoryTask(player, guiInventory));
    }
}

