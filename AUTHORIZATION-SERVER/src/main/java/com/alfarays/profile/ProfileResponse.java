package com.alfarays.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileResponse {

    private String imageId;
    private String name;
    private String path;
    private Long size;
    private String type;
    private String createdAt;
    private String updatedAt;

}
