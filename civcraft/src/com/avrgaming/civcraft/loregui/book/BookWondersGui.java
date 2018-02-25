
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
import com.avrgaming.civcraft.config.ConfigTech;
import com.avrgaming.civcraft.config.ConfigTownUpgrade;
import com.avrgaming.civcraft.loregui.GuiAction;
import com.avrgaming.civcraft.loregui.OpenInventoryTask;
import com.avrgaming.civcraft.loregui.book.BookStructuresGui;
import com.avrgaming.civcraft.lorestorage.LoreGuiItem;
import com.avrgaming.civcraft.lorestorage.LoreGuiItemListener;
import com.avrgaming.civcraft.main.CivGlobal;

import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.structure.wonders.Wonder;
import com.avrgaming.civcraft.threading.TaskMaster;
import com.avrgaming.civcraft.util.CivColor;
import com.avrgaming.civcraft.util.ItemManager;

public class BookWondersGui
implements GuiAction {
    @Override
    public void performAction(InventoryClickEvent event, ItemStack stack) {
        Player player = (Player)event.getWhoClicked();
        Resident res = CivGlobal.getResident(player);
        Inventory inv = Bukkit.getServer().createInventory((InventoryHolder)player, 54, CivSettings.localize.localizedString("resident_wonersGuiHeading"));
        CivSettings.wonders.values().stream().map(info -> {
            ItemStack is;
            double cost = info.cost;
            if (res.getCiv().getCapitol() != null && res.getCiv().getCapitol().getBuffManager().hasBuff("level10_architectorTown")) {
                cost *= 0.9;
            }
            if (!res.getTown().hasTechnology(info.require_tech)) {
                ConfigTech tech = CivSettings.techs.get(info.require_tech);
                is = LoreGuiItem.build(info.displayName, ItemManager.getId(Material.GOLD_INGOT), 0, "\u00a7b" + CivSettings.localize.localizedString("money_requ", cost), "\u00a7a" + CivSettings.localize.localizedString("hammers_requ", info.hammer_cost), "\u00a7d" + CivSettings.localize.localizedString("ppoints", info.points), "\u00a7c" + CivSettings.localize.localizedString("req") + tech.name, "\u00a73" + CivSettings.localize.localizedString("clicktoresearch"));
                is = LoreGuiItem.setAction(is, "ResearchGui");
                is = LoreGuiItem.setActionData(is, "info", tech.name);
            } else if (!res.getSelectedTown().hasUpgrade(info.require_upgrade)) {
                ConfigTownUpgrade upgrade = CivSettings.townUpgrades.get(info.require_upgrade);
                is = LoreGuiItem.build(info.displayName, ItemManager.getId(Material.BLAZE_ROD), 0, "\u00a7b" + CivSettings.localize.localizedString("money_requ", cost), "\u00a7a" + CivSettings.localize.localizedString("hammers_requ", info.hammer_cost), "\u00a7d" + CivSettings.localize.localizedString("ppoints", info.points), "\u00a7c" + CivSettings.localize.localizedString("req") + upgrade.name, "\u00a73" + CivSettings.localize.localizedString("clicktoupgrade"));
                is = LoreGuiItem.setAction(is, "UpgradeGuiBuy");
                is = LoreGuiItem.setActionData(is, "info", upgrade.name);
            } else if (!res.getSelectedTown().hasStructure(info.require_structure)) {
                ConfigBuildableInfo structure = CivSettings.structures.get(info.require_structure);
                is = LoreGuiItem.build(info.displayName, ItemManager.getId(Material.EMERALD), 0, "\u00a7b" + CivSettings.localize.localizedString("money_requ", cost), "\u00a7a" + CivSettings.localize.localizedString("hammers_requ", info.hammer_cost), "\u00a7d" + CivSettings.localize.localizedString("ppoints", info.points), "\u00a7c" + CivSettings.localize.localizedString("requ") + structure.displayName, "\u00a73" + CivSettings.localize.localizedString("clicktobuild"));
                is = LoreGuiItem.setAction(is, "WonderGuiBuild");
                is = LoreGuiItem.setActionData(is, "info", structure.displayName);
            } else if (!info.isAvailable(res.getTown())) {
                is = LoreGuiItem.build(info.displayName, ItemManager.getId(Material.DIAMOND), 0, "\u00a7b" + CivSettings.localize.localizedString("money_requ", cost), "\u00a7a" + CivSettings.localize.localizedString("hammers_requ", info.hammer_cost), "\u00a7d" + CivSettings.localize.localizedString("ppoints", info.points), "\u042d\u0442\u043e \u0447\u0443\u0434\u043e \u0441\u0435\u0439\u0447\u0430\u0441 \u043d\u0435\u0434\u043e\u0441\u0442\u0443\u043f\u043d\u043e.");
            } else if (!Wonder.isWonderAvailable(info.id)) {
                is = LoreGuiItem.build(info.displayName, ItemManager.getId(Material.DIAMOND_SWORD), 0, "\u00a7b" + CivSettings.localize.localizedString("money_requ", cost), "\u00a7a" + CivSettings.localize.localizedString("hammers_requ", info.hammer_cost), "\u00a7d" + CivSettings.localize.localizedString("ppoints", info.points), "\u00a7c\u042d\u0442\u043e \u0447\u0443\u0434\u043e \u0443\u0436\u0435 \u043f\u043e\u0441\u0442\u0440\u043e\u0435\u043d\u043e \u0443 \u0432\u0430\u0441 \u0432 \u0433\u0434\u0435-\u0442\u043e \u0432 " + CivColor.GoldBold + "\u041c\u0418\u0420\u0415!");
            } else {
                is = LoreGuiItem.build(info.displayName, ItemManager.getId(Material.DIAMOND_BLOCK), 0, "\u00a76" + CivSettings.localize.localizedString("clicktobuild"), "\u00a7b" + CivSettings.localize.localizedString("money_requ", cost), "\u00a7a" + CivSettings.localize.localizedString("hammers_requ", info.hammer_cost), "\u00a7d" + CivSettings.localize.localizedString("ppoints", info.points));
                is = LoreGuiItem.setAction(is, "WonderGuiBuild");
                is = LoreGuiItem.setActionData(is, "info", info.displayName);
            }
            return is;
        }
        ).forEachOrdered(is -> {
            inv.addItem(new ItemStack[]{is});
        }
        );
        ItemStack backButton = LoreGuiItem.build(CivSettings.localize.localizedString("bookReborn_back"), ItemManager.getId(Material.MAP), 0, CivSettings.localize.localizedString("bookReborn_backTo", BookStructuresGui.inv.getName()));
        backButton = LoreGuiItem.setAction(backButton, "OpenInventory");
        backButton = LoreGuiItem.setActionData(backButton, "invType", "showGuiInv");
        backButton = LoreGuiItem.setActionData(backButton, "invName", BookStructuresGui.inv.getName());
        inv.setItem(53, backButton);
        LoreGuiItemListener.guiInventories.put(inv.getName(), inv);
        TaskMaster.syncTask(new OpenInventoryTask(player, inv));
    }
}

