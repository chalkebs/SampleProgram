package com.fundooApiNote.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Entity
@Table(name = "Note")
@Data
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"creationTime", "updationTime"},allowGetters = true)
public class NoteEntity implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long NoteId;
	
	@NotBlank
	private String NoteTitle;

	@NotBlank
	private String NoteData;
	
	@Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
	private Date creationTime;
	
	@Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
	private Date updationTime;
	
	@NotBlank
	private Long userId;

	@Column(name = "colour", columnDefinition = "varchar(255) default 'white'")
	@NotBlank
	private String colour;
	
	@Column(name = "Archive", columnDefinition = "boolean default false", nullable = false)
	@NotBlank
	private boolean Archive;
	
	@Column(name = "pin", columnDefinition = "boolean default false", nullable = false)
	@NotBlank
	private boolean pin;
	
	@Column(name = "trash", columnDefinition = "boolean default false", nullable = false)
	@NotBlank
	private boolean trash;
	
	public Long getNoteId() 
	{
		return NoteId;
	}

	public void setNoteId(Long noteId) 
	{
		NoteId = noteId;
	}

	public String getNoteTitle() 
	{
		return NoteTitle;
	}
	
	public void setNoteTitle(String noteTitle) 
	{
		NoteTitle = noteTitle;
	}
	
	public String getNoteData() 
	{
		return NoteData;
	}

	public void setNoteData(String noteData) 
	{
		NoteData = noteData;
	}

	public Date getCreationTime() 
	{
		return creationTime;
	}

	public void setCreationTime(Date creationTime) 
	{
		this.creationTime = creationTime;
	}

	public Date getUpdationTime() 
	{
		return updationTime;
	}

	public void setUpdationTime(Date updationTime) 
	{
		this.updationTime = updationTime;
	}

	public Long getUserId() 
	{
		return userId;
	}

	public void setUserId(Long userId) 
	{
		this.userId = userId;
	}
	
	public String getColour() 
	{
		return colour;
	}

	public void setColour(String colour) 
	{
		this.colour = colour;
	}

	public boolean isArchive() 
	
	{
		return Archive;
	}

	public boolean isArchive() {
		return Archive;
	}

	public void setArchive(boolean archive) {
		Archive = archive;
	}

	public boolean isPin() {
		return pin;
	}

	public void setPin(boolean pin) {
		this.pin = pin;
	}

	public boolean isTrash() {
		return trash;
	}

	public void setTrash(boolean trash) {
		this.trash = trash;
	}d setArchive(boolean archive) 
	{
		Archive = archive;
	}

	public boolean isPin() 
	{
		return pin;
	}

	public void setPin(boolean pin) 
	{
		this.pin = pin;
	}

	public boolean isTrash() 
	{
		return trash;
	}

	public void setTrash(boolean trash) 
	{
		this.trash = trash;
	}
}
