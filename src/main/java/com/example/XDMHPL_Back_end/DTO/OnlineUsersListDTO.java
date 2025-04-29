package com.example.XDMHPL_Back_end.DTO;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OnlineUsersListDTO {
    private List<Integer> onlineUserIds;
    
    public OnlineUsersListDTO(List<Integer> onlineUserIds) {
        this.onlineUserIds = onlineUserIds;
    }
    
    public List<Integer> getOnlineUserIds() {
        return onlineUserIds;
    }
    
    public void setOnlineUserIds(List<Integer> onlineUserIds) {
        this.onlineUserIds = onlineUserIds;
    }
}
