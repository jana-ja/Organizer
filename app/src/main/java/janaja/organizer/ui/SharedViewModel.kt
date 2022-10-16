package janaja.organizer.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import janaja.organizer.data.Repository
import janaja.organizer.data.model.Note

class SharedViewModel : ViewModel() {

    val repository = Repository.getRepository()
    val notes: LiveData<MutableList<Note>> = repository.dummyNoteData
    val reminders: LiveData<MutableList<Note>> = repository.dummyTodoData

    fun deleteNotes(selected: List<Boolean>){
        val indices: MutableList<Int> = mutableListOf()
        for(i in selected.indices)
            if(selected[i]) indices.add(i)
        repository.deleteNotes(indices)
    }

}