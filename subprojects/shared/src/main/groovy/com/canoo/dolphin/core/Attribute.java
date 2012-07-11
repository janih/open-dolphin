package com.canoo.dolphin.core;

public interface Attribute extends Observable {
    String DATA_ID_PROPERTY = "dataId";

    Object getValue();

    void setValue(Object value);

    String getPropertyName();

    String getDataId();

    long getId();
}