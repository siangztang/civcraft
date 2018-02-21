
package com.avrgaming.civcraft.items.units;

import gpl.AttributeUtil;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
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

public class AdvancedTools
extends UnitMaterial {
    public AdvancedTools(String id, ConfigUnit configUnit) {
        super(id, configUnit);
    }

    public static void spawn(Inventory inv, Town town) throws CivException {
        ItemStack is = LoreMaterial.spawn(Unit.ADVANCED_TOOLS_ARTIFACT);
        AdvancedTools.setOwningTown(town, is);
        AttributeUtil attrs = new AttributeUtil(is);
        attrs.addEnhancement("LoreEnhancementSoulBound", null, null);
        attrs.addLore(CivColor.Gold+CivSettings.localize.localizedString("itemLore_Souldbound"));
        attrs.addLore(CivColor.LightGray + "Effect:");
        attrs.addLore(CivColor.LightGray + "Passive");
        attrs.addLore(CivColor.LightGray + "40% Chance");
        attrs.addLore(CivColor.LightGray + "\u0415\u0441\u043b\u0438 \u0435\u0441\u0442\u044c \u00a7c\u0422\u0430\u043b\u0438\u0441\u043c\u0430\u043d \u0428\u0430\u0445\u0442\u0435\u0440\u0430\u00a77, \u0442\u043e");
        attrs.addLore(CivColor.LightGray + "\u0428\u0430\u043d\u0441 \u0443\u043c\u0435\u043d\u044c\u0448\u0438\u0442\u0441\u044f \u0434\u043e 20%");
        attrs.addLore(CivColor.LightGray + "\u041e\u0442\u043a\u0430\u0442: \u043d\u0435\u0442");
        is = attrs.getStack();
        if (!Unit.addItemNoStack(inv, is)) {
            throw new CivException(CivSettings.localize.localizedString("var_arrtifacts_errorBarracksFull", Unit.ADVANCED_TOOLS_ARTIFACT.getUnit().name));
        }
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        event.setCancelled(true);
        Player player = event.getPlayer();
        player.updateInventory();
    }
}

