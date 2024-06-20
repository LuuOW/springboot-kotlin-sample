package com.cropyback.cropyback.api.verification

import com.cropyback.cropyback.api.verification.models.request.SMSRequest
import com.cropyback.cropyback.api.verification.models.request.SMSVerifyRequest
import com.cropyback.cropyback.api.verification.models.response.SMSRequestResponse
import com.cropyback.cropyback.api.verification.models.response.SMSVerifyResponse
import com.cropyback.cropyback.database.sql.service.UserJpaService
import com.twilio.Twilio
import com.twilio.exception.TwilioException
import com.twilio.rest.verify.v2.service.VerificationCheck
import com.twilio.rest.verify.v2.service.VerificationCreator
import org.apache.kafka.common.requests.DeleteAclsResponse.log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api")
class SMSController(
    @Autowired private val env: Environment
) {

    @Autowired
    private lateinit var userService: UserJpaService

    private val TWILIO_ACCOUNT_SID: String = env.getProperty("twilio.account.sid", "")
    private val TWILIO_AUTH_TOKEN: String = env.getProperty("twilio.auth.token", "")
    private val VERIFY_SERVICE_SID: String = env.getProperty("twilio.service.sid", "")


    @PostMapping("/sms/request")
    fun sendSMSVerification(@RequestBody request: SMSRequest): ResponseEntity<SMSRequestResponse> {
        val isReset = request.isReset
        try {
            if (isReset) {
                if (userService.phoneExist(request.phone)) {
                    Twilio.init(TWILIO_ACCOUNT_SID,TWILIO_AUTH_TOKEN)
                    VerificationCreator(VERIFY_SERVICE_SID, request.phone, "sms")
                        .create()
                    return ResponseEntity.ok().body(SMSRequestResponse("Verification Code Sent"))
                } else {
                    return ResponseEntity.ok().body(SMSRequestResponse(""))
                }
            } else {
                Twilio.init(TWILIO_ACCOUNT_SID,TWILIO_AUTH_TOKEN)
                VerificationCreator(VERIFY_SERVICE_SID, request.phone, "sms")
                    .create()
                return ResponseEntity.ok().body(SMSRequestResponse("Verification Code Sent"))
            }
        } catch(tE: TwilioException) {
            throw Exception("Error in twilio: ${tE.message}," + "Reason: ${tE.cause}")
        }
    }

    @PostMapping("/sms/verify")
    fun verifySMSCode(@RequestBody smsInfo: SMSVerifyRequest):ResponseEntity<SMSVerifyResponse> {
        try {
            Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN)
            val verificationCheck = VerificationCheck.creator(VERIFY_SERVICE_SID)
                .setTo(smsInfo.phone)
                .setCode(smsInfo.code)
                .create()

            if (verificationCheck.status == "approved") {
                return ResponseEntity.ok(SMSVerifyResponse("Code successfully verified",true))
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(SMSVerifyResponse("Incorrect code",false))
            }

        } catch (e: TwilioException) {
            log.error("Twilio verification failed: {}", e.message, e) // Log the error with stack trace
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(SMSVerifyResponse("Verification failed",false))
        }
    }

}