package com.example.smarteat.utils

import android.os.Environment
import com.example.smarteat.models.User
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.lang.Exception

object ReadWriter {

    /**
     * Write content to the file. Create new file if file doesn't exist
     * @param output file for writing
     * @param content text, which will be write to the file
     * @return True - content was writing to the file successful
     */
    fun writeDataToFile(output: File, content: String) : Boolean {
        return try {
            if (!output.exists())
                output.createNewFile()
            output.writeText(content)
            true
        } catch (ex: IOException) {
            false
        } catch (ex: Exception) {
            ex.printStackTrace()
            false
        }
    }

    /**
     * Read text from file
     * @param input file with text
     * @return text from the file
     */
    fun readDataFromFile(input: File): String {
        return if (!input.exists())
            ""
        else
            input.readText()
    }
}