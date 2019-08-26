package com.fundooApiNote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fundooApiNote.model.NoteEntity;

@Repository
public interface NoteRepo extends JpaRepository<NoteEntity, Long>
{
	/* save(), 
	 * findOne(), 
	 * findAll(), 
	 * count(), 
	 * delete()
	 * all methods of PagingAndSortingRepository
	 * all methods of CrudRepository */
}
