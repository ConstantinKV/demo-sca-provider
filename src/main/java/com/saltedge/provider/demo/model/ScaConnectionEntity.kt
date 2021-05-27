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

    @Column(unique=true)
    var connectionId: String = ""

    @Column(columnDefinition="TEXT")
    var rsaPublicKey: String = ""

    @Column
    var returnUrl: String = ""

    @Column
    var accessToken: String = ""

    @Column
    var revoked = false

    @Column
    var userId: String = ""

    val isAuthorized: Boolean
        get() = accessToken.isNotEmpty() && rsaPublicKey.isNotEmpty()
}
