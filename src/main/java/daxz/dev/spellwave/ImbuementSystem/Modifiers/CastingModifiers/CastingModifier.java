package daxz.dev.spellwave.ImbuementSystem.Modifiers.CastingModifiers;

import java.util.Map;
import java.util.Set;

public interface CastingModifier {

    Map<CastingModifierKey, Object> getAttributes();

    default Set<CastingModifierKey> getAvailableKeys() {
        return getAttributes().keySet();
    }

    default boolean hasKey(CastingModifierKey key) {
        return getAttributes().containsKey(key);
    }

    default <T> T getValue(CastingModifierKey key, Class<T> type) {
        Object value = getAttributes().get(key);
        if (value == null) return null;
        if (!type.isInstance(value)) {
            throw new IllegalStateException(
                    "Key " + key + " expected " + type.getSimpleName()
                            + " but was " + value.getClass().getSimpleName());
        }
        return type.cast(value);
    }

    default void setValue(CastingModifierKey key, Object value) {
        if (hasKey(key)) {
            getAttributes().put(key, value);
        }
    }

    default float getFloat(CastingModifierKey key) {
        Number n = getValue(key, Number.class);
        return n == null ? 0f : n.floatValue();
    }

    default void adjustFloat(CastingModifierKey key, float delta) {
        if (hasKey(key)) {
            getAttributes().merge(key, delta, (oldVal, deltaVal) ->
                    ((Number) oldVal).floatValue() + (Float) deltaVal);
        }
    }
}