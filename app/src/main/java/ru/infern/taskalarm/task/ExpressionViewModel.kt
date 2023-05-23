package ru.infern.taskalarm.task

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * ViewModel класс для управления выражением.
 *
 */
class ExpressionViewModel : ViewModel() {
    val expressionLiveData: MutableLiveData<Expression> = MutableLiveData() //
    val currentAnswerLiveData: MutableLiveData<Int> = MutableLiveData() //

    private var expressionMaker: ExpressionMaker = ExpressionMaker() //

    init {
        makeExpression()
    }

    /**
     * Создает новое выражение и обновляет LiveData.
     *
     */
    private fun makeExpression() {
        expressionMaker.makeFields()
        val expression = Expression(
            expressionMaker.firstNumber,
            expressionMaker.secondNumber,
            expressionMaker.sign,
            expressionMaker.answer
        )
        expressionLiveData.value = expression
        currentAnswerLiveData.value = 0
    }

    /**
     * Проверяет ответ пользователя на текущее выражение.
     * Если ответ верный, возвращает true.
     * Если ответ неверный, создает новое выражение и возвращает false.
     *
     * @param answer Ответ пользователя.
     * @return Возвращает true, если ответ верный, иначе false.
     */
    fun submitAnswer(answer: Int): Boolean {
        return if (answer == expressionMaker.answer) {
            true
        } else {
            makeExpression()
            false
        }
    }
}