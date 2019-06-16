package ga.patrick.mcdonats.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Staff {

/**
 * A username that is assigned to staff member and is used to authorize him in the system.
 * Cannot be changed.
 */
@Id
String username;

/**
 * Hash of staff member's password, produced by BCrypt.
 */
@Column(nullable = false)
String password;

/**
 * Staff member can process only one order at once.
 * The field is null, when staff member does not process any orders.
 */
@OneToOne
@JoinColumn(name = "order_id")
Order currentOrder;

/**
 * Staff status is used to prohibit them to login into system.
 * <p>
 * As soon as records of previous orders are not to be deleted from system, and
 * they contain foreign key to staff member that has processed or cancelled them,
 * the status lets disable accounts of retired staff members.
 */
StaffStatus status;

enum StaffStatus {
    /**
     * Enables access to system for staff member.
     */
    ACTIVE,
    /**
     * Disables access to system for staff member.
     */
    DISABLED
}

}
