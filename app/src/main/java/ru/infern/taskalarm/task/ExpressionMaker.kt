package ru.infern.taskalarm.task

import kotlin.random.Random
import kotlin.random.nextInt

/**
 * Класс для создания выражения.
 *
 */
class ExpressionMaker {
    var firstNumber: Int = 0 // певое число
        private set
    var secondNumber: Int = 0 // второе число
        private set
    var sign: Char = '+' // знак выражения
        private set
    var answer: Int = 0 // ответ
        private set

    init {
        makeFields()
    }

    /**
     * Генерирует случайные числа и знак операции для выражения.
     *
     */
    private fun makeNumbersAndSignFields() {
        sign = listOf('+', '-').random()
        firstNumber = Random.nextInt(10 until 100)
        secondNumber = if (sign == '-') Random.nextInt(10 until firstNumber)
        else Random.nextInt(10 until 100)
    }

    /**
     * Вычисляет значение выражения.
     *
     */
    private fun calculateExpression() {
        answer = when (sign) {
            '+' -> firstNumber + secondNumber
            '-' -> firstNumber - secondNumber
            else -> 0
        }
    }

    /**
     * Создает поля для выражения.
     *
     */
    fun makeFields() {
        makeNumbersAndSignFields()
        calculateExpression()
    }
}