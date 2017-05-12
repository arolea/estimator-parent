package com.learning.estimator.persistence.operations;

/**
 * Defines available user operations
 *
 * @author rolea
 */
public enum UserOperations {

    SAVE_USER("save_user"),
    FIND_USER("find_user"),
    FIND_USER_BY_NAME("find_user_by_name"),
    FIND_ALL_USERS("find_all_users"),
    PAGINATE_USERS("paginate_users"),
    DELETE_USER("delete_user"),
    DELETE_ALL_USERS("delete_all_users"),
    COUNT_USERS("count_users"),;

    private String id;

    private UserOperations(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
