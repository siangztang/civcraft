
package com.avrgaming.civcraft.items.units;

import gpl.AttributeUtil;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.avrgaming.civcraft.config.CivSettings;
import com.avrgaming.civcraft.config.ConfigUnit;
import com.avrgaming.civcraft.items.units.Unit;
import com.avrgaming.civcraft.items.units.UnitMaterial;
import com.avrgaming.civcraft.lorestorage.LoreMaterial;

import com.avrgaming.civcraft.exception.CivException;
import com.avrgaming.civcraft.object.Town;
import com.avrgaming.civcraft.util.CivColor;

public class CacheOfGluttons
extends UnitMaterial {
    public CacheOfGluttons(String id, ConfigUnit configUnit) {
        super(id, configUnit);
    }

    public static void spawn(Inventory inv, Town town) throws CivException {
        ItemStack is = LoreMaterial.spawn(Unit.CACHEOFGLUTTONS_ARTIFACT);
        CacheOfGluttons.setOwningTown(town, is);
        AttributeUtil attrs = new AttributeUtil(is);
        attrs.addEnhancement("LoreEnhancementSoulBound", null, null);
        attrs.addLore("\u00a76SoulBound");
        attrs.addLore(CivColor.LightGray + "\u042d\u0444\u0444\u0435\u043a\u0442:");
        attrs.addLore(CivColor.LightGray + "\u041f\u0430\u0441\u0441\u0438\u0432\u043d\u044b\u0439");
        attrs.addLore(CivColor.LightGray + "\u0412\u0430\u0448\u0430 \u0435\u0434\u0430 \u043d\u0435 \u0442\u0440\u0430\u0442\u0438\u0442\u0441\u044f \u0432\u043e\u043e\u0431\u0449\u0435");
        attrs.addLore(CivColor.LightGray + "\u041e\u0442\u043a\u0430\u0442: \u043d\u0435\u0442");
        is = attrs.getStack();
        if (!Unit.addItemNoStack(inv, is)) {
            throw new CivException(CivSettings.localize.localizedString("var_arrtifacts_errorBarracksFull", Unit.CACHEOFGLUTTONS_ARTIFACT.getUnit().name));
        }
    }
}

