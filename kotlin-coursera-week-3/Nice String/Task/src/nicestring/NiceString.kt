package nicestring

fun String.isNice(): Boolean {

    val substrings = listOf("bu", "ba", "be")
    val doesNotContainsSubstrings = substrings.none { this.contains(it) }

    val vowels = listOf('a', 'e', 'i', 'o', 'u')
    val containsEnoughVowels = this.filter { it in vowels }.count() >= 3

    val containsDoubleLetter = this.zipWithNext { a, b -> a == b }.any { it }

    val conditionsMet = listOf(
            doesNotContainsSubstrings,
            containsEnoughVowels,
            containsDoubleLetter
    ).count { it }

    return conditionsMet >= 2
}

fun main() {
    println("aza".isNice())
}