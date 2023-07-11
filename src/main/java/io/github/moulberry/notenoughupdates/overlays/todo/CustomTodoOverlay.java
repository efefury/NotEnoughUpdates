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

package io.github.moulberry.notenoughupdates.overlays.todo;

import com.google.gson.Gson;
import io.github.moulberry.notenoughupdates.NotEnoughUpdates;
import io.github.moulberry.notenoughupdates.core.config.gui.GuiOptionEditorDraggableList;
import io.github.moulberry.notenoughupdates.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomTodoOverlay extends GuiScreen {

	public final ResourceLocation pv_bg = new ResourceLocation("notenoughupdates:pv_bg.png");
	public final ResourceLocation create_button = new ResourceLocation("notenoughupdates:pv_elements.png");
	private static final Gson gson = NotEnoughUpdates.INSTANCE.manager.gson;
	public boolean inCreateMenu = false;
	private final CreateMenuOverlay createMenuOverlay;
	public CustomTodoOverlay() {
		createMenuOverlay = new CreateMenuOverlay(this);
	}

	private int guiLeft, guiTop = 0;

	public static void saveFile() {
		File file = new File("config/notenoughupdates", "todos.json");

		try (
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				java.nio.file.Files.newOutputStream(file.toPath()),
				StandardCharsets.UTF_8
			))
		) {
			writer.write(gson.toJson(Todo.getJsonObject()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public enum DisplayTime {
		READY(100),
		VERY_SOON(50),
		SOON(75),
		KINDA_SOON(90),
		ALWAYS(0);

		final long time;

		DisplayTime(long time) {this.time = time;}

		public long getPercentage() {
			return time;
		}
	}


	public enum TodoType {
		CHAT, ACTIONBAR, TAB, SCOREBOARD
	}

	public enum TodoMode {
		EQUALS, CONTAINS, STARTSWITH, ENDSWITH
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		int sizeX = 431;
		int sizeY = 202;
		guiLeft = (this.width - sizeX) / 2;
		guiTop = (this.height - sizeY) / 2;

		Minecraft.getMinecraft().getTextureManager().bindTexture(pv_bg);
		fancyGlThings(guiLeft, guiTop, sizeX, sizeY);

		GlStateManager.color(1, 1, 1, 1);

		createButton();
		if (!inCreateMenu) {
			String str = "§7Custom Todo Menu";
			int stringWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(str);
			Utils.drawStringScaled(str, guiLeft + ((431f / 2) - stringWidth), guiTop + 11, true, 0, 2);
		} else {
			createMenuOverlay.draw(guiLeft, guiTop);
		}

		if (!inCreateMenu) {
			int i = 0;
			for (Todo todo : Todo.getTodos()) {
				i++;

				String name = todo.getName();
				if (Objects.equals(EnumChatFormatting.getTextWithoutFormattingCodes(name), name)) {
					name = "§7" + name;
				}
				int x = guiLeft + 20;
				int y = guiTop + 30 + (25 * i);
				double fontHeight = Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
				Utils.drawStringScaled(
					name,
					x,
					y,
					true,
					0,
					1.5f
				);

				drawHorizontalLine(
					guiLeft + 17,
					(int) (guiLeft + (Minecraft.getMinecraft().fontRendererObj.getStringWidth(name) * 1.5) + 25),
					(int) (y + (fontHeight * 1.5) + 3),
					Color.GRAY.getRGB()
				);

				Minecraft.getMinecraft().getTextureManager().bindTexture(GuiOptionEditorDraggableList.DELETE);
				fancyGlThings(guiLeft + 400, y, 11, 14);

				Utils.drawStringF("§fEdit", guiLeft + 357 + 9, y + 5, true, 0);

				int topRight = (guiLeft + 357 + Minecraft.getMinecraft().fontRendererObj.getStringWidth("§fEdit")) + 15;
				int topLeft = guiLeft + 357;
				drawBox(
					topLeft,
					topRight,
					y,
					(int) (y + fontHeight + 8),
					Color.BLACK
				);

			}
		}
	}

	public void drawBox(int topLeft, int topRight, int topY, int bottomY, Color color) {
		drawHorizontalLine(topLeft, topRight, topY, color.getRGB());
		drawHorizontalLine(topLeft, topRight, bottomY, color.getRGB());
		drawVerticalLine(topLeft, topY, bottomY, color.getRGB());
		drawVerticalLine(topRight, topY, bottomY, color.getRGB());
	}

	public void fancyGlThings(int x, int y, int width, int height) {
		GlStateManager.disableLighting();
		GlStateManager.enableDepth();
		GlStateManager.enableBlend();
		GL14.glBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableAlpha();
		GlStateManager.alphaFunc(516, 0.1F);
		GlStateManager.color(1, 1, 1, 1);
		Utils.drawTexturedRect(x, y, width, height, GL11.GL_NEAREST);
	}

	public void createButton() {
		Minecraft.getMinecraft().getTextureManager().bindTexture(create_button);
		Utils.drawTexturedRect(guiLeft + 13, guiTop + 11, 81, 20,
			0, 81 / 256f, 216 / 256f, 236 / 256f, GL11.GL_NEAREST
		);
		String str = inCreateMenu ? "§aGo Back" : "§aCreate";
		double stringWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(str) * 1.4;

		Utils.drawStringScaled(
			str,
			(float) (guiLeft + 13 + (81f / 2) - (stringWidth / 2)),
			guiTop + 16,
			true,
			0,
			1.4f
		);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		if (inCreateMenu) createMenuOverlay.mouseClicked(mouseX, mouseY, mouseButton, guiLeft, guiTop);
		if ((mouseX >= guiLeft + 13 && mouseX <= guiLeft + 13 + 88) &&
			(mouseY >= guiTop + 11 && mouseY <= guiTop + 11 + 20)) {
			boolean future = !inCreateMenu;
			if (future) {
				createMenuOverlay.reset();
			}
			inCreateMenu = future;
		}

		if (inCreateMenu) return;
		int i = 0;

		List<Todo> copy = new ArrayList<>(Todo.getTodos());
		boolean edited = false;
		for (Todo todo : Todo.getTodos()) {
			i++;

			int topRight = (guiLeft + 357 + Minecraft.getMinecraft().fontRendererObj.getStringWidth("§fEdit")) + 15;
			int topLeft = guiLeft + 357;
			int y = guiTop + 30 + (25 * i);
			if ((mouseX >= topLeft && mouseX <= topRight) &&
				(mouseY >= y && mouseY <= y + 17)) {
				createMenuOverlay.setTodo(todo);
				inCreateMenu = true;
			}

			if ((mouseX >= guiLeft + 395 && mouseX <= guiLeft + 395 + 25) &&
				(mouseY >= y && mouseY <= y + 23)) {
				todo.delete(copy);
				edited = true;
			}
		}

		if (edited) saveFile();
		Todo.Companion.setTodos(copy);
		Todo.setTodos(copy);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		createMenuOverlay.keyTyped(typedChar, keyCode);
	}
}
