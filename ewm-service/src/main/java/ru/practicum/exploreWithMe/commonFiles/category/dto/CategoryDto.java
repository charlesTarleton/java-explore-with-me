package ru.practicum.exploreWithMe.commonFiles.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Long id;

    @NotBlank
    @Size(min = 1, max = 50)
    private String name;
}
