package org.se2final.components;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class MenuBand extends HorizontalLayout {
    public MenuBand(){
        HorizontalLayout buttonPannel = new HorizontalLayout();
        this.setSizeFull();
        this.setHeight("100px");
        this.addStyleName("border_bottom");

        Button buttonRegister = new Button("Registrieren");
        Button buttonLogin = new Button("Anmelden");

        buttonPannel.addComponents(buttonRegister, buttonLogin);
        this.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
        this.addComponent(buttonPannel);
    }
}
