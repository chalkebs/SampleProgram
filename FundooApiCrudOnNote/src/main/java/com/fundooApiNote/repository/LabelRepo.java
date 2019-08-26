package com.fundooApiNote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fundooApiNote.model.LabelEntity;

@Repository
public interface LabelRepo extends JpaRepository<LabelEntity, Long>
{

}
