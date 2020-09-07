package org.se2final.gui.ui.gui.components;



import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import org.se2final.control.DataInputCheck;
import org.se2final.control.ImageUploader;
import org.se2final.gui.ui.service.konstanten.Roles;
import org.se2final.gui.ui.service.konstanten.Views;
import org.se2final.model.objects.dao.CarsDAO;
import org.se2final.model.objects.dto.Cars;
import org.se2final.model.objects.dto.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CarAddEditViewWindow extends Window {
    User user = (User) VaadinSession.getCurrent().getAttribute(Roles.CURRENT);
    static final String EINFOLDER = "/WEB-INF/cars/";
    String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath() + EINFOLDER;
    String filenameAttachement = "";
    Label statusUpload = new Label();
    Button saveButton;
    FormLayout content = new FormLayout();

    public CarAddEditViewWindow(){

    }

    public CarAddEditViewWindow(Cars currentCar){

        //Daten für Suche
        TextField carBrand = new TextField("Automarke:");
        TextField carYear = new TextField("Erstzulassung(Jahr):");
        TextField carModel = new TextField("Modellbezeichnung:");
        TextField carPS = new TextField("Leistung(PS):");
        TextField carMaxSpeed = new TextField("Maximale Geschwindigkeit(km/h):");

        //Bild
        ImageUploaderintern receiver = new ImageUploaderintern();
        Upload upload = new Upload("Anhang hinzufügen:", receiver);
        upload.setImmediateMode(true);
        upload.setWidth("1000px");
        upload.setAcceptMimeTypes("image/jpg");
        upload.setButtonCaption("Bild auswählen ...");
        upload.addSucceededListener(receiver);

        //Beschreibung
        RichTextArea carDescription = new RichTextArea();
        carDescription.setWidth("1000px");

        //Buttons
        HorizontalLayout buttonPane = new HorizontalLayout();

        saveButton = new Button("Speichern");
        Button cancelButton = new Button("Abbrechen");

        buttonPane.addComponents(saveButton, cancelButton);


        if(currentCar!=null){
            this.setCaption("Auto bearbeiten:");
            carBrand.setValue(currentCar.getCarBrand());
            carYear.setValue(currentCar.getCarYear());
            carModel.setValue(currentCar.getCarModel());
            carPS.setValue(String.valueOf(currentCar.getCarPS()));
            carMaxSpeed.setValue(String.valueOf(currentCar.getCarMaxSpeed()));

            carDescription.setValue(currentCar.getCarDescription());

            saveButton.addClickListener(e -> {
                if(DataInputCheck.isNumeric(carPS.getValue())&&DataInputCheck.isNumeric(carMaxSpeed.getValue())){

                    currentCar.setCarBrand(carBrand.getValue());
                    currentCar.setCarYear(carYear.getValue());
                    currentCar.setCarDescription(carDescription.getValue());
                    currentCar.setOwnerID(user.getId());
                    currentCar.setCarMaxSpeed(Integer.parseInt(carMaxSpeed.getValue()));
                    currentCar.setCarPS(Integer.parseInt(carPS.getValue()));
                    currentCar.setCarModel(carModel.getValue());

                    if(filenameAttachement.isEmpty()){
                        currentCar.setCarPicture(currentCar.getCarPicture());
                    }else{
                        currentCar.setCarPicture(filenameAttachement);
                    }


                    CarsDAO.getInstance().updateCar(currentCar);

                    this.close();

                    Notification.show("Das Auto wurde erfolgreich aktualisiert!", "", Notification.Type.HUMANIZED_MESSAGE);
                    UI.getCurrent().getNavigator().navigateTo(Views.CARS);
                }else{
                    Notification.show("Fehler:", "Die Felder Leistung(PS) und maximaler Geschwindigkeit(km/h) akzeptieren nur Ganzzahlwerte!", Notification.Type.ERROR_MESSAGE);
                }



            });


        }else{
            this.setCaption("Auto hinzufügen:");
            saveButton.addClickListener(e -> {
                if(DataInputCheck.isNumeric(carPS.getValue())&&DataInputCheck.isNumeric(carMaxSpeed.getValue())){
                    Cars tempCar = new Cars();

                    tempCar.setCarBrand(carBrand.getValue());
                    tempCar.setCarYear(carYear.getValue());
                    tempCar.setCarDescription(carDescription.getValue());
                    tempCar.setCarPicture(filenameAttachement);
                    tempCar.setOwnerID(user.getId());
                    tempCar.setCarMaxSpeed(Integer.parseInt(carMaxSpeed.getValue()));
                    tempCar.setCarPS(Integer.parseInt(carPS.getValue()));
                    tempCar.setCarModel(carModel.getValue());


                    CarsDAO.getInstance().enterCar(tempCar);

                    this.close();


                    Notification.show("Das Auto wurde erfolgreich im System registriert!", "", Notification.Type.HUMANIZED_MESSAGE);
                    UI.getCurrent().getNavigator().navigateTo(Views.CARS);
                }else{
                    Notification.show("Fehler:", "Die Felder Leistung(PS) und maximaler Geschwindigkeit(km/h) akzeptieren nur Ganzzahlwerte!", Notification.Type.ERROR_MESSAGE);
                }



            });

        }


        buttonPane.setSizeUndefined();

        content.addComponents(carBrand, carModel, carMaxSpeed, carPS, carYear, upload, statusUpload, carDescription, buttonPane);


        content.setSizeUndefined(); // Shrink to fit
        content.setMargin(true);
        setContent(content);
        center();
        setClosable(false);
        setResizable(false);
        setModal(true);

        //Click Listener
        cancelButton.addClickListener(event -> {
            if(filenameAttachement.isEmpty()){
                this.close();
            }else{
                File image = new File(VaadinService.getCurrent().getBaseDirectory().getAbsolutePath() +"/WEB-INF/cars/"+filenameAttachement+".jpg");
                image.delete();
                this.close();
            }
        });


    }

    public void carView(int carID){
        content.removeAllComponents();

        Cars tempCar = null;
        try {
            tempCar = CarsDAO.getInstance().getCarByID(carID);
        } catch (SQLException ex) {
            Logger.getLogger(CarAddEditViewWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Überschrift
        this.setCaption(tempCar.getCarBrand() + " - "+tempCar.getCarModel());

        //Bild
        Image image = new Image("", ImageUploader.carPicture(tempCar.getCarPicture()));
        image.setWidth(800, Unit.PIXELS);

        //Felder auslesen
        Label carYear = new Label(tempCar.getCarYear());
        carYear.setCaption("Baujahr:");
        Label carDescription = new Label(tempCar.getCarDescription(), ContentMode.HTML);
        Label carPs = new Label(String.valueOf(tempCar.getCarPS())+" PS");
        carPs.setCaption("Leistung:");
        Label carMaxSpeed = new Label(String.valueOf(tempCar.getCarMaxSpeed())+" km/h");
        carMaxSpeed.setCaption("Maximalgeschwindigkeit:");

        HorizontalLayout keyFacts = new HorizontalLayout();
        keyFacts.setWidth(800, Unit.PIXELS);
        keyFacts.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        keyFacts.addComponents(carYear, carPs, carMaxSpeed);

        //Buttons
        Button buttonClose = new Button("Schließen");
        Button buttonReservation = new Button("Reservieren");

        HorizontalLayout buttonPane = new HorizontalLayout();
        buttonPane.setWidth(800, Unit.PIXELS);
        buttonPane.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        if(user.getRolle().equals(Roles.KUNDE)){
            buttonPane.addComponents(buttonReservation, buttonClose);
        }else {
            buttonPane.addComponent(buttonClose);
        }


        content.addComponents(image, keyFacts, carDescription, buttonPane);
        content.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        content.setSizeUndefined(); // Shrink to fit
        content.setMargin(true);
        setContent(content);
        center();
        setClosable(false);
        setResizable(false);
        setModal(true);

        //Click Listener für Buttons
        buttonClose.addClickListener(event -> this.close());

        buttonReservation.addClickListener(event -> {
            this.close();
            ReservationWindow newReservation = null;
            try {
                newReservation = new ReservationWindow(carID);
            } catch (SQLException ex) {
                Logger.getLogger(CarAddEditViewWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
            UI.getCurrent().addWindow(newReservation);
        });
    }

    class ImageUploaderintern implements Upload.Receiver, Upload.SucceededListener {
        private File file;

        public OutputStream receiveUpload(String filename,
                                          String mimeType) {
            // Create uploadProfilbild stream
            FileOutputStream fos; // Stream to write to
            try {
                // Open the file for writing.
                filenameAttachement = user.getId() + String.valueOf(System.currentTimeMillis());
                file = new File(basepath + filenameAttachement + ".jpg"); /// Dazwischen filename noch die USERID ?!
                fos = new FileOutputStream(file);

            } catch (final FileNotFoundException e) {
                new Notification("Datei konnte nicht gefunden werden.",
                        e.getMessage(),
                        Notification.Type.ERROR_MESSAGE)
                        .show(Page.getCurrent());
                return null;
            }
            return fos; // Return the output stream to write to
        }
        public void uploadSucceeded(Upload.SucceededEvent event) {
            // Content for the PopupView
            statusUpload.setCaption("Upload erfolgreich!");
            statusUpload.setValue("Dateiname lautet: "+filenameAttachement+".jpg");

        }
    }
}
