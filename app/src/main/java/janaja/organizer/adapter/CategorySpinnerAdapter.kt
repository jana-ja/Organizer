package janaja.organizer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import janaja.organizer.R


class CategorySpinnerAdapter(context: Context, @LayoutRes private val layoutResource: Int = R.layout.spinner_item, private val itemList: List<String>) :
    ArrayAdapter<String>(context, layoutResource, itemList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return createViewFromResource(position, parent)
    }

    private fun createViewFromResource(position: Int, parent: ViewGroup?): View{
        val view = LayoutInflater.from(context).inflate(layoutResource, parent, false)
        val textView: TextView = view.findViewById(R.id.spinner_item_tv)
        textView.text = itemList[position]
        return view
    }
}