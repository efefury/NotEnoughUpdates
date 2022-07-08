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

import io.github.moulberry.notenoughupdates.profileviewer.weight.struct.WeightStruct;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;

public abstract class Weight {

	protected final SlayerWeight slayerWeight;
	protected final SkillsWeight skillsWeight;
	protected final DungeonsWeight dungeonsWeight;

	public Weight(SlayerWeight slayerWeight, SkillsWeight skillsWeight, DungeonsWeight dungeonsWeight) {
		this.slayerWeight = slayerWeight;
		this.skillsWeight = skillsWeight;
		this.dungeonsWeight = dungeonsWeight;

	}

	public SkillsWeight getSkillsWeight() {
		return skillsWeight;
	}

	public SlayerWeight getSlayerWeight() {
		return slayerWeight;
	}

	public DungeonsWeight getDungeonsWeight() {
		return dungeonsWeight;
	}

	public WeightStruct getTotalWeight() {
		WeightStruct w = new WeightStruct();
		w.add(slayerWeight.getWeightStruct());
		w.add(skillsWeight.getWeightStruct());
		w.add(dungeonsWeight.getWeightStruct());
		return w;
	}

	public abstract Weight calculateWeight(String exclude, List<Integer> slayer, Map<String, Pair<Integer, Integer>> skills, double cataXp, int cataLvl, double skillAvg);
}
