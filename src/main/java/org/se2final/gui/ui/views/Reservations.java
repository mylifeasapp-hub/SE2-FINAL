package org.se2final.gui.ui.views;


import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.*;
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
        List<Reservation> resList = getItems();


        reservationsGrid.removeAllColumns();
        reservationsGrid.setCaptionAsHtml(true);
        reservationsGrid.setCaption("<div style=\"font-size:30px\">Reservierungen:</div>");
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
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.YYYY");
            String date = dateFormat.format(r.getResDate());
            DateFormat timeFormat = new SimpleDateFormat("HH:mm");
            date = date + " um " + timeFormat.format(r.getResTime()) + " Uhr";
            return date;
        }).setCaption("Zeitpunkt Reservierung:");
        if(user.getRolle().equals(Roles.MITARBEITER)) {
            reservationsGrid.addColumn(r -> {
                String custName = null;
                try {
                    User tempUser = UserDAO.getInstance().getUserByID(r.getUserID());
                    custName = tempUser.getSurname() + ", " + tempUser.getName();
                } catch (SQLException ex) {
                    Logger.getLogger(Reservations.class.getName()).log(Level.SEVERE, null, ex);
                }
                return custName;
            }).setCaption("Kundenname:");
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
        }
        else{
            reservationsGrid.addColumn(r -> {
                String custName = null;
                try {
                    User tempUser = UserDAO.getInstance().getUserByID(r.getWorkID());
                    custName = tempUser.getSurname() + ", " + tempUser.getName();
                } catch (SQLException ex) {
                    Logger.getLogger(Reservations.class.getName()).log(Level.SEVERE, null, ex);
                }
                return custName;
            }).setCaption("Ansprechpartner:");
            reservationsGrid.addColumn(r -> {
                String custMail = null;
                try {
                    User tempUser = UserDAO.getInstance().getUserByID(r.getWorkID());
                    custMail = tempUser.getEmail();
                } catch (SQLException ex) {
                    Logger.getLogger(Reservations.class.getName()).log(Level.SEVERE, null, ex);
                }
                return custMail;
            }).setCaption("Ansprechpartner E-Mail:");

        }
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
        if(user.getRolle().equals(Roles.MITARBEITER)){
            reservationsGrid.addColumn(r -> {
                Button buttonAccept;
                if(r.getResStatus().equals(Status.SENT)){
                    buttonAccept = new Button(VaadinIcons.CHECK_CIRCLE);
                    buttonAccept.addClickListener(event -> {
                        try {
                            Reservation currentReservation = new Reservation();
                            currentReservation.setCarID(r.getCarID());
                            currentReservation.setUserID(r.getUserID());
                            currentReservation.setResStatus(r.getResStatus());
                            currentReservation.setResDate(r.getResDate());
                            currentReservation.setResTime(r.getResTime());

                            ReservationDAO.getInstance().setStatusReservation(currentReservation, Status.ACCEPT);
                            Notification.show("Erfolg:", "Status erfolgreich in \""+Status.ACCEPT+"\" geändert!", Notification.Type.HUMANIZED_MESSAGE);
                            reservationsGrid.setItems(getItems());
                            reservationsGrid.getDataProvider().refreshAll();
                        } catch (Exception ex) {
                            Logger.getLogger(Landing.class.getName()).log(Level.SEVERE, null, ex);
                            Notification.show("Fehler:", "Status konnte nicht geändert werden!", Notification.Type.ERROR_MESSAGE);
                        }
                    });
                }
                else{
                    buttonAccept = new Button("");
                    buttonAccept.setEnabled(false);
                }
                return buttonAccept;
            }, new ComponentRenderer()).setCaption("Annehmen:");

            reservationsGrid.addColumn(r -> {
                Button buttonDecline;
                if(r.getResStatus().equals(Status.SENT)){
                    buttonDecline = new Button(VaadinIcons.CLOSE_CIRCLE);
                    buttonDecline.addClickListener(event -> {
                        try {
                            Reservation currentReservation = new Reservation();
                            currentReservation.setCarID(r.getCarID());
                            currentReservation.setUserID(r.getUserID());
                            currentReservation.setResStatus(r.getResStatus());
                            currentReservation.setResDate(r.getResDate());
                            currentReservation.setResTime(r.getResTime());

                            ReservationDAO.getInstance().setStatusReservation(currentReservation, Status.DECLINE);
                            Notification.show("Erfolg:", "Status erfolgreich in \""+Status.DECLINE+"\" geändert!", Notification.Type.HUMANIZED_MESSAGE);
                            reservationsGrid.setItems(getItems());
                            reservationsGrid.getDataProvider().refreshAll();
                        } catch (Exception ex) {
                            Logger.getLogger(Landing.class.getName()).log(Level.SEVERE, null, ex);
                            Notification.show("Fehler:", "Status konnte nicht geändert werden!", Notification.Type.ERROR_MESSAGE);
                        }
                    });
                }
                else{
                    buttonDecline = new Button("");
                    buttonDecline.setEnabled(false);
                }
                return buttonDecline;
            }, new ComponentRenderer()).setCaption("Ablehnen:");


        }


        reservationsGrid.setWidth("80%");
        this.addComponents(menuBandUser, reservationsGrid);
        this.setComponentAlignment(reservationsGrid, Alignment.TOP_CENTER);
    }

    private List<Reservation> getItems(){
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
        return resList;
    }
}
