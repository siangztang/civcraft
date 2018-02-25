
package com.avrgaming.civcraft.loregui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import com.avrgaming.civcraft.command.civ.CivSpaceCommand;
import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.loregui.GuiAction;
import com.avrgaming.civcraft.loregui.OpenInventoryTask;
import com.avrgaming.civcraft.lorestorage.LoreGuiItem;
import com.avrgaming.civcraft.lorestorage.LoreGuiItemListener;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;

import com.avrgaming.civcraft.object.Civilization;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.threading.TaskMaster;
import com.avrgaming.civcraft.util.ItemManager;

public class CivSpaceProgress
implements GuiAction {
    @Override
    public void performAction(InventoryClickEvent event, ItemStack stack) {
        Player player = (Player)event.getWhoClicked();
        Resident interactor = CivGlobal.getResident(player);
        Civilization civ = interactor.getCiv();
        if (!civ.getMissionActive()) {
            CivMessage.sendError((Object)player, CivSettings.localize.localizedString("var_spaceshuttle_noProgress"));
            return;
        }
        Inventory guiInventory = Bukkit.getServer().createInventory((InventoryHolder)player, 9, CivSettings.localize.localizedString("bookReborn_civSpaceProgressHeading"));
        String[] split = civ.getMissionProgress().split(":");
        String missionName = CivSettings.spacemissions_levels.get((Object)Integer.valueOf((int)civ.getCurrentMission())).name;
        double beakers = Math.round(Double.parseDouble(split[0]));
        double hammers = Math.round(Double.parseDouble(split[1]));
        int percentageCompleteBeakers = (int)((double)Math.round(Double.parseDouble(split[0])) / Double.parseDouble(CivSettings.spacemissions_levels.get((Object)Integer.valueOf((int)civ.getCurrentMission())).require_beakers) * 100.0);
        int percentageCompleteHammers = (int)((double)Math.round(Double.parseDouble(split[1])) / Double.parseDouble(CivSettings.spacemissions_levels.get((Object)Integer.valueOf((int)civ.getCurrentMission())).require_hammers) * 100.0);
        ItemStack progress = LoreGuiItem.build("\u00a7b" + missionName, ItemManager.getId(Material.DIAMOND_SWORD), 0, "\u00a76\u041f\u0440\u043e\u0431\u0438\u0440\u043a\u0438: " + beakers + "\u00a7c" + "(" + percentageCompleteBeakers + "%)", "\u00a7d\u041c\u043e\u043b\u043e\u0442\u043e\u0447\u043a\u0438: " + hammers + "\u00a7c" + "(" + percentageCompleteHammers + "%)");
        guiInventory.setItem(0, progress);
        ItemStack backButton = LoreGuiItem.build("\u041d\u0430\u0437\u0430\u0434", ItemManager.getId(Material.MAP), 0, "\u041d\u0430\u0437\u0430\u0434 \u043a '\u0420\u0430\u043a\u0435\u0442\u043e\u0441\u0442\u0440\u043e\u0435\u043d\u0438\u0435'");
        backButton = LoreGuiItem.setAction(backButton, "OpenInventory");
        backButton = LoreGuiItem.setActionData(backButton, "invType", "showGuiInv");
        backButton = LoreGuiItem.setActionData(backButton, "invName", CivSpaceCommand.guiInventory.getName());
        guiInventory.setItem(8, backButton);
        LoreGuiItemListener.guiInventories.put(guiInventory.getName(), guiInventory);
        TaskMaster.syncTask(new OpenInventoryTask(player, guiInventory));
    }
}

