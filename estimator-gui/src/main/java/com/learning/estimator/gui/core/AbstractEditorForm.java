package com.learning.estimator.gui.core;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class implemented by all editors
 *
 * @author rolea
 */
@SuppressWarnings("serial")
public abstract class AbstractEditorForm<T> extends AbstractForm<T> {

    private final String header;
    private Map<String, MButton> buttons;

    public AbstractEditorForm(String header) {
        this.header = header;
        this.buttons = new LinkedHashMap<>();
        setEagerValidation(true);
        setImmediate(true);
    }

    public void addButton(String id, String caption, String description) {
        buttons.put(id, new MButton(caption).withDescription(description));
    }

    public void addButtonListener(String id, ClickListener listener) {
        MButton button = buttons.get(id);
        if (button != null) {
            button.addClickListener(listener);
        }
    }

    public Map<String, MButton> getButtons() {
        return buttons;
    }

    /**
     * Returns an editor for the type
     */
    @Override
    protected Component createContent() {
        MarginInfo margin = new MarginInfo(true, true, true, true);
        MVerticalLayout editor = new MVerticalLayout(new Header(header)).withMargin(margin);
        if (buttons.size() != 0) {
            MHorizontalLayout layout = new MHorizontalLayout();
            buttons.values().forEach(layout::addComponent);
            editor.addComponent(layout);
        }
        editor.addComponents(this.getForm().withMargin(margin), getToolbar());
        return editor;
    }

    protected abstract MFormLayout getForm();

    /**
     * Resets the values to default for all the editors contained in the form
     */
    public abstract void clearFields();

    public void disableNonEditableFields() {

    }

    public abstract T getDataAsObject();

}
