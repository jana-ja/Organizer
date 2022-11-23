package janaja.organizer.data

import androidx.lifecycle.MutableLiveData
import janaja.organizer.data.model.Line
import janaja.organizer.data.model.Note
import janaja.organizer.data.model.Todo

class Repository {


    val dummyNoteData: MutableLiveData<MutableList<Note>> = MutableLiveData(mutableListOf(
        Note(0, "Title", mutableListOf(Line(0,"Body"))),
        Note(1, "Title Haha", mutableListOf(Line(1,"ICh mache nOtiz"),Line(2,"Super toll")),true),
        Note(2, "Wow", mutableListOf(Line(3,"GuNa"),Line(4,"hehe"),Line(5,"länger"))),
        Note(3, "Kaufen", mutableListOf(Line(6,"Spa"))),
        Note(4, "Kaufen", mutableListOf(Line(7,"Spaghetti"),Line(8,"Hände"),Line(9,"Tomaten"),Line(10,"brrrr"),Line(11,"Spaghetti"),Line(12,"Hände"),Line(13,"Tomaten"),Line(14,"brrrr"))),
    ))
    val dummyTodoData: MutableLiveData<MutableList<Todo>> = MutableLiveData(mutableListOf(Todo(5, "Heute", mutableListOf(Line(15,"Wasser trinken", repeat = true), Line(16,"Aufräumen"))),
    Todo(6, "Diese Woche", mutableListOf(Line(17,"Sport", repeat = true),Line(18,"Lesen"), Line(19,"Pflanzen gießen", repeat = true))),
    Todo(7, "Diesen Monat", mutableListOf(Line(20,"Putzen", repeat = true),Line(21,"Auto Check", repeat = true))),
    Todo(8, "Backlog", mutableListOf(Line(22,"Aussortieren")))
    ))


    fun getNote(id: Long): Note? {
        // TODO dummy method
        dummyNoteData.value?.filter { note: Note -> note.id == id }.also {
            if(!it.isNullOrEmpty()) return it[0]
        }
        return null
    }

    fun getTodo(id: Long): Todo? {
        // TODO dummy method
        dummyTodoData.value?.filter { todo: Todo -> todo.id == id }.also {
            if(!it.isNullOrEmpty()) return it[0]
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

    private fun <T> MutableLiveData<T>.notifyObservers() {
        this.value = this.value
    }

    fun updateNote(note: Note) {
        // TODO dummy method
        val list = dummyNoteData.value
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

    fun updateTodo(todo: Todo) {
        // TODO dummy method
        val list = dummyTodoData.value
        if(list != null) {
            val oldTodo = list.find { it.id == todo.id }
            if(oldTodo != null) {
                val index = list.indexOf(oldTodo)
                list.removeAt(index)
                list.add(index, todo)
                return
            }
        }
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