
package com.avrgaming.civcraft.loregui;

import java.sql.SQLException;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.loregui.GuiAction;
import com.avrgaming.civcraft.lorestorage.LoreGuiItem;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.object.Civilization;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.util.CivColor;

public class GiftTradeGood
implements GuiAction {
    @Override
    public void performAction(InventoryClickEvent event, ItemStack stack) throws CivException {
        Player player = (Player)event.getWhoClicked();
        Resident resident = CivGlobal.getResident(player);
        String civilizationName = LoreGuiItem.getActionData(stack, "civilizationName");
        String tradeGoodID = LoreGuiItem.getActionData(stack, "tradeGoodID");
        Civilization from = resident.getCiv();
        Civilization to = CivGlobal.getCiv(civilizationName);
        try {
            to.depositTradeGood(tradeGoodID);
            from.withdrawTradeGood(tradeGoodID);
            to.saveNow();
            from.saveNow();
        }
        catch (CivException e) {
            String message = e.getMessage();
            if (message.contains("\u041d\u0435\u043b\u044c\u0437\u044f \u0438\u043c\u0435\u0442\u044c \u0431\u043e\u043b\u0435\u0435")) {
                message = CivSettings.localize.localizedString("cmd_civ_trade_gift_errorFullSlots");
            }
            CivMessage.sendError((Object)player, message);
            if (e.getMessage().contains("\u0423 \u0432\u0430\u0448\u0435\u0439 \u0446\u0438\u0432\u0438\u043b\u0438\u0437\u0430\u0446\u0438\u0438 \u043d\u0435\u0442")) {
                to.withdrawTradeGood(tradeGoodID);
            }
            try {
                to.saveNow();
            }
            catch (SQLException e1) {
                e1.printStackTrace();
            }
            player.closeInventory();
            return;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        CivMessage.sendCiv(from, CivSettings.localize.localizedString("cmd_civ_trade_gift_succusessMessageFrom", player.getDisplayName(), "\u00a7a" + to.getName() + CivColor.RESET, "\u00a7c" + CivSettings.goods.get((Object)tradeGoodID).name + CivColor.RESET));
        CivMessage.sendCiv(to, CivSettings.localize.localizedString("cmd_civ_trade_gift_succusessMessageTo", player.getDisplayName(), "\u00a7a" + from.getName() + CivColor.RESET, "\u00a7c" + CivSettings.goods.get((Object)tradeGoodID).name + CivColor.RESET));
        player.closeInventory();
    }
}

