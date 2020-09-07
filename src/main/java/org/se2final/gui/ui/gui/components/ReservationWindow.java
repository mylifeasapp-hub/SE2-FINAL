package org.se2final.gui.ui.gui.components;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import org.se2final.gui.ui.service.konstanten.Roles;
import org.se2final.gui.ui.service.konstanten.Status;
import org.se2final.model.objects.dao.CarsDAO;
import org.se2final.model.objects.dao.ReservationDAO;
import org.se2final.model.objects.dto.Cars;
import org.se2final.model.objects.dto.Reservation;
import org.se2final.model.objects.dto.User;

import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.sql.Date;

public class ReservationWindow extends Window {
    User user = (User) VaadinSession.getCurrent().getAttribute(Roles.CURRENT);
    public ReservationWindow(int carID) throws SQLException {
        super("Auto reservieren:");
        FormLayout content = new FormLayout();

        Cars currentCar = CarsDAO.getInstance().getCarByID(carID);

        Label carTitle = new Label();
        carTitle.setValue("Modell:");
        Label carBrand = new Label(currentCar.getCarBrand());
        Label carModell = new Label(currentCar.getCarModel());
        Label carYear = new Label(currentCar.getCarYear());


        DateTimeField resDateTime = new DateTimeField("Reservierungszeit");
        resDateTime.setValue(LocalDateTime.now());
        resDateTime.setDateFormat("dd.MM.yyyy hh:mm");



        //Buttons
        HorizontalLayout buttonPane = new HorizontalLayout();
        Button reserveButton = new Button("Speichern");
        Button cancelButton = new Button("Abbrechen");
        buttonPane.addComponents(reserveButton, cancelButton);
        buttonPane.setSizeUndefined();

        content.addComponents(carTitle, carBrand, carModell, carYear, resDateTime, buttonPane);

        content.setSizeUndefined(); // Shrink to fit
        content.setMargin(true);
        setContent(content);
        center();
        setClosable(false);
        setResizable(false);

        //Click Listener
        cancelButton.addClickListener(event -> this.close());

        reserveButton.addClickListener(event -> {
            Reservation newReservation = new Reservation();
            newReservation.setResDate(Date.valueOf(resDateTime.getValue().toLocalDate()));
            newReservation.setResTime(Time.valueOf(resDateTime.getValue().toLocalTime()));
            newReservation.setCarID(currentCar.getCarID());
            newReservation.setUserID(user.getId());
            newReservation.setResStatus(Status.SENT);

            ReservationDAO.getInstance().sendReservation(newReservation);

            this.close();
        });
    }
}
