package com.danielfortes.globalsolution_carcontrol

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AgendamentoDAO {
    @Query("SELECT * FROM TB_Agendamento")
    suspend fun getAgendamentos(): List<TBAgendamento>

    @Insert
    suspend fun addAgendamento(t: TBAgendamento)

    @Delete
    suspend fun deleteAgendamento(t: TBAgendamento)
}
