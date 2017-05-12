package com.learning.estimator.gui.utils;

/**
 * Defines available log colors for the GUI log area
 *
 * @author rolea
 */
public enum LogColor {

    GREEN("<font color=\"green\">", "</font>"),
    RED("<font color=\"red\">", "</font>"),
    ORANGE("<font color=\"orange\">", "</font>"),
    BLUE("<font color=\"blue\">", "</font>");

    private String openTag;
    private String closeTag;

    private LogColor(String openTag, String closeTag) {
        this.openTag = openTag;
        this.closeTag = closeTag;
    }

    public String getWrappedMessage(String message) {
        return openTag + message + closeTag;
    }

}
