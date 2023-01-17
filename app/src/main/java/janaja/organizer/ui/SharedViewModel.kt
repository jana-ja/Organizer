package janaja.organizer.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import janaja.organizer.data.Repository
import janaja.organizer.data.local.getDatabase
import janaja.organizer.data.model.Note
import janaja.organizer.data.model.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SharedViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = Repository(getDatabase(application))
    val notes: LiveData<MutableList<Note>?> = repository.notes
    val detailNote = repository.detailNote
    val todos: LiveData<MutableList<Todo>?> = repository.todos
    val detailTodo = repository.detailTodo
    val finishedUpdatingTodo = repository.finishedUpdatingTodo
    val finishedNoteDbOperation = repository.finishedNoteDbOperation


    fun initDbIfEmpty() {
        viewModelScope.launch {
            repository.initDbIfEmpty()
        }
    }

    fun deleteNotes(selectedIds: List<Long>) {
        viewModelScope.launch {
            repository.deleteNotes(selectedIds)

        }
    }

    fun addNote() {
        viewModelScope.launch {
            repository.addNote()
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            repository.updateNote(note)
        }
    }

    fun updateTodo(todo: Todo) {
        viewModelScope.launch {
            repository.updateTodo(todo)
        }
    }

    fun loadAndConvertAllTodos() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.loadAndConvertAllTodos()
        }
    }

    fun loadAndConvertAllNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.loadAndConvertAllNotes()
        }
    }

    fun loadTodo(id: Long) {
        viewModelScope.launch {
            repository.loadTodo(id)
        }
    }

    fun unloadDetailTodo() {
        repository.unloadDetailTodo()
    }

    fun loadNote(noteId: Long) {
        viewModelScope.launch {
            repository.loadNote(noteId)
        }
    }

    fun resetTodosLiveData() {
        repository.resetTodosLiveData()
    }

    fun updateAndSetNote(note: Note) {
        viewModelScope.launch {
            repository.updateAndSetNote(note)
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            repository.deleteNote(id)

        }
    }

    fun invalidateDetailNote() {
        repository.invalidateDetailNote()
    }

}