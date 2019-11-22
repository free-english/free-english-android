package io.github.freeenglish.data.sync

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import io.github.freeenglish.data.AppDatabase
import io.github.freeenglish.data.entities.Definition
import io.github.freeenglish.data.entities.Word
const val FILE_PATH = "questions.json"


suspend fun initDataBase(context: Context) {


//    AssetManager am  = this.getAssets()

    val assetsManager =context.assets


   // android.os.Debug.waitForDebugger()
//    Log.d("TESTTAG",assetsManager.list("").toString())
    assetsManager.open(FILE_PATH).use { inputStream ->
        JsonReader(inputStream.reader()).use { jsonReader ->
            val plantType = object : TypeToken<List<Definitions>>() {}.type
            val definitions: List<Definitions> = Gson().fromJson(jsonReader, plantType)

            val database = AppDatabase.getInstance(context)

            val result = mapToDb(definitions)
          database.dataSyncDao().insertWords(result.first)

        }
    }
}

private fun mapToDb(definitions: List<Definitions>): Pair<List<Word>, List<Definition>> {
    var resultWord: MutableList<Word> = ArrayList<Word>()
    var resultDefinition: MutableList<Definition> = ArrayList<Definition>()
    definitions.forEachIndexed { index, definition ->
        val id = index + 1L
        resultWord.add(Word(id, definition.value))
        definition.definition.forEach { inner ->
            resultDefinition.add(Definition(0, id, inner.meaning, inner.examples[0]))
        }
    }
    Log.d("WordSearch",resultWord.toString())
    Log.d("DefinitionSearch",resultDefinition.toString())
    return Pair(resultWord,resultDefinition)
}