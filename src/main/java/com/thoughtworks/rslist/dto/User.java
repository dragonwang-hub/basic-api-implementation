package com.thoughtworks.rslist.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @NotEmpty
    @Size(max = 8)
    private String userName;

    @NotNull
    @Min(18)
    @Max(100)
    private int age;

    @NotEmpty
    private String gender;

    @Email
    private String email;

    @Pattern(regexp = "^1[0-9]{10}$")
    private String phone;
}
