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

public class WeightStruct {

	private double base;
	private double overflow;

	public WeightStruct() {
		this(0, 0);
	}

	public WeightStruct(double base) {
		this(base, 0);
	}

	public WeightStruct(double base, double overflow) {
		this.base = base;
		this.overflow = overflow;
	}

	/**
	 * @return The weight struct being added, not this
	 **/
	public WeightStruct add(WeightStruct o) {
		this.base += o.base;
		this.overflow += o.overflow;
		return o;
	}

	public double getOverflow() {
		return overflow;
	}

	public double getBase() {
		return base;
	}

	public double getRaw() {
		return base + overflow;
	}

	public void reset() {
		this.base = 0;
		this.overflow = 0;
	}

	public String toString() {
		System.out.println(base + " + " + overflow)
	}
}
