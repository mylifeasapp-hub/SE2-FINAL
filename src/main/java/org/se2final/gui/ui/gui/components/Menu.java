package org.se2final.gui.ui.gui.components;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import org.se2final.control.LoginCheck;
import org.se2final.gui.ui.service.konstanten.Roles;
import org.se2final.gui.ui.service.konstanten.Views;
import org.se2final.model.objects.dto.User;

public class Menu extends MenuBar {
    User user = (User) VaadinSession.getCurrent().getAttribute(Roles.CURRENT);
    public Menu(){
        MenuBar.MenuItem item1 = this.addItem("", VaadinIcons.MENU, null);

        item1.addItem("Mein Profil", VaadinIcons.SPECIALIST, (MenuBar.Command) menuItem -> UI.getCurrent().getNavigator().navigateTo(Views.PROFILE));
        item1.addItem("Suche", VaadinIcons.SEARCH, (MenuBar.Command) selectedItem -> UI.getCurrent().getNavigator().navigateTo(Views.LANDING));
        item1.addItem("Reservierungen", VaadinIcons.INBOX, (MenuBar.Command) selectedItem -> UI.getCurrent().getNavigator().navigateTo(Views.RESERVATIONS));
        if(user.getRolle().matches(Roles.MITARBEITER)){
            item1.addItem("Meine Autos", VaadinIcons.CAR, (MenuBar.Command) selectedItem -> UI.getCurrent().getNavigator().navigateTo(Views.CARS));
        }
        item1.addItem("Abmelden", VaadinIcons.SIGN_OUT, (MenuBar.Command) selectedItem -> LoginCheck.logoutUser());

    }
}
