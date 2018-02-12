
package com.avrgaming.civcraft.interactive;

import java.sql.SQLException;
import org.bukkit.entity.Player;
import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.interactive.InteractiveResponse;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.object.Civilization;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.util.CivColor;

public class InteractiveTalentConfirmation
implements InteractiveResponse {
    public Civilization target;
    public Player leader;
    public String id;
    public String succesesMessage;

    public InteractiveTalentConfirmation(Civilization target, Player leader, String id, String succesesMessage) {
        this.target = target;
        this.leader = leader;
        this.id = id;
        this.succesesMessage = succesesMessage;
    }

    @Override
    public void respond(String message, Resident resident) {
        if (!(message.equalsIgnoreCase("yes"))) {
            CivMessage.sendError((Object)this.leader, CivSettings.localize.localizedString("interactive_confirmTalent_cancel", CivColor.GoldBold + this.target.getName() + "\u00a7c"));
            resident.clearInteractiveMode();
            return;
        }
        if (this.target.isTalentIsUsed()) {
            CivMessage.sendError((Object)this.leader, CivSettings.localize.localizedString("cmd_civ_talent_choose_notNow", this.target.getCapitol().getName(), this.target.getCapitol().getCultureLevel() + 1));
            return;
        }
        Town capitol = this.target.getCapitol();
        this.addBuffToTown(capitol, this.id);
        this.target.setIsUsedTalent(true);
        capitol.addTalent(this.id);
        try {
            this.target.saveNow();
            capitol.saveNow();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        CivMessage.sendCiv(this.target, this.succesesMessage);
        CivMessage.send((Object)this.leader, "\u00a7a" + CivSettings.localize.localizedString("cmd_civ_talent_choose_sucussesSender"));
    }

    protected void addBuffToTown(Town town, String id) {
        try {
            town.getBuffManager().addBuff(id, id, "\u041a\u0430\u043f\u0438\u0442\u043e\u043b\u0438\u0439 in " + town.getName());
        }
        catch (CivException e) {
            e.printStackTrace();
        }
    }
}

