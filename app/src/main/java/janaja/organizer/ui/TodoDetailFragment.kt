package janaja.organizer.ui

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import janaja.organizer.R
import janaja.organizer.adapter.DetailNoteEntryRecyclerViewAdapter
import janaja.organizer.adapter.DetailTodoEntryRecyclerViewAdapter
import janaja.organizer.data.Repository
import janaja.organizer.data.model.Line
import janaja.organizer.data.model.Note
import janaja.organizer.databinding.FragmentNoteDetailBinding
import kotlin.random.Random

class TodoDetailFragment : Fragment() {

    private val viewModel: SharedViewModel by activityViewModels()
    private val repo = Repository.getRepository()
    private lateinit var binding: FragmentNoteDetailBinding

    // TODO lateinit and null type not possible. how to handle porperly?
    private var note: Note? = null
    private lateinit var adapter: DetailTodoEntryRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteDetailBinding.inflate(inflater)

        val noteId = requireArguments().getLong("noteId")
        val note = repo.getNote(noteId)

        if (note != null) {
            this.note = note
            binding.detailNoteTitle.setText(note.title)
            // todos are always checklists
            binding.detailNoteBody.visibility = View.GONE
            binding.detailNoteBodyRv.visibility = View.VISIBLE
            adapter = DetailTodoEntryRecyclerViewAdapter(note.body)
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
                    R.id.editable -> {
                        if (menuItem.isChecked) {
                            // is editable, make not editable
                            makeNotEditable()
                            menuItem.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_lock_24);
                        } else {
                            // is not editable, make editable
                            makeEditable()
                            menuItem.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_lock_open_24);
                        }
                        menuItem.isChecked = !menuItem.isChecked
                        return true
                    }
                    else -> return false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        makeNotEditable()
    }

    private fun makeEditable(){
        binding.detailNoteTitle.isEnabled = true
        // TODO update all necessary lines so rv adapter gets notified
        adapter.dataset.forEach { line -> line.notify = !line.notify }
        adapter.editable = true
        adapter.updateList()

    }

    private fun makeNotEditable(){
        binding.detailNoteTitle.isEnabled = false
// TODO update all necessary lines so rv adapter gets notified
        adapter.dataset.forEach { line -> line.notify = !line.notify }
        adapter.editable = false
        adapter.updateList()
    }

    override fun onStop() {
        super.onStop()
        if (note != null) {
            note!!.title = binding.detailNoteTitle.text.toString()
            if (note!!.isCheckList) {
                note!!.body = adapter.getAllLines()
            } else {
                val body = binding.detailNoteBody.text.toString()
                // TODO richtige ID
                note!!.body = body.split("\n").map { s -> Line(Random.nextLong(), s, false) }.toMutableList()
            }
            viewModel.updateNote(note!!)
        }
    }

}