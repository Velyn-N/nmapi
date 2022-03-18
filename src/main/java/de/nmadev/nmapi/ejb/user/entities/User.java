package de.nmadev.nmapi.ejb.user.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="user")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class User implements Serializable {
	private static final long serialVersionUID = 6919769741589115481L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	protected String username;
	protected String email;
	@Column(name = "userpassword")
	protected String password;

	protected boolean accepted;
	protected boolean banned;

	@Column(name = "azubi")
	protected boolean isAzubi = false;
	@Column(name = "ausbilder")
	protected boolean isAusbilder = false;
	@Column(name = "ausbildungsnachweis_name")
	protected String ausbildungsNachweisName;


	@OneToMany(mappedBy = "userid", fetch = FetchType.EAGER)
	protected List<UserPermission> userPermissions;



	public User(Long id, String password, String username, boolean accepted, boolean banned) {
		this.id = id;
		this.password = password;
		this.username = username;
		this.accepted = accepted;
		this.banned = banned;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName()+" [id=" + id + ", username=" + username + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id != other.id)
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}	
}
