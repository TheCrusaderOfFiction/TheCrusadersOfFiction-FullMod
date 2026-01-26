package net.wolfygames7237.Crusadersoffiction.Item.structure.renderer;

import net.minecraft.world.phys.Vec3;

public interface ICompressedCenteredItem {
    /** Return translation offsets relative to center (X, Y, Z) */
    default Vec3 getGhostTranslation() {
        return Vec3.ZERO; // default: no extra translation
    }
}
