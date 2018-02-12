
package com.avrgaming.civcraft.command.civ;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import com.avrgaming.civcraft.command.CommandBase;
import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.config.ConfigSpaceMissions;
import com.avrgaming.civcraft.loregui.OpenInventoryTask;
import com.avrgaming.civcraft.lorestorage.LoreGuiItem;
import com.avrgaming.civcraft.lorestorage.LoreGuiItemListener;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.object.Civilization;
import com.avrgaming.civcraft.threading.TaskMaster;
import com.avrgaming.civcraft.util.CivColor;
import com.avrgaming.civcraft.util.ItemManager;

public class CivSpaceCommand
extends CommandBase {
    public static Inventory guiInventory;

    @Override
    public void init() {
        this.command = "/civ space";
        this.displayName = CivSettings.localize.localizedString("cmd_civ_space_name");
        this.commands.put("gui", CivSettings.localize.localizedString("cmd_civ_space_guiDesc"));
        this.commands.put("succusess", CivSettings.localize.localizedString("cmd_civ_space_succusessDesc"));
        this.commands.put("future", CivSettings.localize.localizedString("cmd_civ_space_futureDesc"));
        this.commands.put("progress", CivSettings.localize.localizedString("cmd_civ_space_progressDesc"));
    }

    public void progress_cmd() throws CivException {
        Civilization civ = this.getSenderCiv();
        if (!civ.getMissionActive()) {
            throw new CivException(CivSettings.localize.localizedString("var_spaceshuttle_noProgress"));
        }
        String[] split = civ.getMissionProgress().split(":");
        String missionName = CivSettings.spacemissions_levels.get((Object)Integer.valueOf((int)civ.getCurrentMission())).name;
        double beakers = Math.round(Double.parseDouble(split[0]));
        double hammers = Math.round(Double.parseDouble(split[1]));
        int percentageCompleteBeakers = (int)((double)Math.round(Double.parseDouble(split[0])) / Double.parseDouble(CivSettings.spacemissions_levels.get((Object)Integer.valueOf((int)civ.getCurrentMission())).require_beakers) * 100.0);
        int percentageCompleteHammers = (int)((double)Math.round(Double.parseDouble(split[1])) / Double.parseDouble(CivSettings.spacemissions_levels.get((Object)Integer.valueOf((int)civ.getCurrentMission())).require_hammers) * 100.0);
        String message = "\u00a7b" + missionName + ":" + CivColor.RESET + "\n" + "\u00a76" + "Beakers: " + beakers + "\u00a7c" + " (" + percentageCompleteBeakers + "%)" + "\u00a7d" + "Hammers: " + hammers + "\u00a7c" + " (" + percentageCompleteHammers + "%)";
        throw new CivException(message);
    }

    public void future_cmd() throws CivException {
        Civilization civ = this.getSenderCiv();
        if (civ.getCurrentMission() >= 8) {
            throw new CivException(CivSettings.localize.localizedString("var_spaceshuttle_end", CivSettings.spacemissions_levels.get((Object)Integer.valueOf((int)7)).name));
        }
        int current = civ.getCurrentMission();
        StringBuilder futureMissions = new StringBuilder(CivSettings.localize.localizedString("cmd_space_future")+": \n\u00a7d");
        if (current == 7 && civ.getMissionActive()) {
            throw new CivException(CivSettings.localize.localizedString("var_spaceshuttle_end", CivSettings.spacemissions_levels.get((Object)Integer.valueOf((int)7)).name));
        }
        if (civ.getMissionActive()) {
            ++current;
        }
        for (int i = current; i <= 7; ++i) {
            ConfigSpaceMissions configSpaceMissions = CivSettings.spacemissions_levels.get(i);
            futureMissions.append(configSpaceMissions.name).append("\n");
        }
        throw new CivException(futureMissions.toString());
    }

    public void succusess_cmd() throws CivException {
        Civilization civ = this.getSenderCiv();
        int ended = civ.getCurrentMission();
        StringBuilder endedMissions = new StringBuilder("\u00a7a\u0417\u0430\u0432\u0435\u0440\u0448\u0435\u043d\u043d\u044b\u0435 \u043c\u0438\u0441\u0441\u0438\u0438: \n\u00a76");
        for (int i = 1; i < ended; ++i) {
            ConfigSpaceMissions configSpaceMissions = CivSettings.spacemissions_levels.get(i);
            endedMissions.append(configSpaceMissions.name).append("\n");
        }
        throw new CivException(endedMissions.toString());
    }

    public void gui_cmd() throws CivException {
        this.runGui(this.getPlayer());
    }

    @Override
    public void doDefaultAction() throws CivException {
        this.showHelp();
    }

    @Override
    public void showHelp() {
        this.showBasicHelp();
    }

    @Override
    public void permissionCheck() throws CivException {
        this.validLeader();
    }

    public void runGui(Player player) {
        guiInventory = Bukkit.getServer().createInventory((InventoryHolder)player, 9, CivSettings.localize.localizedString("bookReborn_civSpaceHeading"));
        ItemStack progressButton = LoreGuiItem.build("\u041f\u0440\u043e\u0433\u0440\u0435\u0441\u0441 \u043c\u0438\u0441\u0441\u0438\u0439", ItemManager.getId(Material.MAP), 0, "\u00a76" + CivSettings.localize.localizedString("click_to_view"));
        progressButton = LoreGuiItem.setAction(progressButton, "CivSpaceProgress");
        guiInventory.setItem(0, progressButton);
        ItemStack endedMissionButton = LoreGuiItem.build("\u0417\u0430\u0432\u0435\u0440\u0448\u0435\u043d\u043d\u044b\u0435 \u043c\u0438\u0441\u0441\u0438\u0438", ItemManager.getId(Material.MAP), 0, "\u00a76" + CivSettings.localize.localizedString("click_to_view"));
        endedMissionButton = LoreGuiItem.setAction(endedMissionButton, "CivSpaceEnded");
        guiInventory.setItem(1, endedMissionButton);
        ItemStack futureMissionsButton = LoreGuiItem.build("\u0411\u0443\u0434\u0443\u0449\u0438\u0435 \u043c\u0438\u0441\u0441\u0438\u0438", ItemManager.getId(Material.MAP), 0, "\u00a76" + CivSettings.localize.localizedString("click_to_view"));
        futureMissionsButton = LoreGuiItem.setAction(futureMissionsButton, "CivSpaceFuture");
        guiInventory.setItem(2, futureMissionsButton);
        LoreGuiItemListener.guiInventories.put(guiInventory.getName(), guiInventory);
        TaskMaster.syncTask(new OpenInventoryTask(player, guiInventory));
    }
}

