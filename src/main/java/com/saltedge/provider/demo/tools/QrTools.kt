/*
 * @author Constantin Chelban
 */
package com.saltedge.provider.demo.tools

import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import java.io.ByteArrayOutputStream
import java.lang.Exception

object QrTools {
    fun encodeTextAsQrPngImage(text: String?, width: Int, height: Int): ByteArray? {
        return try {
            val bitMatrix: BitMatrix = QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, width, height)
            val byteArrayOutputStream = ByteArrayOutputStream()
            MatrixToImageWriter.writeToStream(bitMatrix, "png", byteArrayOutputStream)
            byteArrayOutputStream.toByteArray()
        } catch (e: Exception) {
            null
        }
    }
}
