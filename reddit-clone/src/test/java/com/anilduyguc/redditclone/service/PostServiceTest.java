package com.anilduyguc.redditclone.service;

import com.anilduyguc.redditclone.dto.PostRequest;
import com.anilduyguc.redditclone.dto.PostResponse;
import com.anilduyguc.redditclone.mapper.PostMapper;
import com.anilduyguc.redditclone.model.Post;
import com.anilduyguc.redditclone.model.Subreddit;
import com.anilduyguc.redditclone.model.User;
import com.anilduyguc.redditclone.repository.PostRepository;
import com.anilduyguc.redditclone.repository.SubredditRepository;
import com.anilduyguc.redditclone.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static java.util.Collections.emptyList;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock
    private PostRepository postRepository;
    @Mock
    private SubredditRepository subredditRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthService authService;
    @Mock
    private PostMapper postMapper;
    @Captor
    private ArgumentCaptor<Post> postArgumentCaptor;
    private PostService postService;

    @BeforeEach
    public void setUp(){
        postService = new PostService(postRepository, subredditRepository, userRepository, authService, postMapper);
    }

    @Test
    @DisplayName("Should save post")
    void shouldSavePost() {

        User currentUser = new User(123L, "test user", "secret password", "user@email.com", Instant.now(), true);
        Subreddit subreddit = new Subreddit(123L, "First Subreddit", "Subreddit Description", emptyList(), Instant.now(), currentUser);
        Post post = new Post(123L, "First Post", "http://url.site", "Test",
                0, null, Instant.now(), null);
        PostRequest postRequest = new PostRequest(null, "First Subreddit", "First Post", "http://url.site", "Test");

        Mockito.when(subredditRepository.findByName("First Subreddit"))
                .thenReturn(Optional.of(subreddit));
        Mockito.when(authService.getCurrentUser())
                .thenReturn(currentUser);
        Mockito.when(postMapper.map(postRequest, subreddit, currentUser))
                .thenReturn(post);

        postService.save(postRequest);
        Mockito.verify(postRepository, Mockito.times(1)).save(postArgumentCaptor.capture());

        Assertions.assertThat(postArgumentCaptor.getValue().getPostId()).isEqualTo(post.getPostId());
        Assertions.assertThat(postArgumentCaptor.getValue().getPostName()).isEqualTo(post.getPostName());
    }

    @Test
    @DisplayName("Should find post by id")
    void shouldFindPostById() {

        Post post = new Post(123L, "First Post", "https://google.com", "Test", 0 , null, Instant.now(), null);
        PostResponse expectedPostResponse = new PostResponse(123L, "First Post", "http://url.site", "Test",
                "Test User", "Test Subredit", 0, 0, "1 Hour Ago", false, false);
        Mockito.when(postRepository.findById(123L)).thenReturn(Optional.of(post));
        Mockito.when(postMapper.mapToDto(Mockito.any(Post.class))).thenReturn(expectedPostResponse);

        PostResponse actualResponse = postService.getPost(123L);
        Assertions.assertThat(actualResponse.getId()).isEqualTo(expectedPostResponse.getId());
        Assertions.assertThat(actualResponse.getUsername()).isEqualTo(expectedPostResponse.getUsername());
    }

    @Test
    void getAllPosts() {
    }

    @Test
    void getPostsBySubreddit() {
    }

    @Test
    void getPostsByUsername() {
    }
}