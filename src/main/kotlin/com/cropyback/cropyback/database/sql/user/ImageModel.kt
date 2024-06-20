package com.cropyback.cropyback.database.sql.user

import jakarta.persistence.*

@Entity
@Table(name="images")
data class ImageModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val name: String = "",
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB") // Add column definition
    val data: ByteArray = byteArrayOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImageModel

        if (id != other.id) return false
        if (name != other.name) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }
}
