package techtabu.jpapageable.customer

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

/**
 * @author TechTabu
 */

@Entity(name = "customer")
@Table(name = "customer")
data class Customer (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,

    var firstName : String,
    var lastName : String,
    var customerNumber: String,
    var email : String
)