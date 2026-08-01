package daxz.dev.spellwave.ImbuementSystem.Modifiers.CastingModifiers;

import daxz.dev.spellwave.Spellwave;
import daxz.dev.spellwave.Utilities.Lib.ParticleMathLib;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Bolt implements CastingModifier {

    public static final double BASE_RANGE = 15.0;
    public static final double BASE_SPEED = 1.0;
    public static final double BASE_WIDTH = 0;
    public static final Particle BASE_PARTICLE = Particle.ELECTRIC_SPARK;

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


    public static Bolt fromModifiers(Player player, Map<CastingModifierKey, Float> modifiers) {
        double range = BASE_RANGE + modifiers.getOrDefault(CastingModifierKey.RANGE, 0f);
        double speed = BASE_SPEED + modifiers.getOrDefault(CastingModifierKey.SPEED, 0f);
        double width = BASE_WIDTH + modifiers.getOrDefault(CastingModifierKey.WIDTH, 0f);

        Location startLoc = player.getEyeLocation();

        return new Bolt(BASE_PARTICLE, range, speed, width, startLoc);
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

        int delay = 0;
        int period = Math.max(1, (int) Math.round(2 / Math.max(speed, 0.01)));

        List<Location> beam;
        if (endLoc == null) {
            beam = ParticleMathLib.getPointsBetween(startLoc, startLoc.clone().add(startLoc.getDirection().normalize().multiply(range)), 0.5);
        } else {
            beam = ParticleMathLib.getPointsBetween(startLoc, endLoc, 0.5);
        }

        new BukkitRunnable() {
            int tick = 0;

            public void run() {
                if (tick >= beam.size()) {
                    cancel();
                    return;
                }

                Location point = beam.get(tick);
                particle.builder()
                        .location(point)
                        .offset(width, width, width)
                        .force(true)
                        .count(1)
                        .spawn();

                tick++;
            }
        }.runTaskTimer(Spellwave.getInstance(), delay, period);
    }
}