package com.thoughtworks.rslist.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("user_name")
    private String userName;

    @NotNull
    @Min(18)
    @Max(100)
    @JsonProperty("user_age")
    private int age;

    @NotEmpty
    @JsonProperty("user_gender")
    private String gender;

    @Email
    @JsonProperty("user_email")
    private String email;

    @Pattern(regexp = "^1[0-9]{10}$")
    @JsonProperty("user_phone")
    private String phone;

    @JsonIgnore
    private int voteNumb;

    public User(String userName, int age, String gender, String email, String phone) {
        this.userName = userName;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
    }
}
