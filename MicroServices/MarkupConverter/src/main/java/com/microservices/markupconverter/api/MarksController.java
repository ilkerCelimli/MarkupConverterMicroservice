package com.microservices.markupconverter.api;

import com.microservices.markupconverter.entity.Marks;
import com.microservices.markupconverter.entity.Request;
import com.microservices.markupconverter.service.ConverterService;
import com.microservices.markupconverter.service.Impl.ConverterServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@RequestMapping("/marksConverter")
@RestController
public class MarksController {

    private final ConverterService converterService;

    public MarksController(ConverterServiceImpl converterService) {
        this.converterService = converterService;
    }


    @PostMapping(value = "/convert",produces = "application/text")

    public void convertMarkToHtml(@RequestBody Request request, HttpServletResponse res) throws SQLException, IOException {
        String a = converterService.addMarks(request);
        res.getWriter().write(a);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Request> findById(@PathVariable(value = "id") long id) throws Exception {
        Optional<Marks> opt =  this.converterService.findMarks(id);
        if(opt.isPresent()) {
            Request request = new Request();
            request.setRequest(opt.get().getHtmlString());
            return ResponseEntity.ok(request);
        }
        else throw new Exception("Not found");
    }

    @DeleteMapping("/deleteMarks/{id}")
    public ResponseEntity deleteMark(@PathVariable(value = "id") long id) throws SQLException {
        try {
            this.converterService.deleteMarks(id);
            return ResponseEntity.ok().build();
        }
        catch (SQLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
