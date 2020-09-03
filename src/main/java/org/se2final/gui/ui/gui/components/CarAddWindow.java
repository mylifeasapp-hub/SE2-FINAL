package org.se2final.gui.ui.gui.components;


import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import com.vaadin.ui.Image;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import org.se2final.control.DataInputCheck;
import org.se2final.control.ImageUploader;
import org.se2final.gui.ui.MyUI;
import org.se2final.gui.ui.service.konstanten.Roles;
import org.se2final.model.objects.dao.CarsDAO;
import org.se2final.model.objects.dto.Cars;
import org.se2final.model.objects.dto.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;


public class CarAddWindow extends Window {
    User user = (User) VaadinSession.getCurrent().getAttribute(Roles.CURRENT);
    static final String EINFOLDER = "/WEB-INF/cars/";
    String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath() + EINFOLDER;
    String filenameAttachement = "";
    Label statusUpload = new Label();

    public CarAddWindow(){
        super("Auto hinzufügen:");
        FormLayout content = new FormLayout();

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
        Button loginButton = new Button("Speichern");
        Button cancelButton = new Button("Abbrechen");
        buttonPane.addComponents(loginButton, cancelButton);
        buttonPane.setSizeUndefined();

        content.addComponents(carBrand, carModel, carMaxSpeed, carPS, carYear, upload, statusUpload, carDescription, buttonPane);


        content.setSizeUndefined(); // Shrink to fit
        content.setMargin(true);
        setContent(content);
        center();
        setClosable(false);
        setResizable(false);

        //Click Listener
        cancelButton.addClickListener(event -> this.close());

        loginButton.addClickListener(e -> {
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
                Page.getCurrent().reload();


                Notification.show("Das Auto wurde erfolgreich im System registriert!", "", Notification.Type.HUMANIZED_MESSAGE);
            }else{
                Notification.show("Fehler:", "Die Felder Leistung(PS) und maximaler Geschwindigkeit(km/h) akzeptieren nur Ganzzahlwerte!", Notification.Type.ERROR_MESSAGE);
            }



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
