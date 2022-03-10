package com.iperka.vacations.api.users.dto;

import com.iperka.vacations.api.helpers.DTO;
import com.iperka.vacations.api.users.User;

import org.springframework.lang.NonNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements DTO<User> {
    @NonNull
    @Schema(description = "Hash of user email.", example = "78e731027d8fd50ed642340b7c9a63b3", required = true)
    private String emailHash;

    @NonNull
    @Schema(description = "Hash of user phone.", example = "78e731027d8fd50ed642340b7c9a63b3", required = true)
    private String phoneHash;

    @Override
    public User toObject() {
        User user = new User();
        user.setEmailHash(emailHash);
        user.setPhoneHash(phoneHash);
        return user;
    }

    @Schema(description = "Defines the email of the user.", nullable = false, example = "78e731027d8fd50ed642340b7c9a63b3")
    public void setEmailHash(String emailHash) {
        this.emailHash = emailHash;
    }

    @Schema(description = "Defines the phone of the user.", nullable = false, example = "78e731027d8fd50ed642340b7c9a63b3")
    public void setPhoneHash(String phoneHash) {
        this.phoneHash = phoneHash;
    }
}
