package org.se2final.gui.ui.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.se2final.gui.ui.gui.components.MenuBandUser;
import org.se2final.gui.ui.service.konstanten.Roles;
import org.se2final.gui.ui.service.konstanten.Views;
import org.se2final.model.objects.dto.User;

public class Landing extends VerticalLayout implements View {
    User user;

    public void enter(ViewChangeListener.ViewChangeEvent event) {
        user = (User) VaadinSession.getCurrent().getAttribute(Roles.CURRENT);

        //Wenn eingeloggt -> Landing Page, sonst -> Startseite
        if(user == null){
            UI.getCurrent().getNavigator().navigateTo(Views.START);
        }else {
            this.setUp();
        }
    }

    public void setUp(){
        final MenuBandUser menuBandUser = new MenuBandUser();

        this.addComponent(menuBandUser);

    }
}
