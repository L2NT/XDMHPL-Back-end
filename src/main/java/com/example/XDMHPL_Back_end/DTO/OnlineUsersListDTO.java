package com.example.XDMHPL_Back_end.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnlineUsersListDTO {
    private List<OnlineStatusDTO> onlineUsers;
}
