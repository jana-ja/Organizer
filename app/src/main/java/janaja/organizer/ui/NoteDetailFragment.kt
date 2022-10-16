package janaja.organizer.ui

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import janaja.organizer.R
import janaja.organizer.adapter.NoteEntryRecyclerViewAdapter
import janaja.organizer.data.Repository
import janaja.organizer.databinding.FragmentNoteDetailBinding

class NoteDetailFragment : Fragment() {

    private val repo = Repository.getRepository()
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
            if(note.isCheckList){
                binding.detailNoteBody.visibility = View.GONE
                binding.detailNoteBodyRv.visibility = View.VISIBLE
                binding.detailNoteBodyRv.adapter = NoteEntryRecyclerViewAdapter(note.body)
            } else {
                binding.detailNoteBody.text = note.body.joinToString(separator = "\n")
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

}