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

    private val _finishedNoteDbOperation: MutableLiveData<Boolean> = MutableLiveData(true)
    val finishedNoteDbOperation: LiveData<Boolean>
        get() = _finishedNoteDbOperation

    private val _notes = MutableLiveData<MutableList<Note>?>()
    val notes: LiveData<MutableList<Note>?>
        get() = _notes
    private val _detailNote = MutableLiveData<Note?>()
    val detailNote: LiveData<Note?>
        get() = _detailNote

    // dummy data
    private val dummyNoteData: List<Note> = mutableListOf(
        Note(0, "Title", mutableListOf(NoteLine("Body"))),
        Note(1, "Title Haha", mutableListOf(NoteLine("Absolut gute Notiz"), NoteLine("Super toll")), true),
        Note(4, "Kaufen", mutableListOf(NoteLine("Spaghetti"), NoteLine("Hefe"), NoteLine("Tomaten")))
    )


    private val dummyTodoData: List<Todo> = listOf(
        Todo(5, "Heute", mutableListOf(TodoLine("Wasser trinken", repeat = true), TodoLine("Aufräumen"))),
        Todo(6, "Diese Woche", mutableListOf(TodoLine("Sport", repeat = true), TodoLine("Pflanzen gießen", repeat = true))),
        Todo(7, "Diesen Monat", mutableListOf(TodoLine("Putzen", repeat = true), TodoLine("Auto Check", repeat = true))),
        Todo(8, "Backlog", mutableListOf(TodoLine("Aussortieren")))
    )


    suspend fun initDbIfEmpty() {
        _finishedNoteDbOperation.value = false
        _finishedUpdatingTodo.value = false
        if (database.roomTodoDao.isEmpty()) {
            dummyTodoData[0].initResetTime(TimePeriod.DAYS, 1, 0, 3) // every day at 3 am
            dummyTodoData[1].initResetTime(TimePeriod.WEEKS, 1, 1, 3) // every monday at 3 am
            dummyTodoData[2].initResetTime(TimePeriod.MONTHS, 1, 1, 3) // every months 1st at 3 am
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
        _finishedNoteDbOperation.value = true
        _finishedUpdatingTodo.value = true
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
        // needs to be postvalue because loadAll runs on IO thread and calls update when its time to reset a_todo
        _finishedUpdatingTodo.postValue(false)
        // update todo_lines in db
        val roomTodoLines = todo.body.map { todoLine -> todoLine.toRoomTodoLine(todo.id) }
        database.roomTodoLineDao.deleteAllByTodoId(todo.id)
        database.roomTodoLineDao.insertAll(roomTodoLines)
//        database.roomTodoLineDao.updateAll(roomTodoLines)
//        database.roomTodoLineDao.insertOnlyNew(roomTodoLines)

        // update todo_in db
        database.roomTodoDao.update(todo.toRoomTodo())
        _finishedUpdatingTodo.postValue(true)
    }


// functions for notes


    suspend fun loadAndConvertAllNotes() {
        val roomNotes = database.roomNoteDao.getAll().reversed()
        val convertedNotes = mutableListOf<Note>()
        // TODO mit coroutine parallelisieren
        roomNotes.forEach {
            val roomBody = database.roomNoteLineDao.getAllForNoteId(it.id)
            // TODO get categories from db
            convertedNotes.add(convertRoomNoteToNote(it, roomBody))
        }
        // sort converted notes by pin (pinned ones first)
        convertedNotes.sortBy { !it.isPinned }
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
        _finishedNoteDbOperation.value = false
        database.roomNoteDao.deletebyIds(selectedIds)
//        selectedIds.reversed().forEach { dummyNoteData.value?.removeAt(it) }
//        dummyNoteData.notifyObservers()
        _finishedNoteDbOperation.value = true
    }

    suspend fun addNote() {

        // insert note
        val id: Long = database.roomNoteDao.insert(RoomNote())
        // load note for detail view
        loadNote(id)

    }

    suspend fun updateNote(note: Note) {
        _finishedNoteDbOperation.value = false
        // update note lines
        val roomNoteLines = note.body.map { noteLine -> noteLine.toRoomNoteLine(note.id) }
        database.roomNoteLineDao.deleteAllByNoteId(note.id)
        database.roomNoteLineDao.insertAll(roomNoteLines)
        // update note
        database.roomNoteDao.update(note.toRoomNote())
        _finishedNoteDbOperation.value = true
    }

    fun resetTodosLiveData() {
        _todos.value = null
    }

    suspend fun updateAndSetNote(note: Note) {
        updateNote(note)
        _detailNote.value = note
    }

    suspend fun deleteNote(id: Long) {
        database.roomNoteDao.deleteById(id)
    }

    fun invalidateDetailNote() {
        _detailNote.value = null
    }
}