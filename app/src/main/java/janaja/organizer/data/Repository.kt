package janaja.organizer.data

import janaja.organizer.data.model.Line
import janaja.organizer.data.model.Note

class Repository {


    val dummyNoteData = mutableListOf(
        Note(0, "Title", mutableListOf(Line("Body"))),
        Note(1, "Title Haha", mutableListOf(Line("ICh mache nOtiz"),Line("Super toll"))),
        Note(2, "Wow", mutableListOf(Line("GuNa"),Line("hehe"),Line("länger"))),
        Note(3, "Kaufen", mutableListOf(Line("Spa"))),
        Note(4, "Kaufen", mutableListOf(Line("Spaghetti"),Line("Hände"),Line("Tomaten"),Line("brrrr"),Line("Spaghetti"),Line("Hände"),Line("Tomaten"),Line("brrrr")))
    )
    val dummyTodoData = mutableListOf(
        Note(0, "Title", mutableListOf(Line("Body")),true),
        Note(1, "Title Haha", mutableListOf(Line("ICh mache nOtiz"),Line("Super toll")),true),
        Note(2, "Wow", mutableListOf(Line("GuNa"),Line("hehe"),Line("länger")),true),
        Note(3, "Kaufen", mutableListOf(Line("Spa")),true),
        Note(4, "Kaufen", mutableListOf(Line("Spaghetti"),Line("Hände"),Line("Tomaten"),Line("brrrr")),true)
    )

    fun getNote(id: Long): Note? {
        // TODO dummy method
        dummyNoteData.filter { note: Note -> note.id == id }.also {
            if (it.isNotEmpty()) return it[0]
        }
        dummyTodoData.filter { note: Note -> note.id == id }.also {
            return if (it.isNotEmpty()) it[0] else null
        }
    }

    companion object {
        private var instance: Repository? = null

        fun getInstance(): Repository {
            return instance ?: synchronized(this) {
                instance ?: Repository().also { instance = it }
            }
        }
    }


}