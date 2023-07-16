package it.uniroma3.siwClassical.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Event {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	private String location;
	
	@NotBlank
	private String city;
	
	@NotBlank
	private String name;

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd  HH:mm")
	private LocalDateTime dateEvent;

	@OneToMany(mappedBy = "eventReviewed")
	private Set<Review> reviews;
	
	@ManyToOne
	private Musician soloist;
	
	@ManyToMany
	private Set<Musician> orchestra;
	
	@ManyToOne
	private Musician orchestraDirector;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Image poster;

	@OneToMany(mappedBy = "event")
	private Set<Group> groups;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public LocalDateTime getDateEvent() {
		return dateEvent;
	}

	public void setDateEvent(LocalDateTime dateEvent) {
		this.dateEvent = dateEvent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Set<Review> getReviews() {
		return reviews;
	}

	public void setReviews(Set<Review> reviews) {
		this.reviews = reviews;
	}

	public Musician getSoloist() {
		return soloist;
	}

	public void setSoloist(Musician soloist) {
		this.soloist = soloist;
	}

	public Set<Musician> getOrchestra() {
		return orchestra;
	}

	public void setOrchestra(Set<Musician> orchestra) {
		this.orchestra = orchestra;
	}

	public Musician getOrchestraDirector() {
		return orchestraDirector;
	}

	public void setOrchestraDirector(Musician orchestraDirector) {
		this.orchestraDirector = orchestraDirector;
	}

	public Image getPoster() {
		return poster;
	}

	public void setPoster(Image poster) {
		this.poster = poster;
	}

	public Set<Group> getGroups() {
		return groups;
	}

	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}

	@Override
	public int hashCode() {
		return Objects.hash(city, dateEvent, id, location, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		return Objects.equals(city, other.city) && Objects.equals(dateEvent, other.dateEvent)
				&& Objects.equals(id, other.id) && Objects.equals(location, other.location)
				&& Objects.equals(name, other.name);
	}
	
	
	

}
