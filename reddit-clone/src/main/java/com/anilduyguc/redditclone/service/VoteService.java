package com.anilduyguc.redditclone.service;

import com.anilduyguc.redditclone.dto.VoteDto;
import com.anilduyguc.redditclone.exceptions.PostNotFoundException;
import com.anilduyguc.redditclone.exceptions.SpringRedditException;
import com.anilduyguc.redditclone.model.Post;
import com.anilduyguc.redditclone.model.Vote;
import com.anilduyguc.redditclone.repository.PostRepository;
import com.anilduyguc.redditclone.repository.VoteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.anilduyguc.redditclone.model.VoteType.UPVOTE;

@Service
@AllArgsConstructor
@Slf4j
public class VoteService {
    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;
    @Transactional
    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post not found with Id: " + voteDto.getPostId()));

        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());

        if (voteByPostAndUser.isPresent() &&
                voteByPostAndUser.get().getVoteType()
                        .equals(voteDto.getVoteType())) {
            throw new SpringRedditException("You have already "
                    + voteDto.getVoteType() + "'d for this post");
        }
        if (UPVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }
        voteRepository.save(mapToVote(voteDto, post));
        postRepository.save(post);
    }
    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
