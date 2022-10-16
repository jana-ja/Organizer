package janaja.organizer.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import janaja.organizer.data.model.Line
import janaja.organizer.data.model.Note

class Repository {


    val dummyNoteData: MutableLiveData<MutableList<Note>> = MutableLiveData(mutableListOf(
        Note(0, "Title", mutableListOf(Line("Body"))),
        Note(1, "Title Haha", mutableListOf(Line("ICh mache nOtiz"),Line("Super toll")),true),
        Note(2, "Wow", mutableListOf(Line("GuNa"),Line("hehe"),Line("länger"))),
        Note(3, "Kaufen", mutableListOf(Line("Spa"))),
        Note(4, "Kaufen", mutableListOf(Line("Spaghetti"),Line("Hände"),Line("Tomaten"),Line("brrrr"),Line("Spaghetti"),Line("Hände"),Line("Tomaten"),Line("brrrr")))
    ))
    val dummyTodoData: LiveData<MutableList<Note>> = MutableLiveData(mutableListOf(
        Note(5, "Heute", mutableListOf(Line("Wasser trinken"), Line("Aufräumen")),true),
        Note(6, "Diese Woche", mutableListOf(Line("Sport"),Line("Lesen"), Line("Pflanzen gießen")),true),
        Note(7, "Diesen Monat", mutableListOf(Line("Putzen"),Line("Auto Check")),true),
        Note(8, "Backlog", mutableListOf(Line("Aussortieren")),true),
    ))

    fun getNote(id: Long): Note? {
        // TODO dummy method
        dummyNoteData.value?.filter { note: Note -> note.id == id }.also {
            if(it != null)
            if (it.isNotEmpty()) return it[0]
        }
        dummyTodoData.value?.filter { note: Note -> note.id == id }.also {
            if(it != null)
                return if (it.isNotEmpty()) it[0] else null
        }
        return null
    }

    fun addNote(note: Note){
        dummyNoteData.value?.add(0, note)
        dummyNoteData.notifyObservers()
    }

    fun deleteNotes(indices: List<Int>) {
        // TODO dummy method
        indices.reversed().forEach { dummyNoteData.value?.removeAt(it) }
        dummyNoteData.notifyObservers()
    }

    fun <T> MutableLiveData<T>.notifyObservers() {
        this.value = this.value
    }

    companion object {
        private var instance: Repository? = null

        fun getRepository(): Repository {
            return instance ?: synchronized(this) {
                instance ?: Repository().also { instance = it }
            }
        }
    }


}