package com.cledson_leite.library_api.api.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
public class BookDto {
    private String title;
    private String author;
    private String isbn;
}
