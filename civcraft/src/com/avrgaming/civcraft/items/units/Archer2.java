
package com.avrgaming.civcraft.items.units;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import gpl.AttributeUtil;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.config.ConfigUnit;
import com.avrgaming.civcraft.items.units.Unit;
import com.avrgaming.civcraft.items.units.UnitMaterial;
import com.avrgaming.civcraft.lorestorage.LoreMaterial;
import com.avrgaming.civcraft.main.CivGlobal;
import com.avrgaming.civcraft.main.CivMessage;
import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.object.Resident;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.util.CivColor;

public class Archer2
extends UnitMaterial {
    public Archer2(String id, ConfigUnit configUnit) {
        super(id, configUnit);
    }

    public static void spawn(Inventory inv, Town town) throws CivException {
        ItemStack is = LoreMaterial.spawn(Unit.ARCHER2_ARTIFACT);
        Archer2.setOwningTown(town, is);
        AttributeUtil attrs = new AttributeUtil(is);
        attrs.addEnhancement("LoreEnhancementSoulBound", null, null);
        attrs.addLore(CivColor.Gold+CivSettings.localize.localizedString("itemLore_Souldbound"));
        attrs.addLore(CivColor.Yellow +"Single Use");
        attrs.addLore(CivColor.LightGray + "\u042d\u0444\u0444\u0435\u043a\u0442:");
        attrs.addLore(CivColor.LightGray + "\u0410\u043a\u0442\u0438\u0432\u0438\u0440\u0443\u0435\u043c\u044b\u0439");
        attrs.addLore(CivColor.LightGray + "\u0417\u0430\u043f\u0440\u0435\u0449\u0430\u0435\u0442 \u0431\u0438\u0442\u044c \u0441 \u0440\u0443\u043a\u0438");
        attrs.addLore(CivColor.LightGray + "\u041d\u043e \u0430\u0442\u0430\u043a\u0438 \u0441 \u043b\u0443\u043a\u0430 \u043f\u043e\u0434\u0436\u0438\u0433\u0430\u044e\u0442 \u043d\u0430 ~3\u0441\u0435\u043a");
        attrs.addLore(CivColor.LightGray + "\u0418 \u0437\u0430\u043c\u0435\u0434\u043b\u044f\u044e\u0442 \u0438\u0433\u0440\u043e\u043a\u043e\u0432 \u043d\u0430 ~3 \u0441\u0435\u043a\u0443\u043d\u0434\u044b");
        attrs.addLore(CivColor.LightGray + "1 \u043c\u0438\u043d\u0443\u0442\u0430");
        attrs.addLore(CivColor.LightGray + "\u041e\u0442\u043a\u0430\u0442: 1 \u043c\u0438\u043d\u0443\u0442\u0430");
        is = attrs.getStack();
        if (!Unit.addItemNoStack(inv, is)) {
            throw new CivException(CivSettings.localize.localizedString("var_arrtifacts_errorBarracksFull", Unit.ARCHER2_ARTIFACT.getUnit().name));
        }
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
		SimpleDateFormat sdf = new SimpleDateFormat("M/dd h:mm:ss a z");

        Player player = event.getPlayer();
        Resident interacter = CivGlobal.getResident(player);
        long nextUse = CivGlobal.getUnitCooldown(this.getClass(), event.getPlayer());
        long timeNow = Calendar.getInstance().getTimeInMillis();
        if (nextUse < timeNow) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 1200, 100));
            CivGlobal.setUnitCooldown(this.getClass(), 1, event.getPlayer());
            CivMessage.sendSuccess(interacter, CivSettings.localize.localizedString("var_artifact_useSuccusess", sdf.format(timeNow + 60000L), Unit.ARCHER2_ARTIFACT.getUnit().name));
        } else {
            CivMessage.sendError(interacter, CivSettings.localize.localizedString("var_artifact_useFailure", sdf.format(nextUse), Unit.ARCHER2_ARTIFACT.getUnit().name));
        }
        event.setCancelled(true);
        player.updateInventory();
    }
}

