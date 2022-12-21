package janaja.organizer.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.noodle.Noodle
import janaja.organizer.data.model.NoteLine
import janaja.organizer.data.model.Note
import janaja.organizer.data.model.Todo
import janaja.organizer.data.model.TodoLine

class Repository(noodle: Noodle) {

    // db


    private val todoCollection = noodle.collectionOf(Todo::class.java)


    // livedata
    private val _todos = MutableLiveData<MutableList<Todo>>()
    val todos: LiveData<MutableList<Todo>>
        get() = _todos
    private val _detailTodo = MutableLiveData<Todo?>()
    val detailTodo: LiveData<Todo?>
        get() = _detailTodo

    val dummyNoteData: MutableLiveData<MutableList<Note>> = MutableLiveData(
        mutableListOf(
            Note(0, "Title", mutableListOf(NoteLine(0, "Body"))),
            Note(1, "Title Haha", mutableListOf(NoteLine(1, "ICh mache nOtiz"), NoteLine(2, "Super toll")), true),
            Note(2, "Wow", mutableListOf(NoteLine(3, "GuNa"), NoteLine(4, "hehe"), NoteLine(5, "länger"))),
            Note(3, "Kaufen", mutableListOf(NoteLine(6, "Spa"))),
            Note(
                4,
                "Kaufen",
                mutableListOf(
                    NoteLine(7, "Spaghetti"),
                    NoteLine(8, "Hände"),
                    NoteLine(9, "Tomaten"),
                    NoteLine(10, "brrrr"),
                    NoteLine(11, "Spaghetti"),
                    NoteLine(12, "Hände"),
                    NoteLine(13, "Tomaten"),
                    NoteLine(14, "brrrr")
                )
            ),
        )
    )

    private val dummyTodoData: List<Todo> = listOf(
        Todo(5, "Heute", mutableListOf(TodoLine(15, "Wasser trinken", repeat = true), TodoLine(16, "Aufräumen"))),
        Todo(6, "Diese Woche", mutableListOf(TodoLine(17, "Sport", repeat = true), TodoLine(19, "Pflanzen gießen", repeat = true))),
        Todo(7, "Diesen Monat", mutableListOf(TodoLine(20, "Putzen", repeat = true), TodoLine(21, "Auto Check", repeat = true))),
        Todo(8, "Backlog", mutableListOf(TodoLine(22, "Aussortieren")))
    )


    fun initDbIfEmpty() {
        if (todoCollection.count() == 0) {
            todoCollection.putAll(dummyTodoData)
        }
    }

    // functions for todos

    fun loadAllTodos() {
        if (todoCollection.count() != 0)
            _todos.postValue(todoCollection.all)
    }

    fun checkTodoReset() {
        todos.value?.forEach { it.tryReset() } // TODO id reset true update toto in db
    }

    fun loadTodo(id: Long) {
        _detailTodo.postValue(todoCollection.get(id))
    }

    fun unloadDetailTodo() {
        _detailTodo.value = null
    }

    fun updateTodo(todo: Todo) {
        todoCollection.delete(todo.id)
        todoCollection.put(todo)
    }


    // functions for notes

    fun loadAllNotes() {}

    fun deleteNotes(indices: List<Int>) {
        // TODO dummy method
        indices.reversed().forEach { dummyNoteData.value?.removeAt(it) }
        dummyNoteData.notifyObservers()
    }

    fun addNote(note: Note) {
        dummyNoteData.value?.add(0, note)
        dummyNoteData.notifyObservers()
    }

    fun getNote(id: Long): Note? {
        // TODO dummy method
        dummyNoteData.value?.filter { note: Note -> note.id == id }.also {
            if (!it.isNullOrEmpty()) return it[0]
        }
        return null
    }

    fun updateNote(note: Note) {
        // TODO dummy method
        val list = dummyNoteData.value
        if (list != null) {
            val oldNote = list.find { it.id == note.id }
            if (oldNote != null) {
                val index = list.indexOf(oldNote)
                list.removeAt(index)
                list.add(index, note)
                return
            }
        }
        // TODO handle error
    }


    private fun <T> MutableLiveData<T>.notifyObservers() {
        this.value = this.value
    }


    companion object {
        private var instance: Repository? = null

        fun getRepository(noodle: Noodle): Repository {
            return instance ?: synchronized(this) {
                instance ?: Repository(noodle).also { instance = it }
            }
        }
    }


}