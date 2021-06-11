package com.danielfortes.globalsolution_carcontrol

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LavaRapidoDAO {
    @Query("SELECT * FROM TB_LavaRapido")
    suspend fun getLavaRapido(): List<TBLavaRapido>

    @Insert
    suspend fun addLavaRapido(t: TBLavaRapido)

    @Delete
    suspend fun deleteLavaRapido(t: TBLavaRapido)
}
