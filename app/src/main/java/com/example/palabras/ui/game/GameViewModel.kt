package com.example.palabras.ui.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.palabras.MAX_NO_OF_WORDS
import com.example.palabras.SCORE_INCREASE
import com.example.palabras.countriesList

class GameViewModel: ViewModel() {

    private var _score = MutableLiveData(0)
    val score: LiveData<Int>
    get() = _score

    private var _currentWordCount = MutableLiveData(0)
    val currentWordCount: LiveData<Int>
    get() = _currentWordCount

    private val _currentScrambledWord = MutableLiveData<String>()
    val currentScrambledWord: LiveData<String>
    get() = _currentScrambledWord


    private var usedList: MutableList<String> = mutableListOf()
    private lateinit var currentWord: String

    init {
        getNextWord()
    }

    private fun getNextWord(){
        currentWord = countriesList.random().toString()
        val tempWord = currentWord.toCharArray()
        tempWord.shuffle()
        while(String(tempWord).equals(currentWord, false)){
            tempWord.shuffle()
        }
        if (usedList.contains(currentWord)){
            getNextWord()
        }else{
            _currentScrambledWord.value = String(tempWord)
            _currentWordCount.value = (_currentWordCount.value)?.inc()
            usedList.add(currentWord)
        }
    }


    /*
    * Returns true if the current word count is less than MAX_NO_OF_WORDS.
    * Updates the next word.
    */
    fun nextWord(): Boolean{
        return if (_currentWordCount.value!! < MAX_NO_OF_WORDS){
            getNextWord()
            true
        }else false
    }

    private fun increaseScore(){
        _score.value = (_score.value)?.plus(SCORE_INCREASE)
    }

    fun isUserWordCorrect(playerWord: String): Boolean{
        if (playerWord.equals(currentWord, true)){
            increaseScore()
            return true
        }
        return false
    }

    /*
     * Re-initializes the game data to restart the game.
     */
    fun reinitializeData() {
        _score.value = 0
        _currentWordCount.value = 0
        usedList.clear()
        getNextWord()
    }



}