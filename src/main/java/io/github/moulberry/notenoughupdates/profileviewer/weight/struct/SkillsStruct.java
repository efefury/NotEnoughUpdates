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

package io.github.moulberry.notenoughupdates.profileviewer.weight.struct;

public class SkillsStruct {

private final	String name;
	private final	int currentLevel;
	private final	int maxLevel;
	private final	long totalExp;
	private final	long expCurrent;
	private final	long expForNext;
	private final double progressToNext;

	public SkillsStruct(String name, int currentLevel, int maxLevel, long totalExp, long expCurrent, long expForNext, double progressToNext) {
		this.name = name;
		this.currentLevel = currentLevel;
		this.maxLevel = maxLevel;
		this.totalExp = totalExp;
		this.expCurrent = expCurrent;
		this.expForNext = expForNext;
		this.progressToNext = progressToNext;
	}

	public boolean isMaxed() {
			return currentLevel == maxLevel;
		}

		public double getProgressLevel() {
			return currentLevel + progressToNext;
		}

	public long getExpCurrent() {
		return expCurrent;
	}

	public long getTotalExp() {
		return totalExp;
	}

	public int getCurrentLevel() {
		return currentLevel;
	}

	public long getExpForNext() {
		return expForNext;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public double getProgressToNext() {
		return progressToNext;
	}

	public String getName() {
		return name;
	}
}
