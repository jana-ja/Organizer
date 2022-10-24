package janaja.organizer.data

import androidx.lifecycle.MutableLiveData
import janaja.organizer.data.model.Line
import janaja.organizer.data.model.Note

class Repository {


    val dummyData: MutableLiveData<MutableList<Note>> = MutableLiveData(mutableListOf(
        Note(0, "Title", mutableListOf(Line("Body"))),
        Note(1, "Title Haha", mutableListOf(Line("ICh mache nOtiz"),Line("Super toll")),true),
        Note(2, "Wow", mutableListOf(Line("GuNa"),Line("hehe"),Line("länger"))),
        Note(3, "Kaufen", mutableListOf(Line("Spa"))),
        Note(4, "Kaufen", mutableListOf(Line("Spaghetti"),Line("Hände"),Line("Tomaten"),Line("brrrr"),Line("Spaghetti"),Line("Hände"),Line("Tomaten"),Line("brrrr"))),
        Note(5, "Heute", mutableListOf(Line("Wasser trinken"), Line("Aufräumen")),true, isTodo = true),
        Note(6, "Diese Woche", mutableListOf(Line("Sport"),Line("Lesen"), Line("Pflanzen gießen")),true, isTodo = true),
        Note(7, "Diesen Monat", mutableListOf(Line("Putzen"),Line("Auto Check")),true, isTodo = true),
        Note(8, "Backlog", mutableListOf(Line("Aussortieren")),true, isTodo = true),
    ))

    val dummyTodoData: MutableLiveData<MutableList<Note>> = MutableLiveData(dummyData.value!!.filter{ it.isTodo }.toMutableList())

    fun getNote(id: Long): Note? {
        // TODO dummy method
        dummyData.value?.filter { note: Note -> note.id == id }.also {
            if(!it.isNullOrEmpty()) return it[0]
        }
        return null
    }

    fun addNote(note: Note){
        dummyData.value?.add(0, note)
        dummyData.notifyObservers()
    }

    fun deleteNotes(indices: List<Int>) {
        // TODO dummy method
        indices.reversed().forEach { dummyData.value?.removeAt(it) }
        dummyData.notifyObservers()
    }

    fun <T> MutableLiveData<T>.notifyObservers() {
        this.value = this.value
    }

    fun updateNote(note: Note) {
        // TODO dummy method
        val list = dummyData.value
        if(list != null) {
            val oldNote = list.find { it.id == note.id }
            if(oldNote != null) {
                val index = list.indexOf(oldNote)
                list.removeAt(index)
                list.add(index, note)
                return
            }
        }
        // TODO handle error
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