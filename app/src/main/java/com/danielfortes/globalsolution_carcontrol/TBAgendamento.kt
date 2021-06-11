package com.danielfortes.globalsolution_carcontrol

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TB_Agendamento")
data class TBAgendamento(
    @PrimaryKey(autoGenerate = true)
    var id: Int=0,
    @ColumnInfo(name="Nome")
    val nome: String,
    @ColumnInfo(name="Data")
    val data: String,
    @ColumnInfo(name="CPF")
    val cpf: String
)
