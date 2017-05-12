package com.learning.estimator.persistence.operations;

/**
 * Defines available group operations
 *
 * @author rolea
 */
public enum GroupOperations {

    SAVE_USER_GROUP("save_user_group"),
    FIND_USER_GROUP("find_user_group"),
    FIND_USER_GROUP_BY_NAME("find_user_group_by_name"),
    FIND_ALL_USES_GROUPS("find_all_users_groups"),
    PAGINATE_USER_GROUPS("paginate_users_groups"),
    DELETE_USER_GROUP("delete_user_group"),
    DELETE_ALL_USER_GROUPS("delete_all_users_groups"),
    COUNT_USES_GROUPS("count_users_groups"),;

    private String id;

    private GroupOperations(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
