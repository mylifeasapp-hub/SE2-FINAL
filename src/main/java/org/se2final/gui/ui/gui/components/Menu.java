package org.se2final.gui.ui.gui.components;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import org.se2final.control.LoginCheck;
import org.se2final.gui.ui.service.konstanten.Views;

public class Menu extends MenuBar {
    public Menu(){
        MenuBar.MenuItem item1 = this.addItem("", VaadinIcons.MENU, null);

        item1.addItem("Mein Profil", VaadinIcons.SPECIALIST, (MenuBar.Command) menuItem -> UI.getCurrent().getNavigator().navigateTo(Views.PROFILE));
        item1.addItem("Suche", VaadinIcons.SEARCH, (MenuBar.Command) selectedItem -> UI.getCurrent().getNavigator().navigateTo(Views.SEARCH));
        item1.addItem("Reservierungen", VaadinIcons.INBOX, (MenuBar.Command) selectedItem -> UI.getCurrent().getNavigator().navigateTo(Views.RESERVATIONS));
        item1.addItem("Abmelden", VaadinIcons.SIGN_OUT, (MenuBar.Command) selectedItem -> LoginCheck.logoutUser());
    }
}
