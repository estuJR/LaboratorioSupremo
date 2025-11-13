package uvg.plataformas.labs.data.local.dao

import androidx.room.*
import uvg.plataformas.labs.data.local.entity.UsuarioEntity

@Dao
interface UsuarioDao {

    @Query("SELECT * FROM usuarios")
    suspend fun getAllUsuarios(): List<UsuarioEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(usuario: UsuarioEntity)

    @Query("DELETE FROM usuarios")
    suspend fun deleteAll()

    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    suspend fun getUsuarioById(id: String): UsuarioEntity?
}