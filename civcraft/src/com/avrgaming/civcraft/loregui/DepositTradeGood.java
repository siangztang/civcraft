
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
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.util.CivColor;

public class DepositTradeGood
implements GuiAction {
    @Override
    public void performAction(InventoryClickEvent event, ItemStack stack) throws CivException {
        Player player = (Player)event.getWhoClicked();
        Resident resident = CivGlobal.getResident(player);
        String townName = LoreGuiItem.getActionData(stack, "townName");
        String tradeGoodID = LoreGuiItem.getActionData(stack, "tradeGoodID");
        Civilization from = resident.getCiv();
        Town to = CivGlobal.getTown(townName);
        try {
            to.depositTradeGood(tradeGoodID);
            from.withdrawTradeGood(tradeGoodID);
            to.saveNow();
            from.saveNow();
        }
        catch (CivException e) {
            String message = e.getMessage();
            CivMessage.sendError((Object)player, message);
            if (message.contains("\u0423 \u0432\u0430\u0448\u0435\u0439 \u0446\u0438\u0432\u0438\u043b\u0438\u0437\u0430\u0446\u0438\u0438 \u043d\u0435\u0442")) {
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
        CivMessage.sendCiv(from, CivSettings.localize.localizedString("cmd_civ_trade_deposit_succusessMessageFrom", player.getDisplayName(), "\u00a7a" + to.getName() + CivColor.RESET, "\u00a7c" + CivSettings.goods.get((Object)tradeGoodID).name + CivColor.RESET));
        CivMessage.sendTown(to, "\u00a7e" + CivSettings.localize.localizedString("cmd_civ_trade_deposit_succusessMessageFrom", new StringBuilder().append(player.getDisplayName()).append("\u00a7e").toString(), new StringBuilder().append("\u00a7a").append(from.getName()).append("\u00a7e").toString(), new StringBuilder().append("\u00a7c").append(CivSettings.goods.get((Object)tradeGoodID).name).append("\u00a7e").toString()));
        player.closeInventory();
    }
}

