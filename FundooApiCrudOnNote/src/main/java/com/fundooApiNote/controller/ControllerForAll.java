package com.fundooApiNote.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fundooApiNote.exception.NewException;
import com.fundooApiNote.exception.NoteNotFoundException;
import com.fundooApiNote.response.Response;
import com.fundooApiNote.response.ResponseOfToken;
import com.fundooApiNote.dto.Logindto;
import com.fundooApiNote.dto.Registerdto;
import com.fundooApiNote.model.LabelEntity;
import com.fundooApiNote.model.NoteEntity;
import com.fundooApiNote.model.User;
import com.fundooApiNote.repository.LabelRepo;
import com.fundooApiNote.repository.NoteRepo;
import com.fundooApiNote.repository.UserRepository;
import com.fundooApiNote.service.Services;
import com.fundooApiNote.utility.TokenGenerator;

@RestController
@RequestMapping("/crud")
public class ControllerForAll 
{
	@Autowired
	Services service;

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	NoteRepo noteRepo;
	
	@Autowired
	LabelRepo labelRepo;
	
	@Autowired
	TokenGenerator tokenGen;
	
	@PostMapping("/register")
	public ResponseEntity<Response> register(@RequestBody Registerdto userDto)
			throws NewException, UnsupportedEncodingException 
	{
		Response response = service.UserRegistration(userDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/login")
	public ResponseEntity<ResponseOfToken> onLogin(@RequestBody Logindto logindto)
			throws NewException, UnsupportedEncodingException 
	{
		ResponseOfToken response = service.UserLogin(logindto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping(value = "/emailValidation")
	public ResponseEntity<Response> emailValidation(@RequestHeader String token) 
			throws NewException 
	{
		Response response = service.EmailValidation(token);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@GetMapping("/getallusers")
	public List<User> getAllUsers()
	{
		return userRepo.findAll();
	}
	
	/*
	 * 
	 * Crud Operations 
	 * on Note
	 * 
	 */
	@PostMapping("login/newNote")
	public NoteEntity newNote(@Valid @RequestBody NoteEntity noteEntity, @RequestHeader String token)
	{
		Long id = tokenGen.decodeToken(token);
		noteEntity.setUserId(id);
		
		return noteRepo.save(noteEntity);
	}
	
	@GetMapping("login/displayNoteByToken")
	public NoteEntity displayNoteByToken(@RequestHeader String token) 
	{
		Long id = tokenGen.decodeToken(token);
		return noteRepo.findById(id).orElseThrow(() -> new NoteNotFoundException("Note"));
	}
	
	@PutMapping("login/updateNote")
	public NoteEntity updateNotebyNoteId(@RequestHeader Long NoteId,
	                             @Valid @RequestBody NoteEntity note1) 
	{
		NoteEntity noteEntity = noteRepo.findById(NoteId)
	            .orElseThrow(() -> new NoteNotFoundException("Note"));

		noteEntity.setNoteTitle(note1.getNoteTitle());
		noteEntity.setNoteData(note1.getNoteData());

		NoteEntity updatedNote = noteRepo.save(noteEntity);
	    return updatedNote;
	}
	
	@DeleteMapping("login/deleteNote")
	public void deleteNoteByNoteId(@RequestHeader Long NoteId)
	{
		NoteEntity noteEntity = noteRepo.findById(NoteId)
	            .orElseThrow(() -> new NoteNotFoundException("Note"));
		
		noteRepo.delete(noteEntity);
	}
	
	/*
	 * 
	 * Crud Operations 
	 * on Label
	 * 
	 */
	@PostMapping("login/newLabel")
	public LabelEntity newLabel(@Valid @RequestBody LabelEntity labelEntity,@RequestHeader Long NoteId)
	{
		labelEntity.setNoteId(NoteId);
		return labelRepo.save(labelEntity);
	}
	
	@GetMapping("login/displayLabelByNoteId")
	public LabelEntity displayLabelByNoteId(@RequestHeader Long NoteId) 
	{
		return labelRepo.findById(NoteId).orElseThrow(() -> new NoteNotFoundException("Label"));
	}
	
	@PutMapping("login/updateLabel")
	public LabelEntity updateLabelbyLabelId(@RequestHeader Long LabelId,
	                             @Valid @RequestBody LabelEntity label1) 
	{
		LabelEntity labelEntity = labelRepo.findById(LabelId)
	            .orElseThrow(() -> new NoteNotFoundException("Label"));

		labelEntity.setLabelTitle(label1.getLabelTitle());

		LabelEntity updatedLabel = labelRepo.save(labelEntity);
	    return updatedLabel;
	}
	
	@DeleteMapping("login/deleteLabel")
	public void deleteLabelByLabelId(@RequestHeader Long LabelId)
	{
		NoteEntity noteEntity = noteRepo.findById(LabelId)
	            .orElseThrow(() -> new NoteNotFoundException("Label"));
		
		noteRepo.delete(noteEntity);
	}
}
