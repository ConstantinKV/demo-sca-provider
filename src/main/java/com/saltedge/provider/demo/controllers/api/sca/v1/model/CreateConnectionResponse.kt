/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.controllers.api.sca.v1.model

data class CreateConnectionResponse(val data: CreateConnectionResponseData)

data class CreateConnectionResponseData(val authentication_url: String)
