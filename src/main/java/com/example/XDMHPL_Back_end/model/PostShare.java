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
public class PostShare  extends Post{
	@Column(name = "OriginalPostID", nullable = false)
    private int originalPostID;  // Luôn lưu ID của bài đăng gốc ban đầu
    
    @Column(name = "ParentShareID")
    private Integer parentShareID;  // ID của bài đăng share trực tiếp (có thể null nếu share từ bài gốc)
    
    @ManyToOne
    @JoinColumn(name = "OriginalPostID", insertable = false, updatable = false)
    private Post originalPost;
    
    @ManyToOne
    @JoinColumn(name = "ParentShareID", insertable = false, updatable = false)
    private PostShare parentShare;  // Bài đăng share mà người dùng đã share từ đó
    
}
