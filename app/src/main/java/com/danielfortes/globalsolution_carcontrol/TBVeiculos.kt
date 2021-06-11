package com.danielfortes.globalsolution_carcontrol

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TB_Veiculos")
data class TBVeiculos(
    @PrimaryKey(autoGenerate = true)
    var id: Int=0,
    @ColumnInfo(name="marca")
    val marca: String,
    @ColumnInfo(name="ano")
    val ano: String,
    @ColumnInfo(name="modelo")
    val modelo: String,
    @ColumnInfo(name="placa")
    val placa: String
)
