package janaja.organizer.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import janaja.organizer.data.Repository
import janaja.organizer.data.model.Note
import janaja.organizer.data.model.Todo

class SharedViewModel : ViewModel() {

    val repository = Repository.getRepository()
    val notes: LiveData<MutableList<Note>> = repository.dummyNoteData
    val reminders: LiveData<MutableList<Todo>> = repository.dummyTodoData

    fun deleteNotes(selected: List<Boolean>){
        val indices: MutableList<Int> = mutableListOf()
        for(i in selected.indices)
            if(selected[i]) indices.add(i)
        repository.deleteNotes(indices)
    }

    fun addNote(note: Note){
        repository.addNote(note)
    }

    fun updateNote(note: Note){
        repository.updateNote(note)
    }

    fun updateTodo(todo: Todo) {
        repository.updateTodo(todo)
    }

}