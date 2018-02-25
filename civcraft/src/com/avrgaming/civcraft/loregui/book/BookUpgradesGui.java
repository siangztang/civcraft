
package com.avrgaming.civcraft.loregui.book;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.config.ConfigBuildableInfo;
import com.avrgaming.civcraft.config.ConfigTownUpgrade;
import com.avrgaming.civcraft.tutorial.Book;
import com.avrgaming.civcraft.loregui.GuiAction;
import com.avrgaming.civcraft.loregui.OpenInventoryTask;
import com.avrgaming.civcraft.lorestorage.LoreGuiItem;
import com.avrgaming.civcraft.lorestorage.LoreGuiItemListener;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;

import com.avrgaming.civcraft.object.Civilization;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.threading.TaskMaster;
import com.avrgaming.civcraft.util.ItemManager;

public class BookUpgradesGui
implements GuiAction {
    @Override
    public void performAction(InventoryClickEvent event, ItemStack stack) {
        Player player = (Player)event.getWhoClicked();
        Resident whoClicked = CivGlobal.getResident(player);
        if (whoClicked.getTown() == null) {
            Book.spawnGuiBook(player);
            CivMessage.send((Object)player, "\u00a7c\u0412\u044b \u043d\u0435 \u0441\u043e\u0441\u0442\u043e\u0438\u0442\u0435 \u0432 \u0433\u043e\u0440\u043e\u0434\u0435");
            return;
        }
        Civilization civ = whoClicked.getCiv();
        Town town = whoClicked.getSelectedTown();
        if (!(town.getMayorGroup().hasMember(whoClicked) || town.getAssistantGroup().hasMember(whoClicked) || civ.getLeaderGroup().hasMember(whoClicked))) {
            Book.spawnGuiBook(player);
            CivMessage.send((Object)player, "\u00a7c\u0422\u043e\u043b\u044c\u043a\u043e \u043c\u0435\u0440\u044b \u0438\u043b\u0438 \u043b\u0438\u0434\u0435\u0440\u044b \u043c\u043e\u0433\u0443\u0442 \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442\u044c \u044d\u0442\u043e\u0442 UI.");
            return;
        }
        Inventory inv = Bukkit.getServer().createInventory((InventoryHolder)player, 54, CivSettings.localize.localizedString("resident_upgradesGuiHeading"));
        for (ConfigTownUpgrade upgrade : ConfigTownUpgrade.getAllUpgrades(town)) {
            double cost = upgrade.cost;
            if (town.getCiv().getGovernment().id.equalsIgnoreCase("gov_theocracy")) {
                cost *= 0.9;
            }
            ItemStack is = null;
            if (upgrade.isAvailable(town)) {
                is = LoreGuiItem.build(upgrade.name, ItemManager.getId(Material.EMERALD_BLOCK), 0, "\u00a7b" + CivSettings.localize.localizedString("money_requ", Math.round(cost)), "\u00a76" + CivSettings.localize.localizedString("tutorial_lore_clicktoView"));
                is = LoreGuiItem.setAction(is, "UpgradeGuiBuy");
                is = LoreGuiItem.setActionData(is, "info", upgrade.name);
            } else if (!town.hasStructure(upgrade.require_structure)) {
                ConfigBuildableInfo structure = CivSettings.structures.get(upgrade.require_structure);
                is = LoreGuiItem.build(upgrade.name, ItemManager.getId(Material.EMERALD), 0, "\u00a7b" + CivSettings.localize.localizedString("money_requ", Math.round(cost)), "\u00a7c" + CivSettings.localize.localizedString("requ") + structure.displayName, "\u00a73" + CivSettings.localize.localizedString("clicktobuild"));
                is = LoreGuiItem.setAction(is, "WonderGuiBuild");
                is = LoreGuiItem.setActionData(is, "info", structure.displayName);
            } else if (!town.hasUpgrade(upgrade.require_upgrade)) {
                ConfigTownUpgrade upgrade1 = CivSettings.getUpgradeById(upgrade.require_upgrade);
                is = LoreGuiItem.build(upgrade.name, ItemManager.getId(Material.GLOWSTONE_DUST), 0, "\u00a7b" + CivSettings.localize.localizedString("money_requ", Math.round(cost)), "\u00a7c" + CivSettings.localize.localizedString("requ") + upgrade1.name, "\u00a73" + CivSettings.localize.localizedString("tutorial_lore_clicktoView"));
                is = LoreGuiItem.setAction(is, "UpgradeGuiBuy");
                is = LoreGuiItem.setActionData(is, "info", upgrade1.name);
            }
            if (is == null) continue;
            inv.addItem(new ItemStack[]{is});
        }
        ItemStack backButton = LoreGuiItem.build(CivSettings.localize.localizedString("bookReborn_back"), ItemManager.getId(Material.MAP), 0, CivSettings.localize.localizedString("bookReborn_backToDashBoard"));
        backButton = LoreGuiItem.setAction(backButton, "OpenInventory");
        backButton = LoreGuiItem.setActionData(backButton, "invType", "showGuiInv");
        backButton = LoreGuiItem.setActionData(backButton, "invName", Book.guiInventory.getName());
        inv.setItem(53, backButton);
        LoreGuiItemListener.guiInventories.put(inv.getName(), inv);
        TaskMaster.syncTask(new OpenInventoryTask(player, inv));
    }
}

