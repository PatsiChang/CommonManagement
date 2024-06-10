package com.common.validation.repository;

import com.common.validation.bean.ProfanityWords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfanityWordsRepository extends JpaRepository<ProfanityWords, String> {
    List<ProfanityWords> findAll();
}
