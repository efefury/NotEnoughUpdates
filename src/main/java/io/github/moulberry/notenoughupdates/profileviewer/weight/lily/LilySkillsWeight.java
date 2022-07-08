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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.moulberry.notenoughupdates.profileviewer.weight.struct.WeightStruct;
import io.github.moulberry.notenoughupdates.profileviewer.weight.weight.SkillsWeight;
import io.github.moulberry.notenoughupdates.util.Constants;
import io.github.moulberry.notenoughupdates.util.Utils;

public class LilySkillsWeight extends SkillsWeight {


	public LilySkillsWeight(JsonObject player) {
		super(player);
	}


	@Override
	public WeightStruct getSkillsWeight(String skillName, int currentLevel, int currentXp, double skillAverage) {
		JsonArray srwTable = Utils.getElement(Constants.WEIGHT.LILY_SKILL_RATIO_WEIGHT, skillName).getAsJsonArray();
		double base =
			(
				(12 * Math.pow((skillAverage / 60), 2.44780217148309)) *
					srwTable.get(currentLevel).getAsDouble() *
					srwTable.get(srwTable.size() - 1).getAsDouble()
			) +
				(srwTable.get(srwTable.size() - 1).getAsDouble() * Math.pow(currentLevel / 60.0, Math.pow(2, 0.5)));
		base *= 1.8162162162162162;
		double overflow = 0;
		if (currentXp > Constants.WEIGHT.SKILLS_LEVEL_60_XP) {
			double factor = Utils.getElement(Constants.WEIGHT.SKILL_FACTORS, skillName).getAsDouble();
			double effectiveOver = effectiveXP(currentXp - Constants.WEIGHT.SKILLS_LEVEL_60_XP, factor);
			double t = (effectiveOver / Constants.WEIGHT.SKILLS_LEVEL_60_XP) * (Utils.getElement(Constants.WEIGHT.SKILL_OVERFLOW_MULTIPLIERS, skillName).getAsDouble());
			if (t > 0) {
				overflow += 1.8162162162162162 * t;
			}
		}

		return weightStruct.add(new WeightStruct(base, overflow));
	}

	private double effectiveXP(double xp, double factor) {
		if (xp < Constants.WEIGHT.SKILLS_LEVEL_60_XP) {
			return xp;
		} else {
			double remainingXP = xp;
			double z = 0;
			for (int i = 0; i <= (int) (xp / Constants.WEIGHT.SKILLS_LEVEL_60_XP); i++) {
				if (remainingXP >= Constants.WEIGHT.SKILLS_LEVEL_60_XP) {
					remainingXP -= Constants.WEIGHT.SKILLS_LEVEL_60_XP;
					z += Math.pow(factor, i);
				}
			}
			return z * Constants.WEIGHT.SKILLS_LEVEL_60_XP;
		}
	}
}
