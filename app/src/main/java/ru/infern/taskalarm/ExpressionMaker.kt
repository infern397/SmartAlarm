package ru.infern.taskalarm

import kotlin.random.Random
import kotlin.random.nextInt

class ExpressionMaker {
    var firstNumber = 0
    var secondNumber = 0
    var sign = '+'
    var answer = 0

    init {
        makeNumbersAndSignFields()
        calculateExpression()
    }

    fun makeNumbersAndSignFields() {
        sign = listOf('+', '-').random()
        firstNumber = Random.nextInt(10 until 100)
        secondNumber =
            if (sign == '-') Random.nextInt(10 until firstNumber)
            else Random.nextInt(10 until 100)
    }

    fun calculateExpression() {
        answer = when (sign) {
            '+' -> firstNumber + secondNumber
            '-' -> firstNumber - secondNumber
            else -> 0
        }
    }

    fun makeFields() {
        makeNumbersAndSignFields()
        calculateExpression()
    }
}