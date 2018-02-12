// 
// Decompiled by Procyon v0.5.30
// 

package com.avrgaming.civcraft.structure;

import com.avrgaming.civcraft.util.CivColor;
import com.avrgaming.civcraft.util.ItemManager;
import com.avrgaming.civcraft.util.SimpleBlock;
import com.avrgaming.civcraft.util.BlockCoord;
import com.avrgaming.civcraft.object.Resident;
import org.bukkit.event.player.PlayerInteractEvent;
import com.avrgaming.civcraft.object.StructureSign;
import java.util.HashMap;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.main.CivGlobal;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import java.sql.SQLException;
import java.sql.ResultSet;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.object.Town;
import org.bukkit.Location;

public class Pasture2 extends Structure
{
    protected Pasture2(final Location center, final String id, final Town town) throws CivException {
        super(center, id, town);
    }
    
    public Pasture2(final ResultSet rs) throws SQLException, CivException {
        super(rs);
    }
    
    @Override
    public void loadSettings() {
        super.loadSettings();
    }
    
    public String getkey() {
        return this.getTown().getName() + "_" + this.getConfigId() + "_" + this.getCorner().toString();
    }
    
    @Override
    public String getDynmapDescription() {
        return null;
    }
    
    @Override
    public String getMarkerIconName() {
        return "pin";
    }
    
    public void buyItem(final int id, final Player player) {
        final ItemStack stack = new ItemStack(id, 64, (short)0);
        final HashMap<Integer, ItemStack> leftovers = (HashMap<Integer, ItemStack>)player.getInventory().addItem(new ItemStack[] { stack });
        for (final ItemStack itemStack : leftovers.values()) {
            player.getWorld().dropItem(player.getLocation(), itemStack);
        }
        CivMessage.sendSuccess(CivGlobal.getResident(player), CivSettings.localize.localizedString("var_pasture2_Succusess"));
        player.updateInventory();
    }
    
    @Override
    public void processSignAction(final Player player, final StructureSign sign, final PlayerInteractEvent event) throws CivException {
        final Resident resident = CivGlobal.getResident(player);
        if (resident == null) {
            return;
        }
        Boolean hasPermission = false;
        if (resident.getCiv() == this.getCiv()) {
            hasPermission = true;
        }
        final String action = sign.getAction();
        switch (action) {
            case "buy319": {
                if (!hasPermission) {
                    throw new CivException("§c" + CivSettings.localize.localizedString("var_pasture2_noPerms", this.getCiv().getName()));
                }
                if (!resident.getTreasury().hasEnough(756.0)) {
                    final double reaming = 756.0 - resident.getTreasury().getBalance();
                    throw new CivException("§c" + CivSettings.localize.localizedString("var_pasture2_noMoney", "§a" + reaming, "\u041c\u043e\u043d\u0435\u0442\u044b"));
                }
                this.buyItem(319, player);
                resident.getTreasury().withdraw(756.0);
                break;
            }
            case "buy363": {
                if (!hasPermission) {
                    throw new CivException("§c" + CivSettings.localize.localizedString("var_pasture2_noPerms", this.getCiv().getName()));
                }
                if (!resident.getTreasury().hasEnough(756.0)) {
                    final double reaming = 756.0 - resident.getTreasury().getBalance();
                    throw new CivException("§c" + CivSettings.localize.localizedString("var_pasture2_noMoney", "§a" + reaming, "\u041c\u043e\u043d\u0435\u0442\u044b"));
                }
                this.buyItem(363, player);
                resident.getTreasury().withdraw(756.0);
                break;
            }
            case "buy411": {
                if (!hasPermission) {
                    throw new CivException("§c" + CivSettings.localize.localizedString("var_pasture2_noPerms", this.getCiv().getName()));
                }
                if (!resident.getTreasury().hasEnough(756.0)) {
                    final double reaming = 756.0 - resident.getTreasury().getBalance();
                    throw new CivException("§c" + CivSettings.localize.localizedString("var_pasture2_noMoney", "§a" + reaming, "\u041c\u043e\u043d\u0435\u0442\u044b"));
                }
                this.buyItem(365, player);
                resident.getTreasury().withdraw(756.0);
                break;
            }
            case "buy423": {
                if (!hasPermission) {
                    throw new CivException("§c" + CivSettings.localize.localizedString("var_pasture2_noPerms", this.getCiv().getName()));
                }
                if (!resident.getTreasury().hasEnough(756.0)) {
                    final double reaming = 756.0 - resident.getTreasury().getBalance();
                    throw new CivException("§c" + CivSettings.localize.localizedString("var_pasture2_noMoney", "§a" + reaming, "\u041c\u043e\u043d\u0435\u0442\u044b"));
                }
                this.buyItem(423, player);
                resident.getTreasury().withdraw(756.0);
                break;
            }
        }
    }
    
    @Override
    public void onPostBuild(final BlockCoord absCoord, final SimpleBlock commandBlock) {
        final String command = commandBlock.command;
        switch (command) {
            case "/buy319": {
                ItemManager.setTypeId(absCoord.getBlock(), commandBlock.getType());
                ItemManager.setData(absCoord.getBlock(), commandBlock.getData());
                final StructureSign structSign = new StructureSign(absCoord, this);
                structSign.setText("\u041f\u043e\u043a\u0443\u043f\u043a\u0430\n\u0421\u044b\u0440\u0430\u044f \u0441\u0432\u0438\u043d\u0438\u043d\u0430\n§a756 \u041c\u043e\u043d\u0435\u0442\n" + CivColor.RESET + CivColor.UNDERLINE + "64 \u0448\u0442\u0443\u043a\u0438");
                structSign.setDirection(commandBlock.getData());
                structSign.setAction("buy319");
                structSign.update();
                this.addStructureSign(structSign);
                CivGlobal.addStructureSign(structSign);
                break;
            }
            case "/buy363": {
                ItemManager.setTypeId(absCoord.getBlock(), commandBlock.getType());
                ItemManager.setData(absCoord.getBlock(), commandBlock.getData());
                final StructureSign structSign = new StructureSign(absCoord, this);
                structSign.setText("\u041f\u043e\u043a\u0443\u043f\u043a\u0430\n\u0421\u044b\u0440\u0430\u044f \u0433\u043e\u0432\u044f\u0434\u0438\u043d\u0430\n§a756 \u041c\u043e\u043d\u0435\u0442\n" + CivColor.RESET + CivColor.UNDERLINE + "64 \u0448\u0442\u0443\u043a\u0438");
                structSign.setDirection(commandBlock.getData());
                structSign.setAction("buy363");
                structSign.update();
                this.addStructureSign(structSign);
                CivGlobal.addStructureSign(structSign);
                break;
            }
            case "/buy411": {
                ItemManager.setTypeId(absCoord.getBlock(), commandBlock.getType());
                ItemManager.setData(absCoord.getBlock(), commandBlock.getData());
                final StructureSign structSign = new StructureSign(absCoord, this);
                structSign.setText("\u041f\u043e\u043a\u0443\u043f\u043a\u0430\n\u0421\u044b\u0440\u0430\u044f \u043a\u0443\u0440\u0438\u0446\u0430\n§a756 \u041c\u043e\u043d\u0435\u0442\n" + CivColor.RESET + CivColor.UNDERLINE + "64 \u0448\u0442\u0443\u043a\u0438");
                structSign.setDirection(commandBlock.getData());
                structSign.setAction("buy411");
                structSign.update();
                this.addStructureSign(structSign);
                CivGlobal.addStructureSign(structSign);
                break;
            }
            case "/buy423": {
                ItemManager.setTypeId(absCoord.getBlock(), commandBlock.getType());
                ItemManager.setData(absCoord.getBlock(), commandBlock.getData());
                final StructureSign structSign = new StructureSign(absCoord, this);
                structSign.setText("\u041f\u043e\u043a\u0443\u043f\u043a\u0430\n\u0421\u044b\u0440\u0430\u044f \u0431\u0430\u0440\u0430\u043d\u0438\u043d\u0430\n§a756 \u041c\u043e\u043d\u0435\u0442\n" + CivColor.RESET + CivColor.UNDERLINE + "64 \u0448\u0442\u0443\u043a\u0438");
                structSign.setDirection(commandBlock.getData());
                structSign.setAction("buy423");
                structSign.update();
                this.addStructureSign(structSign);
                CivGlobal.addStructureSign(structSign);
                break;
            }
        }
    }
}
