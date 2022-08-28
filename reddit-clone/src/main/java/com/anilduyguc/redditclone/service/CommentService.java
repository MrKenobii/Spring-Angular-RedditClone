package com.anilduyguc.redditclone.service;

import com.anilduyguc.redditclone.dto.CommentDto;
import com.anilduyguc.redditclone.exceptions.PostNotFoundException;
import com.anilduyguc.redditclone.mapper.CommentMapper;
import com.anilduyguc.redditclone.model.Comment;
import com.anilduyguc.redditclone.model.NotificationEmail;
import com.anilduyguc.redditclone.model.Post;
import com.anilduyguc.redditclone.model.User;
import com.anilduyguc.redditclone.repository.CommentRepository;
import com.anilduyguc.redditclone.repository.PostRepository;
import com.anilduyguc.redditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class CommentService {
    private static final String POST_URL = "";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;
    private final CommentRepository commentRepository;
    public void saveComment(CommentDto commentDto){
        Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentDto.getPostId().toString()));
        Comment comment = commentMapper.map(commentDto, post, authService.getCurrentUser());
        commentRepository.save(comment);

        String message = mailContentBuilder.build(post.getUser().getUsername() + " posted a comment on your post." + POST_URL);
        sendCommentNotification(message, post.getUser());
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(user.getUsername() + " Commented on your post", user.getEmail(), message));
    }

    public List<CommentDto> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());

    }

    public List<CommentDto> getAllCommentsForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
