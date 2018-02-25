
package com.avrgaming.civcraft.loregui.book;

import java.text.SimpleDateFormat;
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
import com.avrgaming.civcraft.main.CivGlobal;

import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.threading.TaskMaster;
import com.avrgaming.civcraft.util.CivColor;
import com.avrgaming.civcraft.util.ItemManager;

public class BookResidentGui
implements GuiAction {
    public static String Civilization(Resident resident) {
        if (resident.getCiv() == null) {
            return "";
        }
        return resident.getCiv().getName();
    }

    public static String Town(Resident resident) {
        if (resident.getTown() == null) {
            return "";
        }
        return resident.getTown().getName();
    }

    public static String Camp(Resident resident) {
        if (resident.getCamp() == null) {
            return "";
        }
        return resident.getCamp().getName();
    }

    @Override
    public void performAction(InventoryClickEvent event, ItemStack stack) {
        SimpleDateFormat sdf = new SimpleDateFormat("M/dd h:mm:ss a z");
        Player player = (Player)event.getWhoClicked();
        Resident resident = CivGlobal.getResident(player);
        Inventory guiInventory = Bukkit.getServer().createInventory((InventoryHolder)player, 9, CivSettings.localize.localizedString("bookReborn_resInfoHeading"));
        ItemStack playerInfo = LoreGuiItem.build(CivSettings.localize.localizedString("bookReborn_infoMenu_name"), ItemManager.getId(Material.SKULL_ITEM), 3, CivColor.LightGray + "\u0418\u0433\u0440\u043e\u043a: " + resident.getName(), "\u00a76" + CivSettings.CURRENCY_NAME + ": " + "\u00a7a" + resident.getTreasury().getBalance(), "\u00a72" + CivSettings.localize.localizedString("cmd_res_showRegistrationDate", new StringBuilder().append("\u00a7a").append(sdf.format(resident.getRegistered())).toString()), "\u00a7b" + CivSettings.localize.localizedString("Civilization") + " " + BookResidentGui.Civilization(resident), "\u00a7d" + CivSettings.localize.localizedString("Town") + " " + BookResidentGui.Town(resident), "\u00a7c" + CivSettings.localize.localizedString("Camp") + BookResidentGui.Camp(resident));
        guiInventory.setItem(0, playerInfo);
        ItemStack backButton = LoreGuiItem.build("\u041d\u0430\u0437\u0430\u0434", ItemManager.getId(Material.MAP), 0, CivSettings.localize.localizedString("bookReborn_backToDashBoard"));
        backButton = LoreGuiItem.setAction(backButton, "OpenInventory");
        backButton = LoreGuiItem.setActionData(backButton, "invType", "showGuiInv");
        backButton = LoreGuiItem.setActionData(backButton, "invName", Book.guiInventory.getName());
        guiInventory.setItem(8, backButton);
        LoreGuiItemListener.guiInventories.put(guiInventory.getName(), guiInventory);
        TaskMaster.syncTask(new OpenInventoryTask(player, guiInventory));
    }
}

