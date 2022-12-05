package janaja.organizer.ui

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import janaja.organizer.R
import janaja.organizer.adapter.DetailTodoEntryRVA
import janaja.organizer.data.Repository
import janaja.organizer.data.model.Todo
import janaja.organizer.databinding.FragmentNoteDetailBinding

class TodoDetailFragment : Fragment() {

    private val viewModel: SharedViewModel by activityViewModels()
    private val repo = Repository.getRepository()
    private lateinit var binding: FragmentNoteDetailBinding

    // TODO lateinit and null type not possible. how to handle properly?
    private var note: Todo? = null
    private lateinit var adapter: DetailTodoEntryRVA

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteDetailBinding.inflate(inflater)

        val noteId = requireArguments().getLong("todoId")
        val note = repo.getTodo(noteId)

        if (note != null) {
            this.note = note
            binding.detailNoteTitle.setText(note.title)
            // todos are always checklists
            binding.detailNoteBody.visibility = View.GONE
            binding.detailNoteBodyRv.visibility = View.VISIBLE
            adapter = DetailTodoEntryRVA(note.body)
            binding.detailNoteBodyRv.adapter = adapter
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // top app bar menu
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.todo_detail_top_app_bar, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menu_todo_detail_settings -> {
                        // todo
                        return true
                    }
                    else -> return false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)


    }


    override fun onStop() {
        super.onStop()
        if (note != null) {
            note!!.title = binding.detailNoteTitle.text.toString()
            note!!.body = adapter.getAllLines()

            viewModel.updateTodo(note!!)
        }
    }

}