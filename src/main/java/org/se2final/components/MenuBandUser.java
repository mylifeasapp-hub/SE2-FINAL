package org.se2final.components;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;

public class MenuBandUser extends HorizontalLayout {
    public MenuBandUser(){
        HorizontalLayout buttonPannel = new HorizontalLayout();
        this.setSizeFull();
        this.setHeight("100px");
        this.addStyleName("border_bottom");

        Label pageTitle = new Label("<div style=\"font-size:50px\">CARPOOL by CarLook Ltd.</div>", ContentMode.HTML);

        Label userName = new Label("Hallo, \"Username\"!", ContentMode.HTML);

        Menu menu = new Menu();

        buttonPannel.addComponents(userName, menu);
        this.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
        this.addComponents(pageTitle,buttonPannel);
        this.setComponentAlignment(pageTitle, Alignment.MIDDLE_LEFT);


    }
}
