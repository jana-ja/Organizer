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
    val notes: LiveData<MutableList<Note>> = repository.dummyNoteData
    val todos: LiveData<MutableList<Todo>?> = repository.todos
    val detailTodo = repository.detailTodo
    val finishedUpdating = repository.finishedUpdating



    fun initDbIfEmpty(){
        viewModelScope.launch {
            repository.initDbIfEmpty()
        }
    }

    fun deleteNotes(selected: List<Boolean>) {
        val indices: MutableList<Int> = mutableListOf()
        for (i in selected.indices)
            if (selected[i]) indices.add(i)
        repository.deleteNotes(indices)
    }

    fun addNote(note: Note) {
        repository.addNote(note)
    }

    fun updateNote(note: Note) {
        repository.updateNote(note)
    }

    fun updateTodo(todo: Todo) {
        viewModelScope.launch {
            repository.updateTodo(todo)
        }
    }

    fun loadAndConvertAllTodos(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.loadAndConvertAllTodos()
        }
    }

    fun loadTodo(id: Long){
        viewModelScope.launch {
            repository.loadTodo(id)
        }
    }

    fun unloadDetailTodo() {
        repository.unloadDetailTodo()
    }

    fun getNote(noteId: Long): Note? {
        return repository.getNote(noteId)
    }

    fun resetTodosLiveData() {
        repository.resetTodosLiveData()
    }

}