package janaja.organizer.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import janaja.organizer.data.local.AppDatabase
import janaja.organizer.data.model.*

class Repository(private val database: AppDatabase) {


    // TODO neue archtitektur: direkte verbindung per livedata mit RoomTodo etc. im viewmodel darauf observen und die daten zu Todo etc umbauen. wie dann machen mit lines und todo einzeln laden?
    // livedata
    private val _finishedUpdatingTodo: MutableLiveData<Boolean> = MutableLiveData(true)
    val finishedUpdatingTodo: LiveData<Boolean>
        get() = _finishedUpdatingTodo

    private val _todos = MutableLiveData<MutableList<Todo>?>()
    val todos: LiveData<MutableList<Todo>?>
        get() = _todos
    private val _detailTodo = MutableLiveData<Todo?>()
    val detailTodo: LiveData<Todo?>
        get() = _detailTodo

    private val _finishedUpdatingNote: MutableLiveData<Boolean> = MutableLiveData(true)
    val finishedUpdatingNote: LiveData<Boolean>
        get() = _finishedUpdatingNote
    
    private val _notes = MutableLiveData<MutableList<Note>?>()
    val notes: LiveData<MutableList<Note>?>
        get() = _notes
    private val _detailNote = MutableLiveData<Note?>()
    val detailNote: LiveData<Note?>
        get() = _detailNote

    // dummy data
    private val dummyNoteData: List<Note> = mutableListOf(
            Note(0, "Title", mutableListOf(NoteLine("Body"))),
            Note(1, "Title Haha", mutableListOf(NoteLine("ICh mache nOtiz"), NoteLine("Super toll")), true),
            Note(2, "Wow", mutableListOf(NoteLine("GuNa"), NoteLine("hehe"), NoteLine("länger"))),
            Note(3, "Kaufen", mutableListOf(NoteLine("Spa"))),
            Note(4, "Kaufen", mutableListOf(NoteLine("Spaghetti"), NoteLine("Hände"), NoteLine("Tomaten"))),
    )


    private val dummyTodoData: List<Todo> = listOf(
        Todo(5, "Heute", mutableListOf(TodoLine("Wasser trinken", repeat = true), TodoLine("Aufräumen"))),
        Todo(6, "Diese Woche", mutableListOf(TodoLine("Sport", repeat = true), TodoLine("Pflanzen gießen", repeat = true))),
        Todo(7, "Diesen Monat", mutableListOf(TodoLine("Putzen", repeat = true), TodoLine("Auto Check", repeat = true))),
        Todo(8, "Backlog", mutableListOf(TodoLine("Aussortieren")))
    )


    suspend fun initDbIfEmpty() {
        if (database.roomTodoDao.isEmpty()) {
            dummyTodoData.forEach {
                database.roomTodoDao.insert(it.toRoomTodo())
                database.roomTodoLineDao.insertAll(it.body.map { todoLine -> todoLine.toRoomTodoLine(it.id) })
            }
        }
        if (database.roomNoteDao.isEmpty()) {
            dummyNoteData.forEach {
                database.roomNoteDao.insert(it.toRoomNote())
                database.roomNoteLineDao.insertAll(it.body.map { noteLine -> noteLine.toRoomNoteLine(it.id) })
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
        if (todo.tryReset()) {
            updateTodo(todo)
        }
        return todo
    }

    fun unloadDetailTodo() {
        _detailTodo.value = null
    }

    suspend fun updateTodo(todo: Todo) {
        _finishedUpdatingTodo.value = false //.postValue(false)
        // update todo_lines in db
        val roomTodoLines = todo.body.map { todoLine -> todoLine.toRoomTodoLine(todo.id) }
        database.roomTodoLineDao.deleteAllByTodoId(todo.id)
        database.roomTodoLineDao.insertAll(roomTodoLines)
//        database.roomTodoLineDao.updateAll(roomTodoLines)
//        database.roomTodoLineDao.insertOnlyNew(roomTodoLines)

        // update todo_in db
        database.roomTodoDao.update(todo.toRoomTodo())
        _finishedUpdatingTodo.value = true //postValue(true)
    }


// functions for notes


    suspend fun loadAndConvertAllNotes() {
        val roomNotes = database.roomNoteDao.getAll()
        val convertedNotes = mutableListOf<Note>()
        // TODO mit coroutine parallelisieren
        roomNotes.forEach {
            val roomBody = database.roomNoteLineDao.getAllForNoteId(it.id)
            // TODO get categories from db
            convertedNotes.add(convertRoomNoteToNote(it, roomBody))
        }
        _notes.postValue(convertedNotes)
    }

    private fun convertRoomNoteToNote(roomNote: RoomNote, roomBody: MutableList<RoomNoteLine>): Note {
        val body = roomBody.map { roomNoteLine -> roomNoteLine.toNoteLine() }.toMutableList()
        return roomNote.toNote(body)
    }

    suspend fun loadNote(id: Long) {
        val roomNote = database.roomNoteDao.getById(id)
        val roomBody = database.roomNoteLineDao.getAllForNoteId(roomNote.id)
        _detailNote.value = convertRoomNoteToNote(roomNote, roomBody)
    }

    suspend fun deleteNotes(selectedIds: List<Long>) {
        database.roomNoteDao.deletebyIds(selectedIds)
//        selectedIds.reversed().forEach { dummyNoteData.value?.removeAt(it) }
//        dummyNoteData.notifyObservers()
    }

    suspend fun addNote() {

        // insert note
        val id: Long = database.roomNoteDao.insert(RoomNote())
        // load note for detail view
        loadNote(id)

    }

    suspend fun updateNote(note: Note) {
        _finishedUpdatingNote.value = false
        // update note lines
        val roomNoteLines = note.body.map { noteLine -> noteLine.toRoomNoteLine(note.id) }
        database.roomNoteLineDao.deleteAllByNoteId(note.id)
        database.roomNoteLineDao.insertAll(roomNoteLines)

        // update note
        database.roomNoteDao.update(note.toRoomNote())
        _finishedUpdatingNote.value = true
    }

    fun resetTodosLiveData() {
        _todos.value = null
    }

}