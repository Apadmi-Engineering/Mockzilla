package com.apadmi.mockzilla.ui.engine.filter

import kotlin.math.max
import kotlin.math.min

data object FuzzyFilter {
    /**
     * Filters a list of items to only return items matching
     * the filter exactly or with minor edits, sorted by the
     * number of edits required.
     *
     * @param list List of items
     * @param filter Filter string
     * @param keySelector String selector to compare against filter string
     *     for each item
     */
    fun <T> filter(
        list: List<T>,
        filter: String,
        keySelector: (T) -> String,
    ): List<T> {
        @Suppress("IDENTIFIER_LENGTH")
        val f = filter.lowercase()
        return list
            .map { item ->
                val target = keySelector(item).lowercase()
                val exactMatch = target.contains(f)
                if (exactMatch) {
                    item to 0
                } else {
                    item to distance(filter = f, target = target)
                }
            }
            // Set a maximum distance based on the length of the filter string,
            // up to a maximum of 2 edits allowed to be considered a match for
            // longer filter strings.
            .filter { (_, distance) -> distance < min(max(1, f.length / 2), 3) }
            .sortedBy { (_, distance) -> distance }
            .map { (item, _) -> item }
    }

    /**
     * Returns the smallest number of additions, deletions or substitutions
     * required to convert the filter string to the target string, or any
     * substring of the target string. An identical match returns 0 and
     * a completely unrelated filter string will return the length of
     * the longer string.
     *
     * This algorithm is a modified Local Levenshtein distance described
     * in the following research paper: https://arxiv.org/pdf/2211.02767.pdf
     */
    private fun distance(
        filter: String,
        target: String,
    ): Int {
        if (filter.isEmpty()) {
            // Trivial match to 0 length substring in the target string
            return 0
        }
        if (target.isEmpty()) {
            // Trivial deletions to remove every character in the filter
            // string to match the 0 length target string.
            return filter.length
        }

        // If we had a matrix of 1 more rows than the length of the filter
        // string and 1 more columns than the length of the target string, we
        // could already fill in some of the edit distances to substrings of
        // each. "" trivially matches any target substring, "F" would require
        // 1 deletion to match "", whereas "FIL" would require 3 deletions to
        // match "" and so on.
        // - T A R G E T
        // - 0 0 0 0 0 0 0
        // F 1 ? ? ? ? ? ?
        // I 2 ? ? ? ? ? ?
        // L 3 ? ? ? ? ? ?
        // T 4 ? ? ? ? ? ?
        // E 5 ? ? ? ? ? ?
        // R 6 ? ? ? ? ? ?

        // We can fill in the rest of the matrix iteratively, and only need
        // to hold two rows in memory at a given time, since the algorithm
        // only needs to look at the previous distances in a row and the prior
        // row to fill in each of the remaining values.

        // Initialise the matrix row for matching "" to the target string, which
        // we consider an exact match since "" is always a substring of anything.
        var previousEditDistances = MutableList(target.length + 1) { 0 }

        for (row in filter.indices) {
            // We will work out the next row, and we know that the first column here
            // will be a distance equal to the number of characters that we have to delete
            // from the substring of our filter string to match against "".
            val currentEditDistances = MutableList(target.length + 1) { 0 }
            currentEditDistances[0] = row + 1

            for (column in target.indices) {
                // We now determine the value of the other columns in this row, looking
                // at the previous row and the values in the earlier columns to determine
                // the cost of the 3 ways we could adjust the filter string, and taking
                // the lowest cost each time.
                currentEditDistances[column + 1] = listOf(
                    // We could delete a character from the filter string. The prior
                    // row at this same column has the cost to match the target string for a
                    // filter string that is one character less than the one we're looking
                    // at, so the cost for this row and column is one greater.
                    previousEditDistances[column + 1] + 1,

                    // We could insert a character into the filter string. The prior
                    // column at this same row has the cost to match a substring of the
                    // target string that was one fewer characters, so we can insert the
                    // character at a cost of one more edit.
                    currentEditDistances[column] + 1,

                    // We could substitute a character. If the character at this
                    // point in both substrings is the same, this has no cost beyond
                    // what we already paid in the prior row to match a substring of
                    // the target string with a filter substring that was one shorter
                    // than what we have now.
                    if (filter[row] == target[column]) {
                        previousEditDistances[column]
                    } else {
                        previousEditDistances[column] + 1
                    },
                ).min()
            }

            // Having calculated this entire row, we can replace the
            // previous row with it and loop till we finish with the
            // costs for the last row for the entire filter string.
            previousEditDistances = currentEditDistances
        }

        // We return the fewest number of edits that will match
        // any substring of the target with our filter string.
        return previousEditDistances.min()
    }
}
