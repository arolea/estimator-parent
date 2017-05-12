package com.learning.estimator.gui.core;

import com.vaadin.ui.Button.ClickListener;

/**
 * Implemented by button listeners components
 *
 * @author rolea
 */
public interface IButtonsListeners {

    ClickListener getListener(String id);

}
