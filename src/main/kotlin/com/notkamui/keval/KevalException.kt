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

/**
 * Generic Keval Exception
 *
 * @param message is the message to display in the stacktrace
 */
sealed class KevalException(message: String) : Exception(message)

/**
 * Invalid Expression Exception, is thrown when the expression is considered invalid (i.e. Mismatched parenthesis or missing operands)
 *
 * @property expression is the invalid expression
 * @property position is the estimated position of the error
 */
open class KevalInvalidExpressionException internal constructor(
    val expression: String,
    val position: Int
) : KevalException("Invalid expression at $position in $expression")

/**
 * Invalid Operator Exception, is thrown when an invalid/unknown operator is found
 *
 * @property invalidSymbol is the given invalid operator
 * @param expression is the invalid expression
 * @param position is the estimated position of the error
 */
class KevalInvalidSymbolException internal constructor(
    val invalidSymbol: String,
    expression: String,
    position: Int
) : KevalInvalidExpressionException(expression, position)

/**
 * Zero Division Exception, is thrown when a zero division occurs (i.e. x/0, x%0)
 */
class KevalZeroDivisionException : KevalException("Division by zero")

/**
 * DSL Exception, is thrown when a required field isn't defined
 *
 * @param what is the name of the undefined field
 */
class KevalDSLException internal constructor(what: String) :
    KevalException("All required fields must be properly defined: $what")