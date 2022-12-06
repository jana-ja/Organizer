package janaja.organizer.ui

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import janaja.organizer.R
import janaja.organizer.adapter.DetailTodoEntryRVA
import janaja.organizer.data.Repository
import janaja.organizer.data.model.Todo
import janaja.organizer.databinding.FragmentTodoDetailBinding
import janaja.organizer.util.TodoDetailCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodoDetailFragment : Fragment(), TodoDetailCallback {

    private val viewModel: SharedViewModel by activityViewModels()
    private val repo = Repository.getRepository()
    private lateinit var binding: FragmentTodoDetailBinding


    // TODO lateinit and null type not possible. how to handle properly?
    private var note: Todo? = null
    private lateinit var adapter: DetailTodoEntryRVA

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodoDetailBinding.inflate(inflater)

        val noteId = requireArguments().getLong("todoId")
        val note = repo.getTodo(noteId)

        if (note != null) {
            this.note = note
            binding.detailNoteTitle.setText(note.title)
            adapter = DetailTodoEntryRVA(note.body, this)
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
                return when (menuItem.itemId) {
                    R.id.menu_todo_detail_settings -> {
                        // todo
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        // add line button
        binding.addLineCl.setOnClickListener {
            adapter.addLine()
        }
    }

    override fun onStop() {
        super.onStop()
        if (note != null) {
            note!!.title = binding.detailNoteTitle.text.toString()
            note!!.body = adapter.getAllLines()

            viewModel.updateTodo(note!!)
        }
    }

    override fun showSoftKeyboard(view: View) {
        val focus = view.requestFocus()
        lifecycleScope.launch {
            delay(100)
            withContext(Dispatchers.Main) {
                if (focus) {
                    val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
                }
            }
        }
        // showing soft keyboard without delay works from within this fragment, but not from recyclerview adapter
        // i guess its because the new viewholders view is not finished
        // even though soft keyboard is also not shown when calling this function from the recyclerviewadapter but using a view from this fragment so??
        // TODO further testing on slow devices
    }

}