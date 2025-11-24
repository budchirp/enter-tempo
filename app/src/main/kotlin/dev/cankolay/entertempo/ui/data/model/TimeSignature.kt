package dev.cankolay.entertempo.ui.data.model

data class TimeSignature(val beatsPerMeasure: Int, val noteValue: Int) {
    override fun toString(): String {
        return "$beatsPerMeasure/$noteValue"
    }
}
