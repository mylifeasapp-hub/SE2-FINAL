package org.se2final.components;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class Footer extends HorizontalLayout {
    public Footer(){
        Label developedBy = new Label("Developed by dprost2s");
        addComponent(developedBy);
        setSizeUndefined();
    }
}
