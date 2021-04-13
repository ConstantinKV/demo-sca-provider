/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.model

import org.springframework.data.jpa.repository.JpaRepository

interface ScaConnectionsRepository : JpaRepository<ScaConnectionEntity, Long> {
    fun findFirstByConnectionId(connectionId: String?): ScaConnectionEntity?
}
