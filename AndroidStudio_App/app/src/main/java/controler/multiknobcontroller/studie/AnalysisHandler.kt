package controler.multiknobcontroller.studie

import android.content.Context
import android.os.SystemClock
import android.util.Log
import java.io.File
import java.io.FileOutputStream

class AnalysisHandler(private val context: Context, private val fileName: String) {
    companion object{
        private const val TAG = "AnalyseHandler"
    }
    private var startTime: Long = 0
    private var elapsedTime: Long = 0
    private var analyseStarted = false

    init {
        createCsvFile()
    }

    fun startAnalysis() {
        if(analyseStarted) return
        Log.d(TAG, "Analyse started")
        startTime = SystemClock.elapsedRealtime()
        analyseStarted = true
    }

    fun pauseTime(){
        if(!analyseStarted) return
        val endTime = SystemClock.elapsedRealtime()
        elapsedTime = endTime - startTime
        Log.d(TAG, "timer was paused : $elapsedTime")
    }

    fun stopAnalysis(city: String) {
        if(!analyseStarted) return
        analyseStarted = false
        appendToCsvFile(elapsedTime, city)
        Log.d(TAG, "Analyse stoped : $city")
    }

    private fun createCsvFile() {
        val file = File(context.getExternalFilesDir(null), fileName)
        if (!file.exists()) {
            FileOutputStream(file, true).bufferedWriter().use { writer ->
                writer.write("Proband,Test,Attempt,ElapsedTime(ms),City/Street")
                writer.newLine()
            }
        }
    }

    private fun appendToCsvFile(elapsedTime: Long, city: String) {
        val file = File(context.getExternalFilesDir(null), fileName)
        val probandNumber = ProbandManager.getProbandNumber()
        val attemptNumber = ProbandManager.getAttemptCount()
        val testNumber = ProbandManager.getTestNumber()

        FileOutputStream(file, true).bufferedWriter().use { writer ->
            writer.write("$probandNumber,$testNumber,$attemptNumber,$elapsedTime,$city")
            writer.newLine()
        }
    }
}
