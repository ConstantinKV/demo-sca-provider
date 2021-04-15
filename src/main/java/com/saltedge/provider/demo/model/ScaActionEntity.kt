/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.model

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
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

    val isClosed: Boolean
        get() = "pending" != status
}
