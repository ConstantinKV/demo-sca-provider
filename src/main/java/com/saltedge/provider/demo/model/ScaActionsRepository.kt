/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.model

import org.springframework.data.jpa.repository.JpaRepository

interface ScaActionsRepository : JpaRepository<ScaActionEntity, Long>
