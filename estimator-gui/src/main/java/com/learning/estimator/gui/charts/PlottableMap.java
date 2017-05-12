package com.learning.estimator.gui.charts;

import java.util.Map;

/**
 * Abstracts a list of points used to plot a chart
 *
 * @author rolea
 */
public class PlottableMap {

    private String lineName;
    private Map<Object, Number> data;

    public PlottableMap(String lineName, Map<Object, Number> data) {
        this.lineName = lineName;
        this.data = data;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public Map<Object, Number> getData() {
        return data;
    }

    public void setData(Map<Object, Number> data) {
        this.data = data;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        result = prime * result + ((lineName == null) ? 0 : lineName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PlottableMap other = (PlottableMap) obj;
        if (data == null) {
            if (other.data != null)
                return false;
        } else if (!data.equals(other.data))
            return false;
        if (lineName == null) {
            if (other.lineName != null)
                return false;
        } else if (!lineName.equals(other.lineName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PlottableLine [lineName=" + lineName + ", data=" + data + "]";
    }

}
