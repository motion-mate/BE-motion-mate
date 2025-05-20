package com.motionmate.service.goods;

import com.motionmate.domain.goods.Inquiry;
import com.motionmate.domain.goods.InquiryRepository;
import com.motionmate.domain.user.User;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
}
