/*
MIT License

Copyright (c) 2021 Jimmy "notKamui" TEILLARD

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package com.notkamui.keval

private enum class TokenType {
    FIRST,
    OPERAND,
    OPERATOR,
    LPAREN,
    RPAREN,
    COMMA,
}

private fun shouldAssumeMul(tokenType: TokenType): Boolean =
    tokenType == TokenType.OPERAND || tokenType == TokenType.RPAREN

// This function add product symbols where they should be assumed
private fun List<String>.assumeMul(symbolsSet: Set<String>, tokensToString: String): List<String> {
    var currentPos = 0
    var prevToken = TokenType.FIRST
    val ret = mutableListOf<String>()
    this.forEach { token ->
        prevToken = when {
            token.isNumeric() -> TokenType.OPERAND.also {
                if (shouldAssumeMul(prevToken)) ret.add("*")
            }

            token.isKevalOperator(symbolsSet) -> TokenType.OPERATOR
            token == "(" -> TokenType.LPAREN.also {
                if (shouldAssumeMul(prevToken)) ret.add("*")
            }

            token == ")" -> TokenType.RPAREN
            token == "," -> TokenType.COMMA
            else -> throw KevalInvalidSymbolException(token, tokensToString, currentPos)
        }
        ret.add(token)
        currentPos += token.length
    }
    return ret
}

/**
 * Checks if a string is numeric or not
 *
 * @receiver is the string to check
 * @return true if the string is numeric, false otherwise
 */
internal fun String.isNumeric(): Boolean {
    this.toDoubleOrNull() ?: return false
    return true
}

/**
 * Checks if a string is a Keval Operator or not
 *
 * @receiver is the string to check
 * @return true if the string is a valid operator, false otherwise
 */
internal fun String.isKevalOperator(symbolsSet: Set<String>): Boolean = this in symbolsSet

/**
 * Tokenizes a mathematical expression
 *
 * @receiver is the string to tokenize
 * @return the list of tokens
 * @throws KevalInvalidSymbolException if the expression contains an invalid symbol
 */
internal fun String.tokenize(symbolsSet: Set<String>): List<String> {
    val limits = """ |[^a-zA-Z0-9._]|,|\(|\)"""
    val tokens = this
        .split("""(?<=($limits))|(?=($limits))""".toRegex()) // tokenizing
        .filter { it.isNotBlank() } // removing possible empty tokens
        .map { it.replace("\\s".toRegex(), "") } // sanitizing

    val tokensToString = tokens.joinToString("")

    return tokens.assumeMul(symbolsSet, tokensToString)
}