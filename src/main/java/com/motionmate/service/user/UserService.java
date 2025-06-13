package com.motionmate.service.user;

import com.motionmate.domain.follow.FollowRepository;
import com.motionmate.domain.user.*;
import com.motionmate.dto.exercise.S3FileRequest;
import com.motionmate.dto.exercise.S3FileResponse;
import com.motionmate.dto.follow.FollowResponseDto;
import com.motionmate.dto.user.*;
import com.motionmate.global.exception.CustomException;
import com.motionmate.mapper.follow.FollowMapper;
import com.motionmate.mapper.s3.S3FileMapper;
import com.motionmate.mapper.user.UserProfileMapper;
import com.motionmate.utils.S3ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final S3ServiceUtils s3ServiceUtils;
    private final UserProfileImageRepository userProfileImageRepository;

    int userPk = 101;

    // 메인페이지 프로필 데이터
    @Transactional(readOnly = true)
    public MainPageUserProfileDto getMainPageUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        return UserProfileMapper.toMainPageUserProfileDto(user);
    }

    @Transactional
    public void registerUser(Long userId, UserProfileRegisterRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        if (user.getProfile() == null) {
            UserProfile newProfile = UserProfile.createEmptyProfile();
            user.connectProfile(newProfile);
            userRepository.save(user);
        }

        UserProfile profile = user.getProfile();

        if (profile.getNickname() != null && !profile.getNickname().isBlank()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "이미 닉네임이 설정되어 있습니다.");
        }

        String finalImageUrl = dto.getProfileImageUrl();
        String finalBucketKey = dto.getBucketKey();

        if (finalBucketKey != null && !finalBucketKey.isBlank()) {
            S3FileRequest s3FileRequest = new S3FileRequest(finalBucketKey, finalImageUrl, dto.getNickname());
            S3FileResponse movedFile = s3ServiceUtils.moveFromTempToUpload(s3FileRequest, 101);

            finalImageUrl = movedFile.url();
            finalBucketKey = movedFile.bucketKey();

            UserProfileImage profileImage = UserProfileImage.builder()
                    .url(finalImageUrl)
                    .bucketKey(finalBucketKey)
                    .orgName(movedFile.orgName())
                    .userProfile(profile)
                    .build();

            userProfileImageRepository.save(profileImage);

            s3ServiceUtils.deleteUserTempFiles(101);
        }
        profile.updateProfile(
                dto.getNickname(),
                dto.getBio(),
                dto.getGoal(),
                dto.getBirthDate(),
                finalImageUrl,
                finalBucketKey
        );
    }


    @Transactional(readOnly = true)
    public boolean isProfileRegistered(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        UserProfile profile = user.getProfile();
        return profile != null && profile.getNickname() != null && !profile.getNickname().isBlank();
    }



    // 내 정보 조회
    @Transactional(readOnly = true)
    public UserProfileDto getMyInfo(Long userId) {
        if (userId == null) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        User user = userRepository.findWithProfileAndFollowById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        UserProfile profile = user.getProfile();

        int followerCount = user.getFollowers().size();
        int followingCount = user.getFollowings().size();

        List<Long> followingIds = followRepository.findFollowing(userId);

        List<FollowResponseDto> followers = user.getFollowers().stream()
                .map(f -> FollowMapper.toDto(f.getFromUser(), followingIds))
                .toList();
        List<FollowResponseDto> followings = user.getFollowings().stream()
                .map(f -> FollowMapper.toDto(f.getToUser(), followingIds))
                .toList();
        return UserProfileMapper.toUserProfileDto(user, profile);
    }

    @Transactional
    public UserResponseDto getMySummary(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getOauthNickname())
                .profileImageUrl(user.getProfile().getProfileImageUrl())
                .build();
    }

    // 다른 유저 프로필 조회
    @Transactional(readOnly = true)
    public UserProfileDto getUserProfile(Long userId) {
        User user = userRepository.findWithProfileAndFollowById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        UserProfile profile = user.getProfile();

        int followerCount = user.getFollowers().size();
        int followingCount = user.getFollowings().size();

        List<Long> followingIds = followRepository.findFollowing(userId);

        List<FollowResponseDto> followers = user.getFollowers().stream()
                .map(f -> FollowMapper.toDto(f.getFromUser(), followingIds))
                .toList();

        List<FollowResponseDto> followings = user.getFollowings().stream()
                .map(f -> FollowMapper.toDto(f.getToUser(), followingIds))
                .toList();

        return UserProfileMapper.toUserProfileDto(user, profile);
    }

    // 내 프로필 수정
    @Transactional
    public void updateUserProfile(Long userId, UserProfileUpdateRequestDto dto) {
        if (userId == null) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        UserProfile profile = user.getProfile();
        if (profile == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "아직 프로필이 생성되지 않았습니다.");
        }

        UserProfileImage oldImage = userProfileImageRepository.findByUserProfileId(profile.getId()).orElse(null);


        String finalImageUrl = profile.getProfileImageUrl();
        String finalBucketKey = profile.getBucketKey();

        String newImageUrl = dto.getProfileImageUrl();
        String newBucketKey = dto.getBucketKey();

        boolean isNewImageUploaded = newBucketKey != null
                && !newBucketKey.isBlank()
                && !newBucketKey.equals(profile.getBucketKey());

        boolean isImageDeleted =  newBucketKey != null && newBucketKey.isBlank();

        // 새 이미지가 업로드된 경우 → 피드 방식처럼 처리
        if (isImageDeleted) {
            // 기존 이미지 삭제
            if (oldImage != null) {
                profile.setUserProfileImage(null);
                s3ServiceUtils.deleteFile(oldImage.getBucketKey());
                userProfileImageRepository.delete(oldImage);
                userProfileImageRepository.flush();
            }
            finalImageUrl = null;
            finalBucketKey = null;
        } else if (isNewImageUploaded) {
            // 기존 이미지 삭제
            if (oldImage != null) {
                profile.setUserProfileImage(null);
                s3ServiceUtils.deleteFile(oldImage.getBucketKey());
                userProfileImageRepository.delete(oldImage);
                userProfileImageRepository.flush();
            }

            // move 처리
            S3FileRequest s3FileRequest = new S3FileRequest(newImageUrl, newBucketKey, dto.getOrgName());
            S3FileResponse moved = s3ServiceUtils.moveFromTempToUpload(s3FileRequest, userPk);
            S3FileRequest imageInfo = S3FileMapper.toS3FileRequest(moved);

            finalImageUrl = imageInfo.url();
            finalBucketKey = imageInfo.bucketKey();

            // 새 이미지 저장
            UserProfileImage newImage = UserProfileImage.builder()
                    .url(imageInfo.url())
                    .bucketKey(imageInfo.bucketKey())
                    .orgName(imageInfo.orgName())
                    .userProfile(profile)
                    .build();

            userProfileImageRepository.save(newImage);
            profile.setUserProfileImage(newImage);
            s3ServiceUtils.deleteUserTempFiles(userPk);
        }

        // 프로필 정보 업데이트 (닉네임, 바이오 등 포함)
        profile.updateProfile(
                dto.getNickname(),
                dto.getBio(),
                dto.getGoal(),
                dto.getBirthDate(),
                finalImageUrl,
                finalBucketKey
        );
    }




//    // TODO 피드 목록
//    @Transactional(readOnly = true)
//    public List<FeedResponseDto> getMyFeeds(Long userId) {
//        List<Feed> feeds = feedRepository.findByUserId(userId);
//
//        return feeds.stream()
//                .map(FeedMapper::fromEntity)
//                .toList();
//    }


//    // TODO 운동 기록
//    @Transactional(readOnly = true)
//    public List<ExerciseRecordDto> getMyRecords(Long userId) {
//        // exerciseRecordRepository.findByUserId(userId) 등
//    }

//    // TODO 주문 내역
//    @Transactional(readOnly = true)
//    public List<OrderResponseDto> getMyOrders(Long userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
//
//        List<Order> orders = orderRepository.findByUser(user);
//
//        return orders.stream()
//                .map(OrderMapper::toDto)
//                .toList();
//    }
//
//
//    @Transactional(readOnly = true)
//    public List<FollowResponseDto> getMyFollowing(Long userId) {
//        List<Follow> followings = followRepository.findAllByFromUser_Id(userId);
//        return followings.stream()
//                .map(f -> FollowMapper.toDto(f.getToUser()))
//                .toList();
//    }
//
//    @Transactional(readOnly = true)
//    public List<FollowResponseDto> getMyFollowers(Long userId) {
//        List<Follow> followers = followRepository.findAllByToUser_Id(userId);
//        return followers.stream()
//                .map(f -> FollowMapper.toDto(f.getFromUser()))
//                .toList();
//    }
//
//    public List<CartItemResponseDto> getMyCartItems(Long userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
//
//        List<CartItem> cartItems = cartItemRepository.findAllByUser(user);
//        return cartItems.stream()
//                .map(CartItemMapper::toDto)
//                .toList();
//    }




}