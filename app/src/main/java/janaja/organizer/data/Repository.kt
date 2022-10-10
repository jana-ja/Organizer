package janaja.organizer.data

import janaja.organizer.data.model.Note

class Repository {


    val dummyNoteData = mutableListOf(
        Note(0, "Title", mutableListOf("Body")),
        Note(1, "Title Haha", mutableListOf("ICh mache nOtiz","Super toll")),
        Note(2, "Wow", mutableListOf("GuNa","hehe","länger"), true),
        Note(3, "Kaufen", mutableListOf("Spa")),
        Note(4, "Kaufen", mutableListOf("Spaghetti","Hände","Tomaten","brrrr","lelel","Spaghetti","Hände","Tomaten","brrrr","lelel","Tomaten","brrrr","lelel"))
    )
    val dummyTodoData = mutableListOf(
        Note(0, "Täglich", mutableListOf("Wasser trinken", "1 Teil aufräumen", "Essen"),true),
        Note(1, "Wöchentlich", mutableListOf("ICh mache nOtiz","Super toll"), true),
        Note(2, "Monatlich", mutableListOf("GuNa","hehe","länger"), true),
        Note(4, "Kaufen", mutableListOf("Spaghetti","Hände","Tomaten","brrrr","lelel","Spaghetti","Hände","Tomaten","brrrr","lelel","Tomaten","brrrr","lelel"), true)
    )

    fun getNote(id: Long): Note? {
        // TODO dummy method
        dummyNoteData.filter { note: Note -> note.id == id }.also {
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