package com.motionmate.service.goods;

import com.motionmate.domain.goods.Inquiry;
import com.motionmate.domain.goods.InquiryRepository;
import com.motionmate.domain.user.User;
import com.motionmate.global.exception.CustomException;
import com.motionmate.domain.goods.InquiryRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class InquiryService {
    private final InquiryRepository repository;



    public InquiryService(InquiryRepository repository){
        this.repository = repository;
    }

    public Inquiry save(Inquiry inquiry){
        return repository.save(inquiry);
    }

    public List<Inquiry> findAll(){
        return repository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public List<Inquiry> findByUser(User user) { return repository.findByUser(user);}

    public Inquiry findById(Long id){
        return repository.findById(id).orElseThrow(()-> new NoSuchElementException("해당 문의가 존재하지 않습니다."));
    }

    // 문의 삭제
    @Transactional
    public void deleteInquiry(Long id, User user) {
        Inquiry inquiry = repository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "해당 문의가 존재하지 않습니다."));

        if (!inquiry.getUser().getId().equals(user.getId())) {
            throw new CustomException(HttpStatus.FORBIDDEN, "본인의 문의만 삭제할 수 있습니다.");
        }

        repository.delete(inquiry);
    }

    // 문의 답변
    @Transactional
    public Inquiry answerInquiry(Long id, String answer, User user){
        Inquiry inquiry = repository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "문의가 존재하지 않습니다."));

        inquiry.registerAnswer(answer);
        return inquiry; // ✅ 등록 후 반환
    }

}
