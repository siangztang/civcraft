
package com.avrgaming.civcraft.loregui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.loregui.GuiAction;
import com.avrgaming.civcraft.loregui.OpenInventoryTask;
import com.avrgaming.civcraft.lorestorage.LoreGuiItem;
import com.avrgaming.civcraft.lorestorage.LoreGuiItemListener;
import com.avrgaming.civcraft.threading.TaskMaster;
import com.avrgaming.civcraft.util.ItemManager;

public class Confirmation
implements GuiAction {
    @Override
    public void performAction(InventoryClickEvent event, ItemStack stack) {
        Inventory inv = Bukkit.createInventory((InventoryHolder)event.getWhoClicked(), (int)27, (String)"\u00a7a\u041f\u043e\u0434\u0442\u0432\u0435\u0440\u0436\u0434\u0435\u043d\u0438\u0435");
        String fields = LoreGuiItem.getActionData(stack, "passFields");
        String action = LoreGuiItem.getActionData(stack, "passAction");
        String confirmText = LoreGuiItem.getActionData(stack, "confirmText");
        ItemStack confirm = LoreGuiItem.build("\u00a7a" + confirmText, ItemManager.getId(Material.EMERALD_BLOCK), 0, new String[0]);
        confirm = LoreGuiItem.setAction(confirm, action);
        for (String field : fields.split(",")) {
            confirm = LoreGuiItem.setActionData(confirm, field, LoreGuiItem.getActionData(stack, field));
        }
        inv.setItem(11, confirm);
        ItemStack cancel = LoreGuiItem.build("\u00a7c\u041e\u0442\u043c\u0435\u043d\u0430", ItemManager.getId(Material.REDSTONE_BLOCK), 0, new String[0]);
        cancel = LoreGuiItem.setAction(cancel, "CloseInventory");
        inv.setItem(15, cancel);
        LoreGuiItemListener.guiInventories.put(inv.getName(), inv);
        TaskMaster.syncTask(new OpenInventoryTask((Player)event.getWhoClicked(), inv));
    }
}

