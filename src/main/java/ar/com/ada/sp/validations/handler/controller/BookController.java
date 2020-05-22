package ar.com.ada.sp.validations.handler.controller;

import ar.com.ada.sp.validations.handler.model.DTO.Book;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

@RestController
@RequestMapping("/books")
public class BookController {

    @PostMapping({" ", "/"})
    public ResponseEntity addNewBook(@Valid @RequestBody Book book) throws URISyntaxException {
        long id = new Random().nextLong() % (100 - 10) + 10;
        book.setId(id);
        return ResponseEntity
                .created(new URI("/books/"+id))
                .body(book);
    }
}
