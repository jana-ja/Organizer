package janaja.organizer.ui.home

import janaja.organizer.data.model.Note

interface HomeNoteInterface {
    fun loadNote(noteId: Long)
    fun updateNote(note: Note)
}