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
import com.google.gson.JsonObject;
import io.github.moulberry.notenoughupdates.NotEnoughUpdates;
import io.github.moulberry.notenoughupdates.itemeditor.GuiElementTextField;
import io.github.moulberry.notenoughupdates.recipes.RecipeGenerator;
import io.github.moulberry.notenoughupdates.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CreateMenuOverlay {

	public final ResourceLocation pv_dropdown = new ResourceLocation("notenoughupdates:pv_dropdown.png");

	public final GuiElementTextField nameTextField = new GuiElementTextField("", GuiElementTextField.SCALE_TEXT);

	public final GuiElementTextField regexTextField = new GuiElementTextField("", GuiElementTextField.SCALE_TEXT);

	public final GuiElementTextField timeTextField = new GuiElementTextField("", GuiElementTextField.SCALE_TEXT);

	private CustomTodoOverlay.TodoType createdType = CustomTodoOverlay.TodoType.CHAT;
	private CustomTodoOverlay.TodoMode createMode = CustomTodoOverlay.TodoMode.EQUALS;
	private CustomTodoOverlay.DisplayTime displayTime = CustomTodoOverlay.DisplayTime.ALWAYS;

	private ItemStack iconItem;

	private Todo source;
	private boolean dropDownType = false;
	private boolean dropDownTime = false;

	private boolean dropDownMode = false;

	private final CustomTodoOverlay overlay;

	public CreateMenuOverlay(CustomTodoOverlay overlay) {
		this.overlay = overlay;

		nameTextField.setCustomBorderColour(Color.GREEN.getRGB());
		nameTextField.setSize(100, 20);

		timeTextField.setCustomBorderColour(Color.YELLOW.getRGB());
		timeTextField.setSize(80, 20);

		regexTextField.setCustomBorderColour(Color.CYAN.getRGB());
		regexTextField.setSize(180, 20);

	}

	public void setTodo(Todo todo) {
		regexTextField.setText(todo.getRegex());
		nameTextField.setText(todo.getName());
		timeTextField.setText(Utils.prettyTime(todo.getTime(), true));
		createMode = todo.getMode();
		createdType = todo.getType();
		displayTime = todo.getDisplayType();
		source = todo;
	}

	public void draw(int guiLeft, int guiTop) {
		String str;
		if (source != null) str = "§7Edit";
		else str = "§7Create";
		int stringWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(str);
		Utils.drawStringScaled(str, guiLeft + (431 / 2f) - stringWidth, guiTop + 11, true, 0, 2);

		Utils.drawStringCenteredScaled("§aName", (float) guiLeft + 75, guiTop + 55, true, 1);
		nameTextField.render(guiLeft + 25, guiTop + 60);

		Utils.drawStringCenteredScaled("§aTime", (float) guiLeft + 200, guiTop + 55, true, 1);
		timeTextField.render(guiLeft + 160, guiTop + 60);

		Utils.drawStringCenteredScaled("§7(ex.: 7d 05h 3m 2s)", (float) guiLeft + 210, guiTop + 87, true, 0.8f);
		timeTextField.render(guiLeft + 160, guiTop + 60);

		Utils.drawStringCenteredScaled("§aRegex (Text)", (float) guiLeft + 25 + 90, guiTop + 105, true, 1);
		regexTextField.render(guiLeft + 25, guiTop + 110);

		nameTextField.setText(nameTextField.getText().replace("&&", "§"));
		regexTextField.setText(regexTextField.getText().replace("&&", "§"));

		GlStateManager.color(1, 1, 1, 1);
		Minecraft.getMinecraft().getTextureManager().bindTexture(pv_dropdown);
		Utils.drawTexturedRect(guiLeft + 295, guiTop + 60, 100, 20, 0, 100 / 200f, 0, 20 / 185f, GL11.GL_NEAREST);
		Utils.drawStringCenteredScaled("§aInput Type", (float) guiLeft + 295 + 50, guiTop + 55, true, 1);

		GlStateManager.color(1, 1, 1, 1);

		Minecraft.getMinecraft().getTextureManager().bindTexture(pv_dropdown);
		Utils.drawTexturedRect(guiLeft + 265, guiTop + 110, 100, 20, 0, 100 / 200f, 0, 20 / 185f, GL11.GL_NEAREST);
		if (!dropDownType) {
			Utils.drawStringCenteredScaled("§aMode", (float) guiLeft + 265 + 50, guiTop + 105, true, 1);
		}

		GlStateManager.color(1, 1, 1, 1);
		Minecraft.getMinecraft().getTextureManager().bindTexture(pv_dropdown);
		Utils.drawTexturedRect(guiLeft + 34, guiTop + 160, 100, 20, 0, 100 / 200f, 0, 20 / 185f, GL11.GL_NEAREST);
		Utils.drawStringCenteredScaled("§aDisplay When", (float) guiLeft + 34 + 50, guiTop + 155, true, 1);

		if (createdType != null) {
			Utils.drawStringCenteredScaled(
				"§f" + StringUtils.capitalize(createdType.name().replace("_", " ").toLowerCase()),
				(float) guiLeft + 295 + 50,
				guiTop + 70,
				true,
				1
			);
		}

		if (displayTime != null) {
			Utils.drawStringCenteredScaled(
				"§f" + StringUtils.capitalize(displayTime.name().replace("_", " ").toLowerCase()),
				(float) guiLeft + 34 + 50,
				guiTop + 170,
				true,
				1
			);
		}

		if (createMode != null && !dropDownType) {
			Utils.drawStringCenteredScaled(
				"§f" + StringUtils.capitalize(createMode.name().replace("_", " ").toLowerCase()),
				(float) guiLeft + 265 + 50,
				guiTop + 120,
				true,
				1
			);
		}

		if (dropDownMode) {
			makeDropDownThing(265, 130, 80, guiLeft, guiTop);
			int i = 2;
			for (CustomTodoOverlay.TodoMode value : CustomTodoOverlay.TodoMode.values()) {
				String name = StringUtils.capitalize(value.name().replace("_", " ").toLowerCase());
				Utils.drawStringScaled("§7" + name, (float) guiLeft + 270, (guiTop + 95) + (i * 20), true, 0, 1.3f);
				i++;
			}
		}

		if (dropDownTime) {
			makeDropDownThing(34, 180, 100, guiLeft, guiTop);
			int i = 2;
			for (CustomTodoOverlay.DisplayTime value : CustomTodoOverlay.DisplayTime.values()) {
				String name = StringUtils.capitalize(value.name().replace("_", " ").toLowerCase());
				Utils.drawStringScaled("§7" + name, (float) guiLeft + 39, (guiTop + 145) + (i * 20), true, 0, 1.3f);
				i++;
			}
		}

		if (dropDownType) {
			makeDropDownThing(295, 80, 80, guiLeft, guiTop);
			int i = 2;
			for (CustomTodoOverlay.TodoType value : CustomTodoOverlay.TodoType.values()) {
				String name = StringUtils.capitalize(value.name().replace("_", " ").toLowerCase());
				Utils.drawStringScaled("§7" + name, (float) guiLeft + 300, (guiTop + 45) + (i * 20), true, 0, 1.3f);
				i++;
			}
		}

		if (nameTextField.getFocus() || regexTextField.getFocus() || timeTextField.getFocus()) {
			dropDownType = false;
			dropDownMode = false;
		}


		createSaveButton(guiLeft, guiTop);
	}

	public void save() throws IOException {
		String name = nameTextField.getText();
		String time = timeTextField.getText();
		String regex = regexTextField.getText();
		if (name.isEmpty()) return;
		if (time.isEmpty()) return;
		if (regex.isEmpty()) return;

		long ms = (RecipeGenerator.parseDuration(time.toLowerCase()) * 1000L);

		String textWithoutFormattingCodes = EnumChatFormatting.getTextWithoutFormattingCodes(name);

		if (Objects.equals(textWithoutFormattingCodes, name)) name = EnumChatFormatting.DARK_AQUA + name;

		String property =
			textWithoutFormattingCodes + "-" + createdType.name() + "-" + createMode.name();

		long lastFinished = 0;
		if (source != null && source.getRegex().equals(regex)) lastFinished = source.getLastFinished();
		Todo todo = new Todo(createdType, createMode, name, regex, property, ms, lastFinished, displayTime, iconItem);
		// EnumChatFormatting.getTextWithoutFormattingCodes(name) + "-" + createdType.name() + "-" + createMode.name()

		Gson gson = NotEnoughUpdates.INSTANCE.manager.gson;
		JsonObject json = gson.toJsonTree(todo).getAsJsonObject();
		overlay.inCreateMenu = false;
		if (source != null) {
			source.delete();
		}
		Todo.addTodoToList(todo);
		Todo.getJsonObject().add(
			property,
			json
		);
		CustomTodoOverlay.saveFile();
	}

	public void makeDropDownThing(int x, int y, int height, int guiLeft, int guiTop) {
		GlStateManager.color(1, 1, 1, 1);
		Minecraft.getMinecraft().getTextureManager().bindTexture(pv_dropdown);
		Utils.drawTexturedRect(guiLeft + x, guiTop + y, 100, 3, 100 / 200f, 1, 0, 3 / 185f, GL11.GL_NEAREST);
		Utils.drawTexturedRect(
			guiLeft + x,
			guiTop + y + height - 4,
			100,
			4,
			100 / 200f,
			1,
			181 / 185f,
			1,
			GL11.GL_NEAREST
		);
		Utils.drawTexturedRect(
			guiLeft + x,
			guiTop + y,
			100,
			height - 4,
			100 / 200f,
			1,
			(181 - height) / 185f,
			181 / 185f,
			GL11.GL_NEAREST
		);
	}

	private void createSaveButton(int guiLeft, int guiTop) {
		GlStateManager.color(1, 1, 1, 1);

		double stringWidth = (Minecraft.getMinecraft().fontRendererObj.getStringWidth("§aSave") * 1.4);
		Minecraft.getMinecraft().getTextureManager().bindTexture(overlay.create_button);
		Utils.drawTexturedRect(guiLeft + (431 / 2f) - (81 / 2f), guiTop + 202 - 20 - 10, 81, 20,
			0, 81 / 256f, 216 / 256f, 236 / 256f, GL11.GL_NEAREST
		);
		Utils.drawStringCenteredScaled(
			"§aSave",
			(float) (guiLeft + ((431 / 2f) - (81f / 2 / 2f) + (stringWidth / 2))),
			guiTop + 202 - 20,
			true,
			1.4f
		);
	}

	public void keyTyped(char typedChar, int keyCode) {
		if (nameTextField.getFocus()) nameTextField.keyTyped(typedChar, keyCode);
		if (timeTextField.getFocus()) timeTextField.keyTyped(typedChar, keyCode);
		if (regexTextField.getFocus()) regexTextField.keyTyped(typedChar, keyCode);

	}

	public void mouseClicked(int mouseX, int mouseY, int mouseButton, int guiLeft, int guiTop) {
		if (mouseX > (guiLeft + (431 / 2) - (81 / 2)) &&
			mouseX < (guiLeft + (431 / 2) - (81 / 2)) + 81) {
			if (mouseY > (guiTop + 202 - 20 - 10) && mouseY < (guiTop + 202 - 20 - 10) + 20) {
				try {
					save();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}

		// in text field
		createTextFields(mouseX, mouseY, mouseButton, guiLeft, guiTop);

		// display time

		// guiLeft + 34, guiTop + 160
		if ((mouseX >= guiLeft + 34 && mouseX <= guiLeft + 134) &&
			(mouseY >= guiTop + 160 && mouseY <= guiTop + 175)) {
			dropDownTime = !dropDownTime;
			dropDownMode = false;
			dropDownType = false;
		}

		// if clicked the mode button
		if (dropDownTime) {
			int i = 2;
			for (CustomTodoOverlay.DisplayTime value : CustomTodoOverlay.DisplayTime.values()) {
				String name = StringUtils.capitalize(value.name().replace("_", " ").toLowerCase());
				double stringWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(name) * 1.3;
				// 160
				float x = guiLeft + 39;
				float y = (guiTop + 145) + (i * 20);

				if ((mouseX >= x && mouseX <= x + stringWidth) && (mouseY >= y && mouseY < y + 20)) {
					displayTime = value;
					dropDownTime = false;
				}
				i++;
			}
		}

		// clicking on the input type button
		if ((mouseX >= guiLeft + 295 && mouseX <= guiLeft + 395) &&
			(mouseY >= guiTop + 110 && mouseY <= guiTop + 125)) {
			if (dropDownType) return;
			dropDownMode = !dropDownMode;
		}

		// if clicked the mode button
		if (dropDownMode) {
			int i = 2;
			for (CustomTodoOverlay.TodoMode value : CustomTodoOverlay.TodoMode.values()) {
				String name = StringUtils.capitalize(value.name().replace("_", " ").toLowerCase());
				double stringWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(name) * 1.3;
				float x = guiLeft + 270;
				float y = (guiTop + 95) + (i * 20);

				if ((mouseX >= x && mouseX <= x + stringWidth) && (mouseY >= y && mouseY < y + 20)) {
					createMode = value;
					dropDownMode = false;
				}
				i++;
			}
		}

		if ((mouseX >= guiLeft + 265 && mouseX <= guiLeft + 365) &&
			(mouseY >= guiTop + 60 && mouseY <= guiTop + 75)) {
			dropDownType = !dropDownType;
			dropDownMode = false;

		}

		if (dropDownType) {
			int i = 2;
			for (CustomTodoOverlay.TodoType value : CustomTodoOverlay.TodoType.values()) {
				String name = StringUtils.capitalize(value.name().replace("_", " ").toLowerCase());
				double stringWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(name) * 1.3;
				float x = guiLeft + 295;
				float y = (guiTop + 45) + (i * 20);
				if ((mouseX >= x + 10 && mouseX <= x + 10 + stringWidth) && (mouseY >= y && mouseY < y + 20)) {
					createdType = value;
					dropDownType = false;
				}
				i++;
			}
		}

	}

	public void createTextFields(int mouseX, int mouseY, int mouseButton, int guiLeft, int guiTop) {
		if ((mouseX >= guiLeft + 25 && mouseX <= guiLeft + 25 + 100) &&
			(mouseY >= guiTop + 60 + 5 && mouseY <= guiTop + 60 + 20)) {
			nameTextField.mouseClicked(mouseX, mouseY, mouseButton);
		} else {
			nameTextField.otherComponentClick();
		}

		if ((mouseX >= guiLeft + 25 && mouseX <= guiLeft + 25 + 180) &&
			(mouseY >= guiTop + 110 + 5 && mouseY <= guiTop + 110 + 20)) {
			regexTextField.mouseClicked(mouseX, mouseY, mouseButton);
		} else {
			regexTextField.otherComponentClick();
		}

		if ((mouseX >= guiLeft + 160 && mouseX <= guiLeft + 160 + 80) &&
			(mouseY >= guiTop + 60 + 5 && mouseY <= guiTop + 60 + 20)) {
			timeTextField.mouseClicked(mouseX, mouseY, mouseButton);
		} else {
			timeTextField.otherComponentClick();
		}
	}

	public void reset() {
		regexTextField.setText("");
		nameTextField.setText("");
		timeTextField.setText("");
		createMode = CustomTodoOverlay.TodoMode.EQUALS;
		createdType = CustomTodoOverlay.TodoType.CHAT;
		displayTime = CustomTodoOverlay.DisplayTime.ALWAYS;
		source = null;
	}
}
