package com.learning.estimator.statisticsgenerator.service.batch.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Accumulates values into a Map
 *
 * Note that steps (and the tasks within steps) are thread confined
 *
 * @author rolea
 */
public class MapAccumulator<T> implements IResultProvider<T> {

    private Map<T, Number> result;

    public MapAccumulator() {
        this.result = new HashMap<>();
    }

    public void merge(Map<T, Number> other) {
        other.forEach((otherKey, otherValue) -> {
            if (result.containsKey(otherKey))
                result.put(otherKey, result.get(otherKey).doubleValue() + otherValue.doubleValue());
            else
                result.put(otherKey, otherValue);
        });
    }

    @Override
    public Object getResult() {
        return Collections.unmodifiableMap(result);
    }

}
