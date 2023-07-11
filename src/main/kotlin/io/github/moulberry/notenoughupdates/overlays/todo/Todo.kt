/*
 * Copyright (C) 2023 NotEnoughUpdates contributors
 *
 * This file is part of NotEnoughUpdates.
 *
 * NotEnoughUpdates is free software: you can redistribute it
 * and/or modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * NotEnoughUpdates is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with NotEnoughUpdates. If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.moulberry.notenoughupdates.overlays.todo

import com.google.gson.JsonObject
import io.github.moulberry.notenoughupdates.NotEnoughUpdates
import io.github.moulberry.notenoughupdates.overlays.todo.CustomTodoOverlay.DisplayTime
import io.github.moulberry.notenoughupdates.overlays.todo.CustomTodoOverlay.TodoMode
import io.github.moulberry.notenoughupdates.overlays.todo.CustomTodoOverlay.TodoType
import net.minecraft.item.ItemStack

/*
* Copyright (C) 2023 NotEnoughUpdates contributors
*
* This file is part of NotEnoughUpdates.
*
* NotEnoughUpdates is free software: you can redistribute it
* and/or modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation, either
* version 3 of the License, or (at your option) any later version.
*
* NotEnoughUpdates is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with NotEnoughUpdates. If not, see <https://www.gnu.org/licenses/>.
*/

class Todo internal constructor(
    val type: TodoType, val mode: TodoMode, val name: String, val regex: String,
    private val internalName: String,
    val time: Long,
    var lastFinished: Long,
    var displayType: DisplayTime,
    var icon: ItemStack
) {


    fun delete(todos: MutableList<Todo?>) {
        todos.remove(this)
        if (jsonObject.has(internalName)) jsonObject.remove(internalName)
    }

    fun delete() {
        todos.remove(this)
        if (jsonObject.has(internalName)) jsonObject.remove(internalName)
    }

    // static :pray:
    fun execute() {
        this.lastFinished = System.currentTimeMillis()
        CustomTodoOverlay.saveFile()
    }

    companion object {

        @JvmStatic
        lateinit var jsonObject: JsonObject

        @JvmStatic
        fun setObject(jsonObject: JsonObject) {
            this.jsonObject = jsonObject;
        }


        @JvmStatic
        var todos = mutableListOf<Todo>()

        @JvmStatic
        fun addTodoToList(todo: Todo) {
            todos.add(todo)
        }

        @JvmStatic
        fun fromJson(jsonObject: JsonObject): Todo {
            // {"type":"CHAT","mode":"EQUALS","name":"hi","regex":"adf","time":60000}
            val type = TodoType.valueOf(jsonObject["type"].asString)
            val mode = TodoMode.valueOf(jsonObject["mode"].asString)
            val name = jsonObject["name"].asString
            val regex = jsonObject["regex"].asString
            val time = jsonObject["time"].asLong
            val internalName = jsonObject["internalName"].asString
            val lastFinished: Long = jsonObject["lastFinished"].asLong
            val displayType = DisplayTime.valueOf(jsonObject["displayType"].asString)
            val item = NotEnoughUpdates.INSTANCE.manager.createItem(jsonObject["iconItem"].asString)

            return Todo(type, mode, name, regex, internalName, time, lastFinished, displayType, item)
        }

    }
}
