
package com.avrgaming.civcraft.items.units;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import gpl.AttributeUtil;

import org.bukkit.Location;
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

public class Diver
extends UnitMaterial {
    public Diver(String id, ConfigUnit configUnit) {
        super(id, configUnit);
    }

    public static void spawn(Inventory inv, Town town) throws CivException {
        ItemStack is = LoreMaterial.spawn(Unit.DIVER_ARTIFACT);
        Diver.setOwningTown(town, is);
        AttributeUtil attrs = new AttributeUtil(is);
        attrs.addEnhancement("LoreEnhancementSoulBound", null, null);
        attrs.addLore("\u00a76SoulBound");
        attrs.addLore(CivColor.Yellow +"Single Use");
        attrs.addLore(CivColor.LightGray + "\u042d\u0444\u0444\u0435\u043a\u0442:");
        attrs.addLore(CivColor.LightGray + "\u0410\u043a\u0442\u0438\u0432\u0438\u0440\u0443\u0435\u043c\u044b\u0439");
        attrs.addLore(CivColor.LightGray + "\u0414\u044b\u0445\u0430\u043d\u0438\u0435 \u043f\u043e\u0434 \u0432\u043e\u0434\u043e\u0439 III");
        attrs.addLore(CivColor.LightGray + "5 \u043c\u0438\u043d\u0443\u0442");
        attrs.addLore(CivColor.LightGray + "\u041e\u0442\u043a\u0430\u0442: 25 \u043c\u0438\u043d\u0443\u0442");
        is = attrs.getStack();
        if (!Unit.addItemNoStack(inv, is)) {
            throw new CivException(CivSettings.localize.localizedString("var_arrtifacts_errorBarracksFull", Unit.DIVER_ARTIFACT.getUnit().name));
        }
    }

    public static void spawnOnce(Location location) {
        ItemStack is = LoreMaterial.spawn(Unit.DIVER_ARTIFACT);
        AttributeUtil attrs = new AttributeUtil(is);
        attrs.addEnhancement("LoreEnhancementSoulBound", null, null);
        attrs.addLore("\u00a76SoulBound");
        attrs.addLore("\u00a7e\u041f\u043e\u0441\u043b\u0435 \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u044f \u043f\u0440\u043e\u043f\u0430\u0434\u0430\u0435\u0442");
        attrs.addLore(CivColor.LightGray + "\u042d\u0444\u0444\u0435\u043a\u0442:");
        attrs.addLore(CivColor.LightGray + "\u0410\u043a\u0442\u0438\u0432\u0438\u0440\u0443\u0435\u043c\u044b\u0439");
        attrs.addLore(CivColor.LightGray + "\u0414\u044b\u0445\u0430\u043d\u0438\u0435 \u043f\u043e\u0434 \u0432\u043e\u0434\u043e\u0439 III");
        attrs.addLore(CivColor.LightGray + "5 \u043c\u0438\u043d\u0443\u0442");
        attrs.addLore(CivColor.LightGray + "\u041e\u0442\u043a\u0430\u0442: 25 \u043c\u0438\u043d\u0443\u0442");
        is = attrs.getStack();
        location.getWorld().dropItemNaturally(location, is);
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
		SimpleDateFormat sdf = new SimpleDateFormat("M/dd h:mm:ss a z");
        Player player = event.getPlayer();
        Resident interacter = CivGlobal.getResident(player);
        long nextUse = CivGlobal.getUnitCooldown(this.getClass(), event.getPlayer());
        long timeNow = Calendar.getInstance().getTimeInMillis();
        if (nextUse < timeNow) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 6000, 2));
            CivGlobal.setUnitCooldown(this.getClass(), 25, event.getPlayer());
            CivMessage.sendSuccess(interacter, CivSettings.localize.localizedString("var_artifact_useSuccusess", sdf.format(timeNow + 1500000L), Unit.DIVER_ARTIFACT.getUnit().name));
            if (CivGlobal.isOneUseArtifact(event.getItem())) {
            	this.removeChildren(player.getInventory());
                CivMessage.sendError(interacter, CivSettings.localize.localizedString("var_artifact_useSuccusessButNot"));
            }
        } else {
            CivMessage.sendError(interacter, CivSettings.localize.localizedString("var_artifact_useFailure", sdf.format(nextUse), Unit.DIVER_ARTIFACT.getUnit().name));
        }
        event.setCancelled(true);
    }
}

