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
import io.github.moulberry.notenoughupdates.profileviewer.weight.struct.SkillsStruct;
import io.github.moulberry.notenoughupdates.profileviewer.weight.struct.WeightStruct;
import io.github.moulberry.notenoughupdates.profileviewer.weight.weight.DungeonsWeight;
import io.github.moulberry.notenoughupdates.profileviewer.weight.weight.Weight;
import io.github.moulberry.notenoughupdates.util.Constants;
import io.github.moulberry.notenoughupdates.util.Utils;

import java.util.Map;

public class LilyDungeonsWeight extends DungeonsWeight {

	public LilyDungeonsWeight(JsonObject player) {
		super(player);
	}

	@Override
	public WeightStruct getWeightStruct() {
		return super.getWeightStruct();
	}

	@Override
	public WeightStruct getDungeonWeight(double cataXP, int level) {

		double extra = 0;
		double n = 0;
		if (cataXP < 569809640) {
			n = 0.2 * Math.pow(level / 50, 1.538679118869934);
		} else {
			extra = 500.0 * Math.pow((cataXP - Constants.WEIGHT.CATACOMBS_LEVEL_50_XP) / 142452410.0, 1.0 / 1.781925776625157);
		}

		if (level != 0) {
			if (cataXP < 569809640) {
				return weightStruct.add(
					new WeightStruct(1.2733079672009226 * ((Math.pow(1.18340401286164044, (level + 1)) - 1.05994990217254) *
						(1 + n)))
				);
			} else {
				return weightStruct.add(new WeightStruct((4100 + extra) * 2));
			}
		} else {
			return new WeightStruct();
		}
	}

	public WeightStruct getDungeonCompletionWeight(String cataMode) {
		double max1000 = 0;
		double mMax1000 = 0;

		for (Map.Entry<String, JsonElement> stringJsonElementEntry : Constants.WEIGHT.DUNGEON_COMPLETION_WORTH.entrySet()) {
			double value = stringJsonElementEntry.getValue().getAsDouble();
			if (stringJsonElementEntry.getKey().startsWith("f")) {
				max1000 += value;
			} else {
				mMax1000 += value;
			}
		}
		max1000 *= 1000;
		mMax1000 *= 1000;
		double upperBound = 1500;
		if (cataMode.equals("normal")) {
			if (Utils.getElement(player, "dungeons.dungeon_types.catacombs.tier_completions") == null) {
				return new WeightStruct();
			}

			double score = 0;
			for (Map.Entry<String, JsonElement> normalFloor : Utils.getElement(player, "dungeons.dungeon_types.catacombs.tier_completions").getAsJsonObject().entrySet()) {
				int amount = normalFloor.getValue().getAsInt();
				double excess = 0;
				if (amount > 1000) {
					excess = amount - 1000;
					amount = 1000;
				}

				double floorScore = amount * Constants.WEIGHT.DUNGEON_COMPLETION_WORTH.get("f" + normalFloor.getKey()).getAsDouble();
				if (excess > 0) floorScore *= Math.log(excess / 1000 + 1) / Math.log(7.5) + 1;
				score += floorScore;
			}

			return weightStruct.add(new WeightStruct(score / max1000 * upperBound * 2));
		} else {
			if (Utils.getElement(player, "dungeons.dungeon_types.master_catacombs.tier_completions") == null) {
				return new WeightStruct();
			}

			for (Map.Entry<String, JsonElement> masterFloor : Utils.getElement(
				player,
				"dungeons.dungeon_types.master_catacombs.tier_completions"
			)
				.getAsJsonObject()
				.entrySet()) {
				if (Utils.getElement(Constants.WEIGHT.DUNGEON_COMPLETION_BUFFS, masterFloor.getKey()) != null) {
					int amount = masterFloor.getValue().getAsInt();
					double threshold = 20;
					if (amount >= threshold) {
						upperBound += Utils.getElement(Constants.WEIGHT.DUNGEON_COMPLETION_BUFFS, masterFloor.getKey()).getAsInt();
					} else {
						upperBound +=
							Utils.getElement(Constants.WEIGHT.DUNGEON_COMPLETION_BUFFS, masterFloor.getKey()).getAsInt() *
								Math.pow((amount / threshold), 1.840896416);
					}
				}
			}

			double masterScore = 0;
			for (Map.Entry<String, JsonElement> masterFloor : Utils.getElement(player, "dungeons.dungeon_types.master_catacombs.tier_completions").getAsJsonObject().entrySet()) {
				int amount = masterFloor.getValue().getAsInt();
				double excess = 0;
				if (amount > 1000) {
					excess = amount - 1000;
					amount = 1000;
				}

				double floorScore = amount * Constants.WEIGHT.DUNGEON_COMPLETION_WORTH
					.get("m" + masterFloor.getKey())
					.getAsDouble();
				if (excess > 0) {
					floorScore *= (Math.log((excess / 1000) + 1) / Math.log(6)) + 1;
				}
				masterScore += floorScore;
			}

			return weightStruct.add(new WeightStruct((masterScore / mMax1000) * upperBound * 2));
		}
	}
}
