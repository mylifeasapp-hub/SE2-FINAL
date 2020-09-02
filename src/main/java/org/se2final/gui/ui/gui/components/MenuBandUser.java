package org.se2final.gui.ui.gui.components;

import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.se2final.gui.ui.service.konstanten.Roles;
import org.se2final.model.objects.dto.User;

public class MenuBandUser extends HorizontalLayout {
    public MenuBandUser(){
        HorizontalLayout buttonPannel = new HorizontalLayout();
        this.setSizeFull();
        this.setHeight("100px");
        this.addStyleName("border_bottom");

        User user = (User) VaadinSession.getCurrent().getAttribute(Roles.CURRENT);

        Label pageTitle = new Label("<div style=\"font-size:50px\">CARPOOL by CarLook Ltd.</div>", ContentMode.HTML);

        Label userName = new Label("Hallo, " + user.getName() + " " + user.getSurname()+"!", ContentMode.HTML);

        Menu menu = new Menu();

        buttonPannel.addComponents(userName, menu);
        this.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
        this.addComponents(pageTitle,buttonPannel);
        this.setComponentAlignment(pageTitle, Alignment.MIDDLE_LEFT);


    }
}
