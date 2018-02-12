
package com.avrgaming.civcraft.command.civ;

import java.util.Iterator;
import java.util.TreeSet;
import org.bukkit.entity.Player;
import com.avrgaming.civcraft.command.CommandBase;
import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.config.ConfigLevelTalent;
import com.avrgaming.civcraft.interactive.InteractiveTalentConfirmation;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.object.Buff;
import com.avrgaming.civcraft.object.Civilization;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.util.CivColor;

public class CivTalentCommand extends CommandBase {
    protected void addBuffToTown(Town town, String id) {
        try {
            town.getBuffManager().addBuff(id, id, "\u041a\u0430\u043f\u0438\u0442\u043e\u043b\u0438\u0439 in " + town.getName());
        }
        catch (CivException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {
        this.command = "/civ talent";
        this.displayName = CivSettings.localize.localizedString("cmd_civ_talent_name");
        this.commands.put("list", CivSettings.localize.localizedString("cmd_civ_talent_listDesc"));
        this.commands.put("choose", CivSettings.localize.localizedString("cmd_civ_talent_chooseDesc"));
        this.commands.put("next", CivSettings.localize.localizedString("cmd_civ_talent_nextDesc"));
        this.commands.put("info", CivSettings.localize.localizedString("cmd_civ_talent_infoDesc"));
    }

    public void info_cmd() {
        CivMessage.send((Object)this.sender, "\u00a7a" + CivSettings.localize.localizedString("cmd_civ_talent_info_link"));
    }

    public void next_cmd() throws CivException {
        Resident sender = this.getResident();
        Civilization civ = this.getSenderCiv();
        Town capitol = civ.getCapitol();
        if (capitol == null) {
            return;
        }
        if (capitol.getCultureLevel() > 10) {
            throw new CivException("\u00a7c" + CivSettings.localize.localizedString("cmd_civ_talent_next_ended"));
        }
        ConfigLevelTalent configLevelTalent = CivSettings.talentLevels.get(capitol.getCultureLevel() + 1);
        CivMessage.sendHeading(sender, configLevelTalent.levelName + " (" + configLevelTalent.level + ")");
        CivMessage.send((Object)sender, "\u00a7c" + configLevelTalent.levelBuffDesc1);
        CivMessage.send((Object)sender, "\u00a7a" + configLevelTalent.levelBuffDesc2);
        CivMessage.send((Object)sender, "\u00a7b" + configLevelTalent.levelBuffDesc3);
    }

    public void list_cmd() throws CivException {
        Town capitol = this.getSenderCiv().getCapitol();
        boolean has = false;
        if (capitol == null) {
            return;
        }
        TreeSet<String> talents = new TreeSet<String>();
        for (Buff buff : capitol.getBuffManager().getAllBuffs()) {
            if (!buff.getId().contains("level")) continue;
            int talentLevel = Integer.parseInt(buff.getId().replaceAll("[^\\d]", ""));
            ConfigLevelTalent configLevelTalent = CivSettings.talentLevels.get(talentLevel);
            String description = "\u0421\u043b\u043e\u043c\u0430\u043b\u043e\u0441\u044c =(";
            if (configLevelTalent.levelBuff1.equals(buff.getId())) {
                description = configLevelTalent.levelBuffDesc1;
            } else if (configLevelTalent.levelBuff2.equals(buff.getId())) {
                description = configLevelTalent.levelBuffDesc2;
            } else if (configLevelTalent.levelBuff3.equals(buff.getId())) {
                description = configLevelTalent.levelBuffDesc3;
            }
            talents.add("\u00a7a" + configLevelTalent.level + CivColor.RESET + " (" + CivColor.GoldBold + configLevelTalent.levelName + CivColor.RESET + ")" + CivColor.LightBlueBold + ": " + "\u00a73" + description);
            has = true;
        }
        if (!has) {
            throw new CivException(CivSettings.localize.localizedString("cmd_civ_talent_list_noOne"));
        }
        Civilization civ = this.getSenderCiv();
        CivMessage.sendHeading(this.sender, CivSettings.localize.localizedString("cmd_civ_talent_list_heading", civ.getName()));
        Iterator<String> iter = talents.iterator();
        while (iter.hasNext()) {
            CivMessage.send((Object)this.sender, (String)iter.next());
        }
    }

    public void choose_cmd() throws CivException {
        Resident sender = this.getResident();
        Player player = this.getPlayer();
        Civilization civ = this.getSenderCiv();
        Town capitol = civ.getCapitol();
        if (capitol == null) {
            return;
        }
        if (capitol.getCultureLevel() > 10) {
            throw new CivException("\u00a7c" + CivSettings.localize.localizedString("cmd_civ_talent_choose_ended"));
        }
        if (civ.isTalentIsUsed() && capitol.getCultureLevel() >= 10) {
            throw new CivException("\u00a7c" + CivSettings.localize.localizedString("cmd_civ_talent_choose_ended"));
        }
        ConfigLevelTalent configLevelTalent = CivSettings.talentLevels.get(capitol.getCultureLevel());
        Integer talentChoosen = 999;
        if (this.args.length < 2) {
            CivMessage.send((Object)sender, "\u00a7d" + CivSettings.localize.localizedString("cmd_civ_talent_choose_chooseOne"));
            CivMessage.sendHeading(sender, configLevelTalent.levelName + " (" + configLevelTalent.level + ")");
            CivMessage.send((Object)sender, "\u00a7c1 - " + configLevelTalent.levelBuffDesc1);
            CivMessage.send((Object)sender, "\u00a7a2 - " + configLevelTalent.levelBuffDesc2);
            CivMessage.send((Object)sender, "\u00a7b3 - " + configLevelTalent.levelBuffDesc3);
        } else {
            try {
                talentChoosen = Integer.parseInt(this.args[1]);
            }
            catch (NumberFormatException ignored) {
                throw new CivException(CivSettings.localize.localizedString("cmd_civ_talent_choose_numberformat", this.args[1]));
            }
        }
        InteractiveTalentConfirmation confirmation = null;
        String message = null;
        if (talentChoosen != 999) {
            switch (talentChoosen) {
                case 1: {
                    if (civ.isTalentIsUsed()) {
                        throw new CivException("\u00a7c" + CivSettings.localize.localizedString("cmd_civ_talent_choose_notNow", civ.getCapitol().getName(), civ.getCapitol().getCultureLevel() + 1));
                    }
                    confirmation = new InteractiveTalentConfirmation(civ, this.getPlayer(), configLevelTalent.levelBuff1, CivSettings.localize.localizedString("cmd_civ_talent_choose_sucusses", player.getDisplayName(), talentChoosen, configLevelTalent.levelBuffDesc1, capitol.getCultureLevel()));
                    message = "\u00a7a" + CivSettings.localize.localizedString("cmd_civ_talent_choose_interactiveConfirmationText", new StringBuilder().append(CivColor.GreenBold).append(capitol.getCultureLevel()).append("\u00a7a").toString(), new StringBuilder().append(CivColor.GoldBold).append(configLevelTalent.levelBuffDesc1).append("\u00a7a").toString(), new StringBuilder().append(CivColor.LightBlueBold).append(talentChoosen).append("\u00a7a").toString());
                    break;
                }
                case 2: {
                    if (civ.isTalentIsUsed()) {
                        throw new CivException("\u00a7c" + CivSettings.localize.localizedString("cmd_civ_talent_choose_notNow", civ.getCapitol().getName(), civ.getCapitol().getCultureLevel() + 1));
                    }
                    confirmation = new InteractiveTalentConfirmation(civ, this.getPlayer(), configLevelTalent.levelBuff2, CivSettings.localize.localizedString("cmd_civ_talent_choose_sucusses", player.getDisplayName(), talentChoosen, configLevelTalent.levelBuffDesc2, capitol.getCultureLevel()));
                    message = "\u00a7a" + CivSettings.localize.localizedString("cmd_civ_talent_choose_interactiveConfirmationText", new StringBuilder().append(CivColor.GreenBold).append(capitol.getCultureLevel()).append("\u00a7a").toString(), new StringBuilder().append(CivColor.GoldBold).append(configLevelTalent.levelBuffDesc2).append("\u00a7a").toString(), new StringBuilder().append(CivColor.LightBlueBold).append(talentChoosen).append("\u00a7a").toString());
                    break;
                }
                case 3: {
                    if (civ.isTalentIsUsed()) {
                        throw new CivException("\u00a7c" + CivSettings.localize.localizedString("cmd_civ_talent_choose_notNow", civ.getCapitol().getName(), civ.getCapitol().getCultureLevel() + 1));
                    }
                    confirmation = new InteractiveTalentConfirmation(civ, this.getPlayer(), configLevelTalent.levelBuff3, CivSettings.localize.localizedString("cmd_civ_talent_choose_sucusses", player.getDisplayName(), talentChoosen, configLevelTalent.levelBuffDesc3, capitol.getCultureLevel()));
                    message = "\u00a7a" + CivSettings.localize.localizedString("cmd_civ_talent_choose_interactiveConfirmationText", new StringBuilder().append(CivColor.GreenBold).append(capitol.getCultureLevel()).append("\u00a7a").toString(), new StringBuilder().append(CivColor.GoldBold).append(configLevelTalent.levelBuffDesc3).append("\u00a7a").toString(), new StringBuilder().append(CivColor.LightBlueBold).append(talentChoosen).append("\u00a7a").toString());
                    break;
                }
                default: {
                    CivMessage.send((Object)sender, "\u00a7c" + CivSettings.localize.localizedString("cmd_civ_talent_choose_badInteger", talentChoosen));
                }
            }
        }
        if (confirmation != null && message != null) {
            this.getResident().setInteractiveMode(confirmation);
            CivMessage.sendHeading(sender, CivSettings.localize.localizedString("cmd_civ_talent_choose_interactiveConfirmationHeading"));
            CivMessage.send((Object)sender, message);
            CivMessage.send((Object)sender, "\u00a78" + CivSettings.localize.localizedString("cmd_civ_talent_choose_interactiveConfirmationTypeSth"));
        }
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
}

