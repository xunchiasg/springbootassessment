package com.example.demo.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Entity
@Data // Generates getters and setters
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book")
@Builder //lombok builder

public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @NotBlank(message = "ID cannot be blank.")
    @Size(min = 3, message = "ID must be at least 3 characters.")
    @Size(max = 255, message = "ID must not be more than 255 characters.")
    private Long id;

    @Column(name = "title")
    @NotBlank(message = "Title is mandatory")
    @Size(min = 3, message = "Title must be at least 3 characters.")
    @Size(max = 255, message = "ID must not be more than 255 characters.")
    private String title;


    @Column(name = "author")
    @NotBlank(message = "Author is mandatory")
    @Size(min = 16, message = "Author must be at least 10 characters.")
    @Size(max = 128, message = "ID must not be more than 128 characters.")
    private String author;
}






