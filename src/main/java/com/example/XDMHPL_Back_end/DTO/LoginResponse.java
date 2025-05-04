package com.example.XDMHPL_Back_end.DTO;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String sessionId;
    private UserDTO user;
    private String message;

}