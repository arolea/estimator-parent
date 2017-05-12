package com.learning.estimator.statisticsgenerator.service.batch.utils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Accumulates values into a list
 *
 * Note that steps (and the tasks within steps) are thread confined
 *
 * @author rolea
 */
public class ListAccumulator<T> implements IResultProvider<T> {

    private List<T> result;

    public ListAccumulator() {
        result = new LinkedList<>();
    }

    public void merge(List<T> other) {
        result.addAll(other);
    }

    @Override
    public Object getResult() {
        return Collections.unmodifiableList(result);
    }

}
