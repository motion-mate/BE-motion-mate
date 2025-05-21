package com.motionmate.service;

import com.motionmate.domain.feed.Feed;
import com.motionmate.domain.feed.FeedComment;
import com.motionmate.domain.feed.FeedCommentRepository;
import com.motionmate.domain.feed.FeedRepository;
import com.motionmate.domain.user.User;
import com.motionmate.domain.user.UserRepository;
import com.motionmate.dto.feed.FeedCommentRequestDto;
import com.motionmate.dto.feed.FeedCommentResponseDto;
import com.motionmate.dto.feed.FeedCommentUpdateDto;
import com.motionmate.global.exception.CustomException;
import com.motionmate.mapper.FeedCommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedCommentService {

    private final FeedRepository feedRepository;
    private final UserRepository userRepository;
    private final FeedCommentRepository feedCommentRepository;

    //댓글 등록
    public FeedCommentResponseDto createComment(Long feedId, Long userId, FeedCommentRequestDto dto) {
      Feed feed =  feedRepository.findById(feedId)
                .orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND, "피드가 존재하지 않습니다."));

      if (userId == null) {
          throw new CustomException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다");
      }

      User user = userRepository.findById(userId)
              .orElseThrow(()-> new CustomException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다"));

      FeedComment saved = feedCommentRepository.save(FeedCommentMapper.toEntity(dto, user, feed));
      return FeedCommentMapper.fromEntity(saved);
    }

    //댓글 전체 조회
    public List<FeedCommentResponseDto> getAllComments(Long feedId, Long userId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND, "피드가 존재하지 않습니다."));

        List<FeedComment> comments = feedCommentRepository.findByFeedOrderByCreatedAtDesc(feed);

        return comments.stream()
                .map(comment -> {
                    return FeedCommentMapper.fromEntity(comment, userId);
                })
                .toList();
    }

    //댓글 미리보기
    public List<FeedCommentResponseDto> getPreviewComments(Long feedId){
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND, "피드가 존재하지 않습니다."));
        return feedCommentRepository.findTop10ByFeedOrderByCreatedAtDesc(feed).stream()
                .map(FeedCommentMapper::fromEntity)
                .toList();
    }

    //댓글 수정
    @Transactional
    public FeedCommentResponseDto updateComment(Long commentId, Long userId, FeedCommentUpdateDto dto) {
        FeedComment comment = feedCommentRepository.findById(commentId)
                .orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."));

        if(!comment.getUser().getId().equals(userId)){
            throw new CustomException(HttpStatus.FORBIDDEN, "수정 권한이 없습니다.");
        }

        comment.updateContent(dto.getContent());

       FeedComment updated = feedCommentRepository.findById(commentId)
                .orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND, "댓글을 다시 불러오지 못했습니다."));
        return FeedCommentMapper.fromEntity(updated);
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        FeedComment comment = feedCommentRepository.findById(commentId)
                .orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."));

        if(!comment.getUser().getId().equals(userId)){
            throw new CustomException(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다.");
        }

        feedCommentRepository.delete(comment);
    }

}
