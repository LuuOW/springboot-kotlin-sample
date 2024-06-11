package com.cropyback.cropyback.api.verification.mail

import com.cropyback.cropyback.authentication.utils.GeneratorsUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Service


@Service
class MailCacheService(
    private val cacheManager: CacheManager
){

    private val ge = GeneratorsUtil

    fun storeCode(code: String, hash:String) {
        val codesCache = cacheManager.getCache("codesCache")
        codesCache?.put(code, hash)
    }

    fun verifyCode(code: String): Boolean {
        val codesCache = cacheManager.getCache("codesCache")
        val cachedHash = codesCache!!.get(code)!!.get()
        return ge.verifyCode(code, cachedHash.toString())
    }

}