package com.learning.estimator.statisticsgenerator.service.batch.readers;

import com.learning.estimator.model.statistics.StatisticsQuery;
import com.learning.estimator.persistence.facade.statistics.StatisticsFacade;
import org.springframework.batch.item.adapter.AbstractMethodInvokingDelegator.InvocationTargetThrowableWrapper;
import org.springframework.batch.item.adapter.DynamicMethodInvocationException;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.util.ClassUtils;
import org.springframework.util.MethodInvoker;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract reader that supports streaming
 *
 * @author rolea
 */
public class AbstractPersistenceFacadeReader<T> extends AbstractItemCountingItemStreamItemReader<T> {

    private StatisticsFacade facade;
    private StatisticsQuery query;

    private volatile int pageIndex = 0;
    private int pageSize = 100;
    private volatile int current = 0;
    private volatile List<T> results;
    private Object lock = new Object();
    private String methodName;

    public AbstractPersistenceFacadeReader(StatisticsFacade facade, StatisticsQuery query, int pageSize, String methodName) {
        setName(ClassUtils.getShortName(AbstractPersistenceFacadeReader.class));
        this.facade = facade;
        this.query = query;
        this.pageSize = pageSize;
        this.methodName = methodName;
    }

    @Override
    protected T doRead() throws Exception {
        synchronized (lock) {
            if (results == null || current >= results.size()) {
                results = doPageRead();
                current = 0;
                pageIndex++;
                if (results.size() <= 0) {
                    return null;
                }
            }
            if (current < results.size()) {
                T curLine = results.get(current);
                current++;
                return curLine;
            } else {
                return null;
            }
        }
    }

    @Override
    protected void jumpToItem(int itemLastIndex) throws Exception {
        synchronized (lock) {
            pageIndex = (itemLastIndex - 1) / pageSize;
            current = (itemLastIndex - 1) % pageSize;
            results = doPageRead();
            pageIndex++;
        }
    }

    @SuppressWarnings("unchecked")
    protected List<T> doPageRead() throws Exception {
        MethodInvoker invoker = createMethodInvoker(facade, methodName);
        List<Object> parameters = new ArrayList<Object>();
        parameters.add(query.getUserId());
        parameters.add(query.getProjectId());
        parameters.add(query.getGroupId());
        parameters.add(query.getBegindDate());
        parameters.add(query.getEndDate());
        parameters.add(pageIndex);
        parameters.add(pageSize);
        invoker.setArguments(parameters.toArray());
        List<T> curPage = (List<T>) doInvoke(invoker);
        return curPage;
    }

    @Override
    protected void doOpen() throws Exception {
    }

    @Override
    protected void doClose() throws Exception {
        synchronized (lock) {
            current = 0;
            pageIndex = 0;
            results = null;
        }
    }

    private Object doInvoke(MethodInvoker invoker) throws Exception {
        try {
            invoker.prepare();
        } catch (ClassNotFoundException e) {
            throw new DynamicMethodInvocationException(e);
        } catch (NoSuchMethodException e) {
            throw new DynamicMethodInvocationException(e);
        }

        try {
            return invoker.invoke();
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof Exception) {
                throw (Exception) e.getCause();
            } else {
                throw new InvocationTargetThrowableWrapper(e.getCause());
            }
        } catch (IllegalAccessException e) {
            throw new DynamicMethodInvocationException(e);
        }
    }

    private MethodInvoker createMethodInvoker(Object targetObject, String targetMethod) {
        MethodInvoker invoker = new MethodInvoker();
        invoker.setTargetObject(targetObject);
        invoker.setTargetMethod(targetMethod);
        return invoker;
    }
}
