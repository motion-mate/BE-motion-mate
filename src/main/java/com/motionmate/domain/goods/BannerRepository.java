package com.motionmate.domain.goods;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long> {
    List<Banner> findAllByVisibleIsTrueAndStartDateBeforeAndEndDateAfterOrderByOrderIndexAsc(
            LocalDateTime now1, LocalDateTime now2
    );

    // ❌ findAllByOrderIndexAsc() ❌
    List<Banner> findAllByOrderByOrderIndexAsc();  // ✅ 제대로 된 Spring Data 메서드 이름

}
