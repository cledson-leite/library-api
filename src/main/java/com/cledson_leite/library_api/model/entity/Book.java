package com.cledson_leite.library_api.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity // anotação informa que é uma entidade jpa
@Table( name = "livros") //essa dar um nome a tabela
public class Book {
    @Id
    @Column(name = "codigo") // dar um nome a coluna, caso não tenha o nome sera o mesmo do atribudo
    @GeneratedValue(strategy = GenerationType.IDENTITY) //gera um id automaticamente
    private Long id;
    private String title;
    private String author;
    private String isbn;
}
