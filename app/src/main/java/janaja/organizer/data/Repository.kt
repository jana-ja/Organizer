package janaja.organizer.data

import janaja.organizer.data.model.Note

class Repository {


    val dummyData = mutableListOf(
        Note(0, "Title", "Body"),
        Note(1, "Title Haha", "ICh mache nOtiz\nSuper toll"),
        Note(2, "Wow", "GuNa\nhehe\nlänger"),
        Note(3, "Kaufen", "Spa"),
        Note(4, "Kaufen", "Spaghetti\nHände\nTomaten\nbrrrr\nlelel")
    )

    fun getNote(id: Long): Note? {
        // TODO dummy method
        dummyData.filter { note: Note -> note.id == id }.also {
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