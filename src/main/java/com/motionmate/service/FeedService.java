package com.motionmate.service;

import com.motionmate.domain.feed.Feed;
import com.motionmate.domain.feed.FeedRepository;
import com.motionmate.domain.user.User;
import com.motionmate.domain.user.UserRepository;
import com.motionmate.dto.feed.FeedRequestDto;
import com.motionmate.dto.feed.FeedResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final FeedRepository repository;
    private final UserRepository userRePository;
    //피드 업로드
    public FeedResponseDto upload(FeedRequestDto request, Long userId) {
        User user = userRePository.findById(userId);
        Feed saved = repository.save(request.toEntity(user));
        return FeedResponseDto.fromEntity(saved);
    }

    //전체 피드 조회
    public List<FeedResponseDto> getAllFeed() {
        return repository.findAll().stream()
                .map(FeedResponseDto::fromEntity)
                .toList();
    }

    //피드 상세 페이지
    public FeedResponseDto getFeedDetail(long id) {
    }
}
