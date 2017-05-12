package com.learning.estimator.gui.core;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Panel;
import org.vaadin.viritin.MSize;
import org.vaadin.viritin.SortableLazyList;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.fields.MValueChangeListener;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MPanel;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Arrays;
import java.util.Collection;

/**
 * Data is represented as a table which supports column collapsing and reordering<br>
 *
 * @author rolea
 */
@SuppressWarnings("serial")
public abstract class AbstractDataRepresentation<T> extends MVerticalLayout {

    private MTable<T> entityTable;
    private MButton addButton;
    private boolean addButtonEnabled;

    private String[] propertyValues;

    // optional properties
    private String[] columnHeaders;
    private String[] collapsedColumns;

    public AbstractDataRepresentation(String[] propertyValues) {
        this.propertyValues = propertyValues;
    }

    public abstract void init();

    @SuppressWarnings("unchecked")
    public void setBeans(SortableLazyList<T> list) {
        entityTable.setBeans(list);
    }

    public void setColumnHeaders(String[] columnHeaders) {
        this.columnHeaders = columnHeaders;
    }

    public void setCollapsedColumns(String[] collapsedColumns) {
        this.collapsedColumns = collapsedColumns;
    }

    public void addAddListener(ClickListener listener) {
        addButton.addClickListener(listener);
    }

    public void enableAddButton() {
        this.addButtonEnabled = true;
    }

    public void addSelectListener(MValueChangeListener<T> listener) {
        entityTable.addMValueChangeListener(listener);
    }

    public void setTableBeans(Collection<T> beans) {
        entityTable.setBeans(beans);
    }

    public void refreshRows() {
        entityTable.refreshRows();
    }

    public void refreshRowCache() {
        entityTable.refreshRowCache();
    }

    public T getTableValue() {
        return entityTable.getValue();
    }

    public void setTableValue(T entity) {
        entityTable.setValue(entity);
    }

    public void clearTableData() {
        entityTable.removeAllItems();
    }

    public void setSelected(T value) {
        entityTable.setSelected(value);
    }

    /**
     * Invoke this after the first data binding in order to ensure collapsed columns are not shown by default
     */
    public void ensureCollapsed() {
        if (collapsedColumns != null)
            Arrays.stream(collapsedColumns).forEach(column -> entityTable.setColumnCollapsed(column, true));
    }

    public void addConverter(String propertyId, Converter<String, ?> converter) {
        entityTable.setConverter(propertyId, converter);
    }

    public void setTableIndex(int index) {
        entityTable.setCurrentPageFirstItemIndex(index);
    }

    public abstract int getPageSize();

    /**
     * Builds the component
     */
    public void buildContent() {
        this.withMargin(false).withSpacing(false).withSize(MSize.FULL_SIZE);

        this.entityTable = new MTable<T>().withProperties(propertyValues).withSize(MSize.FULL_SIZE);
        this.entityTable.setColumnReorderingAllowed(true);
        this.entityTable.setColumnCollapsingAllowed(true);

        if (columnHeaders != null)
            entityTable.setColumnHeaders(columnHeaders);

        if (collapsedColumns != null)
            Arrays.stream(collapsedColumns).forEach(column -> entityTable.setColumnCollapsed(column, true));

        if (addButtonEnabled) {
            addButton = new MButton(FontAwesome.PLUS);
            MHorizontalLayout buttonsLayout = new MHorizontalLayout(addButton).withMargin(false).withSpacing(false);
            addComponent(buttonsLayout);
        }

        Panel tablePanel = new MPanel(entityTable).withSize(MSize.FULL_SIZE);
        addComponent(tablePanel);
        this.setExpandRatio(tablePanel, 1.0f);
    }

}
