package com.example.XDMHPL_Back_end.Repositories;

import com.example.XDMHPL_Back_end.model.Comment;
import com.example.XDMHPL_Back_end.model.Post;
import com.example.XDMHPL_Back_end.model.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CommentRepositoryIntegrationTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveAndFindAll_shouldWorkWithH2() {
        // Tạo user và post đơn giản (chi tiết tuỳ model thực tế)
        Users u = new Users();
        u.setUserName("inttest");
        u.setEmail("int@test.test");
        u = userRepository.save(u);

        Post p = new Post();
        p.setUser(u);
        p.setContent("post for comment");
        p = postRepository.save(p);

        Comment c = new Comment();
        c.setContent("comment h2");
        c.setCreationDate(new Date());
        c.setUser(u);
        c.setPost(p);

        commentRepository.save(c);

        List<Comment> all = commentRepository.findAll();
        assertThat(all).isNotEmpty();
        assertThat(all.get(0).getContent()).isEqualTo("comment h2");
    }
}
