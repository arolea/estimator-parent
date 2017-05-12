package com.learning.estimator.gui.components.filtering;

import com.learning.estimator.model.statistics.AvailableStatistics;
import com.learning.estimator.persistence.facade.client.PersistenceFacadeClientSide;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.button.MButton.MClickListener;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Displays the result of a statistic computation
 *
 * @author rolea
 */
@SuppressWarnings("serial")
@SpringComponent
@Scope(value = "vaadin-session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class StatisticsFilter extends MHorizontalLayout {

    @Autowired
    private PersistenceFacadeClientSide facade;

    private Map<String, ComboBox> combos;
    private Map<String, PopupDateField> dates;
    private Map<String, TextField> fields;
    private Map<String, MButton> buttons;
    private Map<String, String> buttonCaptions;

    @PostConstruct
    public void init() {
        UI.getCurrent().setPollInterval(1000);

        combos = new LinkedHashMap<>();
        dates = new LinkedHashMap<>();
        fields = new LinkedHashMap<>();
        buttons = new LinkedHashMap<>();
        buttonCaptions = new LinkedHashMap<>();

        this.addComboBox("statistic", "Statistic", "Choose a statistic");
        this.addComboBox("user", "User", "Select user");
        this.addComboBox("project", "Project", "Select project");
        this.addComboBox("group", "Group", "Select group");

        this.addDateTimeField("beginDate", "Start date", "Select starting date");
        this.addDateTimeField("endDate", "End date", "Select ending date");

        this.addTextField("bin", "Granularity", "Bin size for statistics");

        this.addButton("compute", "Compute", "Compute statistic", "Compute the statistic");
        this.addButton("cancel", "Cancel", "Cancel computation", "Cancel the current computation");

        buildContent();
    }

    public void buildContent() {
        this.withMargin(false).withSpacing(true);
        combos.forEach((id, combo) -> this.addComponent(combo));
        dates.forEach((id, date) -> this.addComponent(date));
        fields.forEach((id, field) -> this.addComponent(field));
        buttons.forEach((id, button) -> this.addComponent(new MVerticalLayout(button).withCaption(buttonCaptions.get(id)).withMargin(false).withSpacing(false)));
        this.setHeightUndefined();
    }

    public void addComboBox(String id, String caption, String description) {
        ComboBox combo = new ComboBox(caption);
        combo.setDescription(description);
        combos.put(id, combo);
    }

    public void addTextField(String id, String caption, String description) {
        TextField combo = new TextField(caption);
        combo.setDescription(description);
        fields.put(id, combo);
    }

    public void addDateTimeField(String id, String caption, String description) {
        PopupDateField popupDate = new PopupDateField();
        popupDate.setValue(new Date());
        popupDate.setDateFormat("yyyy-MM-dd");
        popupDate.setInputPrompt("Select a date");
        popupDate.setCaption(caption);
        popupDate.setDescription(description);
        dates.put(id, popupDate);
    }

    public void addButton(String id, String name, String caption, String description) {
        MButton button = new MButton(name).withDescription(description);
        buttons.put(id, button);
        buttonCaptions.put(id, caption);
    }

    public void addComboListener(String id, ValueChangeListener listener) {
        this.combos.get(id).addValueChangeListener(listener);
    }

    public void addButtonListener(String id, MClickListener listener) {
        this.buttons.get(id).addClickListener(listener);
    }

    public String getTextFieldValue(String id) {
        return fields.get(id).getValue();
    }

    public Object getComboBoxValue(String id) {
        return combos.get(id).getValue();
    }

    public LocalDate getDateFieldValue(String id) {
        Date date = dates.get(id).getValue();
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public void initComboValues() {
        this.setComboValues("statistic", Arrays.stream(AvailableStatistics.values()).filter(AvailableStatistics::isEnabled).collect(Collectors.toList()));
        this.setComboValues("user", facade.findAllUsers());
        this.setComboValues("project", facade.findAllProjects());
    }

    public void setComboValues(String id, Collection<?> values) {
        combos.get(id).removeAllItems();
        combos.get(id).addItems(values);
    }

    public void setButtonEnables(String id, boolean enabled) {
        buttons.get(id).setEnabled(enabled);
    }


}
