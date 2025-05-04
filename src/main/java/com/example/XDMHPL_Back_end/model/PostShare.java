package com.example.XDMHPL_Back_end.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sharepost")
@Data
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("SHARE")
public class PostShare extends Post {
    @Column(name = "OriginalPostID", nullable = false)
    private int originalPostID;  
    
    @Column(name = "ParentShareID")
    private Integer parentShareID;  
    
    @ManyToOne
    @JoinColumn(name = "OriginalPostID", insertable = false, updatable = false)
    private Post originalPost;
    
    @ManyToOne
    @JoinColumn(name = "ParentShareID", insertable = false, updatable = false)
    private Post parentPost; 
}