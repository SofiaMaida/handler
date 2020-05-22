package ar.com.ada.sp.validations.handler.model.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Getter @Setter
@NoArgsConstructor
@JsonPropertyOrder({"id", "title", "author", "year"})
public class Book {

    private Long id;
    @NotBlank(message = "title is requeride")
    @Pattern(regexp = "^[0-9a-zA-ZáéíóúÁÉÍÓÚÜüñÑ\\s]*$", message = "title contains nor allowed characters")
    private String title;

    @NotBlank(message = "author is requeride")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚÜüñÑ\\s]*$", message = "author contains not allowed characters")
    private String author;

    @JsonFormat(pattern = "yyyy")
    @NotNull(message = "year is requeride")
    @PastOrPresent(message = "the year must be past or present")
    private Date year;

    public Book(Long id, String title, String author, Date year) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public Book(String title, String author, Date year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", year=" + year +
                '}';
    }
}
