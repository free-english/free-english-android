package io.github.freeenglish.progress

import android.content.Context
import android.os.Bundle
import android.util.LongSparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.freeenglish.R
import io.github.freeenglish.data.AppDatabase
import io.github.freeenglish.questions.QuestionFragment
import kotlinx.android.synthetic.main.item_list_in_progress.view.*
import kotlinx.android.synthetic.main.main_fragment.*
import androidx.appcompat.app.AppCompatActivity
import io.github.freeenglish.data.entities.Word


class DefinitionsInProgressFragment : Fragment() {

    companion object {
        fun newInstance() = DefinitionsInProgressFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.in_progress_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvInProgress)

        recyclerView.layoutManager = LinearLayoutManager(view.context)

        lifecycleScope.launchWhenCreated {
            val appDataBase = AppDatabase.getInstance(context!!)
            val startDao = appDataBase.statDao()
            val questionsDao = appDataBase.questionsDao()
            val definitions = startDao.getInProgressDefinitions()
            val wordsForDefinitions =
                questionsDao.getWordWithDefinitions(definitions.map { it.wordId })

            val words = LongSparseArray<String>()
            wordsForDefinitions.forEach { words.put(it.id, it.value) }
            val definitionsInProgressList = arrayListOf<DefinitionInProgress>()
            definitions.forEach {
                definitionsInProgressList.add(
                    DefinitionInProgress(
                        words[it.wordId],
                        it.meaning,
                        it.progress
                    )
                )

            }

            recyclerView.adapter =
                InProgressAdapter(
                    definitionsInProgressList.sortedBy { it.progress * -1 },
                    context!!
                )
        }
        view.findViewById<Button>(R.id.btnBack).setOnClickListener {
            activity?.onBackPressed()
        }
    }
}

data class DefinitionInProgress(
    val word: String,
    val meaning: String,
    val progress: Int
)

class InProgressAdapter(val items: List<DefinitionInProgress>, val context: Context) :
    RecyclerView.Adapter<ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_list_in_progress,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvWord?.text = items[position].word
        holder.tvMeaning?.text = items[position].meaning
        holder.pbProgress.progress = items[position].progress
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvWord = view.tvWord
    val tvMeaning = view.tvMeaning
    val pbProgress = view.pbProgres
}