package com.learning.estimator.statisticsgenerator.service.batch.writers;

import com.learning.estimator.model.statistics.IDataProvider;
import com.learning.estimator.model.statistics.IDateProvider;
import com.learning.estimator.model.statistics.StatisticsQuery;
import com.learning.estimator.statisticsgenerator.service.batch.utils.IResultProvider;
import com.learning.estimator.statisticsgenerator.service.batch.utils.MapAccumulator;
import org.springframework.batch.item.ItemWriter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Collects data for a time evolution statistic
 *
 * @author rolea
 */
public class TimeEvolutionWriter<T extends IDataProvider & IDateProvider> implements ItemWriter<T> {

    private StatisticsQuery query;
    private MapAccumulator<LocalDate> accumulator;

    public TimeEvolutionWriter(StatisticsQuery query, IResultProvider<LocalDate> accumulator) {
        this.query = query;
        this.accumulator = (MapAccumulator<LocalDate>) accumulator;
    }

    private static final LocalDate getBin(LocalDate data, Integer binSize) {
        return LocalDate.ofEpochDay((long) (binSize * (Math.round(data.toEpochDay() / binSize))));
    }

    @Override
    public void write(List<? extends T> items) throws Exception {
        Map<LocalDate, Number> partialResult = new ConcurrentHashMap<>();
        items.forEach(item -> {
            LocalDate bin = getBin(item.getDate(), query.getBinSize());
            if (partialResult.containsKey(bin)) {
                partialResult.put(bin, partialResult.get(bin).doubleValue() + item.getData());
            } else {
                partialResult.put(bin, item.getData());
            }
        });
        accumulator.merge(partialResult);
    }

}
