package com.example.XDMHPL_Back_end.model;
import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "friends")
@NoArgsConstructor
@Data
@AllArgsConstructor
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int friendID;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "FriendUserID")
    private Users friendUser;

    @Enumerated(EnumType.STRING) // Lưu giá trị enum dưới dạng chuỗi (ACCEPTED, PENDING)
    @Column(name = "Status")
    private FriendStatus status;

    @Column(name = "CreatedAt")
    private Date createdAt;

}
