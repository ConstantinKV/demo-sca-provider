/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.model

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.persistence.*

@Entity(name = "sca_action")
@Table(name = "sca_action")
class ScaActionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null

    @CreationTimestamp
    @Column(updatable = false)
    var createdAt: Instant? = null

    @UpdateTimestamp
    @Column
    var updatedAt: Instant? = null

    @Column
    var code: String = ""

    @Column
    var status: String = ""

    @Column
    var descriptionType: String = ""

    val isActive: Boolean
        get() = listOf("pending", "confirm_processing", "deny_processing").contains(status)

    val isClosed: Boolean
        get() = !isActive

    val createdAtValue: Instant
        get() = createdAt ?: Instant.ofEpochSecond(0)

    val createdAtDescription: String
        get() = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault())
            .format(createdAtValue)

    val expiresAt: Instant
        get() = createdAtValue.plus(10, ChronoUnit.MINUTES)

    val isExpired: Boolean
        get() = expiresAt.isBefore(Instant.now())
}
