package com.danielfortes.globalsolution_carcontrol

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface VeiculosDAO {
    @Query("SELECT * FROM TB_VEICULOS")
    suspend fun getVeiculos(): List<TBVeiculos>

    @Insert
    suspend fun addVeiculo(t: TBVeiculos)

    @Delete
    suspend fun deleteVeiculo(t: TBVeiculos)
}
