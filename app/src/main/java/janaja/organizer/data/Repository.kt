package janaja.organizer.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import janaja.organizer.data.local.AppDatabase
import janaja.organizer.data.model.*

class Repository(val database: AppDatabase) {

    // livedata
    private val _finishedUpdating: MutableLiveData<Boolean> = MutableLiveData(true)
    val finishedUpdating: LiveData<Boolean>
        get() = _finishedUpdating

    private val _todos = MutableLiveData<MutableList<Todo>?>()
    val todos: LiveData<MutableList<Todo>?>
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
        Todo(5, "Heute", mutableListOf(TodoLine( "Wasser trinken", repeat = true), TodoLine( "Aufräumen"))),
        Todo(6, "Diese Woche", mutableListOf(TodoLine( "Sport", repeat = true), TodoLine( "Pflanzen gießen", repeat = true))),
        Todo(7, "Diesen Monat", mutableListOf(TodoLine( "Putzen", repeat = true), TodoLine( "Auto Check", repeat = true))),
        Todo(8, "Backlog", mutableListOf(TodoLine( "Aussortieren")))
    )


    suspend fun initDbIfEmpty() {
        if(database.roomTodoDao.isEmpty()) {
            dummyTodoData.forEach {
                database.roomTodoDao.insert(it.toRoomTodo())
                database.roomTodoLineDao.insertAll(it.body.map { todoLine -> todoLine.toRoomTodoLine(it.id) })
            }
        }
    }

    // functions for todos

    suspend fun loadAndConvertAllTodos() {
        val roomTodos = database.roomTodoDao.getAll()
        val convertedTodos = mutableListOf<Todo>()
        // TODO mit coroutine parallelisieren
        roomTodos.forEach {
            val roomBody = database.roomTodoLineDao.getAllForTodoId(it.id)
            convertedTodos.add(convertRoomTodoToTodo(it, roomBody))
        }
        _todos.postValue(convertedTodos)
    }

    suspend fun loadTodo(id: Long) {
        val roomTodo = database.roomTodoDao.getById(id)
        val roomBody = database.roomTodoLineDao.getAllForTodoId(roomTodo.id)
        _detailTodo.value = convertRoomTodoToTodo(roomTodo, roomBody)
    }

    private suspend fun convertRoomTodoToTodo(roomTodo: RoomTodo, roomBody: List<RoomTodoLine>): Todo {
        val body = roomBody.map { roomTodoLine -> roomTodoLine.toTodoLine() }.toMutableList()
        val todo = roomTodo.toTodo(body)
        if(todo.tryReset()){
            updateTodo(todo)
        }
        return todo
    }

    fun unloadDetailTodo() {
        _detailTodo.value = null
    }

    suspend fun updateTodo(todo: Todo) {
        _finishedUpdating.postValue(false)
        // update todo_lines in db
        val roomTodoLines = todo.body.map { todoLine -> todoLine.toRoomTodoLine(todo.id) }
        database.roomTodoLineDao.deleteAllByTodoId(todo.id)
        database.roomTodoLineDao.insertAll(roomTodoLines)
//        database.roomTodoLineDao.updateAll(roomTodoLines)
//        database.roomTodoLineDao.insertOnlyNew(roomTodoLines)

        // update todo_in db
        database.roomTodoDao.update(todo.toRoomTodo())
        _finishedUpdating.postValue(true)
    }


// functions for notes

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

    fun resetTodosLiveData() {
        _todos.value = null
    }

}