package com.learning.estimator.gui.core;

import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.button.MButton.MClickListener;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Abstract superclass for filtering components
 *
 * @author rolea
 */
@SuppressWarnings("serial")
public abstract class AbstractFilteringComponent extends MHorizontalLayout {

    protected MButton filterButton;
    private Map<String, Component> components = new LinkedHashMap<>();

    public void addComboBox(String id, String caption, String description) {
        ComboBox combo = new ComboBox(caption);
        combo.setDescription(description);
        components.put(id, combo);
    }

    public void addTextField(String id, String caption, String description) {
        TextField combo = new TextField(caption);
        combo.setDescription(description);
        components.put(id, combo);
    }

    public void addDateTimeField(String id, String caption, String description) {
        PopupDateField popupDate = new PopupDateField();
        popupDate.setValue(new Date());
        popupDate.setDateFormat("yyyy-MM-dd");
        popupDate.setInputPrompt("Select a date");
        popupDate.setCaption(caption);
        popupDate.setDescription(description);
        components.put(id, popupDate);
    }

    public void removeComponent(String id) {
        components.remove(id);
    }

    public Object getComboBoxValue(String id) {
        return ((ComboBox) components.get(id)).getValue();
    }

    public String getTextFieldValue(String id) {
        return ((TextField) components.get(id)).getValue();
    }

    public LocalDate getDateFieldValue(String id) {
        Date date = ((PopupDateField) components.get(id)).getValue();
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public void setComboValues(String id, Collection<?> values) {
        ((AbstractSelect) components.get(id)).removeAllItems();
        ((AbstractSelect) components.get(id)).addItems(values);
    }

    public void setDateValue(String id, Date date) {
        ((PopupDateField) components.get(id)).setValue(date);
    }

    public void setTextValue(String id, String text) {
        ((TextField) components.get(id)).setValue(text);
    }

    public void addFilterListener(MButton.MClickListener listener) {
        filterButton.addClickListener(listener);
    }

    public ComboBox getComboBox(String id) {
        return (ComboBox) components.get(id);
    }

    public void buildContent(String filterButtonName, String filterCaption, String filterDescription) {
        this.withMargin(false).withSpacing(true);
        components.forEach((id, combo) -> {
            this.addComponent(combo);
        });
        filterButton = new MButton(filterButtonName).withDescription(filterDescription);
        addComponent(new MVerticalLayout(filterButton).withCaption(filterCaption).withMargin(false).withSpacing(false));
        filterButton.addClickListener(getClickListener());
        this.setHeightUndefined();
    }

    protected abstract MClickListener getClickListener();

}
