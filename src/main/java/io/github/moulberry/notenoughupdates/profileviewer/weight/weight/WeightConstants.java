/*
 * Copyright (C) 2022 NotEnoughUpdates contributors
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

package io.github.moulberry.notenoughupdates.profileviewer.weight.weight;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.moulberry.notenoughupdates.events.RepositoryReloadEvent;
import io.github.moulberry.notenoughupdates.util.Utils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WeightConstants {

	public final long CATACOMBS_LEVEL_50_XP;
	public final long SKILLS_LEVEL_60_XP;
	public final long SKILLS_LEVEL_50_XP;

	public final JsonObject jsonObject;
	public final JsonObject lilyObject;
	public final JsonObject weightObject;

	public final JsonObject LILY_SKILL_RATIO_WEIGHT;
	public final JsonArray SKILL_NAMES;
	public final JsonArray SLAYER_NAMES;
	public final JsonObject SKILL_FACTORS;
	public final JsonObject SLAYER_DEPRECATION_SCALING;
	public final JsonObject SKILL_OVERFLOW_MULTIPLIERS;
	public final JsonObject DUNGEON_COMPLETION_WORTH;
	public final JsonObject DUNGEON_COMPLETION_BUFFS;

	public final double LILY_OVERALL_SKILL_MULTIPLIER;

	public WeightConstants(JsonObject jsonObject) {
		this.jsonObject = jsonObject;
		this.lilyObject = jsonObject.get("lily_weight").getAsJsonObject();
		this.weightObject = jsonObject.get("weight").getAsJsonObject();

		LILY_SKILL_RATIO_WEIGHT = lilyObject.getAsJsonObject("skillRatioWeight");
		LILY_OVERALL_SKILL_MULTIPLIER = lilyObject.get("skillOverall").getAsDouble();
		SLAYER_DEPRECATION_SCALING = lilyObject.getAsJsonObject("slayerDeprecationScaling");
		SKILL_FACTORS = lilyObject.getAsJsonObject("skillFactors");
		SKILL_OVERFLOW_MULTIPLIERS = lilyObject.getAsJsonObject("skillOverflowMultipliers");
		DUNGEON_COMPLETION_WORTH = lilyObject.getAsJsonObject("dungeonCompletionWorth");
		DUNGEON_COMPLETION_BUFFS = lilyObject.getAsJsonObject("dungeonCompletionBuffs");

		SKILLS_LEVEL_50_XP = weightObject.get("skillMaxXP50").getAsInt();
		SKILLS_LEVEL_60_XP = weightObject.get("skillMaxXP60").getAsInt();
		CATACOMBS_LEVEL_50_XP = weightObject.get("dungeonMaxXP").getAsInt();

		SKILL_NAMES = weightObject.getAsJsonArray("skillNames");
		SLAYER_NAMES = weightObject.getAsJsonArray("slayerNames");
	}
}
