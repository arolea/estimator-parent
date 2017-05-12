package com.learning.estimator.gui.core;

import com.learning.estimator.common.logger.ILogger;
import com.learning.estimator.common.logger.LogManager;
import com.learning.estimator.gui.utils.LogArea;
import com.learning.estimator.gui.utils.LogColor;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.fields.MValueChangeEvent;

import java.util.Map;

/**
 * Generic implementation for CRUD editor listeners
 *
 * @author rolea
 */
public abstract class AbstractEditorListeners<T> {

    private static final ILogger LOG = LogManager.getLogger(AbstractEditorListeners.class);
    @Autowired
    private LogArea logArea;

    public void addEntity(ClickEvent e) {
        try {
            T entity = createNewEntity();
            getRepresentation().setTableValue(entity);
            getEditor().setEntity(entity);
            if (getEditor().getPopup() == null) {
                Window window = getEditor().openInModalPopup();
                window.addCloseListener((event) -> {
                    this.cancelEditEntity(null);
                });
            }
        } catch (Exception ex) {
            LOG.error(ex);
        }
    }

    public void deleteEntity(T e) {
        try {
            T entity = getRepresentation().getTableValue();
            if (entity != null) {
                performDelete(entity);
                listEntities();
                getEditor().closePopup();
            } else {
                Notification.show("No entity selected !");
            }
        } catch (Exception exception) {
            logArea.logInfo("Error encountered while deleting entity", LogColor.RED);
            LOG.error("Exception encountered while deleting entity", exception);
        } finally {
        }
    }

    public void saveEntity(T entity) {
        try {
            if (entity == null)
                entity = getEditor().getDataAsObject();
            T newEntity = performSave(entity);
            getRepresentation().setTableValue(newEntity);
        } catch (Exception exception) {
            LOG.error("Exception encountered while saving entity", exception);
            logArea.logInfo("Exception occured while saving entity", LogColor.RED);
        } finally {
            listEntities();
            getEditor().closePopup();
        }
    }

    public void selectEntity(MValueChangeEvent<T> entity) {
        try {
            if (getRepresentation().getTableValue() != null && entity.getValue() != null && getEditor().getPopup() == null) {
                Window window = getEditor().openInModalPopup();
                window.addCloseListener((event) -> {
                    this.cancelEditEntity(null);
                });
                editEntity(entity.getValue());
                getEditor().disableNonEditableFields();
            }
        } catch (Exception ex) {
            LOG.error(ex);
        }
    }

    public void cancelEditEntity(T entity) {
        try {
            listEntities();
            getEditor().closePopup();
        } catch (Exception ex) {
            LOG.error(ex);
        }
    }

    /**
     * Override this in order to provide additional listeners
     */
    public Map<String, Button.ClickListener> getAdditionalListeners() {
        return null;
    }

    private void editEntity(T entity) {
        try {
            getEditor().setEntity(entity);
            getEditor().focusFirst();
        } catch (Exception exception) {
            LOG.error("Error encountered while editting entity", exception);
            logArea.logInfo("Exception occured while editing entity", LogColor.RED);
        }
    }

    protected void listEntities() {
        getRepresentation().init();
        getRepresentation().setSelected(null);
        getEditor().clearFields();
    }

    public abstract T performSave(T entity);

    public abstract void performDelete(T entity);

    public abstract T createNewEntity();

    public abstract AbstractDataRepresentation<T> getRepresentation();

    public abstract AbstractEditorForm<T> getEditor();

    public abstract boolean isAddEnabled();

    public abstract boolean isDeleteEnabled();

    public abstract boolean isSelectEnabled();

    public abstract boolean isSaveEnabled();

    public abstract boolean isCancelEnabled();

}
