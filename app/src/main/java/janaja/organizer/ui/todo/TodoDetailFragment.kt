package janaja.organizer.ui.todo

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
import janaja.organizer.ui.SharedViewModel
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
    private lateinit var todo: Todo
    private lateinit var adapter: DetailTodoEntryRVA

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodoDetailBinding.inflate(inflater)

        val noteId = requireArguments().getLong("todoId")
        val todo = repo.getTodo(noteId)

        if (todo != null) {
            this.todo = todo
            binding.detailNoteTitle.setText(todo.title)
            adapter = DetailTodoEntryRVA(todo.body, this)
            binding.detailNoteBodyRv.adapter = adapter
        } else {
            // TODO error handling
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
                        TodoSettingsDialog(todo).show(requireActivity().supportFragmentManager, "SettingsDialog")
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

        // is true when navigation here from home screen add button
        val newLine = requireArguments().getBoolean("newLine")
        if (newLine)
            adapter.addLineEnd()
    }


    override fun onStop() {
        super.onStop()

        todo.title = binding.detailNoteTitle.text.toString()
        todo.body = adapter.getAllLines()

        viewModel.updateTodo(todo)
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