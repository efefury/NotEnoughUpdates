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

package io.github.moulberry.notenoughupdates.listener;

import io.github.moulberry.notenoughupdates.NotEnoughUpdates;
import io.github.moulberry.notenoughupdates.autosubscribe.NEUAutoSubscribe;
import io.github.moulberry.notenoughupdates.overlays.todo.CustomTodoOverlay;
import io.github.moulberry.notenoughupdates.overlays.todo.Todo;
import io.github.moulberry.notenoughupdates.util.SidebarUtil;
import io.github.moulberry.notenoughupdates.util.TabListUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NEUAutoSubscribe
public class TodoListener {

	// i do not want edited messages please
	@SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
	public void onGuiChat(ClientChatReceivedEvent e) {
		if (!NotEnoughUpdates.INSTANCE.isOnSkyblock()) return;
		String formattedText = e.message.getFormattedText();

		switch (e.type) {
			case 0:
				checkIfTodosFit(CustomTodoOverlay.TodoType.CHAT, formattedText);
				break;
			case 2:
				checkIfTodosFit(CustomTodoOverlay.TodoType.ACTIONBAR, formattedText);
				break;
		}
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if (event.phase != TickEvent.Phase.START) return;
		if (Minecraft.getMinecraft().theWorld == null) return;
		if (Minecraft.getMinecraft().thePlayer == null) return;
		if (!NotEnoughUpdates.INSTANCE.isOnSkyblock()) return;

		checkIfTodosFit(CustomTodoOverlay.TodoType.SCOREBOARD, SidebarUtil.readSidebarLines());

		checkIfTodosFit(
			CustomTodoOverlay.TodoType.TAB,
			TabListUtils.getTabList().stream().map(EnumChatFormatting::getTextWithoutFormattingCodes).collect(
				Collectors.toList())
		);
	}

	public void checkIfTodosFit(CustomTodoOverlay.TodoType mode, String string) {
		List<String> loopInto = new ArrayList<>();
		loopInto.add(string);
		checkIfTodosFit(mode, loopInto);
	}

	public void checkIfTodosFit(CustomTodoOverlay.TodoType mode, List<String> loopInto) {
		Todo.getTodos()
				.stream()
				.filter(todo -> todo.getType() == mode)
				.collect(Collectors.toList())
				.forEach(todo -> {
					for (String line : loopInto) {
						checkIfTodoFits(todo, line);
					}
				});
	}

	public void checkIfTodoFits(Todo todo, String formattedText) {
		Pattern pattern = Pattern.compile(Pattern.quote(todo.getRegex()));
		Matcher matcher = pattern.matcher(formattedText);
		switch (todo.getMode()) {
			case EQUALS:
				if (matcher.matches()) {
					todo.execute();
				}
				break;
			case CONTAINS:
				if (matcher.find()) {
					todo.execute();
				}
				break;

			// i think?
			case STARTSWITH:
				if (matcher.lookingAt()) {
					todo.execute();
				}
				break;
		}
	}
}

