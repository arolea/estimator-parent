package com.learning.estimator.persistencesd.repositories.persistence.utils;

import com.learning.estimator.model.entities.Task;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.querydsl.SimpleEntityPathResolver;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

/**
 * Workaround, must be removed once fixed
 * Lazy fetching does not work for paged queries via QueryDSL (error during count query generation)
 * https://jira.spring.io/browse/DATAJPA-684
 */
@SuppressWarnings(value = {"unchecked", "rawtypes"})
public class ITaskRepositoryImpl extends SimpleJpaRepository<Task, Long> implements ITaskRepositoryHelper {

    private final EntityManager entityManager;
    private final EntityPath<Task> path;
    private final PathBuilder<Task> builder;
    private final Querydsl querydsl;

    @Autowired
    public ITaskRepositoryImpl(EntityManager entityManager) {
        super(Task.class, entityManager);

        this.entityManager = entityManager;
        this.path = SimpleEntityPathResolver.INSTANCE.createPath(Task.class);
        this.builder = new PathBuilder<>(path.getType(), path.getMetadata());
        this.querydsl = new Querydsl(entityManager, builder);
    }

    @Override
    public Page<Task> customFindByPredicate(Predicate predicate, Pageable pageable) {
        JPAQuery countQuery = createQuery(predicate);
        JPAQuery query = (JPAQuery) querydsl.applyPagination(pageable, createQuery(predicate));

        query.setHint(EntityGraph.EntityGraphType.LOAD.getKey(), entityManager.getEntityGraph("graph.Task.fetchPolicy"));

        Long total = countQuery.fetchCount();
        List<Task> content = total > pageable.getOffset() ? query.fetch() : Collections.<Task>emptyList();

        return new PageImpl<>(content, pageable, total);
    }

    private JPAQuery createQuery(Predicate predicate) {
        return querydsl.createQuery(path).where(predicate);
    }

}
