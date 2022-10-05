package janaja.organizer.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import janaja.organizer.R
import janaja.organizer.data.Repository
import janaja.organizer.data.model.Note
import janaja.organizer.databinding.FragmentHomeBinding
import janaja.organizer.databinding.FragmentNoteDetailBinding

class NoteDetailFragment : Fragment() {

    private val repo = Repository.getInstance()
    private lateinit var binding: FragmentNoteDetailBinding
    private var noteId: Long = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteDetailBinding.inflate(inflater)

        noteId = requireArguments().getLong("noteId")
        val note = repo.getNote(noteId)

        if(note != null) {
            binding.detailNoteTitle.text = note.title
            binding.detailNoteBody.text = note.body
        }

        return binding.root
    }

}