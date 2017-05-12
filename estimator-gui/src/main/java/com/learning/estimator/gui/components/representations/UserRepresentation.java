package com.learning.estimator.gui.components.representations;

import com.learning.estimator.gui.core.AbstractDataRepresentation;
import com.learning.estimator.model.entities.User;
import com.learning.estimator.model.entities.UserRole;
import com.learning.estimator.persistence.facade.client.PersistenceFacadeClientSide;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.vaadin.viritin.SortableLazyList;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * User representation
 *
 * @author rolea
 */
@SuppressWarnings("serial")
@SpringComponent
@Scope(value = "vaadin-session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserRepresentation extends AbstractDataRepresentation<User> {

    private static final int PAGE_SIZE = 20;
    private static final String[] TABLE_PROPERTIES = new String[]{"userId", "username", "password", "roles"};
    private static final String[] TABLE_HEADERS = new String[]{"User id", "Username", "Password Hash", "Roles"};
    @Autowired
    private PersistenceFacadeClientSide facade;
    public UserRepresentation() {
        super(TABLE_PROPERTIES);
        setColumnHeaders(TABLE_HEADERS);
        enableAddButton();
        buildContent();
        addConverter("roles", new RolesConverter());
    }

    @PostConstruct
    public void init() {
        this.setBeans(new SortableLazyList<>(
                // entity fetching strategy
                (firstRow, asc, sortProperty) -> facade.findUsers(firstRow / PAGE_SIZE, PAGE_SIZE),
                // count fetching strategy
                () -> facade.countUsers().intValue(),
                PAGE_SIZE
        ));
    }

    @Override
    public int getPageSize() {
        return PAGE_SIZE;
    }

    private class RolesConverter implements Converter<String, Set<UserRole>> {

        @Override
        public Set<UserRole> convertToModel(String value, Class<? extends Set<UserRole>> targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
            Set<UserRole> result = new HashSet<UserRole>();
            if (value == null)
                return result;
            Arrays.stream(value.split(" , ")).forEach(token -> result.add(UserRole.valueOf(token)));
            return result;
        }

        @Override
        public String convertToPresentation(Set<UserRole> value, Class<? extends String> targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
            if (value == null || value.size() == 0)
                return "None";
            else
                return value.stream().map(UserRole::name).collect(Collectors.joining(" , "));
        }

        @SuppressWarnings("unchecked")
        @Override
        public Class<Set<UserRole>> getModelType() {
            return (Class<Set<UserRole>>) (Class<?>) Set.class;
        }

        @Override
        public Class<String> getPresentationType() {
            return String.class;
        }

    }

}
