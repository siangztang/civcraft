
package com.avrgaming.civcraft.loregui.book;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.tutorial.Book;
import com.avrgaming.civcraft.loregui.GuiAction;
import com.avrgaming.civcraft.loregui.OpenInventoryTask;
import com.avrgaming.civcraft.lorestorage.LoreGuiItem;
import com.avrgaming.civcraft.lorestorage.LoreGuiItemListener;
import com.avrgaming.civcraft.threading.TaskMaster;
import com.avrgaming.civcraft.util.ItemManager;

public class BookCivSpace
implements GuiAction {
    public static Inventory guiInventory;

    @Override
    public void performAction(InventoryClickEvent event, ItemStack stack) {
        Player player = (Player)event.getWhoClicked();
        guiInventory = Bukkit.getServer().createInventory((InventoryHolder)player, 9, CivSettings.localize.localizedString("bookReborn_civSpaceHeading"));
        ItemStack progressButton = LoreGuiItem.build("\u041f\u0440\u043e\u0433\u0440\u0435\u0441\u0441 \u043c\u0438\u0441\u0441\u0438\u0439", ItemManager.getId(Material.MAP), 0, "\u00a76" + CivSettings.localize.localizedString("click_to_view"));
        progressButton = LoreGuiItem.setAction(progressButton, "BookCivSpaceProgress");
        guiInventory.setItem(0, progressButton);
        ItemStack endedMissionButton = LoreGuiItem.build("\u0417\u0430\u0432\u0435\u0440\u0448\u0435\u043d\u043d\u044b\u0435 \u043c\u0438\u0441\u0441\u0438\u0438", ItemManager.getId(Material.MAP), 0, "\u00a76" + CivSettings.localize.localizedString("click_to_view"));
        endedMissionButton = LoreGuiItem.setAction(endedMissionButton, "BookCivSpaceEnded");
        guiInventory.setItem(1, endedMissionButton);
        ItemStack futureMissionsButton = LoreGuiItem.build("\u0411\u0443\u0434\u0443\u0449\u0438\u0435 \u043c\u0438\u0441\u0441\u0438\u0438", ItemManager.getId(Material.MAP), 0, "\u00a76" + CivSettings.localize.localizedString("click_to_view"));
        futureMissionsButton = LoreGuiItem.setAction(futureMissionsButton, "BookCivSpaceFuture");
        guiInventory.setItem(2, futureMissionsButton);
        ItemStack backButton = LoreGuiItem.build("\u041d\u0430\u0437\u0430\u0434", ItemManager.getId(Material.MAP), 0, CivSettings.localize.localizedString("bookReborn_backToDashBoard"));
        backButton = LoreGuiItem.setAction(backButton, "OpenInventory");
        backButton = LoreGuiItem.setActionData(backButton, "invType", "showGuiInv");
        backButton = LoreGuiItem.setActionData(backButton, "invName", Book.guiInventory.getName());
        guiInventory.setItem(8, backButton);
        LoreGuiItemListener.guiInventories.put(guiInventory.getName(), guiInventory);
        TaskMaster.syncTask(new OpenInventoryTask(player, guiInventory));
    }
}

