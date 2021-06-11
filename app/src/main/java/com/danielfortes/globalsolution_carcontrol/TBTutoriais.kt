package com.danielfortes.globalsolution_carcontrol


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TB_Tutoriais")
data class TBTutoriais(
    @PrimaryKey(autoGenerate = true)
    var id: Int=0,
    @ColumnInfo(name="Titulo")
    val titulo: String,
    @ColumnInfo(name="corpotutorial")
    val corpotutorial: String,
    @ColumnInfo(name="Link")
    val link: String
)
