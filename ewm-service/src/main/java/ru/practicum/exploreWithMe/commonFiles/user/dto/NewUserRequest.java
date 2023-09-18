package ru.practicum.exploreWithMe.commonFiles.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class NewUserRequest {
    @NotBlank
    @Email
    @Length(min = 6, max = 254)
    String email;

    @NotBlank
    @Length(min = 2, max = 250)
    String name;
}
