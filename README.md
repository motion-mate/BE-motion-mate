# 🏃 MotionMate - 운동 & 커뮤니티 플랫폼

**운동을 기록하고, 사람들과 소통하며, 굿즈까지 즐길 수 있는 올인원 플랫폼!**  
- 다양한 운동을 직접 기록하고 루틴도 만들 수 있어요  
- 실시간 채팅, 피드 공유, 굿즈 구매까지 하나의 앱에서 🎉

## 🖼️ 주요 화면

### 🏠 홈 화면  
![홈](./images/home.png)

## 💪 운동 기록 기능

![운동선택](./images/exercise-select.png)  
![수정화면](./images/exercise-edit.png)  
![운동목록](./images/exercise-list.png)  
![운동기록1](./images/exercise-record1.png)  
![운동기록2](./images/exercise-record2.png)  
![루틴생성](./images/routine-create.png)  
![루틴목록](./images/routine-list.png)

## 📷 피드(SNS)

![피드](./images/feed.png)  
![업로드](./images/feed-upload.png)  
![상세조회](./images/feed-detail.png)

## 💬 채팅

![채팅목록](./images/chat-list.png)  
![채팅방](./images/chat-room.png)  
![방생성](./images/chat-create.png)

## 🛍️ 굿즈 마켓

![마켓홈](./images/goods-home.png)  
![상품목록](./images/goods-list.png)  
![이벤트](./images/goods-event.png)  
![문의](./images/goods-inquiry.png)  
![마이페이지](./images/mypage.png)

## 🛠️ 기술 스택
![기술 스택](./images/stack.png)

## 🧩 주요 기능 요약

| 기능             | 설명                                                  |
|----------------|-----------------------------------------------------|
| 소셜 로그인       | 구글, 네이버, 카카오 OAuth2 로그인 지원               |
| 운동 기록 및 루틴 | 운동 기록 + 루틴 생성 및 실행                        |
| 피드 기능         | 이미지 업로드, 댓글, 좋아요 포함 SNS 기능             |
| 실시간 채팅       | WebSocket + Redis 기반 채팅방                        |
| 굿즈 마켓         | 쇼핑몰 기능 (목록, 주문, 배송 관리 등)                |
| 이미지 업로드     | S3 업로드 (temp/upload 분리)                          |
| 마이페이지        | 운동, 피드, 주문 내역 통합 조회                       |

## 🚀 퍼포먼스 최적화

### 채팅 Redis Pub/Sub


### 운동기록 N+1 해결



### 굿즈 선착순 동시성 제어



## ✅ 요약

* 소셜 로그인 / 실시간 기능 통합
* S3 업로드 / Redis Pub/Sub
* 성능 최적화, 동시성 제어까지 적용된 구조

```

