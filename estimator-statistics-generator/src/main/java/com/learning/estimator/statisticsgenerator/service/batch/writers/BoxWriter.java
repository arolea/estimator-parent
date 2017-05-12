package com.learning.estimator.statisticsgenerator.service.batch.writers;

import com.learning.estimator.model.statistics.IDataProvider;
import com.learning.estimator.model.statistics.IDateProvider;
import com.learning.estimator.statisticsgenerator.service.batch.utils.IResultProvider;
import com.learning.estimator.statisticsgenerator.service.batch.utils.ListAccumulator;
import org.springframework.batch.item.ItemWriter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Collects data for a box statistic
 *
 * @author rolea
 */
public class BoxWriter<T extends IDataProvider & IDateProvider> implements ItemWriter<T> {

    private ListAccumulator<Double> accumulator;

    public BoxWriter(IResultProvider<Double> accumulator) {
        this.accumulator = (ListAccumulator<Double>) accumulator;
    }

    @Override
    public void write(List<? extends T> items) throws Exception {
        accumulator.merge(items.stream().map(IDataProvider::getData).collect(Collectors.toList()));
    }

}
