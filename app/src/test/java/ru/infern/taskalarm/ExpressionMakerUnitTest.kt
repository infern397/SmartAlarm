package ru.infern.taskalarm

import org.junit.Test

import org.junit.Assert.*
import ru.infern.taskalarm.task.ExpressionMaker

/**
 * Класс проверки работы ExpressionMaker
 *
 */
class ExpressionMakerUnitTest {
    /**
     * Метод для теста ответа ExpressionMaker
     */
    @Test
    fun makeNumbersAndSignFieldsTest() {
        val expressionMaker = ExpressionMaker()
        val expectedAnswer = when (expressionMaker.sign) {
            '+' -> expressionMaker.firstNumber + expressionMaker.secondNumber
            '-' -> expressionMaker.firstNumber - expressionMaker.secondNumber
            else -> 0
        }
        assertEquals(expressionMaker.answer, expectedAnswer)
    }
}