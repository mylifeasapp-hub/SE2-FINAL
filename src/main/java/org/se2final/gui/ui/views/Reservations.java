package org.se2final.gui.ui.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.renderers.LocalDateRenderer;
import com.vaadin.ui.renderers.LocalDateTimeRenderer;
import org.se2final.gui.ui.gui.components.MenuBandUser;
import org.se2final.gui.ui.service.konstanten.Roles;
import org.se2final.gui.ui.service.konstanten.Status;
import org.se2final.gui.ui.service.konstanten.Views;
import org.se2final.model.objects.dao.CarsDAO;
import org.se2final.model.objects.dao.ReservationDAO;
import org.se2final.model.objects.dao.UserDAO;
import org.se2final.model.objects.dto.Cars;
import org.se2final.model.objects.dto.Reservation;
import org.se2final.model.objects.dto.User;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Reservations extends VerticalLayout implements View {
    User user = (User) VaadinSession.getCurrent().getAttribute(Roles.CURRENT);;
    Grid<Reservation> reservationsGrid = new Grid<>();

    public void enter(ViewChangeListener.ViewChangeEvent event) {
        //Wenn eingeloggt -> Landing Page, sonst -> Startseite
        if(user == null){
            UI.getCurrent().getNavigator().navigateTo(Views.START);
        }else {
            this.setUp();
        }
    }

    public void setUp(){
        final MenuBandUser menuBandUser = new MenuBandUser();

        reservationsGrid.setSizeUndefined();
        List<Reservation> resList = null;
        try {
            if (user.getRolle().equals(Roles.MITARBEITER)) {
                resList = ReservationDAO.getInstance().getReservationsForWorker(user.getId());
            } else if (user.getRolle().equals(Roles.KUNDE)) {
                resList = ReservationDAO.getInstance().getReservationsForCustomer(user.getId());
            }
        }catch (SQLException ex){
            Logger.getLogger(Reservations.class.getName()).log(Level.SEVERE, null, ex);
        }

        reservationsGrid.removeAllColumns();
        reservationsGrid.setCaptionAsHtml(true);
        reservationsGrid.setCaption("Reservierungen:");
        reservationsGrid.setHeightByRows(!resList.isEmpty() ? resList.size() : 1);
        reservationsGrid.setItems(resList);

        reservationsGrid.addColumn(r -> {
            if(r.getResStatus().equals(Status.SENT)&&user.getRolle().equals(Roles.MITARBEITER)){
                return Status.RECEIVED;
            }
            else{
                return r.getResStatus();
            }
        }).setCaption("Status:");
        reservationsGrid.addColumn(r -> {
            String custName = null;
            try {
                User tempUser = UserDAO.getInstance().getUserByID(r.getUserID());
                custName = tempUser.getSurname() + ", "+ tempUser.getName();
            } catch (SQLException ex) {
                Logger.getLogger(Reservations.class.getName()).log(Level.SEVERE, null, ex);
            }
            return custName;
        }).setCaption("Kundenname:");
        reservationsGrid.addColumn(r -> {
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.YYYY");
            String date = dateFormat.format(r.getResDate());
            DateFormat timeFormat = new SimpleDateFormat("HH:mm");
            date = date + " um " + timeFormat.format(r.getResTime()) + " Uhr";
            return date;
        }).setCaption("Zeitpunkt Reservierung:");
        reservationsGrid.addColumn(r -> {
            String custMail = null;
            try {
                User tempUser = UserDAO.getInstance().getUserByID(r.getUserID());
                custMail = tempUser.getEmail();
            } catch (SQLException ex) {
                Logger.getLogger(Reservations.class.getName()).log(Level.SEVERE, null, ex);
            }
            return custMail;
        }).setCaption("Kunden E-Mail:");
        reservationsGrid.addColumn(reservation -> {
            String carType = null;
            try {
                Cars tempCar = CarsDAO.getInstance().getCarByID(reservation.getCarID());
                carType = tempCar.getCarBrand() + " - " +tempCar.getCarModel()+" - "+tempCar.getCarYear();
            } catch (SQLException ex) {
                Logger.getLogger(Reservations.class.getName()).log(Level.SEVERE, null, ex);
            }
            return carType;
        }).setCaption("Auto-Modell:");



        reservationsGrid.setWidth("80%");
        this.addComponents(menuBandUser, reservationsGrid);
        this.setComponentAlignment(reservationsGrid, Alignment.TOP_CENTER);
    }
}
