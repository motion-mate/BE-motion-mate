---

# 🏃 MotionMate - 운동 & 커뮤니티 플랫폼

**운동을 기록하고, 사람들과 소통하며, 굿즈까지 즐길 수 있는 올인원 플랫폼!**

* 다양한 운동을 직접 기록하고 루틴도 만들 수 있어요
* 실시간 채팅, 피드 공유, 굿즈 구매까지 하나의 앱에서 🎉

---

## 🖼️ 주요 화면

### 🏠 홈 화면

![홈](./images/home.png)

---

### 💪 운동 기록 기능

#### 운동 선택 화면

![운동선택](./images/exercise-select.png)

#### 무게 및 세트 수정 화면

![수정화면](./images/exercise-edit.png)

#### 등록된 운동 목록

![운동목록](./images/exercise-list.png)

#### 운동 기록 화면

![운동기록1](./images/exercise-record1.png)
![운동기록2](./images/exercise-record2.png)

#### 운동 루틴 생성

![루틴생성](./images/routine-create.png)

#### 운동 루틴 목록

![루틴목록](./images/routine-list.png)

---

### 📷 피드(SNS)

#### 피드 메인

![피드](./images/feed.png)

#### 피드 업로드

![업로드](./images/feed-upload.png)

#### 피드 상세 조회

![상세조회](./images/feed-detail.png)

---

### 💬 채팅

#### 채팅방 목록

![채팅목록](./images/chat-list.png)

#### 입장한 채팅방

![채팅방](./images/chat-room.png)

#### 채팅방 생성

![방생성](./images/chat-create.png)

---

### 🛍️ 굿즈 마켓

#### 마켓 홈

![마켓홈](./images/goods-home.png)

#### 상품 목록

![상품목록](./images/goods-list.png)

#### 이벤트

![이벤트](./images/goods-event.png)

#### 고객 문의

![문의](./images/goods-inquiry.png)

#### 마이페이지

![마이페이지](./images/mypage.png)

---

## 🛠️ 기술 스택

![기술 스택](./images/stack.png)

---

## 🧩 주요 기능 요약

| 기능         | 설명                                     |
| ---------- | -------------------------------------- |
| 소셜 로그인     | 구글, 네이버, 카카오 OAuth2 로그인 지원             |
| 운동 기록 및 루틴 | 다양한 운동 기록 + 루틴 생성 및 실행 지원              |
| 피드 기능      | 피드 등록, 이미지 업로드, 댓글, 좋아요 등 SNS 기능 포함    |
| 실시간 채팅     | 채팅방 생성 및 참여, 실시간 WebSocket 기반 채팅       |
| 굿즈 마켓      | 굿즈 목록, 상세 조회, 주문, 배송 정보 관리 등 쇼핑몰 기능 포함 |
| 이미지 업로드    | S3 업로드 (temp/upload 경로 분리)             |
| 마이페이지 기능   | 내가 작성한 피드, 기록한 운동, 주문한 상품 등 통합 조회      |

---

## 🚀 퍼포먼스 최적화

### ✅ 채팅 메시지 Redis Pub/Sub 처리

```java
redisTemplate.convertAndSend(roomId, messageDto);
```

→ Redis로 채팅 메시지를 발행하여 서버 간 부하 분산

---

### ✅ 운동 기록 N+1 문제 해결

```java
@Query("SELECT r FROM ExerciseRecord r JOIN FETCH r.exerciseList")
List<ExerciseRecord> findAllWithExercises();
```

→ 루틴 기록 시 연관된 운동을 `JOIN FETCH`로 한번에 가져옴

---

### ✅ 굿즈 선착순 이벤트 동시성 제어

```java
Boolean success = redisTemplate.opsForValue().setIfAbsent(stockKey, userId);
```

→ Redis `setIfAbsent()`를 활용한 선착순 이벤트 처리

---

## ✅ 요약

* 소셜 로그인으로 빠른 시작
* 직관적인 UI + 실시간 기능 포함
* 운동 기록부터 커뮤니티, 마켓까지 통합된 경험
* S3 업로드 / JWT 인증 / OAuth2 / Redis Pub/Sub 등 실전 기술 활용
* 성능 최적화 및 동시성 제어까지 완비된 구조

---

