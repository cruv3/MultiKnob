package controler.multiknobcontroller.prototype.signals

object SignalBlocker {
    private val blockedSignals = mutableSetOf<Any>()

    fun blockSignal(signal: Any) {
        blockedSignals.add(signal)
    }

    fun unblockSignal(signal: Any) {
        blockedSignals.remove(signal)
    }

    fun isSignalBlocked(signal: Any): Boolean {
        return blockedSignals.contains(signal)
    }

    fun unblockAllSignals(){
        blockedSignals.clear()
    }
}