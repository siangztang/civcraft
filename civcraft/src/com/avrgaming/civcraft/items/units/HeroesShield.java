
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

public class HeroesShield
extends UnitMaterial {
    public HeroesShield(String id, ConfigUnit configUnit) {
        super(id, configUnit);
    }

    public static void spawn(Inventory inv, Town town) throws CivException {
        ItemStack is = LoreMaterial.spawn(Unit.HEROESSHIELD_ARTIFACT);
        HeroesShield.setOwningTown(town, is);
        AttributeUtil attrs = new AttributeUtil(is);
        attrs.addEnhancement("LoreEnhancementSoulBound", null, null);
        attrs.addLore("\u00a76SoulBound");
        attrs.addLore("\u00a7e\u041f\u043e\u0441\u043b\u0435 \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u044f \u043f\u0440\u043e\u043f\u0430\u0434\u0430\u0435\u0442");
        attrs.addLore(CivColor.LightGray + "\u042d\u0444\u0444\u0435\u043a\u0442:");
        attrs.addLore(CivColor.LightGray + "\u0410\u043a\u0442\u0438\u0432\u0438\u0440\u0443\u0435\u043c\u044b\u0439");
        attrs.addLore(CivColor.LightGray + "\u0412\u044b \u0438\u043c\u0435\u0435\u0442\u0435 20% \u0448\u0430\u043d\u0441 \u0443\u0432\u0435\u0440\u043d\u0443\u0442\u044c\u0441\u044f \u043e\u0442 \u0443\u0434\u0430\u0440\u0430");
        attrs.addLore(CivColor.LightGray + "(31 \u0441\u0435\u043a\u0443\u043d\u0434\u0430)");
        attrs.addLore(CivColor.LightGray + "\u041e\u0442\u043a\u0430\u0442: 20 \u043c\u0438\u043d\u0443\u0442");
        is = attrs.getStack();
        if (!Unit.addItemNoStack(inv, is)) {
            throw new CivException(CivSettings.localize.localizedString("var_arrtifacts_errorBarracksFull", Unit.HEROESSHIELD_ARTIFACT.getUnit().name));
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
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 620, 0));
            CivGlobal.setUnitCooldown(this.getClass(), 30, event.getPlayer());
            CivMessage.sendSuccess(interacter, CivSettings.localize.localizedString("var_artifact_useSuccusess", sdf.format(timeNow + 1800000L), Unit.HEROESSHIELD_ARTIFACT.getUnit().name));
            this.removeChildren(player.getInventory());
            CivMessage.sendError(interacter, CivSettings.localize.localizedString("var_artifact_useSuccusessButNot"));
        } else {
            CivMessage.sendError(interacter, CivSettings.localize.localizedString("var_artifact_useFailure", sdf.format(nextUse), Unit.HEROESSHIELD_ARTIFACT.getUnit().name));
        }
        event.setCancelled(true);
    }
}

