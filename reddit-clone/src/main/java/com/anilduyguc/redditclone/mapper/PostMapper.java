package com.anilduyguc.redditclone.mapper;

import com.anilduyguc.redditclone.dto.PostRequest;
import com.anilduyguc.redditclone.dto.PostResponse;
import com.anilduyguc.redditclone.model.*;
import com.anilduyguc.redditclone.repository.CommentRepository;
import com.anilduyguc.redditclone.repository.VoteRepository;
import com.anilduyguc.redditclone.service.AuthService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.anilduyguc.redditclone.model.VoteType.DOWNVOTE;
import static com.anilduyguc.redditclone.model.VoteType.UPVOTE;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "voteCount", constant = "0")
    @Mapping(target = "user", source = "user")
    Post map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.username")
    PostResponse mapToDto(Post post);


}
