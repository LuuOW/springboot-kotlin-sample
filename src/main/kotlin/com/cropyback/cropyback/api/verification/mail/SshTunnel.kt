package com.cropyback.cropyback.api.verification.mail

import com.jcraft.jsch.JSch
import java.nio.file.Files
import java.nio.file.Paths


class SshTunnel {
    fun openTunnel(host: String, port: Int, destination: String, destinationPort: Int): Int {
        val jsch = JSch()

        jsch.addIdentity("") // Pass the key content

        val session = jsch.getSession("ec2-user", host, port) // Replace 'ec2-user' if different
        session.setConfig("StrictHostKeyChecking", "no")
        session.connect()

        return session.setPortForwardingL(0, destination, destinationPort) // 0 means choose a random available local port
    }
}
