package com.anilduyguc.redditclone.service;

import com.anilduyguc.redditclone.dto.SubredditDto;
import com.anilduyguc.redditclone.exceptions.SpringRedditException;
import com.anilduyguc.redditclone.mapper.SubredditMapper;
import com.anilduyguc.redditclone.model.Subreddit;
import com.anilduyguc.redditclone.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {
    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;

    @Transactional
    public SubredditDto save(SubredditDto subredditDto){
        Subreddit save = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
        subredditDto.setId(save.getId());
        return subredditDto;
    }
    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll()
                .stream()
                .map(subredditMapper::mapSubredditToDto)
                .collect(toList());
    }

    public SubredditDto getSubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new SpringRedditException("No subreddit found with ID - " + id));
        return subredditMapper.mapSubredditToDto(subreddit);
    }

//    @Transactional
//    public SubredditDto save(SubredditDto subredditDto){
//        Subreddit subreddit = subredditRepository.save(mapSubredditDto(subredditDto));
//        subredditDto.setId(subreddit.getId());
//        return subredditDto;
//    }
//
//    @Transactional(readOnly = true)
//    public List<SubredditDto> getAll() {
//        return subredditRepository.findAll()
//                .stream()
//                .map(this::mapToDto)
//                .collect(toList());
//    }

//    private SubredditDto mapToDto(Subreddit subreddit) {
//        return SubredditDto.builder()
//                .name(subreddit.getName())
//                .id(subreddit.getId())
//                .numberOfPosts(subreddit.getPosts().size())
//                .build();
//    }

//    private Subreddit mapSubredditDto(SubredditDto subredditDto) {
//        return Subreddit.builder()
//                .name(subredditDto.getName())
//                .description(subredditDto.getDescription())
//                .build();
//    }

}
