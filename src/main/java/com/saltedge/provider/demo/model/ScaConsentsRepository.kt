/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.model

import org.springframework.data.jpa.repository.JpaRepository

interface ScaConsentsRepository : JpaRepository<ScaConsentEntity, Long> {
    fun findFirstByConnectionId(connectionId: String?): ScaConsentEntity?
}
