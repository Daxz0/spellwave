package daxz.dev.spellwave.ImbuementSystem.Modifiers.CastingModifiers;

import daxz.dev.spellwave.Spellwave;
import daxz.dev.spellwave.Utilities.Lib.ParticleMathLib;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Bolt implements CastingModifier {

    private final Map<CastingModifierKey, Object> attributes = new EnumMap<>(CastingModifierKey.class);

    public Bolt(Particle particle, double range, double speed, double width, Location startLoc, Location endLoc) {
        attributes.put(CastingModifierKey.RANGE, null); //shouldnt matter anymore since this will be pre-processed in the spell modifier handler
        attributes.put(CastingModifierKey.SPEED, speed);
        attributes.put(CastingModifierKey.WIDTH, width);
        attributes.put(CastingModifierKey.START_LOC, startLoc);
        attributes.put(CastingModifierKey.END_LOC, endLoc);
        attributes.put(CastingModifierKey.PARTICLE, particle);
    }

    public Bolt(Particle particle, double range, double speed, double width, Location startLoc) {
        attributes.put(CastingModifierKey.RANGE, range);
        attributes.put(CastingModifierKey.SPEED, speed);
        attributes.put(CastingModifierKey.WIDTH, width);
        attributes.put(CastingModifierKey.START_LOC, startLoc);
        attributes.put(CastingModifierKey.END_LOC, null);
        attributes.put(CastingModifierKey.PARTICLE, particle);
    }

    @Override
    public Map<CastingModifierKey, Object> getAttributes() {
        return attributes;
    }

    public void createModifier() {
        double range = getFloat(CastingModifierKey.RANGE);
        double speed = getFloat(CastingModifierKey.SPEED);
        double width = getFloat(CastingModifierKey.WIDTH);
        Location startLoc = getValue(CastingModifierKey.START_LOC, Location.class);
        Location endLoc = getValue(CastingModifierKey.END_LOC, Location.class);
        Particle particle = getValue(CastingModifierKey.PARTICLE, Particle.class);

        // calculate based on speed
        int delay = 0;
        int period = 0;

        //create beam
        List<Location> beam;
        if (endLoc == null) {
            beam = ParticleMathLib.getPointsBetween(startLoc, startLoc.clone().add(startLoc.getDirection().normalize().multiply(range)), 0.5);
        }
        else{
            beam = ParticleMathLib.getPointsBetween(startLoc, endLoc, 0.5);
        }
        int tick = 0;
        BukkitTask task = new BukkitRunnable() {
            public void run() {
                //normal nondirectional particles only
                Location point = beam.get(tick);
                particle.builder()
                        .location(point)
                        .offset(width, width, width)
                        .spawn();


            }
        }.runTaskTimer(Spellwave.getInstance(),delay,period);
    }
}