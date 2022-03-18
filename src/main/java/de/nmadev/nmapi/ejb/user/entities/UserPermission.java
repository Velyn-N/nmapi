package de.nmadev.nmapi.ejb.user.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Cacheable
@Entity
@Table(name="user_permissions")
@Getter @Setter @NoArgsConstructor
public class UserPermission implements Serializable {
	private static final long serialVersionUID = -8335575110162059488L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private long userid;
	private String permission;
}
