package com.learning.estimator.persistence.operations;

/**
 * Defines available project operations
 *
 * @author rolea
 */
public enum ProjectOperations {

    SAVE_PROJECT("save_project"),
    FIND_PROJECT("find_project"),
    FIND_PROJECT_BY_NAME("find_project_by_name"),
    FIND_ALL_PROJECTS("find_all_projects"),
    PAGINATE_PROJECTS("paginate_projects"),
    DELETE_PROJECT("delete_project"),
    DELETE_ALL_PROJECTS("delete_all_projects"),
    COUNT_PROJECTS("count_projects"),;

    private String id;

    private ProjectOperations(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
