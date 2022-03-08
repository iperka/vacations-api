package com.iperka.vacations.api.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleUserDTO {
    @Schema(description = "User id.", example = "iperka|2dd222awd2", required = true)
    private String id;

    @Schema(description = "Name defined by user.", example = "John Doe", required = false)
    private String name;

    @Schema(description = "Username of given user. If no username is given the email will be used.", example = "johndoe", required = true)
    private String username;

    @Schema(description = "User picture.", example = "https://secure.gravatar.com/avatar/15626c5e0c749cb912f9d1ad48dba440?s=480&r=pg&d=https%3A%2F%2Fssl.gstatic.com%2Fs2%2Fprofiles%2Fimages%2Fsilhouette80.png", required = false)
    private String picture;
}
