package com.motionmate.service.feed;

import com.motionmate.domain.feed.Feed;
import com.motionmate.domain.feed.FeedComment;
import com.motionmate.domain.feed.FeedCommentRepository;
import com.motionmate.domain.feed.FeedRepository;
import com.motionmate.domain.notification.Notification;
import com.motionmate.domain.user.User;
import com.motionmate.domain.user.UserRepository;
import com.motionmate.dto.feed.FeedCommentRequestDto;
import com.motionmate.dto.feed.FeedCommentResponseDto;
import com.motionmate.dto.feed.FeedCommentUpdateDto;
import com.motionmate.dto.notification.NotificationRequestDto;
import com.motionmate.global.exception.CustomException;
import com.motionmate.mapper.feed.FeedCommentMapper;
import com.motionmate.service.notification.NotificationService;
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
    private final NotificationService notificationService;

    //댓글 등록
    public FeedCommentResponseDto createComment(Long feedId, User user, FeedCommentRequestDto dto) {
      Feed feed =  feedRepository.findById(feedId)
                .orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND, "피드가 존재하지 않습니다."));

      if (user == null) {
          throw new CustomException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다");
      }

      FeedComment saved = feedCommentRepository.save(FeedCommentMapper.toEntity(dto, user, feed));

      if(!feed.getUser().getId().equals(user.getId())) {
          notificationService.createNotification (
                  NotificationRequestDto.builder()
                          .userId(feed.getUser().getId())
                          .type(Notification.NotificationType.COMMENT)
                          .content(user.getProfile().getNickname() + "님이 회원님의 게시글에 댓글을 남겼습니다.")
                          .build()
          );
      }
      return FeedCommentMapper.fromEntity(saved);
    }

    //댓글 전체 조회
    public List<FeedCommentResponseDto> getAllComments(Long feedId, Long userId) {

        List<FeedComment> comments = feedCommentRepository.findWithUserProfileByFeedId(feedId);

        return comments.stream()
                .map(comment -> {
                    return FeedCommentMapper.fromEntity(comment, userId);
                })
                .toList();
    }

    //댓글 수정
    @Transactional
    public FeedCommentResponseDto updateComment(Long commentId, User user, FeedCommentUpdateDto dto) {
        FeedComment comment = feedCommentRepository.findWithUserProfileById(commentId)
                .orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."));

        if(!comment.getUser().getId().equals(user.getId())){
            throw new CustomException(HttpStatus.FORBIDDEN, "수정 권한이 없습니다.");
        }

        comment.updateContent(dto.getContent());

        return FeedCommentMapper.fromEntity(comment);
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, User user) {
        FeedComment comment = feedCommentRepository.findWithUserById(commentId)
                .orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."));

        if(!comment.getUser().getId().equals(user.getId())){
            throw new CustomException(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다.");
        }

        feedCommentRepository.delete(comment);
    }

    @Transactional
    public void deleteCommentByAdmin(Long commentId) {
        FeedComment comment = feedCommentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."));
        feedCommentRepository.delete(comment);
    }

}
