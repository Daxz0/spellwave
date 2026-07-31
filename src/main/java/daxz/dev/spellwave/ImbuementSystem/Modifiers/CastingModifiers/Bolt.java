package daxz.dev.spellwave.ImbuementSystem.Modifiers.CastingModifiers;

import org.bukkit.Location;

import java.util.EnumMap;
import java.util.Map;

public class Bolt implements CastingModifier {

    private final Map<CastingModifierKey, Object> attributes = new EnumMap<>(CastingModifierKey.class);

    public Bolt(float range, float speed, float width, Location startLoc, Location endLoc) {
        attributes.put(CastingModifierKey.RANGE, range);
        attributes.put(CastingModifierKey.SPEED, speed);
        attributes.put(CastingModifierKey.WIDTH, width);
        attributes.put(CastingModifierKey.START_LOC, startLoc);
        attributes.put(CastingModifierKey.END_LOC, endLoc);
    }

    @Override
    public Map<CastingModifierKey, Object> getAttributes() {
        return attributes;
    }

    public void createModifier() {
        float range = getFloat(CastingModifierKey.RANGE);
        float speed = getFloat(CastingModifierKey.SPEED);
        float width = getFloat(CastingModifierKey.WIDTH);
        Location startLoc = getValue(CastingModifierKey.START_LOC, Location.class);
        Location endLoc = getValue(CastingModifierKey.END_LOC, Location.class);

    }
}