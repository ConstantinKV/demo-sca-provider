/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.model

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import javax.persistence.*

@Entity(name = "sca_connection")
@Table(name = "sca_connection")
class ScaConnectionEntity {
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
    var connectionId: String? = null

    @Column
    var publicKey: String? = null

    @Column(length = 4096)
    var returnUrl: String? = null

    @Column
    var accessToken: String? = null

    @Column
    var revoked = false

    @Column
    var userId: String? = null
}
