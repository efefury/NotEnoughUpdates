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

package io.github.moulberry.notenoughupdates.profileviewer.weight.lily;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.moulberry.notenoughupdates.profileviewer.weight.weight.Weight;
import io.github.moulberry.notenoughupdates.util.Constants;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import java.util.Set;

public class LilyWeight extends Weight {

	// the pair is: key = xp of skill, right = level

	public LilyWeight(JsonObject player) {
		super(new LilySlayerWeight(player), new LilySkillsWeight(player), new LilyDungeonsWeight(player));
	}

	@Override
	public LilySkillsWeight getSkillsWeight() {
		return (LilySkillsWeight) skillsWeight;
	}

	@Override
	public LilySlayerWeight getSlayerWeight() {
		return (LilySlayerWeight) slayerWeight;
	}

	@Override
	public LilyDungeonsWeight getDungeonsWeight() {
		return (LilyDungeonsWeight) dungeonsWeight;
	}

	@Override
	public LilyWeight calculateWeight(String exclude, List<Integer> slayer, Map<String, Pair<Integer, Integer>> skills, double cataXp, int cataLvl, double skillAvg) {
		exclude = exclude.toLowerCase();

		slayerWeight.getWeightStruct().reset();
		skillsWeight.getWeightStruct().reset();
		dungeonsWeight.getWeightStruct().reset();

		for (JsonElement jsonElement : Constants.WEIGHT.SLAYER_NAMES) {
			String slayerName = jsonElement.getAsString();
			if (!exclude.equals(slayerName)) {
				for (Integer integer : slayer) {
					slayerWeight.getSlayerWeight(slayerName, integer);
				}
			}
		}
		for (JsonElement element : Constants.WEIGHT.SKILL_NAMES) {
			String skillName = element.getAsString();
			if (!exclude.equals(skillName)){
				Set<Map.Entry<String, Pair<Integer, Integer>>> entries = skills.entrySet();
				for (Map.Entry<String, Pair<Integer, Integer>> stringIntegerEntry : entries) {
					Pair<Integer, Integer> value = stringIntegerEntry.getValue();
					System.out.println(skillName.toLowerCase());
					System.out.println(value.getValue());
					System.out.println(value.getKey());
					skillsWeight.getSkillsWeight(skillName.toLowerCase(), value.getValue(),  value.getKey(), skillAvg);
				}
			}
		}

		if (!exclude.equals("catacombs")) {
			dungeonsWeight.getDungeonWeight(cataXp, cataLvl);
		}
		getDungeonsWeight().getDungeonCompletionWeight("normal");
		getDungeonsWeight().getDungeonCompletionWeight("master");

		return this;
	}
}
