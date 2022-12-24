package janaja.organizer.ui.home

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import janaja.organizer.R
import janaja.organizer.adapter.HomeNoteRVA
import janaja.organizer.data.model.Note
import janaja.organizer.data.model.Todo
import janaja.organizer.databinding.FragmentHomeBinding
import janaja.organizer.ui.SharedViewModel
import kotlin.random.Random

class HomeFragment : Fragment(), HomeNoteRVA.ContextualAppBarHandler, HomeTodoInterface {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: SharedViewModel by activityViewModels()
    private var selectCount: Int? = null

    var actionMode: ActionMode? = null
    var noteAdapter: HomeNoteRVA? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        noteAdapter  = null
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // top app bar menu
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.home_top_app_bar, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
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

        viewModel.initDbIfEmpty()

        // NOTES

        viewModel.notes.observe(viewLifecycleOwner){ notes ->
            if(noteAdapter == null) {
                noteAdapter = HomeNoteRVA(notes, this).also {
                    binding.cvHomeNotes.setNoteRecyclerViewAdapter(it)
                }
            } else {
                noteAdapter!!.updateList()
            }
        }

        binding.cvHomeNotes.setAddbuttonOnClickListener {
            // TODO dummy data id
            val id = Random.nextLong()
            viewModel.addNote(Note(id))
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToNoteDetailFragment(id))
        }

        // TODOS
        binding.cvHomeReminders.setRvAdapterInterface(this)

        // when returning from a todos detail screen we need to wait until updating is finished
        viewModel.finishedUpdating.observe(viewLifecycleOwner){
            if(it)
                viewModel.loadAndConvertAllTodos()
        }

        viewModel.todos.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.cvHomeReminders.updateTodoRecyclerViewAdapter(it)
            }
        }



    }

    override fun selectAction(selectCount: Int) {
        this.selectCount = selectCount
        if (selectCount == 0) {
            // nothing selected
            if (actionMode != null)
                actionMode!!.finish()
        } else {
            // something selected
            if (actionMode != null) {
                // action mode already active
                actionMode?.title = "$selectCount"
            } else {
                // activate action mode
                actionMode = requireActivity().startActionMode(actionModeCallback)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.resetTodosLiveData()
    }

    private val actionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            mode?.menuInflater?.inflate(R.menu.card_selected_top_app_bar, menu)
            mode?.title = "$selectCount"
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            return when (item?.itemId) {
                R.id.card_selected_menu_delete -> {
                    // TODO delete per ids, not indices
                    // TODO bestätigung dialog
                    noteAdapter?.selected?.let { viewModel.deleteNotes(it) }
                    mode?.finish()
                    true
                }
                else -> false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            actionMode = null
            // TODO only call unselect all on X and not on delete
            noteAdapter?.unselectAll()
        }

    }

    override fun updateTodo(todo: Todo) {
        viewModel.updateTodo(todo)
    }

    // TODO on destroy reset select count and top bar action mode
}