package daxz.dev.spellwave.Utilities.Lib;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class ParticleMathLib {

    public static List<Location> getPointsBetween(Location startLoc, Location endLoc, double spacing) {
        List<Location> list = new ArrayList<>();

        if (spacing <= 0.000001) {
            throw new IllegalArgumentException("spacing must be positive, got " + spacing);
        }

        org.bukkit.util.Vector rel = endLoc.toVector().subtract(startLoc.toVector());
        double len = rel.length();
        if (len < 0.000001) {
            list.add(startLoc.clone());
            return list;
        }

        rel = rel.multiply(1d / len);
        for (double i = 0d; i <= len; i += spacing) {
            list.add(startLoc.clone().add(rel.clone().multiply(i)));
        }
        return list;
    }
}