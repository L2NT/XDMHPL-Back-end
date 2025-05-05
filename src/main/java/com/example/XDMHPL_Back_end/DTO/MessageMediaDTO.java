package com.example.XDMHPL_Back_end.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageMediaDTO {
    private int messageMediaID;
    private String mediaType;
    private String mediaURL;
}
