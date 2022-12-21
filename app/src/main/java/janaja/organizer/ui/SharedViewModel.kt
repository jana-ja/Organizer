package janaja.organizer.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.noodle.Noodle
import janaja.organizer.data.Repository
import janaja.organizer.data.model.Note
import janaja.organizer.data.model.Todo


class SharedViewModel(application: Application) : AndroidViewModel(application) {

    private val noodle: Noodle = Noodle.with(application)
        .addType<Todo>(Todo::class.java)
        .build()
    private val repository = Repository.getRepository(noodle)
    val notes: LiveData<MutableList<Note>> = repository.dummyNoteData
    val todos: LiveData<MutableList<Todo>> = repository.todos
    val detailTodo = repository.detailTodo

    fun checkTodoReset() {
        repository.checkTodoReset()
    }

    fun initDbIfEmpty(){
//        viewModelScope.launch {
            repository.initDbIfEmpty()
//        }
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
//        viewModelScope.launch {
            repository.updateTodo(todo)
//        }
    }

    fun loadAllTodos(){
//        viewModelScope.launch {
            repository.loadAllTodos()
//        }
    }

    fun loadTodo(id: Long){
//        viewModelScope.launch {
            repository.loadTodo(id)
//        }
    }

    fun unloadDetailTodo() {
        repository.unloadDetailTodo()
    }

    fun getNote(noteId: Long): Note? {
        return repository.getNote(noteId)
    }

}