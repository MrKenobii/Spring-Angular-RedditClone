package com.anilduyguc.redditclone.service;

import com.anilduyguc.redditclone.exceptions.SpringRedditException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

public class CommentServiceTest {

    @Test
    void saveComment() {
    }

    @Test
    void getAllCommentsForPost() {
    }

    @Test
    void getAllCommentsForUser() {
    }

    @Test
    @DisplayName("Test should pass when comment does not contain any swears")
    void containsSwearWords() {
        CommentService commentService = new CommentService(null, null, null, null, null ,null , null);
        //Assertions.assertFalse(commentService.containsSwearWords("This is a clean comment"));
        assertThat(commentService.containsSwearWords("This is a clean comment")).isFalse();
    }

    @Test
    @DisplayName("Test should fail when comment contains swears")
    void shouldFailWhenSwearExists() {
          CommentService commentService = new CommentService(null, null, null, null, null ,null , null);
//        SpringRedditException exception = assertThrows(SpringRedditException.class,() -> {
//            commentService.containsSwearWords("This is a shitty comment");
//        });
//        Assertions.assertTrue(exception.getMessage().contains("Comments contains unacceptable language"));
          assertThatThrownBy(() -> {
              commentService.containsSwearWords("This is a shitty comment");
          }).isInstanceOf(SpringRedditException.class)
                  .hasMessage("Comments contains unacceptable language");
    }
}