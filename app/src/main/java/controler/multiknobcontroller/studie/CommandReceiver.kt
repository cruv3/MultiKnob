package controler.multiknobcontroller.studie

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.TextView

class CommandReceiver(private val testLabel: TextView) : BroadcastReceiver() {
    companion object{
        private const val TAG = "CommandReceiver"
    }
    override fun onReceive(context: Context, intent: Intent) {
        val command = intent.getStringExtra("command")
        command?.let {
            handleCommand(it)
        }
    }

    fun handleCommand(input: String) {
        val parts = input.split(" ")
        if (parts.size == 5 && parts[0] == "setProband") {
            val probandNumber = parts[1].toIntOrNull()
            val attemptNumber = parts[2].toIntOrNull()
            val testMode = parts[3].toBoolean()
            val testNumber = parts[4].toIntOrNull()

            if (probandNumber != null && attemptNumber != null && testNumber != null) {
                setTestMode(testMode)
                setTestNumber(testNumber)
                setProbandNumber(probandNumber)
                setAttemptNumber(attemptNumber)
            }
        }
    }

    private fun setTestNumber(number: Int) {
        ProbandManager.setTestNumber(number)
        Log.d(TAG, "Testnumber gesetzt auf $number")
    }

    private fun setProbandNumber(number: Int) {
        ProbandManager.setProbandNumber(number)
        Log.d(TAG, "Probandennummer gesetzt auf $number")
    }

    private fun setAttemptNumber(number: Int) {
        ProbandManager.setAttemptNumber(number)
        Log.d(TAG, "Attempt gesetzt auf $number")
    }

    private fun setTestMode(boolean: Boolean) {
        ProbandManager.setTest(boolean)
        if (!boolean) {
            testLabel.visibility = View.GONE
        } else {
            testLabel.visibility = View.VISIBLE
        }
        Log.d(TAG, "Test gesetzt auf $boolean")
    }
}
