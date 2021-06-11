package com.danielfortes.globalsolution_carcontrol

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TB_LavaRapido")
data class TBLavaRapido(
    @PrimaryKey(autoGenerate = true)
    var id: Int=0,
    @ColumnInfo(name="Nome")
    val nome: String,
    @ColumnInfo(name="CEP")
    val cep: String,
    @ColumnInfo(name="Valor")
    val valor: String,
    )
