package com.microservices.markupconverter.service;

import com.microservices.markupconverter.entity.Marks;
import com.microservices.markupconverter.entity.Request;

import java.sql.SQLException;
import java.util.Optional;

public interface ConverterService {

    String converter(String markdown);
    String addMarks(Request mark) throws SQLException;
    void deleteMarks(long id) throws SQLException;
    void updateMarks(Request request) throws SQLException;
    Optional<Marks> findMarks(long id);

}
