package org.se2final.model.objects.dao;

import org.se2final.model.objects.dto.Cars;

import java.sql.SQLException;
import java.util.List;

public interface SearchI {

    List<Cars> searchCars(String search []) throws SQLException;
}
