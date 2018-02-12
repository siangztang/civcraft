
package com.avrgaming.civcraft.items.units;

import java.util.Random;
import gpl.AttributeUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.config.ConfigUnit;
import com.avrgaming.civcraft.items.units.Unit;
import com.avrgaming.civcraft.items.units.UnitMaterial;
import com.avrgaming.civcraft.lorestorage.LoreMaterial;
import com.avrgaming.civcraft.main.CivCraft;
import com.avrgaming.civcraft.main.CivMessage;

import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.util.CivColor;
import com.avrgaming.civcraft.war.War;

public class Knight
extends UnitMaterial {
    public Knight(String id, ConfigUnit configUnit) {
        super(id, configUnit);
    }

    public static void spawn(Inventory inv, Town town) throws CivException {
        ItemStack is = LoreMaterial.spawn(Unit.KNIGHT_ARTIFACT);
        UnitMaterial.setOwningTown(town, is);
        AttributeUtil attrs = new AttributeUtil(is);
        attrs.addEnhancement("LoreEnhancementSoulBound", null, null);
        attrs.addLore("\u00a76Soulbound");
        attrs.addLore("\u00a7b\u0423\u0440\u043e\u0432\u0435\u043d\u044c 3");
        attrs.addLore(CivColor.LightGray + "\u042d\u0444\u0444\u0435\u043a\u0442:");
        attrs.addLore(CivColor.LightGray + "\u041f\u0430\u0441\u0441\u0438\u0432\u043d\u044b\u0439");
        attrs.addLore(CivColor.LightGray + "+10% \u041a \u0430\u0442\u0430\u043a\u0435 \u043c\u0435\u0447\u0430");
        attrs.addLore(CivColor.LightGray + "+20% \u041a \u0430\u0442\u0430\u043a\u0435 \u043b\u0443\u043a\u0430");
        attrs.addLore(CivColor.LightGray + "+45% \u041a \u0431\u0440\u043e\u043d\u0435");
        is = attrs.getStack();
        if (!Unit.addItemNoStack(inv, is)) {
            throw new CivException(CivSettings.localize.localizedString("var_arrtifacts_errorBarracksFull", Unit.KNIGHT_ARTIFACT.getUnit().name));
        }
    }

    @Override
    public void onPlayerDeath(EntityDeathEvent event, ItemStack stack) {
        Player player = (Player)event.getEntity();
        Random random = CivCraft.civRandom;
        int destroyChance = random.nextInt(100);
        if (War.isWarTime()) {
            if (5 < destroyChance) {
                this.removeChildren(player.getInventory());
                CivMessage.send((Object)player, CivColor.RoseBold + CivSettings.localize.localizedString("var_arrtifacts_Break", destroyChance));
            } else {
                CivMessage.send((Object)player, CivColor.YellowBold + CivSettings.localize.localizedString("var_arrtifacts_Keept_Since_WarTime_No_Rolled"));
            }
        } else {
            CivMessage.send((Object)player, CivColor.LightBlueBold + CivSettings.localize.localizedString("var_arrtifacts_Keept_Since_No_WarTime"));
        }
    }
}

