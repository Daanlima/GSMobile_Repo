package com.danielfortes.globalsolution_carcontrol

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(TBAgendamento::class, TBLavaRapido::class, TBTutoriais::class, TBVeiculos::class), version = 1)
abstract class AppDatabase:RoomDatabase() {
    abstract fun AgendamentoDAO():AgendamentoDAO
    abstract fun LavaRapidoDAO():LavaRapidoDAO
    abstract fun TutoriaisDAO():TutoriaisDAO
    abstract fun VeiculosDAO():VeiculosDAO


}

