package com.cropyback.cropyback.api.verification

import com.twilio.Twilio
import com.twilio.exception.TwilioException
import com.twilio.rest.verify.v2.service.VerificationCheck
import com.twilio.rest.verify.v2.service.VerificationCheckCreator
import com.twilio.rest.verify.v2.service.VerificationCreator
import org.apache.kafka.common.requests.DeleteAclsResponse.log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api")
class SmsCllientController(
    @Autowired private val env: Environment
) {

    private val TWILIO_ACCOUNT_SID: String = env.getProperty("twilio.account.sid", "")
    private val TWILIO_AUTH_TOKEN: String = env.getProperty("twilio.auth.token", "")
    private val VERIFY_SERVICE_SID: String = env.getProperty("twilio.service.sid", "")


    @PostMapping("/verifications/send")
    fun sendSMSVerification(@RequestBody phone: String): ResponseEntity<String> {
        try {
            Twilio.init(TWILIO_ACCOUNT_SID,TWILIO_AUTH_TOKEN)
            VerificationCreator(VERIFY_SERVICE_SID, phone, "sms")
                .create()
            return ResponseEntity.ok().body("Verification Code Sent")
        } catch(tE: TwilioException) {
            throw Exception("Error in twilio: ${tE.message}," + "Reason: ${tE.cause}")
        }
    }

    @PostMapping("/verifications/verify")
    fun verifySMSCode(@RequestBody smsInfo: SmsInfo):ResponseEntity<String> {
        try {
            Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN)
            val verificationCheck = VerificationCheck.creator(VERIFY_SERVICE_SID)
                .setTo(smsInfo.phone)
                .setCode(smsInfo.code)
                .create()

            if (verificationCheck.status == "approved") {
                return ResponseEntity.ok("Code successfully verified")
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect code")
            }

        } catch (e: TwilioException) {
            log.error("Twilio verification failed: {}", e.message, e) // Log the error with stack trace
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Verification failed")
        }
    }

    data class SmsInfo(
        val phone: String,
        val code: String
    )


}