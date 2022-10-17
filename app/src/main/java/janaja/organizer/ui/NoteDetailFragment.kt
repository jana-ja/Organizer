package janaja.organizer.ui

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import janaja.organizer.R
import janaja.organizer.adapter.NoteEntryRecyclerViewAdapter
import janaja.organizer.data.Repository
import janaja.organizer.data.model.Line
import janaja.organizer.data.model.Note
import janaja.organizer.databinding.FragmentNoteDetailBinding

class NoteDetailFragment : Fragment() {

    private val viewModel: SharedViewModel by activityViewModels()
    private val repo = Repository.getRepository()
    private lateinit var binding: FragmentNoteDetailBinding
    // TODO lateinit and null type not possible. how to handle porperly?
    private var note: Note? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteDetailBinding.inflate(inflater)

        val noteId = requireArguments().getLong("noteId")
        val note = repo.getNote(noteId)

        if(note != null) {
            this.note = note
            binding.detailNoteTitle.setText(note.title)
            if(note.isCheckList){
                binding.detailNoteBody.visibility = View.GONE
                binding.detailNoteBodyRv.visibility = View.VISIBLE
                binding.detailNoteBodyRv.adapter = NoteEntryRecyclerViewAdapter(note.body)
            } else {
                binding.detailNoteBody.setText(note.body.joinToString(separator = "\n"))
            }
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // top app bar menu
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.detail_top_app_bar, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
//                return when (menuItem.itemId) {
//                    R.id.menu_clear -> {
//                        // clearCompletedTasks()
//                        true
//                    }
//                    else -> false
//                }
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onStop() {
        super.onStop()
        if(note != null) {
            note!!.title = binding.detailNoteTitle.text.toString()
            val body = binding.detailNoteBody.text.toString()
            note!!.body = body.split("\n").map { s -> Line(s, false) }.toMutableList()
            viewModel.updateNote(note!!)
        }
    }

}