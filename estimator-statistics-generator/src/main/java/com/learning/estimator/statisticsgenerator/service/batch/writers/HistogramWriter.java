package com.learning.estimator.statisticsgenerator.service.batch.writers;

import com.learning.estimator.model.statistics.IDataProvider;
import com.learning.estimator.model.statistics.IDateProvider;
import com.learning.estimator.model.statistics.StatisticsQuery;
import com.learning.estimator.statisticsgenerator.service.batch.utils.IResultProvider;
import com.learning.estimator.statisticsgenerator.service.batch.utils.MapAccumulator;
import org.springframework.batch.item.ItemWriter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Collects data for a histogram statistic
 *
 * @author rolea
 */
public class HistogramWriter<T extends IDataProvider & IDateProvider> implements ItemWriter<T> {

    private StatisticsQuery query;
    private MapAccumulator<Integer> accumulator;

    public HistogramWriter(StatisticsQuery query, IResultProvider<Integer> accumulator) {
        this.query = query;
        this.accumulator = (MapAccumulator<Integer>) accumulator;
    }

    private static final int getBin(Double data, Integer binSize) {
        return (int) (binSize * (Math.round(data / binSize)));
    }

    @Override
    public void write(List<? extends T> items) throws Exception {
        Map<Integer, Number> partialResult = new ConcurrentHashMap<>();
        items.forEach(item -> {
            int bin = getBin(item.getData(), query.getBinSize());
            if (partialResult.containsKey(bin)) {
                partialResult.put(bin, partialResult.get(bin).intValue() + 1);
            } else {
                partialResult.put(bin, 1);
            }
        });
        accumulator.merge(partialResult);
    }

}
