
package com.avrgaming.civcraft.loreenhancements;

import gpl.AttributeUtil;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import com.avrgaming.civcraft.loreenhancements.LoreEnhancement;
import com.avrgaming.civcraft.util.CivColor;

public class LoreEnhancementQuarryItem
extends LoreEnhancement
implements Listener {
    @Override
    public String getDisplayName() {
        return "\u041f\u0440\u0435\u0434\u043c\u0435\u0442 \u0441 \u043a\u0430\u0440\u044c\u0435\u0440\u0430";
    }

    @Override
    public AttributeUtil add(AttributeUtil attrs) {
        attrs.addEnhancement("LoreEnhancementQuarryItem", null, null);
        attrs.addLore(CivColor.RoseBold + this.getDisplayName());
        return attrs;
    }

    @Override
    public String serialize(ItemStack stack) {
        return "";
    }

    @Override
    public ItemStack deserialize(ItemStack stack, String data) {
        return stack;
    }
}

