package com.anilduyguc.redditclone.mapper;

import com.anilduyguc.redditclone.dto.SubredditDto;
import com.anilduyguc.redditclone.model.Post;
import com.anilduyguc.redditclone.model.Subreddit;
import com.anilduyguc.redditclone.model.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubredditMapper {

    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
    SubredditDto mapSubredditToDto(Subreddit subreddit);

    default Integer mapPosts(List<Post> numberOfPosts) {
        return numberOfPosts.size();
    }

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "user", expression = "java(currentUser)")
    Subreddit mapDtoToSubreddit(SubredditDto subredditDto, User currentUser);
}