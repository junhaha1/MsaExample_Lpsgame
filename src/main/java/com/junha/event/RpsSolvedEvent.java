package com.junha.event;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class RpsSolvedEvent implements Serializable{
	private static final long serialVersionUID = 570201864291530492L;
	private final Long rpsCheallengeId;
	private final Long userId;
	private final String alias;
	private final String outcome;
}
