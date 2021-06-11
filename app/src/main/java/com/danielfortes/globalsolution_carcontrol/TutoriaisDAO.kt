package com.danielfortes.globalsolution_carcontrol


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TutoriaisDAO {
    @Query("SELECT * FROM TB_Tutoriais")
    suspend fun getTutoriais(): List<TBTutoriais>

    @Insert
    suspend fun addTutoriais(t: TBTutoriais)

    @Delete
    suspend fun deleteTutoriais(t: TBTutoriais)
}
