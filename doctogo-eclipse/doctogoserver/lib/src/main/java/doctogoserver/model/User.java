package doctogoserver.model;

import java.sql.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;

@Entity 
@Table( name ="dusers", uniqueConstraints = {
        @UniqueConstraint(columnNames = "ID"),
        @UniqueConstraint(columnNames = "EMAIL")})

public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	private Date createdDate;
	private Date updatedDate;

	private String userName;
	private String email;
	private String firstName ;
	private String lastName;
	private String phone;
	private String password;
	
	private boolean helper = false;
	private boolean dispatcherFlag= false;		
	
	private Double longitude;
	private Double latitude;

	//User created notifications 
	@OneToMany(cascade=CascadeType.ALL, mappedBy="user",  fetch = FetchType.LAZY )
	@JsonManagedReference
	private Set<Notification> notifications;
	
	//For dispatcher users, return notifications dispatcher is working or worked on
	@OneToMany(cascade=CascadeType.ALL, mappedBy="dispatcher",  fetch = FetchType.LAZY)
	@JsonManagedReference
	private Set<Notification> dispatchedNotifications;
		
	//For helper user , these represent notifications they are assigned to work on or they completed working on
	@ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name="HELPER_ASSIGNED_NOTIFICATIONS", joinColumns={@JoinColumn(referencedColumnName="ID")}
                                        , inverseJoinColumns={@JoinColumn(referencedColumnName="ID")}) 
	@JsonManagedReference
    private Set<Notification> assignedNotifications;
	
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public Set<Notification> getAssignedNotifications() {
		return assignedNotifications;
	}
	public void setAssignedNotifications(Set<Notification> assignedNotifications) {
		this.assignedNotifications = assignedNotifications;
	}
	public Set<Notification> getDispatchedNotifications() {
		return dispatchedNotifications;
	}
	public void setDispatchedNotifications(Set<Notification> dispatchedNotifications) {
		this.dispatchedNotifications = dispatchedNotifications;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Set<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(Set<Notification> notifications) {
		this.notifications = notifications;
	}

	public boolean isDispatcherFlag() {
		return dispatcherFlag;
	}

	public void setDispatcherFlag(boolean dispatcherFlag) {
		this.dispatcherFlag = dispatcherFlag;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String name) {
		this.userName = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean isHelper() {
		return helper;
	}

	public void setHelper(boolean helper) {
		this.helper = helper;
	}
}