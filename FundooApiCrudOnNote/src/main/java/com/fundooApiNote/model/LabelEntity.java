package com.fundooApiNote.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Entity
@Table(name = "Label")
@Data
public class LabelEntity implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@NotBlank
	private Long NoteId;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long LabelId;
	
	@NotBlank
	private String LabelTitle;
	
	public Long getNoteId() 
	{
		return NoteId;
	}

	public void setNoteId(Long noteId) 
	{
		NoteId = noteId;
	}
	
	public Long getLabelId() 
	{
		return LabelId;
	}

	public void setLabelId(Long labelId) 
	{
		LabelId = labelId;
	}

	public String getLabelTitle() 
	{
		return LabelTitle;
	}

	public void setLabelTitle(String labelTitle) 
	{
		LabelTitle = labelTitle;
	}

}
