package com.example.XDMHPL_Back_end.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatBoxDetailId implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int userID;
    private int chatBoxID;
    
    // Constructors, equals, hashCode
}