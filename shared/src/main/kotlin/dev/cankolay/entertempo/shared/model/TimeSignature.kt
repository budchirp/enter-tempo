package dev.cankolay.entertempo.shared.model

data class TimeSignature(val beatsPerMeasure: Int, val noteValue: Int) {
    override fun toString() = "$beatsPerMeasure/$noteValue"
}
