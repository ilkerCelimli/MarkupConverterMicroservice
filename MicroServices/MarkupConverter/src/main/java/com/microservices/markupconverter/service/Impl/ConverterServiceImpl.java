package com.microservices.markupconverter.service.Impl;

import com.microservices.markupconverter.entity.Marks;
import com.microservices.markupconverter.entity.Request;
import com.microservices.markupconverter.repository.MarksRepository;
import com.microservices.markupconverter.service.ConverterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConverterServiceImpl implements ConverterService {

    private final MarksRepository marksRepository;
    private final Parser parser;
    private final HtmlRenderer htmlRenderer;


    @Override
    public String converter(String markdown) {
        Node node = parser.parse(markdown);
        return htmlRenderer.render(node);
    }

    @Transactional
    @Override
    public String addMarks(Request mark) throws SQLException {

        Marks m = new Marks();
        m.setMarkString(mark.getRequest());
        m.setHtmlString(converter(mark.getRequest()));
        m.setCreatedDate(new Date());
        Marks temp = marksRepository.save(m);
        if (temp.getId() == 0L) {
            SQLException e = new SQLException();
            log.error(e.getSQLState());
            throw e;
        }
        log.info("Converted {}", mark.getRequest());
        return m.getHtmlString();
    }

    @Override
    @Transactional
    public void deleteMarks(long id) throws SQLException {

        marksRepository.findById(id).ifPresent(a -> {
            marksRepository.delete(a);
            Optional<Marks> m = marksRepository.findById(a.getId());
            if (m.isEmpty()) {
                log.info("deleted {}", a);
            }
        });

        if (marksRepository.findById(id).isPresent()) {
            throw new SQLException();
        }


    }

    @Override
    @Transactional
    public void updateMarks(Request request) throws SQLException {

        Optional<Marks> m = findMarks(request.getId());
        AtomicBoolean atomicBoolean = new AtomicBoolean();
        atomicBoolean.set(false);
        m.ifPresent(a -> {
            Date date = new Date();
            a.setMarkString(request.getRequest());
            a.setHtmlString(converter(request.getRequest()));
            a.setUpdatedDate(date);
            Marks f = marksRepository.save(a);
            if(f.getUpdatedDate().getTime() == date.getTime()) {
                atomicBoolean.set(true);
                log.info("updated {}" , f);
            }
            else log.error("Error {}" ,new SQLException());
        });
    if(atomicBoolean.get() == false) {
        throw new SQLException();
    }

    }

    @Override
    @Transactional
    public Optional<Marks> findMarks(long id) {

        return marksRepository.findById(id);
    }

}
