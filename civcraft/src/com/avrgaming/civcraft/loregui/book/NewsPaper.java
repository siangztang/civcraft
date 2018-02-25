
package com.avrgaming.civcraft.loregui.book;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.config.ConfigNewspaper;
import com.avrgaming.civcraft.tutorial.Book;
import com.avrgaming.civcraft.loregui.GuiAction;
import com.avrgaming.civcraft.loregui.OpenInventoryTask;
import com.avrgaming.civcraft.lorestorage.LoreGuiItem;
import com.avrgaming.civcraft.lorestorage.LoreGuiItemListener;
import com.avrgaming.civcraft.main.CivCraft;

import com.avrgaming.civcraft.threading.TaskMaster;
import com.avrgaming.civcraft.util.CivColor;
import com.avrgaming.civcraft.util.ItemManager;

public class NewsPaper
implements GuiAction {
    static Inventory guiInventory;

    @Override
    public void performAction(InventoryClickEvent event, ItemStack stack) {
        ItemStack is;
        Player player = (Player)event.getWhoClicked();
        guiInventory = Bukkit.getServer().createInventory((InventoryHolder)player, 27, CivColor.LightGreenBold + CivSettings.localize.localizedString("bookReborn_news_heading"));
        for (int i = 0; i < 27; ++i) {
            Random rand = CivCraft.civRandom;
            is = LoreGuiItem.build("", ItemManager.getId(Material.STAINED_GLASS_PANE), rand.nextInt(15), new String[0]);
            guiInventory.setItem(i, is);
        }
        for (ConfigNewspaper news : CivSettings.newspapers.values()) {
            boolean useAllLines;
            try {
                Double version = Double.valueOf(news.version);
                useAllLines = version <= 2.2;
            }
            catch (NumberFormatException twoFourFive) {
                useAllLines = false;
            }
            is = useAllLines ? LoreGuiItem.build(CivColor.WhiteBold + news.headline + " " + CivColor.WhiteBold + news.lineotd, news.item, news.iData, CivColor.LightGrayItalic + news.date, CivColor.LightGreenBold + "Aura:", "\u00a7f" + news.line1, "\u00a7f" + news.line2, "\u00a7f" + news.line3, "\u00a7bAlcor:", "\u00a7f" + news.line4, "\u00a7f" + news.line5, "\u00a7f" + news.line6, CivColor.LightPurpleBold + "Orion:", "\u00a7f" + news.line7, "\u00a7f" + news.line8, "\u00a7f" + news.line9, CivColor.GoldBold + "Tauri:", "\u00a7f" + news.line10, "\u00a7f" + news.line11, "\u00a7f" + news.line12, "\u0412\u0435\u0440\u0441\u0438\u044f \u043f\u043b\u0430\u0433\u0438\u043d\u0430: " + news.version) : LoreGuiItem.build(CivColor.WhiteBold + news.headline + " " + CivColor.WhiteBold + news.lineotd, news.item, news.iData, CivColor.LightGrayItalic + news.date, CivColor.LightGreenBold + "Orion:", "\u00a7f" + news.line7, "\u00a7f" + news.line8, "\u00a7f" + news.line9, CivColor.LightPurpleBold + "Tauri:", "\u00a7f" + news.line10, "\u00a7f" + news.line11, "\u00a7f" + news.line12, "\u0412\u0435\u0440\u0441\u0438\u044f \u043f\u043b\u0430\u0433\u0438\u043d\u0430: " + news.version);
            guiInventory.setItem(news.guiData.intValue(), is);
        }
        ItemStack backButton = LoreGuiItem.build(CivSettings.localize.localizedString("bookReborn_back"), ItemManager.getId(Material.MAP), 0, CivSettings.localize.localizedString("bookReborn_backToDashBoard"));
        backButton = LoreGuiItem.setAction(backButton, "OpenInventory");
        backButton = LoreGuiItem.setActionData(backButton, "invType", "showGuiInv");
        backButton = LoreGuiItem.setActionData(backButton, "invName", Book.guiInventory.getName());
        guiInventory.setItem(26, backButton);
        LoreGuiItemListener.guiInventories.put(guiInventory.getName(), guiInventory);
        TaskMaster.syncTask(new OpenInventoryTask(player, guiInventory));
    }
}

