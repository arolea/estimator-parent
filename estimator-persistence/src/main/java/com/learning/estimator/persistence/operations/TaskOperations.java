package com.learning.estimator.persistence.operations;

/**
 * Defines available task operations
 *
 * @author rolea
 */
public enum TaskOperations {

    SAVE_TASK("save_task"),

    FIND_TASK("find_task"),

    FIND_ALL_TASKS("find_all_tasks"),
    FIND_ALL_BY_USER("find_all_by_user"),
    FIND_ALL_BY_PROJECT("find_all_by_project"),
    FIND_ALL_BY_STATUS("find_all_by_status"),
    FIND_ALL_BY_CRITERIA("find_all_by_criteria"),

    PAGINATE_TASKS("paginate_tasks"),
    PAGINATE_BY_USER("paginate_by_user"),
    PAGINATE_BY_PROJECT("paginate_by_project"),
    PAGINATE_BY_STATUS("paginate_by_status"),
    PAGINATE_BY_CRITERIA("paginate_by_criteria"),

    DELETE_TASK("delete_task"),
    DELETE_ALL_TASKS("delete_all_tasks"),

    COUNT_TASKS("count_tasks"),
    COUNT_TASKS_BY_USER("count_tasks_by_user"),
    COUNT_TASKS_BY_PROJECT("count_tasks_by_project"),
    COUNT_TASKS_BY_STATUS("count_tasks_by_status"),
    COUNT_TASKS_BY_CRITERIA("count_tasks_by_criteria"),;

    private String id;

    private TaskOperations(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
