
package com.avrgaming.civcraft.loreenhancements;

import gpl.AttributeUtil;
import org.bukkit.inventory.ItemStack;
import com.avrgaming.civcraft.loreenhancements.LoreEnhancement;

public class LoreEnhancementDropAfterDeath
extends LoreEnhancement {
    @Override
    public AttributeUtil add(AttributeUtil attrs) {
        attrs.addEnhancement("LoreEnhancementBuyItem", null, null);
        attrs.addLore("\u00a7c" + this.getDisplayName());
        return attrs;
    }

    @Override
    public String getDisplayName() {
        return "Vanishing";
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

