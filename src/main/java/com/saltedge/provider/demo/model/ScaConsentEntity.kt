/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.model

import com.fasterxml.jackson.core.type.TypeReference
import com.saltedge.provider.demo.callback.ConsentAccount
import com.saltedge.provider.demo.tools.JsonTools
import com.saltedge.provider.demo.tools.toJson
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import javax.persistence.*

@Entity(name = "sca_consent")
@Table(name = "sca_consent")
class ScaConsentEntity {
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
    var connectionId: String = ""

    @Column
    var userId: String = ""

    @Column
    var consentType: String = ""

    @Column
    var tppName: String = ""

    @Column
    var accounts: String = "[]"

    @Column
    var balance: Boolean = false

    @Column
    var transactions: Boolean = false

    @Column
    var expiresAt: Instant? = null

    @Column
    var revoked = false

    fun getConsentAccounts(): List<ConsentAccount> {
        return try {
            JsonTools.defaultMapper.readValue(accounts, object : TypeReference<List<ConsentAccount>>() {});
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun setConsentAccounts(consentAccounts: List<ConsentAccount>)  {
        return try {
            this.accounts = consentAccounts.toJson() ?: "[]"
        } catch (e: Exception) {
            this.accounts = "[]"
        }
    }
}
