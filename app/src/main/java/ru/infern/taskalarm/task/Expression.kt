package ru.infern.taskalarm.task

/**
 * Модель выражения, содержащая два числа, знак операции и ответ.
 *
 * @property firstNumber первое число
 * @property secondNumber второе число
 * @property sign знак операции
 * @property answer ответ
 */
data class Expression(
    val firstNumber: Int,
    val secondNumber: Int,
    val sign: Char,
    val answer: Int
)