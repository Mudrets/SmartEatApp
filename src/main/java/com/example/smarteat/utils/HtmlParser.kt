package com.example.smarteat.utils

import com.example.smarteat.models.Recipe
import org.jsoup.nodes.Element

object HtmlParser {
    private val subRecipes = ArrayList<String>()

    internal fun htmlToRecipe(section: Element): Recipe {
        subRecipes.clear()
        var split: ArrayList<String> = ArrayList(section.text().split("Что нужно:"))
        val name = split[0].trim()
        split = ArrayList(split[1].split("Как готовить:"))
        for (i in 0..9) {
            val index = split[0].indexOf("- $i")
            if (index != -1)
                split[0] = split[0].replaceRange(index..index, "*")
        }
        var whatNeed = ArrayList<String>(split[0].split('-'))
        whatNeed = ArrayList(whatNeed.subList(1, whatNeed.lastIndex + 1))
        for (i in 0 until whatNeed.size)
            whatNeed[i] = whatNeed[i].trim().replace('*','-')
        split = ArrayList(split[1].split("Советы:"))
        val steps = stringToListOfSteps(split[0].trim())
        var advice = if (split.size == 2)
            split[1].trim()
        else
            ""
        while (advice.isNotBlank() && !advice[0].isLetter())
            advice = advice.replaceRange(0..0, "")
        val subrecipes = ArrayList<String>()
        for (recipe in subRecipes)
            subrecipes.add(recipe)
        return Recipe(name, section.id(), subrecipes, steps, whatNeed, advice)
    }

    private fun stringToListOfSteps(str: String): ArrayList<ArrayList<String>> {
        var critical = false
        var strCopy = str
        for (i in strCopy.indices) {
            if (i > 0 && strCopy[i].isDigit() && strCopy[i - 1] == '-')
               critical = true
            if (strCopy[i] == '.' && critical)
                strCopy = strCopy.replaceRange(i..i, "*")
            if (!strCopy[i].isDigit() && critical)
                critical = false
        }
        val regex = Regex("\\d+\\.")
        val arr = strCopy.split("1.")
        if (arr.size > 2) {
            subRecipes.add(arr[0].trim())
            subRecipes.add(arr[1].split('.').last().trim())
            val replacedArr1 = arr[1].replace(subRecipes[1], "")
            val steps1 = ArrayList<String>(replacedArr1.split(regex))
            for (i in steps1.indices)
                steps1[i] = steps1[i].replace('*', '.').trim()
            val steps2 = ArrayList<String>(arr[2].split(regex))
            for (i in steps2.indices)
                steps2[i] = steps2[i].replace('*', '.').trim()
            return ArrayList(listOf(steps1, steps2))
        } else if (arr.size == 1) {
            return ArrayList(listOf(ArrayList(listOf(arr[0].replace('*', '.').trim()))))
        } else {
            var steps = ArrayList<String>(strCopy.split(regex))
            steps = ArrayList(steps.subList(1, steps.lastIndex))
            for (i in steps.indices)
                steps[i] = steps[i].replace('*', '.').trim()
            return ArrayList(listOf(steps))
        }
    }
}