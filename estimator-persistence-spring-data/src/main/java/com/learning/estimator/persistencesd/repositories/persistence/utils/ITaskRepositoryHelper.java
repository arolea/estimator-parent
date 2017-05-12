package com.learning.estimator.persistencesd.repositories.persistence.utils;

import com.learning.estimator.model.entities.Task;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Workaround, must be removed once fixed
 * Lazy fetching does not work for paged queries via QueryDSL (error during count query generation)
 * https://jira.spring.io/browse/DATAJPA-684
 */
public interface ITaskRepositoryHelper {

    Page<Task> customFindByPredicate(Predicate predicate, Pageable pageable);

}
