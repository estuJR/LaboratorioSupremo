package uvg.plataformas.labs.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey val id: String,
    val nombre: String,
    val simbolo: String,
    val precioUsd: String,
    val changePercent24Hr: String,
    val supply: String,
    val maxSupply: String,
    val marketCapUsd: String,
    val fechaGuardado: String

)