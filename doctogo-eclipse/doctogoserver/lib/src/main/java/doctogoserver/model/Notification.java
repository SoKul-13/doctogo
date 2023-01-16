package doctogoserver.model;

import java.sql.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity 
@Table( name ="notification")
public class Notification{
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne
    @JoinColumn(name="USER_ID", nullable=false)
	@JsonBackReference
	private User user;
	
	private Double longitude;
	private Double latitude;
	private Integer notificationType;
	private double bloodPressure;
	private Integer heartBeat;
	private double oxygenSaturation;
	private NotificationStatus status = NotificationStatus.NEW;
	private Date createdDate;
	private Date updatedDate;
	//One dispatcher is assigned to one notification
	@ManyToOne
    @JoinColumn(name="DISPATCHER_ID", nullable=true)
	@JsonBackReference
	private User dispatcher;
	
	//Helpers assigned to the notification
	@ManyToMany(mappedBy="assignedNotifications")
	@JsonBackReference
    private Set<User> helpers;
	
	
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
	public User getDispatcher() {
		return dispatcher;
	}
	public void setDispatcher(User dispatcher) {
		this.dispatcher = dispatcher;
	}
	public Set<User> getHelpers() {
		return helpers;
	}
	public void setHelpers(Set<User> helpers) {
		this.helpers = helpers;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public double getBloodPressure() {
		return bloodPressure;
	}
	public void setBloodPressure(double bloodPressure) {
		this.bloodPressure = bloodPressure;
	}
	public Integer getHeartBeat() {
		return heartBeat;
	}
	public void setHeartBeat(Integer heartBeat) {
		this.heartBeat = heartBeat;
	}
	public double getOxygenSaturation() {
		return oxygenSaturation;
	}
	public void setOxygenSaturation(double oxygenSaturation) {
		this.oxygenSaturation = oxygenSaturation;
	}
	public NotificationStatus getStatus() {
		return status;
	}
	public void setStatus(NotificationStatus status) {
		this.status = status;
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
	public Integer getNotificationType() {
		return notificationType;
	}
	public void setNotificationType(Integer notificationType) {
		this.notificationType = notificationType;
	}
}