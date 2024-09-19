package controler.multiknobcontroller.studie

object ProbandManager {
    private var currentProbandNumber: Int = 1
    private var attemptCount: MutableMap<Int, Int> = mutableMapOf()
    private var test : Boolean = true
    private var testNumber : Int = 1


    fun setProbandNumber(number: Int) {
        currentProbandNumber = number
        if (!attemptCount.containsKey(number)) {
            attemptCount[number] = 1
        }
    }

    fun setTest(boolean: Boolean) {
        test = boolean
    }

    fun getTest(): Boolean {
        return test
    }

    fun getProbandNumber(): Int {
        return currentProbandNumber
    }

    fun setAttemptNumber(attempt: Int) {
        attemptCount[currentProbandNumber] = attempt
    }

    fun getAttemptCount(): Int {
        return attemptCount[currentProbandNumber] ?: 1
    }

    fun setTestNumber(number: Int) {
        testNumber = number
    }

    fun getTestNumber() : Int {
        return testNumber
    }
}
