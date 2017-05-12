package com.learning.estimator.gui.components.representations;

import com.learning.estimator.gui.core.AbstractDataRepresentation;
import com.learning.estimator.gui.utils.DateConverter;
import com.learning.estimator.model.entities.UserGroup;
import com.learning.estimator.persistence.facade.client.PersistenceFacadeClientSide;
import com.vaadin.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.vaadin.viritin.SortableLazyList;

import javax.annotation.PostConstruct;

/**
 * User group representation
 *
 * @author rolea
 */
@SuppressWarnings("serial")
@SpringComponent
@Scope(value = "vaadin-session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GroupRepresentation extends AbstractDataRepresentation<UserGroup> {

    private static final int PAGE_SIZE = 20;
    private static final String[] TABLE_PROPERTIES = new String[]{"userGroupId", "userGroupName", "createdDate", "version"};
    private static final String[] TABLE_HEADERS = new String[]{"User GRoup ID", "User Group Name", "date Created", "Version"};
    @Autowired
    private PersistenceFacadeClientSide facade;
    public GroupRepresentation() {
        super(TABLE_PROPERTIES);
        setColumnHeaders(TABLE_HEADERS);
        enableAddButton();
        buildContent();
        addConverter("createdDate", new DateConverter());
    }

    @PostConstruct
    public void init() {
        this.setBeans(new SortableLazyList<>(
                // entity fetching strategy
                (firstRow, asc, sortProperty) -> facade.findUserGroups(firstRow / PAGE_SIZE, PAGE_SIZE),
                // count fetching strategy
                () -> facade.countUserGroups().intValue(),
                PAGE_SIZE
        ));
    }

    @Override
    public int getPageSize() {
        return PAGE_SIZE;
    }

}
