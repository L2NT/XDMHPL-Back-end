package com.example.XDMHPL_Back_end.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlineStatusDTO {
    private int userId;
    private boolean online;
}
