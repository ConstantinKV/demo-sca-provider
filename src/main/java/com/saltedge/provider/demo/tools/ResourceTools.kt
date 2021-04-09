/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.tools

import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

object ResourceTools {
    private val log = LoggerFactory.getLogger(ResourceTools::class.java)

    /**
     * Reads key file from `resources`
     *
     * @param filename name of file
     * @return key file content or null
     */
    fun readKeyFile(filename: String): String {
        val fileResource: Resource = ClassPathResource(filename)
        return try {
            readFromInputStream(fileResource.inputStream)
        } catch (e: IOException) {
            log.error(e.message, e)
            ""
        }
    }

    @Throws(IOException::class)
    private fun readFromInputStream(inputStream: InputStream): String {
        val resultStringBuilder = StringBuilder()
        BufferedReader(InputStreamReader(inputStream)).use { br ->
            var line: String?
            while (br.readLine().also { line = it } != null) resultStringBuilder.append(line).append("\n")
        }
        return resultStringBuilder.toString()
    }
}