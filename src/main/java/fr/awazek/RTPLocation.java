package fr.awazek;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class RTPLocation extends Location {

    private boolean isSafe;

    public RTPLocation(World world, double x, double y, double z) {
        super(world, x, y, z);
        this.setY(world.getHighestBlockYAt(this));
        Block underBlock = this.add(0, -1, 0).getBlock();
        isSafe = underBlock.getType().isSolid();
    }

    public boolean isSafe() {
        return isSafe;
    }

}
