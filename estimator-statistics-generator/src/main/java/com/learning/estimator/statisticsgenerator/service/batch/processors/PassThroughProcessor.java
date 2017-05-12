package com.learning.estimator.statisticsgenerator.service.batch.processors;

import com.learning.estimator.model.statistics.IDataProvider;
import com.learning.estimator.model.statistics.IDateProvider;
import org.springframework.batch.item.ItemProcessor;

/**
 * Pass through processor ( does not mutate the entities in any way )
 *
 * @author rolea
 */
public class PassThroughProcessor<I extends IDataProvider & IDateProvider, O extends IDataProvider & IDateProvider> implements ItemProcessor<I, O> {

    @SuppressWarnings("unchecked")
    @Override
    public O process(I item) throws Exception {
        return (O) item;
    }

}
