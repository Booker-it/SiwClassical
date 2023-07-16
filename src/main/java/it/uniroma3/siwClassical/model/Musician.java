package it.uniroma3.siwClassical.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Musician {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	private String name;
	
	@NotBlank
	private String surname;
	
	@NotBlank
	private String nationality;
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfBirth;
	

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfDeath;
	
	@Length(max= 6000)
	private String biography;
	
//	@NotBlank
	@OneToOne //per semplicità consideriamo uno strumento per ora
	private Instrument instrument;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Image photo;
	
	@OneToMany(mappedBy = "soloist")
	private Set<Event> playedSoloist;
	
	@ManyToMany(mappedBy = "orchestra")
	private Set<Event> playedInOrchestra;
	
	@OneToMany(mappedBy = "orchestraDirector")
	private Set<Event> directedOrchestra;
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public LocalDate getDateOfDeath() {
		return dateOfDeath;
	}

	public void setDateOfDeath(LocalDate dateOfDeath) {
		this.dateOfDeath = dateOfDeath;
	}

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	

	public Instrument getInstrument() {
		return instrument;
	}

	public void setInstrument(Instrument instrument) {
		this.instrument = instrument;
	}

	public Image getPhoto() {
		return photo;
	}

	public void setPhoto(Image photo) {
		this.photo = photo;
	}

	public Set<Event> getPlayedSoloist() {
		return playedSoloist;
	}

	public void setPlayedSoloist(Set<Event> playedSoloist) {
		this.playedSoloist = playedSoloist;
	}

	public Set<Event> getPlayedInOrchestra() {
		return playedInOrchestra;
	}

	public void setPlayedInOrchestra(Set<Event> playedInOrchestra) {
		this.playedInOrchestra = playedInOrchestra;
	}

	public Set<Event> getDirectedOrchestra() {
		return directedOrchestra;
	}

	public void setDirectedOrchestra(Set<Event> directedOrchestra) {
		this.directedOrchestra = directedOrchestra;
	}

	@Override
	public int hashCode() {
		return Objects.hash(biography, dateOfBirth, dateOfDeath, name, nationality, surname);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Musician other = (Musician) obj;
		return Objects.equals(biography, other.biography) && Objects.equals(dateOfBirth, other.dateOfBirth)
				&& Objects.equals(dateOfDeath, other.dateOfDeath) && Objects.equals(name, other.name)
				&& Objects.equals(nationality, other.nationality) && Objects.equals(surname, other.surname);
	}
	
	
	

	
}
