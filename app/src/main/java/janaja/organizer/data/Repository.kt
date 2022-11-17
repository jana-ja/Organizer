package janaja.organizer.data

import androidx.lifecycle.MutableLiveData
import janaja.organizer.data.model.Line
import janaja.organizer.data.model.Note

class Repository {


    val dummyData: MutableLiveData<MutableList<Note>> = MutableLiveData(mutableListOf(
        Note(0, "Title", mutableListOf(Line(0,"Body"))),
        Note(1, "Title Haha", mutableListOf(Line(1,"ICh mache nOtiz"),Line(2,"Super toll")),true),
        Note(2, "Wow", mutableListOf(Line(3,"GuNa"),Line(4,"hehe"),Line(5,"länger"))),
        Note(3, "Kaufen", mutableListOf(Line(6,"Spa"))),
        Note(4, "Kaufen", mutableListOf(Line(7,"Spaghetti"),Line(8,"Hände"),Line(9,"Tomaten"),Line(10,"brrrr"),Line(11,"Spaghetti"),Line(12,"Hände"),Line(13,"Tomaten"),Line(14,"brrrr"))),
        Note(5, "Heute", mutableListOf(Line(15,"Wasser trinken", repeat = true), Line(16,"Aufräumen")),true, isTodo = true),
        Note(6, "Diese Woche", mutableListOf(Line(17,"Sport", repeat = true),Line(18,"Lesen"), Line(19,"Pflanzen gießen", repeat = true)),true, isTodo = true),
        Note(7, "Diesen Monat", mutableListOf(Line(20,"Putzen", repeat = true),Line(21,"Auto Check", repeat = true)),true, isTodo = true),
        Note(8, "Backlog", mutableListOf(Line(22,"Aussortieren")),true, isTodo = true),
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