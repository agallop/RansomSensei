package org.ransomsensei.activity_lockscreen.model


data class DiffFragment(val type: Type, val inputSubstring: String, val targetSubstring: String) {
    enum class Type {
        ADDITION, DELETION, REPLACEMENT, MATCH,
    }
}

data class DiffFragmentList(val diffFragments: List<DiffFragment>, val editDistance: Int) {
    fun concat(tail: DiffFragmentList): DiffFragmentList {
        if (tail.diffFragments.isEmpty()) {
            return this
        }
        val last = this.diffFragments.last()
        val next = tail.diffFragments.first()

        if (last.type == next.type) {
            val mergedFragment = DiffFragment(
                type = this.diffFragments.last().type,
                inputSubstring = last.inputSubstring + next.inputSubstring,
                targetSubstring = last.targetSubstring + next.targetSubstring

            )

            return DiffFragmentList(
                diffFragments = this.diffFragments.subList(
                    0, this.diffFragments.size - 1
                ) + mergedFragment + tail.diffFragments.subList(1, tail.diffFragments.size),
                editDistance = this.editDistance + tail.editDistance
            )
        } else {
            return DiffFragmentList(
                diffFragments = this.diffFragments + tail.diffFragments,
                editDistance = this.editDistance + tail.editDistance
            )
        }
    }

    /** Used to print to the terminal */
    fun toColorString(): String {
        val inputStringBuilder = StringBuilder("Input substring: ")
        val targetSubstringBuilder = StringBuilder("Target substring: ")


        for (fragment in diffFragments) {
            inputStringBuilder.append(
                when (fragment.type) {
                    DiffFragment.Type.MATCH -> "\u001b[32m" + fragment.inputSubstring
                    DiffFragment.Type.ADDITION -> "\u001b[31m" + fragment.inputSubstring
                    DiffFragment.Type.DELETION -> ""
                    DiffFragment.Type.REPLACEMENT -> "\u001b[31m" + fragment.inputSubstring
                }
            )
            targetSubstringBuilder.append(
                when (fragment.type) {
                    DiffFragment.Type.MATCH -> "\u001b[32m" + fragment.targetSubstring
                    DiffFragment.Type.ADDITION -> ""
                    DiffFragment.Type.DELETION -> "\u001b[31m" + fragment.targetSubstring
                    DiffFragment.Type.REPLACEMENT -> "\u001b[31m" + fragment.targetSubstring
                }
            )
        }

        inputStringBuilder.append("\u001b[0m")
        targetSubstringBuilder.append("\u001b[0m")


        return inputStringBuilder.toString() +  "\n" + targetSubstringBuilder.toString()
    }
}