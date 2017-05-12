package com.learning.estimator.gui.utils;

import com.learning.estimator.gui.core.AbstractDataRepresentation;
import com.learning.estimator.gui.core.AbstractEditorForm;
import com.learning.estimator.gui.core.AbstractEditorListeners;
import com.learning.estimator.gui.core.IButtonsListeners;

/**
 * Defines utility methods
 *
 * @author rolea
 */
public final class GuiUtils {

    /**
     * Binds the listeners to the data representation and editors
     */
    public static final <T> void bindPresentations(AbstractDataRepresentation<T> data, AbstractEditorForm<T> editor, AbstractEditorListeners<T> listeners) {
        if (listeners.isSelectEnabled())
            data.addSelectListener(listeners::selectEntity);
        if (listeners.isAddEnabled())
            data.addAddListener(listeners::addEntity);

        if (listeners.isDeleteEnabled())
            editor.setDeleteHandler(listeners::deleteEntity);
        if (listeners.isSaveEnabled())
            editor.setSavedHandler(listeners::saveEntity);
        if (listeners.isCancelEnabled())
            editor.setResetHandler(listeners::cancelEditEntity);
    }

    /**
     * Binds the listeners to the buttons
     */
    public static final <T> void bindButtons(AbstractEditorForm<T> hostButtonsComponent, IButtonsListeners hostButtonsListeners) {
        hostButtonsComponent.getButtons().forEach((commandId, button) -> {
            button.addClickListener(hostButtonsListeners.getListener(commandId));
        });
    }

}
