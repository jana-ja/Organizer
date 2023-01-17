package janaja.organizer.ui.note

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import janaja.organizer.R
import janaja.organizer.adapter.DetailChecklistEntryRVA
import janaja.organizer.data.model.NoteLine
import janaja.organizer.data.model.Note
import janaja.organizer.databinding.FragmentNoteDetailBinding
import janaja.organizer.ui.SharedViewModel

class NoteDetailFragment : Fragment() {

    private val viewModel: SharedViewModel by activityViewModels()
    private lateinit var binding: FragmentNoteDetailBinding
    private val TAG = "NoteDetailFragment"
    // TODO lateinit and null type not possible. how to handle properly?
    private var note: Note? = null
    private lateinit var adapter: DetailChecklistEntryRVA
    private var menuCreated: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteDetailBinding.inflate(inflater)

        viewModel.detailNote.observe(viewLifecycleOwner) {
            if (it != null) {
                this.note = it
                binding.detailNoteTitle.setText(it.title)
                if (it.isCheckList) {
                    binding.detailNoteBody.visibility = View.GONE
                    binding.detailNoteBodyRv.visibility = View.VISIBLE
                    adapter = DetailChecklistEntryRVA(it.body)
                    binding.detailNoteBodyRv.adapter = adapter
                } else {
                    binding.detailNoteBody.visibility = View.VISIBLE
                    binding.detailNoteBodyRv.visibility = View.GONE
                    binding.detailNoteBody.setText(it.body.joinToString(separator = "\n"))
                }
                if (menuCreated) {
                    menuProvider.setMenuIcons(); Log.i(TAG, "set menu items from observer")
                }
            }
        }
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(menuProvider, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private val menuProvider = object : MenuProvider {
        lateinit var menu: Menu

        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.note_detail_top_app_bar, menu)
            this.menu = menu
            menuCreated = true
            Log.i(TAG,"menu created")
            note?.let { setMenuIcons(); Log.i(TAG,"set menu items from menu") }
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            if (note == null)
                return false

            // Handle the menu selection
            return when (menuItem.itemId) {
                R.id.menu_note_detail_delete -> {
                    viewModel.deleteNote(note!!.id)
                    findNavController().navigateUp()
                    true
                }
                R.id.menu_note_detail_checklist -> {
                    saveUserInput()
                    note!!.isCheckList = !note!!.isCheckList
                    viewModel.updateAndSetNote(note!!)
                    if (note!!.isCheckList)
                        menuItem.icon = context?.let { AppCompatResources.getDrawable(it, R.drawable.ic_baseline_check_box_24) }
                    else
                        menuItem.icon = context?.let { AppCompatResources.getDrawable(it, R.drawable.ic_outline_check_box_24) }
                    true
                }
                R.id.menu_note_detail_pin -> {
                    note!!.isPinned = !note!!.isPinned
                    if (note!!.isPinned)
                        menuItem.icon = context?.let { AppCompatResources.getDrawable(it, R.drawable.ic_baseline_push_pin_24) }
                    else
                        menuItem.icon = context?.let { AppCompatResources.getDrawable(it, R.drawable.ic_outline_push_pin_24) }
                    true
                }
                else -> false
            }
        }

        fun setMenuIcons(){
            val checkListItem = menu.findItem(R.id.menu_note_detail_checklist)
            if (note!!.isCheckList)
                checkListItem.icon = context?.let { AppCompatResources.getDrawable(it, R.drawable.ic_baseline_check_box_24) }
            else
                checkListItem.icon = context?.let { AppCompatResources.getDrawable(it, R.drawable.ic_outline_check_box_24) }
            val pinItem = menu.findItem(R.id.menu_note_detail_pin)
            if (note!!.isPinned)
                pinItem.icon = context?.let { AppCompatResources.getDrawable(it, R.drawable.ic_baseline_push_pin_24) }
            else
                pinItem.icon = context?.let { AppCompatResources.getDrawable(it, R.drawable.ic_outline_push_pin_24) }
        }

    }

    private fun saveUserInput() {
        note!!.title = binding.detailNoteTitle.text.toString()
        if (note!!.isCheckList) {
            note!!.body = adapter.getAllLines()
        } else {
            val body = binding.detailNoteBody.text.toString()
            note!!.body = body.split("\n").map { s -> NoteLine(s, false) }.toMutableList()
        }
    }

    override fun onStop() {
        super.onStop()
        if (note != null) {
            saveUserInput()
            viewModel.updateNote(note!!)
            viewModel.invalidateDetailNote()
        }
    }

}