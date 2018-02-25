
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

public class BookStructuresGui
implements GuiAction {
    public static Inventory inv = null;

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
        double rate = 1.0;
        rate -= town.getBuffManager().getEffectiveDouble("buff_rush");
        rate -= town.getBuffManager().getEffectiveDouble("buff_grandcanyon_rush");
        rate -= town.getBuffManager().getEffectiveDouble("buff_mother_tree_tile_improvement_cost");
        inv = Bukkit.getServer().createInventory((InventoryHolder)player, 54, CivSettings.localize.localizedString("resident_structuresGuiHeading"));
        for (ConfigBuildableInfo info : CivSettings.structures.values()) {
            ItemStack itemStack;
            int type = ItemManager.getId(Material.EMERALD_BLOCK);
            double hammerCost = Math.round(info.hammer_cost * rate);
            if (town.getMayorGroup() == null || town.getAssistantGroup() == null || civ.getLeaderGroup() == null) {
                itemStack = LoreGuiItem.build(info.displayName, ItemManager.getId(Material.REDSTONE_BLOCK), 0, "\u00a7b" + CivSettings.localize.localizedString("money_requ", Double.parseDouble(String.valueOf(info.cost))), "\u00a7a" + CivSettings.localize.localizedString("hammers_requ", hammerCost), "\u00a7d" + CivSettings.localize.localizedString("upkeep_day", info.upkeep), "\u00a7c" + CivSettings.localize.localizedString("belongtown"));
            } else if (!whoClicked.getCiv().hasTechnology(info.require_tech)) {
                ConfigTech tech = CivSettings.techs.get(info.require_tech);
                String techh = tech.name;
                itemStack = LoreGuiItem.build(info.displayName, ItemManager.getId(Material.REDSTONE), 0, "\u00a7b" + CivSettings.localize.localizedString("money_requ", Double.parseDouble(String.valueOf(info.cost))), "\u00a7a" + CivSettings.localize.localizedString("hammers_requ", hammerCost), "\u00a7d" + CivSettings.localize.localizedString("upkeep_day", info.upkeep), "\u00a7c" + CivSettings.localize.localizedString("req") + tech.name, "\u00a73" + CivSettings.localize.localizedString("clicktoresearch"), "\u00a7d" + CivSettings.localize.localizedString("era_this", tech.era));
                itemStack = LoreGuiItem.setAction(itemStack, "ResearchGui");
                itemStack = LoreGuiItem.setActionData(itemStack, "info", techh);
            } else if (!(town.getMayorGroup().hasMember(whoClicked) || town.getAssistantGroup().hasMember(whoClicked) || civ.getLeaderGroup().hasMember(whoClicked))) {
                itemStack = LoreGuiItem.build(info.displayName, ItemManager.getId(Material.REDSTONE_BLOCK), 0, "\u00a7b" + CivSettings.localize.localizedString("money_requ", Double.parseDouble(String.valueOf(info.cost))), "\u00a7a" + CivSettings.localize.localizedString("hammers_requ", hammerCost), "\u00a7d" + CivSettings.localize.localizedString("upkeep_day", info.upkeep), "\u00a7c" + CivSettings.localize.localizedString("belongtown"));
            } else if (info.isAvailable(town)) {
                if (!info.id.contains("road") && !info.id.contains("wall")) {
                    itemStack = LoreGuiItem.build(info.displayName, type, 0, "\u00a76" + CivSettings.localize.localizedString("clicktobuild"), "\u00a7b" + CivSettings.localize.localizedString("money_requ", Double.parseDouble(String.valueOf(info.cost))), "\u00a7a" + CivSettings.localize.localizedString("hammers_requ", hammerCost), "\u00a7d" + CivSettings.localize.localizedString("upkeep_day", info.upkeep));
                    itemStack = LoreGuiItem.setAction(itemStack, "BuildChooseTemplate");
                    itemStack = LoreGuiItem.setActionData(itemStack, "info", info.id);
                } else {
                    itemStack = LoreGuiItem.build(info.displayName, type, 0, "\u00a76" + CivSettings.localize.localizedString("clicktobuild"), "\u00a7b" + CivSettings.localize.localizedString("money_requ", Double.parseDouble(String.valueOf(info.cost))), "\u00a7a" + CivSettings.localize.localizedString("hammers_requ", hammerCost), "\u00a7d" + CivSettings.localize.localizedString("upkeep_day", info.upkeep));
                    itemStack = LoreGuiItem.setAction(itemStack, "BuildFromIdCr");
                    itemStack = LoreGuiItem.setActionData(itemStack, "buildableName", info.displayName);
                }
            } else {
                ConfigBuildableInfo str = CivSettings.structures.get(info.require_structure);
                if (str != null) {
                    String req_build = str.displayName;
                    itemStack = LoreGuiItem.build(info.displayName, ItemManager.getId(Material.BEDROCK), 0, "\u00a7b" + CivSettings.localize.localizedString("money_requ", Double.parseDouble(String.valueOf(info.cost))), "\u00a7a" + CivSettings.localize.localizedString("hammers_requ", hammerCost), "\u00a7d" + CivSettings.localize.localizedString("upkeep_day", info.upkeep), "\u00a7c" + CivSettings.localize.localizedString("requ") + str.displayName, "\u00a73" + CivSettings.localize.localizedString("clicktobuild"));
                    itemStack = LoreGuiItem.setAction(itemStack, "WonderGuiBuild");
                    itemStack = LoreGuiItem.setActionData(itemStack, "info", req_build);
                } else {
                    itemStack = null;
                }
            }
            if (itemStack == null) continue;
            inv.addItem(itemStack);
        }
        ItemStack backButton = LoreGuiItem.build("Back", ItemManager.getId(Material.MAP), 0, CivSettings.localize.localizedString("bookReborn_backToDashBoard"));
        backButton = LoreGuiItem.setAction(backButton, "OpenInventory");
        backButton = LoreGuiItem.setActionData(backButton, "invType", "showGuiInv");
        backButton = LoreGuiItem.setActionData(backButton, "invName", Book.guiInventory.getName());
        inv.setItem(53, backButton);
        ItemStack wonderButton = LoreGuiItem.build(CivSettings.localize.localizedString("4udesa"), ItemManager.getId(Material.DIAMOND_BLOCK), 0, "\u00a76" + CivSettings.localize.localizedString("click_to_view"));
        wonderButton = LoreGuiItem.setAction(wonderButton, "BookWondersGui");
        inv.setItem(52, wonderButton);
        LoreGuiItemListener.guiInventories.put(inv.getName(), inv);
        TaskMaster.syncTask(new OpenInventoryTask(player, inv));
    }
}

